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
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

// This is the controller class that handles input for our FXML form
// We specify this class in the FXML file when we create it.

public class Controller {

    private final Model model;

    public Controller(){
        this.model = new Model(this);
    }

    @FXML
    public void initialize() {
//      Initial Controller Values
        horizontalSlider.setValue(1);
        verticalSlider.setValue(1);
        colorPicker.setValue(Color.TRANSPARENT);

//      Add Event Listeners
        horizontalSlider.valueProperty().addListener((observable, oldVal, newVal) -> {
            avatarEyes.setScaleX((Double) newVal*0.275+0.5);
            avatarEyes.setScaleY((Double) newVal*0.275+0.5);
        });
        verticalSlider.valueProperty().addListener((observable, oldVal, newVal) -> avatarBrows.setTranslateY((Double) newVal*-1));

//      Initial Model Values
        model.setHairId("hair_curly");
        model.setHairColor(model.getHairSVGColor());
        model.setClothes("resources/clothes.svg");
    }

//    Controller objects
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

    @FXML
    private Pane avatarClothes;

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


//    Event Handlers
    @FXML
    private void selectNewHair(Event e){
        Node node = (Node) e.getSource();
        String hair_id = node.getId();
        model.setHairId(hair_id);
    }

    @FXML
    private void selectNewBrow(Event e){
        model.setEyebrows(getClickedImgUrl(e));
    }

    @FXML
    private void selectNewEyes(Event e){
        model.setEyes(getClickedImgUrl(e));
    }

    @FXML
    private void selectNewMouth(Event e){
        model.setMouth(getClickedImgUrl(e));
    }

    @FXML
    private void selectNewSkin(Event e){
        model.setSkin(getClickedImgUrl(e));
    }

    @FXML
    public void clickAvatarPart(Event e){
        if (null != model.getSelected()) {
            model.getSelected().setEffect(null);
        }
        Node clicked = (Node) e.getSource();
        model.setSelected(clicked);
    }

    @FXML
    private void changeColor(Event e) {
        ColorPicker colorPicker = (ColorPicker) e.getSource();
        Node selected = model.getSelected();

        if (selected == model.getHair()) {
            model.setHairColor(colorPicker.getValue());
        } else if (selected == model.getLeftLapel()) {
            model.setLeftLapelColor(colorPicker.getValue());
        } else if (selected == model.getRightLapel()) {
            model.setRightLapelColor(colorPicker.getValue());
        } else if (selected == model.getJacket()) {
            model.setJacketColor(colorPicker.getValue());
        } else if (selected == model.getTShirt()) {
            model.setTShirtColor(colorPicker.getValue());
        } else if (selected == model.getTShirtNeck()) {
            model.setTShirtNeckColor(colorPicker.getValue());
        } else if (selected == null) {
            model.setBackgroundColor(colorPicker.getValue());
            model.setSelected(null);
        }
    }

    @FXML
    private void removeBGColor() {
        colorPicker.setValue(Color.TRANSPARENT);
        model.setBackgroundColor(Color.TRANSPARENT);
        model.setSelected(null);
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
        boolean isNotHair = clicked != model.getHair().getChildren().get(0);
        if (isNotHair && isNotAvatar(clicked)){
            deselect();
        }
    }

    @FXML
    private void deselect() {
        if (model.getSelected() != null) {
            model.getSelected().setEffect(null);
        }
        model.setSelected(null);
    }


//    VIEW
///////////////////////////

//    Update View Methods
    public void updateView(AvatarPart modifiedPart){
        switch (modifiedPart) {
            case HAIR:
                updateHair();
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

    public void updateHair() {
        avatarHair.getChildren().clear();
        avatarHair.getChildren().add(model.getHair());
    }

    public void updateClothes() {
        Group clothes = model.getClothes();
        avatarClothes.getChildren().clear();
        avatarClothes.getChildren().add(clothes);

        model.setTShirt((SVGPath) clothes.getChildren().get(0));
        model.setJacket((SVGPath) clothes.getChildren().get(1));
        model.setTShirtNeck((SVGPath) clothes.getChildren().get(2));
        model.setLeftLapel((SVGPath) clothes.getChildren().get(3));
        model.setRightLapel((SVGPath) clothes.getChildren().get(4));

        model.setTShirtColor((Color) model.getTShirt().getFill());
        model.setTShirtNeckColor((Color) model.getTShirtNeck().getFill());
        model.setLeftLapelColor((Color) model.getLeftLapel().getFill());
        model.setRightLapelColor((Color) model.getRightLapel().getFill());
        model.setJacketColor((Color) model.getJacket().getFill());
    }

    public void registerAction(Node node) {
        node.setOnMouseClicked((new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                clickAvatarPart(event);
            }
        }));
    }

    public void updateSelectedView() {
        InnerShadow ds = new InnerShadow( 10, Color.RED );
        if (model.getSelected() != null) {
            model.getSelected().setEffect(ds);
        }
    }

    public void updateCommandBar(){
        Node selected = model.getSelected();
        disableControls();
        SVGPath leftLapel = model.getLeftLapel();
        SVGPath rightLapel = model.getRightLapel();
        SVGPath jacket = model.getJacket();
        SVGPath tShirt = model.getTShirt();
        SVGPath tShirtNeck = model.getTShirtNeck();
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
        } else if (selected == model.getHair()){
             colorPicker.setDisable(false);
             colorPicker.setOpacity(1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Hair Color");
             colorPicker.setValue(model.getHairSVGColor());
         } else if (selected == leftLapel || selected == rightLapel || selected == jacket || selected == tShirt || selected ==tShirtNeck ) {
             colorPicker.setDisable(false);
             colorPicker.setOpacity(1);
             controlLabel.setOpacity(1);
             controlLabel.setText("Clothing Part Color");
             if (selected == leftLapel) colorPicker.setValue(model.getLeftLapelColor());
             if (selected == rightLapel) colorPicker.setValue(model.getRightLapelColor());
             if (selected == jacket) colorPicker.setValue(model.getJacketColor());
             if (selected == tShirt) colorPicker.setValue(model.getTShirtColor());
             if (selected == tShirtNeck) colorPicker.setValue(model.getTShirtNeckColor());
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

    public void updateAvatarBG(){
        avatarView.setFill(model.getBackgroundColor());
    }

    public void updateHairColor() {
        Group hair = model.getHair();
        for (int i = 0; i < hair.getChildren().size(); i++) {
            SVGPath path = (SVGPath) hair.getChildren().get(i);
            path.setFill(model.getHairColor());
        }
    }

    public void updateJacketView() {
        model.getJacket().setFill(model.getJacketColor());
    }

    public void updateLeftLapelView(){
        model.getLeftLapel().setFill(model.getLeftLapelColor());
    }

    public void updateRightLapelView() {
        model.getRightLapel().setFill(model.getRightLapelColor());
    }

    public void updateTShirtView(){
        model.getTShirt().setFill(model.getTShirtColor());
    }

    public void updateTShirtNeckView() {
        model.getTShirtNeck().setFill(model.getTShirtNeckColor());
    }

    //    Utilities
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

    private String getClickedImgUrl(Event e) {
        ImageView hairImage = (ImageView) e.getSource();
        return hairImage.getImage().getUrl();
    }

    private boolean isNotAvatar(Node node) {
        return node != avatarEyes && node != avatarBrows &&  node != model.getJacket() && node != avatarContainer
                && node != model.getRightLapel() && node != model.getTShirt()
                && node != model.getTShirtNeck() && node != model.getLeftLapel();
    }

}
