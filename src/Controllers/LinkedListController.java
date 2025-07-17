package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class LinkedListController {

    @FXML private TextField valueInput;
    @FXML private TextField indexInput;
    @FXML private HBox linkedListDisplay;

    private int nodeCount = 0;

    public void handleInsertAtFirst() {
        insertNodeAt(0);
    }

    public void handleInsertAtLast() {
        insertNodeAt(nodeCount);
    }

    public void handleInsertAtIndex() {
        int index = parseIndex();
        if (index < 0 || index > nodeCount) {
            showError("Invalid index");
            return;
        }
        insertNodeAt(index);
    }

    public void handleDeleteAtFirst() {
        if (nodeCount > 0) deleteNodeAt(0);
    }

    public void handleDeleteAtIndex() {
        int index = parseIndex();
        if (index < 0 || index >= nodeCount) {
            showError("Invalid index");
            return;
        }
        deleteNodeAt(index);
    }

    public void handleTraverse() {
        if (nodeCount == 0 || linkedListDisplay.getChildren().isEmpty()) {
            showInfo("Traversal: Linked list is empty.");
            return;
        }

        StringBuilder result = new StringBuilder("Traversal: ");
        for (int i = 0; i < linkedListDisplay.getChildren().size(); i += 2) {
            VBox node = (VBox) linkedListDisplay.getChildren().get(i);
            Label val = (Label) node.getChildren().get(0);
            result.append(val.getText()).append(" ");
        }
        showInfo(result.toString());
    }


    private void insertNodeAt(int index) {
        String value = valueInput.getText().trim();
        if (value.isEmpty()) return;

        // Remove trailing null if present
        removeTrailingNull();

        // Create node box
        VBox nodeBox = new VBox(5);
        nodeBox.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-alignment: center;");
        Label valueLabel = new Label(value);
        valueLabel.setFont(new Font(16));
        nodeBox.getChildren().add(valueLabel);

        // Create arrow box
        VBox arrowBox = new VBox();
        arrowBox.setStyle("-fx-alignment: center;");
        Label arrow = new Label("→");
        arrow.setFont(new Font(18));
        arrow.setTextFill(Color.DARKGRAY);
        arrowBox.getChildren().add(arrow);

        if (index == nodeCount) {
            linkedListDisplay.getChildren().add(nodeBox);
            linkedListDisplay.getChildren().add(arrowBox);
        } else {
            linkedListDisplay.getChildren().add(index * 2, nodeBox);
            linkedListDisplay.getChildren().add(index * 2 + 1, arrowBox);
        }

        nodeCount++;
        valueInput.clear();

        addNullNode(); // Always end with null
    }

    private void deleteNodeAt(int index) {
        if (index < 0 || index >= nodeCount) return;

        // Remove trailing null
        removeTrailingNull();

        // Remove node and its arrow
        linkedListDisplay.getChildren().remove(index * 2); // Remove node
        if (index * 2 < linkedListDisplay.getChildren().size()) {
            linkedListDisplay.getChildren().remove(index * 2); // Remove arrow
        } else if (!linkedListDisplay.getChildren().isEmpty()) {
            linkedListDisplay.getChildren().remove(linkedListDisplay.getChildren().size() - 1);
        }

        nodeCount--;

        // Add back null if there are still nodes
        if (nodeCount > 0) addNullNode();
    }

    private void addNullNode() {
        VBox nullBox = new VBox();
        nullBox.setStyle("-fx-alignment: center;");
        Label nullLabel = new Label("null");
        nullLabel.setTextFill(Color.GRAY);
        nullLabel.setFont(new Font(16));
        nullBox.getChildren().add(nullLabel);
        linkedListDisplay.getChildren().add(nullBox);
    }

    private void removeTrailingNull() {
        int size = linkedListDisplay.getChildren().size();
        if (size > 0) {
            VBox lastBox = (VBox) linkedListDisplay.getChildren().get(size - 1);
            if (lastBox.getChildren().size() == 1 &&
                    ((Label) lastBox.getChildren().get(0)).getText().equals("null")) {
                linkedListDisplay.getChildren().remove(size - 1);
            }
        }
    }

    private int parseIndex() {
        try {
            return Integer.parseInt(indexInput.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void showError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Traversal");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
