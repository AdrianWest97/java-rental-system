/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author wests
 */
public class JPAUtility {

    private static final EntityManagerFactory EMFACTORY;
   

    static {
        EMFACTORY = Persistence.createEntityManagerFactory("rentalSystemPU");
    }

    public static EntityManager getEntityManager() {
        return EMFACTORY.createEntityManager();
    }

    public static void close() {
        EMFACTORY.close();
    }


}
