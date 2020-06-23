import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class Enemy {
    public int speed = 1;
    private Player player;
    public Sprite sprite;
    private Group parentGroup;
    private Group spriteGroup ;

    Enemy(Image pokemon, GameLevel lvl, Player pl, Group pGroup) {
        player = pl;
        speed = lvl.ordinal()+1;
        sprite = new Sprite(pokemon);
        spriteGroup = sprite.getSprite();
        parentGroup = pGroup;
    }

    public void spawn(boolean isLeft,Group g) {
        int multipler = isLeft ? 1 : -1;
        sprite.spriteView.setX(-1000*multipler);
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
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()+(10*multipler));
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
                    sprite.spriteView.setTranslateX(sprite.spriteView.getTranslateX()-(5*multipler));
                }
            }
        };
        timer.start();
    }

    public void defeated(){
        System.out.println("DEFEATED");
        parentGroup.getChildren().remove(spriteGroup);
    }
}
