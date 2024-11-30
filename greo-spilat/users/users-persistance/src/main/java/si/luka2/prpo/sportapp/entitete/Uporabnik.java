package si.luka2.prpo.sportapp.entitete;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "uporabnik") //mapiranje tega classa na databazo
@NamedQueries(value =
        {
                @NamedQuery(name = "Uporabnik.getAll", query = "SELECT u FROM Uporabnik u"),
                @NamedQuery(name = "Uporabnik.getByUporabniskoIme",
                        query = "SELECT u FROM Uporabnik u WHERE u.uporabniskoIme = :uporabniskoIme"),
                @NamedQuery(name = "Uporabnik.getById",
                        query = "SELECT u FROM Uporabnik u WHERE u.id = :id")
        }) //reusable JPA queries
public class Uporabnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ime;

    private String priimek;

    @Column(name = "uporabnisko_ime")
    private String uporabniskoIme;

    public Integer getId(){return this.id;}
    public void setId(Integer id){this.id = id;}

    public String getIme() {return ime;}
    public void setIme(String ime){this.ime = ime;}

    public String getPriimek() {return priimek;}
    public void setPriimek(String priimek){this.priimek = priimek;}

    public String getUporabniskoIme() {return uporabniskoIme;}
    public void setUporabniskoIme(String uporabniskoIme) {this.uporabniskoIme = uporabniskoIme;}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Uporabnik [id=");
        builder.append(this.id);
        builder.append("]");
        builder.append("<br/>Uporabnisko ime = ");
        builder.append(this.uporabniskoIme);
        builder.append("<br/>Ime = ");
        builder.append(this.ime);
        builder.append("<br/>Priimek = ");
        builder.append(this.priimek);
        return builder.toString();
    }
}