import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Pair;

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
    private Text hptext = new Text("3 / 3 HP");
    private Rectangle scorebar = new Rectangle(0, 10, Color.LIGHTBLUE);
    private Group scoregroup = new Group();
    private Text scoretext = new Text("0 EXP");
    private MiniGame game;
    private ArrayList<String> evolutions =  new ArrayList<>(List.of("Charmander", "Charmeleon", "Charizard", "MegaCharizard"));
    private Image fireImg;
    private Media fireSound;
    private static final Media hitSound = new Media(new File("src/assets/audio/Hit.mp3").toURI().toString());
    private static Image fireDeath = new Image("assets/fire/burn.gif", 300, 300, true,true);
    private static Image Charmander =  new Image("assets/pokemon/Charmander.gif",200,200,true,true);
    private static Image Charmeleon =  new Image("assets/pokemon/Charmeleon.gif",250,250,true,true);
    private static Image Charizard =  new Image("assets/pokemon/Charizard.gif",500,500,true,true);
    private static Image MegaCharizard =  new Image("assets/pokemon/MegaCharizard.gif",600,600,true,true);
    private ArrayList<Pair<Double,Double>> evolutionOffsets;

    public void loseLife(){
        MediaPlayer hitSFX = new MediaPlayer(hitSound);
        hitSFX.stop();
        hitSFX.play();
        lives -= 1;
        animateHealthBar();
        if (lives == 1) {
            healthbar.setFill(Color.RED);
        } else if (lives == 2) {
            healthbar.setFill(Color.ORANGE);
        }
        if (lives == 0) {
            for (Enemy e : enemies) {
                e.end();
            }
            resetPlayer();
            game.setGamestate(GameState.GAME_OVER);
            game.updateStage();
        }
    }



    public int getScore(){
        return score;
    }

    public void setScore(int s) {
        score = s+1;
        scoretext.setText(s+1 +" XP");
        scorebar.setWidth(s+1);
    }

    public void gainXP() {
        score += 1;
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (scorebar.getWidth() > score*10) {
                    this.stop();
                }
                scorebar.setWidth(scorebar.getWidth()+1);
            }
        };
        timer.start();
        scoretext.setText(score + " XP");
    }

    public void loseXP() {
        score -= 1;
        scoretext.setText(score + " XP");
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (scorebar.getWidth() < score*10) {
                    this.stop();
                }
                scorebar.setWidth(scorebar.getWidth()-1);
            }
        };
        timer.start();
        scoretext.setText(score + " XP");
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
                        i+=5;
                    }
                }
            }
        };
        hptext.setText(lives+" / 3 HP");
        timer.start();
    }

    public void resetPlayer() {
        lives = 3;
    }

    Player(MiniGame mg, int stage) {
        evolutionOffsets = new ArrayList<>();
        evolutionOffsets.add(new Pair<Double, Double>(0.0,0.0));
        evolutionOffsets.add(new Pair<Double, Double>(-25.0,-25.0));
        evolutionOffsets.add(new Pair<Double, Double>(-125.0,-200.0));
        evolutionOffsets.add(new Pair<Double, Double>(-200.0,-120.0));

        game = mg;
        evolutionStage = stage;
        fireImg = new Image("assets/fire/fire"+evolutionStage+".gif", 100*evolutionStage, 100*evolutionStage, true, true);
        fireSound = new Media(new File("src/assets/audio/fire1.mp3").toURI().toString());
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
        if (evolutionStage == 2) {
            sprite.spriteView.setLayoutY(sprite.spriteView.getLayoutY()-50);
            sprite.spriteView.setLayoutX(sprite.spriteView.getLayoutX());
        }
        else if (evolutionStage == 3) {
            sprite.spriteView.setLayoutY(sprite.spriteView.getLayoutY()-250);
            sprite.spriteView.setLayoutX(sprite.spriteView.getLayoutX());
        }
        else if (evolutionStage == 4) {
            sprite.spriteView.setLayoutY(sprite.spriteView.getLayoutY()-170);
            sprite.spriteView.setLayoutX(sprite.spriteView.getLayoutX()-50);
        }
    }

    public void setEnemies(ArrayList<Enemy> e) {
        enemies = e;
    }

    public void fireLeft(Group parentGroup) {
        sprite.spriteView.setScaleX(1);
        MediaPlayer fireSFX = new MediaPlayer(fireSound);
        fireSFX.stop();
        fireSFX.play();
        ImageView fireView = createFireballView(fireImg,90, (640-15-fireImg.getWidth()), 340);
        Group fireGroup = new Group();
        fireGroup.getChildren().add(fireView);
        parentGroup.getChildren().add(fireGroup);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() < 0.05) {
                    animateMissedFire(parentGroup, 545+fireView.getTranslateX(), fireView.getLayoutY());
                    parentGroup.getChildren().remove(fireGroup);
                    loseXP();
                    this.stop();
                } else {
                    if (fireView.getTranslateX() < -360+((evolutionStage-1)*120)) {
                        fireView.setOpacity(fireView.getOpacity()-(0.1*evolutionStage));
                    }
                    fireView.setTranslateX(fireView.getTranslateX()-10);
                    // Check if fireball hits any enemies
                    for (Enemy e : enemies) {
                        if (!e.spawned) {
                            continue;
                        }
                        if (fireGroup.intersects(e.sprite.spriteGroup.getBoundsInParent())){
                            enemyBurn(parentGroup,(0-e.sprite.spriteView.getImage().getWidth())+e.sprite.spriteView.getTranslateX(), e.sprite.spriteView.getLayoutY(), true);
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
        MediaPlayer fireSFX = new MediaPlayer(fireSound);
        fireSFX.stop();
        fireSFX.play();
        ImageView fireView = createFireballView(fireImg, -90, 655, 340);
        Group fireGroup = new Group();
        fireGroup.getChildren().add(fireView);

        parentGroup.getChildren().add(fireGroup);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() < 0.05) {
                    parentGroup.getChildren().remove(fireGroup);
                    animateMissedFire(parentGroup, 657+fireView.getTranslateX(), fireView.getLayoutY());
                    loseXP();
                    this.stop();
                }
                if (fireView.getTranslateX() > 360-((evolutionStage-1)*120)) {
                    fireView.setOpacity(fireView.getOpacity()-(0.1*evolutionStage));
                }
                fireView.setTranslateX(fireView.getTranslateX()+10);
                // Check if fireball hits any enemies
                // Check if fireball hits any enemies
                for (Enemy e : enemies) {
                    if (!e.spawned) {
                        continue;
                    }
                    if (fireGroup.intersects(e.sprite.spriteGroup.getBoundsInParent())){
                        enemyBurn(parentGroup,1280+e.sprite.spriteView.getTranslateX(), e.sprite.spriteView.getLayoutY(), false);
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

    private void animateMissedFire(Group parentGroup, double x, double y){
        Text pointText = new Text("-1XP");
        pointText.setFill(Color.GREY);
        pointText.setStroke(Color.BLACK);
        pointText.setStrokeWidth(2);
        pointText.setStrokeType(StrokeType.OUTSIDE);
        pointText.setStyle("-fx-font: 40 arial; -fx-font-weight: bold;");
        pointText.setLayoutX(x);
        pointText.setLayoutY(y);
        pointText.setOpacity(0);
        parentGroup.getChildren().addAll(pointText);
        AnimationTimer timer = new AnimationTimer() {
            int i = 80;
            @Override
            public void handle(long l) {
                if (i < 1){
                    parentGroup.getChildren().remove(pointText);
                    this.stop();
                } else {
                    if (i > 60) {
                        pointText.setOpacity(pointText.getOpacity()+0.10);
                    } else {
                        pointText.setOpacity(pointText.getOpacity()-0.10);
                    }
                    pointText.setTranslateY(pointText.getTranslateY()-1);
                    i -= 1;
                }
            }
        };

        timer.start();
    }

    private void enemyBurn(Group parentGroup, double x, double y, boolean isLeft) {
        ImageView fire = new ImageView(fireDeath);
        fire.setOpacity(0);
        fire.setLayoutX(x);
        fire.setLayoutY(y);
        Text pointText = new Text("+1XP");
        pointText.setFill(Color.LIGHTBLUE);
        pointText.setStroke(Color.BLACK);
        pointText.setStrokeWidth(2);
        pointText.setStrokeType(StrokeType.OUTSIDE);
        pointText.setStyle("-fx-font: 40 arial; -fx-font-weight: bold;");
        double lx = x;
        if (x < 50 && isLeft) {
            lx += 160;
        } else if(isLeft) {
            lx -= 40;
        } else if (x > 1250){
            lx -= 70;
        }
        pointText.setLayoutX(lx);
        pointText.setLayoutY(y+50);
        pointText.setOpacity(0);
        parentGroup.getChildren().addAll(fire, pointText);
        AnimationTimer timer = new AnimationTimer() {
            int i = 60;
            @Override
            public void handle(long l) {
                if (i < 1){
                    fire.setOpacity(0);
                    parentGroup.getChildren().remove(pointText);
                    parentGroup.getChildren().remove(fire);
                    this.stop();
                } else {
                    if (i > 50) {
                        fire.setOpacity(fire.getOpacity()+0.20);
                        pointText.setOpacity(pointText.getOpacity()+0.20);
                    } else {
                        fire.setOpacity(fire.getOpacity()-0.10);
                        pointText.setOpacity(pointText.getOpacity()-0.10);
                    }
                    pointText.setTranslateY(pointText.getTranslateY()-1);
                    i -= 1;
                }
            }
        };

        timer.start();
    }
    private ImageView createFireballView(Image fire, int rotation, double startX, double startY) {
        ImageView fireView = new ImageView(fire);
        fireView.setRotate(rotation);
        fireView.setLayoutX(startX);
        fireView.setLayoutY(startY);
        return fireView;
    }

    public Group getPlayerHPGroup(){
        hptext.setX(270);
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
       } else if (evolutionStage == 2){
           evolveView = new ImageView(Charmeleon);
           toggle(evolveView,Charmeleon, Charizard,evolveText);
       } else {
           evolveView = new ImageView(Charizard);
           toggle(evolveView,Charizard, MegaCharizard,evolveText);
       }
       evolveView.setLayoutX(565);
       evolveView.setLayoutY(260);
       evolveView.setTranslateX(evolutionOffsets.get(evolutionStage-1).getKey());
       evolveView.setTranslateY(evolutionOffsets.get(evolutionStage-1).getValue());
       return evolveView;
    }

    private void toggle(ImageView iv, Image a, Image b, Text evolveText) {
        ColorAdjust whiteout = new ColorAdjust();
        whiteout.setBrightness(1.0);
        AnimationTimer timer = new AnimationTimer() {
            private int i = 1;
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (600000000-(i*10000000) < 1500000) {
                    iv.setEffect(null);
                    iv.setImage(b);
                    String pokemon = evolutions.get(evolutionStage);
                    MediaPlayer evolvedSound = new MediaPlayer(new Media(new File("src/assets/audio/"+pokemon+".mp3").toURI().toString()));
                    evolvedSound.play();
                    evolveText.setText("Congrats! " + evolutions.get(evolutionStage-1) + " evolved into " + pokemon + "!");
                    evolveText.setLayoutX(220);
                    if (evolutionStage != 3) {
                        evolutionStage += 1;
                    }
                    fireImg = new Image("assets/fire/fire"+evolutionStage+".gif", 200, 200, true, true);
                    this.stop();
                } else if (now-lastUpdate > 400000000-(i*25000000)) {
                    lastUpdate = now;
                    if (i < 3) {}
                    else if (i%3!=0) {
                        iv.setEffect(whiteout);
                        iv.setImage(b);
                        iv.setTranslateX(evolutionOffsets.get(evolutionStage).getKey());
                        iv.setTranslateY(evolutionOffsets.get(evolutionStage).getValue());
                    } else {
                        iv.setImage(a);
                        iv.setTranslateX(evolutionOffsets.get(evolutionStage-1).getKey());
                        iv.setTranslateY(evolutionOffsets.get(evolutionStage-1).getValue());
                    }
                    i++;
                }
            }
        };
        timer.start();
    }
}
