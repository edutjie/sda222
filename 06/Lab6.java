import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi class ini
class Saham implements Comparable<Saham> {
    public int id;
    public int harga;

    public Saham(int id, int harga) {
        this.id = id;
        this.harga = harga;
    }

    @Override
    public int compareTo(Saham o) {
        // TODO Auto-generated method stub
        if (this.harga == o.harga) {
            return this.id - o.id;
        }
        return this.harga - o.harga;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "id: " + this.id + " harga: " + this.harga;
    }
}

// TODO: Lengkapi class ini
class Heap {
    // referensi Min Heap:
    // https://medium.com/@ankur.singh4012/implementing-min-heap-in-java-413d1c20f90d

    // referensi Max Heap:
    // https://medium.com/@ankur.singh4012/implementing-max-heap-in-java-ea368dadd273

    public int capacity;
    public int size;
    public Saham[] data;
    public boolean isMinHeap = true;
    public int[] indexMap = new int[500000];

    public Heap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.data = new Saham[capacity];
    }

    public Heap(int capacity, boolean isMinHeap) {
        this(capacity);
        this.isMinHeap = isMinHeap;
    }

    public int getLeftChildIndex(int parentIndex) {
        return 2 * parentIndex + 1;
    }

    public int getRightChildIndex(int parentIndex) {
        return 2 * parentIndex + 2;
    }

    public int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    public boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
    }

    public boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    public boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    public Saham leftChild(int index) {
        return data[getLeftChildIndex(index)];
    }

    public Saham rightChild(int index) {
        return data[getRightChildIndex(index)];
    }

    public Saham parent(int index) {
        return data[getParentIndex(index)];
    }

    public void swap(int a, int b) {
        Saham temp = data[a];
        data[a] = data[b];
        data[b] = temp;

        // update indexMap
        indexMap[data[a].id] = a + 1;
        indexMap[data[b].id] = b + 1;
    }

    public void ensureCapacity() {
        if (size == capacity) {
            if (capacity == 0) {
                // handle rte
                data = Arrays.copyOf(data, 1);
                capacity = 1;
            } else {
                data = Arrays.copyOf(data, capacity * 2);
                capacity *= 2;
            }
        }
    }

    // Time Complexity : O(1)
    public Saham peek() {
        if (size == 0) {
            return null;
        }
        return data[0];
    }

    // Time Complexity : O(log n)
    public Saham poll() {
        if (size == 0) {
            return null;
        }
        Saham item = data[0];
        data[0] = data[size - 1];
        size--;

        // update indexMap
        indexMap[data[0].id] = 1;
        indexMap[item.id] = 0;

        heapifyDown(0);
        return item;
    }

    // Time Complexity : O(log n)
    public void add(Saham item) {
        ensureCapacity();
        data[size] = item;
        size++;

        // update indexMap
        indexMap[item.id] = size;

        heapifyUp(size - 1);
    }

    public Saham update(int id, int harga) {
        // get index from indexMap
        int i = indexMap[id] - 1;
        if (i == -1) {
            return null;
        }
        Saham saham = data[i];
        saham.harga = harga;

        // heapify
        if (isMinHeap) {
            if (hasParent(i) && parent(i).compareTo(saham) > 0) {
                heapifyUp(i);
            } else {
                heapifyDown(i);
            }
        } else {
            if (hasParent(i) && parent(i).compareTo(saham) < 0) {
                heapifyUp(i);
            } else {
                heapifyDown(i);
            }
        }
        return saham;
    }

    public void heapifyUp(int index) {
        if (isMinHeap) {
            while (hasParent(index) && parent(index).compareTo(data[index]) > 0) {
                swap(getParentIndex(index), index);
                index = getParentIndex(index);
            }
        } else {
            while (hasParent(index) && parent(index).compareTo(data[index]) < 0) {
                swap(getParentIndex(index), index);
                index = getParentIndex(index);
            }
        }
    }

    public void heapifyDown(int index) {
        while (hasLeftChild(index)) {
            int smallestChildIndex = getLeftChildIndex(index);

            if (isMinHeap) {
                if (hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) < 0) {
                    smallestChildIndex = getRightChildIndex(index);
                }
                if (data[index].compareTo(data[smallestChildIndex]) < 0) {
                    break;
                } else {
                    swap(index, smallestChildIndex);
                }
            } else {
                if (hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) > 0) {
                    smallestChildIndex = getRightChildIndex(index);
                }
                if (data[index].compareTo(data[smallestChildIndex]) > 0) {
                    break;
                } else {
                    swap(index, smallestChildIndex);
                }
            }

            index = smallestChildIndex;
        }
    }

    public void print(PrintWriter out) {
        for (int i = 0; i < size; i++) {
            out.print("id: " + data[i].id + " data: " + data[i].harga + ", ");
        }
        out.println();
    }
}

