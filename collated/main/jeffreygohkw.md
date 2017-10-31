# jeffreygohkw
###### \src\main\java\seedu\address\commons\events\ui\BrowserPanelLocateEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Represents a selection change in the Person List Panel
 */
public class BrowserPanelLocateEvent extends BaseEvent {

    private final ReadOnlyPerson person;

    public BrowserPanelLocateEvent(ReadOnlyPerson person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ReadOnlyPerson getNewSelection() {
        return person;
    }
}
```
###### \src\main\java\seedu\address\commons\events\ui\OpenRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An Event for the opening of a save file from a selected location.
 */
public class OpenRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \src\main\java\seedu\address\commons\events\ui\SaveAsRequestEvent.java
``` java
package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

/**
 * An Event for the saving of data to a selected location.
 */
public class SaveAsRequestEvent extends BaseEvent {

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
```
###### \src\main\java\seedu\address\logic\commands\ChangePrivacyCommand.java
``` java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.Remark;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;

/**
 * Changes the privacy setting of a person's details in the address book
 */
public class ChangePrivacyCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "changeprivacy";
    public static final String COMMAND_ALIAS = "cp";

    public static final String TRUE_WORD = "true";
    public static final String FALSE_WORD = "false";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the privacy of the details of the person"
            + " identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + TRUE_WORD + " OR " + FALSE_WORD + "]"
            + "[" + PREFIX_PHONE + TRUE_WORD + " OR " + FALSE_WORD + "]"
            + "[" + PREFIX_EMAIL + TRUE_WORD + " OR " + FALSE_WORD + "]"
            + "[" + PREFIX_ADDRESS + TRUE_WORD + " OR " + FALSE_WORD + "]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + TRUE_WORD + " "
            + PREFIX_PHONE + FALSE_WORD + " "
            + PREFIX_EMAIL + TRUE_WORD + " "
            + PREFIX_ADDRESS + FALSE_WORD;

    public static final String MESSAGE_CHANGE_PRIVACY_SUCCESS = "Changed the Privacy of the Person: %1$s";
    public static final String MESSAGE_NO_FIELDS = "At least one field to change must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final PersonPrivacySettings pps;

    /**
     * @param index of the person in the filtered person list to change the privacy of
     */
    public ChangePrivacyCommand(Index index, PersonPrivacySettings pps) {
        requireNonNull(index);
        requireNonNull(pps);

        this.index = index;
        this.pps = pps;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToChange = lastShownList.get(index.getZeroBased());

        Person newPerson = createPersonWithChangedPrivacy(personToChange, pps);

        try {
            model.updatePerson(personToChange, newPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_CHANGE_PRIVACY_SUCCESS, newPerson));
    }

    /**
     * Changes a person's fields' privacy
     * @param person the person whose privacy we would like to change
     * @param pps the settings of privacy for each field
     */
    private static Person createPersonWithChangedPrivacy(ReadOnlyPerson person, PersonPrivacySettings pps) {
        assert person != null;

        Name n = person.getName();
        Phone p = person.getPhone();
        Email e = person.getEmail();
        Address a = person.getAddress();
        Remark r = person.getRemark();
        Boolean f = person.getFavourite();
        Set<Tag> t = person.getTags();

        if (pps.getNameIsPrivate() != null) {
            n.setPrivate(pps.getNameIsPrivate());
        }
        if (pps.getPhoneIsPrivate() != null) {
            p.setPrivate(pps.getPhoneIsPrivate());
        }

        if (pps.getEmailIsPrivate() != null) {
            e.setPrivate(pps.getEmailIsPrivate());
        }

        if (pps.getAddressIsPrivate() != null) {
            a.setPrivate(pps.getAddressIsPrivate());
        }

        if (pps.getRemarkIsPrivate() != null) {
            r.setPrivate(pps.getRemarkIsPrivate());
        }

        return new Person(n, p, e, a, f, r, t);
    }

