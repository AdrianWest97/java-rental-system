/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showMessage;

import javafx.scene.control.Alert;

/**
 *
 * @author wests
 */
//displays a popup on screen
public class ShowMessage {
    
     public static void ShowMessage(String title, String Headertext, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(Headertext);
        alert.setContentText(context);
        alert.showAndWait();
    }
}
