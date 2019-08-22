package messengeraps.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javax.swing.ImageIcon;

public class EmojiController implements Initializable {
       
    @FXML
    private Circle fotoMensagem;

    @FXML
    private ImageView fotoEmoji;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }    

    public Circle getFotoMensagem() {
        return fotoMensagem;
    }

    public void setFotoMensagem(ImagePattern fotoContato) {
       fotoMensagem.setFill(fotoContato);
    }

    public ImageView getFotoEmoji() {
        return fotoEmoji;
    }

    public void setFotoEmoji(Image emoji) {
        fotoEmoji.setImage(emoji);
    }
    
    
    
}
