# Esilocke
###### \java\seedu\address\logic\commands\AssignCommand.java
``` java

import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/** Assigns at least 1 person to a specified task in the Address Book**/
public class AssignCommand extends Command {
    public static final String COMMAND_WORD = "assign";
    public static final String COMMAND_ALIAS = "as";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns people to a task in the Address Book. "
            + "Parameters: "
            + "PERSON INDEXES... "
            + PREFIX_TARGET + "TASK ";

    public static final String MESSAGE_SUCCESS = "Assigned %1$s people to \n%2$s";
    public static final String MESSAGE_INVALID_TARGET_ARGS = "Only 1 task index should be specified";
    public static final String MESSAGE_INVALID_PERSONS_ARGS = "At least 1 person index must be specified";
    public static final String MESSAGE_NONE_ASSIGNED = "All the specified persons are already assigned to this task";

    private ArrayList<Index> personIndexes;
    private Index taskIndex;

    public AssignCommand(ArrayList<Index> personIndexes, Index taskIndex) {
        assert(personIndexes.size() > 0);
        this.personIndexes = personIndexes;
        this.taskIndex = taskIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> tasksList = model.getFilteredTaskList();
        ArrayList<ReadOnlyPerson> personIndexes = createPersonsToAssign(this.personIndexes);

        if (taskIndex.getZeroBased() >= tasksList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask assignedTask = tasksList.get(taskIndex.getZeroBased());
        try {
            model.assignToTask(personIndexes, assignedTask);
        } catch (TaskNotFoundException tnfe) {
            throw new AssertionError("The specified task cannot be missing");
        } catch (DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_NONE_ASSIGNED);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, personIndexes.size(), assignedTask));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AssignCommand // instanceof handles nulls
                && this.personIndexes.equals(((AssignCommand) other).personIndexes)
                && this.taskIndex.equals(((AssignCommand) other).taskIndex)); // state check
    }

    /**
     * Creates a {@code ArrayList} that contains all the {@code ReadOnlyPerson} converted from the {@Code Index}
     * @throws CommandException if the specified Index is out of range
     */
    public ArrayList<ReadOnlyPerson> createPersonsToAssign (ArrayList<Index> indexes)  throws CommandException {
        HashSet<ReadOnlyPerson> addedPersons = new HashSet<>();
        ArrayList<ReadOnlyPerson> personsToAssign = new ArrayList<>();
        List<ReadOnlyPerson> personsList = model.getFilteredPersonList();
        try {
            for (Index i : personIndexes) {
                ReadOnlyPerson toAssign = personsList.get(i.getZeroBased());
                if (!addedPersons.contains(toAssign)) {
                    addedPersons.add(toAssign);
                    personsToAssign.add(toAssign);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return personsToAssign;
    }
}
```
###### \java\seedu\address\logic\commands\ClearCommand.java
``` java
    public ClearCommand() {
        isClearAll = true;
        isClearPerson = false;
        isClearTask = false;
        type = null;
        cleared = TYPE_ALL;
    }
```
###### \java\seedu\address\logic\commands\ClearCommand.java
``` java
    public ClearCommand(Prefix type) {
        if (type.equals(PREFIX_TASK)) {
            isClearTask = true;
            isClearPerson = false;
            isClearAll = false;
            this.type = PREFIX_TASK;
            cleared = TYPE_TASKS;
        } else if (type.equals(PREFIX_PERSON)) {
            isClearPerson = true;
            isClearTask = false;
            isClearAll = false;
            this.type = PREFIX_PERSON;
            cleared = TYPE_PERSONS;
        } else {
            throw new AssertionError("An invalid type was provided!");
        }
    }

    @Override
```
###### \java\seedu\address\logic\commands\ClearCommand.java
``` java
    public CommandResult executeUndoableCommand() {
        requireNonNull(model);
        if (isClearAll) {
            model.resetData(new AddressBook());
        } else if (isClearTask) {
            model.resetPartialData(new AddressBook(), type);
        } else if (isClearPerson) {
            model.resetPartialData(new AddressBook(), type);
        } else {
            assert false : "At least one boolean must be true.";
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, cleared));
    }
}
```
###### \java\seedu\address\logic\commands\DismissCommand.java
``` java

import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.task.ReadOnlyTask;
import seedu.address.model.task.exceptions.DuplicateTaskException;
import seedu.address.model.task.exceptions.TaskNotFoundException;

/** Dismisses at least 1 person from a specified task in the Address Book**/
public class DismissCommand extends Command {
    public static final String COMMAND_WORD = "dismiss";
    public static final String COMMAND_ALIAS = "ds";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Dismisses people from a task in the Address Book. "
            + "Parameters: "
            + "PERSON INDEXES... "
            + PREFIX_TARGET + "TASK ";

    public static final String MESSAGE_SUCCESS = "Dismissed %1$s people from task \n%2$s";
    public static final String MESSAGE_INVALID_TARGET_ARGS = "Only 1 task index should be specified";
    public static final String MESSAGE_INVALID_PERSONS_ARGS = "At least 1 person index must be specified";
    public static final String MESSAGE_NONE_ASSIGNED = "None of the specified persons are assigned to this task";

    private ArrayList<Index> personIndexes;
    private Index taskIndex;

    public DismissCommand(ArrayList<Index> personIndexes, Index taskIndex) {
        assert(personIndexes.size() > 0);
        this.personIndexes = personIndexes;
        this.taskIndex = taskIndex;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> tasksList = model.getFilteredTaskList();
        ArrayList<ReadOnlyPerson> personIndexes = createPersonsToDismiss(this.personIndexes);

        if (taskIndex.getZeroBased() >= tasksList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        ReadOnlyTask dismissedTask = tasksList.get(taskIndex.getZeroBased());
        try {
            model.dismissFromTask(personIndexes, dismissedTask);
        } catch (TaskNotFoundException tnfe) {
            throw new AssertionError("The specified task cannot be missing");
        } catch (DuplicateTaskException dte) {
            throw new CommandException(MESSAGE_NONE_ASSIGNED);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, personIndexes.size(), dismissedTask));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DismissCommand // instanceof handles nulls
                && this.personIndexes.equals(((DismissCommand) other).personIndexes)
                && this.taskIndex.equals(((DismissCommand) other).taskIndex)); // state check
    }

    /**
     * Creates a {@code ArrayList} that contains all the {@code ReadOnlyPerson} converted from the {@Code Index}
     * @throws CommandException if the specified Index is out of range
     */
    public ArrayList<ReadOnlyPerson> createPersonsToDismiss (ArrayList<Index> indexes)  throws CommandException {
        HashSet<ReadOnlyPerson> addedPersons = new HashSet<>();
        ArrayList<ReadOnlyPerson> personsToDismiss = new ArrayList<>();
        List<ReadOnlyPerson> personsList = model.getFilteredPersonList();
        try {
            for (Index i : personIndexes) {
                ReadOnlyPerson toDismiss = personsList.get(i.getZeroBased());
                if (!addedPersons.contains(toDismiss)) {
                    addedPersons.add(toDismiss);
                    personsToDismiss.add(toDismiss);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        return personsToDismiss;
    }
}
```
###### \java\seedu\address\logic\commands\EditCommand.java
``` java
    /**
     * Stores the details to edit the task with. Each non-empty field value will replace the
     * corresponding field value of the task.
     */
    public static class EditTaskDescriptor {
        private TaskName taskName;
        private Description description;
        private Deadline deadline;
        private Priority priority;
        private Assignees assignees;

        public EditTaskDescriptor() {}

        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            this.taskName = toCopy.taskName;
            this.description = toCopy.description;
            this.deadline = toCopy.deadline;
            this.priority = toCopy.priority;
            this.assignees = toCopy.assignees;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.taskName, this.description, this.deadline, this.priority);
        }

        public void setTaskName(TaskName taskName) {
            this.taskName = taskName;
        }

        public Optional<TaskName> getTaskName() {
            return Optional.ofNullable(taskName);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDeadline(Deadline deadline) {
            this.deadline = deadline;
        }

        public Optional<Deadline> getDeadline()  {
            return Optional.ofNullable(deadline);
        }

        public void setPriority(Priority priority) {
            this.priority = priority;
        }

        public Optional<Priority> getPriority() {
            return Optional.ofNullable(priority);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditTaskDescriptor)) {
                return false;
            }

            // state check
            EditTaskDescriptor e = (EditTaskDescriptor) other;

            return getTaskName().equals(e.getTaskName())
                    && getDescription().equals(e.getDescription())
                    && getDeadline().equals(e.getDeadline())
                    && getPriority().equals(e.getPriority());
        }
    }
}
```
###### \java\seedu\address\logic\commands\EditTagCommand.java
``` java
public class EditTagCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "edittag";
    public static final String COMMAND_ALIAS = "etag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the specified tag "
            + "and updates all existing contacts that shares this tag with the new value.\n"
            + "Parameters: TAGTOCHANGE (must be alphanumerical) "
            + "TAGNEWNAME (must be alphanumerical)\n"
            + "Example: " + COMMAND_WORD + " friends enemies";

    public static final String MESSAGE_EDIT_TAG_SUCCESS = "Replaced tag %1$s with %2$s";
    public static final String MESSAGE_TAG_NOT_FOUND = "No such tag was found in the address book.";
    public static final String MESSAGE_INSUFFICIENT_ARGS = "Only 2 arguments should be provided!";
    public static final String MESSAGE_INVALID_TAG_NAME = "Tag names must be alphanumerical.";
    public static final String MESSAGE_DUPLICATE_TAGS = "The new name of the tag cannot be the same as the old name.";
    private final ArrayList<Index> affectedIndexes;
    private final Tag toEdit;
    private final Tag newTag;

    /**
     * @param toEdit The value of the tag to be changed
     * @param newTag The new value for the tag
     */
    public EditTagCommand(Tag toEdit, Tag newTag) {
        requireNonNull(toEdit);
        requireNonNull(newTag);
        this.toEdit = toEdit;
        this.newTag = newTag;
        this.affectedIndexes = new ArrayList<>();
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();
        ReadOnlyPerson currentlyViewed;
        Set<Tag> tagSet;
        boolean tagNotPresent = true;
        for (int i = 0; i < lastShownList.size(); i++) {
            currentlyViewed = lastShownList.get(i);
            tagSet = currentlyViewed.getTags();
            if (tagSet.contains(toEdit)) {
                tagNotPresent = false;
                affectedIndexes.add(Index.fromZeroBased(i));
            }
        }

        if (tagNotPresent) {
            throw new CommandException(MESSAGE_TAG_NOT_FOUND);
        }
        try {
            model.editTag(toEdit, newTag, affectedIndexes);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TAGS);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_TAG_SUCCESS, toEdit.tagName, newTag.tagName));
    }


    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditTagCommand)) {
            return false;
        }

        // state check
        EditTagCommand e = (EditTagCommand) other;
        return toEdit.equals(e.toEdit)
                && newTag.equals(e.newTag);
    }
}
```
###### \java\seedu\address\logic\LogicManager.java
``` java
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return model.getFilteredTaskList();
    }
```
###### \java\seedu\address\logic\parser\AddCommandParser.java
``` java
    /**
     * Constructs a ReadOnlyPerson from the arguments provided.
     */
    private static ReadOnlyTask constructTask(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_DESCRIPTION, PREFIX_DEADLINE, PREFIX_PRIORITY);

        if (!(arePrefixesPresent(argMultimap, PREFIX_NAME))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_TASK_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_DESCRIPTION))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_TASK_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_DEADLINE))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_TASK_USAGE));
        }

        if (!(arePrefixesPresent(argMultimap, PREFIX_PRIORITY))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_TASK_USAGE));
        }

        try {
            TaskName name;
            Description description;
            Deadline deadline;
            Priority priority;

            name = ParserUtil.parseTaskName(argMultimap.getValue(PREFIX_NAME)).get();
            description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get();
            deadline = ParserUtil.parseDeadline(argMultimap.getValue(PREFIX_DEADLINE)).get();
            priority = ParserUtil.parsePriority(argMultimap.getValue(PREFIX_PRIORITY)).get();


            ReadOnlyTask task = new Task(name, description, deadline, priority);
            return task;
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
```
###### \java\seedu\address\logic\parser\AssignTaskCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TARGET;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments, and creates a new {@code AssignCommand} object**/
public class AssignTaskCommandParser implements Parser<AssignCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AssignCommand
     * and returns an AssignCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AssignCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TARGET);
        if (!argMultimap.getValue(PREFIX_TARGET).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AssignCommand.MESSAGE_USAGE));
        }
        String target = argMultimap.getValue(PREFIX_TARGET).get();
        String persons = argMultimap.getPreamble();
        ArrayList<Index> targetIndexes = parseIndexes(target);
        ArrayList<Index> personIndexes = parseIndexes(persons);
        if (targetIndexes.size() != 1) {
            throw new ParseException(AssignCommand.MESSAGE_INVALID_TARGET_ARGS);
        } else if (personIndexes.size() < 1) {
            throw new ParseException(AssignCommand.MESSAGE_INVALID_PERSONS_ARGS);
        }
        Index taskIndex = targetIndexes.get(0);
        return new AssignCommand(personIndexes, taskIndex);
    }

    /**
     *   Parses the given {@code String} and returns an ArrayList of Indexes that correspond to
     *   the value in the String.
     *   @throws ParseException if any of the values in the String cannot be converted into an {@code Index}
     */
    private ArrayList<Index> parseIndexes(String args) throws ParseException {
        String[] splitted = args.split(" ");
        ArrayList<Index> targetsToAdd = new ArrayList<>();
        int parsedInt;
        try {
            for (String s : splitted) {
                parsedInt = Integer.parseInt(s);
                targetsToAdd.add(Index.fromOneBased(parsedInt));
            }
        } catch (NumberFormatException nfe) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return targetsToAdd;
    }
}
```
###### \java\seedu\address\logic\parser\DismissTaskCommandParser.java
``` java

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FROM;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.ArrayList;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DismissCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments, and creates a new {@code DismissCommand} object**/
public class DismissTaskCommandParser implements Parser<DismissCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DismissCommand
     * and returns an DismissCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DismissCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FROM);
        if (!argMultimap.getValue(PREFIX_FROM).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DismissCommand.MESSAGE_USAGE));
        }
        String target = argMultimap.getValue(PREFIX_FROM).get();
        String persons = argMultimap.getPreamble();
        ArrayList<Index> targetIndexes = parseIndexes(target);
        ArrayList<Index> personIndexes = parseIndexes(persons);
        if (targetIndexes.size() != 1) {
            throw new ParseException(DismissCommand.MESSAGE_INVALID_TARGET_ARGS);
        } else if (personIndexes.size() < 1) {
            throw new ParseException(DismissCommand.MESSAGE_INVALID_PERSONS_ARGS);
        }
        Index taskIndex = targetIndexes.get(0);
        return new DismissCommand(personIndexes, taskIndex);
    }

    /**
     *   Parses the given {@code String} and returns an ArrayList of Indexes that correspond to
     *   the value in the String.
     *   @throws ParseException if any of the values in the String cannot be converted into an {@code Index}
     */
    private ArrayList<Index> parseIndexes(String args) throws ParseException {
        String[] splitted = args.split(" ");
        ArrayList<Index> targetsToAdd = new ArrayList<>();
        int parsedInt;
        try {
            for (String s : splitted) {
                parsedInt = Integer.parseInt(s);
                targetsToAdd.add(Index.fromOneBased(parsedInt));
            }
        } catch (NumberFormatException nfe) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return targetsToAdd;
    }
}
```
###### \java\seedu\address\logic\parser\EditTagCommandParser.java
``` java
/** Parses input arguments and creates a new EditTagCommand object */
public class EditTagCommandParser implements Parser<EditTagCommand> {
    public static final String EDITTAG_VALIDATION_REGEX = "[\\p{Alnum}\\s]+[\\p{Alnum}]+";
    public static final int EXPECTED_NUMBER_OF_ARGS = 2;

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditTagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArrayList<Tag> tags;
        String trimmed = args.trim();
        if (!args.matches(EDITTAG_VALIDATION_REGEX)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE));
        }

        try {
            tags = readTags(trimmed);
        } catch (IllegalValueException ive) {
            throw new ParseException(MESSAGE_INVALID_TAG_NAME);
        }

        if (tags.size() != EXPECTED_NUMBER_OF_ARGS) {
            throw new ParseException(MESSAGE_INSUFFICIENT_ARGS);
        }

        Tag toChange = tags.get(0);
        Tag newTag = tags.get(1);
        if (toChange.equals(newTag)) {
            throw new ParseException(MESSAGE_DUPLICATE_TAGS);
        }

        return new EditTagCommand(toChange, newTag);
    }
    /** Atempts to read the string and parse it into a Tag set*/
    private ArrayList<Tag> readTags(String args) throws IllegalValueException {
        String[] splittedArgs = args.split("\\s+");
        ArrayList<Tag> tagList = new ArrayList<>();
        for (String s : splittedArgs) {
            Tag newTag = new Tag(s);
            tagList.add(newTag);
        }
        return tagList;
    }
}
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a string into a {@code TaskName} if it is present.
     */
    public static Optional<TaskName> parseTaskName(Optional<String> name) throws IllegalValueException {
        requireNonNull(name);
        return name.isPresent() ? Optional.of(new TaskName(name.get())) : Optional.empty();
    }

    /**
     * Parses a string into a {@code Description} if it is present.
     */
    public static Optional<Description> parseDescription(Optional<String> description) throws IllegalValueException {
        requireNonNull(description);
        return description.isPresent() ? Optional.of(new Description(description.get())) : Optional.empty();
    }

    /**
     * Parses a string into a {@code Deadline} if it is present.
     */
    public static Optional<Deadline> parseDeadline(Optional<String> deadline) throws IllegalValueException {
        requireNonNull(deadline);
        return deadline.isPresent() ? Optional.of(new Deadline(deadline.get())) : Optional.empty();
    }

    /**
     * Parses a string into a {@code Priority} if it is present.
     */
    public static Optional<Priority> parsePriority(Optional<String> priority) throws IllegalValueException {
        requireNonNull(priority);
        return priority.isPresent() ? Optional.of(new Priority(priority.get())) : Optional.empty();
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Resets only the existing contact or task data of this {@code AddressBook}.
     */
    public void resetPartialData(ReadOnlyAddressBook newData, Prefix type) {
        requireNonNull(newData);
        requireNonNull(type);
        try {
            if (type.equals(PREFIX_TASK)) {
                setTasks(newData.getTasksList());
            } else if (type.equals(PREFIX_PERSON)) {
                setPersons(newData.getPersonList());
                setTags(new HashSet<>(newData.getTagList()));
                syncMasterTagListWith(persons);
            } else {
                throw new AssertionError("Type must either be persons or tasks");
            }
        } catch (DuplicatePersonException e) {
            assert false : "Address books should not have duplicate persons";
        } catch (DuplicateTaskException e) {
            assert false : "Address books should not have duplicate tasks";
        }
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Adds a task to the address book.
     *
     * @throws DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(ReadOnlyTask t) throws DuplicateTaskException {
        Task newTask = new Task(t);
        tasks.add(newTask);
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws TaskNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeTask(ReadOnlyTask key) throws TaskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new TaskNotFoundException();
        }
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Replaces the given task {@code target} in the list with {@code editedReadOnlyTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedReadOnlyTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedReadOnlyTask);

        Task editedTask = new Task(editedReadOnlyTask);
        tasks.setTask(target, editedTask);
    }
    //// util methods

    @Override
    public String toString() {
        return persons.asObservableList().size() + " persons, " + tags.asObservableList().size() +  " tags"
                + tasks.asObservableList().size() +  " tasks";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyPerson> getPersonList() {
        return persons.asObservableList();
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    @Override
    public ObservableList<ReadOnlyTask> getTasksList() {
        return tasks.asObservableList();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void resetPartialData(ReadOnlyAddressBook newData, Prefix type) {
        assert(type.equals(PREFIX_TASK) || type.equals(PREFIX_PERSON));
        if (type.equals(PREFIX_TASK)) {
            addressBook.resetPartialData(newData, PREFIX_TASK);
            indicateAddressBookChanged();
        } else {
            addressBook.resetPartialData(newData, PREFIX_PERSON);
            indicateAddressBookChanged();
        }
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    /**
     * Replaces the toChange Tag with the newTag Tag, for all Person objects denoted by the indexes.
     * Guarantees: indexes contains at least 1 person that has the toChange Tag.
     */
    public synchronized void editTag(Tag toChange, Tag newTag, ArrayList<Index> indexes)
            throws PersonNotFoundException, DuplicatePersonException {
        List<ReadOnlyPerson> allPersons = this.getFilteredPersonList();
        Set<Tag> personTags;
        Person toUpdate;
        ReadOnlyPerson toRead;
        int index;
        for (Index i : indexes) {
            index = i.getZeroBased();
            toRead = allPersons.get(index);
            toUpdate = new Person(toRead);
            personTags = new HashSet<>(toRead.getTags());
            personTags.remove(toChange);
            personTags.add(newTag);
            toUpdate.setTags(personTags);
            addressBook.updatePerson(toRead, toUpdate);
        }
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void addTask(ReadOnlyTask toAdd) throws DuplicateTaskException {
        addressBook.addTask(toAdd);
        updateFilteredTaskList(PREDICATE_SHOW_ALL_TASKS);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public synchronized void deleteTask(ReadOnlyTask toDelete) throws TaskNotFoundException {
        addressBook.removeTask(toDelete);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void updateTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireAllNonNull(target, editedTask);

        addressBook.updateTask(target, editedTask);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void assignToTask(ArrayList<ReadOnlyPerson> personsToAssign, ReadOnlyTask taskToAssignTo)
            throws TaskNotFoundException, DuplicateTaskException {
        TaskName taskName = taskToAssignTo.getTaskName();
        Description description = taskToAssignTo.getDescription();
        Deadline deadline = taskToAssignTo.getDeadline();
        Priority priority = taskToAssignTo.getPriority();
        Assignees assignees = taskToAssignTo.getAssignees();

        assignees.assign(personsToAssign);
        ReadOnlyTask updatedTask = new Task(taskName, description, deadline, priority, assignees);
        updateTask(taskToAssignTo, updatedTask);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void dismissFromTask(ArrayList<ReadOnlyPerson> personsToDismiss, ReadOnlyTask taskToDismissFrom)
            throws TaskNotFoundException, DuplicateTaskException {
        TaskName taskName = taskToDismissFrom.getTaskName();
        Description description = taskToDismissFrom.getDescription();
        Deadline deadline = taskToDismissFrom.getDeadline();
        Priority priority = taskToDismissFrom.getPriority();
        Assignees assignees = taskToDismissFrom.getAssignees();

        assignees.dismiss(personsToDismiss);
        ReadOnlyTask updatedTask = new Task(taskName, description, deadline, priority, assignees);
        updateTask(taskToDismissFrom, updatedTask);
    }

```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public ObservableList<ReadOnlyTask> getFilteredTaskList() {
        return FXCollections.unmodifiableObservableList(filteredTasks);
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void updateFilteredTaskList(Predicate<ReadOnlyTask> predicate) {
        requireNonNull(predicate);
        filteredTasks.setPredicate(predicate);
    }
```
###### \java\seedu\address\model\task\Deadline.java
``` java
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
        } else if (deadline.equals(DEADLINE_PLACEHOLDER_VALUE)) {
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
```
###### \java\seedu\address\model\task\Description.java
``` java
/**
 * Represents a task description in the address book.
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Task descriptions can be in any format";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DESCRIPTION_VALIDATION_REGEX = "[^\\s].*";
    public static final String DESCRIPTION_PLACEHOLDER_VALUE = "";
    public final String value;

    /**
     * Validates given description.
     *
     * @throws IllegalValueException if given description string is invalid.
     */
    public Description(String description) throws IllegalValueException {
        if (description == null) {
            this.value = DESCRIPTION_PLACEHOLDER_VALUE;
            return;
        }
        String trimmedDescription = description.trim();
        if (!isValidDescription(trimmedDescription)) {
            throw new IllegalValueException(MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        this.value = trimmedDescription;
    }

    /**
     * Returns true if a given string is a valid description.
     */
    public static boolean isValidDescription(String test) {
        return test.equals(DESCRIPTION_PLACEHOLDER_VALUE) || test.matches(DESCRIPTION_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.value.equals(((Description) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\task\exceptions\DuplicateTaskException.java
``` java
/**
 * Signals that the operation will result in duplicate Person objects.
 */
public class DuplicateTaskException extends DuplicateDataException {
    public DuplicateTaskException() {
        super("Operation would result in duplicate tasks");
    }
}
```
###### \java\seedu\address\model\task\exceptions\TaskNotFoundException.java
``` java
/**
 * Signals that the operation could not find the specified task.
 */
public class TaskNotFoundException extends Exception {
}
```
###### \java\seedu\address\model\task\Priority.java
``` java
/**
 * Represents a task priority in the address book.
 */
public class Priority {

    public static final String MESSAGE_PRIORITY_CONSTRAINTS =
            "Task priorities must be an integer from 1 to 5, inclusive, where 1 represents the lowest priority";

    public static final int PRIORITY_LOWER_BOUND = 1;
    public static final int PRIORITY_UPPER_BOUND = 5;
    public static final String PRIORITY_VALIDATION_REGEX = "[\\d].*";
    public static final String PRIORITY_PLACEHOLDER_VALUE = "";
    public final String value;

    /**
     * Validates given priority.
     *
     * @throws IllegalValueException if given priority string is invalid.
     */
    public Priority(String priority) throws IllegalValueException {
        if (priority == null) {
            this.value = PRIORITY_PLACEHOLDER_VALUE;
            return;
        } else if (priority.equals(PRIORITY_PLACEHOLDER_VALUE)) {
            this.value = PRIORITY_PLACEHOLDER_VALUE;
            return;
        }
        String trimmedPriority = priority.trim();
        try {
            Integer.parseInt(trimmedPriority);
        } catch (NumberFormatException e) {
            throw new IllegalValueException(MESSAGE_PRIORITY_CONSTRAINTS);
        }
        if (!isValidPriority(trimmedPriority)) {
            throw new IllegalValueException(MESSAGE_PRIORITY_CONSTRAINTS);
        }
        this.value = trimmedPriority;
    }

    /**
     * Returns true if a given string is a valid task priority.
     */
    public static boolean isValidPriority(String test) {
        if (test.equals(PRIORITY_PLACEHOLDER_VALUE)) {
            return true;
        } else if (!test.matches(PRIORITY_VALIDATION_REGEX)) {
            return false;
        } else {
            int intTest = Integer.parseInt(test);
            return isWithinBounds(intTest);
        }
    }

    /**
     * Returns true if the value is within the upper and lower bounds of priority
     */
    public static boolean isWithinBounds(int test) {
        return test <= PRIORITY_UPPER_BOUND && test >= PRIORITY_LOWER_BOUND;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Priority // instanceof handles nulls
                && this.value.equals(((Priority) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\task\ReadOnlyTask.java
``` java
/**
 * Provides an immutable interface for a Task in the address book.
 */
public interface ReadOnlyTask {

    TaskName getTaskName();
    Description getDescription();
    Deadline getDeadline();
    Priority getPriority();
    Assignees getAssignees();
    ObjectProperty<TaskName> taskNameProperty();
    ObjectProperty<Description> descriptionProperty();
    ObjectProperty<Deadline> deadlineProperty();
    ObjectProperty<Priority> priorityProperty();
    ObjectProperty<Assignees> assigneeProperty();

    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTaskName())
                .append(" Description: ")
                .append(getDescription())
                .append(" Deadline: ")
                .append(getDeadline())
                .append(" Priority: ")
                .append(getPriority());
        return builder.toString();
    }

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTaskName().equals(this.getTaskName()) // state checks here onwards
                && other.getDescription().equals(this.getDescription())
                && other.getDeadline().equals(this.getDeadline())
                && other.getPriority().equals(this.getPriority()))
                && other.getAssignees().equals(this.getAssignees());
    }
}
```
###### \java\seedu\address\model\task\Task.java
``` java
/**
 * Represents a task object in the address book.
 */
public class Task implements ReadOnlyTask {

    private ObjectProperty<TaskName> taskName;
    private ObjectProperty<Description> description;
    private ObjectProperty<Deadline> deadline;
    private ObjectProperty<Priority> priority;
    private ObjectProperty<Assignees> assignees;

    public Task(TaskName taskName, Description description, Deadline deadline, Priority priority,
                Assignees assignees) {
        this.taskName = new SimpleObjectProperty<>(taskName);
        this.description = new SimpleObjectProperty<>(description);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.priority = new SimpleObjectProperty<>(priority);
        this.assignees = new SimpleObjectProperty<>(assignees);
    }

    public Task(TaskName taskName, Description description, Deadline deadline, Priority priority) {
        this.taskName = new SimpleObjectProperty<>(taskName);
        this.description = new SimpleObjectProperty<>(description);
        this.deadline = new SimpleObjectProperty<>(deadline);
        this.priority = new SimpleObjectProperty<>(priority);
        this.assignees = new SimpleObjectProperty<>(new Assignees());
    }

    public Task(ReadOnlyTask task) {
        this(task.getTaskName(), task.getDescription(), task.getDeadline(), task.getPriority(),
                task.getAssignees());
    }

    public TaskName getTaskName() {
        return taskName.get();
    }

    @Override
    public Description getDescription() {
        return description.get();
    }

    @Override
    public Deadline getDeadline() {
        return deadline.get();
    }

    @Override
    public Priority getPriority() {
        return priority.get();
    }

    @Override
    public Assignees getAssignees() {
        return assignees.get();
    }

    @Override
    public String toString() {
        return getAsText();
    }

    // JavaFX property functions
    @Override
    public ObjectProperty<TaskName> taskNameProperty() {
        return taskName;
    }

    @Override
    public ObjectProperty<Description> descriptionProperty() {
        return description;
    }

    @Override
    public ObjectProperty<Deadline> deadlineProperty() {
        return deadline;
    }

    @Override
    public ObjectProperty<Priority> priorityProperty() {
        return priority;
    }

    @Override
    public ObjectProperty<Assignees> assigneeProperty() {
        return assignees;
    }
    // Setters for TaskBuilder testing

    public void setTaskName(TaskName taskName) {
        this.taskName.set(taskName);
    }

    public void setDeadline(Deadline deadline) {
        this.deadline.set(deadline);
    }

    public void setDescription(Description description) {
        this.description.set(description);
    }

    public void setPriority(Priority priority) {
        this.priority.set(priority);
    }

    public void setAssignees(Assignees assignees) {
        this.assignees.set(assignees);
    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
            || (other instanceof ReadOnlyTask // instanceof handles nulls
            && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(taskName, description, deadline, priority, assignees);
    }
}
```
###### \java\seedu\address\model\task\TaskContainsKeywordPredicate.java
``` java
/**
 * Tests that a {@code ReadOnlyTask}'s {@code TaskName} or {@code Description} matches any of the keywords given.
 */
public class TaskContainsKeywordPredicate  implements Predicate<ReadOnlyTask> {
    private final List<String> keywords;

    public TaskContainsKeywordPredicate(List<String> keywords) {
        this.keywords = keywords;

    }

    @Override
    public boolean test(ReadOnlyTask task) {
        for (int i = 0; i < keywords.size(); i++) {
            String keyword = keywords.get(i);
            if (StringUtil.containsWordIgnoreCase(task.getTaskName().taskName, keyword)
                    || StringUtil.containsWordIgnoreCase(task.getDescription().value, keyword)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskContainsKeywordPredicate // instanceof handles nulls
                && this.keywords.equals(((TaskContainsKeywordPredicate) other).keywords)); // state check
    }
}
```
###### \java\seedu\address\model\task\TaskName.java
``` java
/**
 * Represents a Task name in the address book.
 */
public class TaskName {

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Task names can be in any format, and should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String NAME_VALIDATION_REGEX = "[^\\s].*";

    public final String taskName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public TaskName(String name) throws IllegalValueException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!isValidName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.taskName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return taskName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskName // instanceof handles nulls
                && this.taskName.equals(((TaskName) other).taskName)); // state check
    }

    @Override
    public int hashCode() {
        return taskName.hashCode();
    }

}
```
###### \java\seedu\address\model\task\UniqueTaskList.java
``` java
/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyTask> mappedList = EasyBind.map(internalList, (task) -> task);

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(ReadOnlyTask toAdd) throws DuplicateTaskException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }
        internalList.add(new Task(toAdd));
    }

    /**
     * Replaces the task {@code target} in the list with {@code editedTask}.
     *
     * @throws DuplicateTaskException if the replacement is equivalent to another existing task in the list.
     * @throws TaskNotFoundException if {@code target} could not be found in the list.
     */
    public void setTask(ReadOnlyTask target, ReadOnlyTask editedTask)
            throws DuplicateTaskException, TaskNotFoundException {
        requireNonNull(editedTask);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new TaskNotFoundException();
        }

        if (!target.equals(editedTask) && internalList.contains(editedTask)) {
            throw new DuplicateTaskException();
        }

        internalList.set(index, new Task(editedTask));
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        requireNonNull(toRemove);
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public void setTasks(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setTasks(List<? extends ReadOnlyTask> tasks) throws DuplicateTaskException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask task : tasks) {
            replacement.add(new Task(task));
        }
        setTasks(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyTask> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```
###### \java\seedu\address\storage\XmlAdaptedTask.java
``` java
/** JAXB-friendly version of a Task */
public class XmlAdaptedTask {
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String deadline;
    @XmlElement(required = true)
    private String priority;

    /**
     * Constructs an XmlAdaptedTask.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTask() {}


    /**
     * Converts a given Task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        name = source.getTaskName().taskName;
        description = source.getDescription().value;
        deadline = source.getDeadline().value;
        priority = source.getPriority().value;
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Task toModelType() throws IllegalValueException {
        final TaskName name = new TaskName(this.name);
        final Description description = new Description(this.description);
        final Deadline deadline = new Deadline(this.deadline);
        final Priority priority = new Priority(this.priority);
        return new Task(name, description, deadline, priority, new Assignees());
    }
}
```
###### \java\seedu\address\ui\TaskCard.java
``` java
/**
 * A UI component that displays the information of a {@code Task}
 * */
public class TaskCard  extends UiPart<Region> {

    public static final int DEFAULT_NAME_SIZE = 15;
    public static final int DEFAULT_ATTRIBUTE_SIZE = 10;
    public static final int FONT_SIZE_EXTENDER = 5;
    public static final int DEFAULT_FONT_SIZE_MULTIPLIER = 0;

    private static final String FXML = "TaskListCard.fxml";
    private static int nameSize = DEFAULT_NAME_SIZE;
    private static int attributeSize = DEFAULT_ATTRIBUTE_SIZE;

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyTask task;

    @FXML
    private HBox taskCardPane;
    @FXML
    private Label taskName;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label deadline;
    @FXML
    private Label priority;
    @FXML
    private Label assignCount;

    private int fontSizeMultipler;

    public TaskCard(ReadOnlyTask task, int displayedIndex, int fontSizeMultiplier) {
        super(FXML);
        this.task = task;
        this.fontSizeMultipler = fontSizeMultiplier;
        id.setText(displayedIndex + ". ");
        bindListeners(task);
        updateAttributeSizes();
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Task} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyTask task) {
        taskName.textProperty().bind(Bindings.convert(task.taskNameProperty()));
        description.textProperty().bind(Bindings.convert(task.descriptionProperty()));
        deadline.textProperty().bind(Bindings.convert(task.deadlineProperty()));
        priority.textProperty().bind(Bindings.convert(task.priorityProperty()));
        assignCount.textProperty().bind(Bindings.convert(task.assigneeProperty()));
    }

    /**
     * Set default size for all attributes
     */
    public void updateAttributeSizes() {
        nameSize = DEFAULT_NAME_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);
        attributeSize = DEFAULT_ATTRIBUTE_SIZE + (fontSizeMultipler * FONT_SIZE_EXTENDER);

        // Set styles using set name and attribute sizes
        taskName.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        id.setStyle("-fx-font-size: " + Integer.toString(nameSize));
        description.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        deadline.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
        priority.setStyle("-fx-font-size: " + Integer.toString(attributeSize));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TaskCard)) {
            return false;
        }

        // state check
        TaskCard card = (TaskCard) other;
        return id.getText().equals(card.id.getText())
                && task.equals(card.task);
    }

    public Label getTaskName() {
        return taskName;
    }

    public Label getId() {
        return id;
    }

    public Label getDescription() {
        return description;
    }

    public Label getDeadline() {
        return deadline;
    }

    public Label getPriority() {
        return priority;
    }

    public void setFontSizeMultiplier(int fontSizeMultipler) {
        this.fontSizeMultipler = fontSizeMultipler;
    }

    public int getFontSizeMultiplier() {
        return this.fontSizeMultipler;
    }
}
```
###### \java\seedu\address\ui\TaskListPanel.java
``` java
/**
 * Panel containing the list of tasks.
 */
public class TaskListPanel extends UiPart<Region> {
    private static final int MINIMUM_FONT_SIZE_MULTIPLIER = 0;
    private static final int MAXIMUM_FONT_SIZE_MULTIPLIER = 20;
    private static final String FXML = "TaskListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);

    @FXML
    private ListView<TaskCard> taskListView;

    private int fontSizeMultiplier;
    private ObservableList<ReadOnlyTask> taskList;

    public TaskListPanel(ObservableList<ReadOnlyTask> taskList) {
        super(FXML);
        this.taskList = taskList;
        fontSizeMultiplier = 0;
        setConnections(taskList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ReadOnlyTask> taskList) {
        ObservableList<TaskCard> mappedList = EasyBind.map(
                taskList, (task) -> new TaskCard(task, taskList.indexOf(task) + 1, fontSizeMultiplier));
        taskListView.setItems(mappedList);
        taskListView.setCellFactory(listView -> new TaskListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        taskListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                        raise(new TaskPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Increases all task cards' font sizes in person list
     */
    public void increaseFontSize() {
        logger.info("TaskListPanel: Increasing font sizes");
        fontSizeMultiplier = Math.min(MAXIMUM_FONT_SIZE_MULTIPLIER, fontSizeMultiplier + 1);
        setConnections(taskList);
    }

    /**
     * Decreases all task cards' font sizes in person list
     */
    public void decreaseFontSize() {
        logger.info("TaskListPanel: Decreasing font sizes");
        fontSizeMultiplier = Math.max(MINIMUM_FONT_SIZE_MULTIPLIER, fontSizeMultiplier - 1);
        setConnections(taskList);
    }

    /**
     * Resets all task cards' font sizes in person list
     */
    public void resetFontSize() {
        logger.info("TaskListPanel: Resetting font sizes");
        fontSizeMultiplier = MINIMUM_FONT_SIZE_MULTIPLIER;
        setConnections(taskList);
    }

    /**
     * Scrolls to the {@code TaskCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            taskListView.scrollTo(index);
            taskListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code TaskCard}.
     */
    class TaskListViewCell extends ListCell<TaskCard> {

        @Override
        protected void updateItem(TaskCard task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(task.getRoot());
            }
        }
    }
}
```