    public Index getIndex() {
        return index;
    }

    public PersonPrivacySettings getPps() {
        return pps;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ChangePrivacyCommand)) {
            return false;
        }

        // state check
        ChangePrivacyCommand c = (ChangePrivacyCommand) other;
        return index.equals(c.index)
                && pps.equals(c.pps);
    }

    /**
     * Stores the privacy settings for each field of a person.
     */
    public static class PersonPrivacySettings {
        private Boolean nameIsPrivate;
        private Boolean phoneIsPrivate;
        private Boolean emailIsPrivate;
        private Boolean addressIsPrivate;
        private Boolean remarkIsPrivate;

        public PersonPrivacySettings() {}

        public PersonPrivacySettings(PersonPrivacySettings toCopy) {
            this.nameIsPrivate = toCopy.nameIsPrivate;
            this.phoneIsPrivate = toCopy.phoneIsPrivate;
            this.emailIsPrivate = toCopy.emailIsPrivate;
            this.addressIsPrivate = toCopy.addressIsPrivate;
            this.remarkIsPrivate = toCopy.remarkIsPrivate;
        }

        /**
         * Returns true if at least one field is not null.
         */
        public boolean isAnyFieldNonNull() {
            return CollectionUtil.isAnyNonNull(this.nameIsPrivate, this.phoneIsPrivate,
                    this.emailIsPrivate, this.addressIsPrivate, this.remarkIsPrivate);
        }

        /**
         * Returns the value of nameIsPrivate
         * @return the value of nameIsPrivate
         */
        public Boolean getNameIsPrivate() {
            return nameIsPrivate;
        }

        public void setNameIsPrivate(boolean nameIsPrivate) {
            requireNonNull(nameIsPrivate);
            this.nameIsPrivate = nameIsPrivate;
        }

        /**
         * Returns the value of phoneIsPrivate
         * @return the value of phoneIsPrivate
         */
        public Boolean getPhoneIsPrivate() {
            return phoneIsPrivate;
        }

        public void setPhoneIsPrivate(boolean phoneIsPrivate) {
            requireNonNull(phoneIsPrivate);
            this.phoneIsPrivate = phoneIsPrivate;
        }

        /**
         * Returns the value of emailIsPrivate
         * @return the value of emailIsPrivate
         */
        public Boolean getEmailIsPrivate() {
            return emailIsPrivate;
        }

        public void setEmailIsPrivate(boolean emailIsPrivate) {
            requireNonNull(emailIsPrivate);
            this.emailIsPrivate = emailIsPrivate;
        }

        /**
         * Returns the value of addressIsPrivate
         * @return the value of addressIsPrivate
         */
        public Boolean getAddressIsPrivate() {
            return addressIsPrivate;
        }

        public void setAddressIsPrivate(boolean addressIsPrivate) {
            requireNonNull(addressIsPrivate);
            this.addressIsPrivate = addressIsPrivate;
        }

        /**
         * Returns the value of remarkIsPrivate
         * @return the value of remarkIsPrivate
         */
        public Boolean getRemarkIsPrivate() {
            return remarkIsPrivate;
        }

        public void setRemarkIsPrivate(boolean remarkIsPrivate) {
            requireNonNull(remarkIsPrivate);
            this.remarkIsPrivate = remarkIsPrivate;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof PersonPrivacySettings)) {
                return false;
            }

            // state check
            PersonPrivacySettings c = (PersonPrivacySettings) other;

            return getNameIsPrivate().equals(c.getNameIsPrivate())
                    && getPhoneIsPrivate().equals(c.getPhoneIsPrivate())
                    && getEmailIsPrivate().equals(c.getEmailIsPrivate())
                    && getAddressIsPrivate().equals(c.getAddressIsPrivate())
                    && getRemarkIsPrivate().equals(c.getRemarkIsPrivate());
        }
    }
}
```
###### \src\main\java\seedu\address\logic\commands\EditCommand.java
``` java
        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson;
        try {
            editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        } catch (IllegalArgumentException e) {
            throw new CommandException(MESSAGE_ALL_FIELDS_PRIVATE);
        }
