import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class TP02 {
    private static InputReader in;
    static PrintWriter out;
    static LinkedList listMesin = new LinkedList();
    static int X;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(); // banyaknya mesin yang tersedia
        for (int i = 1; i <= N; i++) {
            int M = in.nextInt(); // banyaknya skor awal Mi pada mesin dengan id i
            listMesin.add(M);

            Node currMesin = listMesin.getLast();
            currMesin.id = i;

            for (int j = 1; j <= M; j++) {
                int Z = in.nextInt(); // skor awal Mi pada mesin dengan id i
                currMesin.scores.root = currMesin.scores.insertNode(currMesin.scores.root, Z);
                // currMesin.scores.getLast().setId(j);
                // currMesin.scores.totalScore += Z;
            }
        }

        // listMesin.display();

        // set player to the first game machine
        listMesin.playerPtr = listMesin.getFirst();

        int Q = in.nextInt(); // banyaknya query
        for (int i = 0; i < Q; i++) {
            String query = in.next(); // query
            // listMesin.playerPtr.scores.preOrder(out, listMesin.playerPtr.scores.root);

            if (query.equals("MAIN")) {
                int Y = in.nextInt();
                out.println(queryMain(Y));
            } else if (query.equals("GERAK")) {
                String arah = in.next();
                out.println(queryGerak(arah));
            } else if (query.equals("HAPUS")) {
                int X = in.nextInt();
                out.println(queryHapus(X));
            } else if (query.equals("LIHAT")) {
                int L = in.nextInt();
                int H = in.nextInt();
                out.println(queryLihat(L, H));
            } else if (query.equals("EVALUASI")) {
                out.println(queryEvaluasi());
            }
        }

        out.close();
    }

    public static int queryMain(int Y) { // Y = skor pemain
        AVLTree currScores = listMesin.playerPtr.scores;
        currScores.root = currScores.insertNode(currScores.root, Y);
        listMesin.playerPtr.data += 1;

        // out.println("currScores size: " + currScores.size);
        // out.println("currScores rank: " + currScores.countGreater(currScores.root,
        // Y));
        // currScores.preOrder(out, currScores.root);
        return currScores.countGreater(currScores.root, Y) + 1;
        // urutan posisi Y pada barisan skor dr mesin permainan yang dimainin
    }

    public static int queryGerak(String arah) {
        if (arah.equals("KANAN")) {
            listMesin.playerPtr = listMesin.playerPtr.next;
            return listMesin.playerPtr.id;
        } else {
            listMesin.playerPtr = listMesin.playerPtr.prev;
            return listMesin.playerPtr.id;
        }
        // nomor id mesin tempat pemain berada setelah gerak
    }

    public static long queryHapus(int X) { // X = banyaknya skor teratas yang curang
        TP02.X = X;
        Node currMesin = listMesin.playerPtr;
        long sumSkor = 0;

        if (currMesin.scores.size <= X) {
            // reset mesin's scores
            sumSkor = currMesin.scores.sum;
            currMesin.scores.root = null;
            currMesin.scores.size = 0;
            currMesin.scores.sum = 0;
            currMesin.data = 0;

            listMesin.playerPtr = currMesin.next;

            // move currMesin to the last position
            if (currMesin != listMesin.getLast()) {
                if (currMesin == listMesin.start) {
                    if (listMesin.size == 1) {
                        listMesin.start = null;
                        listMesin.end = null;
                    } else {
                        // move start and end pointer to the next node of listMesin
                        listMesin.start = listMesin.start.next;
                        listMesin.start.prev = listMesin.end;
                        listMesin.end.next = listMesin.start;
                    }
                } else {
                    // remove currMesin from listMesin

                    Node p = currMesin.prev;
                    Node n = currMesin.next;

                    p.next = n;
                    n.prev = p;
                }
                // add currMesin to the last position of listMesin
                currMesin.prev = listMesin.end;
                listMesin.end.next = currMesin;
                listMesin.start.prev = currMesin;
                currMesin.next = listMesin.start;
                listMesin.end = currMesin;
            }
        } else {
            long sumBeforeDel = currMesin.scores.sum;
            // out.println("Before Delete");
            // currMesin.scores.preOrder(out, currMesin.scores.root);
            // out.println("sum " + currMesin.scores.sum);
            // for (int i = 0; i < X; i++) {
            // // TODO: delete first X node from AVLTree
            // currMesin.scores.root =
            // currMesin.scores.deleteLargest(currMesin.scores.root); // remove largest
            // element
            // }

            while (TP02.X > 0) {
                currMesin.scores.root = currMesin.scores.deleteLargest(currMesin.scores.root);
            }

            sumSkor = sumBeforeDel - currMesin.scores.sum;
            currMesin.data = currMesin.scores.size;
            // out.println("After Delete");
            // currMesin.scores.preOrder(out, currMesin.scores.root);
            // out.println("sum " + currMesin.scores.sum);
            // out.println("sumSkor: " + sumSkor + ", currMesin.data: "
            // + currMesin.data + ", currMesin.scores.size: " + currMesin.scores.size);
        }

        // listMesin.display();
        return sumSkor; // jumlah seluruh skor yang dihapus
    }

    public static int queryLihat(int L, int H) { // L, H = range yang ingin dilihat
        Node currMesin = listMesin.playerPtr;
        // currMesin.scores.preOrder(out, currMesin.scores.root);

        if (L == H) {
            AVLNode searchedNode = currMesin.scores.searchNode(currMesin.scores.root, H);
            return searchedNode != null ? searchedNode.entries.size() : 0;
        }

        int lRank = currMesin.scores.countGreaterEqual(currMesin.scores.root, L);
        // out.println("lRank: " + lRank);
        int hRank = currMesin.scores.countGreater(currMesin.scores.root, H);
        // out.println("hRank: " + hRank);

        return lRank - hRank;
        // banyaknya skor yang nilainya di range L - H (inklusif)
    }

    public static int queryEvaluasi() {
        // out.println(listMesin.playerPtr.data + ", id: " + listMesin.playerPtr.id);
        // listMesin.display();
        int playerPtrIdx = listMesin.sort();
        // listMesin.display();
        return playerPtrIdx; // urutan posisi baru mesin permainan yang berada di depan pemain
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }
}

