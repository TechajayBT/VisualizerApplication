package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class ArrayVisualizerController {

    @FXML
    private TextField sizeInput;

    @FXML
    private VBox valueInputContainer;

    @FXML
    private VBox finalArrayDisplay;

    @FXML
    private Button submitButton;

    private List<TextField> valueFields = new ArrayList<>();

    @FXML
    public void handleCreateArray(ActionEvent event) {
        valueInputContainer.getChildren().clear();
        finalArrayDisplay.getChildren().clear();
        valueFields.clear();
        submitButton.setVisible(false);

        try {
            int size = Integer.parseInt(sizeInput.getText());

            HBox valuesBox = new HBox(10);
            for (int i = 0; i < size; i++) {
                TextField valueField = new TextField();
                valueField.setPromptText("[" + i + "]");
                valueField.setPrefWidth(50);
                valuesBox.getChildren().add(valueField);
                valueFields.add(valueField);
            }

            valueInputContainer.getChildren().add(valuesBox);
            submitButton.setVisible(true);

        } catch (NumberFormatException e) {
            System.out.println("Invalid size.");
        }
    }

    @FXML
    public void handleSubmitValues(ActionEvent event) {
        finalArrayDisplay.getChildren().clear();
        HBox indexRow = new HBox(10);
        for (int i = 0; i < valueFields.size(); i++) {
            Label indexLabel = new Label(" " + i + " ");
            indexLabel.setMinSize(40, 30);
            indexLabel.setStyle("-fx-border-color: black; -fx-alignment: center; -fx-font-weight: bold;");
            indexLabel.setAlignment(javafx.geometry.Pos.CENTER);
            indexRow.getChildren().add(indexLabel);
        }
        HBox valueRow = new HBox(10);
        for (TextField field : valueFields) {
            Label valueLabel = new Label(field.getText());
            valueLabel.setMinSize(40, 30);
            valueLabel.setStyle("-fx-border-color: black; -fx-alignment: center;");
            valueLabel.setAlignment(javafx.geometry.Pos.CENTER);
            valueRow.getChildren().add(valueLabel);
        }
        finalArrayDisplay.getChildren().addAll(indexRow, valueRow);
    }


}
