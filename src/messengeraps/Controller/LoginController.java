package messengeraps.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import messengeraps.MessengerAps;
import messengeraps.Model.Contato;
import messengeraps.SQL.ContatoBD;
import messengeraps.SQL.LoginBD;
import messengeraps.SQL.PrincipalBD;

public class LoginController implements Initializable {

    @FXML
    private TextField textUser;

    @FXML
    private PasswordField textPassword;

    @FXML
    void fecharStage(MouseEvent event) {
        MessengerAps.tela.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PrincipalBD.conectarBD("jdbc:derby://localhost:1527/MessengerDB", "root", "root"); // unica conexao
    }

    @FXML
    void pegarUserAndPassword(MouseEvent event) {
        String usuario = textUser.getText();
        String senha = textPassword.getText();

        if (validarInputs(usuario) && validarInputs(senha)) {
            int usuarioID;
            try {
                usuarioID = Integer.parseInt(usuario);
            } catch (Exception e) {
                usuarioID = 0;
            }

            if (LoginBD.logarBD(usuarioID, senha)) {
                Contato usuarioLogado = ContatoBD.pegarUsuarioLogado(usuarioID);
                usuarioLogado.setUpAllRede();
                carregarTelaCliente(usuarioLogado);
            } else {
                createAlert("Error", "Login Error", "Please, verify the data.");
            }
        } else {
            createAlert("Error", "Validation Error", "Please, verify the data.");
        }

    }

    private void createAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setResizable(false);
        alert.show();
    }

    private boolean validarInputs(String validacao) {
        if (validacao == null || validacao.length() == 0 || validacao.equals(" ") || validacao.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private void carregarTelaCliente(Contato usuarioLogado) {
        try {
            FXMLLoader loader = new FXMLLoader(MessengerAps.class.getResource("View/FXMLTelaCliente.fxml")); // FXML
            Parent telaCliente = (Parent) loader.load();

            TelaClienteController controllerTelaCliente = loader.getController(); // pega controller da telaClienteController
            controllerTelaCliente.setContato(usuarioLogado); // seta o Cliente
            
            setarControllerUserLogado(usuarioLogado, controllerTelaCliente);
            
            Scene sceneTelaCliente = new Scene(telaCliente);
            MessengerAps.tela.setScene(sceneTelaCliente);
            MessengerAps.tela.centerOnScreen();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void setarControllerUserLogado(Contato usuarioLogado, TelaClienteController controller){
        usuarioLogado.setControllerTelaCliente(controller);
        System.out.println("Setou controller! ");
    }

}
