/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAControllers;

import JPAControllers.exceptions.NonexistentEntityException;
import JPAControllers.exceptions.PreexistingEntityException;
import entities.Landlord;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author wests
 */
public class LandlordJpaController implements Serializable {

    public LandlordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean create(Landlord landlord) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(landlord);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (findLandlord(landlord.getId()) != null) {
                throw new PreexistingEntityException("Landlord " + landlord + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Landlord landlord) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            landlord = em.merge(landlord);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = landlord.getId();
                if (findLandlord(id) == null) {
                    throw new NonexistentEntityException("The landlord with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Landlord landlord;
            try {
                landlord = em.getReference(Landlord.class, id);
                landlord.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The landlord with id " + id + " no longer exists.", enfe);
            }
            em.remove(landlord);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Landlord> findLandlordEntities() {
        return findLandlordEntities(true, -1, -1);
    }

    public List<Landlord> findLandlordEntities(int maxResults, int firstResult) {
        return findLandlordEntities(false, maxResults, firstResult);
    }

    private List<Landlord> findLandlordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Landlord.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Landlord findLandlord(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Landlord.class, id);
        } finally {
            em.close();
        }
    }
    


    public int getLandlordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Landlord> rt = cq.from(Landlord.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
