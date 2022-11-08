import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi Class Lantai
class Gedung {
    Lantai head, tail;
    String nama;
    int topLantai;
    Lantai currDenji, currIblis;
    int denjiLevel, iblisLevel;

    public Gedung(String nama, int jumlahLantai) {
        this.nama = nama;
        this.head = null;
        this.tail = null;
        this.topLantai = jumlahLantai;
        for (int i = 0; i < jumlahLantai; i++) {
            this.addLantaiToTail();
        }
    }

    class Lantai {
        private Lantai next, prev;
        // int level;
        String namaGedung;

        public Lantai() {
            this.next = null;
            this.prev = null;
            // this.level = ++topLantai;
            this.namaGedung = nama;
        }

        public Lantai(int level) {
            this.next = null;
            this.prev = null;
            // this.level = level;
            this.namaGedung = nama;
        }

        public void setNextLantai(Lantai Lantai) {
            this.next = Lantai;
        }

        public void setPrevLantai(Lantai Lantai) {
            this.prev = Lantai;
        }

        public Lantai getNextLantai() {
            return this.next;
        }

        public Lantai getPrevLantai() {
            return this.prev;
        }
    }

    public void setCurrDenji(int level) {
        Lantai temp = this.head;
        for (int i = 1; i < level; i++) {
            temp = temp.getNextLantai();
        }
        this.denjiLevel = level;
        this.currDenji = temp;
    }

    public void setCurrIblis(int level) {
        Lantai temp = this.head;
        for (int i = 1; i < level; i++) {
            temp = temp.getNextLantai();
        }
        this.iblisLevel = level;
        this.currIblis = temp;
    }

    public void addLantaiToTail() {
        Lantai newTail = new Lantai();
        Lantai currentTail = this.tail;

        if (currentTail != null) {
            currentTail.setNextLantai(newTail);
            newTail.setPrevLantai(currentTail);
        }
        this.tail = newTail;
        if (this.head == null) {
            this.head = newTail;
        }
    }

    public boolean addLantai() {
        // add lantai below current iblis
        Lantai newLantai = new Lantai();
        Lantai currentIblis = this.currIblis;

        if (currentIblis != null) {
            Lantai prevIblis = currentIblis.getPrevLantai();
            if (prevIblis != null) {
                prevIblis.setNextLantai(newLantai);
                newLantai.setPrevLantai(prevIblis);
            } else {
                this.head = newLantai;
            }
            currentIblis.setPrevLantai(newLantai);
            newLantai.setNextLantai(currentIblis);
            topLantai++;
            return true;
        }
        return false;
    }

    public boolean removeLantai() {
        // remove lantai below current denji
        Lantai currentDenji = this.currDenji;
        Lantai prevDenji = currentDenji.getPrevLantai();
        if ((prevDenji != null) && (this.iblisLevel != (this.denjiLevel - 1))) {
            Lantai prevPrevDenji = prevDenji.getPrevLantai();
            if (prevPrevDenji != null) {
                prevPrevDenji.setNextLantai(currentDenji);
                currentDenji.setPrevLantai(prevPrevDenji);
            } else {
                this.head = currentDenji;
            }
            topLantai--;
            return true;
        }
        return false;
    }

}

// TODO: Lengkapi Class Gedung
class KumpulanGedung {
    Gedung head, tail;

    public KumpulanGedung() {

    }

}

// TODO: Lengkapi Class Karakter
// class Karakter {

// public Karakter() {

// }

// }

public class Lab4_2 {

    private static InputReader in;
    static PrintWriter out;

