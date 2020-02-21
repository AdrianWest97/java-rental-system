/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author wests
 */
public class Validate {

    static Pattern pat;
    static SimpleDateFormat dateFormat;

    public static boolean validEmail(String email) {
        String regEx = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pat.matcher(email);
        return matcher.find();
    }

    public static boolean validPhone(String phone) {
        //telephone number format: 1
        String regEx = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(phone);
        return matcher.find();
    }

    public static boolean validDob(String dob) {
        dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        try {
            if(!dob.equals("")){
            Date d = dateFormat.parse(dob);
            GregorianCalendar calendar = new GregorianCalendar();
            GregorianCalendar now = new GregorianCalendar();
            calendar.setTime(d);
            int age = 0;
            age = now.get(GregorianCalendar.YEAR) - calendar.get(GregorianCalendar.YEAR);
            if (age < 17) {
                return false;
            } else if (calendar.getTime().after(now.getTime()) || calendar.getTime() == now.getTime()) {
                return false;
            } else {
                return true;
            }
            }else{
                return false;
            }
        } catch (ParseException ex) {
            Logger.getLogger(Validate.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static boolean validTRN(String TRN) {
        String regEx = "[0-9]";
        pat = Pattern.compile(regEx);
        Matcher matcher = pat.matcher(TRN);
        if (TRN.length() == 9) {
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

}
