import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

// TODO: Lengkapi class ini
class Saham {
    public int id;
    public int harga;

    public Saham(int id, int harga) {
        this.id = id;
        this.harga = harga;
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
    }

    public void ensureCapacity() {
        if (size == capacity) {
            data = Arrays.copyOf(data, capacity * 2);
            capacity *= 2;
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
        heapifyDown();
        return item;
    }

    // Time Complexity : O(log n)
    public void add(Saham item) {
        ensureCapacity();
        data[size] = item;
        size++;
        heapifyUp();
    }

    public Saham update(int id, int harga) {
        ArrayList<Saham> poppedData = new ArrayList<>();
        Saham updatedData = null;
        for (int i = 0; i < size; i++) {
            if (data[i].id == id) {
                data[i].harga = harga;
                updatedData = data[i];
                this.poll();
                break;
            }
            poppedData.add(this.poll());
        }
        for (Saham saham : poppedData) {
            this.add(saham);
        }
        return updatedData;
    }

    public void heapifyUp() {
        int index = size - 1;
        if (isMinHeap) {
            while (hasParent(index) &&
                    (parent(index).harga > data[index].harga ||
                            (parent(index).harga == data[index].harga && parent(index).id > data[index].id))) {
                swap(getParentIndex(index), index);
                index = getParentIndex(index);
            }
        } else {
            while (hasParent(index) &&
                    (parent(index).harga < data[index].harga ||
                            (parent(index).harga == data[index].harga && parent(index).id < data[index].id))) {
                swap(getParentIndex(index), index);
                index = getParentIndex(index);
            }
        }
    }

    public void heapifyDown() {
        int index = 0;
        while (hasLeftChild(index)) {
            int smallerChildIndex = getLeftChildIndex(index);
            if (isMinHeap) {
                if (hasRightChild(index) && rightChild(index).harga < leftChild(index).harga) {
                    smallerChildIndex = getRightChildIndex(index);
                } else if (hasRightChild(index) && rightChild(index).harga == leftChild(index).harga) {
                    if (rightChild(index).id < leftChild(index).id) {
                        smallerChildIndex = getRightChildIndex(index);
                    }
                }

                if (data[index].harga < data[smallerChildIndex].harga) {
                    break;
                } else if (data[index].harga == data[smallerChildIndex].harga) {
                    if (data[index].id < data[smallerChildIndex].id) {
                        break;
                    }
                } else {
                    swap(index, smallerChildIndex);
                }
            } else {
                if (hasRightChild(index) && rightChild(index).harga > leftChild(index).harga) {
                    smallerChildIndex = getRightChildIndex(index);
                } else if (hasRightChild(index) && rightChild(index).harga == leftChild(index).harga) {
                    if (rightChild(index).id > leftChild(index).id) {
                        smallerChildIndex = getRightChildIndex(index);
                    }
                }

                if (data[index].harga < data[smallerChildIndex].harga) {
                    swap(index, smallerChildIndex);
                } else if (data[index].harga == data[smallerChildIndex].harga) {
                    if (data[index].id < data[smallerChildIndex].id) {
                        swap(index, smallerChildIndex);
                    }
                } else {
                    break;
                }
            }
            index = smallerChildIndex;
        }
    }

    public void print(PrintWriter out) {
        for (int i = 0; i < size; i++) {
            out.print(data[i].id + " ");
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
        if (maxHeap.size == 0 || maxHeap.peek().harga > harga ||
                (maxHeap.peek().harga == harga && maxHeap.peek().id > id)) {
            maxHeap.add(new Saham(id, harga));
        } else {
            minHeap.add(new Saham(id, harga));
        }

        balanceHeaps();
    }

    public void add(Saham saham) {
        if (maxHeap.size == 0 || maxHeap.peek().harga > saham.harga ||
                (maxHeap.peek().harga == saham.harga && maxHeap.peek().id > saham.id)) {
            maxHeap.add(saham);
        } else {
            minHeap.add(saham);
        }

        balanceHeaps();
    }

    public void update(int id, int harga, int newHarga) {
        // maxHeap.print(out);
        // minHeap.print(out);
        Saham updatedData;
        if (maxHeap.peek().harga >= harga || (maxHeap.peek().harga == harga && maxHeap.peek().id >= id)) {
            updatedData = maxHeap.update(id, newHarga);
        } else {
            updatedData = minHeap.update(id, newHarga);
        }
        
        balanceHeaps();
        this.add(updatedData);
        // maxHeap.print(out);
        // minHeap.print(out);
    }
    
    public int getMedian() {
        // maxHeap.print(out);
        // minHeap.print(out);
        if (maxHeap.size > minHeap.size) {
            return maxHeap.peek().id;
        } else {
            return minHeap.peek().id;
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
        HashMap<Integer, Integer> hargaMap = new HashMap<>();

        // TODO
        for (int i = 1; i <= N; i++) {
            int harga = in.nextInt();
            heap.add(i, harga);
            hargaMap.put(i, harga);
        }

        int Q = in.nextInt();

        // TODO
        for (int i = 0; i < Q; i++) {
            String q = in.next();

            if (q.equals("TAMBAH")) {
                int harga = in.nextInt();
                heap.add(N + 1, harga);
                hargaMap.put(N + 1, harga);
                N++;
                out.println(heap.getMedian());
            } else if (q.equals("UBAH")) {
                int nomorSeri = in.nextInt();
                int harga = in.nextInt();
                heap.update(nomorSeri, hargaMap.get(nomorSeri), harga);
                hargaMap.put(nomorSeri, harga);
                out.println(heap.getMedian());
            }
        }
        out.flush();
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