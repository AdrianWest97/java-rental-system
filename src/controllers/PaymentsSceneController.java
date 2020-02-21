/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import SendMail.SendEmail;
import entities.Login;
import entities.Tenant;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import operations.CrudOperations;
import operations.GenerateReciept;
import operations.ReturnPhoto;
import operations.makePayments;

/**
 * FXML Controller class
 *
 * @author wests
 */
public class PaymentsSceneController implements Initializable {

    @FXML
    private TextField id;

    @FXML
    private Label name;

    @FXML
    private VBox resultArea;

    @FXML
    private VBox paymentsArea;

    @FXML
    private Label lastPayment;

    @FXML
    private Label daysLate;

    @FXML
    private Label lateFee;

    @FXML
    private Label nextDate;

    @FXML
    private Label discount;

    @FXML
    private Label oustanding;

    @FXML
    private Label amtDue;

    @FXML
    private TextField valueAmt;

    @FXML
    private ImageView avatar;

    @FXML
    private Button payBtn;

    private Tenant tenant;

    @FXML
    private Label flagMsg;

    @FXML
    private VBox recieptArea;

    @FXML
    private Label amtPayed;

    @FXML
    private Label change;

    @FXML
    private Label balance;

    private String RecieptLocation;

    private String reciepient;

    @FXML
    private ImageView loader;
    @FXML
    private Button emailBtn;

    @FXML
    private Label successMsg;

    Service<Boolean> sendEmailThread;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO       
        resultArea.setVisible(false);
        valueAmt.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            boolean flag;
            try {
                Double.parseDouble(oldValue);
                Double.parseDouble(newValue);
                flag = true;
            } catch (NumberFormatException ex) {
                flag = false;
            }
            if (!flag) {
                int i = 0;
                valueAmt.setStyle("-fx-border-color:red");
                payBtn.setDisable(true);
            } else {
                valueAmt.setStyle("-fx-border-color:green");
                payBtn.setDisable(false);
            }

        });

    }

    @FXML
    void searchTenant(ActionEvent event) {
        //stage.setTitle(title);
        // Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String data = id.getText();
        if (!"".equals(data)) {
            //search for tenant

            try {
                setTenant(new CrudOperations().retriveByEmail(data.toLowerCase()));

                if (getTenant() != null) {
                    //print user
                    //populate fields
                    resultArea.setVisible(true);
                    paymentsArea.setOpacity(1);
                    valueAmt.setDisable(false);
                    if (getTenant().getRegistra().equals(Login.getLoginUser().getEmail())) {
                        // flagMsg.setVisible(false);
                        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
                        Image av = null;
                        try {
                            //search for tenant photo by there email
                            av = ReturnPhoto.ReturnPhoto(getTenant().getContact().getTRN());
                            avatar.setImage(av);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        name.setText(getTenant().getFirstName() + " " + getTenant().getLastName());
                        String LDate = "-";
                        if (getTenant().getPayment().getLastPayDate() != null) {
                            LDate = format.format(getTenant().getPayment().getLastPayDate());
                        }
                        String NDate = format.format(getTenant().getPayment().getNextPayDate());

                        DecimalFormat df = new DecimalFormat(".##");

                        lastPayment.setText("Last payment Date: " + LDate);
                        daysLate.setText("Days Late: " + getTenant().getPayment().getTotalDaysLate());
                        lateFee.setText("Late fee: $" + df.format(getTenant().getPayment().getLateFee()));
                        nextDate.setText("Next payment Date: " + NDate);
                        discount.setText("Discount Amount: $" + df.format(getTenant().getPayment().getDiscAmount()));
                        oustanding.setText("Oustanding Amount: $" + df.format(getTenant().getPayment().getOutsAmt()));
                        amtDue.setText("Amount Due: $" + df.format(getTenant().getPayment().getAmountDue()));

                    } else {
//                        flagMsg.setText("No result");
//                        flagMsg.setVisible(true);
                        resultArea.setVisible(false);
                        paymentsArea.setOpacity(0.52);
                        payBtn.setDisable(true);
                        valueAmt.setDisable(true);
                        flagMsg.setVisible(true);

                        flagMsg.setText("Not found..");
                    }
                }

            } catch (NullPointerException ex) {
                System.out.println(" error 1: " + ex);
            }
        }

    }

    @FXML
    void payRent(ActionEvent event) {

        double amt = Double.parseDouble(valueAmt.getText());
        DecimalFormat df = new DecimalFormat(".##");
        if (amt > 0) {
            //make payment function
            if (getTenant() != null) {
                makePayments pay = new makePayments();
                if (pay.makePayments(getTenant(), amt)) {
                    //generate reciept
                    Tenant t = new CrudOperations().retriveByEmail(getTenant().getContact().getEmail());

                    setRecieptLocation(GenerateReciept.GenerateReciept(t, amt));
                    setReciepient(t.getContact().getEmail());

                    //reciept area
                    amtPayed.setText("Amount payed:    $" + df.format(amt));
                    balance.setText("Balance Overdue: $" + df.format(t.getPayment().getOutsAmt()));
                    change.setText("Change:          $" + df.format(t.getPayment().getChange()));
                    recieptArea.setVisible(true);

                }
            }
        }

    }

    @FXML
    void sendEmail(ActionEvent event) {

        sendEmailThread = new Service<Boolean>() {
            @Override
            protected Task<Boolean> createTask() {
                return new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        SendEmail mail = new SendEmail();
                        return mail.sendFromGMail(getReciepient(), "Rent Reciept", "Receipt for recent payment", getRecieptLocation());
                    }
                };
            }
        ;
        };
        sendEmailThread.setOnRunning((WorkerStateEvent event1) -> {
            loader.setVisible(true);
            emailBtn.setText("Sending..");
            payBtn.setDisable(true);
        });

        sendEmailThread.setOnSucceeded((WorkerStateEvent event1) -> {
            if (sendEmailThread.getValue()) {
                loader.setImage(new Image(getClass().getResource("/images/sticky.png").toString()));
                emailBtn.setText("Sent");
            } else {
                emailBtn.setText("Email Tenant Reciept");
                successMsg.setVisible(true);
                successMsg.setText("Something went wrong while sending email");
                loader.setVisible(false);
            }
            payBtn.setDisable(false);
        });
        sendEmailThread.start();
    }

    @FXML
    void openDocumentLocation(ActionEvent event) {
        if (getRecieptLocation() != null) {
            //locate the file
            File f = new File(getRecieptLocation());
            try {
                Desktop.getDesktop().open(f);
            } catch (IOException ex) {
                Logger.getLogger(PaymentsSceneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @return the RecieptLocation
     */
    public String getRecieptLocation() {
        return RecieptLocation;
    }

    /**
     * @param RecieptLocation the RecieptLocation to set
     */
    public void setRecieptLocation(String RecieptLocation) {
        this.RecieptLocation = RecieptLocation;
    }

    /**
     * @return the reciepient
     */
    public String getReciepient() {
        return reciepient;
    }

    /**
     * @param reciepient the reciepient to set
     */
    public void setReciepient(String reciepient) {
        this.reciepient = reciepient;
    }

    /**
     * @return the tenant
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * @param tenant the tenant to set
     */
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

}
