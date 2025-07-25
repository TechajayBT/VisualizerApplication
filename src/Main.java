import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    private AnchorPane contentArea;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainView.fxml"));
        BorderPane root = loader.load();

        // Get content area and buttons
        contentArea = (AnchorPane) root.lookup("#contentArea");
        Button arrayBtn = (Button) root.lookup("#arrayBtn");
        Button linkedListBtn = (Button) root.lookup("#linkedListBtn");
        Button mergeSortBtn = (Button) root.lookup("#mergeSortBtn");
        Button queueBtn = (Button) root.lookup("#queueBtn");
        Button stackBtn = (Button) root.lookup("#stackBtn");
        Button treeBtn = (Button) root.lookup("#treeBtn");



        // Assign actions
        arrayBtn.setOnAction(e -> loadView("ArrayVisualizer.fxml"));
        linkedListBtn.setOnAction(e -> loadView("LinkedList.fxml"));
        mergeSortBtn.setOnAction(e -> loadView("MergeSort.fxml"));
        queueBtn.setOnAction(e -> loadView("Queue.fxml"));
        stackBtn.setOnAction(e -> loadView("StackVisualizer.fxml"));
        treeBtn.setOnAction(e -> loadView("TreeVisualizer.fxml"));
        // Setup and show scene
        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("All Visualizers");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private void loadView(String fxmlFile) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource("/Views/" + fxmlFile));
            contentArea.getChildren().setAll(view);
            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
