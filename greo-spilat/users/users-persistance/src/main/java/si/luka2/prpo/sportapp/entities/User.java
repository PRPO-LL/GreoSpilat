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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
//     @JsonbTransient
//    @Column(name = "password_hash")
    @Transient
    private String passwordHash;


    @Column(name = "password_hash")
    private String password;


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

    public String getPasswordHash() {return passwordHash;}
    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

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