package fa.training.fjb04.ims.util;

import fa.training.fjb04.ims.util.constant.AppConstants;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordGenerator {
    private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT_CHARS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}/?";

    private static final String ALL_CHAR = LOWERCASE_CHARS + UPPERCASE_CHARS + DIGIT_CHARS + SPECIAL_CHARS;

    public static String generatePassword() {

        SecureRandom random = new SecureRandom();
        List<Character> characters = new ArrayList<>();
        characters.add(getRandomChar(LOWERCASE_CHARS, random));
        characters.add(getRandomChar(UPPERCASE_CHARS, random));
        characters.add(getRandomChar(DIGIT_CHARS, random));
        characters.add(getRandomChar(SPECIAL_CHARS, random));
        for (int i = 4; i < 8; i++) {
            characters.add(getRandomChar(ALL_CHAR, random));
        }

        Collections.shuffle(characters);
        return characters.stream().map(v -> v.toString()).collect(Collectors.joining());
    }


    private static char getRandomChar(String charSet, SecureRandom random) {
        int randomIndex = random.nextInt(charSet.length());
        return charSet.charAt(randomIndex);
    }


}