```
###### \src\main\java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     * A person with private fields cannot be edited
     */
    private static Person createEditedPerson(ReadOnlyPerson personToEdit,
                                             EditPersonDescriptor editPersonDescriptor)
            throws IllegalArgumentException {
        assert personToEdit != null;

        Name updatedName;
        Phone updatedPhone;
        Email updatedEmail;
        Address updatedAddress;
        Remark updatedRemark;
        Set<Tag> updatedTags;
        Boolean updateFavourite;

        areFieldsAllPrivate = true;
        updatedName = createUpdatedName(personToEdit, editPersonDescriptor);

        updatedPhone = createUpdatedPhone(personToEdit, editPersonDescriptor);

        updatedEmail = createUpdatedEmail(personToEdit, editPersonDescriptor);

        updatedAddress = createUpdatedAddress(personToEdit, editPersonDescriptor);

        updatedRemark = createUpdatedRemark(personToEdit, editPersonDescriptor);

        updatedTags = createUpdatedTags(personToEdit, editPersonDescriptor);

        updateFavourite = createUpdatedFavourite(personToEdit, editPersonDescriptor);

        if (areFieldsAllPrivate) {
            throw new IllegalArgumentException();
        }
        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
                          updateFavourite, updatedRemark, updatedTags);
    }

    /**
     * Creates an updated (@code Name) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Name) from either the personToEdit or the editPersonDescriptor depending on privacy
     */
    private static Name createUpdatedName(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Name updatedName;
        if (!personToEdit.getName().isPrivate()) {
            updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
            if (editPersonDescriptor.getName().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedName = personToEdit.getName();
        }
        return updatedName;
    }

    /**
     * Creates an updated (@code Phone) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Phone) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Phone createUpdatedPhone(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Phone updatedPhone;
        if (!personToEdit.getPhone().isPrivate()) {
            updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
            if (editPersonDescriptor.getPhone().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedPhone = personToEdit.getPhone();
        }
        return updatedPhone;
    }

    /**
     * Creates an updated (@code Email) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Email) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Email createUpdatedEmail(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Email updatedEmail;
        if (!personToEdit.getEmail().isPrivate()) {
            updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
            if (editPersonDescriptor.getEmail().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedEmail = personToEdit.getEmail();
        }
        return updatedEmail;
    }

    /**
     * Creates an updated (@code Address) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Address) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Address createUpdatedAddress(ReadOnlyPerson personToEdit,
                                                EditPersonDescriptor editPersonDescriptor) {
        Address updatedAddress;
        if (!personToEdit.getAddress().isPrivate()) {
            updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
            if (editPersonDescriptor.getAddress().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedAddress = personToEdit.getAddress();
        }
        return updatedAddress;
    }

    /**
     * Creates an updated (@code Remark) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Remark) from either the personToEdit or the editPersonDescriptor
     * depending on privacy and the input
     */
    private static Remark createUpdatedRemark(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Remark updatedRemark;
        if (!personToEdit.getRemark().isPrivate()) {
            updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
            if (editPersonDescriptor.getRemark().isPresent()) {
                areFieldsAllPrivate = false;
            }
        } else {
            updatedRemark = personToEdit.getRemark();
        }
        return updatedRemark;
    }

    /**
     * Creates an updated (@code Tag) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Tag) from either the personToEdit or the editPersonDescriptor depending on the input
     */
    private static Set<Tag> createUpdatedTags(ReadOnlyPerson personToEdit, EditPersonDescriptor editPersonDescriptor) {
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        if (editPersonDescriptor.getTags().isPresent()) {
            areFieldsAllPrivate = false;
        }
        return updatedTags;
    }

    /**
     * Creates an updated (@code Favourite) for use in createEditedPerson
     * @param personToEdit The person to edit
     * @param editPersonDescriptor Edited with this editPersonDescriptor
     * @return A new (@code Favourite) from either the personToEdit or the editPersonDescriptor depending on the input
     */
    private static Boolean createUpdatedFavourite(ReadOnlyPerson personToEdit,
                                                  EditPersonDescriptor editPersonDescriptor) {
        Boolean updateFavourite = editPersonDescriptor.getFavourite().orElse(personToEdit.getFavourite());
        if (editPersonDescriptor.getFavourite().isPresent()) {
            areFieldsAllPrivate = false;
        }
        return updateFavourite;
    }
```
###### \src\main\java\seedu\address\logic\commands\LocateCommand.java
``` java
package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.events.ui.BrowserPanelLocateEvent;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;

/**
 * Locates a person's address on Google Maps identified using it's last displayed index from the address book.
 */
public class LocateCommand extends Command {

    public static final String COMMAND_WORD = "locate";
    public static final String COMMAND_ALIAS = "loc";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Locates the address of the person identified by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_LOCATE_PERSON_SUCCESS = "Searching for Person: %1$s";

    private final Index targetIndex;

    public LocateCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new BrowserPanelLocateEvent(
                model.getFilteredPersonList().get(targetIndex.getZeroBased())));
        return new CommandResult(String.format(MESSAGE_LOCATE_PERSON_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof LocateCommand // instanceof handles nulls
                && this.targetIndex.equals(((LocateCommand) other).targetIndex)); // state check
    }
}
```
###### \src\main\java\seedu\address\logic\parser\AddCommandParser.java
``` java
    /**
     * Constructs a ReadOnlyPerson from the arguments provided.
     */
    private static ReadOnlyPerson constructPerson(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_REMARK,
                        PREFIX_TAG, PREFIX_NAME_PRIVATE, PREFIX_PHONE_PRIVATE, PREFIX_EMAIL_PRIVATE,
                        PREFIX_ADDRESS_PRIVATE, PREFIX_REMARK_PRIVATE, PREFIX_TAG_PRIVATE);

        if (!(arePrefixesPresent(argMultimap, PREFIX_NAME)
                || (arePrefixesPresent(argMultimap, PREFIX_NAME_PRIVATE)))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_PHONE)
                || (arePrefixesPresent(argMultimap, PREFIX_PHONE_PRIVATE)))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_EMAIL)
                || (arePrefixesPresent(argMultimap, PREFIX_EMAIL_PRIVATE)))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_ADDRESS)
                || (arePrefixesPresent(argMultimap, PREFIX_ADDRESS_PRIVATE)))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_REMARK)
                || (arePrefixesPresent(argMultimap, PREFIX_REMARK_PRIVATE)))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        try {
            Name name;
            Phone phone;
            Email email;
            Address address;
            Remark remark;

            if ((arePrefixesPresent(argMultimap, PREFIX_NAME))) {
                name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get();
            } else {
                name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME_PRIVATE), true).get();
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_PHONE))) {
                phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE)).get();
            } else {
                phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE_PRIVATE), true).get();
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_EMAIL))) {
                email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL)).get();
            } else {
                email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL_PRIVATE), true).get();
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_ADDRESS))) {
                address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS)).get();
            } else {
                address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS_PRIVATE), true).get();
            }

            if ((arePrefixesPresent(argMultimap, PREFIX_REMARK))) {
                remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).get();
            } else {
                remark = ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK_PRIVATE), true).get();
            }
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            ReadOnlyPerson person = new Person(name, phone, email, address, false, remark, tagList);
            return person;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
```
###### \src\main\java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case ChangePrivacyCommand.COMMAND_WORD:
        case ChangePrivacyCommand.COMMAND_ALIAS:
            return new ChangePrivacyCommandParser().parse(arguments);
