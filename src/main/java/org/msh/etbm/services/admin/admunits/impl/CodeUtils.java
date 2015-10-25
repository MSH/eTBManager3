package org.msh.etbm.services.admin.admunits.impl;

/**
 * Simple utility functions to help on management of administrative unit codes
 *
 * Created by rmemoria on 23/10/15.
 */
public class CodeUtils {
    /**
     * Generate a new code from a given code incrementing its value
     * @param code
     * @return
     */
    static public String incCode(String code) {
        if (code.length() > 3)
            throw new RuntimeException("incCode cannot generate value bigger than 3 digits");
        // transform code to int
        int value = codeToInt(code);

        // inc value
        value++;

        return intToCode(value);
    }

    static public int codeToInt(String code) {
        int index = code.length()-1;
        int value = 0;
        int mult = 1;
        for (int i = index; i >= 0; i--) {
            char c = code.charAt(i);
            int val = 0;
            if ((c >= '0') && (c <= '9'))
                val = ((int)c) - 48;
            else val = ((int)c) - 65 + 10;

            value += val * mult;
            mult *= 36;
        }

        return value;
    }

    /**
     * Convert a code to an integer value
     * @param val
     * @return
     */
    static public String intToCode(int val) {
        String result = "";
        while (true) {
            char c;
            int digit = val % 36;
            val = val / 36;
            if (digit <= 9)
                c = Integer.toString(digit).charAt(0);
            else c = (char)(digit + 65 - 10);
            result = c + result;
            if (val == 0)
                break;
        }

        result = "000" + result;
        result = result.substring(result.length() - 3);
        return result;
    }
}
