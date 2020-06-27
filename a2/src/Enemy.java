import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;

import java.io.File;
import java.util.Random;

public class Enemy {
    public int speed = 1;
    private Player player;
    public Sprite sprite;
    private Group parentGroup;
    private Group spriteGroup ;
    private Level gameLevel;
    public boolean spawned;
    private static final Media defeatedSound = new Media(new File("src/assets/audio/EnemyDefeat.wav").toURI().toString());

    Enemy(Image pokemon, Level level, Player pl, Group pGroup, int xOffset, int yOffset) {
        player = pl;
        speed = level.level;
        gameLevel = level;
        sprite = new Sprite(pokemon);
        sprite.spriteView.setLayoutX(sprite.spriteView.getLayoutX()+xOffset);
        sprite.spriteView.setLayoutY(sprite.spriteView.getLayoutY()+yOffset);
        spriteGroup = sprite.getSprite();
        parentGroup = pGroup;
        spawned = false;
    }


    public void spawn(boolean isLeft,Group g) {
        spawned = true;
        parentGroup = g;
        if (isLeft) {
            sprite.spriteView.setScaleX(-1);
            sprite.spriteView.setLayoutX(0-sprite.spriteView.getImage().getWidth());
        } else {
            sprite.spriteView.setLayoutX(1280);
        }
        sprite.spriteView.setY(100);
        attackPlayer(isLeft);
        parentGroup.getChildren().add(spriteGroup);
    }

    private void attackPlayer(boolean isLeft){
        int multipler = isLeft ? 1 : -1;
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (sprite.spriteGroup.intersects(player.sprite.spriteGroup.getBoundsInParent())) {
                    player.loseLife();
                    animatePlayerDamage();
                    shuffleBack(isLeft);
                    this.stop();
                } else {
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()+(multipler*(speed*2)));
                }
            }
        };
        timer.start();
    }

    private void animatePlayerDamage(){
        Text pointText = new Text("-1HP");
        pointText.setFill(Color.RED);
        pointText.setStroke(Color.BLACK);
        pointText.setStrokeWidth(2);
        pointText.setStrokeType(StrokeType.OUTSIDE);
        pointText.setStyle("-fx-font: 40 arial; -fx-font-weight: bold;");
        pointText.setLayoutX(player.sprite.spriteView.getLayoutX());
        pointText.setLayoutY(player.sprite.spriteView.getLayoutY()+50);
        pointText.setOpacity(0);
        parentGroup.getChildren().addAll(pointText);
        AnimationTimer timer = new AnimationTimer() {
            int i = 50;
            @Override
            public void handle(long l) {
                if (i < 1){
                    parentGroup.getChildren().remove(pointText);
                    player.sprite.spriteView.setOpacity(1);
                    this.stop();
                } else {
                    if (i > 40) {
                        player.sprite.spriteView.setOpacity((i%2)*100);
                        pointText.setOpacity(pointText.getOpacity()-0.10);
                    } else {
                        player.sprite.spriteView.setOpacity(1);
                        pointText.setOpacity(pointText.getOpacity()-0.10);
                    }
                    pointText.setTranslateY(pointText.getTranslateY()-1);
                    i -= 1;
                }
            }
        };

        timer.start();

    }
    private void shuffleBack(boolean isLeft) {
        int multipler = isLeft ? 1 : -1;
        Random rand = new Random();
        AnimationTimer timer = new AnimationTimer() {
            int i = 0;
            @Override
            public void handle(long now) {
                if (i > 500) {
                    attackPlayer(isLeft);
                    this.stop();
                } else if (i > 350) {
                    sprite.spriteView.setRotate(0);
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()-(3*multipler));
                } else {
                    sprite.spriteView.setRotate(rand.nextDouble()*20-10);
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()-(15*multipler));
                }
                i+=25;
            }
        };
        timer.start();
    }

    public void defeated(){
        MediaPlayer defeatedSFX = new MediaPlayer(defeatedSound);
        defeatedSFX.stop();
        defeatedSFX.play();
        AnimationTimer timer = new AnimationTimer() {
            int i = 3;
            @Override
            public void handle(long l) {
                if (i==0){
                    parentGroup.getChildren().remove(spriteGroup);
                    spriteGroup.getChildren().clear();
                    this.stop();
                } else {
                    if (i%2==1) {
                        sprite.spriteView.setOpacity(0);
                    } else {
                        sprite.spriteView.setOpacity(i);
                    }
                    i -= 1;
                }
            }
        };
        timer.start();
        gameLevel.updateEnemyCount(this);

    }

    public void end(){
        parentGroup.getChildren().remove(spriteGroup);
        spriteGroup.getChildren().clear();
    }
}
