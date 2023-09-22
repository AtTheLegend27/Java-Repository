public class UnionFind {

    /* TODO: Add instance variables here. */
    private int [] helper;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        if (N<= 0) {
            throw new IllegalArgumentException("Incorrect");
        }
            helper = new int[N];
            for (int i = 0; i < N; i++) {
                helper[i] = -1;
            }
        }


    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        // TODO: YOUR CODE HERE
        int base = find(v);
        return -helper[base];
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return helper[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        return find(v1) == find(v2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid items are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        // TODO: YOUR CODE HERE
        if (v < 0 || v >= helper.length) {
            throw new IllegalArgumentException("Failed");
        }
        int base = v;
        int temporaryFunction;
        while (helper[base] >= 0) {
            base = helper[base];
        }
        while (helper[v] >= 0) {
            temporaryFunction = helper[v];
            helper[v] = base;
            v = temporaryFunction;
        }
        return base;
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (!connected(v1, v2)) {
            int var1 = find (v1);
            int var2 = find (v2);
            int totalSum1 = -helper[var1];
            int totalSum2 = -helper[var2];
            if (totalSum1 > totalSum2) {
                helper[var2] = var1;
                helper[var1] = -(totalSum1 + totalSum2);
            } else {
                helper[var1] =  var2;
                helper[var2] = -(totalSum1 + totalSum2);
            }
        }
    }
}
