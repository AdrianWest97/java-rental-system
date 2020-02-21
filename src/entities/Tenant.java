package entities;

import JPAControllers.TenantJpaController;
import interfaces.ProgramFiles;
import interfaces.personInterface;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import operations.CrudOperations;

@Entity
public class Tenant implements personInterface, Serializable{

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
  
   @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBirth;
   
   @Column 
   private String trn;

    //has a
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Contact contact;
    
    private CalculatePayments payment;

    private String registra;
    
    private static TenantJpaController controller;

    public static boolean Register(String registra, String fn, String ln,
            String gen, String poBoxTemp, String str1Temp, String p1, String TRN, String emailTemp, Date dobTemp,
            String country, String Parish, String City) {

        Tenant tenant = new Tenant();
        tenant.setRegistra(registra);
        tenant.setFirstName(fn);
        tenant.setLastName(ln);
        tenant.setGender(gen);
        tenant.setDateOfBirth(dobTemp);
        tenant.setEmail(emailTemp);

        Contact contact = new Contact();
        CalculatePayments payments = new CalculatePayments();

        contact.setPoBox(poBoxTemp);
        contact.setStreetLine(str1Temp);
        contact.setParish(Parish);
        contact.setCountry(country);
        contact.setCity(City);

        contact.setEmail(emailTemp.toLowerCase());
        contact.setTelephone(p1);
        contact.setTRN(TRN);

        tenant.setContact(contact);
        tenant.setPayment(payments);
       
        try {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("rentalSystemPU");
         controller = new TenantJpaController(emf);
            System.out.println("reaches....");
            return controller.create(tenant);
        } catch (Exception ex) {
            Logger.getLogger(Tenant.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

//   // check if TRN already exits
//    //check of a tenant exits by a specified email
//    public boolean checkTRN(String trn) {
//        Object obj = null;
//        obj = new CrudOperations<>().readObj(ProgramFiles.TENANT_FILE);
//        boolean found = false;
//        if (obj != null) {
//            ArrayList<Tenant> Tlist = (ArrayList<Tenant>) obj;
//            ListIterator iterate = Tlist.listIterator();
//            while (iterate.hasNext()) {
//                Tenant temp = (Tenant) iterate.next();
//                if (temp.getContact().getTRN().equalsIgnoreCase(trn)) {
//                    found = true;
//                    break;
//                }
//            }
//        }
//        return found;
//    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the contact
     */
    public Contact getContact() {
        return contact;
    }

    @Override
    public String toString() {
        return this.getLastName() + " " + this.getDateOfBirth();
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * @return the payment
     */
    public CalculatePayments getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(CalculatePayments payment) {
        this.payment = payment;
    }

    /**
     * @return the registrar
     */
    public String getRegistra() {
        return registra;
    }

    /**
     * @param registra the registrar to set
     */
    public void setRegistra(String registra) {
        this.registra = registra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the FirstName
     */
    @Override
    public String getFirstName() {
        return FirstName;
    }

    /**
     * @param FirstName the FirstName to set
     */
    @Override
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    /**
     * @return the LastName
     */
    @Override
    public String getLastName() {
        return LastName;
    }

    /**
     * @param LastName the LastName to set
     */
    @Override
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    /**
     * @return the Gender
     */
    @Override
    public String getGender() {
        return Gender;
    }

    /**
     * @param Gender the Gender to set
     */
    @Override
    public void setGender(String Gender) {
        this.Gender = Gender;
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

    /**
     * @return the trn
     */
    public String getTrn() {
        return trn;
    }

    /**
     * @param trn the trn to set
     */
    public void setTrn(String trn) {
        this.trn = trn;
    }

  
}
