package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ChangePrivacyCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class ChangePrivacyCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE);

    private ChangePrivacyCommandParser parser = new ChangePrivacyCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", ChangePrivacyCommand.MESSAGE_NO_FIELDS);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // Non boolean argument
        assertParseFailure(parser, "1" + " " + PREFIX_NAME + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1" + " " + PREFIX_PHONE + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1" + " " + PREFIX_EMAIL + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1" + " " + PREFIX_ADDRESS + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));

        // valid value followed by invalid value. The test case for invalid value  followed by valid value
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1" + " " + PREFIX_NAME + "true" + " " + PREFIX_NAME + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1" + " " + PREFIX_PHONE + "true" + " " + PREFIX_PHONE + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1" + " " + PREFIX_EMAIL + "true" + " " + PREFIX_EMAIL + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1" + " " + PREFIX_ADDRESS + "true" + " " + PREFIX_ADDRESS + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + TAG_DESC_HUSBAND
                + EMAIL_DESC_AMY + ADDRESS_DESC_AMY + NAME_DESC_AMY + TAG_DESC_FRIEND;

        EditCommand.EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
