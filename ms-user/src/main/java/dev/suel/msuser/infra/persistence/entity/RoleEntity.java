package dev.suel.msuser.infra.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity(name = "Role")
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<PermissionEntity> permissions = new HashSet<>();

    public static RoleEntity of(String name) {
        RoleEntity  role = new RoleEntity();
        role.setName(name);
        return role;
    }

    @PrePersist
    private void prePersist() {
        name = name.toUpperCase(Locale.ROOT);
    }
}
