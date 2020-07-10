import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setHeight(800);
        stage.setWidth(1200);
        stage.setMinHeight(400);
        stage.setMinWidth(800);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
