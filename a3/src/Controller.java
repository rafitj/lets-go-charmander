import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.lang.model.AnnotatedConstruct;
import java.io.File;


// This is the controller class that handles input for our FXML form
// We specify this class in the FXML file when we create it.

public class Controller {

    private Model model;

    public Controller(){
        this.model = new Model();
    }

    public void initialize() {
        horizontalSlider.setValue(1);
        verticalSlider.setValue(1);
        avatarEyes.scaleXProperty().bind(horizontalSlider.valueProperty());
        avatarBrows.translateYProperty().bind(verticalSlider.valueProperty());
        addBGColorPicker.setValue(Color.TRANSPARENT);
    }

    private String getClickedImgUrl(Event e) {
        ImageView hairImage = (ImageView) e.getSource();
        return hairImage.getImage().getUrl();
    }

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
    private ImageView avatarHair;

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

    @FXML
    private ColorPicker addBGColorPicker;

//    Update
    private void updateView(AvatarPart modifiedPart){
        switch (modifiedPart) {
            case HAIR:
                avatarHair.setImage(new Image(model.getHair()));
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


//    Event Handlers
    @FXML
    private void selectNewHair(Event e){
        model.setHair(getClickedImgUrl(e));
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
        ImageView clicked = (ImageView) e.getSource();
        model.setSelected(clicked);
        updateSelectedView();
        updateCommandBar();
    }

    @FXML
    private void selectAvatarBG() {
        DropShadow ds = new DropShadow( 5, Color.RED );
        avatarView.setEffect(ds);
        if (null != model.getSelected()) {
            model.getSelected().setEffect(null);
        }
        model.setSelected(null);
        updateCommandBar();
    }

    @FXML
    private void removeBGColor() {
        addBGColorPicker.setValue(Color.TRANSPARENT);
        model.setBackgroundColor(Color.TRANSPARENT);
        updateAvatarBG();
        removeBGButton.setDisable(true);
    }

    @FXML
    private void addBGColor(Event e) {
        ColorPicker colorPicker = (ColorPicker) e.getSource();
        model.setBackgroundColor(colorPicker.getValue());
        updateAvatarBG();
        removeBGButton.setDisable(false);
    }

    @FXML
    private void hoverEnter(Event e) {
        Node node = (Node) e.getSource();
        node.setCursor(Cursor.HAND);
    }

    @FXML
    private void hoverExit(Event e) {
        Node node = (Node) e.getSource();
        node.setCursor(Cursor.DEFAULT);
    }

    private void updateSelectedView() {
        DropShadow ds = new DropShadow( 5, Color.RED );
        model.getSelected().setEffect(ds);
    }

    private void updateAvatarBG(){
        avatarView.setFill(model.getBackgroundColor());
    }

    private void updateCommandBar(){
        ImageView selected = model.getSelected();
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
        } else if (selected == avatarHair) {
             colorPicker.setDisable(false);
             colorPicker.setOpacity(1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Hair Color");
         } else if (selected == null){
             removeBGButton.setDisable(model.getBackgroundColor() == Color.TRANSPARENT);
             addBGColorPicker.setDisable(false);
             addBGColorPicker.setOpacity(1);
             removeBGButton.setOpacity(1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Background Color");
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
        addBGColorPicker.setOpacity(0);
        removeBGButton.setOpacity(0);
        addBGColorPicker.setDisable(true);
        removeBGButton.setDisable(true);
    }

}
