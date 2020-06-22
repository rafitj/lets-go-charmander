import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;

enum GameState {
    HOME,
    PLAY_GAME,
    GAME_OVER,
    QUIT
}

public class MiniGame extends Application {
    private final int width = 1280;
    private final int height = 720;
    private GameState gamestate = GameState.HOME;
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        updateStage(stage);
    }

    private void updateStage(Stage stage) {
        switch (gamestate) {
            case QUIT:
                stage.close();
                break;
            case GAME_OVER:
                renderGameOver(stage);
                break;
            case PLAY_GAME:
                renderGame(stage);
                break;
            default:
                renderHome(stage);
        }
    }

    private void setBackground(Group group){
        Image image = new Image("assets/GameBackground.png", width,height , true, true);
        ImageView imageView = new ImageView(image);
        group.getChildren().add(imageView);
    }

    private void setStage(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    private void renderHome(Stage stage){
        Group homeGroup = new Group();
        Scene homeScene = new Scene(homeGroup, width, height);
        setBackground(homeGroup);
        homeScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.Q) {
                gamestate = GameState.QUIT;
                updateStage(stage);
            }
            if (keyEvent.getCode() == KeyCode.ENTER){
                gamestate = GameState.PLAY_GAME;
                updateStage(stage);
            }
        });
        homeGroup.getChildren().add(new Label("Home"));
        setStage(stage,homeScene);
    }

    private void renderGame(Stage stage) {

    }

    private void renderGameOver(Stage stage) {

    }
}
