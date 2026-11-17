package br.ufpa.refactoring.q2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordPolicyTest {

    @Test
    void validPassword_examples() {
        PasswordPolicy policy = new PasswordPolicy();
        assertTrue(policy.isValid("SenhaForte1"));
        assertTrue(policy.isValid("Abcdefgh12")); // exatamente 10 caracteres
    }

    @Test
    void invalidPassword_examples() {
        PasswordPolicy policy = new PasswordPolicy();
        assertFalse(policy.isValid(null));            // nula
        assertFalse(policy.isValid("curta1A"));       // < 10
        assertFalse(policy.isValid("senhaforte1"));   // sem maiúscula
        assertFalse(policy.isValid("SenhaForteX"));   // sem dígito
        assertFalse(policy.isValid("Senha Forte1"));  // espaço
    }

    @Test
    void refactorGoal_inlineSomeHelpers() {
        PasswordPolicy policy = new PasswordPolicy();
        assertTrue(policy.isValid("ValidPass1"));
        assertFalse(policy.isValid("Invalid Pass1"));
    }
}
