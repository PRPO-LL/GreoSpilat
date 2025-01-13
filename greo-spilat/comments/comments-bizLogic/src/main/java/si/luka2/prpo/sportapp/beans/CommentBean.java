package si.luka2.prpo.sportapp.beans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import si.luka2.prpo.sportapp.entities.Comment;
import si.luka2.prpo.sportapp.entities.Event;
import si.luka2.prpo.sportapp.entities.User;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class CommentBean {

    private static final Logger logger = Logger.getLogger(CommentBean.class.getName());

    @PersistenceContext(unitName = "comments-jpa")
    private EntityManager em;

    @PostConstruct
    private void init() {
        logger.info("Initializing bean: " + CommentBean.class.getSimpleName());
    }

    @PreDestroy
    private void destroy() {
        logger.info("Destroying bean: " + CommentBean.class.getSimpleName());
    }

    public List<Comment> getReplies(int commentId) {
        return em.createNamedQuery("Comment.getReplies", Comment.class)
                .setParameter("commentId", commentId)
                .getResultList();
    }

    public Comment getCommentById(int commentId) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment != null) {
            logger.info("Comment retrived: " + commentId);
            return comment;
        }
        return null;
    }

    @Transactional
    public Comment addComment(Comment comment, int userId, int eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        if (event == null) {
            throw new IllegalArgumentException("Event with ID " + eventId + " does not exist.");
        }

        comment.setUser(user);
        comment.setEvent(event);

        em.persist(comment);
        return comment;
    }

    @Transactional
    public Comment addReply(Comment reply, Comment parentComment, int userId, int eventId) {
        User user = findUserById(userId);
        Event event = findEventById(eventId);

        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        if (event == null) {
            throw new IllegalArgumentException("Event with ID " + eventId + " does not exist.");
        }

        reply.setParentComment(parentComment);
        reply.setUser(user);
        reply.setEvent(event);

        em.persist(reply);
        return reply;
    }


    @Transactional
    public boolean likeComment(int commentId) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment != null) {
            comment.setLikes(comment.getLikes() + 1);
            logger.info("Comment liked: " + comment.getId());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteComment(int commentId) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment != null) {
            em.remove(comment);
            logger.info("Comment deleted: " + commentId);
            return true;
        }
        return false;
    }

    public List<Comment> getCommentsByEvent(int eventId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.event.event_id = :eventId", Comment.class)
                .setParameter("eventId", eventId)
                .getResultList();
    }

    public List<Comment> getCommentsByUser(int userId) {
        return em.createQuery("SELECT c FROM Comment c WHERE c.user.user_id = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public User findUserById(int userId) {
        return em.find(User.class, userId);
    }

    public Event findEventById(int eventId) {
        return em.find(Event.class, eventId);
    }

}
