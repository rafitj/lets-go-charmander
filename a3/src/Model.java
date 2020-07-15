import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

enum AvatarPart {
    HAIR,
    EYES,
    SKIN,
    MOUTH,
    EYEBROWS,
}

public class Model {

    private final SVGLoader svgLoader = new SVGLoader();
    private  Controller view;

    //  Data Members
    private Color backgroundColor;


    private String hairId;
    private String eyes;
    private String skin;
    private String mouth;
    private String eyebrows;

    private Color jacketColor;
    private Color tShirtColor;
    private Color tShirtNeckColor;
    private Color leftLapelColor;
    private Color rightLapelColor;

    private SVGPath jacket;
    private SVGPath tShirt;
    private SVGPath tShirtNeck;
    private SVGPath leftLapel;
    private SVGPath rightLapel;

    private Group clothes;
    private Group hair;

    private Color hairColor;

    private Node selected;

    public Model(Controller controller){
        view = controller;

        eyes="eyes_default.png";
        mouth="mouth_default.png";
        eyebrows="brows_default.png";
        skin="skin_light.png";

        backgroundColor = Color.TRANSPARENT;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        view.updateAvatarBG();
    }

    public Group getClothes(){
        return clothes;
    }

    public void setClothes(String url) {
        clothes = svgLoader.loadSVG(url);
        clothes.setScaleX(1.62);
        clothes.setScaleY(1.62);
        clothes.setLayoutX(10);
        clothes.setLayoutY(-10);
        view.updateClothes();
    }

    public Group getHair(){
        return hair;
    }

    public void setHair(String hairSVG) {
        hair = svgLoader.loadSVG(hairSVG);
        hair.setScaleX(1.62);
        hair.setScaleY(1.62);
        if (hairSVG.contains("curly")) {
            hair.setLayoutY(4);
            hair.setLayoutX(10);
        } else {
            hair.setLayoutY(-4);
            hair.setLayoutX(8);
        }
        view.registerAction(hair);
        view.updateView(AvatarPart.HAIR);
        view.updateHairColor();
    }

    public Color getHairSVGColor(){
        SVGPath path = (SVGPath) hair.getChildren().get(0);
        return (Color) path.getFill();
    }

    public String getHairId() {
        return hairId;
    }

    public void setHairId(String hairId) {
        this.hairId = hairId;
        setHair("resources/hair/"+hairId+".svg");
    }


    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
        view.updateView(AvatarPart.EYES);
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
        view.updateView(AvatarPart.SKIN);
    }

    public String getMouth() {
        return mouth;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
        view.updateView(AvatarPart.MOUTH);
    }

    public String getEyebrows() {
        return eyebrows;
    }

    public void setEyebrows(String eyebrows) {
        this.eyebrows = eyebrows;
        view.updateView(AvatarPart.EYEBROWS);
    }

    public SVGPath getJacket() {
        return jacket;
    }

    public void setJacket(SVGPath jacket) {
        this.jacket = jacket;
        view.registerAction(jacket);
    }

    public SVGPath getTShirt() {
        return tShirt;
    }

    public void setTShirt(SVGPath tShirt) {
        this.tShirt = tShirt;
        view.registerAction(tShirt);
    }

    public SVGPath getTShirtNeck() {
        return tShirtNeck;
    }

    public void setTShirtNeck(SVGPath tShirtNeck) {
        this.tShirtNeck = tShirtNeck;
        view.registerAction(tShirtNeck);
    }

    public SVGPath getLeftLapel() {
        return leftLapel;
    }

    public void setLeftLapel(SVGPath leftLapel) {
        this.leftLapel = leftLapel;
        view.registerAction(leftLapel);
    }

    public SVGPath getRightLapel() {
        return rightLapel;
    }

    public void setRightLapel(SVGPath rightLapel) {
        this.rightLapel = rightLapel;
        view.registerAction(rightLapel);
    }

    public Color getJacketColor() {
        return jacketColor;
    }

    public void setJacketColor(Color jacketColor) {
        this.jacketColor = jacketColor;
        view.updateJacketView();
    }

    public Color getTShirtColor() {
        return tShirtColor;
    }

    public void setTShirtColor(Color tShirtColor) {
        this.tShirtColor = tShirtColor;
        view.updateTShirtView();
    }

    public Color getTShirtNeckColor() {
        return tShirtNeckColor;
    }

    public void setTShirtNeckColor(Color tShirtNeckColor) {
        this.tShirtNeckColor = tShirtNeckColor;
        view.updateTShirtNeckView();
    }

    public Color getLeftLapelColor() {
        return leftLapelColor;
    }

    public void setLeftLapelColor(Color leftLapelColor) {
        this.leftLapelColor = leftLapelColor;
        view.updateLeftLapelView();
    }

    public Color getRightLapelColor() {
        return rightLapelColor;
    }

    public void setRightLapelColor(Color rightLapelColor) {
        this.rightLapelColor = rightLapelColor;
        view.updateRightLapelView();
    }

    public Color getHairColor() {
        if (hairColor == null) {
            return getHairSVGColor();
        }
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
        view.updateHairColor();
    }

    public Node getSelected() {
        return selected;
    }

    public void setSelected(Node selected) {
        this.selected = selected;
        view.updateSelectedView();
        view.updateCommandBar();

    }
}
