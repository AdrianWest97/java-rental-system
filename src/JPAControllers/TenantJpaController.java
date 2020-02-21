/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPAControllers;

import JPAControllers.exceptions.NonexistentEntityException;
import JPAControllers.exceptions.PreexistingEntityException;
import entities.Tenant;
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
public class TenantJpaController implements Serializable {

    public TenantJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public boolean create(Tenant tenant) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tenant);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (findTenant(tenant.getId()) != null || findTenant(tenant.getEmail()) != null || findTenantTrn(tenant.getTrn()) != null) {
                throw new PreexistingEntityException("Tenant " + tenant + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tenant tenant) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tenant = em.merge(tenant);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = tenant.getId();
                if (findTenant(id) == null) {
                    throw new NonexistentEntityException("The tenant with id " + id + " no longer exists.");
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
            Tenant tenant;
            try {
                tenant = em.getReference(Tenant.class, id);
                tenant.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tenant with id " + id + " no longer exists.", enfe);
            }
            em.remove(tenant);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tenant> findTenantEntities() {
        return findTenantEntities(true, -1, -1);
    }

    public List<Tenant> findTenantEntities(int maxResults, int firstResult) {
        return findTenantEntities(false, maxResults, firstResult);
    }

    private List<Tenant> findTenantEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tenant.class));
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

    public Tenant findTenant(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tenant.class, id);
        } finally {
            em.close();
        }
    }
    
    public Tenant findTenant(String email) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tenant.class, email);
        } finally {
            em.close();
        }
    }
    
     public Tenant findTenantTrn(String trn) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tenant.class, trn);
        } finally {
            em.close();
        }
    }
    
    
    public int getTenantCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tenant> rt = cq.from(Tenant.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
