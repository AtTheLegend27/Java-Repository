import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. That is, adds an edge
       in ONE directions, from v1 to v2. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. That is, adds an edge
       in BOTH directions, from v1 to v2 and from v2 to v1. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
            adjLists[v1].add(new Edge(v1, v2, weight));
        }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
            adjLists[v2].add(new Edge(v2, v1, weight));
            adjLists[v1].add(new Edge(v1, v2, weight));
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        if (pathExists(from, to)){
            return true;
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        List<Integer> listAllVertices = new LinkedList<>();
        for (Edge i: adjLists[v]){
            listAllVertices.add(i.to);
        }
        return listAllVertices;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int returnValue = 0;
        for (int i = 0; i < vertexCount; i++){
            if (isAdjacent(v, i)) {
                returnValue += 1;
            }

        }
        return returnValue;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        // TODO: YOUR CODE HERE
        if (start == stop) {
            return true;
        }
        List<Integer> temporaryCounter = dfs(start);
        for (Integer counter : temporaryCounter) {
            if (counter == stop) {
                return true;
            }

        }
        return false;
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        // TODO: YOUR CODE HERE
        List returnList = new LinkedList();
        Iterator<Integer> pathIterator = new DFSIterator(start);
        if (!pathExists(start, stop)) {
            return returnList;
        }
        else if (start == stop) {
            returnList.add(start);
            return returnList;
        }
        else {
            List<Integer> potentialReversePath = new LinkedList();
            // need to do this with while loop: First, add code to stop calling next when you encounter the finish vertex.
            //while loop makes the iterator traverse through the path and record each step in the activePath
            while (pathIterator.hasNext()) {
                int checkStop = pathIterator.next();
                if (checkStop == stop) {
                    break;
                }
                else {
                    potentialReversePath.add(checkStop);
                }
            }
            // now we need to traverse back the list
            //Then, trace back from the finish vertex to the start, by first finding a visited vertex u for which (u, finish) is an edge, then a vertex v visited earlier than u for which (v, u) is an edge, and so on, finally finding a vertex w for which (start, w) is an edge (isAdjacent may be useful here!).
            //somewhere use adjacent
            potentialReversePath.add(stop);
            //don't forget stop wasn't added
            //now I want to iterate backwards
            for (int i = potentialReversePath.size() - 1; i >= 0; i--) {
                int currentSpot = potentialReversePath.get(i);
                if ((currentSpot == start) && isAdjacent(start, stop)) {
                    returnList.add(currentSpot);
                    break;
                }
                else {
                    if (isAdjacent(currentSpot, stop)) {
                        returnList.add(currentSpot);
                        stop = currentSpot;
                    }
                }
            }
            // I did need to reverse the list that I want to return LOL
            //source: https://stackoverflow.com/questions/2102499/iterating-through-a-list-in-reverse-order-in-java
            Collections.reverse(returnList);
        }
        return returnList;
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;

        private int[] specialDegree;

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            // TODO: YOUR CODE HERE
            specialDegree = new int[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                specialDegree[i] = inDegree(i);
                if (inDegree(i) == 0){
                    fringe.push(i);
                }
            }
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            int currentPlace = fringe.pop();
            for (int placeHolder : neighbors(currentPlace)) {
                specialDegree[placeHolder]--;
                if (inDegree(placeHolder) == 0) {
                    fringe.push(placeHolder);
                }
            }
            return currentPlace;
        }
//            for (Edge currentEdge: adjLists[currentPlace]) {
//                specialDegree[currentEdge.to]--;
//                if (inDegree(currentEdge.to) == 0) {
//                    fringe.push(currentEdge.to);
//                }
//            }
//            return currentPlace;

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();
    }
    class comparePaths implements Comparator<Integer> {
        @Override
        public int compare(Integer index1, Integer index2) {
            if (distTo.get(index1) > distTo.get(index2)) {
                return 1;
            } else if (distTo.get(index1) < distTo.get(index2)) {
                return -1;
            }
            return 0;
        }
    }

    int initialSize = adjLists.length;
    HashMap<Integer, Integer> distTo = new HashMap<>();
    HashMap<Integer, Integer> edgeTo = new HashMap<>();
    HashSet<Integer> visited = new HashSet<>();
    public List<Integer> shortestPath(int start, int stop) {
        // TODO: YOUR CODE HERE
        PriorityQueue<Integer> fringe = new PriorityQueue<> (initialSize, new comparePaths());
        for (int i = 0; i < vertexCount; i++){
            if (i == start) {
                distTo.put(i, 0);
            }
            else {
                distTo.put(i, Integer.MAX_VALUE);
            }
        }
        //don't forget the start
        fringe.add(start);
        //while loop for iterating through the path
        while (!fringe.isEmpty()){
            int toAdd = fringe.poll();
            if (!visited.contains(toAdd)) {
                visited.add(toAdd);
                for (Edge edgeTested: adjLists[toAdd]) {
                    fringe.add(edgeTested.to);
                    if (distTo.get(edgeTested.to) > distTo.get(toAdd) + edgeTested.weight) {
                        distTo.put(edgeTested.to, distTo.get(toAdd) + edgeTested.weight);
                        edgeTo.put(edgeTested.to, toAdd);
                    }
                }
            }
        //work backwards to find path
        //helper method
        }

//    public Edge getEdge(int u, int v) {
//        // TODO: YOUR CODE HERE
//        if (isAdjacent(u, v)){
//            return adjLists[u].get(v);
//        }
//        return null;
//    }
        return returnPath(start, stop);
}
    public LinkedList<Integer> returnPath(int startH, int stopH) {
        LinkedList<Integer> helperReturn = new LinkedList<>();
        helperReturn.add(stopH);
        int returnPathHelperVar = edgeTo.get(stopH);
        while (returnPathHelperVar != startH) {
            helperReturn.add(returnPathHelperVar);
            returnPathHelperVar = edgeTo.get(returnPathHelperVar);
        }
        helperReturn.add(startH);
        //need to reverse list
        Collections.reverse(helperReturn);
        return helperReturn;
    }
}
