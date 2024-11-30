package si.luka2.prpo.sportapp.zrna;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

import com.kumuluz.ee.rest.beans.QueryParameters;
//import com.kumuluz.ee.rest.utils.JPAUtils;

import si.luka2.prpo.sportapp.entitete.Uporabnik;

@ApplicationScoped
public class UporabnikiZrno {

    private Logger log = Logger.getLogger(UporabnikiZrno.class.getName());

    @PostConstruct
    private void init() {
        log.info("Inicializacija zrna " + UporabnikiZrno.class.getSimpleName());

        // inicializacija virov
    }

    @PreDestroy
    private void destroy() {
        log.info("Deinicializacija zrna " + UporabnikiZrno.class.getSimpleName());

        // zapiranje virov
    }

    @PersistenceContext(unitName = "greo-spilat-jpa")
    private EntityManager em; //za interakcijo z databazo!!!

    //funkcionalnost za komunikacijo s databazo
    public List<Uporabnik> vrniUporabnike(){

        return em.createNamedQuery("Uporabnik.getAll", Uporabnik.class).getResultList();
    }


    public Long pridobiUporabnikeCount(QueryParameters query) {

        StringBuilder strng = new StringBuilder();
        strng.append("SELECT COUNT(u) FROM Uporabnik u WHERE 1=1");

        /*
            implementiraj se query parametre!
        */

        TypedQuery<Long> q = em.createQuery(strng.toString(), Long.class);

        return q.getSingleResult();

    }


    public Uporabnik pridobiUporabnika(int uporabnikId) {

        return em.createNamedQuery("Uporabnik.getById", Uporabnik.class)
                .setParameter("id", uporabnikId)
                .getSingleResult();
    }

    @Transactional
    public Uporabnik dodajUporabnika(Uporabnik uporabnik) {
        if (uporabnik == null) {
            throw new IllegalArgumentException("Uporabnik ne sme biti null");
        }

        log.info("Uporabnik uspesno kreiran");
        em.persist(uporabnik);
        return uporabnik;
    }

    @Transactional
    public Uporabnik posodobiUporabnika(int uporabnikId, Uporabnik uporabnik) {
        if (uporabnik == null) {
            throw new IllegalArgumentException("Uporabnik ne sme biti null");
        }

        Uporabnik obstojec = em.find(Uporabnik.class, uporabnikId);
        if (obstojec == null) {
            throw new IllegalArgumentException("Uporabnik ki ga zelis spremeniti ne obstaja");
        }

        obstojec.setIme(uporabnik.getIme());
        obstojec.setPriimek(uporabnik.getPriimek());
        obstojec.setUporabniskoIme(uporabnik.getUporabniskoIme());

        return em.merge(obstojec);
    }

    @Transactional
    public boolean odstraniUporanbika(int uporabnikId) {

        Uporabnik obstojec = em.find(Uporabnik.class, uporabnikId);
        if(obstojec != null){
            em.remove(obstojec);
            return true;
        }

        return false;

    }
}
