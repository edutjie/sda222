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
        // for (int i = 1; i <= N; i++) {
        // // TODO: Inisialisasi setiap benteng
        // graph.addEdge(N, M, i);
        // }

        ArrayList<Integer> attBenteng = new ArrayList<Integer>();
        for (int i = 0; i < M; i++) {
            int F = in.nextInt(); // nomor benteng yang sedang diserang
            // TODO: Tandai benteng F sebagai benteng diserang
            graph.adj.get(F).isAttacked = true;
            attBenteng.add(F);
        }

        int E = in.nextInt(); // jumlah jalan yang terdapat pada Kerajaan Fortdom
        for (int i = 0; i < E; i++) {
            // A = source; B = destination; W = jumlah musuh yang ada di jalan tersebut
            // (weight)
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            graph.addEdge(B, A, W);
        }

        // store the result of dijkstra algorithm
        ArrayList<Integer> minDist = new ArrayList<>();
        for (int benteng : attBenteng) {
            ArrayList<Integer> dist = graph.dijkstra(benteng);

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
        // boolean visited[][] = new boolean[5000][5000];
        while (Q-- > 0) {
            // S = source; K = jumlah orang bala bantuan
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            int dist = minDist.get(S);

            // boolean isSaved = false;

            out.println(dist < K ? "YES" : "NO");
            // for (int i = 0; i < result.size(); i++) {
            // out.print(i + ": " + result.get(i) + ", ");
            // }
            // out.println();
            // out.println(graph.bfs(S, K, visited, out) ? "YES" : "NO");
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
    int src, troops;

    public Pair(int src, int troops) {
        this.src = src;
        this.troops = troops;
    }

    @Override
    public int compareTo(Pair o) {
        return this.troops - o.troops;
    }
}

class Edge {
    // from = source; to = destination; weight = jumlah musuh
    int to, weight;

    public Edge(int to, int weight) {
        this.to = to;
        this.weight = weight;
    }
}

class Vertex {
    ArrayDeque<Edge> edges;
    boolean isAttacked;

    public Vertex() {
        this.edges = new ArrayDeque<>();
        this.isAttacked = false;
    }
}

class Graph {
    // referensi:
    // https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/

    public int V;
    public ArrayList<Vertex> adj;

    public Graph(int v) {
        this.V = v;
        this.adj = new ArrayList<>();
        for (int i = 0; i <= v; i++)
            this.adj.add(new Vertex());
    }

    public void addEdge(int v, int w, int weight) {
        this.adj.get(v).edges.add(new Edge(w, weight));
    }

    public ArrayList<Integer> dijkstra(int source) {
        if (source == 0)
            return null;
        ArrayList<Integer> dist = new ArrayList<>();
        for (int i = 0; i <= this.V; i++)
            dist.add(Integer.MAX_VALUE);
        dist.set(source, 0);

        PriorityQueue<Pair> pq = new PriorityQueue<>();
        pq.add(new Pair(source, 0));

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source
            int w = curr.troops; // jumlah bala bantuan

            if (w > dist.get(v))
                continue;

            for (Edge e : this.adj.get(v).edges) {
                int u = e.to;
                int weight = e.weight;
                if (dist.get(v) + weight < dist.get(u)) {
                    dist.set(u, dist.get(v) + weight);
                    pq.add(new Pair(u, dist.get(u)));
                }
            }
        }

        return dist;
    }

    // public ArrayList<Integer> dijkstra(int source, int troops, PrintWriter out) {
    // ArrayList<Integer> dist = new ArrayList<>();
    // for (int i = 0; i <= this.V; i++)
    // dist.add(Integer.MAX_VALUE);
    // dist.set(source, 0);

    // PriorityQueue<Pair> pq = new PriorityQueue<>();
    // pq.add(new Pair(source, 0));

    // while (!pq.isEmpty()) {
    // Pair curr = pq.poll();
    // int v = curr.src; // source
    // int w = curr.troops; // jumlah bala bantuan

    // if (w > dist.get(v))
    // continue;

    // for (Edge e : this.adj.get(v).edges) {
    // int u = e.to;
    // int weight = e.weight;
    // if (dist.get(v) + weight < dist.get(u)) {
    // dist.set(u, dist.get(v) + weight);
    // pq.add(new Pair(u, dist.get(u)));
    // }
    // }
    // }

    // return dist;
    // }

    // public boolean bfs(int src, int troops, boolean[][] visited, PrintWriter out)
    // {
    // ArrayDeque<Pair> queue = new ArrayDeque<>();
    // queue.add(new Pair(src, troops));
    // while (!queue.isEmpty()) {
    // Pair curr = queue.poll();
    // int currSrc = curr.first;
    // int currTroops = curr.second;
    // if (currTroops <= 0)
    // continue;
    // // out.println("======================================================");
    // // out.println("currSrc: " + currSrc + " currTroops: " + currTroops);
    // if (this.adj.get(currSrc - 1).isAttacked)
    // return true;
    // visited[currSrc - 1][currTroops - 1] = true;
    // for (Edge edge : this.adj.get(currSrc - 1).edges) {
    // int nextSrc = edge.to;
    // int enemy = edge.weight;
    // if (!visited[nextSrc - 1][currTroops - 1]) {
    // // out.println("nextSrc: " + nextSrc + " enemy: " + enemy);
    // if (currTroops - enemy > 0)
    // queue.add(new Pair(nextSrc, currTroops - enemy));
    // }
    // }
    // // out.println("END");
    // }
    // return false;
    // }
}