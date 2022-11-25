// Eduardus Tjitrahardja | 2106653602

import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // N = total jumlah benteng; M = jumlah benteng yang diserang;
        int N = in.nextInt(), M = in.nextInt();
        Graph graph = new Graph(N);

        ArrayList<Integer> attBenteng = new ArrayList<Integer>();
        for (int i = 0; i < M; i++) {
            int F = in.nextInt(); // nomor benteng yang sedang diserang
            // TODO: Tandai benteng F sebagai benteng diserang
            attBenteng.add(F);
        }

        int E = in.nextInt(); // jumlah jalan yang terdapat pada Kerajaan Fortdom
        for (int i = 0; i < E; i++) {
            // A = source; B = destination; W = jumlah musuh yang ada di jalan tersebut
            // (weight)
            int A = in.nextInt(), B = in.nextInt();
            long W = in.nextLong();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            graph.addEdge(B, A, W);
        }

        // store the result of dijkstra algorithm
        ArrayList<Long> minDist = new ArrayList<>();
        for (int benteng : attBenteng) {
            ArrayList<Long> dist = graph.dijkstra(benteng);

            // update the minimum distance from dist to minDist
            if (minDist.isEmpty()) {
                minDist = dist;
            } else {
                for (int i = 1; i < dist.size(); i++) {
                    if (dist.get(i) < minDist.get(i)) {
                        minDist.set(i, dist.get(i));
                    }
                }
            }
        }

        int Q = in.nextInt(); // jumlah query
        while (Q-- > 0) {
            // S = source; K = jumlah orang bala bantuan
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            Long dist = minDist.get(S);

            out.println(dist < K ? "YES" : "NO");
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}

class Pair implements Comparable<Pair> {
    // first = source; second = jumlah bala bantuan
    int src;
    long troops;

    public Pair(int src, long troops) {
        this.src = src;
        this.troops = troops;
    }

    @Override
    public int compareTo(Pair o) {
        return (int) (this.troops - o.troops);
    }
}

class Edge {
    // from = source; to = destination; weight = jumlah musuh
    int to;
    long weight;

    public Edge(int to, long weight) {
        this.to = to;
        this.weight = weight;
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

    public void addEdge(int v, int w, long weight) {
        this.adj.get(v).add(new Edge(w, weight));
    }

    public ArrayList<Long> dijkstra(int source) {
        if (source == 0)
            return null;
        ArrayList<Long> dist = new ArrayList<>();
        for (int i = 0; i <= this.V; i++)
            dist.add(Long.MAX_VALUE);
        dist.set(source, (long) 0);

        PriorityQueue<Pair> pq = new PriorityQueue<>();
        pq.add(new Pair(source, 0));

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source
            long w = curr.troops; // jumlah bala bantuan

            if (w > dist.get(v))
                continue;

            for (Edge e : this.adj.get(v)) {
                int u = e.to;
                long weight = e.weight;
                if (dist.get(v) + weight < dist.get(u)) {
                    dist.set(u, dist.get(v) + weight);
                    pq.add(new Pair(u, dist.get(u)));
                }
            }
        }

        return dist;
    }
}