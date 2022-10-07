// import java.io.IOException;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.io.PrintWriter;
// import java.util.*;

// // TODO: Lengkapi Class Lantai
// class Lantai {
//     Lantai prev, next;
//     Karakter denji, iblis;

//     public Lantai(Karakter karakter) {
//         if (karakter.type == 'D') {
//             denji = karakter;
//         } else {
//             iblis = karakter;
//         }
//         this.next = null;
//     }

//     public Lantai() {
//         this(null);
//     }

//     public void naik() {
//         if (this.next != null) {
//             this.next.denji = this.denji;
//             this.denji = null;
//         }
//     }

//     public void turun() {
//         if (this.prev != null) {
//             this.prev.denji = this.denji;
//             this.denji = null;
//         }
//     }
// }

// // TODO: Lengkapi Class Gedung
// class Gedung {
//     String name;
//     Gedung next;
//     Lantai head, tail;
//     int topLantai;

//     public Gedung(String name) {
//         this.name = name;
//         this.head = null;
//         this.tail = null;
//         this.next = this;
//     }

//     public Gedung(String name, int jumlahLantai) {
//         this(name);
//         for (int i = 0; i < jumlahLantai; i++) {
//             this.addLantai();
//         }
//     }

//     public void addGedung(Gedung gedung, Gedung firstGedung) {
//         this.next = gedung;
//         gedung.next = firstGedung;
//     }

//     public void placeKarakter(Karakter karakter, int level) {
//         karakter.gedung = this;
//         karakter.level = level;
//         Lantai tmp = this.head;
//         for (int i = 1; i < lantai; i++) {
//             tmp = tmp.next;
//         }
//         tmp.karakter = karakter;
//         karakter.lantai = tmp;
//     }

//     public void naikLantai() {
//         Lantai tmp = this.head;
//         while (tmp.karakter == null) {
//             tmp = tmp.next;
//         }
//         tmp.naik();
//     }

//     public void moveToNextGedung() {
//         Lantai tmp = this.head;
//         while (tmp != null) {
//             if (tmp.karakter != null) {
//                 if (tmp == this.tail) {
//                     // if sudah sampai di lantai teratas
//                     this.next.placeKarakter(tmp.karakter, this.next.topLantai);
//                     tmp.karakter.isMovingUp = false;
//                 } else if (tmp == this.head) {
//                     // if sudah sampai di lantai terbawah
//                     this.next.placeKarakter(tmp.karakter, 1);
//                     tmp.karakter.isMovingUp = true;
//                 } else {
//                     // if pindah di tengah-tengah gedung
//                     if (tmp.karakter.isMovingUp) {
//                         this.next.placeKarakter(tmp.karakter, 1);
//                     } else {
//                         this.next.placeKarakter(tmp.karakter, this.next.topLantai);
//                     }
//                 }
//                 tmp.karakter = null;
//             }
//             tmp = tmp.next;
//         }
//     }

//     public void addLantaiToHead() {
//         Lantai newLantai = new Lantai();
//         Lantai currLantai = this.head;

//         if (currLantai != null) {
//             currLantai.prev = newLantai;
//             newLantai.next = currLantai;
//         }
//         this.head = newLantai;

//         if (this.tail == null) {
//             this.tail = newLantai;
//         }

//         this.topLantai++;
//     }

//     public void addLantai() {
//         Lantai newLantai = new Lantai();
//         Lantai currLantai = this.tail;

//         if (currLantai != null) {
//             currLantai.next = newLantai;
//             newLantai.prev = currLantai;
//         }
//         this.tail = newLantai;

//         if (this.head == null) {
//             this.head = newLantai;
//         }

//         this.topLantai++;
//     }

//     public Karakter removeHead() {
//         Lantai removedHead = this.head;

//         if (removedHead == null) {
//             return null;
//         }
//         this.head = removedHead.next;

//         if (this.head != null) {
//             this.head.prev = null;
//         }
//         if (removedHead == this.tail) {
//             this.removeTail();
//         }

//         this.topLantai--;
//         return removedHead.karakter;
//     }

//     public Karakter removeTail() {
//         Lantai removedTail = this.tail;

//         if (removedTail == null) {
//             return null;
//         }
//         this.tail = removedTail.prev;

//         if (this.tail != null) {
//             this.tail.next = null;
//         }
//         if (removedTail == this.head) {
//             this.removeHead();
//         }

//         this.topLantai--;
//         return removedTail.karakter;
//     }
// }

// // TODO: Lengkapi Class Karakter
// class Karakter {
//     Gedung gedung;
//     int level;
//     int gerakRange;
//     boolean isMovingUp;
//     int type;
//     Lantai lantai;

