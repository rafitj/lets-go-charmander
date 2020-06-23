import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class Enemy {
    public int speed = 1;
    private Player player;
    public Sprite sprite;
    private Group parentGroup;
    private Group spriteGroup ;
    private Level gameLevel;
    public boolean spawned;

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
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()+(multipler*speed));
                }
            }
        };
        timer.start();
    }

    private void shuffleBack(boolean isLeft) {
        int multipler = isLeft ? 1 : -1;
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if ((isLeft && sprite.spriteView.getTranslateX() < 600) || (!isLeft && sprite.spriteView.getTranslateX() > - 600)) {
                    attackPlayer(isLeft);
                    this.stop();
                } else {
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()-(0.5*multipler));
                }
            }
        };
        timer.start();
    }

    public void defeated(){
        parentGroup.getChildren().remove(spriteGroup);
        spriteGroup.getChildren().clear();
        gameLevel.updateEnemyCount(this);
    }

    public void end(){
        parentGroup.getChildren().remove(spriteGroup);
        spriteGroup.getChildren().clear();
    }
}
