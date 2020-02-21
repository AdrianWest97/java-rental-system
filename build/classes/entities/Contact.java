
package entities;

import interfaces.ContactDetailsInterface;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author wests
 */
@Entity
public class Contact implements ContactDetailsInterface, Serializable {
    //private static final long serialVersionUID = 3458489886428771425L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String StreetLine;
    private String City;
    private String Parish;
    private String Country;
    private String Email;
    private String Telephone;
    private String Telephone2;
    private String TRN;
    private String PoBox;
    

    /**
     * @return the StreetLine
     */
    @Override
    public String getStreetLine() {
        return StreetLine;
    }

    /**
     * @param StreetLine the StreetLine to set
     */
    @Override
    public void setStreetLine(String StreetLine) {
        this.StreetLine = StreetLine;
    }

    /**
     * @return the City
     */
    @Override
    public String getCity() {
        return City;
    }

    /**
     * @param City the City to set
     */
    @Override
    public void setCity(String City) {
        this.City = City;
    }

    /**
     * @return the Parish
     */
    @Override
    public String getParish() {
        return Parish;
    }

    /**
     * @param Parish the Parish to set
     */
    @Override
    public void setParish(String Parish) {
        this.Parish = Parish;
    }

    /**
     * @return the Country
     */
    @Override
    public String getCountry() {
        return Country;
    }

    /**
     * @param Country the Country to set
     */
    @Override
    public void setCountry(String Country) {
        this.Country = Country;
    }

    /**
     * @return the Email
     */
    @Override
    public String getEmail() {
        return Email;
    }

    /**
     * @param Email the Email to set
     */
    @Override
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     * @return the Telephone
     */
    @Override
    public String getTelephone() {
        return Telephone;
    }

    /**
     * @param Telephone the Telephone to set
     */
    @Override
    public void setTelephone(String Telephone) {
        this.Telephone = Telephone;
    }

    @Override
    public void setTRN(String trn) {
        this.TRN = trn;
    }

    @Override
    public String getTRN() {
     return TRN; 
    }

    @Override
    public void setTelephone2(String tel2) {
        this.Telephone2 = tel2;
    }

 
    @Override
    public String getTel2() {
        return Telephone2;
    }

    /**
     * @return the PoBox
     */
    @Override
    public String getPoBox() {
        return PoBox;
    }

    /**
     * @param PoBox the PoBox to set
     */
    @Override
    public void setPoBox(String PoBox) {
        this.PoBox = PoBox;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }
    
}
