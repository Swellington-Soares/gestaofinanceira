package dev.suel.msuser.domain.valueobject;

import java.util.Objects;

public final class Password {
    private final String value;

    public static Password nullable() {
        return new Password("");
    }

    public String getValue() {
        return value;
    }

    public static Password of(String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("A senha não pode ser vazia.");

        if (value.length() < 6 ||  value.length() > 20)
            throw new IllegalArgumentException("A senha deve ter entre 6 e 20 caracteres.");

        if (value.trim().length() != value.length())
            throw new IllegalArgumentException("A senha não pode conter espaços no início ou no fim.");

        if (!value.matches("(?=.*[a-z]).*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra minúscula.");

        if (!value.matches("(?=.*[A-Z]).*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra maiúscula.");

        if (!value.matches("(?=.*\\d).*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos um número.");

        if (!value.matches("(?=.*[^A-Za-z0-9]).*"))
            throw new IllegalArgumentException("A senha deve conter pelo menos um símbolo.");

        return new Password(value);
    }

    public static Password ofForced(String value) {
        if (value == null || value.isEmpty())
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        return new Password(value);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    @Override
    public String toString() {
        return value;
    }

    private Password(String value) {
        this.value = value;
    }
}
