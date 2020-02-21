/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import JPAControllers.TenantJpaController;
import operations.CrudOperations;
import Scenes.ChangeScene;
import comparator.SortList;
import entities.Login;
import entities.Tenant;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import interfaces.ProgramFiles;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import operations.ReturnPhoto;

/**
 * FXML Controller class
 *
 * @author wests
 */
public class MainPanelSceneController implements Initializable {

    @FXML
    private TableView tenantTable;

    @FXML
    private Label sia;

    ChangeScene changeScene;

    @FXML
    private VBox side_view;

    @FXML
    private ImageView t_image;

    @FXML
    private Label t_name;

    @FXML
    private Label t_email;

    @FXML
    private Label t_trn;

    @FXML
    private Label t_phone;

    @FXML
    private TextField search;

    private String selectedTenant;

    @FXML
    private Label amtDue2;

    @FXML
    private Label dueDate;

    private final ObservableList<Tenant> observableList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        sia.setText("Logged in as " + Login.getLoginUser().getFirstName() + " " + Login.getLoginUser().getLastName());
        //firstName
        TableColumn<Tenant, String> fnColumn = new TableColumn<>("First Name");
        fnColumn.setCellValueFactory(new PropertyValueFactory<>("FirstName"));

        //lastName
        TableColumn<Tenant, String> lnColumn = new TableColumn<>("Last Name");
        lnColumn.setCellValueFactory(new PropertyValueFactory<>("LastName"));

        //gender
        TableColumn<Tenant, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("Gender"));

        //dob
        TableColumn<Tenant, String> dobColumn = new TableColumn<>("D.O.B");
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        //Email
        TableColumn<Tenant, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("Email"));

        emailColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getEmail()));

        //Phone
        TableColumn<Tenant, String> phoneColumn = new TableColumn<>("Phone #");
        phoneColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getTelephone()));

        TableColumn<Tenant, String> TRNColumn = new TableColumn<>("TRN");
        TRNColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getTRN()));

        //Country
        TableColumn<Tenant, String> countryColumn = new TableColumn<>("Country");
        countryColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getCountry()));

        TableColumn<Tenant, String> parishColumn = new TableColumn<>("Parish");
        parishColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getParish()));

        TableColumn<Tenant, String> cityColumn = new TableColumn<>("City");
        cityColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getCity()));

        TableColumn<Tenant, String> strtColumn = new TableColumn<>("Street Line");
        strtColumn.setCellValueFactory((TableColumn.CellDataFeatures<Tenant, String> p) -> new SimpleStringProperty(p.getValue().getContact().getStreetLine()));
        tenantTable.setItems(GetTenantsDefaultSort());
        //filtered search
        FilteredList<Tenant> filteredData = new FilteredList<>(observableList, t -> true);
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Tenant -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every tenant with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                //Compare first name and last name of every tenant with filter text.
                if (Tenant.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.

                } else if (Tenant.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.

                }
                return false; // Does not match.
            });
        });
        //Wrap the FilteredList in a SortedList. 
        SortedList<Tenant> sortedData = new SortedList<>(filteredData);
        //  Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tenantTable.comparatorProperty());
        //  Add sorted (and filtered) data to the table.
        tenantTable.setItems(sortedData);
        tenantTable.getColumns()
                .addAll(fnColumn, lnColumn, genderColumn, dobColumn, emailColumn, TRNColumn, phoneColumn, countryColumn, parishColumn, cityColumn, strtColumn);

        try {
            tenantTable.getSelectionModel().selectedItemProperty()
                    .addListener(new ChangeListener<Tenant>() {
                        @Override
                        public void changed(ObservableValue<? extends Tenant> observable, Tenant oldValue, Tenant newValue) {
                            if (newValue != null) {
                                side_view.setVisible(true);
                                populateSidePanel(newValue.getId());

                            }
                        }

                    });
        } catch (NullPointerException ex) {
            System.out.println(ex);
        }
