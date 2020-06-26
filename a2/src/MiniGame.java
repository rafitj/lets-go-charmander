import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

import java.io.File;

enum GameState {
    HOME,
    PLAY_GAME,
    LEVEL_COMPLETE,
    GAME_OVER,
    GAME_WIN,
    QUIT
}

public class MiniGame extends Application {
    // Scene Constants
    private final static int width = 1280;
    private final static int height = 720;
    private final static Image gameOverBG = new Image("assets/WinBackground.jpg", width, height, true, true);
    private final static Image gameOverFX = new Image("assets/WinConfetti.gif", width, height, true, true);
    private final static Image evolutionBG = new Image("assets/EvolutionBackground.gif", width, height, true, true);

    // Scene State
    private GameState gamestate = GameState.HOME;
    private GameLevel gameLevel = GameLevel.ONE;
    private Stage globalStage;
    private Group currentGroup;
    private Scene currentScene;
    private static Image bgImage = new Image("assets/GameBackground.png", width,height , true, true);

    // Level Instance
    public static Level lvl = new Level();

    // Music
    private MediaPlayer bgMusic = new MediaPlayer(new Media(new File("src/assets/audio/PokemonThemeSong.mp3").toURI().toString()));;

    // Main Application Method
    @Override
    public void start(Stage stage) {
        globalStage = stage;
        globalStage.setResizable(false);
        updateStage();
    }

    // GameState setter
    public void setGamestate(GameState state) {
        gamestate = state;
    }

