import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Lab5 {

    private static InputReader in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int numOfInitialPlayers = in.nextInt();
        for (int i = 0; i < numOfInitialPlayers; i++) {
            // TODO: process inputs
            String P = in.next();
            int S = in.nextInt();

            // insert to tree
            tree.root = tree.insertNode(tree.root, S, P);
        }

        int numOfQueries = in.nextInt();
        for (int i = 0; i < numOfQueries; i++) {
            // tree.preOrder(tree.root);
            String cmd = in.next();
            if (cmd.equals("MASUK")) {
                handleQueryMasuk();
            } else {
                handleQueryDuo();
            }
        }

        out.close();
    }

    static void handleQueryMasuk() {
        // TODO
        String P = in.next();
        int S = in.nextInt();

        // insert to tree
        tree.root = tree.insertNode(tree.root, S, P);

        // calculate how many players are have less score than inserted player
        out.println(tree.getRank(tree.root, S));
    }

    static void handleQueryDuo() {
        // TODO
        int K = in.nextInt();
        int B = in.nextInt();

        Node nodeLB = tree.lowerBound(tree.root, K);
        Node nodeUB = tree.upperBound(tree.root, B);

        if (tree.root.height <= 1) {
            out.println("-1 -1");
        } else {
            String entryLB = nodeLB.entries.removeLast();
            if (nodeLB.entries.isEmpty()) {
                tree.root = tree.deleteNode(tree.root, nodeLB.key);
            }

            String entryUB = nodeUB.entries.removeLast();
            if (nodeUB.entries.isEmpty()) {
                tree.root = tree.deleteNode(tree.root, nodeUB.key);
            }

            if (entryLB.compareTo(entryUB) < 0) {
                out.println(entryLB + " " + entryUB);
            } else {
                out.println(entryUB + " " + entryLB);
            }
        }
    }

    // taken from https://codeforces.com/submissions/Petr
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

// TODO: modify as needed
class Node {
    int key, height;
    Node left, right;
    ArrayDeque<String> entries = new ArrayDeque<>();

    Node(int key) {
        this.key = key;
        this.height = 1;
    }
}

class AVLTree {
    // reference: https://www.geeksforgeeks.org/deletion-in-an-avl-tree/

    Node root;

    // Utility function to recalculate a node's height
    void updateHeight(Node node) {
        node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    Node rightRotate(Node node) {
        // TODO: implement right rotate
        Node newRoot = node.left;
        Node tmpChild = newRoot.right;

        // perform rotation
        newRoot.right = node;
        node.left = tmpChild;

        // update heights
        updateHeight(node);
        updateHeight(newRoot);

        // return new root
        return newRoot;
    }

    Node leftRotate(Node node) {
        // TODO: implement left rotate
        Node newRoot = node.right;
        Node tmpChild = newRoot.left;

        // perform rotation
        newRoot.left = node;
        node.right = tmpChild;

        // update heights
        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    Node insertNode(Node node, int key, String playerName) {
        // TODO: implement insert node

        // BST insertion
        if (node == null) {
            Node newNode = new Node(key);
            newNode.entries.add(playerName);
            return newNode;
        }

        if (key < node.key) {
            node.left = insertNode(node.left, key, playerName);
        } else if (key > node.key) {
            node.right = insertNode(node.right, key, playerName);
        } else {
            node.entries.add(playerName);
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

    Node deleteNode(Node node, int key) {
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

            // if (node.entries.size() > 1) {
            // node.entries.removeLast();
            // return node;
            // }

            // node with only one child or no child
            if ((node.left == null) || (node.right == null)) {
                Node tmp = null;
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
                Node tmp = lowerBound(node.right, node.key);

                // copy the inorder successor's content to this node
                node.key = tmp.key;

                // delete the inorder successor
                node.right = deleteNode(node.right, tmp.key);
            }
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

    Node lowerBound(Node node, int value) {
        // TODO: return node with the lowest key that is >= value
        if (node == null)
            return node;

        if (node.key == value)
            return node;

        if (node.key > value) {
            Node tmp = lowerBound(node.left, value);
            if (tmp == null)
                return node;
            return tmp;
        }

        return lowerBound(node.right, value);
    }

    Node upperBound(Node node, int value) {
        // TODO: return node with the greatest key that is <= value
        if (node == null)
            return node;

        if (node.key == value)
            return node;

        if (node.key < value) {
            Node tmp = upperBound(node.right, value);
            if (tmp == null)
                return node;
            return tmp;
        }

        return upperBound(node.left, value);
    }

    Node searchNode(Node node, int key) {
        if (node == null)
            return node;

        if (key > node.key)
            return searchNode(node.right, key);

        if (key < node.key)
            return searchNode(node.left, key);

        return node;
    }

    int getRank(Node node, int key) {
        if (node == null)
            return 0;

        if (key < node.key)
            return getRank(node.left, key);

        if (key > node.key)
            return 1 + countEntries(node.left, key) + getRank(node.right, key);

        return countEntries(node.left, key);
    }

    int countEntries(Node node, int key) {
        // count entries from nodes that key is less than key
        if (node == null)
            return 0;

        if (key < node.key)
            return countEntries(node.left, key);

        if (key > node.key)
            return node.entries.size() + countEntries(node.left, key) + countEntries(node.right, key);

        return node.entries.size() + countEntries(node.left, key);
    }

    void preOrder(Node node) {
        if (node != null) {
            System.out.println(node.key + ":" + node.entries);
            preOrder(node.left);
            preOrder(node.right);
        }
    }

    // Utility function to get height of node
    int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(Node node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }
}