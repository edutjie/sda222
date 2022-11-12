import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class TP2_2 {

  private static InputReader in;
  static PrintWriter out;
  static CircularDoublyLinkedList list = new CircularDoublyLinkedList();

  public static void main(String[] args) {
    InputStream inputStream = System.in;
    in = new InputReader(inputStream);
    OutputStream outputStream = System.out;
    out = new PrintWriter(outputStream);

    int banyakMesin = in.nextInt();
    for (int i = 0; i < banyakMesin; i++) {
      NodeLinkedList mesin = new NodeLinkedList();
      list.insertLast(mesin);

      int banyakSkor = in.nextInt();
      for (int j = 0; j < banyakSkor; j++) {
        int skor = in.nextInt();
        mesin.tree.root = mesin.tree.insertNode(mesin.tree.root, skor);
        mesin.totalSkor += skor;
      }

      if (i == banyakMesin - 1) {
        list.setCircular();
      }
      /* DEBUG TREE */
      // out.print(mesin.key);
      // out.print(" ");
      // out.print(mesin.peringkat);
      // out.print(" ");
      // out.print(mesin.tree.getSize(mesin.tree.root));
      // out.print(" ");
      // out.println(mesin.totalSkor);
      // mesin.tree.root.printInOrder();
      // out.println();
      // out.println();
    }

    /* DEBUG CIRCULAR */
    // for (int i = 0; i < 10; i++) {
    //   out.println(list.current.key);
    //   list.current = list.current.next;
    // }

    int banyakPerintah = in.nextInt();
    for (int i = 0; i < banyakPerintah; i++) {
      String perintah = in.next();
      if (perintah.equals("MAIN")) {
        Integer skor = in.nextInt();
        list.current.totalSkor += skor;
        list.current.tree.root =
          list.current.tree.insertNode(list.current.tree.root, skor);
        out.println(
          list.current.tree.higherNode(list.current.tree.root, skor) + 1
        );
        // list.current.tree.root.printInOrder();
      } else if (perintah.equals("GERAK")) {
        String arah = in.next();
        if (arah.equals("KANAN")) {
          list.current = list.current.next;
        } else {
          list.current = list.current.prev;
        }
        out.println(list.current.key);
        // list.current.tree.root.printInOrder();
      } else if (perintah.equals("HAPUS")) {
        Integer banyakHapus = in.nextInt();
        if (list.current.tree.getSize(list.current.tree.root) <= banyakHapus) {
          if (list.first == list.last) {
            out.println(list.current.totalSkor);
            list.current.tree.root = null;
            list.current.totalSkor = 0;
          } else if (list.current == list.last) {
            out.println(list.current.totalSkor);
            list.current.tree.root = null;
            list.current.totalSkor = 0;
            list.current = list.current.next;
          } else {
            out.println(list.current.totalSkor);
            list.moveToMostRight();
          }
        } else {
          int sum = 0;
          for (int j = 0; j < banyakHapus; j++) {
            NodeAVL temp = list.current.tree.findMax(list.current.tree.root);
            sum += temp.skor;
            list.current.totalSkor -= temp.skor;
            list.current.tree.root =
              list.current.tree.deleteNode(list.current.tree.root, temp.skor);
          }

          out.println(sum);
        }
        // list.current.tree.root.printInOrder();
      } else if (perintah.equals("LIHAT")) {
        int keyLow = in.nextInt();
        int keyHigh = in.nextInt();
        NodeAVL low = list.current.tree.lowerBound(
          list.current.tree.root,
          keyLow
        );
        list.current.tree.flag = 0;
        NodeAVL high = list.current.tree.upperBound(
          list.current.tree.root,
          keyHigh
        );
        list.current.tree.flag = 0;

        if ((low == null) || (high == null)) {
          out.println("0");
        } else if (low.skor == high.skor) {
          out.println(low.count);
        } else if (low.skor < high.skor) {
          int selisih =
            list.current.tree.higherNode(list.current.tree.root, low.skor) -
            list.current.tree.higherNode(list.current.tree.root, high.skor) +
            low.count;

          out.println(selisih);
        }
      } else if (perintah.equals("EVALUASI")) {
        sortMethod(list);
        out.println(list.current.peringkat);
      }
    }
    out.close();
  }

  static class NodeLinkedList implements Comparable<NodeLinkedList> {

    Integer key = 0;
    Integer totalSkor = 0;
    Integer peringkat = 0;
    NodeLinkedList next, prev;
    AVLTree tree = new AVLTree();
    static Integer banyakNodeLinkedList = 0;

    NodeLinkedList() {
      banyakNodeLinkedList++;
      this.peringkat += banyakNodeLinkedList;
      this.key += banyakNodeLinkedList;
    }

    NodeLinkedList(Integer key, Integer peringkat) {
      this.peringkat = peringkat;
      this.key = key;
    }

    public int compareTo(NodeLinkedList o) {
      if (this.tree.getSize(this.tree.root) > o.tree.getSize(o.tree.root)) {
        return 1;
      } else if (
        this.tree.getSize(this.tree.root) < o.tree.getSize(o.tree.root)
      ) {
        return -1;
      } else {
        if (key < o.key) {
          return 1;
        } else if (key > o.key) {
          return -1;
        } else {
          return 0;
        }
      }
    }
  }

  static class CircularDoublyLinkedList {

    NodeLinkedList first, last, current, pointerSort;
    static Integer panjang = 0;

    void insertLast(NodeLinkedList node) {
      if (first == null) {
        first = node;
        last = node;
        current = node;
        pointerSort = node;
      } else {
        node.prev = last;
        last.next = node;
        last = node;
      }

      panjang++;
    }

    void setCircular() {
      last.next = first;
      first.prev = last;
    }

    void moveToMostRight() {
      if (current == first) {
        first = current.next;
      }
      NodeLinkedList temp = new NodeLinkedList(current.key, current.peringkat);
      current.prev.next = current.next;
      current.next.prev = current.prev;
      current = current.next;
      insertLast(temp);
      setCircular();
    }

    void clear() {
      first = last = current = pointerSort = null;
      panjang = 0;
    }
  }

  static class NodeAVL {

    Integer height, size, count, skor;
    NodeAVL left, right;

    NodeAVL(Integer skor) {
      this.height = 1;
      this.size = 1;
      this.count = 1;
      this.skor = skor;
    }

    void printInOrder() {
      if (left != null) left.printInOrder(); // Left
      out.print(skor);
      out.print(" ");
      out.print(height);
      out.print(" ");
      out.println(count);
      if (right != null) right.printInOrder(); // Right
    }
  }

  static class AVLTree {

    NodeAVL root;
    int flag;

    NodeAVL rotateWithRightChild(NodeAVL node) {
      NodeAVL tmp = node.right;
      node.right = tmp.left;
      tmp.left = node;
      // update tinggi kedua node.
      node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
      tmp.height = Math.max(getHeight(tmp.left), getHeight(tmp.right)) + 1;
      node.size = node.count + getSize(node.left) + getSize(node.right);
      tmp.size = tmp.count + getSize(tmp.left) + getSize(tmp.right);
      return tmp;
    }

    NodeAVL rotateWithLeftChild(NodeAVL node) {
      NodeAVL tmp = node.left;
      node.left = tmp.right;
      tmp.right = node;
      // update tinggi kedua node.
      node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
      tmp.height = Math.max(getHeight(tmp.left), getHeight(tmp.right)) + 1;
      node.size = node.count + getSize(node.left) + getSize(node.right);
      tmp.size = tmp.count + getSize(tmp.left) + getSize(tmp.right);
      return tmp;
    }

    NodeAVL doubleWithLeftChild(NodeAVL node) {
      node.left = rotateWithRightChild(node.left);
      return rotateWithLeftChild(node);
    }

    NodeAVL doubleWithRightChild(NodeAVL node) {
      node.right = rotateWithLeftChild(node.right);
      return rotateWithRightChild(node);
    }

    NodeAVL insertNode(NodeAVL node, Integer skor) {
      if (node == null) {
        node = new NodeAVL(skor);
      } else if (skor > node.skor) {
        node.left = insertNode(node.left, skor);
        if (getBalanceAbs(node) == 2) {
          if (skor > node.left.skor) {
            node = rotateWithLeftChild(node);
          } else {
            node = doubleWithLeftChild(node);
          }
        }
      } else if (skor < node.skor) {
        node.right = insertNode(node.right, skor);
        if (getBalanceAbs(node) == 2) {
          if (skor < node.right.skor) {
            node = rotateWithRightChild(node);
          } else {
            node = doubleWithRightChild(node);
          }
        }
      } else {
        node.count++;
        node.size++;
      }

      node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
      node.size = node.count + getSize(node.left) + getSize(node.right);

      return node;
    }

    NodeAVL deleteNode(NodeAVL node, int skor) {
      if (node == null) {
        // out.println(1);
        return node;
      }
      if (skor > node.skor) {
        // out.println(2);
        node.left = deleteNode(node.left, skor);
      } else if (skor < node.skor) {
        // out.println(3);
        node.right = deleteNode(node.right, skor);
      } else {
        if (node.count > 1) {
          node.count--;
        } else if ((node.left == null) || (node.right == null)) {
          NodeAVL temp = node.left != null ? node.left : node.right;

          // No child case
          if (temp == null) {
            // out.println(5);
            temp = node;
            node = null;
          }
          // One child case
          else {
            // out.println(6);
            node = temp;
          }
        } else {
          // out.println(7);
          NodeAVL temp = findMax(node.right);
          node.skor = temp.skor;
          node.count = temp.count;
          temp.count = 1;
          // Delete the inorder successor
          node.right = deleteNode(node.right, temp.skor);
        }
      }
      if (node == null) return node;

      node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
      node.size = node.count + getSize(node.left) + getSize(node.right);

      //   out.print(node.height);
      //   out.print(" ");
      //   out.println(node.size);

      if (getBalance(node) >= 2) {
        if (getBalance(node.left) >= 0) {
          node = rotateWithLeftChild(node);
        } else {
          node = doubleWithLeftChild(node);
        }
      } else if (getBalance(node) <= -2) {
        if (getBalance(node.right) <= 0) {
          node = rotateWithRightChild(node);
        } else {
          node = doubleWithRightChild(node);
        }
      }

      node.height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
      node.size = node.count + getSize(node.left) + getSize(node.right);

      //   out.print(node.height);
      //   out.print(" ");
      //   out.println(node.size);

      return node;
    }

    NodeAVL findMax(NodeAVL t) {
      while (t.left != null) {
        t = t.left;
      }
      return t;
    }

    NodeAVL lowerBound(NodeAVL node, int value) {
      NodeAVL tmp = null;
      if (node == null) {
        return null;
      } else if (value == node.skor) {
        this.flag = 1;
        return node;
      } else if ((node.left != null) && (value > node.skor)) {
        tmp = lowerBound(node.left, value);
      } else if ((node.right != null) && (value < node.skor)) {
        tmp = lowerBound(node.right, value);
      }

      if (this.flag == 1) {
        return tmp;
      } else if ((tmp == null) && (node.skor > value)) {
        this.flag = 1;
        return node;
      } else {
        return null;
      }
    }

    NodeAVL upperBound(NodeAVL node, int value) {
      NodeAVL tmp = null;
      if (node == null) {
        return null;
      } else if (value == node.skor) {
        this.flag = 1;
        return node;
      } else if ((node.left != null) && (value > node.skor)) {
        tmp = upperBound(node.left, value);
      } else if ((node.right != null) && (value < node.skor)) {
        tmp = upperBound(node.right, value);
      }

      if (this.flag == 1) {
        return tmp;
      } else if ((tmp == null) && (node.skor < value)) {
        this.flag = 1;
        return node;
      } else {
        return null;
      }
    }

    int higherNode(NodeAVL node, int x) {
      if (node == null) {
        // out.println(1);
        return 0;
      }
      if (x > node.skor) {
        // out.println(2);
        return higherNode(node.left, x);
      } else if (x < node.skor) {
        // out.println(3);
        int tmp = 0;
        if (node.left != null) {
          tmp = node.left.size;
        }
        return node.count + tmp + higherNode(node.right, x);
      } else {
        // out.println(4);
        if (node.left != null) {
          //   out.println(5);
          return node.left.size;/* perhatikan disini */
        } else {
          //   out.println(6);
          return 0;
        }
      }
    }

    int higherNode2(NodeAVL node, int x) {
      if (node == null) {
        // out.println(1);
        return 0;
      }
      if (x > node.skor) {
        // out.println(2);
        return higherNode(node.left, x);
      } else if (x < node.skor) {
        // out.println(3);
        int tmp = 0;
        if (node.left != null) {
          tmp = node.left.size;
        }
        return node.count + tmp + higherNode(node.right, x);
      } else {
        // out.println(4);
        if (node.left != null) {
          //   out.println(5);
          return node.count + node.left.size;/* perhatikan disini */
        } else {
          //   out.println(6);
          return node.count;
        }
      }
    }

    // Utility function to get height of node
    int getHeight(NodeAVL node) {
      if (node == null) {
        return 0;
      }
      return node.height;
    }

    // Utility function to get balance factor of node
    int getBalance(NodeAVL node) {
      if (node == null) {
        return 0;
      }
      return getHeight(node.left) - getHeight(node.right);
    }

    int getBalanceAbs(NodeAVL node) {
      if (node == null) {
        return -1;
      }
      return Math.abs(getHeight(node.left) - getHeight(node.right));
    }

    int getSize(NodeAVL node) {
      if (node == null) {
        return 0;
      }
      return node.size;
    }
  }

  static CircularDoublyLinkedList sortMethod(
    CircularDoublyLinkedList linkedlist
  ) {
    NodeLinkedList temp = linkedlist.current;
    NodeLinkedList[] array = new NodeLinkedList[NodeLinkedList.banyakNodeLinkedList];

    NodeLinkedList tempFirst = linkedlist.first;
    for (int i = 0; i < array.length; i++) {
      array[i] = tempFirst;
      tempFirst = tempFirst.next;
    }

    array = mergeSort(array);

    linkedlist.clear();

    // int peringkat = 0;
    // for (NodeLinkedList a : array) {
    //   peringkat++;
    //   linkedlist.insertLast(a);
    //   a.peringkat = peringkat;
    // }

    for (int i = 0; i < array.length; i++) {
      linkedlist.insertLast(array[i]);
      array[i].peringkat = i + 1;

      if (array[i].compareTo(temp) == 0) {
        linkedlist.current = array[i];
      }

      if (i == array.length - 1) {
        linkedlist.setCircular();
      }
    }

    // linkedlist.current = temp;

    for (int i = 0; i < array.length; i++) {
      if (linkedlist.current.compareTo(temp) == 0) {
        break;
      }
      linkedlist.current = linkedlist.current.next;
    }

    // for (NodeLinkedList a : array) {
    //   out.println(a.key);
    // }

    return linkedlist;
  }

  static NodeLinkedList[] mergeSort(NodeLinkedList[] list) {
    // Jika panjang array <= 1
    if (list.length <= 1) {
      return list;
    } else {
      int idxTengah = list.length / 2;

      NodeLinkedList[] arrayKiri = new NodeLinkedList[idxTengah];

      System.arraycopy(list, 0, arrayKiri, 0, idxTengah);

      arrayKiri = mergeSort(arrayKiri);

      NodeLinkedList[] arrayKanan = new NodeLinkedList[list.length - idxTengah];

      System.arraycopy(list, idxTengah, arrayKanan, 0, list.length - idxTengah);

      arrayKanan = mergeSort(arrayKanan);

      return (merge(arrayKiri, arrayKanan)); // tail rekursif
    }
  }

  static NodeLinkedList[] merge(
    NodeLinkedList[] memberKiri,
    NodeLinkedList[] memberKanan
  ) {
    NodeLinkedList[] hasil = new NodeLinkedList[memberKiri.length +
    memberKanan.length];
    int idxKiri = 0;
    int idxKanan = 0;
    int idxHasil = 0;

    while (idxKiri < memberKiri.length && idxKanan < memberKanan.length) {
      if (memberKiri[idxKiri].compareTo(memberKanan[idxKanan]) == 1) {
        hasil[idxHasil] = memberKiri[idxKiri];
        idxKiri++;
        idxHasil++;
      } else {
        hasil[idxHasil] = memberKanan[idxKanan];
        idxKanan++;
        idxHasil++;
      }
    }

    // sisa member
    if (idxKiri < memberKiri.length) {
      for (int i = idxHasil; i < hasil.length; i++) {
        hasil[i] = memberKiri[idxKiri];
        idxKiri++;
      }
    } else if (idxKanan < memberKanan.length) {
      for (int i = idxHasil; i < hasil.length; i++) {
        hasil[i] = memberKanan[idxKanan];
        idxKanan++;
      }
    }

    return hasil;
  }

  // taken from https://codeforces.com/submissions/Petr
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
