import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;

/**
 * A JavaFX application for visualizing and simulating percolation systems.
 *
 * <p>This viewer provides:
 * <ul>
 *   <li>Interactive controls to configure grid size and simulation count.</li>
 *   <li>Visual representation of the percolation grid (blocked/open/full sites).</li>
 *   <li>Statistical results including the percolation threshold (p*).</li>
 * </ul>
 *
 * <p><strong>UI Components:</strong>
 * <ul>
 *   <li><strong>Top Panel:</strong> Title and description of percolation theory.</li>
 *   <li><strong>Left Panel:</strong> Scientific context and problem statement.</li>
 *   <li><strong>Center Panel:</strong> Interactive controls and grid visualization.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 * @see Percolation
 * @see ControlPanel
 */
public class PercolationViewer extends Application {
    /** Size of each grid cell in pixels (dynamically calculated). */
    public static double CELL_SIZE;

    /** Canvas for rendering the percolation grid. */
    private Canvas canvas;

    /** Graphics context for drawing on the canvas. */
    private GraphicsContext gc;

    /** Label to display simulation results (e.g., percolation threshold). */
    public Label resultLabel = new Label("Result:");

    /**
     * Initializes and configures the JavaFX application window.
     *
     * @param primaryStage The main window of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);

        // ---- Top Panel: Title and Description ----
        Text title = new Text("Quick-Union algorithm application in Percolation problem");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label description = new Label(
                "Models an n-by-n grid where each site is either open or blocked. " +
                        "A system percolates if there exists a path of open sites connecting " +
                        "the top row to the bottom row."
        );
        description.setWrapText(true);

        VBox topBox = new VBox(5, title, description);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10, 20, 10, 20));
        root.setTop(topBox);

        // ---- Center Panel: Controls and Visualization ----
        ControlPanel control = new ControlPanel();
        canvas = new Canvas(300, 300);
        gc = canvas.getGraphicsContext2D();

        VBox rightBox = new VBox(10, control, canvas, resultLabel);
        rightBox.setPadding(new Insets(10));
        rightBox.setAlignment(Pos.TOP_CENTER);
        root.setCenter(rightBox);

        // ---- Left Panel: Scientific Context ----
        VBox infoPanel = new VBox(10);
        infoPanel.setPadding(new Insets(10));

        Label title2 = new Label("The Problem");
        title2.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label details = new Label(
                "For large n, there exists a threshold p* where:\n" +
                        "- p < p*: System almost never percolates.\n" +
                        "- p > p*: System almost always percolates.\n\n" +
                        "This app estimates p* via Monte Carlo simulation."
        );
        details.setMaxWidth(200);
        details.setWrapText(true);
        infoPanel.getChildren().addAll(title2, details);
        root.setLeft(infoPanel);

        // ---- Simulation Button Action ----
        control.simulateButton.setOnAction(e -> {
            try {
                int size = Integer.parseInt(control.gridSizeInput.getText());
                int simulations = Integer.parseInt(control.simulationCountInput.getText());

                double totalP = 0.0;
                Percolation last = null;

                for (int i = 0; i < simulations; i++) {
                    Percolation p = new Percolation(size);
                    p.simulateUntilPercolated();
                    totalP += p.openSitesRatio();
                    if (i == simulations - 1) last = p;
                }

                double meanP = totalP / simulations;
                resultLabel.setText("Mean threshold (p*): " + String.format("%.4f", meanP));
                drawGrid(last, size);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Error: Invalid input values.");
            }
        });

        Scene scene = new Scene(root, 700, 400);
        primaryStage.setTitle("Percolation Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Draws the percolation grid on the canvas.
     *
     * @param p     The {@link Percolation} model to visualize.
     * @param size  The grid size (n x n).
     */
    private void drawGrid(Percolation p, int size) {
        CELL_SIZE = 240.0 / size;
        canvas.setWidth(size * CELL_SIZE);
        canvas.setHeight(size * CELL_SIZE);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (!p.isOpen(row, col)) {
                    gc.setFill(Color.BLACK);  // Blocked site
                } else if (p.isConnectedTopAndBottom(row, col)) {
                    gc.setFill(Color.BLUE);   // Percolating path
                } else {
                    gc.setFill(Color.DARKGRAY);  // Open but not percolating
                }
                gc.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                gc.setStroke(Color.LIGHTGRAY);
                gc.strokeRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        launch(args);
    }
}