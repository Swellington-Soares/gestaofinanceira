package dev.suel.msuser.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPersonName {

    @Nested
    @DisplayName("Criação válida de nome")
    class ValidPersonNameCreation {

        @Test
        @DisplayName("Deve criar nome válido com uma palavra")
        void shouldCreateValidSingleName() {
            PersonName name = PersonName.of("João");

            assertNotNull(name);
            assertEquals("JOÃO", name.getValue());
        }

        @Test
        @DisplayName("Deve criar nome válido com múltiplas palavras")
        void shouldCreateValidFullName() {
            PersonName name = PersonName.of("Maria Silva");

            assertEquals("MARIA SILVA", name.getValue());
        }

        @Test
        @DisplayName("Deve normalizar nome (trim e uppercase)")
        void shouldNormalizeName() {
            PersonName name = PersonName.of("  ana paula  ");

            assertEquals("ANA PAULA", name.getValue());
        }

        @Test
        @DisplayName("Deve aceitar nomes com caracteres acentuados")
        void shouldAcceptAccentedCharacters() {
            PersonName name = PersonName.of("José Álvares");

            assertEquals("JOSÉ ÁLVARES", name.getValue());
        }
    }

    @Nested
    @DisplayName("Criação inválida de nome")
    class InvalidPersonNameCreation {

        @Test
        @DisplayName("Deve lançar exceção quando nome for null")
        void shouldThrowExceptionWhenNameIsNull() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> PersonName.of(null));

            assertEquals("Nome não pode ser vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome for vazio")
        void shouldThrowExceptionWhenNameIsEmpty() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> PersonName.of(""));

            assertEquals("Nome não pode ser vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome for blank")
        void shouldThrowExceptionWhenNameIsBlank() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> PersonName.of("   "));

            assertEquals("Nome não pode ser vazio.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome contiver números")
        void shouldThrowExceptionWhenNameContainsNumbers() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> PersonName.of("João 123"));

            assertEquals("Nome inválido.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome contiver caracteres especiais")
        void shouldThrowExceptionWhenNameContainsSpecialCharacters() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> PersonName.of("Maria@Silva"));

            assertEquals("Nome inválido.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome tiver múltiplos espaços consecutivos")
        void shouldThrowExceptionWhenNameHasMultipleSpaces() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> PersonName.of("Maria  Silva"));

            assertEquals("Nome inválido.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Nullable name")
    class NullablePersonName {

        @Test
        @DisplayName("Deve criar nome nullable com valor vazio")
        void shouldCreateNullableName() {
            PersonName name = PersonName.nullable();

            assertNotNull(name);
            assertEquals("", name.getValue());
        }
    }

    @Nested
    @DisplayName("Equals, hashCode e toString")
    class EqualsHashCodeAndToString {

        @Test
        @DisplayName("Nomes com mesmo valor devem ser iguais")
        void shouldBeEqualWhenValuesAreSame() {
            PersonName n1 = PersonName.of("João Silva");
            PersonName n2 = PersonName.of("joão silva");

            assertEquals(n1, n2);
            assertEquals(n1.hashCode(), n2.hashCode());
        }

        @Test
        @DisplayName("Nomes com valores diferentes não devem ser iguais")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            PersonName n1 = PersonName.of("João Silva");
            PersonName n2 = PersonName.of("Maria Silva");

            assertNotEquals(n1, n2);
        }

        @Test
        @DisplayName("Nome não deve ser igual a null ou outro tipo")
        void shouldNotBeEqualToNullOrDifferentType() {
            PersonName name = PersonName.of("João");

            assertNotEquals(null, name);
        }

        @Test
        @DisplayName("toString deve retornar o valor do nome")
        void shouldReturnValueOnToString() {
            PersonName name = PersonName.of("Ana Paula");

            assertEquals("ANA PAULA", name.toString());
        }
    }
}
