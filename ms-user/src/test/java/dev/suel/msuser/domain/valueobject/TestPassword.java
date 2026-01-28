package dev.suel.msuser.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPassword {

    @Nested
    @DisplayName("Criação válida de senha")
    class ValidPasswordCreation {

        @Test
        @DisplayName("Deve criar senha válida")
        void shouldCreateValidPassword() {
            Password password = Password.of("Abc@123");

            assertNotNull(password);
            assertEquals("Abc@123", password.getValue());
        }

        @Test
        @DisplayName("Deve aceitar senha com tamanho mínimo (6)")
        void shouldAcceptPasswordWithMinimumLength() {
            Password password = Password.of("A1b@cd");

            assertEquals("A1b@cd", password.getValue());
        }

        @Test
        @DisplayName("Deve aceitar senha com tamanho máximo (20)")
        void shouldAcceptPasswordWithMaximumLength() {
            Password password = Password.of("Abcdef1@Abcdef1@Ab");

            assertEquals("Abcdef1@Abcdef1@Ab", password.getValue());
        }
    }

    @Nested
    @DisplayName("Criação inválida de senha")
    class InvalidPasswordCreation {

        @Test
        @DisplayName("Deve lançar exceção quando senha for null")
        void shouldThrowExceptionWhenPasswordIsNull() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of(null));

            assertEquals("A senha não pode ser vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha for vazia")
        void shouldThrowExceptionWhenPasswordIsEmpty() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of(""));

            assertEquals("A senha não pode ser vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha for menor que 6 caracteres")
        void shouldThrowExceptionWhenPasswordTooShort() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of("Ab1@"));

            assertEquals("A senha deve ter entre 6 e 20 caracteres.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha for maior que 20 caracteres")
        void shouldThrowExceptionWhenPasswordTooLong() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class,
                            () -> Password.of("Abcdef1@Abcdef1@Abc9080"));

            assertEquals("A senha deve ter entre 6 e 20 caracteres.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha tiver espaço no início")
        void shouldThrowExceptionWhenPasswordHasLeadingSpace() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of(" Abc@123"));

            assertEquals("A senha não pode conter espaços no início ou no fim.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha tiver espaço no fim")
        void shouldThrowExceptionWhenPasswordHasTrailingSpace() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of("Abc@123 "));

            assertEquals("A senha não pode conter espaços no início ou no fim.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não tiver letra minúscula")
        void shouldThrowExceptionWhenMissingLowercaseLetter() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of("ABC@123"));

            assertEquals("A senha deve conter pelo menos uma letra minúscula.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não tiver letra maiúscula")
        void shouldThrowExceptionWhenMissingUppercaseLetter() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of("abc@123"));

            assertEquals("A senha deve conter pelo menos uma letra maiúscula.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não tiver número")
        void shouldThrowExceptionWhenMissingNumber() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of("Abc@def"));

            assertEquals("A senha deve conter pelo menos um número.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não tiver símbolo")
        void shouldThrowExceptionWhenMissingSymbol() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.of("Abc1234"));

            assertEquals("A senha deve conter pelo menos um símbolo.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Criação forçada de senha (ofForced)")
    class ForcedPasswordCreation {

        @Test
        @DisplayName("Deve criar senha sem validar regras")
        void shouldCreatePasswordWithoutValidation() {
            Password password = Password.ofForced("123");

            assertEquals("123", password.getValue());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha forçada for null")
        void shouldThrowExceptionWhenForcedPasswordIsNull() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.ofForced(null));

            assertEquals("A senha não pode ser vazia.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando senha forçada for vazia")
        void shouldThrowExceptionWhenForcedPasswordIsEmpty() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Password.ofForced(""));

            assertEquals("A senha não pode ser vazia.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Nullable password")
    class NullablePassword {

        @Test
        @DisplayName("Deve criar senha nullable com valor vazio")
        void shouldCreateNullablePassword() {
            Password password = Password.nullable();

            assertNotNull(password);
            assertEquals("", password.getValue());
        }
    }

    @Nested
    @DisplayName("Equals, hashCode e toString")
    class EqualsHashCodeAndToString {

        @Test
        @DisplayName("Senhas com mesmo valor devem ser iguais")
        void shouldBeEqualWhenValuesAreSame() {
            Password p1 = Password.of("Abc@123");
            Password p2 = Password.of("Abc@123");

            assertEquals(p1, p2);
            assertEquals(p1.hashCode(), p2.hashCode());
        }

        @Test
        @DisplayName("Senhas com valores diferentes não devem ser iguais")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            Password p1 = Password.of("Abc@123");
            Password p2 = Password.of("Abc@124");

            assertNotEquals(p1, p2);
        }

        @Test
        @DisplayName("Senha não deve ser igual a null ou outro tipo")
        void shouldNotBeEqualToNullOrDifferentType() {
            Password password = Password.of("Abc@123");
            assertNotEquals(null, password);
        }

        @Test
        @DisplayName("toString deve retornar o valor da senha")
        void shouldReturnValueOnToString() {
            Password password = Password.of("Abc@123");

            assertEquals("Abc@123", password.toString());
        }
    }
}
