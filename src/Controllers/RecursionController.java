package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.LinkedList;
import java.util.Stack;

public class RecursionController {

    @FXML
    private TextField inputField;

    @FXML
    private Pane treePane;

    @FXML
    private Label resultLabel;

    private static class FactNode {
        int n;
        int x, y;
        int value = -1;
        FactNode child;
        Rectangle box;
        Text label;
        Line lineToChild;
    }

    private int originalN;
    private int buildCounter;

    private FactNode root;
    private FactNode currentBuilder;

    private boolean isBuilding = true;
    private boolean isEvaluating = false;

    private Stack<FactNode> evalStack = new Stack<>();

    public void handleStart() {
        try {
            originalN = Integer.parseInt(inputField.getText());
            if (originalN < 0 || originalN > 7) {
                inputField.setText("Enter 0 ≤ n ≤ 7");
                return;
            }

            treePane.getChildren().clear();
            resultLabel.setText("");
            buildCounter = originalN;
            isBuilding = true;
            isEvaluating = false;
            evalStack.clear();

            root = new FactNode();
            root.n = buildCounter;
            root.x = 570;
            root.y = 40;
            drawNode(root);
            currentBuilder = root;

        } catch (NumberFormatException e) {
            inputField.setText("Invalid input");
        }
    }

    public void handleStep() {
        if (isBuilding) {
            if (buildCounter == 0) {
                // Reached fact(0), done building
                isBuilding = false;
                isEvaluating = true;
                prepareEvalStack(root);
                return;
            }

            buildCounter--;
            FactNode child = new FactNode();
            child.n = buildCounter;
            child.x = currentBuilder.x;
            child.y = currentBuilder.y + 80;
            currentBuilder.child = child;

            Line line = new Line(currentBuilder.x, currentBuilder.y + 15, child.x, child.y - 15);
            treePane.getChildren().add(line);

            drawNode(child);
            currentBuilder = child;

        } else if (isEvaluating) {
            if (!evalStack.isEmpty()) {
                FactNode node = evalStack.pop();

                if (node.n == 0) {
                    node.value = 1;
                } else {
                    if (node.child.value == -1) {
                        // Put back current node and evaluate child first
                        evalStack.push(node);
                        evalStack.push(node.child);
                        return;
                    }
                    node.value = node.n * node.child.value;
                }

                node.label.setText("fact(" + node.n + ") = " + node.value);
                node.box.setFill(Color.LIGHTGREEN);

                if (node == root) {
                    resultLabel.setText("Final Result: fact(" + node.n + ") = " + node.value);
                    isEvaluating = false;
                }
            }
        }
    }

    public void handleReset() {
        inputField.clear();
        treePane.getChildren().clear();
        resultLabel.setText("");
        root = null;
        currentBuilder = null;
        isBuilding = true;
        isEvaluating = false;
        evalStack.clear();
    }

    private void drawNode(FactNode node) {
        Rectangle rect = new Rectangle(node.x - 40, node.y - 15, 80, 30);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        rect.setFill(Color.LIGHTBLUE);
        rect.setStroke(Color.BLACK);

        Text label = new Text(node.x - 30, node.y + 5, "fact(" + node.n + ")");
        treePane.getChildren().addAll(rect, label);

        node.box = rect;
        node.label = label;
    }

    private void prepareEvalStack(FactNode node) {
        Stack<FactNode> temp = new Stack<>();
        while (node != null) {
            temp.push(node);
            node = node.child;
        }
        evalStack = temp;
    }
}
