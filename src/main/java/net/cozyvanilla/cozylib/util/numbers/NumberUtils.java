package net.cozyvanilla.cozylib.util.numbers;

public class NumberUtils {

    private NumberUtils() {}

    /**
     * Cleans a number string by trimming it and removing commas and underscores.
     *
     * @param value the number string to clean
     * @return the cleaned number string
     */
    private static String cleanNumberString(String value) {
        if (value == null || value.isEmpty()) {
            throw new NumberFormatException("Input string is null or empty");
        }

        value = value.trim();

        if (value.isEmpty()) {
            throw new NumberFormatException("Input string is null or empty");
        }

        if (!value.matches("-?[0-9_,.]+")) {
            throw new NumberFormatException("Invalid characters in input: " + value);
        }

        if (value.indexOf('-') > 0) {
            throw new NumberFormatException("Invalid negative sign placement: " + value);
        }

        return value.replace(",", "").replace("_", "");
    }

    /**
     * Removes the decimal part from a number string.
     *
     * @param value the number string to modify
     * @return the number string without its decimal part
     */
    private static String removeDecimalPart(String value) {
        int decimalIndex = value.indexOf('.');

        if (decimalIndex == -1) {
            return value;
        }

        return value.substring(0, decimalIndex);
    }

    /**
     * Converts a number string into a double.
     *
     * @param value the number string to convert
     * @return the parsed double value
     */
    public static double toDouble(String value) {
        String cleanedValue = cleanNumberString(value);

        try {
            return Double.parseDouble(cleanedValue);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Unable to parse double from input: " + value);
        }
    }

    /**
     * Converts a number string into a long.
     * Decimal values are truncated before parsing.
     *
     * @param value the number string to convert
     * @return the parsed long value
     */
    public static long toLong(String value) {
        String cleanedValue = cleanNumberString(value);

        try {
            return Long.parseLong(removeDecimalPart(cleanedValue));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Unable to parse long from input: " + value);
        }
    }

    /**
     * Converts a number string into an int.
     * Decimal values are truncated before parsing.
     *
     * @param value the number string to convert
     * @return the parsed int value
     */
    public static int toInt(String value) {
        String cleanedValue = cleanNumberString(value);

        try {
            return Integer.parseInt(removeDecimalPart(cleanedValue));
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Unable to parse int from input: " + value);
        }
    }

    /**
     * Converts a number string into a float.
     *
     * @param value the number string to convert
     * @return the parsed float value
     */
    public static float toFloat(String value) {
        String cleanedValue = cleanNumberString(value);

        try {
            return Float.parseFloat(cleanedValue);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Unable to parse float from input: " + value);
        }
    }
}