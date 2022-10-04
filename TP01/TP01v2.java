import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class TP01v2 {
    // TODO : Silahkan menambahkan struktur data yang diperlukan
    private static InputReader in;
    private static PrintWriter out;

    private static Menu[] menu;
    // private static HashMap<Integer, Koki> koki = new HashMap<>();
    private static TreeSet<Koki> kokiA = new TreeSet<>();
    private static TreeSet<Koki> kokiS = new TreeSet<>();
    private static TreeSet<Koki> kokiG = new TreeSet<>();
    private static Pelanggan[] pelanggan;
    private static ArrayList<Integer> riwayatIdPelanggan = new ArrayList<>();
    // private static ArrayList<Integer> pelangganIn = new ArrayList<>();
    private static int kapasitasMeja;
    private static boolean[] pelangganBlackList;
    private static ArrayDeque<Integer> rLapar = new ArrayDeque<>();
    private static ArrayDeque<Pesanan> pesanan = new ArrayDeque<>();
    // private static HashMap<String, Integer> promoCost = new HashMap<>();
    private static int ApromoCost, GpromoCost, SpromoCost;
    private static long[][][][] memoryD;
    private static int[] prefD;
    private static int[] prefAS;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int M = in.nextInt(); // jumlah makanan di menu
        menu = new Menu[M];
        for (int i = 0; i < M; i++) {

            int h = in.nextInt(); // harga
            // tmp.add(h);

            String t = in.next(); // tipe makanan
            // tmp.add((int) t.charAt(0));

            Menu tmp = new Menu(h, t);
            menu[i] = tmp;
        }

        int V = in.nextInt(); // jumlah koki
        for (int i = 1; i <= V; i++) {
            // ArrayList<Integer> tmp = new ArrayList<>();

            String S = in.next(); // spesialis

            Koki tmp = new Koki(i, S);
            if (S.equals("A")) {
                kokiA.add(tmp);
            } else if (S.equals("S")) {
                kokiS.add(tmp);
            } else {
                kokiG.add(tmp);
            }

            // tmp.add((int) S.charAt(0));
            // tmp.add(0); // jumlah pelayanan koki

            // koki.put(i, tmp);
        }

        int P = in.nextInt(); // banyaknya pelanggan
        pelanggan = new Pelanggan[P];
        pelangganBlackList = new boolean[P];

        int N = in.nextInt(); // banyaknya kursi
        kapasitasMeja = N;

        memoryD = new long[M + 1][3][3][3]; // inisialisasi array untuk Memorization

        prefD = new int[M]; // inisialisasi array untuk Prefix Sum commad D
        for (int i = 0; i < M; i++) {
            prefD[i] = (i > 0 ? prefD[i - 1] : 0) + menu[i].harga;
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
                        + (pelanggan[riwayatIdPelanggan.get(j) - 1].kesehatan.equals("+") ? 1 : 0);
            }
            out.println();

            int X = in.nextInt(); // jumlah pelayanan
            for (int j = 0; j < X; j++) {
                String command = in.next();
                int firstParam, secondParam;
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
                    ApromoCost = in.nextInt();
                    GpromoCost = in.nextInt();
                    SpromoCost = in.nextInt();

                    // promoCost.put("A", firstParam);
                    // promoCost.put("G", secondParam);
                    // promoCost.put("S", thirdParam);

                    for (int k = 0; k <= M; k++) {
                        for (int l = 0; l < 3; l++) {
                            for (int m = 0; m < 3; m++) {
                                for (int n = 0; n < 3; n++) {
                                    memoryD[k][l][m][n] = 50000000000L;
                                }
                            }
                        }
                    }
                    memoryD[0][0][0][0] = 0;
                    // Arrays.fill(memoryD, -1); // reset array untuk Memorization
                    out.println(commandD(0, M));
                }
            }
            // pelangganIn.clear();
            kapasitasMeja = N;
            rLapar.clear();
            pesanan.clear();
            riwayatIdPelanggan.clear();
        }
        // out.println(menu);
        // out.println(pesanan);
        // out.println(pelanggan);
        // out.println(pelangganIn);
        // out.println(rLapar);
        // out.println(koki);
        // out.println(kokiA);
        // out.println(kokiS);
        // out.println(kokiG);

        out.close();
    }

    public static boolean advanceScan(int j, int R) {
        int start = j - R;
        int end = j - 1;
        int countPos = prefAS[end] - (start > 0 ? prefAS[start - 1] : 0);
        // out.print("COUNT POS: " + countPos + " ");
        return countPos > (R / 2);
    }

    public static int handleCustomer(int j, int N, int I, String K, int U) {

        if (K.equals("?")) {
            int R = in.nextInt(); // range advance scanning
            K = advanceScan(j, R) ? "+" : "-";
        }
        // out.print("KESEHATAN PELANGGAN " + I + ": " + K + ", ");
        Pelanggan tmp = new Pelanggan(I, K, U);
        // tmp.add((int) K.charAt(0));
        // tmp.add(U);

        pelanggan[I - 1] = tmp;
        riwayatIdPelanggan.add(I);

        if (pelangganBlackList[I - 1]) {
            return 3;
        }

        if (K.equals("+")) {
            return 0;
        }

        if (kapasitasMeja == 0) {
            rLapar.add(I);
            return 2;
        }

        // pelangganIn.add(I);
        kapasitasMeja--;
        return 1;
    }

    public static int commandP(int idPelanggan, int idxMakanan) {
        Pelanggan p = pelanggan[idPelanggan - 1];
        Menu m = menu[idxMakanan - 1];
        Pesanan tmp = new Pesanan(p, m);

        pesanan.add(tmp); // append pesanan (idPelanggan, idxMakanan, idKoki)
        p.addTotalCost(m.harga);

        return tmp.koki.id; // return idKoki
    }

    public static int commandL() {
        Pesanan currPesanan = pesanan.poll();
        Koki assignedKoki = currPesanan.koki;
        assignedKoki.addJumlahPelanggan(1);
        if (assignedKoki.spesialis.equals("A")) {
            kokiA.add(kokiA.pollFirst());
        } else if (assignedKoki.spesialis.equals("S")) {
            kokiS.add(kokiS.pollFirst());
        } else {
            kokiG.add(kokiG.pollFirst());
        }
        // assignedKoki.set(1, assignedKoki.get(1) + 1);
        return currPesanan.pelanggan.id; // idPelanggan
    }

    public static int commandB(int idPelanggan) {
        Pelanggan currPelanggan = pelanggan[idPelanggan - 1];
        // int totalPrice = 0;
        // for (int idxMakanan : pesananPelanggan.get(idPelanggan)) {
        // totalPrice += menu.get(idxMakanan).get(0);
        // }
        // pelangganIn.remove((Integer) idPelanggan);
        kapasitasMeja++;
        if (!rLapar.isEmpty()) {
            kapasitasMeja--;
            rLapar.poll();
        }
        if (currPelanggan.totalCost > currPelanggan.uang) {
            pelangganBlackList[idPelanggan - 1] = true;
            return 0;
        }
        return 1; // 1 if cukup, 0 if tidak cukup
    }

    public static void commandC(int Q) {
        TreeSet<Koki> allKoki = new TreeSet<>();
        allKoki.addAll(kokiA);
        allKoki.addAll(kokiS);
        allKoki.addAll(kokiG);
        // sortKoki(kokiIdList, 0, kokiIdList.size() - 1);
        for (int i = 0; i < Q; i++) {
            out.print(allKoki.pollFirst().id + " "); // idKoki Q teratas
        }
        out.println();
    }

    // public static long commandD(int start, int end, int AQuota, int GQuota, int
    // SQuota) {
    // // base case
    // if (start == end)
    // return menu[start].harga;
    // if (start > end)
    // return 0;

    // // Memorization
    // if (memoryD[start][AQuota][GQuota][SQuota] != -1)
    // return memoryD[start][AQuota][GQuota][SQuota];

    // // recursion case
    // long minCost = Long.MAX_VALUE;
    // for (int i = start; i <= end; i++) {
    // long[] tmp = calcD(start, i, AQuota, GQuota, SQuota);
    // long sum = tmp[0] + commandD(i + 1, end, (int) tmp[1], (int) tmp[2], (int)
    // tmp[3]);
    // // quota.put(currMenuType, 1); // reset quota
    // minCost = Math.min(minCost, sum);
    // }

    // // store to memory and return minCost
    // return memoryD[start][AQuota][GQuota][SQuota] = minCost; // harga min untuk
    // beli seluruh menu makanan
    // }

    // public static long[] calcD(int start, int end, long AQuota, long GQuota, long
    // SQuota) {
    // String currMenuType = menu[start].tipe;
    // long currTotalCost = prefD[end] - (start > 0 ? prefD[start - 1] : 0); // sum
    // harga menu dari start hingga end

    // if (currMenuType.equals("A")) {
    // if (AQuota == 1 && start != end && currMenuType.equals(menu[end].tipe)) {
    // currTotalCost = (end + 1 - start) * ApromoCost;
    // AQuota = 0;
    // }
    // } else if (currMenuType.equals("G")) {
    // if (GQuota == 1 && start != end && currMenuType.equals(menu[end].tipe)) {
    // currTotalCost = (end + 1 - start) * GpromoCost;
    // GQuota = 0;
    // }
    // } else {
    // if (SQuota == 1 && start != end && currMenuType.equals(menu[end].tipe)) {
    // currTotalCost = (end + 1 - start) * SpromoCost;
    // SQuota = 0;
    // }
    // }

    // return new long[] { currTotalCost, AQuota, GQuota, SQuota };
    // }

    public static long commandD(int start, int end) {
        for (int i = 1; i <= end; i++) {
            for (int A = 0; A < 3; A++) {
                for (int G = 0; G < 3; G++) {
                    for (int S = 0; S < 3; S++) {
                        if (A == 0) {
                            if (G == 0) {
                                if (S == 0) {
                                    // A=0; G=0; S=0
                                    memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + menu[i - 1].harga;
                                } else if (S == 1) {
                                    // A=0; G=0; S=1
                                    if (menu[i - 1].tipe.equals("S"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + SpromoCost,
                                                memoryD[i - 1][A][G][0] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + SpromoCost;
                                } else {
                                    // A=0; G=0; S=2
                                    if (menu[i - 1].tipe.equals("S"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][A][G][1] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + menu[i - 1].harga;
                                }
                            } else if (G == 1) {
                                if (S == 0 || S == 2) {
                                    // A=0; G=1; S=0 or A=0; G=1; S=2
                                    if (menu[i - 1].tipe.equals("G"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + GpromoCost,
                                                memoryD[i - 1][A][0][S] + GpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + GpromoCost;
                                } else {
                                    // A=0; G=1; S=1
                                    continue;
                                }
                            } else {
                                if (S == 0 || S == 2) {
                                    // A=0; G=2; S=0 or A=0; G=2; S=2
                                    if (menu[i - 1].tipe.equals("G"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][A][1][S] + GpromoCost);
                                    else if (menu[i - 1].tipe.equals("S") && S == 2)
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][A][G][1] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + menu[i - 1].harga;
                                } else {
                                    // A=0; G=2; S=1
                                    if (menu[i - 1].tipe.equals("S"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + SpromoCost,
                                                memoryD[i - 1][A][G][0] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + SpromoCost;
                                }
                            }
                        } else if (A == 1) {
                            if (G == 0) {
                                if (S == 0 || S == 2) {
                                    // A=1; G=0; S=0 or A=1; G=0; S=2
                                    if (menu[i - 1].tipe.equals("A"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + ApromoCost,
                                                memoryD[i - 1][0][G][S] + ApromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + ApromoCost;
                                } else {
                                    // A=1; G=0; S=1
                                    continue;
                                }
                            } else if (G == 1) {
                                continue;
                            } else {
                                if (S == 0 || S == 2) {
                                    // A=1; G=2; S=0 or A=1; G=2; S=2
                                    if (menu[i - 1].tipe.equals("A"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + ApromoCost,
                                                memoryD[i - 1][0][G][S] + ApromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + ApromoCost;

                                } else {
                                    // A=1; G=2; S=1
                                    continue;
                                }
                            }
                        } else {
                            if (G == 0) {
                                if (S == 0 || S == 2) {
                                    // A=2; G=0; S=0 or A=2; G=0; S=2
                                    if (menu[i - 1].tipe.equals("A"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][1][G][S] + ApromoCost);
                                    else if (menu[i - 1].tipe.equals("S") && S == 2)
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][A][G][1] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + menu[i - 1].harga;
                                } else {
                                    // A=2; G=0; S=1
                                    if (menu[i - 1].tipe.equals("S"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + SpromoCost,
                                                memoryD[i - 1][A][G][0] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + SpromoCost;
                                }
                            } else if (G == 1) {
                                if (S == 0 || S == 2) {
                                    // A=2; G=1; S=0 or A=2; G=1; S=2
                                    if (menu[i - 1].tipe.equals("G"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + GpromoCost,
                                                memoryD[i - 1][A][0][S] + GpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + GpromoCost;
                                } else {
                                    // A=2; G=1; S=1
                                    continue;
                                }
                            } else {
                                if (S == 0 || S == 2) {
                                    // A=2; G=2; S=0 or A=2; G=2; S=2
                                    if (menu[i - 1].tipe.equals("A"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][1][G][S] + ApromoCost);
                                    else if (menu[i - 1].tipe.equals("G"))
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][A][1][S] + GpromoCost);
                                    else if (menu[i - 1].tipe.equals("S") && S == 2)
                                        memoryD[i][A][G][S] = Math.min(
                                                memoryD[i - 1][A][G][S] + menu[i - 1].harga,
                                                memoryD[i - 1][A][G][1] + SpromoCost);
                                    else
                                        memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + menu[i - 1].harga;

                                } else {
                                    // A=2; G=2; S=1
                                    memoryD[i][A][G][S] = memoryD[i - 1][A][G][S] + SpromoCost;
                                }
                            }
                        }
                    }
                }
            }
        }

        // get the minimun
        long min = Long.MAX_VALUE;
        for (int A = 0; A < 3; A += 2) {
            for (int G = 0; G < 3; G += 2) {
                for (int S = 0; S < 3; S += 2) {
                    if (memoryD[menu.length][A][G][S] < min)
                        min = memoryD[menu.length][A][G][S];
                }
            }
        }

        // for (int i = 1; i <= end; i++) {
        // for (int A = 0; A < 3; A++) {
        // for (int G = 0; G < 3; G++) {
        // for (int S = 0; S < 3; S++) {
        // out.print(i + " " + A + " " + G + " " + S + ": ");
        // out.println(memoryD[i][A][G][S]);
        // }
        // }
        // }
        // }

        return min;
    }

    // public static long commandD(int start, int end, int A, int G, int S) {
    // // base case
    // if (start == end)
    // return menu[start].harga;
    // if (start > end)
    // return 0;

    // // Memorization
    // if (memoryD[start][A][G][S] != -1)
    // return memoryD[start][A][G][S];

    // long minCost = Long.MAX_VALUE;
    // if (menu[start].tipe.equals("A")) {
    // if (A == 2 || S == 1 || G == 1) {
    // minCost = Math.min(minCost, commandD(start + 1, end, A, G, S) +
    // menu[start].harga);
    // } else if (A == 1) {
    // // minCost = (end + 1 - start) * ApromoCost;
    // minCost = Math.min(minCost, commandD(start + 1, end, A + 1, G, S) +
    // ApromoCost);
    // }
    // }

    // return 0;
    // }

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

    static class Koki implements Comparable<Koki> {
        int id;
        String spesialis;
        int jumlahPelanggan = 0;

        public Koki(int id, String spesialis) {
            this.id = id;
            this.spesialis = spesialis;
        }

        @Override
        public int compareTo(Koki o) {
            // TODO Auto-generated method stub
            if (this.jumlahPelanggan == o.jumlahPelanggan) {
                if (o.spesialis.equals(this.spesialis)) {
                    return this.id - o.id;
                }
                return o.spesialis.compareTo(this.spesialis);
            }
            return this.jumlahPelanggan - o.jumlahPelanggan;
        }

        public void addJumlahPelanggan(int n) {
            this.jumlahPelanggan += n;
        }

        public boolean equals(Koki o) {
            return id == o.id;
        }

        @Override
        public String toString() {
            return "Koki [id=" + id + ", jumlah_pelanggan=" + jumlahPelanggan + ", spesialis=" + spesialis + "]";
        }
    }

    static class Pelanggan {
        int id;
        String kesehatan;
        int uang;
        int totalCost = 0;

        public Pelanggan(int id, String kesehatan, int uang) {
            this.id = id;
            this.kesehatan = kesehatan;
            this.uang = uang;
        }

        public void addTotalCost(int totalCost) {
            this.totalCost += totalCost;
        }

        @Override
        public String toString() {
            return "Pelanggan [id=" + id + ", kesehatan=" + kesehatan + ", totalCost=" + totalCost + ", uang=" + uang
                    + "]";
        }
    }

    static class Menu {
        int harga;
        String tipe;

        public Menu(int harga, String tipe) {
            this.harga = harga;
            this.tipe = tipe;
        }
    }

    static class Pesanan {
        Pelanggan pelanggan;
        Menu menu;
        Koki koki;

        public Pesanan(Pelanggan pelanggan, Menu menu) {
            this.pelanggan = pelanggan;
            this.menu = menu;
            if (menu.tipe.equals("A")) {
                this.koki = kokiA.first();
            } else if (menu.tipe.equals("S")) {
                this.koki = kokiS.first();
            } else {
                this.koki = kokiG.first();
            }
        }
    }
}