//     public Karakter(Gedung gedung, int level, int who) {
//         this.gedung = gedung;
//         this.level = level;
//         this.type = who;
//         if (who == 0) {
//             // who == 0 -> Denji
//             this.gerakRange = 1;
//             this.isMovingUp = true;
//         } else {
//             // who == 1 -> Iblis
//             this.gerakRange = 2;
//             this.isMovingUp = false;
//         }
//     }

//     // equals
//     public boolean equals(Karakter o) {
//         return this.type == o.type;
//     }
// }

// public class Lab4_ {
//     private static InputReader in;
//     static PrintWriter out;

//     private static HashMap<String, Gedung> gedungMap = new HashMap<>();
//     private static Gedung firstGedung;
//     private static Karakter denji, iblis;

//     public static void main(String[] args) {
//         InputStream inputStream = System.in;
//         in = new InputReader(inputStream);
//         OutputStream outputStream = System.out;
//         out = new PrintWriter(outputStream);

//         int jumlahGedung = in.nextInt();
//         for (int i = 0; i < jumlahGedung; i++) {
//             String namaGedung = in.next();
//             int jumlahLantai = in.nextInt();

//             // TODO: Inisiasi gedung pada kondisi awal
//             Gedung gedung;
//             if (i == 0) {
//                 gedung = new Gedung(namaGedung, jumlahLantai);
//                 gedungMap.put(namaGedung, gedung);
//                 firstGedung = gedung;
//             } else {
//                 gedung = new Gedung(namaGedung, jumlahLantai);
//                 gedung.addGedung(gedung, firstGedung);
//                 gedungMap.put(namaGedung, gedung);
//             }
//         }

//         String gedungDenji = in.next();
//         int lantaiDenji = in.nextInt();
//         // TODO: Tetapkan kondisi awal Denji
//         denji = new Karakter(gedungMap.get(gedungDenji), lantaiDenji, 0);
//         denji.gedung.placeKarakter(denji, lantaiDenji);

//         String gedungIblis = in.next();
//         int lantaiIblis = in.nextInt();
//         // TODO: Tetapkan kondisi awal Iblis
//         iblis = new Karakter(gedungMap.get(gedungIblis), lantaiIblis, 1);
//         iblis.gedung.placeKarakter(iblis, lantaiIblis);

//         int Q = in.nextInt();

//         for (int i = 0; i < Q; i++) {

//             String command = in.next();

//             if (command.equals("GERAK")) {
//                 gerak();
//             } else if (command.equals("HANCUR")) {
//                 hancur();
//             } else if (command.equals("TAMBAH")) {
//                 tambah();
//             } else if (command.equals("PINDAH")) {
//                 pindah();
//             }
//         }

//         out.close();
//     }

//     // TODO: Implemen perintah GERAK
//     static void gerak() {
//         for (int i = 0; i < denji.gerakRange; i++) {
//             if (denji.isMovingUp) {
//                 if (denji.level == denji.gedung.topLantai) {
//                     denji.isMovingUp = false;
//                     denji.gedung.moveToNextGedung();
//                 } else {
//                     denji.level++;
//                 }
//             } else {
//                 if (denji.level == 1) {
//                     denji.isMovingUp = true;
//                     denji.level++;
//                 } else {
//                     denji.level--;
//                 }
//             }
//         }

//         // print gedungDenji, lantaiDenji, gedungIblis, lantaiIblis, jumlahPertemuan
//     }

//     // TODO: Implemen perintah HANCUR
//     static void hancur() {
//         // print if berhasil -> gedungHancur, lantaiHancur
//         // print else -> gedungHancur, -1
//     }

//     // TODO: Implemen perintah TAMBAH
//     static void tambah() {
//         // gedungTambah, lantaiTambah
//     }

//     // TODO: Implemen perintah PINDAH
//     static void pindah() {
//         // gedungDenjiPindah, lantaiDenjiPindah
//     }

//     static class InputReader {
//         public BufferedReader reader;
//         public StringTokenizer tokenizer;

//         public InputReader(InputStream stream) {
//             reader = new BufferedReader(new InputStreamReader(stream), 32768);
//             tokenizer = null;
//         }

//         public String next() {
//             while (tokenizer == null || !tokenizer.hasMoreTokens()) {
//                 try {
//                     tokenizer = new StringTokenizer(reader.readLine());
//                 } catch (IOException e) {
//                     throw new RuntimeException(e);
//                 }
//             }
//             return tokenizer.nextToken();
//         }

//         public int nextInt() {
//             return Integer.parseInt(next());
//         }
//     }
// }