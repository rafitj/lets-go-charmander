import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


// This is the controller class that handles input for our FXML form
// We specify this class in the FXML file when we create it.

public class Controller {

    private Model model;

    private final SVGLoader svgLoader = new SVGLoader();

    public Controller(){
        this.model = new Model();
    }

    public void initialize() {
        horizontalSlider.setValue(1);
        verticalSlider.setValue(1);
        horizontalSlider.valueProperty().addListener((observable, oldvalue, newvalue) ->{
            avatarEyes.setScaleX((Double) newvalue*0.25+0.5);
            avatarEyes.setScaleY((Double) newvalue*0.25+0.5);
        });
        verticalSlider.valueProperty().addListener((observable, oldvalue, newvalue) ->{
            avatarBrows.setTranslateY((Double) newvalue*-1);
        });
        colorPicker.setValue(Color.TRANSPARENT);
        hair = svgLoader.loadSVG("resources/hair/hair_curly.svg");
        setHairSVG("resources/hair/hair_curly.svg");

        setClothesSVG();


    }

    private String getClickedImgUrl(Event e) {
        ImageView hairImage = (ImageView) e.getSource();
        return hairImage.getImage().getUrl();
    }

    private Color getHairSVGColor(){
        SVGPath path = (SVGPath) hair.getChildren().get(0);
        return (Color) path.getFill();
    }

    @FXML
    private AnchorPane avatarContainer;

    @FXML
    private Rectangle avatarView;

    @FXML
    private ImageView avatarBrows;

    @FXML
    private ImageView avatarSkin;

    @FXML
    private ImageView avatarEyes;

    @FXML
    private ImageView avatarMouth;

    @FXML
    private Pane avatarHair;
    private Group hair;

    @FXML
    private Pane avatarClothes;
    private Group clothes;

    @FXML
    private Slider verticalSlider;

    @FXML
    private Slider horizontalSlider;

    @FXML
    private Label controlLabel;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button removeBGButton;

    private SVGPath jacket;
    private SVGPath tshirt;
    private SVGPath tshirtneck;
    private SVGPath leftlapel;
    private SVGPath rightlapel;

//    Update
    private void updateView(AvatarPart modifiedPart){
        switch (modifiedPart) {
            case HAIR:
                setHairSVG(model.getHairSVG());
                break;
            case EYES:
                avatarEyes.setImage(new Image(model.getEyes()));
                break;
            case SKIN:
                avatarSkin.setImage(new Image(model.getSkin()));
                break;
            case EYEBROWS:
                avatarBrows.setImage(new Image(model.getEyebrows()));
                break;
            case MOUTH:
                avatarMouth.setImage(new Image(model.getMouth()));
                break;
            default:
                break;
        }
    }

    private void setClothesSVG() {
        clothes = svgLoader.loadSVG("resources/clothes.svg");
        clothes.setScaleX(1.62);
        clothes.setScaleY(1.62);
        clothes.setLayoutX(10);
        clothes.setLayoutY(-10);
        avatarClothes.getChildren().clear();
        avatarClothes.getChildren().add(clothes);

        tshirt = (SVGPath) clothes.getChildren().get(0);
        jacket = (SVGPath) clothes.getChildren().get(1);
        tshirtneck = (SVGPath) clothes.getChildren().get(2);
        leftlapel = (SVGPath) clothes.getChildren().get(3);
        rightlapel = (SVGPath) clothes.getChildren().get(4);

        ArrayList<SVGPath> clothParts = new ArrayList<>();
        clothParts.add(tshirt);
        clothParts.add(jacket);
        clothParts.add(tshirtneck);
        clothParts.add(leftlapel);
        clothParts.add(rightlapel);

        for (SVGPath part : clothParts) {
            part.setOnMouseClicked((new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    clickAvatarPart(event);
                }
            }));
        }

        model.setTshirtColor((Color) tshirt.getFill());
        model.setTshirtNeckColor((Color) tshirtneck.getFill());
        model.setLeftLapelColor((Color) leftlapel.getFill());
        model.setRightLapelColor((Color) rightlapel.getFill());
        model.setJacketColor((Color) jacket.getFill());

    }

    private void setHairSVG(String hairSVG) {
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
        hair.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clickAvatarPart(event);
            }
        }));
        avatarHair.getChildren().clear();
        avatarHair.getChildren().add(hair);
    }

