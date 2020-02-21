package operations;

import JPAControllers.LandlordJpaController;
import JPAControllers.TenantJpaController;
import entities.Tenant;
import java.util.ArrayList;
import interfaces.ProgramFiles;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author wests
 */
public class RegisterTenant {
    
    private TenantJpaController controller;

    public static boolean completeRegistration(Tenant tenant) {
        boolean flag = false;
      try{
        //insert new tenant
         
    }catch(Exception ex){
          System.out.println(ex);
    }
        return flag;

    }
}