```
###### \src\main\java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case LocateCommand.COMMAND_WORD:
        case LocateCommand.COMMAND_ALIAS:
            return new LocateCommandParser().parse(arguments);
```
###### \src\main\java\seedu\address\logic\parser\ChangePrivacyCommandParser.java
``` java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ChangePrivacyCommand;
import seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ChangePrivacyCommand object
 */
public class ChangePrivacyCommandParser implements Parser<ChangePrivacyCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ChangePrivacyCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_REMARK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangePrivacyCommand.MESSAGE_USAGE));
        }

        PersonPrivacySettings pps = new PersonPrivacySettings();

        checkName(argMultimap, pps);
        checkPhone(argMultimap, pps);
        checkEmail(argMultimap, pps);
        checkAddress(argMultimap, pps);
        checkRemark(argMultimap, pps);

        if (!pps.isAnyFieldNonNull()) {
            throw new ParseException(ChangePrivacyCommand.MESSAGE_NO_FIELDS);
        }

        return new ChangePrivacyCommand(index, pps);
    }

    /**
     * Checks the input under the name prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkName(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            if (argMultimap.getValue(PREFIX_NAME).toString().equals("Optional[true]")) {
                pps.setNameIsPrivate(true);

            } else if (argMultimap.getValue(PREFIX_NAME).toString().equals("Optional[false]")) {
                pps.setNameIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the phone prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkPhone(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            if (argMultimap.getValue(PREFIX_PHONE).toString().equals("Optional[true]")) {
                pps.setPhoneIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_PHONE).toString().equals("Optional[false]")) {
                pps.setPhoneIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the email prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkEmail(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            if (argMultimap.getValue(PREFIX_EMAIL).toString().equals("Optional[true]")) {
                pps.setEmailIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_EMAIL).toString().equals("Optional[false]")) {
                pps.setEmailIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the address prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkAddress(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            if (argMultimap.getValue(PREFIX_ADDRESS).toString().equals("Optional[true]")) {
                pps.setAddressIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_ADDRESS).toString().equals("Optional[false]")) {
                pps.setAddressIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }

    /**
     * Checks the input under the address prefix and sets the PersonPrivacySettings depending on the input
     * @param argMultimap The input arguments of the Command
     * @param pps The PersonPrivacySettings to modify
     * @throws ParseException if the input is neither true nor false
     */
    private void checkRemark(ArgumentMultimap argMultimap, PersonPrivacySettings pps) throws ParseException {
        if (argMultimap.getValue(PREFIX_REMARK).isPresent()) {
            if (argMultimap.getValue(PREFIX_REMARK).toString().equals("Optional[true]")) {
                pps.setRemarkIsPrivate(true);
            } else if (argMultimap.getValue(PREFIX_REMARK).toString().equals("Optional[false]")) {
                pps.setRemarkIsPrivate(false);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        ChangePrivacyCommand.MESSAGE_USAGE));
            }
        }
    }
}
```
###### \src\main\java\seedu\address\logic\parser\CliSyntax.java
``` java
    public static final Prefix PREFIX_NAME_PRIVATE = new Prefix("pn/");
    public static final Prefix PREFIX_PHONE_PRIVATE = new Prefix("pp/");
    public static final Prefix PREFIX_EMAIL_PRIVATE = new Prefix("pe/");
    public static final Prefix PREFIX_ADDRESS_PRIVATE = new Prefix("pa/");
    public static final Prefix PREFIX_REMARK_PRIVATE = new Prefix("pr/");
    public static final Prefix PREFIX_TAG_PRIVATE = new Prefix("pt/");
```
###### \src\main\java\seedu\address\logic\parser\CliSyntax.java
``` java

    public static final Prefix PREFIX_TASK = new Prefix("task");
    public static final Prefix PREFIX_DEADLINE = new Prefix("by/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_PRIORITY = new Prefix("p/");
}
```
###### \src\main\java\seedu\address\logic\parser\LocateCommandParser.java
``` java
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.LocateCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SelectCommand object
 */
public class LocateCommandParser implements Parser<LocateCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectCommand
     * and returns an SelectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public LocateCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new LocateCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, LocateCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \src\main\java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> name} into an {@code Optional<Name>} if {@code name} is present.
     * Takes in a (@code boolean isPrivate) which will set the Name to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Name> parseName(Optional<String> name, boolean isPrivate) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new Name(name.get(), isPrivate)) : Optional.empty();
    }
```
###### \src\main\java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> phone} into an {@code Optional<Phone>} if {@code phone} is present.
     * Takes in a (@code boolean isPrivate) which will set the Phone to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Phone> parsePhone(Optional<String> phone, boolean isPrivate) throws IllegalValueException {
        requireNonNull(phone);
        return phone.isPresent() ? Optional.of(new Phone(phone.get(), isPrivate)) : Optional.empty();
    }