//set defualt selected
        tenantTable.getSelectionModel().select(0);

    }

    public void populateSidePanel(int id) {
        DecimalFormat df = new DecimalFormat(".##");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM, yyyy");
        //retrive email
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("rentalSystemPU");
         TenantJpaController controller = new TenantJpaController(emf);
        Tenant t = controller.findTenant(id);
        Image img = ReturnPhoto.ReturnPhoto(t.getContact().getTRN());
        t_image.setImage(img);
        t_email.setText("Email: " + t.getContact().getEmail());
        t_trn.setText("TRN: " + t.getContact().getTRN());
        t_phone.setText("Phone#: " + t.getContact().getTelephone());
        t_name.setText("Name: " + t.getFirstName() + " " + t.getLastName());
        amtDue2.setText("Amount Due: $ " + df.format(t.getPayment().getAmountDue()));
        dueDate.setText("Due date: " + sdf.format(t.getPayment().getNextPayDate()));

    }

    @FXML
    private void showRegForm(ActionEvent event) throws IOException {
        //go to new scene
        Parent RegForm = FXMLLoader.load(getClass().getResource("/views/RegistrationScene.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(RegForm);
        stage.setTitle("Register Tenant");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/images/3d8dbc5f-d9d6-4a39-9bd9-2cbceb8fa081.png").toString()));

        stage.setScene(scene);
        stage.show();
    }

    public void reloadTable() {
        observableList.clear();
        tenantTable.refresh();
        GetTenantsDefaultSort();
    }

    @FXML
    private void refreshTable() {
        reloadTable();
    }

    @FXML
    void sortByDate(ActionEvent event) {
        observableList.clear();
        tenantTable.refresh();
        GetTenantsByDob();
    }

    @FXML
    void sortByLastName(ActionEvent event) {
        reloadTable();
    }

    @FXML
    void makePaymentsView(ActionEvent event) throws IOException {
        Button data = (Button) event.getSource();

        //go to new scene
        Parent paymentForm = FXMLLoader.load(getClass().getResource("/views/PaymentsScene.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(paymentForm);
        stage.setTitle("Make payments");
        stage.setResizable(false);
        stage.getIcons().add(new Image(getClass().getResource("/images/3d8dbc5f-d9d6-4a39-9bd9-2cbceb8fa081.png").toString()));

        stage.setScene(scene);
        stage.setUserData(data.getId());
        //stage.setUserData(data);
        stage.show();
    }

    @FXML
    void logout(ActionEvent event) {
        try {
            //change scene
            Login.setLoginUser(null);
            //update file
            CrudOperations co = new CrudOperations();
            try{
                
                    File file = new File(ProgramFiles.TEMP_LOGIN);
                    file.delete();
                    
            }catch(Exception ex){
                //something..
            }
            //switch scene
            String title = "Login";
            String fxml = "/views/LoginScene.fxml";
            boolean rs = false;
            boolean mx = false;
            new ChangeScene(fxml, event, title, rs, mx);
        } catch (IOException ex) {
            Logger.getLogger(MainPanelSceneController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //by last name
    public ObservableList GetTenantsDefaultSort() {
      EntityManagerFactory emf = Persistence.createEntityManagerFactory("rentalSystemPU");
     TenantJpaController controller = new TenantJpaController(emf);
        
        List<Tenant> arr =  controller.findTenantEntities();
        SortList sort = new SortList();
        if (arr != null) {
            ListIterator itr = arr.listIterator();
            while (itr.hasNext()) {
                Tenant t = (Tenant) itr.next();
                if (t.getGender().equals("male")) {
                    t.setGender("M");
                } else {
                    t.setGender("F");
                }
                if (t.getRegistra().equals(Login.getLoginUser().getEmail())) {
                    observableList.add(t);
                }
            }
            Collections.sort(observableList, sort.sortByLastName);
        }

        return observableList;
    }

    //BY date of bith
    //by last name
    public ObservableList GetTenantsByDob() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("rentalSystemPU");
     TenantJpaController controller = new TenantJpaController(emf);
        
        List<Tenant> arr =  controller.findTenantEntities();
        SortList sort = new SortList();
        if (arr != null) {
            ListIterator itr = arr.listIterator();
            while (itr.hasNext()) {
                Tenant t = (Tenant) itr.next();
                if (t.getGender().equals("male")) {
                    t.setGender("M");
                } else {
                    t.setGender("F");
                }
                if (t.getRegistra().equals(Login.getLoginUser().getEmail())) {
                    observableList.add(t);
                }
            }
            Collections.sort(observableList, sort.sortByDateOfBirth);
        }

        return observableList;
    }

    /**
     * @return the selectedTenant
     */
    public String getSelectedTenant() {
        return selectedTenant;
    }

    /**
     * @param selectedTenant the selectedTenant to set
     */
    public void setSelectedTenant(String selectedTenant) {
        this.selectedTenant = selectedTenant;
    }
}
