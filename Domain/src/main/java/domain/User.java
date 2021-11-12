package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class User implements Entity<Integer> {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    @JsonIgnore
    private String passwordHash;
    private Set<Expense> expenses;
    private Set<MonthlyBudget> budgets;

    public User(String email, String firstName, String lastName, Date dateOfBirth, String passwordHash) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passwordHash = passwordHash;
    }

    public User(Integer id) {
        this.id = id;
    }

    public User() {
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

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Set<MonthlyBudget> getBudgets() {
        return budgets;
    }

    public void setBudgets(Set<MonthlyBudget> budgets) {
        this.budgets = budgets;
    }

    @Override
    public String toString() {
        return "User{" +
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
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName);
    }
}
