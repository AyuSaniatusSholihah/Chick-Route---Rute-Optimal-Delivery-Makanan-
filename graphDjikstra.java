import java.util.*;

// Kelas Node: Mewakili satu lokasi (titik)
class Node {
    String name;
    public Node(String name) { this.name = name; }
    @Override public String toString() { return name; }
    @Override public int hashCode() { return name.hashCode(); }
    @Override public boolean equals(Object obj) { return obj instanceof Node && this.name.equals(((Node)obj).name); }
}

// Kelas Edge: Mewakili jalur antar lokasi (garis)
class Edge {
    Node destination;
    int weight;
    public Edge(Node destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}

// Kelas Utama untuk Graf dan Algoritma Dijkstra
public class graphDjikstra {
    Map<Node, List<Edge>> adjacencyList = new HashMap<>();

    public void addNode(Node node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }
    
    // Untuk jalur dua arah
    public void addUndirectedEdge(Node from, Node to, int weight) {
        addNode(from);
        addNode(to);
        adjacencyList.get(from).add(new Edge(to, weight));
        adjacencyList.get(to).add(new Edge(from, weight));
    }

    // Untuk jalur satu arah
    public void addDirectedEdge(Node from, Node to, int weight) {
        addNode(from);
        addNode(to);
        adjacencyList.get(from).add(new Edge(to, weight));
    }

    public Map<Node, Integer> dijkstra(Node start, Map<Node, Node> prev) {
        Map<Node, Integer> distance = new HashMap<>();
        PriorityQueue<Map.Entry<Node, Integer>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());

        for (Node node : adjacencyList.keySet()) {
            distance.put(node, Integer.MAX_VALUE);
        }
        distance.put(start, 0);
        pq.add(new AbstractMap.SimpleEntry<>(start, 0));

        while (!pq.isEmpty()) {
            Node current = pq.poll().getKey();
            for (Edge edge : adjacencyList.get(current)) {
                int newDist = distance.get(current) + edge.weight;
                if (newDist < distance.get(edge.destination)) {
                    distance.put(edge.destination, newDist);
                    prev.put(edge.destination, current);
                    pq.add(new AbstractMap.SimpleEntry<>(edge.destination, newDist));
                }
            }
        }
        return distance;
    }

    public List<Node> getShortestPath(Node start, Node end, Map<Node, Node> prev) {
        LinkedList<Node> path = new LinkedList<>();
        for (Node at = end; at != null; at = prev.get(at)) {
            path.addFirst(at);
        }
        if (!path.isEmpty() && path.getFirst().equals(start)) return path;
        return Collections.emptyList();
    }
}