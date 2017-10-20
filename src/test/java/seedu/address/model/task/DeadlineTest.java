package seedu.address.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeadlineTest {

    @Test
    public void isValidDeadline() {
        // invalid deadlines
        assertFalse(Deadline.isValidDeadline(" ")); // spaces only
        assertFalse(Deadline.isValidDeadline("alphabets")); // non-numeric letters
        assertFalse(Deadline.isValidDeadline("!@#$%^")); // invalid symbols

        assertFalse(Deadline.isValidDeadline("-04-17")); // missing day
        assertFalse(Deadline.isValidDeadline("04--17")); // missing month
        assertFalse(Deadline.isValidDeadline("04-04")); // missing year

        assertFalse(Deadline.isValidDeadline("day-04-17")); // invalid day
        assertFalse(Deadline.isValidDeadline("04-month-17")); // invalid month
        assertFalse(Deadline.isValidDeadline("04-04-year")); // invalid year
        assertFalse(Deadline.isValidDeadline("32-04-17")); // impossible day
        assertFalse(Deadline.isValidDeadline("04-13-17")); // impossible month
        assertFalse(Deadline.isValidDeadline("04-04--1")); // impossible year

        assertFalse(Deadline.isValidDeadline("0-04-17")); // incomplete day
        assertFalse(Deadline.isValidDeadline("04-0-17")); // incomplete month
        assertFalse(Deadline.isValidDeadline("04-04-1")); // incomplete year
        assertFalse(Deadline.isValidDeadline("04-04-201")); // incomplete year

        assertFalse(Deadline.isValidDeadline("04-04/17")); // inconsistent separators
        assertFalse(Deadline.isValidDeadline("04-042017")); // missing separator

        // valid deadline (empty deadline for optional data)
        assertTrue(Deadline.isValidDeadline("")); // empty string

        // valid deadlines
        assertTrue(Deadline.isValidDeadline("04-04-2017")); // dashes
        assertTrue(Deadline.isValidDeadline("04/04/2017")); // slashes
        assertTrue(Deadline.isValidDeadline("04.04.2017")); // dots
    }
}
