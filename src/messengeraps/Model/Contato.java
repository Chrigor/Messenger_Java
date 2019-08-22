package messengeraps.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import messengeraps.Controller.TelaClienteController;

public class Contato {

    private int idCliente;
    private String nomeCliente;
    private String senhaCliente;
    private String statusCliente;
    private ImagePattern fotoCliente;
    private boolean onlineCliente;
    private List<Contato> contatosCliente;
    private List<Mensagem> mensagensCliente;
    private Socket socketCliente;
    private PrintWriter printWriterCliente;
    private BufferedReader bufferedReaderCliente;
    private TelaClienteController controllerTelaCliente;

    public TelaClienteController getControllerTelaCliente() {
        return controllerTelaCliente;
    }

    public void setControllerTelaCliente(TelaClienteController controllerTelaCliente) {
        this.controllerTelaCliente = controllerTelaCliente;
    }

    public Contato(int id, String nome, String senha, String status, String nomeFoto, boolean online, List<Contato> contatos, List<Mensagem> mensagens) {
        this.setIdCliente(id);
        this.setNomeCliente(nome);
        this.setSenhaCliente(senha);
        this.setStatusCliente(status);
        this.setFotoCliente(nomeFoto);
        this.setOnlineCliente(online);
        this.setContatosCliente(contatos);
        this.setMensagensCliente(mensagens);
    }

    public Contato(int id, String nome, String status, String nomeFoto, boolean online) {
        this.setIdCliente(id);
        this.setNomeCliente(nome);
        this.setStatusCliente(status);
        this.setFotoCliente(nomeFoto);
        this.setOnlineCliente(online);
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getSenhaCliente() {
        return senhaCliente;
    }

    public void setSenhaCliente(String senha) {
        this.senhaCliente = senha;
    }

    public String getStatusCliente() {
        return statusCliente;
    }

    public void setStatusCliente(String statusCliente) {
        this.statusCliente = statusCliente;
    }

    public ImagePattern getFotoCliente() {
        return fotoCliente;
    }

    public void setFotoCliente(String nomeFoto) {
        this.fotoCliente = new ImagePattern(new Image("/messengeraps/Imagens/" + nomeFoto));
    }

    public boolean isOnlineCliente() {
        return onlineCliente;
    }

    public void setOnlineCliente(boolean onlineCliente) {
        this.onlineCliente = onlineCliente;
    }

    public List<Contato> getContatosCliente() {
        return contatosCliente;
    }

    public void setContatosCliente(List<Contato> contatosCliente) {
        this.contatosCliente = contatosCliente;
    }

    public List<Mensagem> getMensagensCliente() {
        return mensagensCliente;
    }

    public void setMensagensCliente(List<Mensagem> mensagensCliente) {
        this.mensagensCliente = mensagensCliente;
    }

    public Socket getSocketCliente() {
        return socketCliente;
    }

    public void setSocketCliente(Socket socketCliente) {
        this.socketCliente = socketCliente;
    }

    public PrintWriter getPrintWriterCliente() {
        return printWriterCliente;
    }

    public void setPrintWriterCliente(PrintWriter printWriterCliente) {
        this.printWriterCliente = printWriterCliente;
    }

    public BufferedReader getBufferedReaderCliente() {
        return bufferedReaderCliente;
    }

    public void setBufferedReaderCliente(BufferedReader bufferedReaderCliente) {
        this.bufferedReaderCliente = bufferedReaderCliente;
    }

    @Override
    public String toString() {
        return "Contato{" + "idCliente=" + idCliente + ", nomeCliente=" + nomeCliente + ", senhaCliente=" + senhaCliente + ", statusCliente=" + statusCliente + ", fotoCliente=" + fotoCliente + ", onlineCliente=" + onlineCliente + ", contatosCliente=" + contatosCliente + ", mensagensCliente=" + mensagensCliente + ", socketCliente=" + socketCliente + ", printWriterCliente=" + printWriterCliente + ", bufferedReaderCliente=" + bufferedReaderCliente + '}';
    }

    public void conectarSocket(String ip, int porta) {
        try {
            Socket clienteSocket = new Socket(ip, porta);
            setSocketCliente(clienteSocket);
            System.out.println("Cliente conectou");
        } catch (IOException ex) {
            System.out.println("Problema ao tentar conexao socket - cliente");
        } catch (Exception e) {
            System.out.println("Problema ao tentar conexao socket - cliente - Unknow error");
        }
    }

    public void desconectarSocket() {
        try {
            getSocketCliente().close();
        } catch (IOException ex) {
            System.out.println("Erro ao desconectar: " + ex.getMessage());
        } catch (Exception e) {
            System.out.println("Erro ao desconectar -  Unknow error");
        }
    }

    public void setUpFluxoDeDados() {
        try {
            InputStreamReader chegada = new InputStreamReader(getSocketCliente().getInputStream()); // pega a entrada de dados
            BufferedReader reader = new BufferedReader(chegada); // converte a entrada pra alto nivel
            PrintWriter writer = new PrintWriter(getSocketCliente().getOutputStream()); // a entrada de dados ligado na saida (servidor)

            setBufferedReaderCliente(reader);
            setPrintWriterCliente(writer);

            Thread escutaMensagem = new Thread(new IncomingReader());
            escutaMensagem.start();

        } catch (IOException ex) {
            Logger.getLogger(Contato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUpAllRede() {
        this.conectarSocket("127.0.0.1", 5500);
        this.setUpFluxoDeDados();
        ObjectOutputStream saida;
        try {
            saida = new ObjectOutputStream(getSocketCliente().getOutputStream()); //
            saida.flush();
            saida.writeObject(new Integer(getIdCliente()));
            System.out.println("Networking established");
        } catch (IOException ex) {
            Logger.getLogger(Contato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class IncomingReader implements Runnable { // classe para se manter ativa lendo

        @Override
        public void run() {
            String mensagemQueChegou;
            System.out.println("Thread Ligado");
            try {
                while ((mensagemQueChegou = bufferedReaderCliente.readLine()) != null) { // se for diferente de null, le
                    try {
                        String[] mensagemEmArray;
                        mensagemEmArray = mensagemQueChegou.split(Pattern.quote(":"));

                        Mensagem mensagem = new Mensagem(Integer.parseInt(mensagemEmArray[0]), getIdCliente(), mensagemEmArray[mensagemEmArray.length - 1]);

                        System.out.println(mensagem.toString());
                        getControllerTelaCliente().setUpMensagemChegou(mensagem);

                    } catch (NumberFormatException e) {
                        System.out.println("Problema na mensagem que chegou: " + e.getMessage());
                    } catch (Exception ex) {
                        System.out.println("Chegade de msg: " + ex.getMessage());
                    }
                    System.out.println("Isso aqui funciona?");
                }

            } catch (IOException ex) {
                System.out.println("Exception sub");
            }
        }
    }

}
