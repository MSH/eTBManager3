package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.indicators.indicator.DataTableIndicator;
import org.msh.etbm.commons.indicators.indicator.DataTableIndicatorImpl;
import org.msh.etbm.commons.indicators.indicator.IndicatorColumn;
import org.msh.etbm.commons.indicators.indicator.IndicatorRow;

import static org.junit.Assert.*;

/**
 * Test the {@link DataTableIndicator}
 *
 * Created by rmemoria on 10/9/16.
 */
public class DataTableIndicatorTest {

    @Test
    public void simpleTest() {
        DataTableIndicator tbl = new DataTableIndicatorImpl(2, 2);

        IndicatorColumn colA = tbl.getIndicatorColumn(0);
        assertNotNull(colA);
        colA.setKey("A");

        IndicatorColumn colB = tbl.getIndicatorColumn(1);
        assertNotNull(colB);
        colB.setKey("B");

        IndicatorColumn colA1 = tbl.addIndicatorColumn(colA);
        assertNotNull(colA1);
        IndicatorColumn colA2 = tbl.addIndicatorColumn(colA);
        assertNotNull(colA2);
        colA1.setKey(1);
        colA2.setKey(2);

        IndicatorColumn colB1 = tbl.addIndicatorColumn(colB);
        assertNotNull(colB1);
        IndicatorColumn colB2 = tbl.addIndicatorColumn(colB);
        assertNotNull(colB2);
        IndicatorColumn colB3 = tbl.addIndicatorColumn(colB);
        assertNotNull(colB3);
        colB1.setKey(1);
        colB1.setKey(2);
        colB1.setKey(3);

        IndicatorRow row = tbl.getIndicatorRow(0);
        row.setKey("R1");

        Object[] cols = {"A", 1};
        Object[] rows = {"R1"};
        tbl.addIndicatorValue(cols, rows, 100);

        System.out.println(tbl.getIndicatorValue(0, 0));
    }

}
