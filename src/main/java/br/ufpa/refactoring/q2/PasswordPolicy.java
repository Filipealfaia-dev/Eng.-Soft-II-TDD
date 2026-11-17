package br.ufpa.refactoring.q2;

public class PasswordPolicy {

    private static final int MIN_LENGTH = 10;

    public boolean isValid(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return false;
        }
        return !password.contains(" ")
                && password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isDigit);
    }
}
