package si.luka2.prpo.sportapp.entitete;

import javax.persistence.*;

@Entity
@Table(name = "event")
@NamedQueries(value = {
        @NamedQuery(name = "Event.getAll", query = "SELECT e FROM Event e"),
        @NamedQuery(name = "Event.getByEventId", query = "SELECT e FROM Event e WHERE e.id = :id")
})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    private String description;

    private String location;

    private String sport;

    //tu implemntiraj date functionality
    private String date;

    public Integer getId(){return this.id;}
    public String getTitle(){return this.title;}
    public String getDescription(){return this.description;}
    public String getLocation(){return this.location;}
    public String getSport(){return this.sport;}
    public String getDate(){return this.date;}

    public void setId(Integer id){this.id = id;}
    public void setTitle(String title){this.title = title;}
    public void setDescription(String description){this.description = description;}
    public void setLocation(String location){this.location = location;}
    public void setSport(String sport){this.sport = sport;}
    public void setDate(String date){this.date = date;}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Events [id=");
        builder.append(this.id);
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
        builder.append(this.date);
        return builder.toString();
    }
}
