package messengeraps.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import messengeraps.Model.Contato;

public class LayoutContatoController {

    public Contato contato;

    @FXML
    private Circle fotoContato;

    @FXML
    private Label labelNomeContato;

    public void setContato(Contato contato) {
        this.contato = contato;
        atualizarInformacoesContato();
    }

    public void atualizarInformacoesContato() {
        fotoContato.setFill(contato.getFotoCliente());
        labelNomeContato.setText(contato.getNomeCliente());
    }

    @FXML
    void clickContato(MouseEvent event) {
        TelaClienteController.contatoConversa = this.contato;
    }

}
