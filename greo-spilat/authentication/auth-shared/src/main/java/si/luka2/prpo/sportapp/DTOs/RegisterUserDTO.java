package si.luka2.prpo.sportapp.DTOs;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegisterUserDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

    private String name;
    private String surname;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSurname() {

        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }
}
