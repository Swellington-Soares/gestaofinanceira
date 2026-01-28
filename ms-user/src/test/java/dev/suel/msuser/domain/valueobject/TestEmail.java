package dev.suel.msuser.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestEmail {

    @Nested
    @DisplayName("Criação de Email válida")
    class ValidEmailCreation {

        @Test
        @DisplayName("Deve criar Email válido")
        void shouldCreateValidEmail() {
            Email email = Email.of("user@test.com");

            assertNotNull(email);
            assertEquals("user@test.com", email.getValue());
        }

        @Test
        @DisplayName("Deve normalizar email (trim e lowercase)")
        void shouldNormalizeEmail() {
            Email email = Email.of("  USER@TEST.COM  ");

            assertEquals("user@test.com", email.getValue());
        }

        @Test
        @DisplayName("Deve aceitar emails com pontos, hífen e underscore")
        void shouldAcceptComplexValidEmails() {
            Email email = Email.of("user.name_test-123@test-domain.com");

            assertEquals("user.name_test-123@test-domain.com", email.getValue());
        }
    }

    @Nested
    @DisplayName("Criação de Email inválida")
    class InvalidEmailCreation {

        @Test
        @DisplayName("Deve lançar exceção quando email for null")
        void shouldThrowExceptionWhenEmailIsNull() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Email.of(null));

            assertEquals("E-mail não pode ser nulo ou vazio", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando email for vazio")
        void shouldThrowExceptionWhenEmailIsEmpty() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Email.of(""));

            assertEquals("E-mail não pode ser nulo ou vazio", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção quando email for blank")
        void shouldThrowExceptionWhenEmailIsBlank() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Email.of("   "));

            assertEquals("E-mail não pode ser nulo ou vazio", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para email sem arroba")
        void shouldThrowExceptionForInvalidEmailWithoutAt() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Email.of("usertest.com"));

            assertEquals("E-mail inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para email sem domínio válido")
        void shouldThrowExceptionForInvalidDomain() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Email.of("user@test"));

            assertEquals("E-mail inválido", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção para email com caracteres inválidos")
        void shouldThrowExceptionForInvalidCharacters() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> Email.of("user@te$st.com"));

            assertEquals("E-mail inválido", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Método nullable")
    class NullableEmail {

        @Test
        @DisplayName("Deve criar Email com valor vazio")
        void shouldCreateNullableEmail() {
            Email email = Email.nullable();

            assertNotNull(email);
            assertEquals("", email.getValue());
        }
    }

    @Nested
    @DisplayName("Equals e HashCode")
    class EqualsAndHashCode {

        @Test
        @DisplayName("Emails com mesmo valor devem ser iguais")
        void shouldBeEqualWhenValuesAreSame() {
            Email email1 = Email.of("user@test.com");
            Email email2 = Email.of("USER@TEST.COM");

            assertEquals(email1, email2);
            assertEquals(email1.hashCode(), email2.hashCode());
        }

        @Test
        @DisplayName("Emails com valores diferentes não devem ser iguais")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            Email email1 = Email.of("user1@test.com");
            Email email2 = Email.of("user2@test.com");

            assertNotEquals(email1, email2);
        }

        @Test
        @DisplayName("Email não deve ser igual a null ou outro tipo")
        void shouldNotBeEqualToNullOrDifferentType() {
            Email email = Email.of("user@test.com");

            assertNotEquals(email, null);
            assertNotEquals(email, "user@test.com");
        }
    }

    @Nested
    @DisplayName("toString")
    class ToStringTest {

        @Test
        @DisplayName("toString deve retornar o valor do email")
        void shouldReturnEmailValueOnToString() {
            Email email = Email.of("user@test.com");

            assertEquals("user@test.com", email.toString());
        }
    }
}
