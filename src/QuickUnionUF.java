/**
 * A Union-Find (Disjoint Set Union) data structure that uses the Quick Union algorithm
 * with naive union (without path compression or weighting).
 *
 * <p>This implementation:
 * <ul>
 *   <li>Supports <em>union</em> and <em>find</em> operations in O(N) worst-case time</li>
 *   <li>Represents sets as trees where each node points to its parent</li>
 *   <li>Performs connectivity checks by comparing root nodes</li>
 * </ul>
 *
 * <p><strong>Applications:</strong>
 * <ul>
 *   <li>Percolation problems</li>
 *   <li>Dynamic connectivity in networks</li>
 *   <li>Image processing (pixel connectivity)</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Disjoint-set_data_structure">Disjoint-set Data Structure</a>
 */
public class QuickUnionUF {
    /**
     * Parent-link array where id[i] = parent of i.
     * If i is a root node, id[i] = i.
     */
    private final int[] id;

    /**
     * Initializes a Union-Find structure with N isolated components.
     *
     * @param N The number of elements (must be positive)
     * @throws IllegalArgumentException if N ≤ 0
     */
    public QuickUnionUF(int N) {
        if (N <= 0) throw new IllegalArgumentException("Number of elements must be positive");
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;  // Each element is its own root initially
        }
    }

    /**
     * Finds the root of element i by chasing parent pointers.
     *
     * @param i The element whose root is sought
     * @return The root of element i
     * @throws IndexOutOfBoundsException if i is out of bounds
     */
    private int root(int i) {
        validateIndex(i);
        while (i != id[i]) {
            i = id[i];
        }
        return i;
    }

    /**
     * Checks if elements p and q belong to the same set.
     *
     * @param p First element
     * @param q Second element
     * @return true if p and q are connected, false otherwise
     * @throws IndexOutOfBoundsException if p or q is invalid
     */
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    /**
     * Merges the set containing p with the set containing q.
     *
     * @param p First element
     * @param q Second element
     * @throws IndexOutOfBoundsException if p or q is invalid
     */
    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        id[i] = j;  // Make i's root point to j's root
    }

    /**
     * Validates that index i is within bounds.
     *
     * @throws IndexOutOfBoundsException if i is invalid
     */
    private void validateIndex(int i) {
        if (i < 0 || i >= id.length) {
            throw new IndexOutOfBoundsException("Index " + i + " is out of bounds [0, " + (id.length - 1) + "]");
        }
    }

    /**
     * Demo usage of the QuickUnionUF class.
     *
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        QuickUnionUF uf = new QuickUnionUF(10);

        uf.union(1, 2);
        uf.union(2, 5);
        uf.union(5, 6);

        System.out.println("Are 1 and 6 connected? " + uf.connected(1, 6));  // true
        System.out.println("Are 0 and 6 connected? " + uf.connected(0, 6));  // false
    }
}
