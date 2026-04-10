package net.cozyvanilla.cozylib.utilities.numbers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntegerUtils {

    /**
     * Parses the given string into an integer, stripping out non‑digit characters (except a leading minus).
     * Examples:
     *   "1000"    → 1000
     *   "1,234"   → 1234
     *   "-56px"   → -56
     *
     * @param value the string to convert
     * @return the parsed integer
     * @throws NumberFormatException if no valid digits are found
     */
    public static int toInt(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new NumberFormatException("Cannot parse integer from empty or null string");
        }
        String str = value.trim();
        boolean negative = str.startsWith("-");

        String digits = str.replaceAll("\\D", "");
        if (digits.isEmpty()) {
            throw new NumberFormatException("No digits found in input: " + value);
        }
        if (negative) {
            digits = "-" + digits;
        }
        return Integer.parseInt(digits);
    }

    /**
     * Converts a double value to an int by rounding to the nearest whole number.
     *
     * @param value the double value to convert
     * @return the rounded int value
     */
    public static int toInt(double value) {
        return Math.toIntExact(Math.round(value));
    }

    /**
     * Converts a float value to an int by rounding to the nearest whole number.
     *
     * @param value the float value to convert
     * @return the rounded int value
     */
    public static int toInt(float value) {
        return Math.round(value);
    }

    /**
     * Converts a BigDecimal value to an int using HALF_UP rounding.
     *
     * @param value the BigDecimal value to convert
     * @return the rounded int value
     */
    public static int toInt(BigDecimal value) {
        return value.setScale(0, RoundingMode.HALF_UP).intValueExact();
    }
}