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
    public Comment addComment(Comment comment) {
        comment.setCreatedAt(java.time.LocalDateTime.now());
        em.persist(comment);
        logger.info("Comment added: " + comment.getContent());
        return comment;
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

}
