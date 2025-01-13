package si.luka2.prpo.sportapp.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import si.luka2.prpo.sportapp.entities.Joins;
import si.luka2.prpo.sportapp.entities.Event;
import si.luka2.prpo.sportapp.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class JoinBean {
    private static final Logger logger = Logger.getLogger(JoinBean.class.getName());

    @PersistenceContext(unitName = "joins-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        logger.info("Initializing bean: " + JoinBean.class.getSimpleName());
    }

    @PreDestroy
    private void destroy() {
        logger.info("Destroying bean: " + JoinBean.class.getSimpleName());
    }

    @Transactional
    public Joins addJoin(int userId, int eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (user == null || event == null) {
            throw new IllegalArgumentException("User or Event not found.");
        }

        Joins join = new Joins();
        join.setUser(user);
        join.setEvent(event);

        em.persist(join);
        return join;
    }

    public List<Joins> getJoinsByEvent(int eventId) {
        return em.createNamedQuery("Joins.getByEvent", Joins.class)
                .setParameter("eventId", eventId)
                .getResultList();
    }

    public List<Joins> getJoinsByUser(int userId) {
        return em.createNamedQuery("Joins.getByUser", Joins.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Transactional
    public boolean removeJoin(int userId, int eventId) {
        Joins join = em.createQuery("SELECT j FROM Joins j WHERE j.user.user_id = :userId AND j.event.event_id = :eventId", Joins.class)
                .setParameter("userId", userId)
                .setParameter("eventId", eventId)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (join == null) {
            return false; // No join relationship found
        }

        em.remove(join); // Remove the relationship
        return true;
    }

    public User findUserById(int userId) {
        return em.find(User.class, userId);
    }

    public Event findEventById(int eventId) {
        return em.find(Event.class, eventId);
    }

}
