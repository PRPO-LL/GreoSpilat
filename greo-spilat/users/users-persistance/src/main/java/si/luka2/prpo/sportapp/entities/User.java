package si.luka2.prpo.sportapp.entities;


import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

@Entity
@Table(name = "users") //mapiranje tega classa na databazo
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM User u"),
                @NamedQuery(name = "User.getByUsername",
                        query = "SELECT u FROM User u WHERE u.username = :username"),
                @NamedQuery(name = "User.getById",
                        query = "SELECT u FROM User u WHERE u.user_id = :user_id")
        }) //reusable JPA queries
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private Integer user_id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
//     @JsonbTransient
//    @Column(name = "password_hash")


//     Json to String converter for preferences
//    @Column(name = "preference", columnDefinition = "jsonb")
//    @Convert(converter = JsonObjectConverter.class)
//    private JsonObject preferences;

//    public Integer getId(){return this.user_id;}
//    public void setId(Integer user_id){this.user_id = user_id;}

    public String getName() {return name;}
    public void setName(String name){this.name = name;}


    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}


    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}


    public Integer getUser_id() {return user_id;}
    public void setUser_id(Integer user_id) {this.user_id = user_id;}

//    public User(){}
//    public JsonbProperty getPreferences() {return this.preferences;}
//    public void setPreferences(JsonbProperty preferences) {this.preferences = preferences;}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [ id=");
        builder.append(this.user_id);
        builder.append(" ]");
        builder.append("<br/>Username = ");
        builder.append(this.username);
        builder.append("<br/>Name = ");
        builder.append(this.name);
        builder.append("<br/>Email = ");
        builder.append(this.email);
        return builder.toString();
    }
}