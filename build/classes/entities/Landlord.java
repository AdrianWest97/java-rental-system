package entities;

import JPAControllers.LandlordJpaController;
import interfaces.CryptoKeys;
import interfaces.ProgramFiles;
import interfaces.personInterface;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Transient;
import operations.CrudOperations;

import validation.Crypto;

/**
 *
 * @author wests
 */

@Entity
public class Landlord implements personInterface, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
        
    @Column
    private String FirstName;
     @Column
    private String LastName;
       @Column
    private String Gender;
    @Column
    private String email;
    @Column
    private String password;
    
    @Transient
    private LandlordJpaController controller;
   
  @Override
    public String getFirstName() {
        return this.FirstName;
    }

    @Override
    public String getLastName() {
        return this.LastName;
    }

    @Override
    public String getGender() {
        return this.Gender;
    }


    @Override
    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    @Override
    public void setGender(String gender) {
        this.Gender = gender;
    }

    /**
     * @return the email
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }


    public boolean registerLandlord(String email, String fname, String lname, String gender, String password) {
        Landlord landlord = new Landlord();
        //register
//        CrudOperations crud = new CrudOperations();
//        HashMap<String, Landlord> map = null;
//        //read hash map
//        if (crud.readObj(ProgramFiles.LOGIN_FILE) != null) {
//            map = (HashMap<String, Landlord>) crud.readObj(ProgramFiles.LOGIN_FILE);
//            //create new map
//        } else {
//            map = new HashMap();
//        }
            landlord.setEmail(email.toLowerCase());
            landlord.setFirstName(fname);
            landlord.setLastName(lname);
            landlord.setGender(gender);
            landlord.setPassword(Crypto.encrypt(password, CryptoKeys.SECRET_KEY));
   
           // map.put(landlord.getEmail().toLowerCase(), landlord);  
        try {
             EntityManagerFactory emf = Persistence.createEntityManagerFactory("rentalSystemPU");
            System.out.println("Psersitence unit created");
            controller = new LandlordJpaController(emf);    
            return controller.create(landlord);
        } catch (Exception ex) {
            Logger.getLogger(Landlord.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }



    
    }