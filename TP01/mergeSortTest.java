import java.io.*;
import java.util.*;

public class mergeSortTest {
    private static InputReader in;
    private static PrintWriter out;

    private static HashMap<Integer, ArrayList<Integer>> koki = new HashMap<>();
    private static ArrayList<Integer> kokiA = new ArrayList<>();
    private static ArrayList<Integer> kokiS = new ArrayList<>();
    private static ArrayList<Integer> kokiG = new ArrayList<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int V = in.nextInt(); // jumlah koki
        for (int i = 1; i <= V; i++) {
            ArrayList<Integer> tmp = new ArrayList<>();

            String S = in.next(); // spesialis

            if (S.equals("A")) {
                kokiA.add(i);
            } else if (S.equals("S")) {
                kokiS.add(i);
            } else {
                kokiG.add(i);
            }

            tmp.add((int) S.charAt(0));
            tmp.add(0); // jumlah pelayanan koki

            koki.put(i, tmp);
        }

        ArrayList<Integer> test = new ArrayList<>();
        test.add(1);
        test.add(3);
        test.add(2);
        test.add(5);
        test.add(4);

        // implement a merge sort
        sortKoki(test, 0, test.size() - 1);

        out.println(test);

        out.close();
    }

    private static void sortKoki(ArrayList<Integer> arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            sortKoki(arr, l, m);
            sortKoki(arr, m + 1, r);

            sortKokiMerger(arr, l, m, r);
        }
    }

    private static void sortKokiMerger(ArrayList<Integer> arr, int l, int m, int r) {
        // ide dari https://www.geeksforgeeks.org/merge-sort/
        
        int n1 = m - l + 1;
        int n2 = r - m;

        ArrayList<Integer> L = new ArrayList<>();
        ArrayList<Integer> R = new ArrayList<>();

        for (int i = 0; i < n1; i++) {
            L.add(arr.get(l + i));
        }

        for (int j = 0; j < n2; j++) {
            R.add(arr.get(m + 1 + j));
        }

        int i = 0, j = 0;

        int k = l;
        while (i < n1 && j < n2) {
            if (koki.get(L.get(i)).get(1) < koki.get(R.get(j)).get(1)) {
                arr.set(k, L.get(i));
                i++;
            } else if (koki.get(L.get(i)).get(1) > koki.get(R.get(j)).get(1)) {
                arr.set(k, R.get(j));
                j++;
            } else {
                if (L.get(i) < R.get(j)) {
                    arr.set(k, L.get(i));
                    i++;
                } else {
                    arr.set(k, R.get(j));
                    j++;
                }
            }
            k++;
        }

        while (i < n1) {
            arr.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            arr.set(k, R.get(j));
            j++;
            k++;
        }
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