//    Event Handlers
    @FXML
    private void selectNewHair(Event e){
        Node node = (Node) e.getSource();
        String hair_id = node.getId();
        model.setHair(hair_id);
        updateView(AvatarPart.HAIR);
    }

    @FXML
    private void selectNewBrow(Event e){
        model.setEyebrows(getClickedImgUrl(e));
        updateView(AvatarPart.EYEBROWS);
    }

    @FXML
    private void selectNewEyes(Event e){
        model.setEyes(getClickedImgUrl(e));
        updateView(AvatarPart.EYES);
    }

    @FXML
    private void selectNewMouth(Event e){
        model.setMouth(getClickedImgUrl(e));
        updateView(AvatarPart.MOUTH);
    }

    @FXML
    private void selectNewSkin(Event e){
        model.setSkin(getClickedImgUrl(e));
        updateView(AvatarPart.SKIN);
    }

    @FXML
    private void clickAvatarPart(Event e){
        if (null != model.getSelected()) {
            model.getSelected().setEffect(null);
        }
        avatarView.setEffect(null);
        Node clicked = (Node) e.getSource();
        model.setSelected(clicked);
        updateSelectedView();
        updateCommandBar();
    }

    @FXML
    private void changeColor(Event e) {
        ColorPicker colorPicker = (ColorPicker) e.getSource();
        Node selected = model.getSelected();
        if (selected == hair) {
            model.setHairColor(colorPicker.getValue());
            for (int i = 0; i < hair.getChildren().size(); i++) {
                SVGPath path = (SVGPath) hair.getChildren().get(i);
                path.setFill(model.getHairColor());
            }
        } else if (selected == null) {
            model.setBackgroundColor(colorPicker.getValue());
            model.setSelected(null);
            updateAvatarBG();
            updateCommandBar();
        } else if (selected == leftlapel || selected == rightlapel || selected == jacket || selected == tshirt || selected ==tshirtneck){
            if (selected == leftlapel) {
                model.setLeftLapelColor(colorPicker.getValue());
                leftlapel.setFill(model.getLeftLapelColor());
            } else if (selected == rightlapel) {
                model.setRightLapelColor(colorPicker.getValue());
                rightlapel.setFill(model.getRightLapelColor());
            } else if (selected == jacket) {
                model.setJacketColor(colorPicker.getValue());
                jacket.setFill(model.getJacketColor());
            } else if (selected == tshirt) {
                model.setTshirtColor(colorPicker.getValue());
                tshirt.setFill(model.getTshirtColor());
            } else  {
                model.setTshirtNeckColor(colorPicker.getValue());
                tshirtneck.setFill(model.getTshirtNeckColor());
            }
        }

    }

    @FXML
    private void removeBGColor() {
        colorPicker.setValue(Color.TRANSPARENT);
        model.setBackgroundColor(Color.TRANSPARENT);
        model.setSelected(null);
        updateAvatarBG();
        updateCommandBar();
    }

    @FXML
    private void hoverEnter(Event e) {
        Node node = (Node) e.getSource();
        node.setEffect(new InnerShadow( 15, Color.GREEN ));
        updateSelectedView();
    }

    @FXML
    private void hoverExit(Event e) {
        Node node = (Node) e.getSource();
        node.setEffect(null);
        updateSelectedView();
    }

    @FXML
    private void saveImage(Event e){
        deselect();
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        WritableImage image = avatarContainer.snapshot(snapshotParameters, null);

        Button downloadButton = (Button) e.getSource();
        Stage mainStage = (Stage) downloadButton.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Download Avatar Image");
        File fileDest = fileChooser.showSaveDialog(mainStage);

        if (fileDest != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null),"png",fileDest);
            } catch (IOException err) {
                System.out.println(err.getMessage());
            }
        }
    }

    @FXML
    private void handleRandomClick(MouseEvent e) {
        Node clicked = e.getPickResult().getIntersectedNode();
        if (clicked != hair.getChildren().get(0) && clicked != avatarEyes && clicked != avatarBrows && clicked != jacket && clicked != avatarContainer
            && clicked != leftlapel && clicked != rightlapel && clicked !=tshirt && clicked != tshirtneck) {
            deselect();

        }
    }

    @FXML
    private void deselect() {
        if (model.getSelected() != null) {
            model.getSelected().setEffect(null);
        }
        model.setSelected(null);
        updateCommandBar();
    }

    private void updateSelectedView() {
        InnerShadow ds = new InnerShadow( 10, Color.RED );
        if (model.getSelected() != null) {
            model.getSelected().setEffect(ds);
        }
    }

    private void updateAvatarBG(){
        avatarView.setFill(model.getBackgroundColor());
    }

    private void updateCommandBar(){
        Node selected = model.getSelected();
        disableControls();
         if (selected == avatarBrows) {
            verticalSlider.setDisable(false);
            verticalSlider.setOpacity(1);
            controlLabel.setOpacity(1);
            controlLabel.setText("Eye Brow Level");
        } else if (selected == avatarEyes) {
            horizontalSlider.setDisable(false);
            horizontalSlider.setOpacity(1);
            controlLabel.setOpacity(1);
            controlLabel.setText("Eye Size");
        } else if (selected == hair) {
             colorPicker.setDisable(false);
             colorPicker.setOpacity(1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Hair Color");
             colorPicker.setValue(getHairSVGColor());
         } else if (selected == leftlapel || selected == rightlapel || selected == jacket || selected == tshirt || selected ==tshirtneck ) {
             colorPicker.setDisable(false);
             colorPicker.setOpacity(1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Clothing Part Color");
             if (selected == leftlapel) colorPicker.setValue(model.getLeftLapelColor());
             if (selected == rightlapel) colorPicker.setValue(model.getRightLapelColor());
             if (selected == jacket) colorPicker.setValue(model.getJacketColor());
             if (selected == tshirt) colorPicker.setValue(model.getTshirtColor());
             if (selected == tshirtneck) colorPicker.setValue(model.getTshirtNeckColor());
         } else if (selected == null){
             boolean isTransparent = model.getBackgroundColor().equals(Color.TRANSPARENT);
             removeBGButton.setDisable(isTransparent);
             colorPicker.setDisable(false);
             colorPicker.setOpacity(1);
             removeBGButton.setOpacity(isTransparent ? 0.5 : 1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Background Color");
             colorPicker.setValue(model.getBackgroundColor());
         }
    }

    private void disableControls() {
        horizontalSlider.setDisable(true);
        verticalSlider.setDisable(true);
        colorPicker.setDisable(true);
        controlLabel.setOpacity(0);
        verticalSlider.setOpacity(0);
        horizontalSlider.setOpacity(0);
        colorPicker.setOpacity(0);
        removeBGButton.setOpacity(0);
        removeBGButton.setDisable(true);
    }

}
