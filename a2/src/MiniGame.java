import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;

enum GameState {
    HOME,
    PLAY_GAME,
    LEVEL_COMPLETE,
    GAME_OVER,
    QUIT
}

public class MiniGame extends Application {
    private final int width = 1280;
    private final int height = 720;
    private GameState gamestate = GameState.HOME;
    private GameLevel gameLevel = GameLevel.ONE;
    private Stage globalStage;
    private static Level lvl = new Level();

    @Override
    public void start(Stage stage) {
        globalStage = stage;
        globalStage.setResizable(false);
        updateStage();
    }

    private void updateStage() {
        switch (gamestate) {
            case QUIT:
                globalStage.close();
                break;
            case GAME_OVER:
                renderGameOver();
                break;
            case PLAY_GAME:
                renderGame();
                break;
            case LEVEL_COMPLETE:
                renderLevelComplete();
                break;
            default:
                renderHome();
        }
    }

    private void renderLevelComplete(){

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

    private void renderHome(){

        Media music = new Media(new File("src/assets/audio/PokemonThemeSong.mp3").toURI().toString());
        MediaPlayer bgMusic = new MediaPlayer(music);
        bgMusic.play();

        Group homeGroup = new Group();
        Scene homeScene = new Scene(homeGroup, width, height);
        setBackground(homeGroup);

        // Render game logo
        Image gameLogo = new Image("assets/GameLogo.png", 650, 800 , true, true);
        ImageView logoView = new ImageView(gameLogo);
        logoView.setLayoutX((width/2)-(gameLogo.getWidth()/2));
        logoView.setLayoutY(25);
        logoView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 30, 0, 0, 0);");
        homeGroup.getChildren().add(logoView);

        // Render sprite
        Player player = new Player(gameLevel.ordinal());
        Group playSprite = player.sprite.getSprite();
        homeGroup.getChildren().add(playSprite);

        StackPane enterBox = createTextBox("Press Enter to Start",Color.WHITE, Color.BLACK);
        enterBox.setLayoutX(100);
        enterBox.setLayoutY(500);
        homeGroup.getChildren().add(enterBox);

        StackPane quitBox = createTextBox("Press Q to Quit", Color.WHITE, Color.BLACK);
        quitBox.setLayoutX(900);
        quitBox.setLayoutY(585);
        homeGroup.getChildren().add(quitBox);

        StackPane controls = createTextBox("Press ← OR → to Fire", Color.rgb(170,127,82), Color.WHITE);
        controls.setLayoutX(100);
        controls.setLayoutY(585);
        homeGroup.getChildren().add(controls);


        homeScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.Q) {
                gamestate = GameState.QUIT;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.ENTER){
                gamestate = GameState.PLAY_GAME;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.DIGIT0) {
                gameLevel = GameLevel.ONE;
                gamestate = GameState.PLAY_GAME;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.DIGIT2) {
                gameLevel = GameLevel.TWO;
                gamestate = GameState.PLAY_GAME;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.DIGIT3) {
                gameLevel = GameLevel.THREE;
                gamestate = GameState.PLAY_GAME;
                updateStage();
            }
        });

        Label credits = new Label("Created by Rafit Jamil 2078514");
        credits.setTextFill(Color.WHITE);
        credits.setOpacity(0.5);
        credits.setStyle("-fx-font: 16 arial; -fx-font-weight: bold;");
        credits.setLayoutX(1000);
        credits.setLayoutY(25);
        homeGroup.getChildren().add(credits);

        setStage(globalStage,homeScene);
    }

    private void renderGame() {
        Group gameGroup = new Group();
        Scene gameScene = new Scene(gameGroup, width, height);
        setBackground(gameGroup);

        lvl.newLevel(gameLevel, gameGroup);

        // Render player
        Group playSprite = lvl.player.sprite.getSprite();
        gameGroup.getChildren().add(playSprite);


        // Level Details
        Label levelText = new Label("Level " + lvl.level);
        levelText.setTextFill(Color.WHITE);
        levelText.setOpacity(0.9);
        levelText.setStyle("-fx-font: 30 arial; -fx-font-weight: bold;");
        levelText.setLayoutX(600);
        levelText.setLayoutY(25);
        Label levelInfo = new Label(lvl.getLevelInfo());
        levelInfo.setTextFill(Color.WHITE);
        levelInfo.setOpacity(0.9);
        levelInfo.setStyle("-fx-font: 45 arial; -fx-font-weight: bold;");
        levelInfo.setLayoutX(500);
        levelInfo.setLayoutY(60);
        gameGroup.getChildren().addAll(levelText, levelInfo);

        // HP and Score
        gameGroup.getChildren().add(lvl.player.getPlayerHPGroup());
        gameGroup.getChildren().add(lvl.player.getScoregroup());

        // Spawn enemies
         lvl.spawnEnemies(gameGroup);

        gameScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                lvl.player.fireLeft(gameGroup);
            }
            if (keyEvent.getCode() == KeyCode.RIGHT){
                lvl.player.fireRight(gameGroup);
            }
        });

        setStage(globalStage,gameScene);
    }

    private void renderGameOver() {

    }

    private StackPane createTextBox(String str, Color fill, Color textFill){
        StackPane textbox = new StackPane();
        Rectangle rectangle = new Rectangle(300,70 ,fill);
        rectangle.setStroke(Color.rgb(170,127,82));
        rectangle.setStrokeWidth(3);
        rectangle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 30, 0, 0, 0); ");
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);
        Text text = new Text(str);
        text.setFill(textFill);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-font: 24 arial; -fx-font-weight: bold;");
        textbox.getChildren().addAll(rectangle,text);
        return textbox;
    }
}
