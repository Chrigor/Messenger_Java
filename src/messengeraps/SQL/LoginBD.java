package messengeraps.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginBD extends PrincipalBD {


    public LoginBD(String urlBD, String userDB, String senhaDB) {
        super(urlBD, userDB, senhaDB);
    }


    public static boolean logarBD(int id, String senha) {
        boolean logouSQL = false;

        if (ContatoBD.verificarExistenciaContato(id) && PrincipalBD.isConected()) {
            String comandoSQL = "select ID, SENHA from pessoas where ID = ?";

            try {
                PreparedStatement execucaoBD = getConexaoBD().prepareStatement(comandoSQL);
                execucaoBD.setInt(1, id);

                ResultSet resultadoSQL = execucaoBD.executeQuery();

                while (resultadoSQL.next()) {
                    int idSQL = resultadoSQL.getInt("ID");
                    String senhaSQL = resultadoSQL.getString("SENHA");

                    if (id == idSQL && senha.equals(senhaSQL)) {
                        logouSQL = true;
                        break;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LoginBD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return logouSQL;
    }

}
