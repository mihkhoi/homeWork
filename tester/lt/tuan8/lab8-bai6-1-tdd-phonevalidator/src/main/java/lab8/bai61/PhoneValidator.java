package lab8.bai61;

public
class PhoneValidator {

  public
    static boolean isValid(String phone) {
        if (phone == null) {
            return false;
        }

        String trimmed = phone.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        for (int i = 0; i < trimmed.length(); i++) {
            char ch = trimmed.charAt(i);
            boolean isDigit = Character.isDigit(ch);
            boolean isPlus = ch == '+';
            boolean isSpace = Character.isWhitespace(ch);

            if (!isDigit && !isPlus && !isSpace) {
                return false;
            }
        }

        String normalized = trimmed.replaceAll("\\s+", "");

        if (normalized.startsWith("+84")) {
            normalized = "0" + normalized.substring(3);
        }

        if (!normalized.startsWith("0")) {
            return false;
        }

        if (!normalized.matches("\\d+")) {
            return false;
        }

        if (normalized.length() != 10) {
            return false;
        }

        return normalized.matches("0(3|5|7|8|9)\\d{8}");
    }
}
