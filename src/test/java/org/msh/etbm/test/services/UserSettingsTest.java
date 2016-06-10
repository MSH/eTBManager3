package org.msh.etbm.test.services;

import org.junit.Test;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.services.session.usersettings.UserSettingsFormData;
import org.msh.etbm.services.session.usersettings.UserSettingsService;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;

/**
 * Test the service {@link UserSettingsService}
 * Created by rmemoria on 14/5/16.
 */
public class UserSettingsTest extends AuthenticatedTest {

    @Autowired
    UserSettingsService userSettingsService;

    @Test
    public void testRecoverAndSave() {
        UserSettingsFormData data = userSettingsService.get();

        assertNotNull(data.getName());
        assertNotNull(data.getEmail());

        data.setMobile(Optional.of("21999744853"));
        data.setName(Optional.of("Jimmy"));

        userSettingsService.update(data);

        UserSettingsFormData data2 = userSettingsService.get();

        assertEquals(data.getEmail(), data2.getEmail());
        assertEquals(data.getName(), data2.getName());
    }

    @Test
    public void invalidEmail() {
        UserSettingsFormData data = userSettingsService.get();

        Optional<String> email = data.getEmail();

        // enter an invalid e-mail address
        data.setEmail(Optional.of("teste"));

        try {
            userSettingsService.update(data);
            fail("e-mail should not be valid");
        } catch (EntityValidationException e) {
            assertNotNull(e.getBindingResult().getFieldError("email"));
            assertEquals(1, e.getBindingResult().getErrorCount());
            assertThat(e, isA(EntityValidationException.class));
        }

        // read it again
        UserSettingsFormData data2 = userSettingsService.get();

        // and check if e-mail continue the same
        assertEquals(email, data2.getEmail());
    }

    @Test
    public void invalidMobile() {
        UserSettingsFormData data = userSettingsService.get();

        Optional<String> mobile = data.getMobile();

        // enter an invalid e-mail address
        data.setMobile(Optional.of("teste"));

        try {
            userSettingsService.update(data);
            fail("Mobile number should not be valid");
        } catch (EntityValidationException e) {
            assertNotNull(e.getBindingResult().getFieldError("mobile"));
            assertEquals(1, e.getBindingResult().getErrorCount());
            assertThat(e, isA(EntityValidationException.class));
        }

        // read it again
        UserSettingsFormData data2 = userSettingsService.get();

        // and check if e-mail continue the same
        assertEquals(mobile, data2.getMobile());
    }
}
