// MergeSort.java
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

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MergeSort {

    @FXML
    private Pane barContainer;

    @FXML
    private TextField inputField;

    private int[] originalArray;
    private int[] workingArray;
    private final int delay = 500;

    private double verticalGap = 80;
    private double nodeHeight = 40;
    private double minNodeWidth = 60;
    private double containerMargin = 50;
    private double minHorizontalGap = 30;

    private int stepCounter = 0;
    private double currentContainerWidth = 800;
    private double currentContainerHeight = 600;

    private AtomicBoolean isRunning = new AtomicBoolean(false);

    // Step mode additions
    private boolean stepMode = false;
    private int currentStepIndex = 0;
    private List<Runnable> mergeSteps = new ArrayList<>();

    public void generateArray() {
        if (isRunning.get()) return;
        String[] tokens = inputField.getText().split(",");
        try {
            originalArray = Arrays.stream(tokens)
                    .map(String::trim)
                    .mapToInt(Integer::parseInt)
                    .toArray();
            workingArray = Arrays.copyOf(originalArray, originalArray.length);
            updateResponsiveParameters();
            barContainer.getChildren().clear();
            showOriginalArray();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format.");
        }
    }

    public void startMergeSort() {
        if (originalArray == null || isRunning.get()) return;

        isRunning.set(true);
        stepMode = false;
        workingArray = Arrays.copyOf(originalArray, originalArray.length);

        new Thread(() -> {
            try {
                updateResponsiveParametersBackground();
                double availableWidth = currentContainerWidth - (2 * containerMargin);
                double rootX = currentContainerWidth / 2.0;
                double rootY = 80;

                mergeSortAnimated(0, workingArray.length - 1, rootX, rootY, availableWidth);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isRunning.set(false);
            }
        }).start();
    }

    private void mergeSortAnimated(int left, int right, double x, double y, double availableWidth) throws InterruptedException {
        int[] currentSubarray = Arrays.copyOfRange(workingArray, left, right + 1);

        if (left == right) {
            Platform.runLater(() -> createTreeNode(currentSubarray, x, y, Color.LIGHTGREEN, "Leaf"));
            Thread.sleep(delay);
            return;
        }

        Platform.runLater(() -> createTreeNode(currentSubarray, x, y, Color.LIGHTCORAL, "Divide"));
        Thread.sleep(delay);

        int mid = (left + right) / 2;
        double childY = y + verticalGap;
        double spacing = Math.max(availableWidth / 4, minHorizontalGap * 2);
        double leftChildX = Math.max(containerMargin + minNodeWidth / 2, x - spacing);
        double rightChildX = Math.min(currentContainerWidth - containerMargin - minNodeWidth / 2, x + spacing);
        double childAvailableWidth = Math.max(100, availableWidth / 2);

        mergeSortAnimated(left, mid, leftChildX, childY, childAvailableWidth);
        mergeSortAnimated(mid + 1, right, rightChildX, childY, childAvailableWidth);

        int[] merged = mergeArrays(left, mid, right);
        Platform.runLater(() -> createTreeNode(merged, x, y + 15, Color.LIGHTBLUE, "Merged"));
        Thread.sleep(delay);
    }

    // Step Mode Methods

    public void handleStepForward() {
        if (stepMode && currentStepIndex < mergeSteps.size()) {
            mergeSteps.get(currentStepIndex).run();
            currentStepIndex++;
        }
    }

    public void handleStepBackward() {
        if (stepMode && currentStepIndex > 0) {
            currentStepIndex--;
            Platform.runLater(() -> {
                barContainer.getChildren().clear();
                updateResponsiveParameters();
                showOriginalArray();
                for (int i = 0; i < currentStepIndex; i++) {
                    mergeSteps.get(i).run();
                }
            });
        }
    }

    public void prepareStepMergeSort() {
        if (originalArray != null && !isRunning.get()) {
            isRunning.set(true);
            stepMode = true;
            currentStepIndex = 0;
            mergeSteps.clear();
            workingArray = Arrays.copyOf(originalArray, originalArray.length);

            new Thread(() -> {
                try {
                    updateResponsiveParametersBackground();
                    double availableWidth = currentContainerWidth - (2 * containerMargin);
                    double rootX = currentContainerWidth / 2.0;
                    double rootY = 80;

                    mergeSortStepMode(0, workingArray.length - 1, rootX, rootY, availableWidth);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    isRunning.set(false);
                }
            }).start();
        }
    }

    private void mergeSortStepMode(int left, int right, double x, double y, double availableWidth) {
        int[] currentSubarray = Arrays.copyOfRange(workingArray, left, right + 1);

        if (left == right) {
            mergeSteps.add(() -> createTreeNode(currentSubarray, x, y, Color.LIGHTGREEN, "Leaf"));
            return;
        }

        mergeSteps.add(() -> createTreeNode(currentSubarray, x, y, Color.LIGHTCORAL, "Divide"));

        int mid = (left + right) / 2;
        double childY = y + verticalGap;
        double spacing = Math.max(availableWidth / 4, minHorizontalGap * 2);
        double leftChildX = Math.max(containerMargin + minNodeWidth / 2, x - spacing);
        double rightChildX = Math.min(currentContainerWidth - containerMargin - minNodeWidth / 2, x + spacing);
        double childAvailableWidth = Math.max(100, availableWidth / 2);

        mergeSortStepMode(left, mid, leftChildX, childY, childAvailableWidth);
        mergeSortStepMode(mid + 1, right, rightChildX, childY, childAvailableWidth);

        int[] merged = mergeArrays(left, mid, right);
        mergeSteps.add(() -> createTreeNode(merged, x, y + 15, Color.LIGHTBLUE, "Merged"));
    }

    private int[] mergeArrays(int left, int mid, int right) {
        int[] merged = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (workingArray[i] <= workingArray[j]) {
                merged[k++] = workingArray[i++];
            } else {
                merged[k++] = workingArray[j++];
            }
        }

        while (i <= mid) merged[k++] = workingArray[i++];
        while (j <= right) merged[k++] = workingArray[j++];

        System.arraycopy(merged, 0, workingArray, left, merged.length);
        return merged;
    }

    private void showOriginalArray() {
        int n = originalArray.length;
        double totalWidth = n * minNodeWidth + (n - 1) * minHorizontalGap;
        double startX = (currentContainerWidth - totalWidth) / 2;
        double y = 30;

        for (int i = 0; i < n; i++) {
            double x = startX + i * (minNodeWidth + minHorizontalGap);
            Rectangle box = new Rectangle(x, y, minNodeWidth, nodeHeight);
            box.setFill(Color.LIGHTGRAY);
            box.setStroke(Color.BLACK);

            Text text = new Text(String.valueOf(originalArray[i]));
            text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            text.setX(x + minNodeWidth / 2 - 6);
            text.setY(y + nodeHeight / 2 + 5);

            barContainer.getChildren().addAll(box, text);
        }
    }

    private void createTreeNode(int[] array, double x, double y, Color color, String label) {
        double width = array.length * minNodeWidth + (array.length - 1) * minHorizontalGap;
        double startX = x - width / 2;

        for (int i = 0; i < array.length; i++) {
            double rectX = startX + i * (minNodeWidth + minHorizontalGap);
            Rectangle rect = new Rectangle(rectX, y, minNodeWidth, nodeHeight);
            rect.setFill(color);
            rect.setStroke(Color.BLACK);

            Text text = new Text(String.valueOf(array[i]));
            text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            text.setX(rectX + minNodeWidth / 2 - 6);
            text.setY(y + nodeHeight / 2 + 5);

            barContainer.getChildren().addAll(rect, text);
        }

        Text labelText = new Text(label);
        labelText.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        labelText.setX(x - 15);
        labelText.setY(y - 5);
        barContainer.getChildren().add(labelText);
    }

    private void updateResponsiveParameters() {
        currentContainerWidth = barContainer.getWidth();
        currentContainerHeight = barContainer.getHeight();
    }

    private void updateResponsiveParametersBackground() {
        Platform.runLater(this::updateResponsiveParameters);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
    }
}
