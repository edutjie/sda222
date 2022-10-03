import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class TP01 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;

    private static ArrayList<ArrayList<Integer>> menu = new ArrayList<>();
    private static HashMap<Integer, ArrayList<Integer>> koki = new HashMap<>();
    private static ArrayList<Integer> kokiA = new ArrayList<>();
    private static ArrayList<Integer> kokiS = new ArrayList<>();
    private static ArrayList<Integer> kokiG = new ArrayList<>();
    private static HashMap<Integer, ArrayList<Integer>> pelanggan = new HashMap<>();
    private static ArrayList<Integer> riwayatIdPelanggan = new ArrayList<>();
    private static ArrayList<Integer> pelangganIn = new ArrayList<>();
    private static ArrayDeque<Integer> pelangganBlackList = new ArrayDeque<>();
    private static ArrayDeque<Integer> rLapar = new ArrayDeque<>();
    private static ArrayDeque<ArrayList<Integer>> pesanan = new ArrayDeque<>();
    private static HashMap<Integer, ArrayList<Integer>> pesananPelanggan = new HashMap<>();
    private static HashMap<Integer, Integer> promoCost = new HashMap<>();
    private static int[] memoryD;
    public static int[] prefD;
    public static int[] prefAS;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int M = in.nextInt(); // jumlah makanan di menu
        for (int i = 0; i < M; i++) {
            ArrayList<Integer> tmp = new ArrayList<>();

            int h = in.nextInt(); // harga
            tmp.add(h);

            String t = in.next(); // tipe makanan
            tmp.add((int) t.charAt(0));

            menu.add(tmp);
        }

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

        int P = in.nextInt(); // banyaknya pelanggan
        int N = in.nextInt(); // banyaknya kursi

        memoryD = new int[M]; // inisialisasi array untuk Memorization

        prefD = new int[M]; // inisialisasi array untuk Prefix Sum commad D
        for (int i = 0; i < M; i++) {
            prefD[i] = (i > 0 ? prefD[i - 1] : 0) + menu.get(i).get(0);
        }
        // for (int i = 0; i < pref.length; i++) {
        // out.print(pref[i] + " ");
        // }
        // out.println();

        int Y = in.nextInt(); // jumlah hari beroperasi
        for (int i = 0; i < Y; i++) {
            int Pi = in.nextInt(); // jumlah pelanggan yang datang pada hari ke-i
            prefAS = new int[Pi]; // inisialisasi array untuk Prefix Sum Advance Scanning
            for (int j = 0; j < Pi; j++) {
                int I = in.nextInt(); // id
                String K = in.next(); // kesehatan
                int U = in.nextInt(); // uang

                out.print(handleCustomer(j, N, I, K, U) + " ");

                // update pref advance scanning
                prefAS[j] = (j > 0 ? prefAS[j - 1] : 0)
                        + (pelanggan.get(riwayatIdPelanggan.get(j)).get(0) == '+' ? 1 : 0);
            }
            out.println();

            int X = in.nextInt(); // jumlah pelayanan
            for (int j = 0; j < X; j++) {
                String command = in.next();
                int firstParam, secondParam, thirdParam;
                if (command.equals("P")) {
                    firstParam = in.nextInt();
                    secondParam = in.nextInt();
                    out.println(commandP(firstParam, secondParam));
                } else if (command.equals("L")) {
                    out.println(commandL());
                } else if (command.equals("B")) {
                    firstParam = in.nextInt();
                    out.println(commandB(firstParam));
                } else if (command.equals("C")) {
                    firstParam = in.nextInt();
                    commandC(firstParam);
                } else if (command.equals("D")) {
                    firstParam = in.nextInt();
                    secondParam = in.nextInt();
                    thirdParam = in.nextInt();
                    promoCost.put((int) 'A', firstParam);
                    promoCost.put((int) 'G', secondParam);
                    promoCost.put((int) 'S', thirdParam);
                    Arrays.fill(memoryD, -1); // reset array untuk Memorization
                    out.println(commandD(0, M - 1, 1, 1, 1));
                }
            }
            pelangganIn.clear();
            rLapar.clear();
            pesanan.clear();
        }
        // out.println(menu);
        // out.println(pesanan);
        // out.println(pelanggan);
        // out.println(pelangganIn);
        // out.println(rLapar);
        // out.println(koki);

        out.close();
    }

    public static boolean advanceScan(int j, int R) {
        int start = j - R;
        int end = j;
        int countPos = prefAS[end] - (start > 0 ? prefAS[start - 1] : 0);
        return countPos > (R / 2);
    }

    public static int handleCustomer(int j, int N, int I, String K, int U) {
        ArrayList<Integer> tmp = new ArrayList<Integer>();

        if (K.equals("?")) {
            int R = in.nextInt(); // range advance scanning
            K = advanceScan(j, R) ? "+" : "-";
        }

        tmp.add((int) K.charAt(0));
        tmp.add(U);

        pelanggan.put(I, tmp);
        riwayatIdPelanggan.add(I);

        if (pelangganBlackList.contains(I)) {
            return 3;
        }

        if (K.equals("+")) {
            return 0;
        }

        if (pelangganIn.size() >= N) {
            rLapar.add(I);
            return 2;
        }

        pelangganIn.add(I);
        return 1;
    }

    public static int commandP(int idPelanggan, int idxMakanan) {
        ArrayList<Integer> tmp = new ArrayList<Integer>();
        tmp.add(idPelanggan);
        idxMakanan -= 1;
        tmp.add(idxMakanan);

        int t = menu.get(idxMakanan).get(1);
        if (t == 'A') {
            sortKoki(kokiA, 0, kokiA.size() - 1);
            tmp.add(kokiA.get(0));
        } else if (t == 'S') {
            sortKoki(kokiS, 0, kokiS.size() - 1);
            tmp.add(kokiS.get(0));
        } else {
            sortKoki(kokiG, 0, kokiG.size() - 1);
            tmp.add(kokiG.get(0));
        }

        // simpan pesanan per pelanggan
        if (pesananPelanggan.containsKey(idPelanggan)) {
            pesananPelanggan.get(idPelanggan).add(idxMakanan);
        } else {
            ArrayList<Integer> tmp2 = new ArrayList<Integer>();
            tmp2.add(idxMakanan);
            pesananPelanggan.put(idPelanggan, tmp2);
        }

        pesanan.add(tmp); // append pesanan (idPelanggan, idxMakanan, idKoki)

        return tmp.get(2); // return idKoki
    }

    public static void sortKoki(ArrayList<Integer> arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;

            sortKoki(arr, l, m);
            sortKoki(arr, m + 1, r);

            sortKokiMerger(arr, l, m, r);
        }
    }

    public static void sortKokiMerger(ArrayList<Integer> arr, int l, int m, int r) {
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
                if (koki.get(L.get(i)).get(0) > koki.get(R.get(j)).get(0)) {
                    arr.set(k, L.get(i));
                    i++;
                } else if (koki.get(L.get(i)).get(0) < koki.get(R.get(j)).get(0)) {
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

    public static int commandL() {
        ArrayList<Integer> currPesanan = pesanan.poll();
        ArrayList<Integer> assignedKoki = koki.get(currPesanan.get(2));
        assignedKoki.set(1, assignedKoki.get(1) + 1);
        return currPesanan.get(0); // idPelanggan
    }

    public static int commandB(int idPelanggan) {
        ArrayList<Integer> currPelanggan = pelanggan.get(idPelanggan);
        int totalPrice = 0;
        for (int idxMakanan : pesananPelanggan.get(idPelanggan)) {
            totalPrice += menu.get(idxMakanan).get(0);
        }
        pelangganIn.remove((Integer) idPelanggan);
        if (!rLapar.isEmpty())
            pelangganIn.add(rLapar.poll());
        if (totalPrice > currPelanggan.get(1)) {
            pelangganBlackList.add(idPelanggan);
            return 0;
        }
        return 1; // 1 if cukup, 0 if tidak cukup
    }

    public static void commandC(int Q) {
        ArrayList<Integer> kokiIdList = new ArrayList<>(koki.keySet());
        sortKoki(kokiIdList, 0, kokiIdList.size() - 1);
        for (int i = 0; i < Q; i++) {
            out.print(kokiIdList.get(i) + " "); // idKoki Q teratas
        }
        out.println();
        // out.println(kokiIdList);
    }

    public static int commandD(int start, int end, int AQuota, int SQuota, int GQuota) {
        // base case
        if (start == end)
            return menu.get(start).get(0);
        if (start > end)
            return 0;

        // Memorization
        if (memoryD[start] != -1)
            return memoryD[start];

        HashMap<Integer, Integer> quota = new HashMap<>();
        quota.put((int) 'A', AQuota);
        quota.put((int) 'S', SQuota);
        quota.put((int) 'G', GQuota);
        // out.println("AQuota: " + AQuota);

        // recursion case
        int minCost = Integer.MAX_VALUE;
        for (int i = start; i <= end; i++) {
            HashMap<Integer, Integer> tmp = calcD(start, i, quota);
            // out.println("AQuota dari tmp: " + tmp.get((int) 'A'));
            int sum = tmp.get(-1) + commandD(i + 1, end, tmp.get((int) 'A'), tmp.get((int) 'S'), tmp.get((int) 'G'));
            minCost = Math.min(minCost, sum);
        }

        // store to memory and return minCost
        return memoryD[start] = minCost; // harga min untuk beli seluruh menu makanan
    }

    public static HashMap<Integer, Integer> calcD(int start, int end, HashMap<Integer, Integer> quota) {
        HashMap<Integer, Integer> tmp = new HashMap<>();
        tmp.put((int) 'A', quota.get((int) 'A'));
        tmp.put((int) 'S', quota.get((int) 'S'));
        tmp.put((int) 'G', quota.get((int) 'G'));

        Integer currMenuType = menu.get(start).get(1);
        if (tmp.get(currMenuType) == 1 && start != end && currMenuType == menu.get(end).get(1)) {
            tmp.put(-1, (end + 1 - start) * promoCost.get(currMenuType));
            tmp.put(currMenuType, tmp.get(currMenuType) - 1); // update quota
        } else {
            tmp.put(-1, prefD[end] - (start > 0 ? prefD[start - 1] : 0)); // sum harga menu dari start hingga end
        }
        return tmp;
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

class Koki implements Comparable<Koki> {
    String S;
    int jumlah_pelanggan = 0;

    public Koki(String S, int jumlah_pelanggan) {
        this.S = S;
        this.jumlah_pelanggan = jumlah_pelanggan;
    }

    @Override
    public int compareTo(Koki o) {
        // TODO Auto-generated method stub
        if (this.jumlah_pelanggan == o.jumlah_pelanggan) {
            return o.S.compareTo(this.S);
        }
        return this.jumlah_pelanggan - o.jumlah_pelanggan;
    }

    public void addJumlahPelanggan(int n) {
        this.jumlah_pelanggan += n;
    }
}

class Pelanggan {
    int I;
    int K;
    int U;
    int total_cost = 0;

    public Pelanggan(int I, int K, int U) {
        this.I = I;
        this.K = K;
        this.U = U;
    }

    public void addTotalCost(int total_cost) {
        this.total_cost += total_cost;
    }
}