    private static Gedung[] kumpulanGedung;
    private static int denjiGedungPointer, iblisGedungPointer;
    private static boolean denjiIsUp = true, iblisIsUp = false;
    private static int jumlahPertemuan;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int jumlahGedung = in.nextInt();
        kumpulanGedung = new Gedung[jumlahGedung];
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // TODO: Inisiasi gedung pada kondisi awal
            kumpulanGedung[i] = new Gedung(namaGedung, jumlahLantai);
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        // TODO: Tetapkan kondisi awal Denji
        for (int i = 0; i < jumlahGedung; i++) {
            if (kumpulanGedung[i].nama.equals(gedungDenji)) {
                kumpulanGedung[i].setCurrDenji(lantaiDenji);
                denjiGedungPointer = i;
            }
        }
        // out.println("Denji berada di lantai " +
        // kumpulanGedung[denjiGedungPointer].denjiLevel + " gedung "
        // + kumpulanGedung[denjiGedungPointer].nama);

        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        // TODO: Tetapkan kondisi awal Iblis
        for (int i = 0; i < jumlahGedung; i++) {
            if (kumpulanGedung[i].nama.equals(gedungIblis)) {
                kumpulanGedung[i].setCurrIblis(lantaiIblis);
                iblisGedungPointer = i;
            }
        }

        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {

            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
            out.println("Denji berada di lantai " +
                    kumpulanGedung[denjiGedungPointer].denjiLevel + " gedung "
                    + kumpulanGedung[denjiGedungPointer].nama + " dengan arah " + (denjiIsUp ? "NAIK" : "TURUN"));
            out.println("Iblis berada di lantai " +
                    kumpulanGedung[iblisGedungPointer].iblisLevel + " gedung "
                    + kumpulanGedung[iblisGedungPointer].nama + " dengan arah " + (denjiIsUp ? "NAIK" : "TURUN"));
        }