class MedianHeap {
    // referensi:
    // https://stephenjoel2k.medium.com/two-heaps-min-heap-max-heap-c3d32cbb671d

    public Heap maxHeap;
    public Heap minHeap;

    public MedianHeap(int capacity) {
        maxHeap = new Heap(capacity, false);
        minHeap = new Heap(capacity);
    }

    public void add(int id, int harga) {
        Saham saham = new Saham(id, harga);
        if (maxHeap.size == 0 || maxHeap.peek().compareTo(saham) > 0) {
            maxHeap.add(saham);
        } else {
            minHeap.add(saham);
        }

        balanceHeaps();

        // out.println();
        // out.print("MAX: ");
        // maxHeap.print(out);
        // out.print("MIN: ");
        // minHeap.print(out);

    }

    public int getHarga(Heap heap) {
        if (heap.size == 0) {
            return 0;
        }
        return heap.peek().harga;
    }

    public int getId(Heap heap) {
        if (heap.size == 0) {
            return 0;
        }
        return heap.peek().id;
    }

    public void update(int id, int harga) {
        // maxHeap.print(out);
        // minHeap.print(out);
        Saham updatedData;
        if (maxHeap.indexMap[id] != 0) {
            updatedData = maxHeap.update(id, harga);
            // move data to minHeap if it is greater than minHeap's peek
            if (updatedData != null && updatedData.compareTo(minHeap.peek()) > 0) {
                minHeap.add(maxHeap.poll());
            }
        } else {
            updatedData = minHeap.update(id, harga);
            // move data to maxHeap if it is less than maxHeap's peek
            if (updatedData != null && updatedData.compareTo(maxHeap.peek()) < 0) {
                maxHeap.add(minHeap.poll());
            }
        }

        balanceHeaps();
        // maxHeap.print(out);
        // minHeap.print(out);
        // out.println();
        // out.println("UPDATE: " + updatedData);
        // out.print("MAX: ");
        // maxHeap.print(out);
        // out.print("MIN: ");
        // minHeap.print(out);
    }

    public int getMedian() {
        // maxHeap.print(out);
        // minHeap.print(out);
        if (maxHeap.size == minHeap.size) {
            return getId(minHeap);
        } else {
            return getId(maxHeap);
        }
    }

    public void balanceHeaps() {
        if (maxHeap.size > minHeap.size + 1) {
            minHeap.add(maxHeap.poll());
        } else if (minHeap.size > maxHeap.size) {
            maxHeap.add(minHeap.poll());
        }
    }
}

public class Lab6 {

    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        MedianHeap heap = new MedianHeap(N);

        // TODO
        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();
            heap.add(i, harga);
        }

        int Q = in.nextInt();

        // TODO
        for (int i = 0; i < Q; i++) {
            String q = in.next();

            if (q.equals("TAMBAH")) {
                int harga = in.nextInt();
                heap.add(N + 1, harga);
                N++;
                out.println(heap.getMedian());
            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();
                heap.update(nomorSeri, harga);
                out.println(heap.getMedian());
            }
        }
        out.close();
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}