// doubly circular linked list
// referensi:
// https://www.sanfoundry.com/java-program-implement-circular-doubly-linked-list/
class Node {
    long data;
    int id;
    Node next, prev;
    AVLTree scores = new AVLTree();

    /* Constructor */
    public Node() {
        next = null;
        prev = null;
        data = 0;
    }

    /* Constructor */
    public Node(long d, Node n, Node p) {
        data = d;
        next = n;
        prev = p;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return (this.id == node.id);
        } else {
            return false;
        }
    }
}

/* Class linkedList */
class LinkedList {
    Node start, end, playerPtr;
    int size;

    /* Constructor */
    public LinkedList() {
        start = null;
        end = null;
        size = 0;
    }

    /* Function to check if list is empty */
    public boolean isEmpty() {
        return start == null;
    }

    public Node getFirst() {
        return start;
    }

    public Node getLast() {
        return end;
    }

    /* Function to insert element at begining */
    public void addFirst(long val) {
        Node nptr = new Node(val, null, null);
        if (start == null) {
            nptr.next = nptr;
            nptr.prev = nptr;
            start = nptr;
            end = start;
        } else {
            nptr.prev = end;
            end.next = nptr;
            start.prev = nptr;
            nptr.next = start;
            start = nptr;
        }
        size++;
    }

    /* Function to insert element at end */
    public void add(long val) {
        Node nptr = new Node(val, null, null);
        if (start == null) {
            nptr.next = nptr;
            nptr.prev = nptr;
            start = nptr;
            end = start;
        } else {
            nptr.prev = end;
            end.next = nptr;
            start.prev = nptr;
            nptr.next = start;
            end = nptr;
        }
        size++;
    }

