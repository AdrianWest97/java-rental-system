/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scenes;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author wests
 */
public class ChangeScene {

    public ChangeScene(String fxml, ActionEvent event, String title, boolean resizable, boolean mx) throws IOException {
        //go to new scene
        Parent mainScene = FXMLLoader.load(getClass().getResource(fxml));
        // Stage stage = new Stage();
        Scene scene = new Scene(mainScene);
        
        //stage.setTitle(title);
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        mainStage.setScene(scene);
        mainStage.setTitle(title);
        mainStage.setResizable(resizable);
        mainStage.setMaximized(mx);
        mainStage.getIcons().add(new Image(getClass().getResource("/images/3d8dbc5f-d9d6-4a39-9bd9-2cbceb8fa081.png").toString()));
        mainStage.show();
    }
   
}
