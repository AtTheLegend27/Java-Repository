import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.*;

/* A mutable and finite Graph object. Edge labels are stored via a HashMap
   where labels are mapped to a key calculated by the following. The graph is
   undirected (whenever an Edge is added, the dual Edge is also added). Vertices
   are numbered starting from 0. */
public class Graph {

    /* Maps vertices to a list of its neighboring vertices. */
    private HashMap<Integer, Set<Integer>> neighbors = new HashMap<>();
    /* Maps vertices to a list of its connected edges. */
    private HashMap<Integer, Set<Edge>> edges = new HashMap<>();
    /* A sorted set of all edges. */
    private TreeSet<Edge> allEdges = new TreeSet<>();

    /* Returns the vertices that neighbor V. */
    public TreeSet<Integer> getNeighbors(int v) {
        return new TreeSet<Integer>(neighbors.get(v));
    }

    /* Returns all edges adjacent to V. */
    public TreeSet<Edge> getEdges(int v) {
        return new TreeSet<Edge>(edges.get(v));
    }

    /* Returns a sorted list of all vertices. */
    public TreeSet<Integer> getAllVertices() {
        return new TreeSet<Integer>(neighbors.keySet());
    }

    /* Returns a sorted list of all edges. */
    public TreeSet<Edge> getAllEdges() {
        return new TreeSet<Edge>(allEdges);
    }

    /* Adds vertex V to the graph. */
    public void addVertex(Integer v) {
        if (neighbors.get(v) == null) {
            neighbors.put(v, new HashSet<Integer>());
            edges.put(v, new HashSet<Edge>());
        }
    }

    /* Adds Edge E to the graph. */
    public void addEdge(Edge e) {
        addEdgeHelper(e.getSource(), e.getDest(), e.getWeight());
    }

    /* Creates an Edge between V1 and V2 with no weight. */
    public void addEdge(int v1, int v2) {
        addEdgeHelper(v1, v2, 0);
    }

    /* Creates an Edge between V1 and V2 with weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        addEdgeHelper(v1, v2, weight);
    }

    /* Returns true if V1 and V2 are connected by an edge. */
    public boolean isNeighbor(int v1, int v2) {
        return neighbors.get(v1).contains(v2) && neighbors.get(v2).contains(v1);
    }

    /* Returns true if the graph contains V as a vertex. */
    public boolean containsVertex(int v) {
        return neighbors.get(v) != null;
    }

    /* Returns true if the graph contains the edge E. */
    public boolean containsEdge(Edge e) {
        return allEdges.contains(e);
    }

    /* Returns if this graph spans G. */
    public boolean spans(Graph g) {
        TreeSet<Integer> all = getAllVertices();
        if (all.size() != g.getAllVertices().size()) {
            return false;
        }
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> vertices = new ArrayDeque<>();
        Integer curr;

        vertices.add(all.first());
        while ((curr = vertices.poll()) != null) {
            if (!visited.contains(curr)) {
                visited.add(curr);
                for (int n : getNeighbors(curr)) {
                    vertices.add(n);
                }
            }
        }
        return visited.size() == g.getAllVertices().size();
    }

    /* Overrides objects equals method. */
    public boolean equals(Object o) {
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph other = (Graph) o;
        return neighbors.equals(other.neighbors) && edges.equals(other.edges);
    }

    /* A helper function that adds a new edge from V1 to V2 with WEIGHT as the
       label. */
    private void addEdgeHelper(int v1, int v2, int weight) {
        addVertex(v1);
        addVertex(v2);

        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);

        Edge e1 = new Edge(v1, v2, weight);
        Edge e2 = new Edge(v2, v1, weight);
        edges.get(v1).add(e1);
        edges.get(v2).add(e2);
        allEdges.add(e1);
    }
    HashMap<Integer, Integer> weightTracker = new HashMap<>();
    private class edgeComparator implements Comparator<Edge> {
        @Override
        public int compare (Edge v1, Edge v2) {
            return v1.getWeight() - v2.getWeight();
        }
    }
