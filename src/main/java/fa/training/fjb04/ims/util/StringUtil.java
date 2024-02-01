package fa.training.fjb04.ims.util;


import com.ibm.icu.text.Normalizer;

import java.util.regex.Pattern;

public class StringUtil {

    public static String formatString(String input) {
        String[] name = input.trim().toLowerCase().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String s : name) {
            builder.append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1).toLowerCase()).append(" ");
        }
        return builder.toString().trim();
    }

    public static String normalizeAndRemoveDiacritics(String input) {
        // Chuẩn hóa chuỗi về Unicode Normalization Form D (NFD)
        String normalizedString = Normalizer.normalize(input, Normalizer.NFD);

        // Loại bỏ các dấu thanh và dấu mũ diacritical
        normalizedString = removeDiacritics(normalizedString);

        return normalizedString;
    }

    private static String removeDiacritics(String input) {
        // Thay thế các ký tự có thể tương đương với "đ" thành "d"
        String replacedString = input.replaceAll("[đĐ]", "D");

        // Loại bỏ các dấu thanh và dấu mũ diacritical
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(replacedString).replaceAll("");
    }

    public static String newArrayAccount(String[] array) {
        StringBuilder result = new StringBuilder();
        for (String s : array) {
            String firstChar = normalizeAndRemoveDiacritics(String.valueOf(s.charAt(0)));
            result.append(firstChar.toUpperCase());
        }
        return result.toString().trim();
    }
}
