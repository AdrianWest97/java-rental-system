/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author wests
 */
public interface personInterface  {


    String getFirstName();
    String getLastName();
    String getGender();
    String getEmail();
    

    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setGender(String gender);  
    
    void setEmail(String email);
    
}
