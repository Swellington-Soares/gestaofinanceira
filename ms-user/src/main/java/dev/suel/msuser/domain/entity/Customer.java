package dev.suel.msuser.domain.entity;

import dev.suel.msuser.domain.valueobject.Email;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Customer {
    private Long id;
    private PersonName name;
    private Email email;
    private Password password;
    private Instant createdAt;


    private final Set<Role> roles = new HashSet<>();

    public void setRoles(Set<Role> roles) {
        this.roles.addAll(roles);
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void clearRoles() {
        this.roles.clear();
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public boolean hasRole(String roleName) {
        return this.roles.stream().anyMatch(r -> r.getName().equals(roleName));
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public Customer() {
    }

    public Customer(
            Long id,
            PersonName name,
            Email email,
            Password password,
            Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public Long getId() {
        return id;
    }

    public Customer setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name != null ? name.getValue() : "";
    }

    public Customer setName(PersonName name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email != null ? email.getValue() : "";
    }

    public Customer setEmail(Email email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password != null ? password.getValue() : "";
    }

    public Customer setPassword(Password password) {
        this.password = password;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getEmail(), customer.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEmail());
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Customer setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    private boolean isAdmin() {
        return roles.stream().anyMatch(r -> r.getName().equals("ADMIN"));
    }

    public static class CustomerBuilder {
        private Long id;
        private PersonName name = PersonName.nullable();
        private Email email = Email.nullable();
        private Password password = Password.nullable();
        private Instant createdAt;
        private Set<Role> roles = new HashSet<>();

        public CustomerBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerBuilder name(PersonName name) {
            this.name = name;
            return this;
        }

        public CustomerBuilder email(Email email) {
            this.email = email;
            return this;
        }

        public CustomerBuilder password(Password password) {
            this.password = password;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer(id, name, email, password, createdAt);
            customer.setRoles(roles);
            return customer;
        }

        public CustomerBuilder addRole(Role role) {
           roles.add(role);
           return this;
        }

        public CustomerBuilder roles(Set<Role> roles) {
            this.roles = roles;
            return this;
        }
    }


}
