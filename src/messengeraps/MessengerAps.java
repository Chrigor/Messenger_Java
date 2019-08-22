/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengeraps;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MessengerAps extends Application {
    
    public static Stage tela; 
            
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("View/FXMLogin.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT); // retira os 3 buttons padroes
        stage.getIcons().add(new Image("/messengeraps/Imagens/icon.png"));
        stage.show();
        tela = stage; 
    }
    

    public static void main(String[] args) {
        launch(args);
    }
    
}
