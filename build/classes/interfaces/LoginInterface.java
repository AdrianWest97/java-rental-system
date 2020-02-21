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
public interface LoginInterface {
    void setEmail(String email);
    void setPassword(String password);
    String getEmail();
    String getPassword();
}
