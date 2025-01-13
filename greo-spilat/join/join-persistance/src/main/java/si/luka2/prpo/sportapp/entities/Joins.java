package si.luka2.prpo.sportapp.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "joins")
@NamedQueries({
        @NamedQuery(name = "Joins.getAll", query = "SELECT j FROM Joins j"),
        @NamedQuery(name = "Joins.getByEvent", query = "SELECT j FROM Joins j WHERE j.event.event_id = :eventId"),
        @NamedQuery(name = "Joins.getByUser", query = "SELECT j FROM Joins j WHERE j.user.user_id = :userId")
})
public class Joins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "join_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    public Joins() {
        this.joinedAt = LocalDateTime.now();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public String toString() {
        return "Joins{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", event=" + (event != null ? event.getTitle() : "null") +
                ", joinedAt=" + joinedAt +
                '}';
    }
}
