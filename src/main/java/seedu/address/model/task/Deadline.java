package seedu.address.model.task;

import java.util.Calendar;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents the deadline of a task in the address book.
 */
public class Deadline {
    public static final String MESSAGE_DEADLINE_CONSTRAINTS =
            "Task deadlines must be in the format DD-MM-YYYY, with '-', '.', '.' as separators";
    public static final String DEADLINE_PLACEHOLDER_VALUE = "";
    /*
    Deadline format: DDSMMSYYYY, in DAY-MONTH-YEAR format.
    S represents the separators, and can be any of these characters: - . /
     */
    public static final String DEADLINE_VALIDATION_REGEX = "\\d\\d[-./]\\d\\d[-./]\\d\\d\\d\\d.*";
    private static final String DEADLINE_PERIOD_DELIMITER = ".";

    /*
    Expected indexes for the separator characters
     */
    private static final int DEADLINE_SEPARATOR_INDEX_1 = 2;
    private static final int DEADLINE_SEPARATOR_INDEX_2 = 5;
    private static final int DEADLINE_DAY_INDEX = 0;
    private static final int DEADLINE_MONTH_INDEX = 1;
    private static final int DEADLINE_YEAR_INDEX = 2;

    public final Calendar calendar;
    public final String value;

    /**
     * Validates given deadline.
     *
     * @throws IllegalValueException if given deadline string is invalid.
     */
    public Deadline(String deadline) throws IllegalValueException {
        if (deadline == null) {
            this.value = DEADLINE_PLACEHOLDER_VALUE;
            this.calendar = null;
            return;
        }
        String trimmedDeadline = deadline.trim();
        if (!isValidDeadline(trimmedDeadline)) {
            throw new IllegalValueException(MESSAGE_DEADLINE_CONSTRAINTS);
        }
        this.value = trimmedDeadline;
        this.calendar = Calendar.getInstance();
        calendar.clear();
        char separator = trimmedDeadline.charAt(DEADLINE_SEPARATOR_INDEX_1);
        String[] splitTest = trimmedDeadline.split(Character.toString(separator));
        int day = Integer.parseInt(splitTest[DEADLINE_DAY_INDEX]);
        int month = Integer.parseInt(splitTest[DEADLINE_MONTH_INDEX]);
        int year = Integer.parseInt(splitTest[DEADLINE_YEAR_INDEX]);

        this.calendar.set(year, month, day);
    }

    /**
     * Returns true if a given string is in valid deadline format.
     */
    public static boolean isValidDeadline(String test) {
        if (test.equals(DEADLINE_PLACEHOLDER_VALUE)) {
            return true;
        } else if (!test.matches(DEADLINE_VALIDATION_REGEX)) {
            return false;
        } else if (test.charAt(DEADLINE_SEPARATOR_INDEX_1) != test.charAt(DEADLINE_SEPARATOR_INDEX_2)) {
            return false;
        } else {
            return isValidDate(test);
        }
    }

    /**
     * Returns true if the given string is a valid date.
     * Guarantees: given string format is valid
     */
    public static boolean isValidDate(String test) {
        Calendar testCalendar = setCalendar(test);
        try {
            testCalendar.setLenient(false);
            testCalendar.getTime();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Returns a Calendar object that represents the given date string.
     */
    private static Calendar setCalendar(String date) {
        Calendar result = Calendar.getInstance();
        result.clear();
        String separator = Character.toString(date.charAt(DEADLINE_SEPARATOR_INDEX_1));
        if (separator.equals(DEADLINE_PERIOD_DELIMITER)) {
            separator = "\\.";
        }

        String[] splitTest = date.split(separator);

        int day = Integer.parseInt(splitTest[DEADLINE_DAY_INDEX]);
        int month = Integer.parseInt(splitTest[DEADLINE_MONTH_INDEX]);
        int year = Integer.parseInt(splitTest[DEADLINE_YEAR_INDEX]);

        result.set(year, month, day);
        return result;
    }
    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                && this.value.equals(((Deadline) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
