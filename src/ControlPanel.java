import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

/**
 * A JavaFX control panel for configuring and triggering percolation simulations.
 *
 * <p>This panel provides interactive UI components to:
 * <ul>
 *   <li>Set the grid size (N x N) for the percolation model.</li>
 *   <li>Specify the number of Monte Carlo simulations to run.</li>
 *   <li>Initiate simulations with a dedicated button.</li>
 * </ul>
 *
 * <p><strong>UI Structure:</strong>
 * <ul>
 *   <li><strong>Grid Size Input:</strong> Text field for setting N (default: 10).</li>
 *   <li><strong>Simulation Count Input:</strong> Text field for setting iterations (default: 100).</li>
 *   <li><strong>Simulate Button:</strong> Triggers the simulation when clicked.</li>
 * </ul>
 *
 * @author YourName
 * @version 1.0
 * @see PercolationViewer
 * @see Percolation
 */
public class ControlPanel extends VBox {
    /** Input field for grid size (N x N). */
    public TextField gridSizeInput;

    /** Input field for number of simulations. */
    public TextField simulationCountInput;

    /** Button to initiate simulations. */
    public Button simulateButton;

    /**
     * Constructs a control panel with default input values and layout.
     *
     * <p>The panel includes:
     * <ul>
     *   <li>Labeled text fields for grid size and simulation count.</li>
     *   <li>A centrally aligned "Simulate" button.</li>
     * </ul>
     */
    public ControlPanel() {
        super(10); // Vertical spacing between components
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_CENTER);

        // Grid size input
        Label gridSizeLabel = new Label("Grid Size (N):");
        gridSizeInput = new TextField("10");
        gridSizeInput.setTooltip(new Tooltip("Enter the grid dimension (e.g., 10 for 10x10)"));
        VBox gridSizeBox = new VBox(5, gridSizeLabel, gridSizeInput);

        // Simulation count input
        Label simCountLabel = new Label("Simulation Count:");
        simulationCountInput = new TextField("100");
        simulationCountInput.setTooltip(new Tooltip("Number of Monte Carlo iterations"));
        VBox simCountBox = new VBox(5, simCountLabel, simulationCountInput);

        // Simulation button
        simulateButton = new Button("Simulate");
        simulateButton.setStyle("-fx-base: #4CAF50;"); // Green color for action

        // Horizontal layout for inputs and button
        HBox inputRow = new HBox(20, gridSizeBox, simCountBox, simulateButton);
        inputRow.setAlignment(Pos.CENTER);

        getChildren().add(inputRow);
    }
}