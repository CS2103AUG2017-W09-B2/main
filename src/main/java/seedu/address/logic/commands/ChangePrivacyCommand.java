//@@author jeffreygohkw
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
