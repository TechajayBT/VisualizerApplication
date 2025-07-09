package Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class MergeSort {

    @FXML
    private Pane barContainer;

    @FXML
    private TextField inputField;

    private int[] originalArray;
    private int[] workingArray;
    private final int delay = 500; 

    // Responsive constants
    private double verticalGap = 80;
    private double nodeHeight = 40;
    private double minNodeWidth = 60;
    private double containerMargin = 50;
    private double minHorizontalGap = 30;

    private int stepCounter = 0;
    private double currentContainerWidth = 800;
    private double currentContainerHeight = 600;

    // Flag to prevent multiple concurrent sorts
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public void generateArray() {
        String input = inputField.getText().trim();
        if (input.isEmpty()) {
            input = "64,34,25,12,22,11,90"; // Default example
        }

        String[] parts = input.split(",");

        try {
            originalArray = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                originalArray[i] = Integer.parseInt(parts[i].trim());
            }

            // Clear previous visualization
            Platform.runLater(() -> {
                barContainer.getChildren().clear();
                // Update responsive parameters after clearing
                updateResponsiveParameters();
                // Show original array
                showOriginalArray();
            });

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter comma-separated integers.");
        }
    }

    public void startMergeSort() {
        if (originalArray != null && !isRunning.get()) {
            isRunning.set(true);

            new Thread(() -> {
                try {
                    // Update responsive parameters on background thread
                    updateResponsiveParametersBackground();

                    // Clear previous visualization
                    Platform.runLater(() -> barContainer.getChildren().clear());

                    // Small delay to ensure UI is cleared
                    Thread.sleep(100);

                    // Create working copy
                    workingArray = Arrays.copyOf(originalArray, originalArray.length);
                    stepCounter = 0;

                    // Calculate tree depth for responsive sizing
                    int treeDepth = calculateTreeDepth(originalArray.length);

                    // Adjust vertical spacing based on container height and tree depth
                    double requiredHeight = treeDepth * (nodeHeight + verticalGap) + 200;
                    if (requiredHeight > currentContainerHeight) {
                        verticalGap = Math.max(40, (currentContainerHeight - 200 - (treeDepth * nodeHeight)) / treeDepth);
                    }

                    // Get container dimensions
                    double availableWidth = currentContainerWidth - (2 * containerMargin);
                    double rootX = currentContainerWidth / 2.0;
                    double rootY = 80;

                    // Start visualization with responsive positioning
                    TreeNodeGraphic result = mergeSort(0, workingArray.length - 1, rootX, rootY, availableWidth);

                    // Show final sorted array
                    showFinalArray();

                } catch (InterruptedException e) {
                    System.out.println("Merge sort visualization interrupted");
                } finally {
                    isRunning.set(false);
                }
            }).start();
        }
    }

    private void updateResponsiveParameters() {
        // Get current container dimensions - simplified version
        if (barContainer.getScene() != null) {
            currentContainerWidth = barContainer.getScene().getWidth();
            currentContainerHeight = barContainer.getScene().getHeight();
        } else if (barContainer.getWidth() > 0) {
            currentContainerWidth = barContainer.getWidth();
            currentContainerHeight = barContainer.getHeight();
        } else {
            currentContainerWidth = Math.max(barContainer.getPrefWidth(), 800);
            currentContainerHeight = Math.max(barContainer.getPrefHeight(), 600);
        }

        // Ensure minimum dimensions
        if (currentContainerWidth < 400) currentContainerWidth = 800;
        if (currentContainerHeight < 300) currentContainerHeight = 600;

        // Adjust parameters based on container size
        if (currentContainerWidth < 600) {
            minNodeWidth = 40;
            minHorizontalGap = 15;
            containerMargin = 20;
            nodeHeight = 30;
        } else if (currentContainerWidth < 1000) {
            minNodeWidth = 50;
            minHorizontalGap = 25;
            containerMargin = 30;
            nodeHeight = 35;
        } else {
            minNodeWidth = 60;
            minHorizontalGap = 30;
            containerMargin = 50;
            nodeHeight = 40;
        }

        // Adjust based on array size
        if (originalArray != null) {
            int arraySize = originalArray.length;
            if (arraySize > 10) {
                minHorizontalGap = Math.max(15, minHorizontalGap * 0.7);
                verticalGap = Math.max(50, verticalGap * 0.8);
            } else if (arraySize < 5) {
                minHorizontalGap = Math.max(minHorizontalGap, 40);
                verticalGap = Math.max(verticalGap, 100);
            }
        }
    }

    private void updateResponsiveParametersBackground() {
        // Simplified version that doesn't use Platform.runLater with CountDownLatch
        try {
            // Use existing dimensions or reasonable defaults
            if (currentContainerWidth < 400) currentContainerWidth = 800;
            if (currentContainerHeight < 300) currentContainerHeight = 600;

            // Adjust parameters based on container size
            if (currentContainerWidth < 600) {
                minNodeWidth = 40;
                minHorizontalGap = 15;
                containerMargin = 20;
                nodeHeight = 30;
            } else if (currentContainerWidth < 1000) {
                minNodeWidth = 50;
                minHorizontalGap = 25;
                containerMargin = 30;
                nodeHeight = 35;
            } else {
                minNodeWidth = 60;
                minHorizontalGap = 30;
                containerMargin = 50;
                nodeHeight = 40;
            }

            // Adjust based on array size
            if (originalArray != null) {
                int arraySize = originalArray.length;
                if (arraySize > 10) {
                    minHorizontalGap = Math.max(15, minHorizontalGap * 0.7);
                    verticalGap = Math.max(50, verticalGap * 0.8);
                } else if (arraySize < 5) {
                    minHorizontalGap = Math.max(minHorizontalGap, 40);
                    verticalGap = Math.max(verticalGap, 100);
                }
            }
        } catch (Exception e) {
            System.out.println("Error updating responsive parameters: " + e.getMessage());
        }
    }

    private int calculateTreeDepth(int arraySize) {
        return (int) Math.ceil(Math.log(arraySize) / Math.log(2));
    }

    private void showOriginalArray() {
        String arrayText = "Original Array: " + Arrays.toString(originalArray);
        Text title = new Text(containerMargin, 30, arrayText);

        // Responsive font size
        int fontSize = currentContainerWidth < 600 ? 12 : 16;
        title.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        title.setFill(Color.DARKBLUE);

        // Wrap text if too long
        if (arrayText.length() * 8 > currentContainerWidth - 2 * containerMargin) {
            title.setWrappingWidth(currentContainerWidth - 2 * containerMargin);
        }

        barContainer.getChildren().add(title);
    }

    private void showFinalArray() {
        Platform.runLater(() -> {
            String arrayText = "Final Sorted Array: " + Arrays.toString(workingArray);
            double yPosition = Math.max(currentContainerHeight - 50, 500);

            Text title = new Text(containerMargin, yPosition, arrayText);

            // Responsive font size
            int fontSize = currentContainerWidth < 600 ? 12 : 16;
            title.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
            title.setFill(Color.DARKGREEN);

            // Wrap text if too long
            if (arrayText.length() * 8 > currentContainerWidth - 2 * containerMargin) {
                title.setWrappingWidth(currentContainerWidth - 2 * containerMargin);
            }

            barContainer.getChildren().add(title);
        });
    }

    private TreeNodeGraphic mergeSort(int left, int right, double x, double y, double availableWidth) throws InterruptedException {
        stepCounter++;

        // Create subarray for current range
        int[] currentSubarray = Arrays.copyOfRange(workingArray, left, right + 1);

        if (left == right) {
            // Base case: single element
            TreeNodeGraphic leaf = createTreeNode(currentSubarray, x, y, Color.LIGHTGREEN, "Leaf");
            Thread.sleep(delay);
            return leaf;
        }

        // Create node for current unsorted subarray
        TreeNodeGraphic currentNode = createTreeNode(currentSubarray, x, y, Color.LIGHTCORAL, "Divide");
        Thread.sleep(delay);

        int mid = (left + right) / 2;
        double childY = y + verticalGap;

        // Calculate positions for child nodes
        double spacing = Math.max(availableWidth / 4, minHorizontalGap * 2);
        double leftChildX = Math.max(containerMargin + minNodeWidth/2, x - spacing);
        double rightChildX = Math.min(currentContainerWidth - containerMargin - minNodeWidth/2, x + spacing);

        // Calculate available width for child subtrees
        double childAvailableWidth = Math.max(100, availableWidth / 2);

        // Recursively sort left and right halves
        TreeNodeGraphic leftNode = mergeSort(left, mid, leftChildX, childY, childAvailableWidth);
        TreeNodeGraphic rightNode = mergeSort(mid + 1, right, rightChildX, childY, childAvailableWidth);

        // Merge the sorted halves
        int[] merged = mergeArrays(left, mid, right);

        // Create merged result node
        TreeNodeGraphic mergedNode = createTreeNode(merged, x, y + 15, Color.LIGHTBLUE, "Merged");

        // Draw connections
        Platform.runLater(() -> {
            try {
                drawLine(currentNode, leftNode, Color.RED);
                drawLine(currentNode, rightNode, Color.RED);
                drawLine(mergedNode, leftNode, Color.BLUE);
                drawLine(mergedNode, rightNode, Color.BLUE);
            } catch (Exception e) {
                System.out.println("Error drawing lines: " + e.getMessage());
            }
        });

        Thread.sleep(delay);
        return mergedNode;
    }

    private int[] mergeArrays(int left, int mid, int right) {
        int[] leftArray = Arrays.copyOfRange(workingArray, left, mid + 1);
        int[] rightArray = Arrays.copyOfRange(workingArray, mid + 1, right + 1);

        int[] result = new int[right - left + 1];
        int i = 0, j = 0, k = 0;

        // Merge process
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                result[k++] = leftArray[i++];
            } else {
                result[k++] = rightArray[j++];
            }
        }

        // Copy remaining elements
        while (i < leftArray.length) {
            result[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            result[k++] = rightArray[j++];
        }

        // Update working array
        System.arraycopy(result, 0, workingArray, left, result.length);
        return result;
    }

    private TreeNodeGraphic createTreeNode(int[] subarray, double x, double y, Color color, String label) {
        try {
            String textValue = arrayToString(subarray);

            // Calculate responsive node dimensions
            double charWidth = currentContainerWidth < 600 ? 6 : 8;
            double textWidth = textValue.length() * charWidth;
            double padding = currentContainerWidth < 600 ? 15 : 20;
            double boxWidth = Math.max(textWidth + padding, minNodeWidth);

            // Create the visual components
            Pane group = new Pane();

            // Create rectangle
            Rectangle box = new Rectangle(boxWidth, nodeHeight, color);
            box.setArcWidth(8);
            box.setArcHeight(8);
            box.setStroke(Color.BLACK);
            box.setStrokeWidth(1);

            // Create text with responsive font
            Text text = new Text(textValue);
            text.setX(boxWidth / 2 - textWidth / 2);
            text.setY(nodeHeight / 2 + 4);
            text.setFill(Color.BLACK);

            int fontSize = currentContainerWidth < 600 ? 10 : 12;
            text.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));

            // Create label with responsive font
            Text labelText = new Text(label);
            labelText.setX(boxWidth / 2 - label.length() * (fontSize * 0.3));
            labelText.setY(-4);
            labelText.setFill(Color.DARKBLUE);
            labelText.setFont(Font.font("Arial", FontWeight.BOLD, Math.max(8, fontSize - 2)));

            group.getChildren().addAll(box, text, labelText);
            group.setLayoutX(x - boxWidth / 2);
            group.setLayoutY(y);

            TreeNodeGraphic nodeGraphic = new TreeNodeGraphic(group, x, y, boxWidth);

            // Add to container on JavaFX thread
            Platform.runLater(() -> {
                try {
                    barContainer.getChildren().add(group);
                } catch (Exception e) {
                    System.out.println("Error adding node to container: " + e.getMessage());
                }
            });

            return nodeGraphic;

        } catch (Exception e) {
            System.out.println("Error creating tree node: " + e.getMessage());
            return null;
        }
    }

    private void drawLine(TreeNodeGraphic parent, TreeNodeGraphic child, Color color) {
        try {
            double startX = parent.x;
            double startY = parent.y + nodeHeight;

            double endX = child.x;
            double endY = child.y;

            Line line = new Line(startX, startY, endX, endY);
            line.setStroke(color);
            line.setStrokeWidth(currentContainerWidth < 600 ? 1 : 1.5);

            barContainer.getChildren().add(line);
        } catch (Exception e) {
            System.out.println("Error drawing line: " + e.getMessage());
        }
    }

    private String arrayToString(int[] arr) {
        if (arr.length == 1) {
            return String.valueOf(arr[0]);
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private static class TreeNodeGraphic {
        Pane group;
        double x;
        double y;
        double width;

        TreeNodeGraphic(Pane group, double x, double y, double width) {
            this.group = group;
            this.x = x;
            this.y = y;
            this.width = width;
        }
    }
}