```
###### \src\main\java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> address} into an {@code Optional<Address>} if {@code address} is present.
     * Takes in a (@code boolean isPrivate) which will set the Address to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Address> parseAddress(Optional<String> address, boolean isPrivate)
            throws IllegalValueException {
        requireNonNull(address);
        return address.isPresent() ? Optional.of(new Address(address.get(), isPrivate)) : Optional.empty();
    }
```
###### \src\main\java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> remark} into an {@code Optional<Remark>} if {@code remark} is present.
     * Takes in a (@code boolean isPrivate) which will set the Remark to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Remark> parseRemark(Optional<String> remark, boolean isPrivate)
            throws IllegalValueException {
        requireNonNull(remark);
        return remark.isPresent() ? Optional.of(new Remark(remark.get(), isPrivate)) : Optional.empty();
    }
```
###### \src\main\java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code Optional<String> email} into an {@code Optional<Email>} if {@code email} is present.
     * Takes in a (@code boolean isPrivate) which will set the Email to be private if true.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Email> parseEmail(Optional<String> email, boolean isPrivate) throws IllegalValueException {
        requireNonNull(email);
        return email.isPresent() ? Optional.of(new Email(email.get(), isPrivate)) : Optional.empty();
    }
```
###### \src\main\java\seedu\address\MainApp.java
``` java
        MainWindow mw = ui.getMainWindow();
        mw.setMainApp(this);
        mw.setStorage(storage);
        mw.setModel(model);
```
###### \src\main\java\seedu\address\model\person\Address.java
``` java
    private boolean isPrivate = false;
```
###### \src\main\java\seedu\address\model\person\Address.java
``` java
    public Address(String address, boolean isPrivate) throws IllegalValueException {
        this(address);
        this.setPrivate(isPrivate);
    }
```
###### \src\main\java\seedu\address\model\person\Address.java
``` java
        if (isPrivate) {
            return "<Private Address>";
        }
```
###### \src\main\java\seedu\address\model\person\Address.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \src\main\java\seedu\address\model\person\Email.java
``` java
    private boolean isPrivate = false;
```
###### \src\main\java\seedu\address\model\person\Email.java
``` java

    public Email(String email, boolean isPrivate) throws IllegalValueException {
        this(email);
        this.setPrivate(isPrivate);
    }
```
###### \src\main\java\seedu\address\model\person\Email.java
``` java
        if (isPrivate) {
            return "<Private Email>";
        }
```
###### \src\main\java\seedu\address\model\person\Email.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \src\main\java\seedu\address\model\person\Name.java
``` java
    private boolean isPrivate = false;
```
###### \src\main\java\seedu\address\model\person\Name.java
``` java
    public Name(String name, boolean isPrivate) throws IllegalValueException {
        this(name);
        this.setPrivate(isPrivate);
    }
```
###### \src\main\java\seedu\address\model\person\Name.java
``` java
        if (isPrivate) {
            return "<Private Name>";
        }
```
###### \src\main\java\seedu\address\model\person\Name.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \src\main\java\seedu\address\model\person\Phone.java
``` java
    private boolean isPrivate = false;
```
###### \src\main\java\seedu\address\model\person\Phone.java
``` java
    public Phone(String phone, boolean isPrivate) throws IllegalValueException {
        this(phone);
        this.setPrivate(isPrivate);
    }
```
###### \src\main\java\seedu\address\model\person\Phone.java
``` java
        if (isPrivate) {
            return "<Private Phone>";
        }
```
###### \src\main\java\seedu\address\model\person\Phone.java
``` java
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Phone // instanceof handles nulls
                && this.value.equals(((Phone) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
```
###### \src\main\java\seedu\address\model\person\Phone.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \src\main\java\seedu\address\model\person\Phone.java
``` java

}
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean nameIsPrivate;
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean phoneIsPrivate;
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean emailIsPrivate;
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean addressIsPrivate;
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean remarkIsPrivate;
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
        nameIsPrivate = source.getName().isPrivate();
        phoneIsPrivate = source.getPhone().isPrivate();
        emailIsPrivate = source.getEmail().isPrivate();
        addressIsPrivate = source.getAddress().isPrivate();
        remarkIsPrivate = source.getRemark().isPrivate();
```
###### \src\main\java\seedu\address\storage\XmlAdaptedPerson.java
``` java
        if (nameIsPrivate == null) {
            nameIsPrivate = false;
        }
        if (phoneIsPrivate == null) {
            phoneIsPrivate = false;
        }
        if (emailIsPrivate == null) {
            emailIsPrivate = false;
        }
        if (addressIsPrivate == null) {
            addressIsPrivate = false;
        }
        if (remarkIsPrivate == null) {
            remarkIsPrivate = false;
        }
        final Name name = new Name(this.name, this.nameIsPrivate);
        final Phone phone = new Phone(this.phone, this.phoneIsPrivate);
        final Email email = new Email(this.email, this.emailIsPrivate);
        final Address address = new Address(this.address, this.addressIsPrivate);
        final Boolean favourite = new Boolean(this.favourite);
        final Remark remark = new Remark(this.remark, this.remarkIsPrivate);
```
###### \src\main\java\seedu\address\ui\BrowserPanel.java
``` java
    /**
     * Loads a google search for a person'saddress if their address is not private
     * Prints out a message on the result display otherwise
     * @param person The person's address we want to search for
     */
    private void loadMapsPage(ReadOnlyPerson person) {
        if (person.getAddress().isPrivate()) {
            raise(new NewResultAvailableEvent(PRIVATE_ADDRESS_CANNOT_SEARCH));
        } else {
            loadPage(GOOGLE_MAPS_URL_PREFIX + person.getAddress().toString().replaceAll(" ", "+")
                + GOOGLE_MAPS_URL_SUFFIX);
        }
    }
```
###### \src\main\java\seedu\address\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handleBrowserPanelLocateEvent(BrowserPanelLocateEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadMapsPage(event.getNewSelection());
    }
