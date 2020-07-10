import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


// This is the controller class that handles input for our FXML form
// We specify this class in the FXML file when we create it.

public class Controller {

    private Model model;

    public Controller(){
        this.model = new Model();
    }
    private String getClickedImgUrl(Event e) {
        ImageView hairImage = (ImageView) e.getSource();
        return hairImage.getImage().getUrl();
    }
    private String getSourceId(Event e) {
        Node n = (Node) e.getSource();
        return n.getId();
    }

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
        ImageView clicked = (ImageView) e.getSource();
        model.setSelected(clicked);
        updateSelectedView();
    }

    private void updateSelectedView() {
        DropShadow ds = new DropShadow( 5, Color.RED );
        model.getSelected().setEffect(ds);
    }

}
