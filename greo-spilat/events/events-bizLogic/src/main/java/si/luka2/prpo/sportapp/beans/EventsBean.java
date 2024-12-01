package si.luka2.prpo.sportapp.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import si.luka2.prpo.sportapp.entities.Event;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EventsBean {


    @PersistenceContext(unitName = "events-jpa")
    private EntityManager em;

    private Logger log = Logger.getLogger(EventsBean.class.getName());

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + EventsBean.class.getSimpleName());
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinizializacija zrna " + EventsBean.class.getSimpleName());
    }

    public Long getEventsCount(QueryParameters query) {

        StringBuilder strng = new StringBuilder();
        strng.append("SELECT COUNT(e) FROM Event e WHERE 1=1");

        TypedQuery<Long> q = em.createQuery(strng.toString(), Long.class);
        return q.getSingleResult();
    }
    public List<Event> getEvents() {
        return em.createNamedQuery("Event.getAll", Event.class).getResultList();
    }

    public Event getEvent(int eventId) {
        return em.createNamedQuery("Event.getByEventId", Event.class )
                .setParameter("event_id", eventId)
                .getSingleResult();
    }

    @Transactional
    public Event addEvent(Event event){
        if(event == null){
            throw new IllegalArgumentException("Event is null");
        }
        log.info("Adding event: " + event.getTitle());
        em.persist(event);
        return event;
    }

    @Transactional
    public Event updateEvent(int eventID, Event event){
        if(event == null){
            throw new IllegalArgumentException("Event is null");
        }
        log.info("Updating event: " + event.getTitle());

        Event currentEvent = em.find(Event.class, eventID);

        if(currentEvent == null){
            throw new IllegalArgumentException("Event not found");
        }
        currentEvent.setTitle(event.getTitle());
        currentEvent.setDescription(event.getDescription());
        currentEvent.setDate(event.getDate());
        currentEvent.setLocation(event.getLocation());
        currentEvent.setSport(event.getSport());

        return em.merge(currentEvent);
    }
    @Transactional
    public boolean deleteEvent(int eventID){

        Event event = em.find(Event.class, eventID);
        if(event != null){
            log.info("Deleting event: " + event.getTitle());
            em.remove(event);
            return true;
        }
        log.info("Event not found");
        return false;
    }
}
