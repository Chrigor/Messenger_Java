package messengeraps.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import messengeraps.MessengerAps;
import messengeraps.Model.Contato;
import messengeraps.Model.Mensagem;
import messengeraps.SQL.MensagemBD;

public class TelaClienteController implements Initializable {

    public static Contato contatoConversa;
    public Contato usuarioLogado;
    public ArrayList<String> arrayEmoji;
    public static Mensagem ultimaMensagemQueChegou;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarInicio();
    }

    @FXML
    private Circle fotoContatoLogado;

    @FXML
    private Label nomeContatoLogado;

    @FXML
    private ScrollPane scrollContatos;

    @FXML
    private VBox vboxContatos;

    @FXML
    private Button btSend;

    @FXML
    private TextField textFieldMensagem;

    @FXML
    private Circle fotoContatoConversa;

    @FXML
    private Label nomeContatoConversa;

    @FXML
    private Label statusContatoConversa;

    @FXML
    private ScrollPane scrollConversa;

    @FXML
    private VBox vboxConversa;

    @FXML
    void mouseMovedScroll(MouseEvent event) {
        System.out.println("MOUSE PASSOU");
        // pegarMensagensContatoAtivo(contatoConversa.getIdCliente());
    }

    @FXML
    void alterarInformacoesConversa() {
        if (contatoConversa == null && usuarioLogado.getContatosCliente() != null) {
            contatoConversa = usuarioLogado.getContatosCliente().get(0);
        }

        if (contatoConversa != null) {
            nomeContatoConversa.setText(contatoConversa.getNomeCliente());
            fotoContatoConversa.setFill(contatoConversa.getFotoCliente());
            statusContatoConversa.setText(contatoConversa.getStatusCliente());
            pegarMensagensContatoAtivo(contatoConversa.getIdCliente());
        } else {
            nomeContatoConversa.setText("Contato Padrão");
            statusContatoConversa.setText("Você não tem amigos! :(");
        }

    }

    @FXML
    void clickMicrofone(MouseEvent event) {

    }

    @FXML
    void botaoSendEnviar(MouseEvent event) {
        String mensagemTextField = textFieldMensagem.getText();

        if (validarInputs(mensagemTextField)) { // condições pra bloquear    
            verificarQualTipoDeMensagem(new Mensagem(usuarioLogado.getIdCliente(), contatoConversa.getIdCliente(), mensagemTextField));

            getVboxConversa().focusedProperty();
            enviarProServidor(mensagemTextField);
            armazenarMensagem(mensagemTextField);
            textFieldMensagem.setText("");
        }
    }

    private void armazenarMensagem(String mensagem) {
        MensagemBD.inserirMensagemBD(usuarioLogado.getIdCliente(), contatoConversa.getIdCliente(), mensagem);
        adicionarMensagemNoList(mensagem);
    }

    @FXML
    void fecharStage(MouseEvent event) {
        usuarioLogado.desconectarSocket();
        MessengerAps.tela.close();
    }

    @FXML
    void botaoSendDisponivel(KeyEvent event) {
        String mensagemTextField = textFieldMensagem.getText();

        if (!validarInputs(mensagemTextField)) { // condições pra bloquear
            btSend.setDisable(true); // bloqueia
        } else {
            if (event.getCode() == ENTER) { // se for Enter, enviar msg 
                verificarQualTipoDeMensagem(new Mensagem(usuarioLogado.getIdCliente(), contatoConversa.getIdCliente(), mensagemTextField));
                enviarProServidor(mensagemTextField);
                armazenarMensagem(mensagemTextField);
                textFieldMensagem.setText("");
            }
            btSend.setDisable(false); // desbloqueia
        }
    }

    private void enviarMensagemNaTela(String mensagem) {
        FXMLLoader loader = new FXMLLoader(messengeraps.MessengerAps.class.getResource("View/FXMLMensagem.fxml")); // FXML
        try {
            Node MensagemGrafica = (Node) loader.load();
            MensagemController controllerMensagem = loader.getController(); // pega controller
            controllerMensagem.setTxtMensagem(mensagem);
            controllerMensagem.setFotoMensagem(usuarioLogado.getFotoCliente());

            vboxConversa.getChildren().add(MensagemGrafica);
        } catch (IOException ex) {
            Logger.getLogger(TelaClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void adicionarMensagemNoList(String mensagem) {
        List<Mensagem> conversaUsuarioLogado = usuarioLogado.getMensagensCliente();
        conversaUsuarioLogado.add(new Mensagem(usuarioLogado.getIdCliente(), contatoConversa.getIdCliente(), mensagem));
        usuarioLogado.setMensagensCliente(conversaUsuarioLogado);
    }

    private void enviarMensagemNaTela(Mensagem mensagem) {
        FXMLLoader loader = new FXMLLoader(messengeraps.MessengerAps.class.getResource("View/FXMLMensagem.fxml")); // FXML
        try {
            Node MensagemGrafica = (Node) loader.load();
            MensagemController controllerMensagem = loader.getController(); // pega controller
            controllerMensagem.setTxtMensagem(mensagem.getMensagem());

            if (mensagem.getIdEnviou() == usuarioLogado.getIdCliente()) {
                controllerMensagem.setFotoMensagem(usuarioLogado.getFotoCliente());
            } else {
                controllerMensagem.setFotoMensagem(verificarFotoMensagem(mensagem.getIdEnviou()).getFotoCliente());
            }

            vboxConversa.getChildren().add(MensagemGrafica);

        } catch (IOException ex) {
            Logger.getLogger(TelaClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarEmojiNaTela(String mensagem) {
        mensagem = configurarStringEmoji(mensagem);

        FXMLLoader loader = new FXMLLoader(messengeraps.MessengerAps.class.getResource("View/FXMLEmoji.fxml")); // FXML
        try {
            Node MensagemGrafica = (Node) loader.load();
            EmojiController controllerEmoji = loader.getController(); // pega controller

            Image imagemEmoji = new Image("/messengeraps/Emoji/" + mensagem + ".png");
            controllerEmoji.setFotoMensagem(usuarioLogado.getFotoCliente());
            controllerEmoji.setFotoEmoji(imagemEmoji); // arrumar

            vboxConversa.getChildren().add(MensagemGrafica);

        } catch (IOException ex) {
            Logger.getLogger(TelaClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarEmojiNaTela(Mensagem mensagem) {
        String emoji = configurarStringEmoji(mensagem.getMensagem());

        FXMLLoader loader = new FXMLLoader(messengeraps.MessengerAps.class.getResource("View/FXMLEmoji.fxml")); // FXML
        try {
            Node MensagemGrafica = (Node) loader.load();
            EmojiController controllerEmoji = loader.getController(); // pega controller

            Image imagemEmoji = new Image("/messengeraps/Emoji/" + emoji + ".png");

            if (mensagem.getIdEnviou() == usuarioLogado.getIdCliente()) {
                controllerEmoji.setFotoMensagem(usuarioLogado.getFotoCliente());
            } else {
                controllerEmoji.setFotoMensagem(verificarFotoMensagem(mensagem.getIdEnviou()).getFotoCliente());
            }

            controllerEmoji.setFotoEmoji(imagemEmoji); // arrumar

            vboxConversa.getChildren().add(MensagemGrafica);

        } catch (IOException ex) {
            Logger.getLogger(TelaClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void configurarInicio() {
        btSend.setDisable(true);
        scrollContatos.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // nunca crescer na horizonta 
        scrollConversa.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        iniciarArrayEmoji();
    }

    private void iniciarArrayEmoji() {
        arrayEmoji = new ArrayList<String>();

        String[] comandosEmoji = {"APAIXONADO.png", "BATATA.png", "CONTINUE.png", "DANADO.png", "FIRE.png", "FOFO.png", "FUCK.png", "GHOST.png",
            "HIHI.png", "IMPRESSIONADO.png", "LOL.png", "LUA.png", "NAO.png", "OK.png", "PENSANTE.png", "PUTO.png", "TEDIO.png", "TROLL.png", "TURN.png",
            "DER.png", "WHAT.png", "PROERD.png", "HUE.png", "NOTBAD.png"};

        for (String n : comandosEmoji) {
            arrayEmoji.add(n);
        }

        System.out.println(arrayEmoji);
    }

    public void verificarQualTipoDeMensagem(String mensagem) {
        String cutMensagem[] = mensagem.split(Pattern.quote(";"));

        if (mensagem.substring(0, 1).equals(";") && cutMensagem.length == 2 && validarEmoji(mensagem)) {
            enviarEmojiNaTela(mensagem);
        } else {
            enviarMensagemNaTela(mensagem);
        }

    }

    private void verificarQualTipoDeMensagem(Mensagem mensagem) {
        String cutMensagem[] = mensagem.getMensagem().split(Pattern.quote(";"));

        if (mensagem.getMensagem().substring(0, 1).equals(";") && cutMensagem.length == 2 && validarEmoji(mensagem.getMensagem())) {
            enviarEmojiNaTela(mensagem);
        } else {
            enviarMensagemNaTela(mensagem);
        }

    }

    public void setContato(Contato usuario) {
        this.usuarioLogado = usuario;
        atualizarInterfaceGrafica();
    }

    public Contato getContato() {
        return this.usuarioLogado;
    }

    public void atualizarInterfaceGrafica() {
        setarDadosUsuario();
        addClientesNaTela(usuarioLogado.getContatosCliente());
        alterarInformacoesConversa();
    }

    public void setarDadosUsuario() {
        nomeContatoLogado.setText(usuarioLogado.getNomeCliente());
        fotoContatoLogado.setFill(usuarioLogado.getFotoCliente());
    }

    public void addClientesNaTela(List<Contato> contatos) {
        if (contatos != null) {
            for (Contato contato : contatos) {
                try {
                    FXMLLoader loader = new FXMLLoader(messengeraps.MessengerAps.class.getResource("View/FXMLContatoLayout.fxml")); // FXML
                    Node layoutContato = (Node) loader.load();

                    LayoutContatoController controllerContatoLayout = loader.getController(); // pega controller da telaClienteController
                    controllerContatoLayout.setContato(contato); // seta o Cliente

                    vboxContatos.getChildren().add(layoutContato); // adiciona na tela
                } catch (IOException ex) {
                    Logger.getLogger(TelaClienteController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

    }

    private boolean validarInputs(String validacao) {
        if (validacao == null || validacao.length() == 0 || validacao.equals(" ") || validacao.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    private String configurarStringEmoji(String emoji) {
        emoji = emoji.substring(1, emoji.length() - 1);
        emoji = emoji.toUpperCase();
        return emoji;
    }

    private boolean validarEmoji(String mensagem) {
        boolean retorno = false;
        String comandoEmoji = configurarStringEmoji(mensagem) + ".png";

        for (String emoji : arrayEmoji) {
            if (emoji.equals(comandoEmoji)) {
                retorno = true;
                break;
            }
        }

        return retorno;
    }

    private void pegarMensagensContatoAtivo(int idContatoAtivo) {
        vboxConversa.getChildren().clear();
        for (Mensagem mensagem : usuarioLogado.getMensagensCliente()) {
            if (mensagem.getIdRecebeu() == idContatoAtivo || mensagem.getIdEnviou() == idContatoAtivo) {
                verificarQualTipoDeMensagem(mensagem);
            }
        }
    }

    private void adicionarMensagemNoBD(String mensagem) {
        MensagemBD.inserirMensagemBD(usuarioLogado.getIdCliente(), contatoConversa.getIdCliente(), mensagem);
    }

    public void setUpMensagemChegou(Mensagem mensagem) {

        adicionarMensagemNoList(mensagem.getMensagem());
        verificarQualTipoDeMensagem(mensagem);

        alterarInformacoesConversa();
        pegarMensagensContatoAtivo(contatoConversa.getIdCliente());

        MensagemBD.inserirMensagemBD(mensagem.getIdEnviou(), usuarioLogado.getIdCliente(), mensagem.getMensagem());

    }

    private void enviarProServidor(String mensagem) {
        usuarioLogado.getPrintWriterCliente().println(contatoConversa.getIdCliente() + ":" + usuarioLogado.getIdCliente() + ":" + mensagem);
        usuarioLogado.getPrintWriterCliente().flush();
    }

    public VBox getVboxConversa() {
        return vboxConversa;
    }

    public void setVboxConversa(VBox vboxConversa) {
        this.vboxConversa = vboxConversa;
    }

    private Contato verificarFotoMensagem(int id) {

        for (int i = 0; i < usuarioLogado.getContatosCliente().size(); i++) {
            if (usuarioLogado.getContatosCliente().get(i).getIdCliente() == id) {
                return usuarioLogado.getContatosCliente().get(i);
            }
        }

        return null;
    }

}
