import java.util.*;

class tspAlgorithm {
    public static List<Node> tspBruteForce(Node start, List<Node> destinations, graphDjikstra graph) {
        List<Node> bestPath = new ArrayList<>();
        int minDistance = Integer.MAX_VALUE;

        Map<Node, Map<Node, Integer>> jarakCache = new HashMap<>();
        Map<Node, Map<Node, Node>> prevCache = new HashMap<>();
        List<Node> allRelevantNodes = new ArrayList<>(destinations);
        allRelevantNodes.add(start);

        for (Node from : allRelevantNodes) {
            Map<Node, Node> prev = new HashMap<>();
            Map<Node, Integer> dist = graph.dijkstra(from, prev);
            jarakCache.put(from, dist);
            prevCache.put(from, prev);
        }

        List<List<Node>> permutations = generatePermutations(destinations);

        for (List<Node> perm : permutations) {
            List<Node> currentPathSegment = new ArrayList<>();
            int totalDistance = 0;
            Node current = start;

            for (Node next : perm) {
                totalDistance += jarakCache.get(current).get(next);
                current = next;
            }

            if (totalDistance < minDistance) {
                minDistance = totalDistance;
                bestPath = new ArrayList<>(perm);
            }
        }
        
        // Reconstruct the full path for display
        List<Node> fullDisplayPath = new ArrayList<>();
        Node current = start;
        fullDisplayPath.add(current);
        for(Node next : bestPath) {
            List<Node> segment = graph.getShortestPath(current, next, prevCache.get(current));
            if (!segment.isEmpty()) segment.removeFirst();
            fullDisplayPath.addAll(segment);
            current = next;
        }
        
        return fullDisplayPath;
    }

    private static List<List<Node>> generatePermutations(List<Node> nodes) {
        List<List<Node>> result = new ArrayList<>();
        permute(nodes, 0, result);
        return result;
    }

    private static void permute(List<Node> nodes, int start, List<List<Node>> result) {
        if (start == nodes.size() - 1) {
            result.add(new ArrayList<>(nodes));
            return;
        }
        for (int i = start; i < nodes.size(); i++) {
            Collections.swap(nodes, i, start);
            permute(nodes, start + 1, result);
            Collections.swap(nodes, i, start);
        }
    }
}