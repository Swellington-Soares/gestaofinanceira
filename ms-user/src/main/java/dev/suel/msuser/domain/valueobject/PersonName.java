package dev.suel.msuser.domain.valueobject;


import java.util.Objects;
import java.util.regex.Pattern;

public final class PersonName {
    private static final String NAME_REGEX = "^\\p{L}+(?: \\p{L}+)*$";

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private final String value;

    private PersonName(String value) {
        this.value = value;
    }

    public static PersonName of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }

        String normalized = value.trim().toUpperCase();

        if (!NAME_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        return new PersonName(normalized);
    }

    public static PersonName nullable() {
        return new PersonName("");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonName that = (PersonName) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    @Override
    public String toString() {
        return value;
    }
}
