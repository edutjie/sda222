import java.io.*;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TP01 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;
    private static HashMap<String, Integer> menu = new HashMap<>();
    private static HashMap<Integer, String> koki = new HashMap<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int M = in.nextInt();
        // masih salah data structure
        for (int i = 0; i < M; i++) {
            int h = in.nextInt();
            String t = in.next();
            menu.put(t, h);
        }

        int V = in.nextInt();
        for (int i = 1; i <= V; i++) {
            String S = in.next();
            koki.put(i, S);
        }

        int P = in.nextInt(); // banyaknya pelanggan
        int N = in.nextInt(); // banyaknya kursi

        int Y = in.nextInt(); // jumlah hari beroperasi
        for (int i = 0; i < Y; i++) {
            int Pi = in.nextInt(); // jumlah pelanggan yang datang pada hari ke-i
            for (int j = 0; j < Pi; j++) {
                
            }
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