package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.SortCommand.MESSAGE_INVALID_INPUT;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object based on the field and order parameters provided
 */
public class SortCommandParser implements Parser<SortCommand> {

    public static final int FIELD_ARG_POSITION = 0;
    public static final int ORDER_ARG_POSITION = 1;

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns a SortCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        // Converts arg arrays to lower case to account for caps entries
        String[] argKeywords = trimmedArgs.split("\\s");
        for (int i = 0; i < argKeywords.length; i++) {
            argKeywords[i] = argKeywords[i].toLowerCase();
        }

        if (argKeywords.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        if (!SortCommand.ACCEPTED_FIELD_PARAMETERS.contains(argKeywords[FIELD_ARG_POSITION])
                    || !SortCommand.ACCEPTED_ORDER_PARAMETERS.contains(argKeywords[ORDER_ARG_POSITION])) {
            throw new ParseException(String.format(MESSAGE_INVALID_INPUT, SortCommand.MESSAGE_USAGE));
        }

        // If there are no problems with the input, return a new instance of SortCommand
        return new SortCommand(argKeywords[FIELD_ARG_POSITION], argKeywords[ORDER_ARG_POSITION]);

    }

}
