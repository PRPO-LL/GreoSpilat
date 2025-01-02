package si.luka2.prpo.sportapp.entities;

import javax.persistence.*;

@Entity
@Table(name = "userAuth")
@NamedQueries(value =
        {
                @NamedQuery(name = "User.getAll", query = "SELECT u FROM UserAuth u"),
                @NamedQuery(name = "User.getByUsername",
                        query = "SELECT u FROM UserAuth u WHERE u.username = :username"),
                @NamedQuery(name = "User.getById",
                        query = "SELECT u FROM UserAuth u WHERE u.id = :id")
        }) //reusable JPA queries
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    @Column(name = "password_hash", nullable = false)
    private String hashPassword;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getHashPassword() {
        return hashPassword;
    }
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User [ id=");
        builder.append(this.id);
        builder.append(" ]");
        builder.append("<br/>Username = ");
        builder.append(this.username);
        builder.append("<br/>Password Hash = ");
        builder.append(this.hashPassword);
        return builder.toString();
    }

}
