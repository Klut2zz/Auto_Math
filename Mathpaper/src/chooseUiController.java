import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class chooseUiController {

    @FXML
    private Button backButton;

    @FXML
    private Button conButton;

    @FXML
    private Button highButton;

    @FXML
    private Button midButton;

    @FXML
    private Button priButton;

    @FXML
    private Label showstatus;

    @FXML
    private Button changePswBtn;

    @FXML
    private TextField textNumber;

    @FXML
    private ImageView imageView;
    @FXML
    void changePswEvent(ActionEvent event) {

    }

    @FXML
    public void initialize(){
        imageView.setImage(
                new Image("css/Image/back3.png")
        );
    }
}
