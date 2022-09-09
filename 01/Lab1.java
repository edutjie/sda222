import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Lab1 {
    private static InputReader in;
    private static PrintWriter out;

    static int getTotalDeletedLetters(int N, char[] x) {
        // TODO: implement method getTotalDeletedLetter(int, char[]) to get the answer
        String base = "SOFITA";

        // create a hashmap containing all letters in base as keys
        // and the values is 0 to perform counting
        Map<Character, Integer> hmap = new HashMap<>();
        hmap.put('S', 0);
        hmap.put('O', 0);
        hmap.put('F', 0);
        hmap.put('I', 0);
        hmap.put('T', 0);
        hmap.put('A', 0);

        for (int i = 0; i < N; i++) {
            // if the current character is in hashmap keys
            if (hmap.containsKey(x[i])) {
                // get the index of current character from base
                int j = base.indexOf(x[i]);
                if (j > 0) {
                    // if j > 0: meaning current character is not the first char in base,
                    // so we can access the previous char in base
                    char prev_char = base.charAt(j - 1);
                    if (hmap.get(prev_char) > 0) {
                        // if previous char value is greater than 0
                        // update current char value by 1
                        hmap.put(x[i], hmap.get(x[i]) + 1);
                        // decrease prev char value by 1: meaning the char's
                        // been used by current char
                        hmap.put(prev_char, hmap.get(prev_char) - 1);
                    }
                } else {
                    // update current char value by 1
                    hmap.put(x[i], hmap.get(x[i]) + 1);
                }
            }
        }
        // after all operations' been done, the value of A (the last char in base) will
        // be how much SOFITA subsequences in x, so we multiply it with 6 (base.length)
        // substract N with it, so we can get the length of the rest character that
        // doens't form SOFITA
        return N - (hmap.get('A') * 6);
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

        // long startTime = System.nanoTime();
        int ans = getTotalDeletedLetters(N, x);
        // long endTime = System.nanoTime();
        // long totalTime = endTime - startTime;
        out.println(ans);
        // System.out.println("Total time: " + totalTime);

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