import javafx.scene.Group;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Sprite {
    private String imgURL;

    public Sprite(String pokemon){
        imgURL = "https://professorlotus.com/Sprites/"+pokemon+".gif";
    }

    public Group getSprite() {
        Group spriteGroup = new Group();

        Image sprite = new Image(imgURL, 300, 300 , true, true);
        ImageView spriteView = new ImageView(sprite);
        spriteView.setLayoutX((1280/2)-(sprite.getWidth()/2));
        spriteView.setLayoutY(275);

        Ellipse spriteShadow = new Ellipse(125, 20);
        spriteShadow.setFill(Color.BLACK);
        spriteShadow.setOpacity(0.3);
        spriteShadow.setCenterX(635);
        spriteShadow.setCenterY(560);
        spriteShadow.setEffect(new GaussianBlur(30));

        spriteGroup.getChildren().addAll(spriteShadow,spriteView);

        return spriteGroup;
    }





}
