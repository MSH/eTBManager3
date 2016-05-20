package org.msh.etbm.test.commons.commands;

import org.junit.Test;
import org.msh.etbm.commons.commands.details.CommandLogDetail;
import org.msh.etbm.commons.commands.details.DetailWriter;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmemoria on 7/3/16.
 */
public class DetailWriterTest {

    private static final String TEXT = "This is a simple text";

    @Test
    public void testWriter() {
        DetailWriter writer = new DetailWriter();

        Date dt = new Date();

        writer.setText(TEXT);
        writer.addItem("Patient.name", "Ricardo Lima");
        writer.addItem("TbCase.iniTreatmentDate", dt);
        writer.addItem("TbCase.age", 44);

        CommandLogDetail det = writer.getDetail();

        assertNotNull(det);
        assertEquals(det.getItems().size(), 3);
        assertEquals(det.getText(), TEXT);
    }
}
