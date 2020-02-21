package operations;

import SendMail.SendEmail;
import entities.Tenant;
import interfaces.CryptoKeys;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import interfaces.ProgramFiles;
import java.util.Arrays;

/**
 *
 * @author wests
 * @param <T>
 */
public final class CrudOperations<T> {

    /**
     *
     * @param obj
     * @param filename
     * @return
     */
    SendEmail report = new SendEmail();

    public boolean insertObj(T obj, String filename) {
        ObjectOutputStream out = null;
        File toWrite = new File(filename);
       
        //check if the file exits
        if (!toWrite.exists()) {
            try {
                toWrite.getAbsoluteFile().getParentFile().mkdirs();
                toWrite.createNewFile();
            } catch (IOException ex) {

                String error = CrudOperations.class.getName() + " \n" + Level.SEVERE + " \n" + Arrays.toString(ex.getStackTrace());
                //make report
                report.logError(CryptoKeys.SENDER_MAIL, "Error Occured", error);
            }
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream(toWrite));
            out.writeObject(obj);
            out.flush();
            return true;
        } catch (IOException ex) {
            String error = CrudOperations.class.getName() + " \n" + Level.SEVERE + " \n" + Arrays.toString(ex.getStackTrace());
            //make report
            report.logError(CryptoKeys.SENDER_MAIL, "Error Occured", error);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    String error = CrudOperations.class.getName() + " \n" + Level.SEVERE + " \n" + Arrays.toString(ex.getStackTrace());
                    //make report
                    report.logError(CryptoKeys.SENDER_MAIL, "Error Occured", error);
                }
            }
        }
        return false;
    }

    //read data from a file
    public T readObj(String filename) {
        ObjectInputStream ois = null;
        File toRead = new File(filename);
        if (toRead.exists() && toRead.length() > 0) {
            try {
                ois = new ObjectInputStream(new FileInputStream(filename));
            } catch (IOException ex) {
                Logger.getLogger(CrudOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            T obj;
            try {
                obj = (T) ois.readObject();
                ois.close();
                return obj;
            } catch (IOException | ClassNotFoundException ex) {

                String error = CrudOperations.class.getName() + " \n" + Level.SEVERE + " \n" + Arrays.toString(ex.getStackTrace());
                //make report
                report.logError(CryptoKeys.SENDER_MAIL, "Error Occured", error);

            }
        }
        return null;
    }

    //check of a tenant exits by a specified email
    public boolean checkIFExists(String key, String filename) {
        Object obj = null;
        obj = new CrudOperations<>().readObj(filename);
        boolean found = false;
        if (obj != null) {
            ArrayList<Tenant> Tlist = (ArrayList<Tenant>) obj;
            ListIterator iterate = Tlist.listIterator();
            while (iterate.hasNext()) {
                Tenant temp = (Tenant) iterate.next();
                if (temp.getContact().getEmail().equals(key)) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    //update tenant file specifie by email
    public boolean updateFile(Tenant t, String email) {
        ArrayList<Tenant> arrayTemp = (ArrayList<Tenant>) readObj(ProgramFiles.TENANT_FILE);
        if (arrayTemp != null) {
            ListIterator iterator = arrayTemp.listIterator();
            while (iterator.hasNext()) {
                Tenant temp = (Tenant) iterator.next();
                if (temp.getContact().getEmail().equals(email)) {
                    //remove tenant and x position 
                    iterator.remove();
                    //add updated object
                    arrayTemp.add(t);
                    //re-insert into database
                    if (insertObj((T) arrayTemp, ProgramFiles.TENANT_FILE)) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    //retrive a tenant specified by email
    public Tenant retriveByEmail(String email) {
        ArrayList<Tenant> arrayTentant = null;
        Tenant temp = null;
        try {
            arrayTentant = (ArrayList<Tenant>) readObj(ProgramFiles.TENANT_FILE);
            //iterate 

            ListIterator iterate = arrayTentant.listIterator();
            while (iterate.hasNext()) {
                Tenant t = (Tenant) iterate.next();
                if (t.getContact().getEmail().toLowerCase().equals(email.toLowerCase())) {
                    temp = t;
                }
            }
        } catch (NullPointerException ex) {

            String error = CrudOperations.class.getName() + " \n" + Level.SEVERE + " \n" + Arrays.toString(ex.getStackTrace());
            //make report
            report.logError(CryptoKeys.SENDER_MAIL, "Error Occured", error);
        }
        return temp;

    }
}
