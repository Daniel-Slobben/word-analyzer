package utils;

import java.util.Random;

public class TestUtils {
    public static String getStringOfLength(int length) {
        final String possibilities = "abcdABCD_@! ";
        var largeString = new StringBuilder();
        var random = new Random();
        for (int i = 0; i < length; i++) {
            largeString.append(possibilities.charAt(random.nextInt(possibilities.length())));
        }
        return largeString.toString();
    }
}