```
###### \src\main\java\seedu\address\ui\MainWindow.java
``` java
    private MainApp mainApp;
    private Storage storage;
    private Model model;
```
###### \src\main\java\seedu\address\ui\MainWindow.java
``` java
    @FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem exitMenuItem;
```
###### \src\main\java\seedu\address\ui\MainWindow.java
``` java
        setAccelerator(openMenuItem, KeyCombination.valueOf("CTRL+O"));
        setAccelerator(saveMenuItem, KeyCombination.valueOf("CTRL+S"));
        setAccelerator(exitMenuItem, KeyCombination.valueOf("ALT+F4"));
```
###### \src\main\java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp the MainApp itself
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Is called by the main application to provide MainWindow with Storage
     *
     * @param s the Storage used by MainApp
     */
    public void setStorage(Storage s) {
        this.storage = s;
    }

    /**
     * Is called by the main application to  provide MainWindow with Model
     *
     * @param m the Model used by MainApp
     */
    public void setModel(Model m) {
        this.model = m;
    }
```
###### \src\main\java\seedu\address\ui\MainWindow.java
``` java
    /**
     * Opens the data from a desired location
     */
    @FXML
    private void handleOpen() throws IOException, DataConversionException {
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            // Change file path to the opened file
            storage.changeFilePath(file.getPath(), prefs);
            // Reset data in the model to the data from the opened file
            model.resetData(XmlFileStorage.loadDataFromSaveFile(file));
            // Update the UI
            fillInnerParts();
        }
        raise(new OpenRequestEvent());
    }

    /**
     * Saves the data at a desired location
     */
    @FXML
    private void handleSaveAs() throws IOException {
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            // Change file path to the new save file
            storage.changeFilePath(file.getPath(), prefs);
            // Save the address book data and the user preferences
            storage.saveAddressBook(model.getAddressBook());
            storage.saveUserPrefs(prefs);
            // Update the UI
            fillInnerParts();
        }
        raise(new SaveAsRequestEvent());
    }
```
###### \src\test\java\seedu\address\logic\commands\ChangePrivacyCommandTest.java
``` java
package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static seedu.address.logic.commands.ChangePrivacyCommand.PersonPrivacySettings;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

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
###### \src\test\java\seedu\address\logic\commands\EditCommandTest.java
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
###### \src\test\java\seedu\address\logic\commands\LocateCommandTest.java
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
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
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
###### \src\test\java\seedu\address\logic\parser\AddressBookParserTest.java
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
###### \src\test\java\seedu\address\logic\parser\AddressBookParserTest.java
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
###### \src\test\java\seedu\address\logic\parser\AddressBookParserTest.java
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
###### \src\test\java\seedu\address\logic\parser\ChangePrivacyCommandParserTest.java
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
###### \src\test\java\seedu\address\logic\parser\LocateCommandParserTest.java
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
###### \src\test\java\seedu\address\logic\parser\SortCommandParserTest.java
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
###### \src\test\java\seedu\address\model\person\AddressTest.java
``` java
    @Test
    public void privateAddressIsHidden_success() throws IllegalValueException {
        Address a = new Address("Any Address", true);
        assertTrue(a.isPrivate());
        assertEquals(a.toString(), "<Private Address>");
    }
