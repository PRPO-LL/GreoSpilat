package si.luka2.prpo.sportapp.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@NamedQueries(value = {
        @NamedQuery(name = "Event.getAll", query = "SELECT e FROM Event e"),
        @NamedQuery(name = "Event.getByEventId", query = "SELECT e FROM Event e WHERE e.event_id = :event_id")
})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false, updatable = false)
    private Integer event_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "creator_id", nullable = false)
    private Integer creator_id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "sport")
    private String sport;

    @Column(name = "max_participants")
    private Integer max_participants;

    @Column(name = "participants", nullable = false)
    private int participants = 1;

    public Integer getIid(){return this.event_id;}
    public String getTitle(){return this.title;}
    public String getDescription(){return this.description;}
    public String getLocation(){return this.location;}
    public String getSport(){return this.sport;}
    public LocalDateTime getDate(){return this.startTime;}
    public Integer getCreatorId(){return this.creator_id;}
    public Integer getMaxParticipants(){return this.max_participants;}
    public int getParticipants() {return participants;}

    public void setId(Integer event_id){this.event_id = event_id;}
    public void setTitle(String title){this.title = title;}
    public void setDescription(String description){this.description = description;}
    public void setLocation(String location){this.location = location;}
    public void setSport(String sport){this.sport = sport;}
    public void setDate(LocalDateTime startTime){this.startTime = startTime;}
    public void setCreatorId(Integer creator_id){this.creator_id = creator_id;}
    public void setMaxParticipants(Integer max_participants){this.max_participants = max_participants;}
    public void setParticipants(int participants) {this.participants = participants;}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Events [id=");
        builder.append(this.event_id);
        builder.append("]<br/>");
        builder.append("<br/>Title = ");
        builder.append(this.title);
        builder.append("<br/>Description = ");
        builder.append(this.description);
        builder.append("<br/>Location = ");
        builder.append(this.location);
        builder.append("<br/>Sport = ");
        builder.append(this.sport);
        builder.append("<br/>Date = ");
        builder.append(this.startTime);
        return builder.toString();
    }
}
