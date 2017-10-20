package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PriorityTest {

    @Test
    public void isValidPriority() {
        // invalid names

        assertFalse(Priority.isValidPriority(" ")); // spaces only
        assertFalse(Priority.isValidPriority("invalid")); // invalid priority
        assertFalse(Priority.isValidPriority("777")); // priority out of range
        assertFalse(Priority.isValidPriority("0")); // priority out of range

        // valid names
        assertTrue(Priority.isValidPriority("")); // empty string
        assertTrue(Priority.isValidPriority("1")); // numerical representation
    }
}
