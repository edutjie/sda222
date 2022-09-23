import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;

    public static int[] memory;
    public static int[] pref;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        // Inisialisasi array untuk Memorization
        memory = new int[N];
        Arrays.fill(memory, -1);

        // Inisialisasi array untuk Prefix Sum
        pref = new int[N];
        for (int i = 0; i < N; i++) {
            if (i > 0)
                pref[i] = pref[i - 1] + (A[i] == 'R' ? 1 : 0);
            else
                pref[i] = A[i] == 'R' ? 1 : 0;
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        // TODO : Implementasikan solusi rekursif untuk mendapatkan skor vote maksimal
        // untuk RED pada subarray A[start ... end] (inklusif)

        // ide dari https://www.youtube.com/watch?v=PhWWJmaKfMc&ab_channel=takeUforward

        // base case
        if (start == end)
            return A[start] == 'R' ? 1 : 0;
        if (start > end)
            return 0;

        // Memorization
        if (memory[start] != -1)
            return memory[start];

        // recursion case
        int maxAns = Integer.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            int sum = calculateRed(start, i) + getMaxRedVotes(i + 1, end);
            maxAns = Math.max(maxAns, sum);
        }

        // store to memory and return maxAns
        return memory[start] = maxAns;
    }

    public static int calculateRed(int start, int end) {
        // menggunakan Prefix Sum untuk mencari jumlah R
        int countR = pref[end] - (start > 0 ? pref[start - 1] : 0);
        int countB = end + 1 - start - countR;
        return countR > countB ? countR + countB : 0;
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}