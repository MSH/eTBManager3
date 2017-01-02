package org.msh.etbm.commons.sqlquery;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of functions to help on SQL parsing
 * Created by rmemoria on 23/12/16.
 */
public class SQLParseUtils {

    /**
     * Parse the first field in the string delimited by commas (,).
     * It also supports expression inside round brackets
     * @param s
     * @return
     */
    public static int parseFirstField(String s) {
        int i = 0;
        // number of left round brackets
        int count = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            switch (c) {
                case '(':
                    count++;
                    break;
                case ')':
                    count--;
                    break;
                case ',':
                    if (count == 0) {
                        return i;
                    }
            }
            i++;
        }

        return s.length();
    }

    /**
     * Parse all fields separated by commas (,) and return in an array of Strings.
     * Supports round brackets as expressions
     * @param fields the list of fields separated by commas
     * @return
     */
    public static List<String> parseFields(String fields) {
        List<String> lst = new ArrayList<>();

        String s = fields;
        while (true) {
            int pos = parseFirstField(s);
            String f = s.substring(0, pos);

            lst.add(f.trim());

            if (pos + 1 >= s.length()) {
                break;
            }

            s = s.substring(pos + 1);
            if (s.isEmpty()) {
                break;
            }
        }

        return lst;
    }
}
