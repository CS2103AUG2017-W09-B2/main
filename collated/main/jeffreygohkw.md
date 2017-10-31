# jeffreygohkw
###### \java\seedu\address\commons\events\ui\BrowserPanelLocateEvent.java
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
###### \java\seedu\address\commons\events\ui\OpenRequestEvent.java
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
###### \java\seedu\address\commons\events\ui\SaveAsRequestEvent.java
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
###### \java\seedu\address\logic\commands\ChangePrivacyCommand.java
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
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
                Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
                model.updatePerson(personToEdit, editedPerson);
```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
        } catch (IllegalArgumentException e) {
            throw new CommandException(MESSAGE_ALL_FIELDS_PRIVATE);
        }
```
###### \java\seedu\address\logic\commands\EditCommand.java
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
###### \java\seedu\address\logic\commands\LocateCommand.java
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
###### \java\seedu\address\logic\parser\AddCommandParser.java
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
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case ChangePrivacyCommand.COMMAND_WORD:
        case ChangePrivacyCommand.COMMAND_ALIAS:
            return new ChangePrivacyCommandParser().parse(arguments);
```
###### \java\seedu\address\logic\parser\AddressBookParser.java
``` java
        case LocateCommand.COMMAND_WORD:
        case LocateCommand.COMMAND_ALIAS:
            return new LocateCommandParser().parse(arguments);
```
###### \java\seedu\address\logic\parser\ChangePrivacyCommandParser.java
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
###### \java\seedu\address\logic\parser\CliSyntax.java
``` java
    public static final Prefix PREFIX_NAME_PRIVATE = new Prefix("pn/");
    public static final Prefix PREFIX_PHONE_PRIVATE = new Prefix("pp/");
    public static final Prefix PREFIX_EMAIL_PRIVATE = new Prefix("pe/");
    public static final Prefix PREFIX_ADDRESS_PRIVATE = new Prefix("pa/");
    public static final Prefix PREFIX_REMARK_PRIVATE = new Prefix("pr/");
    public static final Prefix PREFIX_TAG_PRIVATE = new Prefix("pt/");
```
###### \java\seedu\address\logic\parser\CliSyntax.java
``` java

    public static final Prefix PREFIX_TASK = new Prefix("task");
    public static final Prefix PREFIX_DEADLINE = new Prefix("by/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("d/");
    public static final Prefix PREFIX_PRIORITY = new Prefix("p/");
    public static final Prefix PREFIX_TARGET = new Prefix("to/");
    public static final Prefix PREFIX_FROM = new Prefix("from/");
}
```
###### \java\seedu\address\logic\parser\LocateCommandParser.java
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
###### \java\seedu\address\logic\parser\ParserUtil.java
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
###### \java\seedu\address\logic\parser\ParserUtil.java
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
###### \java\seedu\address\logic\parser\ParserUtil.java
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
###### \java\seedu\address\logic\parser\ParserUtil.java
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
###### \java\seedu\address\logic\parser\ParserUtil.java
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
###### \java\seedu\address\MainApp.java
``` java
        MainWindow mw = ui.getMainWindow();
        mw.setMainApp(this);
        mw.setStorage(storage);
        mw.setModel(model);
```
###### \java\seedu\address\model\person\Address.java
``` java
    private boolean isPrivate = false;
```
###### \java\seedu\address\model\person\Address.java
``` java
    public Address(String address, boolean isPrivate) throws IllegalValueException {
        this(address);
        this.setPrivate(isPrivate);
    }
```
###### \java\seedu\address\model\person\Address.java
``` java
        if (isPrivate) {
            return "<Private Address>";
        }
```
###### \java\seedu\address\model\person\Address.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \java\seedu\address\model\person\Email.java
``` java
    private boolean isPrivate = false;
```
###### \java\seedu\address\model\person\Email.java
``` java

    public Email(String email, boolean isPrivate) throws IllegalValueException {
        this(email);
        this.setPrivate(isPrivate);
    }
```
###### \java\seedu\address\model\person\Email.java
``` java
        if (isPrivate) {
            return "<Private Email>";
        }
```
###### \java\seedu\address\model\person\Email.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \java\seedu\address\model\person\Name.java
``` java
    private boolean isPrivate = false;
```
###### \java\seedu\address\model\person\Name.java
``` java
    public Name(String name, boolean isPrivate) throws IllegalValueException {
        this(name);
        this.setPrivate(isPrivate);
    }
