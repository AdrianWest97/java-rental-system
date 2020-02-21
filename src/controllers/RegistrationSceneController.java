/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import validation.Validate;
import entities.Login;
import entities.Tenant;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import showMessage.ShowMessage;
import interfaces.ProgramFiles;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author wests
 */
public class RegistrationSceneController implements Initializable {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;

    @FXML
    private RadioButton male;

    @FXML
    private DatePicker dob;
    @FXML
    private TextField email;
    @FXML
    private TextField trn;
    @FXML
    private TextField phone1;

    @FXML
    private TextField phone2;

    @FXML
    private ChoiceBox country;
    @FXML
    private ChoiceBox city;
    @FXML
    private ChoiceBox parish;
    @FXML
    private TextField str1;
    @FXML
    private TextField poBox;
    DateTimeFormatter formatter;
    @FXML
    private ImageView photo;

    @FXML
    private ImageView loader;

    @FXML
    private Button registerbtn;

    private BufferedImage image;
    private File imageFile;
    private String extension = "";

    Service<String> registerTask;
    Service<Void> imageUploadTask;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        country.getItems().addAll("Jamaica");
        //country.getItems().set(0, "Country");

        city.getItems().addAll(
                "Kingston",
                "Mandeville",
                "Montego Bay",
                "Ochi Rios",
                "May Pen"
        );

        parish.getItems().addAll(
                "Kingston",
                "Manchester",
                "St. Ann",
                "St. Catherine",
                "St. Andrew",
                "St. Elizabeth",
                "Hanova",
                "Clarendon",
                "St. James"
        );

        email.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!Validate.validEmail(newValue)) {
                email.setStyle("-fx-border-color:red");
            } else {
                email.setStyle("-fx-border-color:-swatch-500");
            }
        }));

        phone1.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!Validate.validPhone(newValue)) {
                phone1.setStyle("-fx-border-color:red");
            } else {
                phone1.setStyle("-fx-border-color:-swatch-500");
            }
        }));
        phone2.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!Validate.validPhone(newValue)) {
                phone2.setStyle("-fx-border-color:red");
            } else {
                phone2.setStyle("-fx-border-color:-swatch-500");
            }
        }));

        trn.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!Validate.validTRN(newValue)) {
                trn.setStyle("-fx-border-color:red");
            } else {
                trn.setStyle("-fx-border-color:-swatch-500");
            }
        }));

    }

    @FXML
    private void validateAndRegister(ActionEvent event) {

        registerTask = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        Login l = Login.getLoginUser();
                        String fn, ln, gen = null, poBoxTemp, counTemp, cityTemp, parishTemp, str1Temp, p1, p2, TRN;
                        String emailTemp;
                        Date dobTemp;
                        fn = firstName.getText();
                        ln = lastName.getText();
                        poBoxTemp = poBox.getText();
                        counTemp = (String) country.getValue();
                        cityTemp = (String) city.getValue();
                        parishTemp = (String) parish.getValue();
                        str1Temp = str1.getText();
                        emailTemp = email.getText();
                        String date  = dob.getValue().toString();
                        Date d = new Date(date);
                        dobTemp = d;
                        p1 = phone1.getText();
                        p2 = phone2.getText();
                        TRN = trn.getText();
                        boolean success = false;
                        String msg = "";
                        if ("".equals(fn) || "".equals(ln) || "".equals(poBoxTemp) || "".equals(counTemp)
                                || "".equals(cityTemp) || "".equals(parishTemp) || "".equals(str1Temp)
                                || "".equals(emailTemp) || "".equals(dobTemp.toString())) {
                            msg = "Ensure all fields are filled out, and tenant is older than 16";
                        } else if (!Validate.validDob(dob.getValue().toString())) {
                            msg = "Check Tenant Date of Bith";
                        } else if (!Validate.validEmail(emailTemp)) {
                            msg = "Email address is invalid";
                        } else if (!Validate.validPhone(p1)) {
                            msg = "Phone number format is incorrect";
                        } else if (!Validate.validTRN(TRN)) {
                            msg = "TRN is invalid";
                        } else {
                            if (male.isSelected()) {
                                gen = "male";
                            } else {
                                gen = "female";
                            }

                            success = Tenant.Register(Login.getLoginUser().getEmail(), fn, ln, gen, poBoxTemp, str1Temp, p1, TRN, emailTemp,
                                    dobTemp, counTemp, parishTemp, cityTemp);
                            if (success) {
                                //save image 
                                saveFile(getImage(), getExtension(), getImageFile());
                                msg = "s";
                            }else{
                                msg="Email entered already exists";
                            }
                        }
                        return msg;
                    }
                };
            }
            //saveFile(getImage(), getExtension(), getImageFile());
        };
        registerTask.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                loader.setVisible(true);
                registerbtn.setText("Processing..");

            }
        });
        registerTask.setOnSucceeded((WorkerStateEvent event1) -> {
            String msg = registerTask.getValue();
            if (msg.equals("s")) {
                loader.setImage(new Image(getClass().getResource("/images/sticky.png").toString()));
                registerbtn.setText("Registered");
                
            } else {
                loader.setVisible(false);
                registerbtn.setText("Register");
                ShowMessage.ShowMessage("Info", "Validation", msg);
            }
        });

        registerTask.start();

    }

    @FXML
    public void uploadImage(ActionEvent event) {
        Image img = null;
        File file = null;

        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG")
        );
        chooser.setTitle("Tenant Profile photo");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        file = chooser.showOpenDialog(stage);
        if (file != null) {
            try {
                BufferedImage bfimg = ImageIO.read(file.getAbsoluteFile());
                String imagePath = FilePath(file.getAbsolutePath());
                img = new Image(file.toURI().toString());
                File toWrite = new File(imagePath);
                int i = imagePath.lastIndexOf('.');
                String ext = "";
                //set the image location
                if (i >= 0) {
                    ext = imagePath.substring(i + 1);
                }
                setImageFile(toWrite);
                setImage(bfimg);
                setExtension(ext);
                //save image
                photo.setImage(img);
            } catch (IOException ex) {
                Logger.getLogger(RegistrationSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String FilePath(String path) {
        File f = new File(ProgramFiles.IMAGE_DIR);
        String filep = f.getAbsolutePath();
        String strNew = filep.replace(ProgramFiles.IMAGE_DIR, "\\");
        String ext = "";
        int i = path.lastIndexOf('.');
        if (i >= 0) {
            ext = path.substring(i + 1);
        }
        return strNew + "\\" + trn.getText() + "." + ext;
    }

    //save the image file
    private boolean saveFile(BufferedImage image, String ext, File toWrite) {
        if (toWrite != null) {
            if (!toWrite.exists()) {
                try {
                    toWrite.getAbsoluteFile().getParentFile().mkdirs();
                    toWrite.createNewFile();
                    ImageIO.write(image, ext, toWrite);
                    return true;
                } catch (IOException ex) {
                    Logger.getLogger(RegistrationSceneController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return false;
    }

    /**
     * @return the image
     */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * @return the imageFile
     */
    public File getImageFile() {
        return imageFile;
    }

    /**
     * @param imageFile the imageFile to set
     */
    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * @return the extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * @param extension the extension to set
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
}
