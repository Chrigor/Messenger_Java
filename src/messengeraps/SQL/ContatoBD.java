package messengeraps.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import messengeraps.Model.Contato;

public class ContatoBD extends PrincipalBD {

    public ContatoBD(String urlBD, String userDB, String senhaDB) {
        super(urlBD, userDB, senhaDB);
    }

    public static Contato pegarContato(int id) {
        Contato retornoContato;
        if (verificarExistenciaContato(id) && isConected()) {
            try {
                retornoContato = new Contato(id, getNomeContatoBD(id), getStatusContatoBD(id), getFotoContatoBD(id), isOnlineContatoBD(id));
            } catch (Exception e) {
                retornoContato = new Contato(id, getNomeContatoBD(id), getStatusContatoBD(id), "padrao.jpg", isOnlineContatoBD(id));
            }
            return retornoContato;
        } else {
            return null;
        }
    }

    public static Contato pegarUsuarioLogado(int id) {
        Contato retornoContato = null;
        if (verificarExistenciaContato(id) && isConected()) {
            try {
                retornoContato = new Contato(id, getNomeContatoBD(id), getSenhaContatoBD(id), getStatusContatoBD(id), getFotoContatoBD(id),
                        isOnlineContatoBD(id), pegarListContatos(id), MensagemBD.pegarMensagemBD(id));
            } catch (Exception e) {
                retornoContato = new Contato(id, getNomeContatoBD(id), getSenhaContatoBD(id), getStatusContatoBD(id), "padrao.jpg",
                        isOnlineContatoBD(id), pegarListContatos(id), MensagemBD.pegarMensagemBD(id));
            }
        }
        System.out.println("Usuario que logou: " + retornoContato.getMensagensCliente());
        return retornoContato;
    }

    public static List<Contato> pegarListContatos(int id) {
        String comandoSQL = "select IDPOSSUI, IDCONTATO from contatos where IDPOSSUI = ?";
        Contato contatoAmigoBD;
        List<Contato> contatosBD = new ArrayList<Contato>();

        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                int idAmigoContato = resultadoSQL.getInt("IDCONTATO");
                contatoAmigoBD = pegarContato(idAmigoContato);
                contatosBD.add(contatoAmigoBD);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return contatosBD;
    }

    public static String getSenhaContatoBD(int id) {
        String comandoSQL = "select SENHA from pessoas where ID = ?";
        String senhaSQL = null;
        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                senhaSQL = resultadoSQL.getString("SENHA");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return senhaSQL;
    }

    public static String getNomeContatoBD(int id) {
        String comandoSQL = "select NOME from pessoas where ID = ?";
        String nomeSQL = null;
        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                nomeSQL = resultadoSQL.getString("nome");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return nomeSQL;
    }

    public static String getStatusContatoBD(int id) {
        String comandoSQL = "select STATUS from pessoas where ID = ?";
        String statusSQL = null;
        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                statusSQL = resultadoSQL.getString("STATUS");
                System.out.println(statusSQL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return statusSQL;
    }

    public static String getFotoContatoBD(int id) {
        String comandoSQL = "select IMAGEM from pessoas where ID = ?";
        String imagemSQL = null;
        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                imagemSQL = resultadoSQL.getString("IMAGEM");
                System.out.println(imagemSQL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return imagemSQL;
    }

    public static boolean isOnlineContatoBD(int id) {
        String comandoSQL = "select ONLINE from pessoas where ID = ?";
        boolean onlineSQL = false;

        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                onlineSQL = resultadoSQL.getBoolean("ONLINE");
                System.out.println(onlineSQL);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return onlineSQL;
    }

    public static boolean verificarExistenciaContato(int id) {
        String comandoSQL = "select count(*) from pessoas where ID = ?";
        boolean existeContato = false;
        try {
            PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
            execucaoBD.setInt(1, id);

            ResultSet resultadoSQL = execucaoBD.executeQuery();

            while (resultadoSQL.next()) {
                int quantidade = resultadoSQL.getInt("1");
                if (quantidade != 0) {
                    existeContato = true;
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return existeContato;
    }

}
