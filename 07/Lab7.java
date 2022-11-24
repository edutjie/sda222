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

        for (int i = 0; i < M; i++) {
            int F = in.nextInt(); // nomor benteng yang sedang diserang
            // TODO: Tandai benteng F sebagai benteng diserang
            graph.adj.get(F - 1).isAttacked = true;
        }

        int E = in.nextInt(); // jumlah jalan yang terdapat pada Kerajaan Fortdom
        for (int i = 0; i < E; i++) {
            // A = source; B = destination; W = jumlah musuh yang ada di jalan tersebut
            // (weight)
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            // TODO: Inisialisasi jalan berarah dari benteng A ke B dengan W musuh
            graph.addEdge(A, B, W);
        }

        int Q = in.nextInt(); // jumlah query
        boolean visited[][] = new boolean[5000][5000];
        while (Q-- > 0) {
            // S = source; K = jumlah orang bala bantuan
            int S = in.nextInt(), K = in.nextInt();
            // TODO: Implementasi query
            out.println(graph.bfs(S, K, visited, out) ? "YES" : "NO");
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

class Pair {
    // first = source; second = jumlah bala bantuan
    int first, second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }
}

class Edge {
    // from = source; to = destination; weight = jumlah musuh
    int from, to, weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

class Vertex {
    LinkedList<Edge> edges;
    boolean isAttacked;

    public Vertex() {
        this.edges = new LinkedList<Edge>();
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
        for (int i = 1; i <= v; i++)
            this.adj.add(new Vertex());
    }

    public void addEdge(int v, int w, int weight) {
        this.adj.get(v - 1).edges.add(new Edge(v, w, weight));
    }

    public boolean bfs(int src, int troops, boolean[][] visited, PrintWriter out) {
        ArrayDeque<Pair> queue = new ArrayDeque<>();
        queue.add(new Pair(src, troops));
        while (!queue.isEmpty()) {
            Pair curr = queue.poll();
            int currSrc = curr.first;
            int currTroops = curr.second;
            if (currTroops <= 0)
                continue;
            // out.println("======================================================");
            // out.println("currSrc: " + currSrc + " currTroops: " + currTroops);
            if (this.adj.get(currSrc - 1).isAttacked)
                return true;
            visited[currSrc - 1][currTroops - 1] = true;
            for (Edge edge : this.adj.get(currSrc - 1).edges) {
                int nextSrc = edge.to;
                int enemy = edge.weight;
                if (!visited[nextSrc - 1][currTroops - 1]) {
                    // out.println("nextSrc: " + nextSrc + " enemy: " + enemy);
                    if (currTroops - enemy > 0)
                        queue.add(new Pair(nextSrc, currTroops - enemy));
                }
            }
            // out.println("END");
        }

        return false;
    }
}