```
###### \src\test\java\seedu\address\model\person\EmailTest.java
``` java
    @Test
    public void privateEmailIsHidden_success() throws IllegalValueException {
        Email e = new Email("AnyEmail@example.com", true);
        assertTrue(e.isPrivate());
        assertEquals(e.toString(), "<Private Email>");
    }
```
###### \src\test\java\seedu\address\model\person\NameTest.java
``` java
    @Test
    public void privateNameIsHidden_success() throws IllegalValueException {
        Name n = new Name("Any Name", true);
        assertTrue(n.isPrivate());
        assertEquals(n.toString(), "<Private Name>");
    }
```
###### \src\test\java\seedu\address\model\person\PhoneTest.java
``` java
    @Test
    public void privatePhoneIsHidden_success() throws IllegalValueException {
        Phone p = new Phone("999", true);
        assertTrue(p.isPrivate());
        assertEquals(p.toString(), "<Private Phone>");
    }
```
###### \src\test\java\seedu\address\testutil\PersonPrivacySettingsBuilder.java
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
###### \src\test\java\seedu\address\ui\BrowserPanelTest.java
``` java
        // google maps page of a person
        postNow(panelLocateEventStub);
        URL expectedMapUrl = new URL(GOOGLE_MAPS_URL_PREFIX
                + BOB.getAddress().toString().replaceAll(" ", "+") + GOOGLE_MAPS_URL_SUFFIX);

        waitUntilBrowserLoaded(browserPanelHandle);
        assertEquals(expectedMapUrl, browserPanelHandle.getLoadedUrl());
```
