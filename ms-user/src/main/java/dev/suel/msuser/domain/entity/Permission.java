package dev.suel.msuser.domain.entity;

import java.util.Locale;
import java.util.Objects;

public final class Permission {
    private String name;

    private Permission(String name) {
        this.name = name;
    }

    public static Permission of(String name) {
        if (name == null || name.isBlank() || name.toLowerCase(Locale.ROOT).startsWith("role"))
            throw new IllegalArgumentException("Permissão não pode iniciar com ROLE");
        return new Permission(name.toUpperCase(Locale.ROOT));
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Permission role = (Permission) o;
        return Objects.equals(name.toUpperCase(Locale.ROOT), role.name.toUpperCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toUpperCase(Locale.ROOT));
    }
}
