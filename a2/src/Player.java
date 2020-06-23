import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;


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

    private static Image Charmander =  new Image("assets/pokemon/Charmander.gif",300,300,true,true);
    private static Image Charmeleon =  new Image("assets/pokemon/Charmander.gif",300,300,true,true);
    private static Image  Charizard =  new Image("assets/pokemon/Charmander.gif",300,300,true,true);

    public int getLives(){
        return lives;
    }

    public void loseLife(){
        if (lives == 0) {

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

    public void gainXP() {
        score += 30;
        animateXPBar();
        scoretext.setText(score + " XP");
        AnimationTimer timer = new AnimationTimer() {
            int i = 0;
            @Override
            public void handle(long now) {
                if (scorebar.getWidth() < scorebar.getWidth()+30) {
                    this.stop();
                } else {
                    scorebar.setWidth(scorebar.getWidth()-1);
                    if (i <= 30) {
                        scoretext.setText((score-30+i) + " XP");
                        i+=1;
                    }
                }
            }
        };
        timer.start();
    }

    public void loseXP() {
        score -= 15;
        scoretext.setText(score + " XP");
        AnimationTimer timer = new AnimationTimer() {
            int i = 15;
            @Override
            public void handle(long now) {
                if (scorebar.getWidth() < scorebar.getWidth()-15) {
                    this.stop();
                } else {
                    scorebar.setWidth(scorebar.getWidth()-1);
                    if (i > 0) {
                        scoretext.setText((score+15-i) + " XP");
                        i-=1;
                    }
                }
            }
        };
        timer.start();
    }

    private void animateXPBar(){

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

    Player(int stage) {
        evolutionStage = stage;
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
        int fireStage = evolutionStage;
        sprite.spriteView.setScaleX(1);
        Image fireImg = new Image("assets/fire/fire"+fireStage+".gif", 200, 200, true, true);
        ImageView fireView = createFireballView(fireImg,90, 425, 300);
        parentGroup.getChildren().add(fireView);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() < 0.05) {
                    parentGroup.getChildren().remove(fireView);
                    loseXP();
                    this.stop();
                } else {
                    if (fireView.getTranslateX() < -150 ) {
                        fireView.setOpacity(fireView.getOpacity()-0.05);
                    }
                    fireView.setTranslateX(fireView.getTranslateX()-1);
//                    // Check if fireball hits any enemies
//                    for (Enemy e : enemies) {
//
//                    }
                }
            }
        };
        timer.start();

    }

    public void fireRight(Group parentGroup) {
        int fireStage = evolutionStage;
        sprite.spriteView.setScaleX(-1);
        Image fireImg = new Image("assets/fire/fire"+fireStage+".gif", 200, 200, true, true);
        ImageView fireView = createFireballView(fireImg, -90, 575, 300);
        parentGroup.getChildren().add(fireView);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() < 0.05) {
                    parentGroup.getChildren().remove(fireView);
                    loseXP();
                    this.stop();
                }
                if (fireView.getTranslateX() > 150 ) {
                    fireView.setOpacity(fireView.getOpacity()-0.05);
                }
                fireView.setTranslateX(fireView.getTranslateX()+1);
                // Check if fireball hits any enemies
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
}
