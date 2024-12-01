package si.luka2.prpo.sportapp.beans;

import si.luka2.prpo.sportapp.entities.UserEvents;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UserEventsBean {
    private Logger log = Logger.getLogger(UserEventsBean.class.getName());

    @PostConstruct
    public void init() {
        log.info("UserEventsBean init");
    }

    @PreDestroy
    public void destroy() {
        log.info("UserEventsBean destroy");
    }

    @PersistenceContext(unitName = "greo-spilat-jpa")
    private EntityManager em;

    @Transactional
    public UserEvents addUserEvent(UserEvents userEvents) {

        if(userEvents == null) {
            throw new IllegalArgumentException("userEvents is null");
        }

        em.persist(userEvents);
        return userEvents;
    }

    public List<UserEvents> getUserEvents(int userId) {
        return em.createQuery("SELECT ue FROM UserEvents ue WHERE ue.user.user_id = :user_id", UserEvents.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    public List<UserEvents> getAllUserEvents() {
        return em.createQuery("SELECT ue FROM UserEvents ue", UserEvents.class)
                .getResultList();
    }

    //for counting how many events does each user have
    public Long getUserEventsCount(int userId) {
        return em.createQuery("SELECT COUNT(ue) FROM UserEvents ue WHERE ue.user.user_id = :user_id", Long.class)
                .setParameter("user_id", userId)
                .getSingleResult();
    }

//    //for counting how many events are out there
//    public Long getAllUserEventsCount() {
//        return em.createQuery("SELECT COUNT(ue) FROM UserEvents ue", Long.class)
//                .getSingleResult();
//    }


    @Transactional
    public boolean deleteUserEvent(int userEventId) {

        UserEvents userEvents = em.find(UserEvents.class, userEventId);
        if(userEvents == null) {
            log.info("UserEvents with id " + userEventId + " not found");
            return false;
        }
        log.info("UserEvents with id " + userEventId + " deleted");
        em.remove(userEvents);
        return true;
    }
}