//    class compareEdges implements Comparator<Edge> {
//        public int compare(Edge edge1, Edge edge2) {
//            if (edge1.getWeight() > edge2.getWeight()) {
//                return 1;
//            } else if (edge1.getWeight() < edge2.getWeight()) {
//                return -1;
//            }
//            return 0;
//        }
//    }
//    HashMap<Edge, Integer> listForFringe = new HashMap<>();
//    PriorityQueue<Edge> fringe = new PriorityQueue<Edge>(getAllEdges().size(), new compareEdges());

    public Graph prims(int start) {
        Graph T = new Graph();
        PriorityQueue<Edge> orderedEdges = new PriorityQueue<>(new edgeComparator());
        orderedEdges.addAll(getEdges(start));

        while (T.getAllVertices().size() != getAllVertices().size()) {
            Edge e = orderedEdges.poll();
            TreeSet<Edge> nextEdges = new TreeSet<>();
            if (T.containsVertex(e.getSource()) && T.containsVertex(e.getDest())) {
                continue;
            } else if (T.containsVertex(e.getSource())) {
                nextEdges = getEdges(e.getSource());
                T.addEdge(e);
            } else if (T.containsVertex(e.getDest())) {
                nextEdges = getEdges(e.getDest());
                T.addEdge(e);
            }

            orderedEdges.addAll(nextEdges);



//            for (Edge i : nextEdges) {
//                if (i.getWeight() < weightTracker.get(i.getDest()) && !T.containsVertex(i.getDest())) {
//                    weightTracker.put(i.getDest(), i.getWeight());
//                    fromVertex.put(i.getDest(),u);
//                }
//            }
//            T.addVertex(u);
//            if (u != start) {
//                T.addEdge(new Edge(fromVertex.get(u), u, weightTracker.get(u)));
//            }
//        }
        }
        return T;
    }

    public Graph kruskals() {
        Graph T = new Graph();
        HashSet<Integer> visited = new HashSet<Integer>();
        WeightedQuickUnionUF QU = new WeightedQuickUnionUF(getAllVertices().size());
        for (Edge e : getAllEdges()) {
            if (!QU.connected(e.getSource(), e.getDest())) {
                if (!T.containsVertex(e.getSource())) {
                    T.addVertex(e.getSource());
                } else if (!T.containsVertex(e.getDest())) {
                    T.addVertex(e.getDest());
                }
                T.addEdge(e);
                QU.union(e.getSource(), e.getDest());
            }

        }

        return T;


        // TODO: YOUR CODE HERE
//        Graph T = new Graph();
//        int totalUpdatedVertices;
//        //TreeSet<Edge> totalEdgesSorted = getAllEdges().size();
//        //account for edge numbers
//        //for loop to look for each scenario until we get to N-1 vertices
//        for (Edge i: getAllEdges()){
//            fringe.add(i);
//        }
//        for (totalUpdatedVertices = 0; totalUpdatedVertices < getAllVertices().size() - 1; totalUpdatedVertices++) {
//            Edge temporaryEdgeHolder = fringe.poll();
//            int vertex1 = temporaryEdgeHolder.getSource();
//            int vertex2 = temporaryEdgeHolder.getDest();
//            int weightOfEdge = temporaryEdgeHolder.getWeight();
//            if (totalUpdatedVertices == 0) {
//                T.addEdge(temporaryEdgeHolder);
//                //add vertices to visited
//                visited.add(vertex1);
//                visited.add(vertex2);
//                totalUpdatedVertices += 1;
//            }
//            //condition if visited has both the vertices in the hashset essentially if a cycle occurs
//            else if (T.containsVertex(vertex1) && T.containsVertex(vertex2)) {
//                totalUpdatedVertices -= 1;
//                continue;
//            }
//            //condition if visited contains only vertex 1
//            else if (T.containsVertex(vertex1) && !T.containsVertex(vertex2)) {
//                T.addEdge(vertex1, vertex2, weightOfEdge);
//                visited.add(vertex2);
//            } //condition if visited contains only vertex 2
//            else if (!T.containsVertex(vertex1) && T.containsVertex(vertex2)) {
//                T.addEdge(vertex2, vertex1, weightOfEdge);
//                visited.add(vertex1);
//            }
//            //condition for normal implementation of adding an edge and vertices
////            else {
////
////            }
//        }
//        return T;
    }
}