    /* Function to insert element at position */
    public void addAt(long val, int pos) {
        Node nptr = new Node(val, null, null);
        if (pos == 1) {
            addFirst(val);
            return;
        }
        Node ptr = start;
        for (int i = 2; i <= size; i++) {
            if (i == pos) {
                Node tmp = ptr.next;
                ptr.next = nptr;
                nptr.prev = ptr;
                nptr.next = tmp;
                tmp.prev = nptr;
            }
            ptr = ptr.next;
        }
        size++;
    }

    /* Function to delete node at position */
    public Node removeAt(int pos) {
        if (pos == 1) {
            if (size == 1) {
                start = null;
                end = null;
                size = 0;
                return start;
            }
            start = start.next;
            start.prev = end;
            end.next = start;
            size--;
            return start;
        }
        if (pos == size) {
            end = end.prev;
            end.next = start;
            start.prev = end;
            size--;
        }
        Node ptr = start.next;
        for (int i = 2; i <= size; i++) {
            if (i == pos) {
                Node p = ptr.prev;
                Node n = ptr.next;

                p.next = n;
                n.prev = p;
                size--;
                return ptr;
            }
            ptr = ptr.next;
        }
        return null;
    }

    public Node remove(Node node) {
        if (node == start) {
            if (size == 1) {
                start = null;
                end = null;
                size = 0;
                return start;
            }
            start = start.next;
            start.prev = end;
            end.next = start;
            size--;
            return start;
        }
        if (node == end) {
            end = end.prev;
            end.next = start;
            start.prev = end;
            size--;
        }
        Node ptr = start.next;
        for (int i = 2; i <= size; i++) {
            if (ptr == node) {
                Node p = ptr.prev;
                Node n = ptr.next;

                p.next = n;
                n.prev = p;
                size--;
                return ptr;
            }
            ptr = ptr.next;
        }
        return null;
    }

    // move node to tail
    public void moveToLast(Node node) {
        if (node == end) {
            return;
        }
        if (node == start) {
            start = start.next;
            end = end.next;
            return;
        }
        Node ptr = start.next;
        for (int i = 2; i <= size; i++) {
            if (ptr == node) {
                Node p = ptr.prev;
                Node n = ptr.next;

                p.next = n;
                n.prev = p;

                end.next = ptr;
                ptr.prev = end;
                ptr.next = start;
                start.prev = ptr;
                end = ptr;
                return;
            }
            ptr = ptr.next;
        }
    }

    public int indexOf(int val) {
        Node ptr = start;
        for (int i = 1; i <= size; i++) {
            if (ptr.data == val) {
                return i;
            }
            ptr = ptr.next;
        }
        return -1;
    }

    // sort Node's data and id in descending order using merge sort
    public int sort() {
        // transform circular linked list to non-circular linked list
        end.next = null;
        start.prev = null;
        start = mergeSort(start);

        // transform it back to circular linked list
        Node ptr = start;
        int playerPtrIdx = 1;
        boolean isCountingIdx = true;
        while (ptr.next != null) {
            if (isCountingIdx && ptr != playerPtr)
                playerPtrIdx++;
            else
                isCountingIdx = false;

            ptr = ptr.next;
        }
        end = ptr;
        end.next = start;
        start.prev = end;

        return playerPtrIdx;
    }

