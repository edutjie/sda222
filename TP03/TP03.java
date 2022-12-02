// Eduardus Tjitrahardja | 2106653602

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class TP03 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // N = banyaknya pos, M = banyaknya terowongan
        int N = in.nextInt(), M = in.nextInt();
        Graph graph = new Graph(N);

        for (int i = 0; i < M; i++) {
            // A = nomor pos awal, B = nomor pos akhir,
            // L = panjang terowongan, S = ukuran terowongan
            int A = in.nextInt(), B = in.nextInt(), L = in.nextInt(), S = in.nextInt();
            graph.addEdge(A, B, L, S);
            graph.addEdge(B, A, L, S);
        }

        int P = in.nextInt(); // P = banyaknya kurcaci yang ada di dalam terowongan
        ArrayList<Integer> kurcaci = new ArrayList<Integer>();
        for (int i = 0; i < P; i++) {
            int R = in.nextInt(); // R = nomor pos kurcaci
            kurcaci.add(R);
        }

        int Q = in.nextInt(); // Q = n query
        for (int i = 0; i < Q; i++) {
            String query = in.next();
            if (query.equals("KABUR")) {
                // F = nomor pos awal, E = nomor pos akhir
                int F = in.nextInt(), E = in.nextInt();
                // prints jumlah logistik terbanyak yang dapat dibawah kabur dr F ke E
                out.println(graph.getMaxSize(F).get(E));
            } else if (query.equals("SIMULASI")) {
                int K = in.nextInt(); // K = banyaknya pos exit

                // get the minimum distance from each exit to each node
                ArrayList<Integer> exits = new ArrayList<>();
                for (int j = 0; j < K; j++) {
                    int X = in.nextInt(); // X = nomor pos exit
                    exits.add(X);
                }

                ArrayList<Integer> minExit = graph.dijkstra(exits);

                // get the maximum time that is needed for all kurcaci to reach the exit
                int maxTime = 0;
                for (int posKurcaci : kurcaci) {
                    int time = minExit.get(posKurcaci);
                    if (time > maxTime) {
                        maxTime = time;
                    }
                }

                // prints jumlah detik terkecil yang dibutuhkan untuk
                // semua kurcaci keluar dari terowongan
                out.println(maxTime);
            } else if (query.equals("SUPER")) {
                // V1 = nomor pos awal, V2 = nomor pos akhir, V3 = nomor pos selanjutnya
                int V1 = in.nextInt(), V2 = in.nextInt(), V3 = in.nextInt();

                // out.println(graph.superDijkstra(V1));
                // out.println(graph.superDijkstra(V2));
                // out.println("-----------------");

                int[][] dist = graph.superDijkstra(V1);
                int dist1 = dist[V2][0];
                int dist1Super = dist[V2][1];
                // int minDist = Math.min(dist[V2][0], dist[V2][1]);

                dist = graph.superDijkstra(V2);
                int dist2 = dist[V3][0];
                int dist2Super = dist[V3][1];

                int minDist = Math.min(dist1 + dist2Super, dist1Super + dist2);
                // returns jumlah detik terkecil yang dibutuhkan kurcaci untuk ke V3
                out.println(minDist);
            }
        }

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

class Pair implements Comparable<Pair> {
    // first = source; second = jumlah bala bantuan
    int src;
    int weight;
    int k;

    public Pair(int src, int weight) {
        this.src = src;
        this.weight = weight;
    }

    public Pair(int src, int weight, int k) {
        this.src = src;
        this.weight = weight;
        this.k = k;
    }

    @Override
    public int compareTo(Pair o) {
        return (int) (this.weight - o.weight);
    }
}

class Edge {
    // from = source; to = destination;
    // length = panjang terowongan; size = ukuran terowongan
    int to;
    int length;
    int size;

    public Edge(int to, int length, int size) {
        this.to = to;
        this.length = length;
        this.size = size;
    }
}

class Graph {
    // referensi:
    // https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/

    public int V;
    public ArrayList<ArrayDeque<Edge>> adj;

    public Graph(int v) {
        this.V = v;
        this.adj = new ArrayList<>();
        for (int i = 0; i <= v; i++)
            this.adj.add(new ArrayDeque<>());
    }

    public void addEdge(int from, int to, int length, int size) {
        this.adj.get(from).add(new Edge(to, length, size));
    }

    /*
     * all dijkstra made by looking at this reference:
     * https://cp-algorithms.com/graph/01_bfs.html
     */

    public ArrayList<Integer> getMaxSize(int source) {
        if (source == 0)
            return null;
        ArrayList<Integer> bottleneck = new ArrayList<>();
        for (int i = 0; i <= this.V; i++)
            bottleneck.add(Integer.MIN_VALUE);
        bottleneck.set(source, Integer.MAX_VALUE);

        // priority queue
        Heap pq = new Heap(this.V); // max heap
        pq.add(new Pair(source, 0));

        // visited array
        boolean[] visited = new boolean[this.V + 1];

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source

            visited[v] = true; // mark as visited

            for (Edge e : this.adj.get(v)) {
                int u = e.to;
                int weight = e.size;
                int maxBottleneck = Math.max(bottleneck.get(u), Math.min(bottleneck.get(v), weight));
                if (!visited[u] && maxBottleneck > bottleneck.get(u)) {
                    // out.println("v = " + v + ", u = " + u + ", weight = " + weight + ",
                    // bottleneck(u) = " + bottleneck.get(u) + ", bottleneck(v) = " +
                    // bottleneck.get(v));
                    bottleneck.set(u, maxBottleneck);
                    pq.add(new Pair(u, maxBottleneck));
                }
            }
        }

