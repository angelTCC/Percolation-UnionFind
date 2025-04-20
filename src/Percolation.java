import java.util.*;

/**
 * Models a percolation system using an N-by-N grid of sites. A system percolates
 * if there exists a path of open sites connecting the top row to the bottom row.
 *
 * <p>This implementation uses:
 * <ul>
 *   <li>A boolean matrix to track open/blocked sites.</li>
 *   <li>Weighted Quick-Union with Path Compression (QuickUnionUF) for efficient connectivity checks.</li>
 *   <li>Virtual top/bottom nodes to simplify percolation detection.</li>
 * </ul>
 *
 * <p>For theoretical context, see:
 * <a href="https://en.wikipedia.org/wiki/Percolation_theory">Percolation Theory</a>.
 *
 * @author YourName
 * @version 1.0
 */
public class Percolation {
    // --------------------- Constants & Fields ---------------------
    /** Size of the grid (N x N). */
    private final int n;

    /** Grid representation: {@code grid[row][col] = true} iff the site is open. */
    private final boolean[][] grid;

    /** Union-Find structure for tracking connectivity. */
    private final QuickUnionUF uf;

    /** Virtual node index connected to all top-row sites. */
    private final int topVirtual;

    /** Virtual node index connected to all bottom-row sites. */
    private final int bottomVirtual;

    /** Count of open sites (0 ≤ openSitesCount ≤ n²). */
    private int openSitesCount = 0;

    // --------------------- Constructor ---------------------
    /**
     * Initializes an N-by-N grid with all sites blocked.
     *
     * @param n Grid size. Must be positive.
     * @throws IllegalArgumentException if {@code n ≤ 0}.
     */
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("Grid size must be positive.");
        this.n = n;
        this.grid = new boolean[n][n];  // All sites initialized to blocked (false)
        this.uf = new QuickUnionUF(n * n + 2);  // +2 for virtual nodes
        this.topVirtual = n * n;
        this.bottomVirtual = n * n + 1;
    }

    // --------------------- Core Methods ---------------------
    /**
     * Opens the site at {@code (row, col)} if it is not already open and connects it
     * to adjacent open sites. If the site is in the top/bottom row, connects it to
     * the corresponding virtual node.
     *
     * @param row Row index (0-based, 0 ≤ row < n).
     * @param col Column index (0-based, 0 ≤ col < n).
     * @throws IndexOutOfBoundsException if indices are out of bounds.
     */
    public void open(int row, int col) {
        validateBounds(row, col);
        if (isOpen(row, col)) return;

        grid[row][col] = true;
        openSitesCount++;

        int current = index(row, col);
        // Connect to virtual nodes if in top/bottom row
        if (row == 0) uf.union(current, topVirtual);
        if (row == n - 1) uf.union(current, bottomVirtual);

        // Connect to open neighbors (up, down, left, right)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int adjRow = row + dir[0], adjCol = col + dir[1];
            if (isValid(adjRow, adjCol) && isOpen(adjRow, adjCol)) {
                uf.union(current, index(adjRow, adjCol));
            }
        }
    }

    /**
     * Checks if the site at {@code (row, col)} is open.
     *
     * @param row Row index (0-based).
     * @param col Column index (0-based).
     * @return {@code true} if open, {@code false} if blocked.
     * @throws IndexOutOfBoundsException if indices are invalid.
     */
    public boolean isOpen(int row, int col) {
        validateBounds(row, col);
        return grid[row][col];
    }

    /**
     * Checks if the system percolates (i.e., top and bottom virtual nodes are connected).
     *
     * @return {@code true} if percolates, {@code false} otherwise.
     */
    public boolean percolates() {
        return uf.connected(topVirtual, bottomVirtual);
    }

    // --------------------- Helper Methods ---------------------
    /**
     * Converts 2D (row, col) coordinates to a 1D index for Union-Find.
     * @throws IndexOutOfBoundsException if indices are invalid.
     */
    private int index(int row, int col) {
        validateBounds(row, col);
        return row * n + col;
    }

    /** Validates that (row, col) is within grid bounds. */
    private void validateBounds(int row, int col) {
        if (!isValid(row, col)) {
            throw new IndexOutOfBoundsException(
                    String.format("(%d, %d) is outside grid bounds [0, %d)", row, col, n)
            );
        }
    }

    /** Checks if (row, col) is a valid grid index. */
    private boolean isValid(int row, int col) {
        return row >= 0 && row < n && col >= 0 && col < n;
    }

    // --------------------- Simulation & Analysis ---------------------
    /**
     * Simulates random site openings until the system percolates and prints the threshold.
     * The percolation threshold is the fraction of open sites when percolation occurs.
     */
    public void simulateUntilPercolated() {
        List<int[]> sites = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sites.add(new int[]{i, j});
            }
        }
        Collections.shuffle(sites);

        for (int[] site : sites) {
            int row = site[0], col = site[1];
            if (!isOpen(row, col)) {
                open(row, col);
                if (percolates()) {
                    System.out.printf(
                            "System percolates after opening %d sites (threshold = %.4f)%n",
                            openSitesCount, openSitesRatio()
                    );
                    break;
                }
            }
        }
    }

    /**
     * Returns the fraction of open sites relative to total sites.
     * @return A value in [0.0, 1.0].
     */
    public double openSitesRatio() {
        return (double) openSitesCount / (n * n);
    }

    public boolean isConnectedTopAndBottom(int row, int col) {
        if (!isOpen(row, col)) return false;
        int idx = index(row, col);
        return uf.connected(idx, topVirtual) && uf.connected(idx, bottomVirtual);
    }
}
