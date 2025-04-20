/**
 * A Union-Find (Disjoint Set Union) data structure using the Quick-Find algorithm.
 *
 * <p>This implementation:
 * <ul>
 *   <li>Performs <strong>find</strong> operations in O(1) time</li>
 *   <li>Performs <strong>union</strong> operations in O(N) time</li>
 *   <li>Maintains connectivity by grouping elements with identical IDs</li>
 * </ul>
 *
 * <p><strong>Performance Characteristics:</strong>
 * <table border="1">
 *   <tr><th>Operation</th><th>Time Complexity</th></tr>
 *   <tr><td>find/connected</td><td>O(1)</td></tr>
 *   <tr><td>union</td><td>O(N)</td></tr>
 *   <tr><td>initialization</td><td>O(N)</td></tr>
 * </table>
 *
 * @author YourName
 * @version 1.0
 * @see <a href="https://en.wikipedia.org/wiki/Disjoint-set_data_structure">Disjoint-set Data Structure</a>
 */
public class QuickFindUF {
    /**
     * Component ID array where id[i] = group identifier for element i.
     * All connected elements share the same ID.
     */
    private final int[] id;

    /**
     * Initializes a Union-Find structure with N isolated components.
     *
     * @param N The number of elements (must be positive)
     * @throws IllegalArgumentException if N ≤ 0
     */
    public QuickFindUF(int N) {
        if (N <= 0) throw new IllegalArgumentException("Number of elements must be positive");
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;  // Each element is its own component initially
        }
    }

    /**
     * Checks if elements p and q belong to the same component.
     *
     * @param p First element (0 ≤ p < N)
     * @param q Second element (0 ≤ q < N)
     * @return true if p and q are connected
     * @throws IndexOutOfBoundsException if p or q is invalid
     */
    public boolean connected(int p, int q) {
        validateIndex(p);
        validateIndex(q);
        return id[p] == id[q];
    }

    /**
     * Merges the component containing p with the component containing q.
     *
     * @param p First element (0 ≤ p < N)
     * @param q Second element (0 ≤ q < N)
     * @throws IndexOutOfBoundsException if p or q is invalid
     */
    public void union(int p, int q) {
        validateIndex(p);
        validateIndex(q);
        int pid = id[p];
        int qid = id[q];

        if (pid == qid) return;  // Already connected

        // Linear scan to update all group IDs (O(N) operation)
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }

    /**
     * Validates that index i is within bounds.
     *
     * @throws IndexOutOfBoundsException if i is invalid
     */
    private void validateIndex(int i) {
        if (i < 0 || i >= id.length) {
            throw new IndexOutOfBoundsException(
                    String.format("Index %d is out of bounds [0, %d]", i, id.length - 1)
            );
        }
    }

    /**
     * Demonstrates basic usage of the QuickFindUF class.
     *
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        QuickFindUF uf = new QuickFindUF(10);  // Create structure with 10 elements

        // Union operations
        uf.union(1, 2);
        uf.union(2, 5);
        uf.union(5, 9);

        // Connectivity checks
        System.out.println("Are 1 and 9 connected? " + uf.connected(1, 9));  // true
        System.out.println("Are 3 and 4 connected? " + uf.connected(3, 4));  // false
        System.out.println("Are 0 and 1 connected? " + uf.connected(0, 1));  // false
    }
}