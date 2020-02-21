package Scenes;

import entities.Login;
import interfaces.ProgramFiles;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import operations.CrudOperations;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        String sc = "";
        String title = "";
        boolean rz=false,mx=false;
        
       //check if user already logged in
        CrudOperations co = new CrudOperations();
        try{
            Login obj = (Login) co.readObj(ProgramFiles.TEMP_LOGIN);
            if(obj!=null && obj.getEmail()!=null && obj.getPassword()!=null){
              
                Login.setLoginUser(obj);
                
                //go to main panel if user is already logged in
                sc = "/Views/MainPanelScene.fxml";
                title ="Tenant Mangement System";
                rz = true;
                mx=true;  
            }else{
                //go to login screen
                sc = "/Views/LoginScene.fxml";
                title ="Tenant Mangement System - Login";
                rz = false;
                mx=false;  
            }
            
        }catch(Exception ex){
            
            //log something
        }
        
        
        Parent root = null;
        try {
              root = FXMLLoader.load(getClass().getResource(sc));    
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        primaryStage.setTitle(title);
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/3d8dbc5f-d9d6-4a39-9bd9-2cbceb8fa081.png").toString()));
        primaryStage.setScene(scene);
        primaryStage.setResizable(rz);
        primaryStage.setMaximized(mx);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }

}