        return bottleneck;
    }

    public ArrayList<Integer> dijkstra(ArrayList<Integer> sources) {
        if (sources.isEmpty())
            return null;
        ArrayList<Integer> dist = new ArrayList<>();
        for (int i = 0; i <= this.V; i++)
            dist.add(Integer.MAX_VALUE);

        Heap pq = new Heap(this.V, true); // min heap

        for (int src : sources) {
            dist.set(src, 0);
            pq.add(new Pair(src, 0));
        }

        // visited array
        boolean[] visited = new boolean[this.V + 1];

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source
            int w = curr.weight; // weight

            visited[v] = true; // mark as visited

            if (w > dist.get(v))
                continue;

            for (Edge e : this.adj.get(v)) {
                int u = e.to;
                int weight = e.length;
                if (!visited[u] && dist.get(v) + weight < dist.get(u)) {
                    dist.set(u, dist.get(v) + weight);
                    pq.add(new Pair(u, dist.get(u)));
                }
            }
        }

        return dist;
    }

    public ArrayList<Integer> dijkstra(int source) {
        if (source == 0)
            return null;
        ArrayList<Integer> dist = new ArrayList<>();
        for (int i = 0; i <= this.V; i++)
            dist.add(Integer.MAX_VALUE);
        dist.set(source, 0);

        Heap pq = new Heap(this.V, true); // min heap
        pq.add(new Pair(source, 0));

        // visited array
        boolean[] visited = new boolean[this.V + 1];

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source
            int w = curr.weight; // weight

            visited[v] = true; // mark as visited

            if (w > dist.get(v))
                continue;

            for (Edge e : this.adj.get(v)) {
                int u = e.to;
                int weight = e.length;
                if (!visited[u] && dist.get(v) + weight < dist.get(u)) {
                    dist.set(u, dist.get(v) + weight);
                    pq.add(new Pair(u, dist.get(u)));
                }

            }
        }

        return dist;
    }

    public int[][] superDijkstra(int source) {
        // modifed using this reference:
        // https://codeforces.com/blog/entry/70589

        if (source == 0)
            return null;
        int K = 1; // max number of edges can be cut
        int[][] dp = new int[this.V + 1][K + 1];
        for (int i = 0; i <= this.V; i++) {
            dp[i][0] = Integer.MAX_VALUE;
            dp[i][1] = Integer.MAX_VALUE;
        }
        dp[source][0] = 0;

        Heap pq = new Heap(this.V, true); // min heap
        pq.add(new Pair(source, 0, 0));

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source
            int w = curr.weight; // weight
            int k = curr.k;

            if (w > dp[v][k])
                continue;

            for (Edge e : this.adj.get(v)) {
                int u = e.to;
                int weight = e.length;

                if (dp[v][k] + weight < dp[u][k]) {
                    dp[u][k] = dp[v][k] + weight;
                    pq.add(new Pair(u, dp[u][k], k));
                }

                if (k + 1 <= K && dp[v][k] < dp[u][k + 1]) {
                    dp[u][k + 1] = dp[v][k];
                    pq.add(new Pair(u, dp[u][k + 1], k + 1));
                }
            }
        }

        return dp;
    }
}

class Heap {
    // referensi Min Heap:
    // https://medium.com/@ankur.singh4012/implementing-min-heap-in-java-413d1c20f90d

    // referensi Max Heap:
    // https://medium.com/@ankur.singh4012/implementing-max-heap-in-java-ea368dadd273

    public int capacity;
    public int size;
    public Pair[] data;
    public boolean isMinHeap = false;
    // public int[] indexMap = new int[500000];

    public Heap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.data = new Pair[capacity];
    }

    public Heap(int capacity, boolean isMinHeap) {
        this(capacity);
        this.isMinHeap = isMinHeap;
    }

    public boolean isEmpty() {
        return this.size == 0;
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

    public Pair leftChild(int index) {
        return data[getLeftChildIndex(index)];
    }

    public Pair rightChild(int index) {
        return data[getRightChildIndex(index)];
    }

    public Pair parent(int index) {
        return data[getParentIndex(index)];
    }

    public void swap(int a, int b) {
        Pair temp = data[a];
        data[a] = data[b];
        data[b] = temp;

        // update indexMap
        // indexMap[data[a].id] = a + 1;
        // indexMap[data[b].id] = b + 1;
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
    public Pair peek() {
        if (size == 0) {
            return null;
        }
        return data[0];
    }

    // Time Complexity : O(log n)
    public Pair poll() {
        if (size == 0) {
            return null;
        }
        Pair item = data[0];
        data[0] = data[size - 1];
        size--;

        // update indexMap
        // indexMap[data[0].id] = 1;
        // indexMap[item.id] = 0;

        heapifyDown(0);
        return item;
    }

    // Time Complexity : O(log n)
    public void add(Pair item) {
        ensureCapacity();
        data[size] = item;
        size++;

        // update indexMap
        // indexMap[item.id] = size;

        heapifyUp(size - 1);
    }

    // public T update(int id, int harga) {
    // // get index from indexMap
    // int i = indexMap[id] - 1;
    // if (i == -1) {
    // return null;
    // }
    // T T = data[i];
    // T.harga = harga;

    // // heapify
    // if (isMinHeap) {
    // if (hasParent(i) && parent(i).compareTo(T) > 0) {
    // heapifyUp(i);
    // } else {
    // heapifyDown(i);
    // }
    // } else {
    // if (hasParent(i) && parent(i).compareTo(T) < 0) {
    // heapifyUp(i);
    // } else {
    // heapifyDown(i);
    // }
    // }
    // return T;
    // }

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

    // public void print(PrintWriter out) {
    // for (int i = 0; i < size; i++) {
    // out.print("id: " + data[i].id + " data: " + data[i].harga + ", ");
    // }
    // out.println();
    // }
}