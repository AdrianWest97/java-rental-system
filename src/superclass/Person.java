
package superclass;
import interfaces.personInterface;
import java.io.Serializable;
import javax.persistence.Column;


//superclass 

public class Person implements personInterface, Serializable{
  
    @Column
    private String FirstName;
     @Column
    private String LastName;
       @Column
    private String Gender;
    @Column
    private String email;
   
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

   

}
