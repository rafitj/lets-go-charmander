import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Player {
    private ArrayList<Enemy> enemies;
    private int lives = 3;
    private int score = 0;
    private int evolutionStage;
    public Sprite sprite;
    private Rectangle healthbar = new Rectangle(600, 20, Color.LIGHTGREEN);
    private Group hpgroup = new Group();
    private Text hptext = new Text("300 / 300 HP");
    private Rectangle scorebar = new Rectangle(0, 10, Color.LIGHTBLUE);
    private Group scoregroup = new Group();
    private Text scoretext = new Text("0 EXP");
    private MiniGame game;
    private ArrayList<String> evolutions =  new ArrayList<>(List.of("Charmander", "Charmeleon", "Charizard"));
    private Image fireImg;
    private MediaPlayer fireSound;

    private static Image Charmander =  new Image("assets/pokemon/Charmander.gif",300,300,true,true);
    private static Image Charmeleon =  new Image("assets/pokemon/Charmeleon.gif",300,300,true,true);
    private static Image Charizard =  new Image("assets/pokemon/Charizard.gif",500,500,true,true);


    public void loseLife(){
        if (lives == 0) {
            for (Enemy e : enemies) {
                e.end();
            }
            resetPlayer();
            game.setGamestate(GameState.GAME_OVER);
            game.updateStage();
        } else {
            lives -= 1;
            animateHealthBar();
            if (lives == 1) {
                healthbar.setFill(Color.RED);
            } else if (lives == 2) {
                healthbar.setFill(Color.ORANGE);
            }
        }
    }

    public int getScore(){
        return score;
    }

    public void setScore(int s) {
        score = s;
        scorebar.setWidth(s);
    }



    public void gainXP() {
        score += 10;
        scoretext.setText(score + " XP");
        AnimationTimer timer = new AnimationTimer() {
            int i = 1;
            @Override
            public void handle(long now) {
                if (scorebar.getWidth() > score) {
                    this.stop();
                } else {
                    scorebar.setWidth(scorebar.getWidth()+1);
                    if (i <= 10) {
                        scoretext.setText((score-10+i) + " XP");
                        i+=1;
                    }
                }
            }
        };
        timer.start();
    }

    public void loseXP() {
        score -= 5;
        scoretext.setText(score + " XP");
        AnimationTimer timer = new AnimationTimer() {
            int i = 5;
            @Override
            public void handle(long now) {
                if (scorebar.getWidth() < score) {
                    this.stop();
                } else {
                    scorebar.setWidth(scorebar.getWidth()-1);
                    if (i > 0) {
                        scoretext.setText((score+5-i) + " XP");
                        i-=1;
                    }
                }
            }
        };
        timer.start();
    }

    private void animateHealthBar() {
        double endWidth = healthbar.getWidth()-200;
        AnimationTimer timer = new AnimationTimer() {
            int i = 5;
            @Override
            public void handle(long now) {
                if (healthbar.getWidth() < endWidth) {
                    this.stop();
                } else {
                    healthbar.setWidth(healthbar.getWidth()-5);
                    if (i <= 100) {
                        hptext.setText("HP: " + ((lives+1)*100-i)+ " / 300");
                        i+=5;
                    }
                }
            }
        };
        timer.start();
    }

    public void resetPlayer() {
        lives = 3;
    }

    Player(MiniGame mg, int stage) {
        game = mg;
        evolutionStage = stage;
        fireImg = new Image("assets/fire/fire"+evolutionStage+".gif", 200, 200, true, true);
        fireSound = new MediaPlayer(new Media(new File("src/assets/audio/fire"+evolutionStage+".mp3").toURI().toString()));
        Image sprtieImg;
        switch (evolutionStage){
            case 1:
                sprtieImg=Charmander;
                break;
            case 2:
                sprtieImg=Charmeleon;
                break;
            default:
                sprtieImg=Charizard;
                break;
        }
        sprite = new Sprite(sprtieImg);
    }

    public void setEnemies(ArrayList<Enemy> e) {
        enemies = e;
    }

    public void fireLeft(Group parentGroup) {
        sprite.spriteView.setScaleX(1);
        fireSound.stop();
        fireSound.play();
        ImageView fireView = createFireballView(fireImg,90, 500, 280);
        Group fireGroup = new Group();
        fireGroup.getChildren().add(fireView);
        parentGroup.getChildren().add(fireGroup);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() < 0.05) {
                    parentGroup.getChildren().remove(fireGroup);
                    loseXP();
                    this.stop();
                } else {
                    if (fireView.getTranslateX() < -250+(evolutionStage*50) ) {
                        fireView.setOpacity(fireView.getOpacity()-0.05);
                    }
                    fireView.setTranslateX(fireView.getTranslateX()-10);
                    // Check if fireball hits any enemies
                    for (Enemy e : enemies) {
                        if (!e.spawned) {
                            continue;
                        }
                        if (fireGroup.intersects(e.sprite.spriteGroup.getBoundsInParent())){
                            parentGroup.getChildren().remove(fireGroup);
                            fireGroup.getChildren().clear();
                            e.defeated();
                            gainXP();
                            this.stop();
                            break;
                        }
                    }
                }
            }
        };
        timer.start();

    }

    public void fireRight(Group parentGroup) {
        sprite.spriteView.setScaleX(-1);
        fireSound.stop();
        fireSound.play();
        ImageView fireView = createFireballView(fireImg, -90, 650, 280);
        Group fireGroup = new Group();
        fireGroup.getChildren().add(fireView);

        parentGroup.getChildren().add(fireGroup);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() < 0.05) {
                    parentGroup.getChildren().remove(fireGroup);
                    loseXP();
                    this.stop();
                }
                if (fireView.getTranslateX() > 250-(evolutionStage*50)  ) {
                    fireView.setOpacity(fireView.getOpacity()-0.05);
                }
                fireView.setTranslateX(fireView.getTranslateX()+10);
                // Check if fireball hits any enemies
                // Check if fireball hits any enemies
                for (Enemy e : enemies) {
                    if (!e.spawned) {
                        continue;
                    }
                    if (fireGroup.intersects(e.sprite.spriteGroup.getBoundsInParent())){
                        parentGroup.getChildren().remove(fireGroup);
                        fireGroup.getChildren().clear();
                        e.defeated();
                        gainXP();
                        this.stop();
                        break;
                    }
                }
            }
        };
        timer.start();
    }

    private ImageView createFireballView(Image fire, int rotation, int startX, int startY) {
        ImageView fireView = new ImageView(fire);
        fireView.setRotate(rotation);
        fireView.setLayoutX(startX);
        fireView.setLayoutY(startY);
        return fireView;
    }

    public Group getPlayerHPGroup(){
        hptext.setX(250);
        hptext.setY(45);
        hptext.setFill(Color.BLACK);
        hptext.setOpacity(0.7);
        hptext.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
        healthbar.setArcWidth(20.0);
        healthbar.setArcHeight(20.0);
        healthbar.setStrokeWidth(4);
        healthbar.setStroke(Color.BLACK);
        Rectangle healthbarBackground = new Rectangle(600, 20, Color.BLACK);
        healthbarBackground.setArcWidth(20.0);
        healthbarBackground.setArcHeight(20.0);
        healthbarBackground.setStrokeWidth(4);
        healthbarBackground.setStroke(Color.BLACK);
        hpgroup.getChildren().addAll(healthbarBackground, healthbar, hptext);
        hpgroup.setLayoutX(340);
        hpgroup.setLayoutY(600);
        return hpgroup;
    }
    public Group getScoregroup(){
        scoretext.setX(275);
        scoretext.setY(35);
        scoretext.setFill(Color.BLACK);
        scoretext.setOpacity(0.7);
        scoretext.setStyle("-fx-font: 20 arial; -fx-font-weight: bold;");
        scorebar.setArcWidth(10.0);
        scorebar.setArcHeight(10.0);
        Rectangle scorebarBG = new Rectangle(600, 10, Color.WHITE);
        scorebarBG.setArcWidth(10.0);
        scorebarBG.setArcHeight(10.0);
        scorebarBG.setStrokeWidth(4);
        scorebarBG.setStroke(Color.WHITE);
        scoregroup.getChildren().addAll(scorebarBG, scorebar, scoretext);
        scoregroup.setLayoutX(340);
        scoregroup.setLayoutY(660);
        return scoregroup;
    }

    public  ImageView evolve(Text evolveText){
        String pokemon = evolutions.get(evolutionStage-1);
        MediaPlayer sound = new MediaPlayer(new Media(new File("src/assets/audio/"+pokemon+".mp3").toURI().toString()));
        sound.play();
        evolveText.setText("What? " + evolutions.get(evolutionStage-1) + " is evolving!");
        ImageView evolveView;
       if (evolutionStage == 1) {
           evolveView = new ImageView(Charmander);
           toggle(evolveView,Charmander, Charmeleon,evolveText);
       } else {
           evolveView = new ImageView(Charmeleon);
           toggle(evolveView,Charmeleon, Charizard,evolveText);
       }
       evolveView.setLayoutX(500);
       evolveView.setLayoutY(180);
       return evolveView;
    }

    private void toggle(ImageView iv, Image a, Image b, Text evolveText) {
        AnimationTimer timer = new AnimationTimer() {
            private int i = 1;
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (600000000-(i*10000000) < 1500000) {
                    iv.setImage(b);
                    String pokemon = evolutions.get(evolutionStage);
                    MediaPlayer evolvedSound = new MediaPlayer(new Media(new File("src/assets/audio/"+pokemon+".mp3").toURI().toString()));
                    evolvedSound.play();
                    evolveText.setText("Congrats! " + evolutions.get(evolutionStage) + " evolved into " + pokemon + "!");
                    evolveText.setLayoutX(150);
                    evolutionStage += 1;
                    fireImg = new Image("assets/fire/fire"+evolutionStage+".gif", 200, 200, true, true);
                    this.stop();
                } else if (now-lastUpdate > 400000000-(i*25000000)) {
                    lastUpdate = now;
                    if (i%2==0) {
                        iv.setImage(b);
                    } else {
                        iv.setImage(a);
                    }
                    i++;
                }
            }
        };
        timer.start();
    }
}
