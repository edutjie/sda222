import java.io.*;
import java.util.ArrayDeque;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab2v2 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;
    private static ArrayDeque<Stack<Integer>> conveyorBelt;

    static int geserKanan() {
        // TODO : Implementasi fitur geser kanan conveyor belt
        Stack<Integer> firstEl = conveyorBelt.removeLast();
        conveyorBelt.addFirst(firstEl);
        return firstEl.isEmpty() ? -1 : firstEl.peek();
    }

    static int beliRasa(int rasa) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan
        Stack<Integer> currEl;
        for (int i = 0; i < conveyorBelt.size(); i++) {
            currEl = conveyorBelt.getFirst();
            if (currEl.peek() == rasa) {
                currEl.pop();
                return i > 0 ? i : 0;
            }
            conveyorBelt.add(conveyorBelt.removeFirst());
        }
        return -1;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();

        conveyorBelt = new ArrayDeque<>();
        for (int i = 0; i < N; ++i) {
            // TODO: Inisiasi toples ke-i
            Stack<Integer> toples = new Stack<Integer>();
            for (int j = 0; j < X; j++) {
                int rasaKeJ = in.nextInt();
                // TODO: Inisiasi kue ke-j ke dalam toples ke-i
                toples.push(rasaKeJ);
            }
            conveyorBelt.addFirst(toples);
        }

        out.println(conveyorBelt);
        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
            out.println(conveyorBelt);
        }
        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
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