    // split, mergeSort, merge methods reference:
    // https://www.geeksforgeeks.org/merge-sort-for-doubly-linked-list/
    public Node split(Node head) {
        Node fast = head, slow = head;
        while (fast.next != null && fast.next.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        Node temp = slow.next;
        slow.next = null;
        return temp;
    }

    public Node mergeSort(Node node) {
        if (node == null || node.next == null) {
            return node;
        }
        Node second = split(node);

        // Recur for left and right halves
        node = mergeSort(node);
        second = mergeSort(second);

        // Merge the two sorted halves
        return merge(node, second);
    }

    // Function to merge two linked lists
    public Node merge(Node first, Node second) {
        // If first linked list is empty
        if (first == null) {
            return second;
        }

        // If second linked list is empty
        if (second == null) {
            return first;
        }

        // Pick the bigger value
        if (first.data > second.data) {
            first.next = merge(first.next, second);
            first.next.prev = first;
            first.prev = null;
            return first;
        } else if (first.data < second.data) {
            second.next = merge(first, second.next);
            second.next.prev = second;
            second.prev = null;
            return second;
        } else {
            if (first.id < second.id) {
                first.next = merge(first.next, second);
                first.next.prev = first;
                first.prev = null;
                return first;
            } else {
                second.next = merge(first, second.next);
                second.next.prev = second;
                second.prev = null;
                return second;
            }
        }
    }

    /* Function to display status of list */
    public void display() {
        System.out.print("\nCircular Doubly Linked List = ");
        Node ptr = start;
        if (size == 0) {
            System.out.print("empty\n");
            return;
        }
        if (start.next == start) {
            System.out.print(start.data + ", id: " + ptr.id + " <-> " + ptr.data + ", id: " + ptr.id + "\n");
            return;
        }
        System.out.print(start.data + ", id: " + ptr.id + " <-> ");
        ptr = start.next;
        while (ptr.next != start) {
            System.out.print(ptr.data + ", id: " + ptr.id + " <-> ");
            ptr = ptr.next;
        }
        System.out.print(ptr.data + ", id: " + ptr.id + " <-> ");
        ptr = ptr.next;
        System.out.print(ptr.data + ", id: " + ptr.id + "\n");
    }
}

// AVL
class AVLNode {
    int key, height, size;
    AVLNode left, right;
    ArrayDeque<Integer> entries = new ArrayDeque<>();

    AVLNode(int key) {
        this.key = key;
        this.height = 1;
        this.size = entries.size();
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", height=" + height +
                ", size=" + size +
                ", entries=" + entries +
                '}';
    }
}

class AVLTree {
    // reference: https://www.geeksforgeeks.org/deletion-in-an-avl-tree/

    AVLNode root;
    // TODO: Implement size
    int size;
    long sum;

    AVLTree() {
        this.root = null;
        this.size = 0;
        this.sum = 0;
    }

    // Utility function to recalculate a node's height
    void updateHeight(AVLNode node) {
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;

        // update size
        node.size = node.entries.size() + getSize(node.left) + getSize(node.right);
    }

    AVLNode rightRotate(AVLNode node) {
        AVLNode newRoot = node.left;
        AVLNode tmpChild = newRoot.right;

        // perform rotation
        newRoot.right = node;
        node.left = tmpChild;

        // update heights
        updateHeight(node);
        updateHeight(newRoot);

        // return new root
        return newRoot;
    }

