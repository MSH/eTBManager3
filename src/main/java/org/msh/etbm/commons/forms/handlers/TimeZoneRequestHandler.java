package org.msh.etbm.commons.forms.handlers;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestHandler;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * Created by rmemoria on 15/5/16.
 */
@Component
public class TimeZoneRequestHandler implements FormRequestHandler<List<Item>> {

    private DecimalFormat df = new DecimalFormat("00");

    @Override
    public String getFormCommandName() {
        return "timeZones";
    }

    @Override
    public List<Item> execFormRequest(FormRequest req) {
        String[] timeZones = TimeZone.getAvailableIDs();
        List<Item> lst = new ArrayList<>();

        for (String tzId: timeZones) {
            TimeZone tz = TimeZone.getTimeZone(tzId);
            String name = getGMTDisplay(tz);

            lst.add(new Item(tzId, name));
        }

        // sort list
        lst.sort(new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        return lst;
    }

    /**
     * Return the display name of the time zone
     * @param tm instance of {@link TimeZone} object
     * @return the display name of the time zone
     */
    private String getGMTDisplay(TimeZone tm) {
        int rawOffset = tm.getRawOffset();
        int hour = rawOffset / (60 * 60 * 1000);
        int min = Math.abs(rawOffset / (60 * 1000)) % 60;

        String gmt = hour == 0 ? "(GMT)" : "(GMT" + df.format(hour) + ":" + df.format(min) + ")";

        return gmt + " " + tm.getID();
    }

}
