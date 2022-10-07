import java.io.*;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Lab2 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;
    private static ArrayDeque<ArrayDeque<Integer>> conveyorBelt;

    static int geserKanan() {
        // TODO : Implementasi fitur geser kanan conveyor belt
        // store to firstEl and remove the last toples in conveyorBelt
        ArrayDeque<Integer> firstEl = conveyorBelt.removeLast();
        // add firstEl to conveyorBelt to its head
        conveyorBelt.addFirst(firstEl);
        // return -1 if it's empty else return to top of firstEl
        return firstEl.isEmpty() ? -1 : firstEl.getLast();
    }

    static int beliRasa(int rasa) {
        // TODO : Implementasi fitur beli rasa, manfaatkan fitur geser kanan
        for (int i = 0; i < conveyorBelt.size(); i++) {
            // check if the top of first toples in conveyorBelt is equeal to rasa
            if (!conveyorBelt.getFirst().isEmpty() && conveyorBelt.getFirst().getLast() == rasa) {
                // remove the cake
                conveyorBelt.getFirst().removeLast();
                return i;
            }
            // slide the conveyorBelt to the left instead of the right
            // because its final state will be the same
            // and it'll be faster because it will instantly find the nearest cake with sofita
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
            ArrayDeque<Integer> toples = new ArrayDeque<Integer>();
            for (int j = 0; j < X; j++) {
                int rasaKeJ = in.nextInt();
                // TODO: Inisiasi kue ke-j ke dalam toples ke-i
                toples.add(rasaKeJ);
            }
            conveyorBelt.addFirst(toples);
        }

        // out.println(conveyorBelt);
        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
            // out.println(conveyorBelt);
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