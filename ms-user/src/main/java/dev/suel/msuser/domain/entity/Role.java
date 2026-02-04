package dev.suel.msuser.domain.entity;

import java.util.*;

public class Role {
    private final Set<Permission> permissions = new HashSet<>();
    private String name;

    public String getName() {
        return name;
    }

    private Role(String name) {
        this.name = name.toUpperCase(Locale.ROOT);
    }

    public static Role of(String name) {
        return new Role(name.toUpperCase(Locale.ROOT));
    }


    public void addPermission(Permission permission) {
        permissions.add(permission);
    }
    public void removePermission(Permission permission) {
        permissions.remove(permission);
    }
    public void addPermissions(Collection<Permission> permissions) {
        this.permissions.addAll(permissions);
    }
    public void removeAllPermissions() {
        permissions.clear();
    }
    public boolean hasPermission(Permission permission) {
        return this.permissions.contains(permission);
    }

    public boolean hasPermission(String permissionName) {
        return this.permissions.stream().anyMatch(p -> p.getName().equals(permissionName));
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name.toUpperCase(Locale.ROOT), role.name.toUpperCase(Locale.ROOT));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name.toUpperCase(Locale.ROOT));
    }
}
