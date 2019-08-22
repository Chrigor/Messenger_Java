package messengeraps.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

public class PrincipalBD {

    private static String urlBD;
    private static String userDB;
    private static String senhaDB;
    private static Connection conexaoBD;

    public PrincipalBD(String urlBD, String userDB, String senhaDB) {
        this.setUrlBD(urlBD);
        this.setUserDB(userDB);
        this.setSenhaDB(senhaDB);
        conectarBD(urlBD, userDB, senhaDB);
    }

    public static String getUrlBD() {
        return urlBD;
    }

    public static void setUrlBD(String urlBD) {
        PrincipalBD.urlBD = urlBD;
    }

    public static String getUserDB() {
        return userDB;
    }

    public static void setUserDB(String userDB) {
        PrincipalBD.userDB = userDB;
    }

    public static String getSenhaDB() {
        return senhaDB;
    }

    public static void setSenhaDB(String senhaDB) {
        PrincipalBD.senhaDB = senhaDB;
    }

    public static Connection getConexaoBD() {
        return conexaoBD;
    }

    public static void setConexaoBD(Connection conexaoBD) {
        PrincipalBD.conexaoBD = conexaoBD;
    }

    public static void conectarBD(String urlBD, String userDB, String senhaDB) {
        try {
            conexaoBD = DriverManager.getConnection(urlBD, senhaDB, senhaDB);
            System.out.println("Conectou no Banco de dados!");
        } catch (SQLException ex) {
            createAlert("Problem in Data Base", "Your BD is off!", "Please, verify with your providor");
        } catch (Exception e) {
            createAlert("Problem in Data Base", "Unknow problem", "Please, verify with your providor");
        }
    }

    private static void createAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.setResizable(false);
        alert.show();
    }

    public static boolean fecharConexao() {
        boolean retorno = false;

        if (isConected()) {
            try {
                conexaoBD.close();
                retorno = true;
            } catch (SQLException ex) {
                Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Não estamos conectados.");
        }

        return retorno;
    }

    public static boolean isConected() {
        boolean retorno = false;
        try {
            retorno = !(conexaoBD.isClosed());
        } catch (SQLException ex) {
            Logger.getLogger(ContatoBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return retorno;
    }

}