        out.close();
    }

    static void checkPertemuan(Gedung gedungDenji, Gedung gedungIblis) {
        if (gedungDenji.nama.equals(gedungIblis.nama) && gedungDenji.denjiLevel == gedungIblis.iblisLevel) {
            jumlahPertemuan++;
        }
    }

    // TODO: Implemen perintah GERAK
    static void gerak() {
        Gedung gedungDenji = kumpulanGedung[denjiGedungPointer];
        Gedung gedungIblis = kumpulanGedung[iblisGedungPointer];

        if (denjiIsUp) {
            if (gedungDenji.currDenji.getNextLantai() != null) {
                gedungDenji.currDenji = gedungDenji.currDenji.getNextLantai();
                gedungDenji.denjiLevel++;
            } else {
                denjiIsUp = false; // denji movementnya jadi ke bawah

                // reset pointer denji di gedung sekarang
                gedungDenji.currDenji = null;
                gedungDenji.denjiLevel = 0;

                // pindah pointer denji ke gedung selanjutnya
                denjiGedungPointer = (denjiGedungPointer + 1) % kumpulanGedung.length;
                gedungDenji = kumpulanGedung[denjiGedungPointer];
                gedungDenji.currDenji = gedungDenji.tail;
                gedungDenji.denjiLevel = gedungDenji.topLantai;
            }
        } else {
            if (gedungDenji.currDenji.getPrevLantai() != null) {
                gedungDenji.currDenji = gedungDenji.currDenji.getPrevLantai();
                gedungDenji.denjiLevel--;
            } else {
                denjiIsUp = true; // denji movementnya jadi ke atas

                // reset pointer denji di gedung sekarang
                gedungDenji.currDenji = null;
                gedungDenji.denjiLevel = 0;

                // pindah pointer denji ke gedung selanjutnya
                denjiGedungPointer = (denjiGedungPointer + 1) % kumpulanGedung.length;
                gedungDenji = kumpulanGedung[denjiGedungPointer];
                gedungDenji.currDenji = gedungDenji.head;
                gedungDenji.denjiLevel = 1;
            }
        }

        checkPertemuan(gedungDenji, gedungIblis);

        for (int i = 0; i < 2; i++) {
            if (iblisIsUp) {
                if (gedungIblis.currIblis.getNextLantai() != null) {
                    gedungIblis.currIblis = gedungIblis.currIblis.getNextLantai();
                    gedungIblis.iblisLevel++;
                } else {
                    iblisIsUp = false; // iblis movementnya jadi ke bawah

                    // reset pointer iblis di gedung sekarang
                    gedungIblis.currIblis = null;
                    gedungIblis.iblisLevel = 0;

                    // pindah pointer iblis ke gedung selanjutnya
                    iblisGedungPointer = (iblisGedungPointer + 1) % kumpulanGedung.length;
                    gedungIblis = kumpulanGedung[iblisGedungPointer];
                    gedungIblis.currIblis = gedungIblis.tail;
                    gedungIblis.iblisLevel = gedungIblis.topLantai;
                }
            } else {
                if (gedungIblis.currIblis.getPrevLantai() != null) {
                    gedungIblis.currIblis = gedungIblis.currIblis.getPrevLantai();
                    gedungIblis.iblisLevel--;
                } else {
                    iblisIsUp = true; // iblis movementnya jadi ke atas

                    // reset pointer iblis di gedung sekarang
                    gedungIblis.currIblis = null;
                    gedungIblis.iblisLevel = 0;

                    // pindah pointer iblis ke gedung selanjutnya
                    iblisGedungPointer = (iblisGedungPointer + 1) % kumpulanGedung.length;
                    gedungIblis = kumpulanGedung[iblisGedungPointer];
                    gedungIblis.currIblis = gedungIblis.head;
                    gedungIblis.iblisLevel = 1;
                }
            }
        }

        checkPertemuan(gedungDenji, gedungIblis);

        // print gedungDenji, lantaiDenji, gedungIblis, lantaiIblis, jumlahPertemuan
        out.println(gedungDenji.nama + " " + gedungDenji.denjiLevel + " " +
                gedungIblis.nama + " " + gedungIblis.iblisLevel + " " + jumlahPertemuan);
    }

    // TODO: Implemen perintah HANCUR
    static void hancur() {
        Gedung gedungDenji = kumpulanGedung[denjiGedungPointer];
        if (gedungDenji.removeLantai()) {
            out.println(gedungDenji.nama + " " + (--gedungDenji.denjiLevel));
            if (denjiGedungPointer == iblisGedungPointer)
                kumpulanGedung[iblisGedungPointer].iblisLevel--;
        } else {
            out.println(gedungDenji.nama + " " + -1);
        }

        // print if berhasil -> gedungHancur, lantaiHancur
        // print else -> gedungHancur, -1
    }

    // TODO: Implemen perintah TAMBAH
    static void tambah() {
        Gedung gedungIblis = kumpulanGedung[iblisGedungPointer];
        if (gedungIblis.addLantai()) {
            out.println(gedungIblis.nama + " " + (++gedungIblis.iblisLevel - 1));
            if (denjiGedungPointer == iblisGedungPointer)
                kumpulanGedung[denjiGedungPointer].denjiLevel++;
        } else {
            out.println(gedungIblis.nama + " " + -1);
        }

        // gedungTambah, lantaiTambah
    }

    // TODO: Implemen perintah PINDAH
    static void pindah() {
        Gedung gedungDenji = kumpulanGedung[denjiGedungPointer];

        if (denjiIsUp) {
            // reset pointer denji di gedung sekarang
            gedungDenji.currDenji = null;
            gedungDenji.denjiLevel = 0;

            // pindah pointer denji ke gedung selanjutnya
            denjiGedungPointer = (denjiGedungPointer + 1) % kumpulanGedung.length;
            gedungDenji = kumpulanGedung[denjiGedungPointer];
            gedungDenji.currDenji = gedungDenji.head;
            gedungDenji.denjiLevel = 1;
        } else {
            // reset pointer denji di gedung sekarang
            gedungDenji.currDenji = null;
            gedungDenji.denjiLevel = 0;

            // pindah pointer denji ke gedung selanjutnya
            denjiGedungPointer = (denjiGedungPointer + 1) % kumpulanGedung.length;
            gedungDenji = kumpulanGedung[denjiGedungPointer];
            gedungDenji.currDenji = gedungDenji.tail;
            gedungDenji.denjiLevel = gedungDenji.topLantai;
        }

        checkPertemuan(gedungDenji, kumpulanGedung[iblisGedungPointer]);

        // gedungDenjiPindah, lantaiDenjiPindah
        out.println(gedungDenji.nama + " " + gedungDenji.denjiLevel);
    }

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