package dev.suel.msuser.domain.entity;

import dev.suel.msuser.domain.valueobject.Email;
import dev.suel.msuser.domain.valueobject.Password;
import dev.suel.msuser.domain.valueobject.PersonName;

import java.time.Instant;
import java.util.Objects;

public class Customer {
    private Long id;
    private PersonName name ;
    private Email email ;
    private Password password ;
    private Instant createdAt ;

    public Customer() {}

    public Customer(Long id, PersonName name, Email email, Password password, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public Customer setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
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

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public static class CustomerBuilder {
        private Long id;
        private PersonName name = PersonName.nullable();
        private Email email =  Email.nullable();
        private Password password =  Password.nullable();
        private Instant createdAt;

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
            return new Customer(id, name, email, password, createdAt);
        }
    }


}
