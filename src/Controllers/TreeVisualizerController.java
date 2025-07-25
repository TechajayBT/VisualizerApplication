package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class TreeVisualizerController {

    @FXML
    private TextField inputField;

    @FXML
    private TextArea outputArea;

    @FXML
    private Pane treePane;

    private class Node {
        int data;
        Node left, right;
        double x, y;

        Node(int value) {
            data = value;
        }
    }

    private Node root;
    private final double NODE_RADIUS = 20;
    private final double V_GAP = 70;

    private int xIndex = 0; // for inorder x assignment

    public void handleInsert() {
        try {
            int value = Integer.parseInt(inputField.getText());
            root = insert(root, value);
            inputField.clear();
            drawTree();
        } catch (NumberFormatException e) {
            outputArea.setText("Please enter a valid number.");
        }
    }

    public void handleInorder() {
        outputArea.setText("Inorder: " + inorder(root));
    }

    public void handlePreorder() {
        outputArea.setText("Preorder: " + preorder(root));
    }

    public void handlePostorder() {
        outputArea.setText("Postorder: " + postorder(root));
    }

    private Node insert(Node node, int value) {
        if (node == null)
            return new Node(value);
        if (value < node.data)
            node.left = insert(node.left, value);
        else
            node.right = insert(node.right, value);
        return node;
    }

    private String inorder(Node node) {
        if (node == null) return "";
        return inorder(node.left) + node.data + " " + inorder(node.right);
    }

    private String preorder(Node node) {
        if (node == null) return "";
        return node.data + " " + preorder(node.left) + preorder(node.right);
    }

    private String postorder(Node node) {
        if (node == null) return "";
        return postorder(node.left) + postorder(node.right) + node.data + " ";
    }

    private int getHeight(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private void drawTree() {
        treePane.getChildren().clear();
        xIndex = 0;
        assignCoordinates(root, 0);
        drawEdges(root);
        drawNodes(root);
    }

    // First pass: assign x based on inorder position, y based on level
    private void assignCoordinates(Node node, int depth) {
        if (node == null) return;

        assignCoordinates(node.left, depth + 1);

        double paneWidth = treePane.getPrefWidth();
        int totalNodes = countNodes(root);
        double spacing = paneWidth / (totalNodes + 1); // safe spacing
        node.x = (xIndex + 1) * spacing;
        node.y = (depth + 1) * V_GAP;
        xIndex++;

        assignCoordinates(node.right, depth + 1);
    }

    private int countNodes(Node node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private void drawEdges(Node node) {
        if (node == null) return;

        if (node.left != null)
            drawLine(node.x, node.y, node.left.x, node.left.y);

        if (node.right != null)
            drawLine(node.x, node.y, node.right.x, node.right.y);

        drawEdges(node.left);
        drawEdges(node.right);
    }

    private void drawNodes(Node node) {
        if (node == null) return;

        Circle circle = new Circle(node.x, node.y, NODE_RADIUS);
        circle.setStyle("-fx-fill: #3498db; -fx-stroke: black;");
        Text text = new Text(node.x - 7, node.y + 4, String.valueOf(node.data));
        treePane.getChildren().addAll(circle, text);

        drawNodes(node.left);
        drawNodes(node.right);
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        treePane.getChildren().add(line);
    }
}
