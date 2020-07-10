import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

// This is the controller class that handles input for our FXML form
// We specify this class in the FXML file when we create it.

enum AvatarPart {
    HAIR,
    EYES,
    SKIN,
    MOUTH,
    EYEBROWS,
    JACKET,
    TSHIRT,
    TSHIRTNECK,
    LEFTLAPEL,
    RIGHTLAPEL
}

public class Model {
//  Data Members
    private String hair;
    private String eyes;
    private String skin;
    private String mouth;
    private String eyebrows;

    private Color jacketColor;
    private Color tshirtColor;
    private Color tshirtNeckColor;
    private Color leftLapelColor;
    private Color rightLapelColor;

    private Color hairColor;

    private int eyeSize;
    private int eyebrowPosition;

    private ImageView selected;

    public Model(){
        hair="hair_curly.png";
        eyes="eyes_default.png";
        mouth="mouth_default.png";
        eyebrows="brows_default.png";
        skin="skin_light.png";
//      TODO: Initialize colors

        eyeSize=1;
        eyebrowPosition=0;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getMouth() {
        return mouth;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
    }

    public String getEyebrows() {
        return eyebrows;
    }

    public void setEyebrows(String eyebrows) {
        this.eyebrows = eyebrows;
    }

    public Color getJacketColor() {
        return jacketColor;
    }

    public void setJacketColor(Color jacketColor) {
        this.jacketColor = jacketColor;
    }

    public Color getTshirtColor() {
        return tshirtColor;
    }

    public void setTshirtColor(Color tshirtColor) {
        this.tshirtColor = tshirtColor;
    }

    public Color getTshirtNeckColor() {
        return tshirtNeckColor;
    }

    public void setTshirtNeckColor(Color tshirtNeckColor) {
        this.tshirtNeckColor = tshirtNeckColor;
    }

    public Color getLeftLapelColor() {
        return leftLapelColor;
    }

    public void setLeftLapelColor(Color leftLapelColor) {
        this.leftLapelColor = leftLapelColor;
    }

    public Color getRightLapelColor() {
        return rightLapelColor;
    }

    public void setRightLapelColor(Color rightLapelColor) {
        this.rightLapelColor = rightLapelColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public int getEyeSize() {
        return eyeSize;
    }

    public void setEyeSize(int eyeSize) {
        this.eyeSize = eyeSize;
    }

    public int getEyebrowPosition() {
        return eyebrowPosition;
    }

    public void setEyebrowPosition(int eyebrowPosition) {
        this.eyebrowPosition = eyebrowPosition;
    }

    public ImageView getSelected() {
        return selected;
    }

    public void setSelected(ImageView selected) {
        this.selected = selected;
    }
}
