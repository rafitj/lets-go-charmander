import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Enemy {
    public int speed = 1;
    private Player player;
    public Sprite sprite;
    private Group parentGroup;
    private Group spriteGroup ;
    private Level gameLevel;
    public boolean spawned;
    public MediaPlayer defeatedSound = new MediaPlayer(new Media(new File("src/assets/audio/enemyDefeat.mp3").toURI().toString()));

    Enemy(Image pokemon, Level level, Player pl, Group pGroup) {
        player = pl;
        speed = level.level;
        gameLevel = level;
        sprite = new Sprite(pokemon);
        spriteGroup = sprite.getSprite();
        parentGroup = pGroup;
        spawned = false;
    }



    public void spawn(boolean isLeft,Group g) {
        spawned = true;
        parentGroup = g;
        if (isLeft) {
            sprite.spriteView.setScaleX(-1);
            sprite.spriteView.setLayoutX(0);
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
                    shuffleBack(isLeft);
                    this.stop();
                } else {
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()+(multipler*(speed*2)));
                }
            }
        };
        timer.start();
    }

    private void shuffleBack(boolean isLeft) {
        int multipler = isLeft ? 1 : -1;
        AnimationTimer timer = new AnimationTimer() {
            int i = 0;
            @Override
            public void handle(long now) {
                if (i > 250) {
                    attackPlayer(isLeft);
                    this.stop();
                } else {
                    i+=5;
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()-(0.5*multipler));
                }
            }
        };
        timer.start();
    }

    public void defeated(){
        defeatedSound.stop();
        defeatedSound.play();
        parentGroup.getChildren().remove(spriteGroup);
        spriteGroup.getChildren().clear();
        gameLevel.updateEnemyCount(this);
    }

    public void end(){
        parentGroup.getChildren().remove(spriteGroup);
        spriteGroup.getChildren().clear();
    }
}
