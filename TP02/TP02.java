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
    static int currLargestScore = 0;
    static AVLNode currScore;

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
        // out.println("currScores rank: " + currScores.getRank(currScores.root, Y));
        // out.println("currEntrySize: " + currEntrySize);
        return currScores.size - (currScores.getRank(currScores.root, Y) - 1);
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

    public static int queryHapus(int X) { // X = banyaknya skor teratas yang curang
        Node currMesin = listMesin.playerPtr;
        int sumSkor = 0;

        if (currMesin.scores.size <= X) {
            // reset mesin's scores
            sumSkor = currMesin.scores.totalEntries;
            currMesin.scores = new AVLTree();
            currMesin.data = 0;

            listMesin.playerPtr = currMesin.next;

            // move currMesin to the last position
            if (currMesin != listMesin.getLast()) {
                // remove currMesin from listMesin
                listMesin.remove(currMesin);

                // add currMesin to the last node of listMesin
                listMesin.add(currMesin.data);
                listMesin.getLast().id = currMesin.id;
            }
        } else {
            for (int i = 0; i < X; i++) {
                // TODO: delete first X node from AVLTree
                currMesin.scores.root = currMesin.scores.deleteLargest(currMesin.scores.root); // remove largest element
                sumSkor += currLargestScore;
            }
            currMesin.data -= X;
            // currMesin.scores.totalScore -= sumSkor;
        }

        // listMesin.display();
        return sumSkor; // jumlah seluruh skor yang dihapus
    }

    public static int queryLihat(int L, int H) { // L, H = range yang ingin dilihat
        Node currMesin = listMesin.playerPtr;
        // currMesin.scores.preOrder(out, currMesin.scores.root);
        int lRank = currMesin.scores.getRank(currMesin.scores.root, L);
        // out.println("lRank: " + lRank);
        int hRank = currMesin.scores.getRank(currMesin.scores.root, H);
        // out.println("hRank: " + hRank);

        if (hRank == lRank) {
            // TODO: currScore might be null
            if (hRank == currMesin.scores.size && H != currScore.key && L != currScore.key)
                return 0;
            else
                return currScore.entries.size();
        }

        if (lRank == 0) {
            return hRank;
        }

        return hRank - lRank + 1;
        // banyaknya skor yang nilainya di range L - H (inklusif)
    }

    public static int queryEvaluasi() {
        // out.println(listMesin.playerPtr.data + ", id: " + listMesin.playerPtr.id);
        // listMesin.display();
        listMesin.sort();
        // listMesin.display();

        // get player mesin index
        Node currMesin = listMesin.getFirst();
        int playerPtrIdx = 1;
        while (!listMesin.playerPtr.equals(currMesin)) {
            currMesin = currMesin.next;
            playerPtrIdx++;
        }
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
    int data, id;
    Node next, prev;
    AVLTree scores = new AVLTree();

    /* Constructor */
    public Node() {
        next = null;
        prev = null;
        data = 0;
    }

    /* Constructor */
    public Node(int d, Node n, Node p) {
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

    // public void sort(Comparator<Node> comparator) {
    // // referensi:
    // //
    // https://www.geeksforgeeks.org/java-program-to-sort-the-elements-of-the-circular-linked-list/

    // // current pointer pointing to the head of the list
    // Node curr = start;

    // // this is the Algorithm discussed above
    // if (start != null) {
    // while (curr.next != start) {
    // Node tmp = curr.next;
    // while (tmp != start) {
    // if (comparator.compare(curr, tmp) > 0) {
    // int value = curr.data;
    // curr.data = tmp.data;
    // tmp.data = value;
    // }
    // tmp = tmp.next;
    // }
    // curr = curr.next;
    // }
    // }
    // }

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
    public void addFirst(int val) {
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
    public void add(int val) {
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
    public void addAt(int val, int pos) {
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
    public void sort() {
        // transform circular linked list to non-circular linked list
        end.next = null;
        start = mergeSort(start);

        // transform it back to circular linked list
        Node ptr = start;
        while (ptr.next != null) {
            ptr = ptr.next;
        }
        end = ptr;
        end.next = start;
    }

    public Node mergeSort(Node head) {
        if (head == null || head.next == null) {
            return head;
        }

        Node middle = getMiddle(head);
        Node nextOfMiddle = middle.next;

        middle.next = null;

        Node left = mergeSort(head);
        Node right = mergeSort(nextOfMiddle);

        Node sortedList = sortedMerge(left, right);
        return sortedList;
    }

    // merge two sorted linked list
    public Node sortedMerge(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        Node result = null;

        if (a.data > b.data) {
            result = a;
            result.next = sortedMerge(a.next, b);
        } else if (a.data < b.data) {
            result = b;
            result.next = sortedMerge(a, b.next);
        } else {
            if (a.id < b.id) {
                result = a;
                result.next = sortedMerge(a.next, b);
            } else {
                result = b;
                result.next = sortedMerge(a, b.next);
            }
        }

        return result;
    }

    // get middle node of linked list
    public Node getMiddle(Node head) {
        if (head == null) {
            return head;
        }

        Node slow = head, fast = head;

        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
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
    int size, totalEntries;

    AVLTree() {
        this.root = null;
        this.size = 0;
        this.totalEntries = 0;
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
            this.totalEntries += key;
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
            this.totalEntries += key;
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
            this.totalEntries -= key;
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
        if (node == null)
            return node;

        if (node.right == null) {
            AVLNode tmp = node.left;
            TP02.currLargestScore = node.key;
            this.totalEntries -= node.key;
            this.size--;
            if (node.entries.size() > 1) {
                node.entries.pollLast();
                node.size--;
                return node;
            } else {
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

    int getRank(AVLNode node, int key) {
        if (node == null)
            return 0;

        if (node.key > key)
            return getRank(node.left, key);
        // else if (node.key < key)
        else {
            // if (node.key == key)
            TP02.currScore = node;
            return getSize(node.left) + node.entries.size() + getRank(node.right, key);
            // return getSize(node.left);
        }
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