
package entities;

import JPAControllers.LandlordJpaController;
import JPAControllers.TenantJpaController;
import validation.Crypto;
import interfaces.CryptoKeys;
import interfaces.LoginInterface;
import interfaces.ProgramFiles;
import java.util.ListIterator;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import operations.CrudOperations;
import superclass.Person;

/**
 *
 * @author wests
 */
public class Login extends Person implements LoginInterface {

    private String email;
    private String password;
    private static Login loginUser;
    
   private static LandlordJpaController controller;

    /**
     * @return the email
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * @param username the email to set
     */
    @Override
    public void setEmail(String username) {
        this.email = username;
    }

    /**
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    
    
    public static boolean Login(String email, String pass) {
        
      
        //en
        boolean found;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("rentalSystemPU");
         controller = new LandlordJpaController(emf);
         
         Landlord landlord = null;
       
         ListIterator itr = controller.findLandlordEntities().listIterator();
         while (itr.hasNext()) {
            Landlord l = (Landlord) itr.next();
            if(l.getEmail().equalsIgnoreCase(email)){
                landlord = l;
                break;
            }
            
        }
         
         
        if (landlord == null) {
            
            return false;
            
        } else {
            
            found = Crypto.decrypt(landlord.getPassword(), CryptoKeys.SECRET_KEY).equals(pass);
        }
        
      
        if (found) {
            //login success
            Login user = new Login();
            user.setPassword(pass);
            user.setEmail(email);
            user.setFirstName(landlord.getFirstName());
            user.setLastName(landlord.getLastName());
            user.setGender(landlord.getGender());
            setLoginUser(user);
            
            //write to a file
            CrudOperations co = new CrudOperations();
            co.insertObj(getLoginUser(), ProgramFiles.TEMP_LOGIN);
            
            return true;
        }
        return false;
    }

    /**
     * @return the loginUser
     */
    public static Login getLoginUser() {
        return loginUser;
    }

    /**
     * @param loginUser the loginUser to set
     */
    public static void setLoginUser(Login loginUser) {
        Login.loginUser = loginUser;
    }
}
