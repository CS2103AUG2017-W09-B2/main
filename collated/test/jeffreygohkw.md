# jeffreygohkw
###### \java\seedu\address\logic\commands\ChangePrivacyCommandTest.java
``` java
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersonsAddressBook;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonPrivacySettingsBuilder;

public class ChangePrivacyCommandTest {

    private Model model = new ModelManager(getTypicalPersonsAddressBook(), new UserPrefs());

    @Test
    public void personPrivacySettingsTests() {
        PersonPrivacySettings pps = new PersonPrivacySettings();

        assertFalse(pps.isAnyFieldNonNull());

        PersonPrivacySettings ppsByBuilder = new PersonPrivacySettingsBuilder().setNamePrivate("true")
            .setPhonePrivate("false").setEmailPrivate("true").setAddressPrivate("true").build();

        pps.setNameIsPrivate(true);
        pps.setPhoneIsPrivate(false);
        pps.setEmailIsPrivate(true);
        pps.setAddressIsPrivate(true);

        assertEquals(ppsByBuilder.getAddressIsPrivate(), pps.getAddressIsPrivate());
        assertEquals(ppsByBuilder.getEmailIsPrivate(), pps.getEmailIsPrivate());
        assertEquals(ppsByBuilder.getNameIsPrivate(), pps.getNameIsPrivate());
        assertEquals(ppsByBuilder.getPhoneIsPrivate(), pps.getPhoneIsPrivate());
        assertEquals(ppsByBuilder.isAnyFieldNonNull(), pps.isAnyFieldNonNull());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person newPerson = new PersonBuilder().withEmail("alice@example.com").build();
        newPerson.getEmail().setPrivate(true);
        newPerson.setRemark(model.getFilteredPersonList().get(0).getRemark());

        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder(newPerson).setNamePrivate("false")
                .setPhonePrivate("false").setEmailPrivate("true").setAddressPrivate("false").build();
        ChangePrivacyCommand changePrivacyCommand = new ChangePrivacyCommand(INDEX_FIRST_PERSON, pps);
        changePrivacyCommand.model = model;

        String expectedMessage = String.format(ChangePrivacyCommand.MESSAGE_CHANGE_PRIVACY_SUCCESS, newPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        expectedModel.updatePerson(model.getFilteredPersonList().get(0), newPerson);

        assertCommandSuccess(changePrivacyCommand, model, expectedMessage, expectedModel);

        PersonPrivacySettings ppsPublic = new PersonPrivacySettingsBuilder(newPerson).setNamePrivate("false")
                .setPhonePrivate("false").setEmailPrivate("false").setAddressPrivate("false").build();

        newPerson.getEmail().setPrivate(false);

        ChangePrivacyCommand changePrivacyCommandPublic = new ChangePrivacyCommand(INDEX_FIRST_PERSON, ppsPublic);
        changePrivacyCommandPublic.setData(model, new CommandHistory(), new UndoRedoStack());

        String expectedMessagePublic = String.format(ChangePrivacyCommand.MESSAGE_CHANGE_PRIVACY_SUCCESS, newPerson);

        expectedModel.updatePerson(model.getFilteredPersonList().get(0), newPerson);

        assertCommandSuccess(changePrivacyCommandPublic, model, expectedMessagePublic, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        ReadOnlyPerson lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        Person personInList = new PersonBuilder().withName(lastPerson.getName().toString())
                .withPhone(lastPerson.getPhone().toString()).withEmail(lastPerson.getEmail().toString())
                .withAddress(lastPerson.getAddress().toString()).withRemark(lastPerson.getRemark().toString())
                .withFavourite(lastPerson.getFavourite().toString())
                .build();

        personInList.setTags(lastPerson.getTags());
        personInList.getName().setPrivate(true);
        personInList.getPhone().setPrivate(true);


        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder(personInList).setNamePrivate("true")
                .setPhonePrivate("true").build();
        ChangePrivacyCommand changePrivacyCommand = new ChangePrivacyCommand(indexLastPerson, pps);
        changePrivacyCommand.setData(model, new CommandHistory(), new UndoRedoStack());

        String expectedMessage = String.format(ChangePrivacyCommand.MESSAGE_CHANGE_PRIVACY_SUCCESS, personInList);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        expectedModel.updatePerson(lastPerson, personInList);

        assertCommandSuccess(changePrivacyCommand, model, expectedMessage, expectedModel);

        PersonPrivacySettings ppsPublic = new PersonPrivacySettingsBuilder(personInList).setNamePrivate("false")
                .setPhonePrivate("false").build();


        personInList.getName().setPrivate(false);
        personInList.getPhone().setPrivate(false);

        ChangePrivacyCommand changePrivacyCommandPublic = new ChangePrivacyCommand(indexLastPerson, ppsPublic);
        changePrivacyCommandPublic.setData(model, new CommandHistory(), new UndoRedoStack());

        String expectedMessagePublic = String.format(ChangePrivacyCommand.MESSAGE_CHANGE_PRIVACY_SUCCESS, personInList);

        expectedModel.updatePerson(lastPerson, personInList);

        assertCommandSuccess(changePrivacyCommandPublic, model, expectedMessagePublic, expectedModel);
    }
}
```
###### \java\seedu\address\logic\commands\EditCommandTest.java
``` java
    @Test
    public void execute_privateFields_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        personInFilteredList.getName().setPrivate(true);
        Name originalName = personInFilteredList.getName();

        personInFilteredList.getPhone().setPrivate(true);
        Phone originalPhone = personInFilteredList.getPhone();

        personInFilteredList.getEmail().setPrivate(true);
        Email originalEmail = personInFilteredList.getEmail();

        personInFilteredList.getAddress().setPrivate(true);
        Address originalAddress = personInFilteredList.getAddress();

        personInFilteredList.getRemark().setPrivate(true);
        Remark originalRemark = personInFilteredList.getRemark();

        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_ALL_FIELDS_PRIVATE);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), personInFilteredList);

        assertCommandFailure(editCommand, model, expectedMessage);

        assertEquals(personInFilteredList.getName(), originalName);
        assertEquals(personInFilteredList.getPhone(), originalPhone);
        assertEquals(personInFilteredList.getEmail(), originalEmail);
        assertEquals(personInFilteredList.getAddress(), originalAddress);
        assertEquals(personInFilteredList.getRemark(), originalRemark);

        personInFilteredList.getName().setPrivate(false);
        personInFilteredList.getPhone().setPrivate(false);
        personInFilteredList.getEmail().setPrivate(false);
        personInFilteredList.getAddress().setPrivate(false);
        personInFilteredList.getRemark().setPrivate(false);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_failure() {
        EditCommand editCommand = prepareCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());

        String expectedMessage = String.format(EditCommand.MESSAGE_ALL_FIELDS_PRIVATE);

        assertCommandFailure(editCommand, model, expectedMessage);
    }
```
###### \java\seedu\address\logic\commands\LocateCommandTest.java
``` java
package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.showFirstPersonOnly;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalPersonsAddressBook;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.BrowserPanelLocateEvent;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.ui.testutil.EventsCollectorRule;

/**
 * Contains integration tests (interaction with the Model) for {@code LocateCommand}.
 */
public class LocateCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalPersonsAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        assertExecutionSuccess(INDEX_FIRST_PERSON);
        assertExecutionSuccess(INDEX_THIRD_PERSON);
        assertExecutionSuccess(lastPersonIndex);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showFirstPersonOnly(model);

        assertExecutionSuccess(INDEX_FIRST_PERSON);
    }

    @Test
    public void execute_invalidIndexFilteredList_failure() {
        showFirstPersonOnly(model);

        Index outOfBoundsIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundsIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        assertExecutionFailure(outOfBoundsIndex, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        LocateCommand locateFirstCommand = new LocateCommand(INDEX_FIRST_PERSON);
        LocateCommand locateSecondCommand = new LocateCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(locateFirstCommand.equals(locateFirstCommand));

        // same values -> returns true
        LocateCommand locateFirstCommandCopy = new LocateCommand(INDEX_FIRST_PERSON);
        assertTrue(locateFirstCommand.equals(locateFirstCommandCopy));

        // different types -> returns false
        assertFalse(locateFirstCommand.equals(1));

        // null -> returns false
        assertFalse(locateFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(locateFirstCommand.equals(locateSecondCommand));
    }

    /**
     * Executes a {@code LocateCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess(Index index) {
        LocateCommand locateCommand = prepareCommand(index);
        ReadOnlyPerson p = model.getFilteredPersonList().get(index.getZeroBased());
        try {
            CommandResult commandResult = locateCommand.execute();
            assertEquals(String.format(LocateCommand.MESSAGE_LOCATE_PERSON_SUCCESS, index.getOneBased()),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        BrowserPanelLocateEvent lastEvent =
                (BrowserPanelLocateEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(p, lastEvent.getNewSelection());
    }

    /**
     * Executes a {@code LocateCommand} with the given {@code index}, and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, String expectedMessage) {
        LocateCommand locateCommand = prepareCommand(index);
        try {
            locateCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code LocateCommand} with parameters {@code index}.
     */
    private LocateCommand prepareCommand(Index index) {
        LocateCommand locateCommand = new LocateCommand(index);
        locateCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return locateCommand;
    }
}
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommandChangePrivacy() throws Exception {
        Person person = new PersonBuilder().build();
        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder(person).build();

        ChangePrivacyCommand command = (ChangePrivacyCommand) parser.parseCommand(
                ChangePrivacyCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_NAME + String.valueOf(person.getName().isPrivate())
                        + " " + PREFIX_PHONE + String.valueOf(person.getPhone().isPrivate())
                        + " " + PREFIX_EMAIL + String.valueOf(person.getEmail().isPrivate())
                        + " " + PREFIX_ADDRESS + String.valueOf(person.getAddress().isPrivate()));
        ChangePrivacyCommand actualCommand = new ChangePrivacyCommand(INDEX_FIRST_PERSON, pps);

        assertTrue(changePrivacyCommandsEqual(command, actualCommand));
    }

    @Test
    public void parseCommandAliasChangePrivacy() throws Exception {
        Person person = new PersonBuilder().build();
        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder(person).build();

        ChangePrivacyCommand command = (ChangePrivacyCommand) parser.parseCommand(
                ChangePrivacyCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased()
                        + " " + PREFIX_NAME + String.valueOf(person.getName().isPrivate())
                        + " " + PREFIX_PHONE + String.valueOf(person.getPhone().isPrivate())
                        + " " + PREFIX_EMAIL + String.valueOf(person.getEmail().isPrivate())
                        + " " + PREFIX_ADDRESS + String.valueOf(person.getAddress().isPrivate()));
        ChangePrivacyCommand actualCommand = new ChangePrivacyCommand(INDEX_FIRST_PERSON, pps);

        assertTrue(changePrivacyCommandsEqual(command, actualCommand));
    }
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommandLocate() throws Exception {
        LocateCommand command = (LocateCommand) parser.parseCommand(
                LocateCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new LocateCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommandAliasLocate() throws Exception {
        LocateCommand command = (LocateCommand) parser.parseCommand(
                LocateCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new LocateCommand(INDEX_FIRST_PERSON), command);
    }
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    /**
     * Checks if 2 ChangePrivacyCommands are equal
     * @param command the expected command
     * @param actualCommand the actual command
     * @return true if all the data are equal
     */
    private boolean changePrivacyCommandsEqual(ChangePrivacyCommand command, ChangePrivacyCommand actualCommand) {
        assertEquals(command.getIndex(), actualCommand.getIndex());
        assertEquals(command.getPps().getAddressIsPrivate(), actualCommand.getPps().getAddressIsPrivate());
        assertEquals(command.getPps().getNameIsPrivate(), actualCommand.getPps().getNameIsPrivate());
        assertEquals(command.getPps().getEmailIsPrivate(), actualCommand.getPps().getEmailIsPrivate());
        assertEquals(command.getPps().getPhoneIsPrivate(), actualCommand.getPps().getPhoneIsPrivate());
        return true;
    }
```
###### \java\seedu\address\logic\parser\ChangePrivacyCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static org.junit.Assert.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ChangePrivacyCommand;
import seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.testutil.PersonPrivacySettingsBuilder;

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
        assertParseFailure(parser, "1" + " " + PREFIX_REMARK + "notBoolean",
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
        assertParseFailure(parser, "1" + " " + PREFIX_REMARK + "true" + " " + PREFIX_REMARK + "notBoolean",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_allFieldsSpecified_success() throws ParseException {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NAME + "true" + " " + PREFIX_EMAIL + "false"
                + " " + PREFIX_ADDRESS + "true" + " " + PREFIX_PHONE + "false";

        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder().setNamePrivate("true")
                .setEmailPrivate("false").setAddressPrivate("true").setPhonePrivate("false").build();
        ChangePrivacyCommand expectedCommand = new ChangePrivacyCommand(targetIndex, pps);

        ChangePrivacyCommand actualCommand = parser.parse(userInput);

        compareChangePrivacyCommand(expectedCommand, actualCommand);

    }

    @Test
    public void parse_someFieldsSpecified_success() throws ParseException {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NAME + "true" + " " + PREFIX_EMAIL + "true";

        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder().setNamePrivate("true")
                .setEmailPrivate("true").build();
        ChangePrivacyCommand expectedCommand = new ChangePrivacyCommand(targetIndex, pps);

        ChangePrivacyCommand actualCommand = parser.parse(userInput);

        compareChangePrivacyCommand(expectedCommand, actualCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() throws ParseException {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NAME + "true";

        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder().setNamePrivate("true").build();
        ChangePrivacyCommand expectedCommand = new ChangePrivacyCommand(targetIndex, pps);

        ChangePrivacyCommand actualCommand = parser.parse(userInput);

        compareChangePrivacyCommand(expectedCommand, actualCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() throws ParseException {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NAME + "true" + " " + PREFIX_EMAIL + "false"
                + " " + PREFIX_ADDRESS + "true" + " " + PREFIX_PHONE + "false" + " " + PREFIX_NAME + "false" + " "
                + PREFIX_EMAIL + "true" + " " + PREFIX_ADDRESS + "false" + " " + PREFIX_PHONE + "true";

        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder().setNamePrivate("false")
                .setEmailPrivate("true").setAddressPrivate("false").setPhonePrivate("true").build();
        ChangePrivacyCommand expectedCommand = new ChangePrivacyCommand(targetIndex, pps);

        ChangePrivacyCommand actualCommand = parser.parse(userInput);

        compareChangePrivacyCommand(expectedCommand, actualCommand);
    }

    @Test
    public void parse_invalidValueFollowedByValidValue_success() throws ParseException {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_NAME + "notBoolean" + " " + PREFIX_NAME + "true";

        PersonPrivacySettings pps = new PersonPrivacySettingsBuilder().setNamePrivate("true").build();
        ChangePrivacyCommand expectedCommand = new ChangePrivacyCommand(targetIndex, pps);

        ChangePrivacyCommand actualCommand = parser.parse(userInput);

        compareChangePrivacyCommand(expectedCommand, actualCommand);
    }

    /**
     * Checks if two ChangePrivacyCommands are equal by comparing their contents
     * @param expectedCommand The expected ChangePrivacyCommand
     * @param actualCommand The actual ChangePrivacyCommand
     */
    private void compareChangePrivacyCommand(ChangePrivacyCommand expectedCommand, ChangePrivacyCommand actualCommand) {
        assertEquals(expectedCommand.getIndex(), actualCommand.getIndex());
        assertEquals(expectedCommand.getPps().getAddressIsPrivate(), actualCommand.getPps().getAddressIsPrivate());
        assertEquals(expectedCommand.getPps().getNameIsPrivate(), actualCommand.getPps().getNameIsPrivate());
        assertEquals(expectedCommand.getPps().getEmailIsPrivate(), actualCommand.getPps().getEmailIsPrivate());
        assertEquals(expectedCommand.getPps().getPhoneIsPrivate(), actualCommand.getPps().getPhoneIsPrivate());
        assertEquals(expectedCommand.getPps().getRemarkIsPrivate(), actualCommand.getPps().getRemarkIsPrivate());
    }
}
```
###### \java\seedu\address\logic\parser\LocateCommandParserTest.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.Test;

import seedu.address.logic.commands.LocateCommand;

/**
 * Test scope: similar to {@code SelectCommandParserTest}.
 * @see SelectCommandParserTest
 */
public class LocateCommandParserTest {

    private LocateCommandParser parser = new LocateCommandParser();

    @Test
    public void parse_validArgs_returnsLocateCommand() {
        assertParseSuccess(parser, "1", new LocateCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\logic\parser\SortCommandParserTest.java
``` java
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
```
###### \java\seedu\address\model\person\AddressTest.java
``` java
    @Test
    public void privateAddressIsHidden_success() throws IllegalValueException {
        Address a = new Address("Any Address", true);
        assertTrue(a.isPrivate());
        assertEquals(a.toString(), "<Private Address>");
    }
```
###### \java\seedu\address\model\person\EmailTest.java
``` java
    @Test
    public void privateEmailIsHidden_success() throws IllegalValueException {
        Email e = new Email("AnyEmail@example.com", true);
        assertTrue(e.isPrivate());
        assertEquals(e.toString(), "<Private Email>");
    }
```
###### \java\seedu\address\model\person\NameTest.java
``` java
    @Test
    public void privateNameIsHidden_success() throws IllegalValueException {
        Name n = new Name("Any Name", true);
        assertTrue(n.isPrivate());
        assertEquals(n.toString(), "<Private Name>");
    }
```
###### \java\seedu\address\model\person\PhoneTest.java
``` java
    @Test
    public void privatePhoneIsHidden_success() throws IllegalValueException {
        Phone p = new Phone("999", true);
        assertTrue(p.isPrivate());
        assertEquals(p.toString(), "<Private Phone>");
    }
```
###### \java\seedu\address\testutil\PersonPrivacySettingsBuilder.java
``` java
package seedu.address.testutil;

import seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * A utility class to help with building PersonPrivacySettings objects.
 */
public class PersonPrivacySettingsBuilder {

    private PersonPrivacySettings pps;
    public PersonPrivacySettingsBuilder() {
        pps = new PersonPrivacySettings();
    }

    public PersonPrivacySettingsBuilder(PersonPrivacySettings pps) {
        this.pps = new PersonPrivacySettings(pps);
    }

    /**
     * Returns an {@code PersonPrivacySettings} with fields containing {@code person}'s privacy details
     */
    public PersonPrivacySettingsBuilder(ReadOnlyPerson person) {
        pps = new PersonPrivacySettings();
        pps.setNameIsPrivate(person.getName().isPrivate());
        pps.setPhoneIsPrivate(person.getPhone().isPrivate());
        pps.setEmailIsPrivate(person.getEmail().isPrivate());
        pps.setAddressIsPrivate(person.getAddress().isPrivate());
    }

    /**
     * Sets the {@code nameIsPrivate} of the {@code PersonPrivacySettings} that we are building.
     */
    public PersonPrivacySettingsBuilder setNamePrivate(String name) {
        if (name.equals("Optional[true]") || name.equals("true")) {
            pps.setNameIsPrivate(true);
        } else if (name.equals("Optional[false]") || name.equals("false")) {
            pps.setNameIsPrivate(false);
        } else {
            throw new IllegalArgumentException("Privacy of name should be true or false");
        }
        return this;
    }

    /**
     * Sets the {@code phoneIsPrivate} of the {@code PersonPrivacySettings} that we are building.
     */
    public PersonPrivacySettingsBuilder setPhonePrivate(String phone) {
        if (phone.equals("Optional[true]") || phone.equals("true")) {
            pps.setPhoneIsPrivate(true);
        } else if (phone.equals("Optional[false]") || phone.equals("false")) {
            pps.setPhoneIsPrivate(false);
        } else {
            throw new IllegalArgumentException("Privacy of phone should be true or false");
        }
        return this;
    }

    /**
     * Sets the {@code emailIsPrivate} of the {@code PersonPrivacySettings} that we are building.
     */
    public PersonPrivacySettingsBuilder setEmailPrivate(String email) {
        if (email.equals("Optional[true]") || email.equals("true")) {
            pps.setEmailIsPrivate(true);
        } else if (email.equals("Optional[false]") || email.equals("false")) {
            pps.setEmailIsPrivate(false);
        } else {
            throw new IllegalArgumentException("Privacy of email should be true or false");
        }
        return this;
    }

    /**
     * Sets the {@code addressIsPrivate} of the {@code PersonPrivacySettings} that we are building.
     */
    public PersonPrivacySettingsBuilder setAddressPrivate(String address) {
        if (address.equals("Optional[true]") || address.equals("true")) {
            pps.setAddressIsPrivate(true);
        } else if (address.equals("Optional[false]") || address.equals("false")) {
            pps.setAddressIsPrivate(false);
        } else {
            throw new IllegalArgumentException("Privacy of address should be true or false");
        }
        return this;
    }

    public PersonPrivacySettings build() {
        return pps;
    }
}
```
###### \java\seedu\address\ui\BrowserPanelTest.java
``` java
        // google maps page of a person
        postNow(panelLocateEventStub);
        URL expectedMapUrl = new URL(GOOGLE_MAPS_URL_PREFIX
                + BOB.getAddress().toString().replaceAll(" ", "+") + GOOGLE_MAPS_URL_SUFFIX);

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedMapUrl, browserPanelHandle.getLoadedUrl());
```