```
###### \java\seedu\address\model\person\Name.java
``` java
        if (isPrivate) {
            return "<Private Name>";
        }
```
###### \java\seedu\address\model\person\Name.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \java\seedu\address\model\person\Phone.java
``` java
    private boolean isPrivate = false;
```
###### \java\seedu\address\model\person\Phone.java
``` java
    public Phone(String phone, boolean isPrivate) throws IllegalValueException {
        this(phone);
        this.setPrivate(isPrivate);
    }
```
###### \java\seedu\address\model\person\Phone.java
``` java
        if (isPrivate) {
            return "<Private Phone>";
        }
```
###### \java\seedu\address\model\person\Phone.java
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
###### \java\seedu\address\model\person\Phone.java
``` java
    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
```
###### \java\seedu\address\model\person\Phone.java
``` java

}
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean nameIsPrivate;
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean phoneIsPrivate;
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean emailIsPrivate;
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean addressIsPrivate;
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
    @XmlElement(required = true)
    private Boolean remarkIsPrivate;
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
``` java
        nameIsPrivate = source.getName().isPrivate();
        phoneIsPrivate = source.getPhone().isPrivate();
        emailIsPrivate = source.getEmail().isPrivate();
        addressIsPrivate = source.getAddress().isPrivate();
        remarkIsPrivate = source.getRemark().isPrivate();
```
###### \java\seedu\address\storage\XmlAdaptedPerson.java
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
###### \java\seedu\address\ui\BrowserPanel.java
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
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    @Subscribe
    private void handleBrowserPanelLocateEvent(BrowserPanelLocateEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadMapsPage(event.getNewSelection());
    }
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    private MainApp mainApp;
    private Storage storage;
    private Model model;
```
###### \java\seedu\address\ui\MainWindow.java
``` java
    @FXML
    private MenuItem openMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem exitMenuItem;
```
###### \java\seedu\address\ui\MainWindow.java
``` java
        setAccelerator(openMenuItem, KeyCombination.valueOf("CTRL+O"));
        setAccelerator(saveMenuItem, KeyCombination.valueOf("CTRL+S"));
        setAccelerator(exitMenuItem, KeyCombination.valueOf("ALT+F4"));
```
###### \java\seedu\address\ui\MainWindow.java
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
###### \java\seedu\address\ui\MainWindow.java
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
###### \resources\view\MainWindow.fxml
``` fxml
    <MenuBar fx:id="menuBar" VBox.vgrow="NEVER">
    <Menu mnemonicParsing="false" text="File">
      <MenuItem fx:id="openMenuItem" mnemonicParsing="false" onAction="#handleOpen" text="Open">
            <graphic>
               <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
                  <image>
                     <Image url="@../images/open-folder.png" />
                  </image>
               </ImageView>
            </graphic></MenuItem>
         <MenuItem fx:id="saveMenuItem" mnemonicParsing="false" onAction="#handleSaveAs" text="Save As">
            <graphic>
               <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
                  <image>
                     <Image url="@../images/floppy-disk.png" />
                  </image>
               </ImageView>
            </graphic>
         </MenuItem>
         <MenuItem fx:id="exitMenuItem" mnemonicParsing="false" onAction="#handleExit" text="Exit">
            <graphic>
               <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
                  <image>
                     <Image url="@../images/quit.png" />
                  </image>
               </ImageView>
            </graphic>
         </MenuItem>
    </Menu>
    <Menu mnemonicParsing="false" text="Help">
        <MenuItem fx:id="helpMenuItem" mnemonicParsing="false" onAction="#handleHelp" text="Help">
            <graphic>
               <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
                  <image>
                     <Image url="@../images/help_icon.png" />
                  </image>
               </ImageView>
            </graphic></MenuItem>
    </Menu>
      <Menu mnemonicParsing="false" text="Font Size">
        <items>
          <MenuItem fx:id="increaseSizeMenuItem" mnemonicParsing="false" onAction="#handleIncreaseFontSize" text="Increase +" />
            <MenuItem fx:id="decreaseSizeMenuItem" mnemonicParsing="false" onAction="#handleDecreaseFontSize" text="Decrease -" />
            <MenuItem fx:id="resetSizeMenuItem" mnemonicParsing="false" onAction="#handleResetFontSize" text="Reset" />
        </items>
      </Menu>
  </MenuBar>
```
