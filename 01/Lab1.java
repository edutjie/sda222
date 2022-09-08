import java.io.*;
import java.util.StringTokenizer;

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        char[] base = "SOFITA".toCharArray();
        String s = new String(x);
        int subseqs = 0;
        int i = 0;
        while (true) {
            i = s.indexOf('S', i);
            if (i == -1)
                break;
            boolean isSubSeq = true;
            int pointer = i;
            for (int j = 1; j < base.length; j++) {
                pointer = s.indexOf(base[j], pointer);
                if (pointer == -1) {
                    isSubSeq = false;
                    break;
                } else {
                    s = s.substring(0, pointer) + s.substring(pointer + 1);
                }
            }
            if (isSubSeq)
                subseqs++;
            i++;
        }
        return N - (base.length * subseqs);
    }

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        long startTime = System.nanoTime();
        int ans = getTotalDeletedLetters(N, x);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        out.println(ans);
        System.out.println("Total time: " + totalTime);

        // don't forget to close/flush the output
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