package org.msh.etbm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 19/8/15.
 */
public class CommandLineArgs {

    public static final String DBURL = "db.url";
    public static final String DBUSER = "db.user";
    public static final String DBPASSWORD = "db.password";

    public static String[] evaluate(String[] args) {
        List<String> res = new ArrayList<>();

        for (String arg: args) {

        }
        return (String[])res.toArray();
    }

    protected static String evaluateArg(String arg) {
        int index = arg.indexOf("=");
        if (index == -1) {
            return null;
        }

        String key = arg.substring(0, index);
        String value = arg.substring(index + 1);

        if (DBURL.equals(arg)) {
            return "spring.datasource.url=";
        }

        return null;
    }
}
