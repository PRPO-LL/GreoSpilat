package si.luka2.prpo.sportapp.beans;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

import com.kumuluz.ee.rest.beans.QueryParameters;
//import com.kumuluz.ee.rest.utils.JPAUtils;

import com.kumuluz.ee.rest.utils.JPAUtils;
import si.luka2.prpo.sportapp.entities.User;

@ApplicationScoped
public class UserBean {

    private Logger log = Logger.getLogger(UserBean.class.getName());

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UserBean.class.getSimpleName());

        // inicializacija virov
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UserBean.class.getSimpleName());

        // zapiranje virov
    }

    @PersistenceContext(unitName = "greo-spilat-jpa")
    private EntityManager em; //za interakcijo z databazo!!!

    //funkcionalnost za komunikacijo s databazo
    public List<User> getUsers(QueryParameters query) {

//        return em.createNamedQuery("User.getAll", User.class).getResultList();
        return JPAUtils.queryEntities(em, User.class, query);
    }


    public Long getUserCount(QueryParameters query) {

        StringBuilder strng = new StringBuilder();
        strng.append("SELECT COUNT(u) FROM User u WHERE 1=1");

        /*
            implementiraj se query parametre!
        */

        TypedQuery<Long> q = em.createQuery(strng.toString(), Long.class);

        return q.getSingleResult();

    }


    public User getUser(int uporabnikId) {

        return em.createNamedQuery("User.getById", User.class)
                .setParameter("user_id", uporabnikId)
                .getSingleResult();
    }

    @Transactional
    public User addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Uporabnik ne sme biti null");
        }
//        if(user.getPassword() == null || user.getPassword().isEmpty()) {
//            throw new IllegalArgumentException("Geslo ne sme biti null");
//        }

        log.info("Uporabnik uspesno kreiran");
//        String hashed = hashPassword()
        em.persist(user);
        return user;
    }

    @Transactional
    public User updateUser(int uporabnikId, User user) {
        if (user == null) {
            throw new IllegalArgumentException("Uporabnik ne sme biti null");
        }

        User obstojec = em.find(User.class, uporabnikId);
        if (obstojec == null) {
            throw new IllegalArgumentException("Uporabnik ki ga zelis spremeniti ne obstaja");
        }

        obstojec.setName(user.getName());
        obstojec.setEmail(user.getEmail());
        obstojec.setUsername(user.getUsername());
//        obstojec.setPasswordHash(user.getPasswordHash());

        return em.merge(obstojec);
    }

    @Transactional
    public boolean deleteUser(int uporabnikId) {

        User obstojec = em.find(User.class, uporabnikId);
        if(obstojec != null){
            em.remove(obstojec);
            return true;
        }

        return false;

    }
}
