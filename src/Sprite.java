import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Sprite {
    public Group spriteGroup;
    public ImageView spriteView;

    public Sprite(Image pokemon){
        spriteGroup = new Group();
        spriteView = new ImageView(pokemon);
        spriteView.setLayoutX((1280/2)-(pokemon.getWidth()/2));
        spriteView.setLayoutY(275);
//
//        Ellipse spriteShadow = new Ellipse(125, 20);
//        spriteShadow.setFill(Color.BLACK);
//        spriteShadow.setOpacity(0.3);
//        spriteShadow.setCenterX(635);
//        spriteShadow.setCenterY(560);
//        spriteShadow.setEffect(new GaussianBlur(30));

        spriteGroup.getChildren().addAll(spriteView);
    }

    public Group getSprite() {
        return spriteGroup;
    }

}
