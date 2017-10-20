package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class NameTest {

    @Test
    public void isValidName() {
        // invalid names
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("")); // empty string

        // valid names
        assertTrue(Name.isValidName(".")); // single character
        assertTrue(Name.isValidName("buy pencil")); // alphanumerical with spaces
        assertTrue(Name.isValidName("buy pencil!")); // special symbols
    }
}
