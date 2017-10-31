//@@author jeffreygohkw
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.SortCommand.MESSAGE_INVALID_INPUT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class SortCommandParserTest {

    private SortCommandParser parser = new SortCommandParser();

    @Test
    public void no_arguments_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_wrongArguments_failure() {
        // no field specified
        assertParseFailure(parser, "asc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "desc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));

        // no order specified
        assertParseFailure(parser, "name",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "phone",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "email",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "address",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));

        // no field or order
        assertParseFailure(parser, "random text",
                String.format(MESSAGE_INVALID_INPUT, SortCommand.MESSAGE_USAGE));

    }


    @Test
    public void parse_validArguments_success() throws ParseException {
        SortCommand expectedCommand;
        SortCommand actualCommand;

        expectedCommand = new SortCommand("name", "asc");
        actualCommand = parser.parse("name asc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("name", "desc");
        actualCommand = parser.parse("name desc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("phone", "asc");
        actualCommand = parser.parse("phone asc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("phone", "desc");
        actualCommand = parser.parse("phone desc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("email", "asc");
        actualCommand = parser.parse("email asc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("email", "desc");
        actualCommand = parser.parse("email desc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("address", "asc");
        actualCommand = parser.parse("address asc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());

        expectedCommand = new SortCommand("address", "desc");
        actualCommand = parser.parse("address desc");

        assertEquals(expectedCommand.getField(), actualCommand.getField());
        assertEquals(expectedCommand.getOrder(), actualCommand.getOrder());
    }
}