    public void updateStage() {
        if(currentGroup != null){
            currentGroup.getChildren().removeAll();
            currentGroup.getChildren().clear();
        }
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
            case GAME_WIN:
                renderGameWin();
                break;
            default:
                renderHome();
        }
    }

    private void renderGameWin() {
        bgMusic.stop();
        bgMusic = new MediaPlayer(new Media(new File("src/assets/audio/Evolve.mp3").toURI().toString()));
        bgMusic.play();

        // Set BG
        currentGroup = new Group();
        bgImage = gameOverBG;
        setBackground();
        ImageView fxView = new ImageView(gameOverFX);
        currentGroup.getChildren().add(fxView);
        currentScene = new Scene(currentGroup, width, height);

        // Text
        Text congratsText = new Text("Congratulations! You Won!");
        congratsText.setFill(Color.BLACK);
        congratsText.setOpacity(0.8);
        congratsText.setStyle("-fx-font: 75 arial; -fx-font-weight: bold;");
        congratsText.setLayoutX(200);
        congratsText.setLayoutY(130);
        Text scoreText = new Text("Total Score: "+lvl.player.getScore());
        scoreText.setFill(Color.BLACK);
        scoreText.setOpacity(0.8);
        scoreText.setStyle("-fx-font: 30 arial; -fx-font-weight: bold;");
        scoreText.setLayoutX(545);
        scoreText.setLayoutY(160);
        Text evolveText = new Text("What?");
        evolveText.setFill(Color.BLACK);
        evolveText.setOpacity(0.8);
        evolveText.setStyle("-fx-font: 35 arial; -fx-font-weight: bold;");
        evolveText.setLayoutX(350);
        evolveText.setLayoutY(550);
        Text instText = new Text("Press Enter to Restart or Q to Quit");
        instText.setFill(Color.BLACK);
        instText.setOpacity(0.8);
        instText.setStyle("-fx-font: 25 arial; -fx-font-weight: bold;");
        instText.setLayoutX(400);
        instText.setLayoutY(600);
        currentGroup.getChildren().addAll(congratsText,scoreText, instText,evolveText,  lvl.player.evolve(evolveText));

        currentScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.Q) {
                gamestate = GameState.QUIT;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.ENTER){
                gamestate = GameState.PLAY_GAME;
                gameLevel = GameLevel.ONE;
                updateStage();
            }
        });

        setStage();
    }


    private void renderLevelComplete(){
        bgMusic.stop();
        bgMusic = new MediaPlayer(new Media(new File("src/assets/audio/Evolve.mp3").toURI().toString()));
        bgMusic.play();

        // Set BG
        currentGroup = new Group();
        bgImage = evolutionBG;
        setBackground();
        currentScene = new Scene(currentGroup, width, height);

        // Text
        Text congratsText = new Text("Level " + (gameLevel.ordinal()+1) + " Complete!");
        congratsText.setFill(Color.WHITE);
        congratsText.setOpacity(0.8);
        congratsText.setStyle("-fx-font: 75 arial; -fx-font-weight: bold;");
        congratsText.setLayoutX(310);
        congratsText.setLayoutY(130);
        Text scoreText = new Text("Total Score: "+lvl.player.getScore());
        scoreText.setFill(Color.WHITE);
        scoreText.setOpacity(0.8);
        scoreText.setStyle("-fx-font: 22 arial; -fx-font-weight: bold;");
        scoreText.setLayoutX(545);
        scoreText.setLayoutY(160);
        Text evolveText = new Text("What?");
        evolveText.setFill(Color.WHITE);
        evolveText.setOpacity(0.8);
        evolveText.setStyle("-fx-font: 35 arial; -fx-font-weight: bold;");
        evolveText.setLayoutX(350);
        evolveText.setLayoutY(550);
        Text instText = new Text("Press Enter to Continue or Q to Quit");
        instText.setFill(Color.WHITE);
        instText.setOpacity(0.8);
        instText.setStyle("-fx-font: 25 arial; -fx-font-weight: bold;");
        instText.setLayoutX(400);
        instText.setLayoutY(600);
        currentGroup.getChildren().addAll(congratsText,scoreText, instText,evolveText,  lvl.player.evolve(evolveText));

        currentScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.Q) {
                gamestate = GameState.QUIT;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.ENTER){
                gamestate = GameState.PLAY_GAME;
                if (gameLevel == GameLevel.ONE) {
                    gameLevel = GameLevel.TWO;
                } else if (gameLevel == GameLevel.TWO) {
                    gameLevel = GameLevel.THREE;
                } else if (gameLevel == GameLevel.THREE) {
                    gameLevel = GameLevel.ONE;
                }
                updateStage();
            }
        });

        setStage();
    }

    private void setBackground(){
        ImageView imageView = new ImageView(bgImage);
        currentGroup.getChildren().add(imageView);
    }

    private void setStage() {
        globalStage.setScene(currentScene);
        globalStage.show();
    }

    private void renderHome(){
        bgMusic.stop();
        bgMusic = new MediaPlayer(new Media(new File("src/assets/audio/PokemonThemeSong.mp3").toURI().toString()));
//        bgMusic.play();

        currentGroup = new Group();
        currentScene = new Scene(currentGroup, width, height);
        setBackground();

        // Render game logo
        Image gameLogo = new Image("assets/GameLogo.png", 650, 800 , true, true);
        ImageView logoView = new ImageView(gameLogo);
        logoView.setLayoutX((width/2)-(gameLogo.getWidth()/2));
        logoView.setLayoutY(25);
        logoView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 30, 0, 0, 0);");
        currentGroup.getChildren().add(logoView);

        // Render sprite
        Player player = new Player(this, gameLevel.ordinal()+1);
        Group playerSprite = player.sprite.getSprite();
        playerSprite.setTranslateY(50);
        currentGroup.getChildren().add(playerSprite);

        StackPane enterBox = createTextBox("Press Enter to Start",Color.WHITE, Color.BLACK);
        enterBox.setLayoutX(100);
        enterBox.setLayoutY(500);
        currentGroup.getChildren().add(enterBox);

        StackPane quitBox = createTextBox("Press Q to Quit", Color.WHITE, Color.BLACK);
        quitBox.setLayoutX(900);
        quitBox.setLayoutY(585);
        currentGroup.getChildren().add(quitBox);

        StackPane controls = createTextBox("Press ← OR → to Fire", Color.rgb(170,127,82), Color.WHITE);
        controls.setLayoutX(100);
        controls.setLayoutY(585);
        currentGroup.getChildren().add(controls);


        currentScene.setOnKeyPressed(keyEvent -> {
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
        currentGroup.getChildren().add(credits);

        setStage();
    }

    private void renderGame() {
        bgMusic.stop();
        bgMusic = new MediaPlayer(new Media(new File("src/assets/audio/Level"+(gameLevel.ordinal()+1)+"Theme.mp3").toURI().toString()));
//        bgMusic.play();

        currentGroup = new Group();
        currentScene = new Scene(currentGroup, width, height);
        bgImage = new Image("assets/GameBackground.png", width,height , true, true);
        setBackground();

        int lvlScore = lvl.player != null ? lvl.player.getScore() : 0;
        lvl.newLevel(this, gameLevel, currentGroup);
        lvl.player.setScore(lvlScore);

        // Render player
        Group playSprite = lvl.player.sprite.getSprite();
        playSprite.setTranslateY(50);
        currentGroup.getChildren().add(playSprite);


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
        currentGroup.getChildren().addAll(levelText, levelInfo);

        // HP and Score
        currentGroup.getChildren().add(lvl.player.getPlayerHPGroup());
        currentGroup.getChildren().add(lvl.player.getScoregroup());

        // Spawn enemies
         lvl.spawnEnemies(currentGroup);

         // Enemies left
        currentGroup.getChildren().add(lvl.getEnemiesLeft());

        currentScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT) {
                lvl.player.fireLeft(currentGroup);
            }
            if (keyEvent.getCode() == KeyCode.RIGHT){
                lvl.player.fireRight(currentGroup);
            }
        });

        setStage();
    }

    private void renderGameOver() {
        bgMusic.stop();
        bgMusic = new MediaPlayer(new Media(new File("src/assets/audio/GameOverTheme.mp3").toURI().toString()));
        bgMusic.play();
        bgMusic.setCycleCount(MediaPlayer.INDEFINITE);

        currentGroup = new Group();
        currentScene = new Scene(currentGroup, width, height);
        currentGroup.getChildren().add(new Rectangle(width,height,Color.BLACK));

        // Show level enemies
        currentGroup.getChildren().add(lvl.getLevelEnemiesSprites());
        lvl.player.setScore(0);

        // Game over text
        Text gameOverText = new Text("Game Over!");
        gameOverText.setFill(Color.WHITE);
        gameOverText.setOpacity(0.8);
        gameOverText.setStyle("-fx-font: 75 arial; -fx-font-weight: bold;");
        gameOverText.setLayoutX(400);
        gameOverText.setLayoutY(480);
        Text gameOverInstrText = new Text("Press Enter to Restart or Q to Quit");
        gameOverInstrText.setFill(Color.WHITE);
        gameOverInstrText.setOpacity(0.8);
        gameOverInstrText.setStyle("-fx-font: 35 arial; -fx-font-weight: bold;");
        gameOverInstrText.setLayoutX(320);
        gameOverInstrText.setLayoutY(550);

        currentGroup.getChildren().addAll(gameOverText,gameOverInstrText);

        currentScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.Q) {
                gamestate = GameState.QUIT;
                updateStage();
            }
            if (keyEvent.getCode() == KeyCode.ENTER){
                gamestate = GameState.PLAY_GAME;
                gameLevel = GameLevel.ONE;
                updateStage();
            }
        });

        setStage();
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
