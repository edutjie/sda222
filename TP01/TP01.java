import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
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

        int Y = in.nextInt(); // jumlah hari beroperasi
        for (int i = 0; i < Y; i++) {
            int Pi = in.nextInt(); // jumlah pelanggan yang datang pada hari ke-i
            for (int j = 0; j < Pi; j++) {

                int I = in.nextInt(); // id
                String K = in.next(); // kesehatan
                int U = in.nextInt(); // uang

                out.print(handleCustomer(j, N, I, K, U) + " ");
            }
            out.println();
            int X = in.nextInt(); // jumlah pelayanan
            for (int k = 0; k < X; k++) {
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
                    out.println(commandD(0, M));
                }
            }
            pelangganIn.clear();
            rLapar.clear();
            pesanan.clear();
        }
        out.println(pesanan);
        out.println(pelanggan);
        out.println(pelangganIn);
        out.println(rLapar);
        out.println(koki);

        out.close();
    }

    public static boolean advanceScan(int j, int R) {
        int countPos = 0;
        for (int i = j - R; i < j; i++) {
            if (pelanggan.get(riwayatIdPelanggan.get(i)).get(0) == '+') {
                countPos++;
            }
        }
        return countPos > (R / 2);
    }

    public static int handleCustomer(int j, int N, int I, String K, int U) {
        ArrayList<Integer> tmp = new ArrayList<Integer>();

        if (pelangganBlackList.contains(I)) {
            return 3;
        }

        if (K.equals("?")) {
            int R = in.nextInt(); // range advance scanning
            K = advanceScan(j, R) ? "+" : "-";
        }

        tmp.add((int) K.charAt(0));
        tmp.add(U);

        pelanggan.put(I, tmp);
        riwayatIdPelanggan.add(I);

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
            sortKoki(kokiA);
            tmp.add(kokiA.get(0));
        } else if (t == 'S') {
            sortKoki(kokiS);
            tmp.add(kokiS.get(0));
        } else {
            sortKoki(kokiG);
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

    public static void sortKoki(ArrayList<Integer> kokiSList) {
        // menggunakan selection sort untuk sorting koki
        // berdasarkan jumlah pelayanan dan id
        for (int i = 0; i < kokiSList.size(); i++) {
            for (int j = i + 1; j < kokiSList.size(); j++) {
                int idKoki1 = kokiSList.get(i);
                int idKoki2 = kokiSList.get(j);
                if (koki.get(idKoki1).get(1) > koki.get(idKoki2).get(1)
                        || (koki.get(idKoki1).get(1) == koki.get(idKoki2).get(1) && idKoki1 > idKoki2)) {
                    kokiSList.set(i, idKoki2);
                    kokiSList.set(j, idKoki1);
                }
            }
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
        pelangganIn.add(rLapar.poll());
        if (totalPrice > currPelanggan.get(1)) {
            pelangganBlackList.add(idPelanggan);
            return 0;
        }
        return 1; // 1 if cukup, 0 if tidak cukup
    }

    public static void commandC(int Q) {
        ArrayList<Integer> kokiIdList = new ArrayList<>(koki.keySet());
        sortFewestKoki(kokiIdList);
        for (int i = 0; i < Q; i++) {
            out.print(kokiIdList.get(i) + " "); // idKoki Q teratas
        }
        out.println();
        out.println(kokiIdList);
    }

    public static void sortFewestKoki(ArrayList<Integer> kokiIdList) {
        for (int i = 0; i < kokiIdList.size(); i++) {
            for (int j = i + 1; j < koki.size(); j++) {
                int idKoki1 = kokiIdList.get(i);
                int idKoki2 = kokiIdList.get(j);
                if (koki.get(idKoki1).get(1) > koki.get(idKoki2).get(1)) {
                    kokiIdList.set(i, idKoki2);
                    kokiIdList.set(j, idKoki1);
                } else if (koki.get(idKoki1).get(1) == koki.get(idKoki2).get(1)
                        && koki.get(idKoki1).get(0) < koki.get(idKoki2).get(0)) {
                    kokiIdList.set(i, idKoki2);
                    kokiIdList.set(j, idKoki1);
                } else if (koki.get(idKoki1).get(1) == koki.get(idKoki2).get(1)
                        && koki.get(idKoki1).get(0) == koki.get(idKoki2).get(0)
                        && idKoki1 > idKoki2) {
                    kokiIdList.set(i, idKoki2);
                    kokiIdList.set(j, idKoki1);
                }
            }
        }
    }

    public static int commandD(int start, int end) {
        // base case
        if (start == end)
            return menu.get(start).get(0);
        if (start > end)
            return 0;

        // Memorization
        // if (memory[start] != -1)
        // return memory[start];

        // recursion case
        int minCost = Integer.MAX_VALUE;
        for (int i = start; i <= end; i++) {
            int sum = calcD(start, i) + commandD(i + 1, end);
            minCost = Math.min(minCost, sum);
        }

        // store to memory and return minCost
        return minCost; // harga min untuk beli seluruh menu makanan
    }

    public static int calcD(int start, int end) {
        if (menu.get(start).get(1) == menu.get(end).get(1)) {
            return (end - start) * promoCost.get(menu.get(start).get(1));
        }

        int sum = 0;
        for (int i = start; i <= end; i++) {
            sum += menu.get(i).get(0);
        }
        return sum;
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