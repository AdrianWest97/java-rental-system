/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Scenes.ChangeScene;
import entities.Landlord;
import entities.Login;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import operations.CrudOperations;
import showMessage.ShowMessage;
import validation.Validate;
import interfaces.ProgramFiles;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author wests
 */
public class LoginSceneController implements Initializable {

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginBtn;
    @FXML
    private Button signUpBtn;
    @FXML
    private TextField landEmail;

    @FXML
    private PasswordField landPass;

    @FXML
    private PasswordField confirmPass;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private RadioButton lmale;

    private Service<Boolean> backgroundThread;
    private Service<String> registerTask;

    @FXML
    private ImageView loader;
    @FXML
    private ImageView loader2;

    @FXML
    private VBox regArea;

    @FXML
    private VBox loginArea;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // TODO
        landEmail.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!Validate.validEmail(newValue)) {
                landEmail.setStyle("-fx-border-color:red");
            } else {
                landEmail.setStyle("-fx-border-color:lightgreen");
            }
        });

        confirmPass.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue == null ? landPass.getText() != null : !newValue.equals(landPass.getText())) {
                confirmPass.setStyle("-fx-border-color:red");
            } else {
                confirmPass.setStyle("-fx-border-color:lightgreen");
            }
        });

    }

    @FXML
    void getLoginData(ActionEvent e) {

        backgroundThread = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {

                        return Login.Login(email.getText(), password.getText());
                    }
                };

            }
        };
        backgroundThread.setOnRunning((WorkerStateEvent event) -> {
            loginBtn.setText("Signing In..");
            loader.setVisible(true);
            loginBtn.setDisable(true);
            regArea.setDisable(true);
        });
        backgroundThread.setOnSucceeded((WorkerStateEvent event) -> {
            boolean success = backgroundThread.getValue();
            if (success) {
                try {
                    String fxml = "/views/MainPanelScene.fxml";
                    String title = "Tenant Registration Application";
                    boolean rs = true;
                    boolean mx = true;
                    ChangeScene changeScene = new ChangeScene(fxml, e, title, rs, mx);
                } catch (IOException ex) {
                    Logger.getLogger(LoginSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                loginBtn.setText("Sign In");
                loader.setVisible(false);
                loginBtn.setDisable(false);
                regArea.setDisable(false);
                ShowMessage.ShowMessage("Info", "Login information", "Password and email does not match");
            }
        });
        backgroundThread.start();

    }

    @FXML
    public void signUp() {
        registerTask = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        Landlord landLord = new Landlord();
                        String gender;
                        if (lmale.isSelected()) {
                            gender = "male";
                        } else {
                            gender = "female";
                        }
                        String msg = "";

                        //validate fields
                        if ("".equals(landEmail.getText()) || "".equals(landPass.getText())
                                || "".equals(confirmPass.getText()) || "".equals(fname.getText()) || "".equals(lname.getText())) {
                            //fill out all fields
                            msg = "Please fill out all fields. Ensure tenant is over 16";
                        } else if (!Validate.validEmail(landEmail.getText())) {
                            msg = "Invalid email";
                        } else if (!confirmPass.getText().equals(landPass.getText())) {
                            msg = "Passwords does not match!";
                            //check if user already exits
                        } else if (new CrudOperations().readObj(ProgramFiles.LOGIN_FILE) != null) {

                            HashMap<String, Landlord> l = (HashMap<String, Landlord>) new CrudOperations().readObj(ProgramFiles.LOGIN_FILE);
                            if (l.get(landEmail.getText().toLowerCase()) != null) {
                                msg = "User email already exits";
                            } else if (landLord.registerLandlord(landEmail.getText(), fname.getText(), lname.getText(), gender, confirmPass.getText())) {
                                msg = "success";
                            } else {
                                msg = "Something went wrong :(";
                            }
                        } else if (landLord.registerLandlord(landEmail.getText(), fname.getText(), lname.getText(), gender, confirmPass.getText())) {
                            msg = "Registration Successfull!";
                        }
                        return msg;
                    }
                };
            }
        };
        registerTask.setOnRunning((WorkerStateEvent event) -> {
            
            loginArea.setDisable(true);
            signUpBtn.setText("Registering...");
            loader2.setVisible(true);

        });
        registerTask.setOnSucceeded((WorkerStateEvent event) -> {
            loginArea.setDisable(false);
            String msg = registerTask.getValue();
            if (msg.equalsIgnoreCase("success")) {
                loader2.setImage(new Image(LoginSceneController.this.getClass().getResource("/images/sticky.png").toString()));
                signUpBtn.setText("Registered");
            } else {
                ShowMessage.ShowMessage("Info", "Lanlord Registration", msg);
                loader2.setVisible(false);
                signUpBtn.setText("Sign Up");
            }
        });
        registerTask.start();
    }
;

}
