// Eduardus Tjitrahardja | 2106653602 | SDA E | TP02

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

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(); // banyaknya mesin yang tersedia
        for (int i = 1; i <= N; i++) {
            int M = in.nextInt(); // banyaknya skor awal Mi pada mesin dengan id i
            listMesin.add(M);

            Node currMesin = listMesin.end;
            currMesin.id = i;

            for (int j = 1; j <= M; j++) {
                int Z = in.nextInt(); // skor awal Mi pada mesin dengan id i
                currMesin.scores.root = currMesin.scores.insertNode(currMesin.scores.root, Z);
            }
        }

        // set player to the first game machine
        listMesin.playerPtr = listMesin.start;

        int Q = in.nextInt(); // banyaknya query
        for (int i = 0; i < Q; i++) {
            String query = in.next(); // query

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
            if (currMesin != listMesin.end) {
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
            for (int i = 0; i < X; i++) {
                // TODO: delete first X node from AVLTree
                currMesin.scores.root = currMesin.scores.deleteLargest(currMesin.scores.root); // remove largest element
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

        if (L == H) {
            // if L == H, return the number of scores in the node where key = H
            // if not found then return 0
            AVLNode searchedNode = currMesin.scores.searchNode(currMesin.scores.root, H);
            return searchedNode != null ? searchedNode.entries.size() : 0;
        }

        int lRank = currMesin.scores.countGreaterEqual(currMesin.scores.root, L);
        int hRank = currMesin.scores.countGreater(currMesin.scores.root, H);

        return lRank - hRank;
        // banyaknya skor yang nilainya di range L - H (inklusif)
    }

    public static int queryEvaluasi() {
        int playerPtrIdx = listMesin.sort(); // sort listMesin
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

    /* Function to insert element at end */
    public void add(long val) {
        Node nptr = new Node(val, null, null);
        if (this.isEmpty()) {
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
        // and count the index of playerPtr
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
        // returns the index of playerPtr
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
        // sort by node's data
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
            // if node's data is equal, sort by id
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

            // add key to entries and update size and sum
            newNode.entries.add(key);
            newNode.size++;
            this.size++;
            this.sum += key;

            return newNode;
        }

        if (key < node.key) {
            node.left = insertNode(node.left, key);
        } else if (key > node.key) {
            node.right = insertNode(node.right, key);
        } else {
            // add key to entries and update size and sum
            node.entries.add(key);
            node.size++;
            this.size++;
            this.sum += key;

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

    AVLNode deleteLargest(AVLNode node) {
        // BST deletion
        if (node == null)
            return node;

        if (node.right == null) {
            AVLNode tmp = node.left;
            this.sum -= node.key;
            this.size -= 1;
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