package si.luka2.prpo.sportapp.entities;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@NamedQueries({
        @NamedQuery(name = "Comment.getAll", query = "SELECT c FROM Comment c"),
        @NamedQuery(name = "Comment.getReplies", query = "SELECT c FROM Comment c WHERE c.parentComment.id = :commentId")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Integer id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer likes = 0;

    // Self-referential relationship: A comment can reply to another comment
    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonbTransient
    private Comment parentComment;

    // One-to-Many: A comment can have multiple replies

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonbTransient
    private List<Comment> replies = new ArrayList<>();

    public Comment() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", likes=" + likes +
                ", parentCommentId=" + (parentComment != null ? parentComment.getId() : "null") +
                ", replies=" + replies.size() +
                '}';
    }
}
