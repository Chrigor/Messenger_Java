package messengeraps.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class MensagemController implements Initializable {
    
    @FXML
    private Circle fotoMensagem;

    @FXML
    private Label txtMensagem;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public Circle getFotoMensagem() {
        return fotoMensagem;
    }

    public void setFotoMensagem(ImagePattern fotoContato) {
        fotoMensagem.setFill(fotoContato);
    }

    public Label getTxtMensagem() {
        return txtMensagem;
    }

    public void setTxtMensagem(String stringMensagem) {
        txtMensagem.setText(stringMensagem);
    }
    
    
    
}
