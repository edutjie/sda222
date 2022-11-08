import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

class TP02 {
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

            Node currMesin = listMesin.getLast();
            currMesin.setId(i);

            for (int j = 1; j <= M; j++) {
                int Z = in.nextInt(); // skor awal Mi pada mesin dengan id i
                currMesin.scores.add(Z);
                currMesin.scores.getLast().setId(j);
            }
        }

        // sort listMesin based on popularity
        listMesin.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o2.data == o1.data) {
                    return o1.id - o2.id;
                }
                return o2.data - o1.data;
            }
        });

        listMesin.display();

        // set player to the first game machine
        listMesin.playerPtr = listMesin.getFirst();
        // listMesin.playerPtr.scores.display();

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
        listMesin.playerPtr.scores.add(Y);
        listMesin.playerPtr.scores.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o2.data - o1.data;
            }
        });
        listMesin.playerPtr.scores.display();

        return listMesin.playerPtr.scores.indexOf(Y);
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

        return 0; // jumlah seluruh skor yang dihapus
    }

    public static int queryLihat(int L, int H) { // L, H = range yang ingin dilihat

        return 0; // banyaknya skor yang nilainya di range L - H (inklusif)
    }

    public static int queryEvaluasi() {

        return 0; // urutan posisi baru mesin permainan yang berada di depan pemain
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
    protected int data, id;
    protected Node next, prev;
    protected LinkedList scores = new LinkedList();

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

    public void setId(int i) {
        id = i;
    }

    /* Function to set link to next node */
    public void setNext(Node n) {
        next = n;
    }

    /* Function to set link to previous node */
    public void setPrev(Node p) {
        prev = p;
    }

    public int getId() {
        return id;
    }

    /* Funtion to get link to next node */
    public Node getNext() {
        return next;
    }

    /* Function to get link to previous node */
    public Node getPrev() {
        return prev;
    }

    /* Function to set data to node */
    public void setData(int d) {
        data = d;
    }

    /* Function to get data from node */
    public int getData() {
        return data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return (this.data == node.data && this.id == node.id);
        } else {
            return false;
        }
    }
}

/* Class linkedList */
class LinkedList {
    protected Node start;
    protected Node end;
    public int size;
    public Node playerPtr;

    /* Constructor */
    public LinkedList() {
        start = null;
        end = null;
        size = 0;
    }

    public void sort(Comparator<Node> comparator) {
        // referensi:
        // https://www.geeksforgeeks.org/java-program-to-sort-the-elements-of-the-circular-linked-list/

        // current pointer pointing to the head of the list
        Node curr = start;

        // this is the Algorithm discussed above
        if (start != null) {
            while (curr.getNext() != start) {
                Node tmp = curr.next;
                while (tmp != start) {
                    if (comparator.compare(curr, tmp) > 0) {
                        int value = curr.getData();
                        curr.setData(tmp.getData());
                        tmp.setData(value);
                    }
                    tmp = tmp.next;
                }
                curr = curr.next;
            }
        }
    }

    /* Function to check if list is empty */
    public boolean isEmpty() {
        return start == null;
    }

    /* Function to get size of list */
    public int getSize() {
        return size;
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
            nptr.setNext(nptr);
            nptr.setPrev(nptr);
            start = nptr;
            end = start;
        } else {
            nptr.setPrev(end);
            end.setNext(nptr);
            start.setPrev(nptr);
            nptr.setNext(start);
            start = nptr;
        }
        size++;
    }

    /* Function to insert element at end */
    public void add(int val) {
        Node nptr = new Node(val, null, null);
        if (start == null) {
            nptr.setNext(nptr);
            nptr.setPrev(nptr);
            start = nptr;
            end = start;
        } else {
            nptr.setPrev(end);
            end.setNext(nptr);
            start.setPrev(nptr);
            nptr.setNext(start);
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
                Node tmp = ptr.getNext();
                ptr.setNext(nptr);
                nptr.setPrev(ptr);
                nptr.setNext(tmp);
                tmp.setPrev(nptr);
            }
            ptr = ptr.getNext();
        }
        size++;
    }

    /* Function to delete node at position */
    public void removeAt(int pos) {
        if (pos == 1) {
            if (size == 1) {
                start = null;
                end = null;
                size = 0;
                return;
            }
            start = start.getNext();
            start.setPrev(end);
            end.setNext(start);
            size--;
            return;
        }
        if (pos == size) {
            end = end.getPrev();
            end.setNext(start);
            start.setPrev(end);
            size--;
        }
        Node ptr = start.getNext();
        for (int i = 2; i <= size; i++) {
            if (i == pos) {
                Node p = ptr.getPrev();
                Node n = ptr.getNext();

                p.setNext(n);
                n.setPrev(p);
                size--;
                return;
            }
            ptr = ptr.getNext();
        }
    }

    public int indexOf(int val) {
        Node ptr = start;
        for (int i = 1; i <= size; i++) {
            if (ptr.getData() == val) {
                return i;
            }
            ptr = ptr.getNext();
        }
        return -1;
    }

    // sort Node's data and id in descending order using merge sort

    /* Function to display status of list */
    public void display() {
        System.out.print("\nCircular Doubly Linked List = ");
        Node ptr = start;
        if (size == 0) {
            System.out.print("empty\n");
            return;
        }
        if (start.getNext() == start) {
            System.out.print(start.getData() + " <-> " + ptr.getData() + "\n");
            return;
        }
        System.out.print(start.getData() + " <-> ");
        ptr = start.getNext();
        while (ptr.getNext() != start) {
            System.out.print(ptr.getData() + " <-> ");
            ptr = ptr.getNext();
        }
        System.out.print(ptr.getData() + " <-> ");
        ptr = ptr.getNext();
        System.out.print(ptr.getData() + "\n");
    }
}