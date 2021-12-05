package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.print.attribute.standard.RequestingUserName;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class UserRequest implements Entity<Integer> {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    @JsonIgnore
    private String passwordHash;
    private String activationToken;

    public UserRequest(String email, String firstName, String lastName, Date dateOfBirth, String passwordHash, String activationToken) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passwordHash = passwordHash;
        this.activationToken = activationToken;
    }

    public UserRequest() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    @Override
    public String toString() {
        return "UserRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequest userRequest = (UserRequest) o;
        return id == userRequest.id && Objects.equals(email, userRequest.email) && Objects.equals(firstName, userRequest.firstName) && Objects.equals(lastName, userRequest.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }

}

