package interfaces;

/**
 *
 * @author Adrian Wests
 */
public interface ContactDetailsInterface {

    void setId(int id);
    void setStreetLine(String streetLine);

    void setCity(String city);

    void setParish(String parish);

    void setCountry(String Country);

    void setPoBox(String poBox);

    void setEmail(String email);

    void setTelephone(String tel);

    void setTelephone2(String tel2);

    void setTRN(String trn);

    int getId();
    String getStreetLine();

    String getCity();

    String getParish();

    String getCountry();

    String getPoBox();

    String getEmail();

    String getTelephone();

    String getTel2();

    String getTRN();

}