    AVLNode leftRotate(AVLNode node) {
        AVLNode newRoot = node.right;
        AVLNode tmpChild = newRoot.left;

        // perform rotation
        newRoot.left = node;
        node.right = tmpChild;

        // update heights
        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    AVLNode insertNode(AVLNode node, int key) {
        // TODO: implement insert node

        // BST insertion
        if (node == null) {
            AVLNode newNode = new AVLNode(key);
            newNode.entries.add(key);
            newNode.size++;
            this.size++;
            this.sum += key;

            // TP02.currScore = newNode;
            return newNode;
        }

        if (key < node.key) {
            node.left = insertNode(node.left, key);
        } else if (key > node.key) {
            node.right = insertNode(node.right, key);
        } else {
            node.entries.add(key);
            node.size++;
            this.size++;
            this.sum += key;
            // TP02.currScore = node;
            return node;
        }

        // AVL

        updateHeight(node);

        // check balance status
        int balance = getBalance(node);

        // LL
        if (balance > 1 && key < node.left.key)
            return rightRotate(node);

        // RR
        if (balance < -1 && key > node.right.key)
            return leftRotate(node);

        // LR
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode deleteNode(AVLNode node, int key) {
        // TODO: implement delete node

        // BST deletion
        if (node == null)
            return node;

        if (key < node.key) {
            node.left = deleteNode(node.left, key);
        } else if (key > node.key) {
            node.right = deleteNode(node.right, key);
        } else {
            // key == root.key -> the node we're going to delete

            // node with only one child or no child
            if ((node.left == null) || (node.right == null)) {
                AVLNode tmp = null;
                if (tmp == node.left)
                    tmp = node.right;
                else
                    tmp = node.left;

                // no child
                if (tmp == null) {
                    tmp = node;
                    node = null;
                } else {
                    // one child
                    node = tmp;
                }
            } else {
                // node with two children
                // inorder successor
                AVLNode tmp = lowerBound(node.right, node.key);

                // copy the inorder successor's content to this node
                node.key = tmp.key;
                node.entries = tmp.entries;

                // delete the inorder successor
                node.right = deleteNode(node.right, tmp.key);
            }

            this.size--;
            this.sum -= key;
        }

        // if the tree had only one node then return
        if (node == null)
            return node;

        // AVL

        updateHeight(node);

        // check balance status
        int balance = getBalance(node);

        // LL
        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);

        // RR
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);

        // LR
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode deleteLargest(AVLNode node) {
        // BST deletion
        if (node == null || TP02.X <= 0)
            return node;

        if (node.right == null) {
            AVLNode tmp = node.left;
            if (node.entries.size() > TP02.X) {
                for (int i = 0; i < TP02.X; i++) {
                    node.entries.pollLast();
                }
                node.size -= TP02.X;
                this.sum -= node.key * TP02.X;
                this.size -= TP02.X;
                TP02.X = 0;
                return node;
            } else {
                TP02.X -= node.entries.size();
                this.sum -= node.key * node.entries.size();
                this.size -= node.entries.size();
                node = null;
                return tmp;
            }
        }

        node.right = deleteLargest(node.right);

        // AVL

        updateHeight(node);

        // check balance status
        int balance = getBalance(node);

        // LL
        if (balance > 1 && getBalance(node.left) >= 0)
            return rightRotate(node);

        // RR
        if (balance < -1 && getBalance(node.right) <= 0)
            return leftRotate(node);

        // LR
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // RL
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode lowerBound(AVLNode node, int value) {
        // TODO: return node with the lowest key that is >= value
        if (node == null)
            return node;

        if (node.key == value)
            return node;

        if (node.key > value) {
            AVLNode tmp = lowerBound(node.left, value);
            if (tmp == null)
                return node;
            return tmp;
        }

        return lowerBound(node.right, value);
    }

    AVLNode upperBound(AVLNode node, int value) {
        // TODO: return node with the greatest key that is <= value
        if (node == null)
            return node;

        if (node.key == value)
            return node;

        if (node.key < value) {
            AVLNode tmp = upperBound(node.right, value);
            if (tmp == null)
                return node;
            return tmp;
        }

        return upperBound(node.left, value);
    }

    // count number of nodes with key > value
    int countGreater(AVLNode node, int value) {
        if (node == null)
            return 0;

        if (node.key > value) {
            return getSize(node.right) + node.entries.size() + countGreater(node.left, value);
        } else if (node.key < value) {
            return countGreater(node.right, value);
        } else {
            // if (node.key == value)
            return getSize(node.right);
        }
    }

    // count number of nodes with key >= value
    int countGreaterEqual(AVLNode node, int value) {
        if (node == null)
            return 0;

        if (node.key > value) {
            return getSize(node.right) + node.entries.size() + countGreaterEqual(node.left, value);
        } else if (node.key < value) {
            return countGreaterEqual(node.right, value);
        } else {
            // if (node.key == value)
            return getSize(node.right) + node.entries.size();
        }
    }

    AVLNode searchNode(AVLNode node, int key) {
        if (node == null)
            return node;

        if (node.key > key)
            return searchNode(node.left, key);
        else if (node.key < key)
            return searchNode(node.right, key);
        else
            return node;
    }

    void preOrder(PrintWriter out, AVLNode node) {
        if (node != null) {
            out.println(node);
            preOrder(out, node.left);
            preOrder(out, node.right);
        }
    }

    int getSize(AVLNode node) {
        if (node == null)
            return 0;

        return node.size;
    }

    // Utility function to get height of node
    int getHeight(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(AVLNode node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
}