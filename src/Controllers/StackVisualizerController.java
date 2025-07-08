package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class StackVisualizerController {

    @FXML
    private Pane stackPane;

    @FXML
    private TextField inputField;

    @FXML
    private TextField sizeField;

    @FXML
    private TextArea logArea;

    private final int blockWidth = 100;
    private final int blockHeight = 40;

    private int[] stack;
    private int top = -1;
    private int maxSize;

    private final ArrayList<Rectangle> blockRects = new ArrayList<>();
    private final ArrayList<Text> blockLabels = new ArrayList<>();

    public void setStackSize() {
        try {
            maxSize = Integer.parseInt(sizeField.getText().trim());
            stack = new int[maxSize];
            top = -1;
            stackPane.getChildren().clear();
            blockRects.clear();
            blockLabels.clear();
            logArea.setText("Stack initialized with size: " + maxSize + "\n");
        } catch (NumberFormatException e) {
            logArea.appendText("Invalid size input.\n");
        }
    }

    public void push() {
        if (top == maxSize - 1) {
            logArea.appendText("Stack Overflow\n");
            return;
        }
        try {
            int value = Integer.parseInt(inputField.getText().trim());
            top++;
            stack[top] = value;

            double x = 150;
            double y = stackPane.getHeight() - ((top + 1) * blockHeight + 10);

            Rectangle rect = new Rectangle(x, y, blockWidth, blockHeight);
            rect.setFill(Color.SKYBLUE);
            rect.setStroke(Color.BLACK);

            Text label = new Text(String.valueOf(value));
            label.setX(x + 40);
            label.setY(y + 25);

            stackPane.getChildren().addAll(rect, label);
            blockRects.add(rect);
            blockLabels.add(label);

            logArea.appendText("Pushed: " + value + "\n");
            inputField.clear();
        } catch (NumberFormatException e) {
            logArea.appendText("Enter a valid number to push.\n");
        }
    }

    public void pop() {
        if (top == -1) {
            logArea.appendText("Stack Underflow\n");
            return;
        }

        int value = stack[top];

        Rectangle rect = blockRects.remove(top);
        Text label = blockLabels.remove(top);

        Platform.runLater(() -> {
            stackPane.getChildren().removeAll(rect, label);
        });

        top--;
        logArea.appendText("Popped: " + value + "\n");
    }

    public void traverse() {
        if (top == -1) {
            logArea.appendText("Stack is empty.\n");
            return;
        }

        logArea.appendText("Traversing from top to bottom:\n");
        for (int i = top; i >= 0; i--) {
            logArea.appendText(stack[i] + "\n");
        }
    }
}
