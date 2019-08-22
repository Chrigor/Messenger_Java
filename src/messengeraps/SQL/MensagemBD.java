package messengeraps.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import messengeraps.Model.Mensagem;

public class MensagemBD extends PrincipalBD {

    public MensagemBD(String urlBD, String userDB, String senhaDB) {
        super(urlBD, userDB, senhaDB);
    }

    public static List<Mensagem> pegarMensagemBD(int idEnviou) {
        List<Mensagem> mensagens = new ArrayList<Mensagem>();
        String comandoSQL = "select * from CONVERSAS where IDENVIOU = ? or IDRECEBEU = ?";

        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, idEnviou);
            execucaoBD.setInt(2, idEnviou);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                String mensagemBD = resultadoSQL.getString("MENSAGEM");
                int idEnviouBD = resultadoSQL.getInt("IDENVIOU");
                int idRecebeu = resultadoSQL.getInt("IDRECEBEU");
                mensagens.add(new Mensagem(idEnviouBD, idRecebeu, mensagemBD));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensagens;
    }

    public static boolean inserirMensagemBD(int idEnviou, int idRecebeu, String mensagem) {
        String comandoSQL = "insert into CONVERSAS values(?,?,?,0)"; // alterar o 0 depois
        boolean retornoUpdate = false;
        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, idEnviou);
            execucaoBD.setInt(2, idRecebeu);
            execucaoBD.setString(3, mensagem);

            execucaoBD.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retornoUpdate;
    }

}
