package si.luka2.prpo.sportapp.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_events")
public class UserEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "event_id")
    private Integer event_id;

    @Column(name = "joined_at")
    private LocalDateTime joined_at;

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public Integer getEvent_id() {return event_id;}
    public void setEvent_id(Integer event_id) {this.event_id = event_id;}
    public LocalDateTime getJoind_at() {return joined_at;}
    public void setJoind_at(LocalDateTime joind_at) {this.joined_at = joind_at;}


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserEvents [id=");
        builder.append(id);
        builder.append(" ]");
        builder.append("<br/>event_id = ");
        builder.append(this.event_id);
        return builder.toString();
    }
}
