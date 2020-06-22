import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Sprite {
    private String imgURL;
    private Group spriteGroup;
    private Image spriteImg;
    private ImageView spriteView;

    public Sprite(String pokemon){
        imgURL = "assets/pokemon/"+pokemon+".gif";
        spriteGroup = new Group();
        spriteImg = new Image(imgURL, 300, 300 , true, true);
        spriteView = new ImageView(spriteImg);
        spriteView.setLayoutX((1280/2)-(spriteImg.getWidth()/2));
        spriteView.setLayoutY(275);

        Ellipse spriteShadow = new Ellipse(125, 20);
        spriteShadow.setFill(Color.BLACK);
        spriteShadow.setOpacity(0.3);
        spriteShadow.setCenterX(635);
        spriteShadow.setCenterY(560);
        spriteShadow.setEffect(new GaussianBlur(30));

        spriteGroup.getChildren().addAll(spriteShadow,spriteView);
    }

    public Group getSprite() {
        return spriteGroup;
    }

    public void turnLeft(int fireStage) {
        spriteView.setScaleX(1);
        Image fireImg = new Image("assets/fire/fire"+fireStage+".gif", 300, 300, true, true);
        ImageView fireView = createFireballView(fireImg,90, 425, 300);
        spriteGroup.getChildren().add(fireView);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                moveFireBall(fireView, -1);
            }
        };
        timer.start();

    }

    public void turnRight(int fireStage) {
        spriteView.setScaleX(-1);
        Image fireImg = new Image("assets/fire/fire"+fireStage+".gif", 300, 300, true, true);
        ImageView fireView = createFireballView(fireImg, -90, 575, 300);
        spriteGroup.getChildren().add(fireView);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (fireView.getOpacity() == 0) {
                    spriteGroup.getChildren().remove(fireView);
                    this.stop();
                }
                moveFireBall(fireView, 1);
            }
        };
        timer.start();
    }

    private void moveFireBall(ImageView iv, int dx){
        if (iv.getTranslateX() > 100 || iv.getTranslateX() < -100) {
            iv.setOpacity(iv.getOpacity()-0.05);
            iv.setTranslateX(iv.getTranslateX()+dx);
        } else {
            iv.setTranslateX(iv.getTranslateX()+dx);
        }
    }

    private ImageView createFireballView(Image fire, int rotation, int startX, int startY) {
        ImageView fireView = new ImageView(fire);
        fireView.setRotate(rotation);
        fireView.setLayoutX(startX);
        fireView.setLayoutY(startY);
        return fireView;
    }

    public void spawnLeft() {

    }
}
