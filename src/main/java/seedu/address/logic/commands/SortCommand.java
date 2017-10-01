package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Sorts all persons in address book by any field. Sorting can be done in ascending or descending order
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final ArrayList<String> ACCEPTED_FIELD_PARAMETERS = new ArrayList<>(Arrays.asList(
            "name", "phone", "email", "address"));

    public static final ArrayList<String> ACCEPTED_ORDER_PARAMETERS = new ArrayList<>(Arrays.asList(
            "asc", "desc"));

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all persons by chosen "
                + "field [NAME/PHONE/EMAIL/ADDRESS] and by order [ASC/DESC]. Case insensitive\n"
                + "Parameters: KEYWORD [FIELD] [ORDER]\n"
                + "Example: " + COMMAND_WORD + " email desc";

    public static final String MESSAGE_INVALID_INPUT = "Invalid Input.\n"
            + "Accepted Field Values: NAME, PHONE, EMAIL, ADDRESS \n"
            + "Accepted Order Values: ASC, DESC";

    public static final String MESSAGE_SUCCESS = "All persons in address book successfully sorted";

    private final String field;
    private final String order;

    public SortCommand(String field, String order) {
        this.field = field;
        this.order = order;
    }

    private String getField() {
        return this.field;
    }

    private String getOrder() {
        return this.order;
    }

    @Override
    public CommandResult execute() {
//        System.out.println(getField() + "\n");
//        System.out.println(getOrder() + "\n");

        model.sortBy(getField(), getOrder());
        return new CommandResult(MESSAGE_SUCCESS);
    }

}