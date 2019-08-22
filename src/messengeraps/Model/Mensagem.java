package messengeraps.Model;

import messengeraps.SQL.MensagemBD;

public class Mensagem {

    private int idEnviou;
    private int idRecebeu;
    private String mensagem;
    /*private MensagemBD mensagemBD;

    public MensagemBD getMensagemBD() {
        return mensagemBD;
    }

    public void setMensagemBD(MensagemBD mensagemBD) {
        this.mensagemBD = mensagemBD;
    } */

    public Mensagem(int idEnviou, int idRecebeu, String mensagem) {
        this.setIdEnviou(idEnviou);
        this.setIdRecebeu(idRecebeu);
        this.setMensagem(mensagem);
        //this.setMensagemBD(new MensagemBD("jdbc:derby://localhost:1527/MessengerDB","root","root"));
    }

    public int getIdEnviou() {
        return idEnviou;
    }

    public void setIdEnviou(int idEnviou) {
        this.idEnviou = idEnviou;
    }

    public int getIdRecebeu() {
        return idRecebeu;
    }

    public void setIdRecebeu(int idRecebeu) {
        this.idRecebeu = idRecebeu;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "Mensagem{" + "idEnviou=" + idEnviou + ", idRecebeu=" + idRecebeu + ", mensagem=" + mensagem + '}';
    }
    
    
}
