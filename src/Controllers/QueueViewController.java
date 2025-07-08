package Controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class QueueViewController {

    @FXML
    private TextField inputField;

    @FXML
    private HBox queueHBox;

    private QueueController queueController;

    @FXML
    public void initialize() {
        queueController = new QueueController();
        queueController.setUpdateListener(updatedQueue -> {
            queueHBox.getChildren().clear();

            if (!updatedQueue.isEmpty()) {
                Label frontLabel = new Label("FRONT →");
                frontLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: green; -fx-padding: 10;");
                queueHBox.getChildren().add(frontLabel);
            }

            for (int i = 0; i < updatedQueue.size(); i++) {
                Integer val = updatedQueue.get(i);
                Label label = new Label(String.valueOf(val));
                label.setStyle(
                        "-fx-border-color: black; " +
                                "-fx-border-width: 2; " +
                                "-fx-padding: 20; " +
                                "-fx-min-width: 50; " +
                                "-fx-alignment: center; " +
                                "-fx-background-color: lightblue; " +
                                "-fx-font-weight: bold; " +
                                "-fx-font-size: 14px;"
                );
                queueHBox.getChildren().add(label);

                if (i < updatedQueue.size() - 1) {
                    Label arrow = new Label("→");
                    arrow.setStyle("-fx-font-size: 18px; -fx-text-fill: gray; -fx-padding: 10;");
                    queueHBox.getChildren().add(arrow);
                }
            }

            if (!updatedQueue.isEmpty()) {
                Label rearLabel = new Label("← REAR");
                rearLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: red; -fx-padding: 10;");
                queueHBox.getChildren().add(rearLabel);
            }
        });
    }

    @FXML
    private void handleEnqueue() {
        try {
            int value = Integer.parseInt(inputField.getText());
            queueController.enqueue(value);
            inputField.clear();
        } catch (NumberFormatException e) {
            inputField.clear();
            inputField.setPromptText("Enter a valid number!");
        }
    }

    @FXML
    private void handleDequeue() {
        queueController.dequeue();
    }
}
