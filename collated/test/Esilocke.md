# Esilocke
###### \java\seedu\address\logic\commands\AssignCommandTest.java
``` java
public class AssignCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_assignOnePerson_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON);
        ReadOnlyTask assignedTask = model.getFilteredTaskList().get(0);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(AssignCommand.MESSAGE_SUCCESS, toAssign.size(),
                assignedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignManyPersons_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON);
        ReadOnlyTask assignedTask = model.getFilteredTaskList().get(0);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(AssignCommand.MESSAGE_SUCCESS, toAssign.size(),
                assignedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_assignDuplicates_addSuccessful() throws Exception {
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        ReadOnlyTask assignedTask = model.getFilteredTaskList().get(0);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(AssignCommand.MESSAGE_SUCCESS, 1, assignedTask);
        assertCommandSuccess(assignCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredPersonList().size());
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, outOfRangeIndex, INDEX_SECOND_PERSON);

        AssignCommand assignCommand = prepareCommand(toAssign, INDEX_FIRST_TASK);
        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredTaskList().size());
        List<Index> toAssign = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        AssignCommand assignCommand = prepareCommand(toAssign, outOfRangeIndex);
        assertCommandFailure(assignCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ArrayList<Index> assignFirstThree = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON,
                INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
        ArrayList<Index> assignFirstTwo = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        AssignCommand assignTwoToFirst = new AssignCommand(assignFirstTwo, INDEX_FIRST_TASK);
        AssignCommand assignThreeToFirst = new AssignCommand(assignFirstThree, INDEX_FIRST_TASK);
        AssignCommand assignTwoToSecond = new AssignCommand(assignFirstTwo, INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(assignTwoToFirst.equals(assignTwoToFirst));

        // same values -> returns true
        AssignCommand assignTwoToFirstCopy = new AssignCommand(assignFirstTwo, INDEX_FIRST_TASK);
        assertTrue(assignTwoToFirst.equals(assignTwoToFirstCopy));

        // different types -> returns false
        assertFalse(assignTwoToFirst.equals(1));

        // null -> returns false
        assertFalse(assignTwoToFirst.equals(null));

        // different person/task indexes -> returns false
        assertFalse(assignTwoToFirst.equals(assignThreeToFirst));
        assertFalse(assignTwoToFirst.equals(assignTwoToSecond));
    }
    /**
     * Generates a new AssignCommand with the specified targets.
     */
    private AssignCommand prepareCommand(List<Index> personsToAssign, Index task) {
        ArrayList<Index> listIndexes = new ArrayList<>(personsToAssign);
        AssignCommand command = new AssignCommand(listIndexes, task);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\DismissCommandTest.java
``` java
public class DismissCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_dismissOnePerson_addSuccessful() throws Exception {
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON);
        ReadOnlyTask dismissedTask = model.getFilteredTaskList().get(0);

        DismissCommand dismissCommand = prepareCommand(toDismiss, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(DismissCommand.MESSAGE_SUCCESS, toDismiss.size(),
                dismissedTask);
        assertCommandSuccess(dismissCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_dismissManyPersons_addSuccessful() throws Exception {
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_THIRD_PERSON);
        ReadOnlyTask dismissedTask = model.getFilteredTaskList().get(0);

        DismissCommand dismissCommand = prepareCommand(toDismiss, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(DismissCommand.MESSAGE_SUCCESS, toDismiss.size(),
                dismissedTask);
        assertCommandSuccess(dismissCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_dismissDuplicates_addSuccessful() throws Exception {
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        ReadOnlyTask dismissedTask = model.getFilteredTaskList().get(0);

        DismissCommand dismissCommand = prepareCommand(toDismiss, INDEX_FIRST_TASK);
        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String expectedMessage = String.format(DismissCommand.MESSAGE_SUCCESS, 1, dismissedTask);
        assertCommandSuccess(dismissCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredPersonList().size());
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON, outOfRangeIndex, INDEX_SECOND_PERSON);

        DismissCommand dismissCommand = prepareCommand(toDismiss, INDEX_FIRST_TASK);
        assertCommandFailure(dismissCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidTaskIndex_throwsCommandException() throws Exception {
        Index outOfRangeIndex = Index.fromZeroBased(model.getFilteredTaskList().size());
        List<Index> toDismiss = Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        DismissCommand dismissCommand = prepareCommand(toDismiss, outOfRangeIndex);
        assertCommandFailure(dismissCommand, model, Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        ArrayList<Index> dismissFirstThree = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON,
                INDEX_SECOND_PERSON, INDEX_THIRD_PERSON));
        ArrayList<Index> dismissFirstTwo = new ArrayList<>(Arrays.asList(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
        DismissCommand dismissTwoToFirst = new DismissCommand(dismissFirstTwo, INDEX_FIRST_TASK);
        DismissCommand dismissThreeToFirst = new DismissCommand(dismissFirstThree, INDEX_FIRST_TASK);
        DismissCommand dismissTwoToSecond = new DismissCommand(dismissFirstTwo, INDEX_SECOND_TASK);

        // same object -> returns true
        assertTrue(dismissTwoToFirst.equals(dismissTwoToFirst));

        // same values -> returns true
        DismissCommand dismissTwoToFirstCopy = new DismissCommand(dismissFirstTwo, INDEX_FIRST_TASK);
        assertTrue(dismissTwoToFirst.equals(dismissTwoToFirstCopy));

        // different types -> returns false
        assertFalse(dismissTwoToFirst.equals(1));

        // null -> returns false
        assertFalse(dismissTwoToFirst.equals(null));

        // different person/task indexes -> returns false
        assertFalse(dismissTwoToFirst.equals(dismissThreeToFirst));
        assertFalse(dismissTwoToFirst.equals(dismissTwoToSecond));
    }
    /**
     * Generates a new DismissCommand with the specified targets.
     */
    private DismissCommand prepareCommand(List<Index> personsToDismiss, Index task) {
        ArrayList<Index> listIndexes = new ArrayList<>(personsToDismiss);
        DismissCommand command = new DismissCommand(listIndexes, task);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
```
###### \java\seedu\address\logic\commands\EditTagCommandTest.java
``` java
public class EditTagCommandTest {
    private Model model = new ModelManager(getTypicalPersonsAddressBook(), new UserPrefs());
    @Test
    public void noTagsPresent() throws IllegalValueException {
        Model taglessModel = new ModelManager(getTaglessAddressBook(), new UserPrefs());
        Model blankModel = new ModelManager(new AddressBook(), new UserPrefs());
        String absentTag = "notInAddressBook";
        EditTagCommand noPersonCommand = prepareCommand(VALID_TAG_FRIEND, absentTag, blankModel);
        EditTagCommand noTagsCommand = prepareCommand(VALID_TAG_FRIEND, absentTag, taglessModel);
        EditTagCommand absentTagCommand = prepareCommand(VALID_TAG_FRIEND, absentTag, model);

        // No people are in this address book
        assertCommandFailure(noPersonCommand, taglessModel, MESSAGE_TAG_NOT_FOUND);
        // All persons do not have tags
        assertCommandFailure(noTagsCommand, taglessModel, MESSAGE_TAG_NOT_FOUND);
        // No persons in address book has the required tag
        assertCommandFailure(absentTagCommand, model, MESSAGE_TAG_NOT_FOUND);
    }

    @Test
    public void editTagSubset() throws IllegalValueException, PersonNotFoundException {
        AddressBook testBook = prepareAddressBook();
        Model testModel = new ModelManager(testBook, new UserPrefs());
        EditTagCommand tagChangeColleagueToHusband = prepareCommand(VALID_TAG_COLLEAGUE, VALID_TAG_HUSBAND, testModel);
        String expectedMessage = String.format(MESSAGE_EDIT_TAG_SUCCESS, VALID_TAG_COLLEAGUE, VALID_TAG_HUSBAND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);
        Tag colleagueTag = new Tag(VALID_TAG_COLLEAGUE);

        // Attempt to change some Person objects
        Model expectedModel = new ModelManager(testModel.getAddressBook(), new UserPrefs());
        ArrayList<Index> indices = new ArrayList<>();
        indices.add(Index.fromZeroBased(0));
        indices.add(Index.fromZeroBased(2));
        expectedModel.editTag(colleagueTag, husbandTag, indices);
        assertCommandSuccess(tagChangeColleagueToHusband, testModel, expectedMessage, expectedModel);
    }
    @Test
    public void editTagAll() throws IllegalValueException, PersonNotFoundException {
        AddressBook testBook = prepareAddressBook();
        Model testModel = new ModelManager(testBook, new UserPrefs());
        EditTagCommand tagChangeFriendToHusband = prepareCommand(VALID_TAG_FRIEND, VALID_TAG_HUSBAND, testModel);
        String expectedMessage = String.format(MESSAGE_EDIT_TAG_SUCCESS, VALID_TAG_FRIEND, VALID_TAG_HUSBAND);
        Tag friendTag = new Tag(VALID_TAG_FRIEND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);

        // Attempt to change all Person objects
        Model expectedModel = new ModelManager(testModel.getAddressBook(), new UserPrefs());
        ArrayList<Index> indices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            indices.add(Index.fromZeroBased(i));
        }
        expectedModel.editTag(friendTag, husbandTag, indices);
        assertCommandSuccess(tagChangeFriendToHusband, testModel, expectedMessage, expectedModel);
    }
    /** Returns a new EditTagCommand with the parameters */
    public EditTagCommand prepareCommand(String toChange, String newValue, Model model) throws IllegalValueException {
        Tag changedTag = new Tag(toChange);
        Tag newTag = new Tag(newValue);
        EditTagCommand editTagCommand = new EditTagCommand(changedTag, newTag);
        editTagCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return editTagCommand;
    }
    /** Returns a pre-made Address Book for testing purposes */
    public AddressBook prepareAddressBook() throws DuplicatePersonException {
        ReadOnlyPerson alice = new PersonBuilder().withName("Alice Pauline")
                .withTags(VALID_TAG_FRIEND, VALID_TAG_COLLEAGUE).build();
        ReadOnlyPerson bernice = new PersonBuilder().withName("Bernice Applecut")
                .withTags(VALID_TAG_FRIEND).build();
        ReadOnlyPerson clarice = new PersonBuilder().withName("Clarice Fenderbunt")
                .withTags(VALID_TAG_FRIEND, VALID_TAG_COLLEAGUE).build();
        ReadOnlyPerson denise = new PersonBuilder().withName("Denise Lieselocke")
                .withTags(VALID_TAG_FRIEND).build();
        ArrayList<ReadOnlyPerson> toAdd = new ArrayList<>(Arrays.asList(alice, bernice, clarice, denise));
        AddressBook preparedBook = new AddressBook();
        for (ReadOnlyPerson r : toAdd) {
            preparedBook.addPerson(r);
        }
        return preparedBook;
    }
}
```
###### \java\seedu\address\logic\commands\EditTaskDescriptorTest.java
``` java
public class EditTaskDescriptorTest {
    @Test
    public void equals() {
        // same values -> returns true
        EditTaskDescriptor descriptorWithSameValues = new EditTaskDescriptor(DESC_PENCIL);
        assertTrue(DESC_PENCIL.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_PENCIL.equals(DESC_PENCIL));

        // null -> returns false
        assertFalse(DESC_PENCIL.equals(null));

        // different types -> returns false
        assertFalse(DESC_PENCIL.equals(5));

        // different values -> returns false
        assertFalse(DESC_PENCIL.equals(DESC_PAPER));

        // different name -> returns false
        EditTaskDescriptor editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL)
                .withTaskName(VALID_TASK_NAME_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));

        // different description -> returns false
        editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL).withDescription(VALID_DESCRIPTION_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));

        // different deadline -> returns false
        editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL).withDeadline(VALID_DEADLINE_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));

        // different priority -> returns false
        editedPencil = new EditTaskDescriptorBuilder(DESC_PENCIL).withPriority(VALID_PRIORITY_PAPER).build();
        assertFalse(DESC_PENCIL.equals(editedPencil));
    }
}
```
###### \java\seedu\address\logic\parser\AddCommandParserTest.java
``` java
    @Test
    public void parseTasksAllFieldsPresent_success() {
        Task expectedTask = new TaskBuilder().withTaskName(VALID_TASK_NAME_PENCIL)
                .withDescription(VALID_DESCRIPTION_PENCIL).withDeadline(VALID_DEADLINE_PENCIL)
                .withPriority(VALID_PRIORITY_PENCIL).build();

        // multiple names - last name accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PAPER
                + TASK_NAME_DESC_PENCIL + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple descriptions - last description accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PAPER + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple deadlines - last deadline accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PAPER + DEADLINE_DESC_PENCIL
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));

        // multiple priorities - last priority accepted
        assertParseSuccess(parser, AddCommand.COMMAND_WORD + TASK_SEPARATOR + TASK_NAME_DESC_PENCIL
                + DESCRIPTION_DESC_PENCIL + DEADLINE_DESC_PENCIL + PRIORITY_DESC_PAPER
                + PRIORITY_DESC_PENCIL, new AddCommand(expectedTask));
    }
```
###### \java\seedu\address\logic\parser\DeleteCommandParserTest.java
``` java
    @Test
    public void parseTaskValidArgs_returnsDeleteCommand() {
        assertParseSuccess(parser, TASK_SEPARATOR + "1",
                new DeleteCommand(INDEX_FIRST_PERSON, DELETE_TYPE_TASK));
    }
```
###### \java\seedu\address\logic\parser\DeleteCommandParserTest.java
``` java
    @Test
    public void parseTaskInvalidArgs_throwsParseException() {
        assertParseFailure(parser, TASK_SEPARATOR + "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
```
###### \java\seedu\address\logic\parser\EditTagCommandParserTest.java
``` java
public class EditTagCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditTagCommand.MESSAGE_USAGE);


    private EditTagCommandParser parser = new EditTagCommandParser();
    @Test
    public void invalidInputTest() {
        // empty argument
        assertParseFailure(parser, " ", MESSAGE_INVALID_FORMAT);
        // too little args
        assertParseFailure(parser, VALID_TAG_FRIEND, MESSAGE_INSUFFICIENT_ARGS);
        // too many args
        assertParseFailure(parser, VALID_TAG_FRIEND + " " + VALID_TAG_FRIEND
                + " " + VALID_TAG_FRIEND, MESSAGE_INSUFFICIENT_ARGS);
        // args are the same
        assertParseFailure(parser, VALID_TAG_FRIEND + " " + VALID_TAG_FRIEND, MESSAGE_DUPLICATE_TAGS);
        // args are invalid
        assertParseFailure(parser, INVALID_TAG_DESC + " " + INVALID_TAG_DESC, MESSAGE_INVALID_FORMAT);
    }
    @Test
    public void validInputTest() throws IllegalValueException {
        Tag friendTag = new Tag(VALID_TAG_FRIEND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);
        Tag friendTagUpper = new Tag (VALID_TAG_FRIEND.toUpperCase());
        // case changes
        assertParseSuccess(parser, VALID_TAG_FRIEND + " "
                + VALID_TAG_FRIEND.toUpperCase(), new EditTagCommand(friendTag, friendTagUpper));
        // two distinct words
        assertParseSuccess(parser, VALID_TAG_FRIEND + " "
                + VALID_TAG_HUSBAND, new EditTagCommand(friendTag, husbandTag));

    }
}
```
###### \java\seedu\address\testutil\EditTaskDescriptorBuilder.java
``` java
/**
 * A utility class to help with building EditTaskDescriptor objects.
 */
public class EditTaskDescriptorBuilder {

    private EditTaskDescriptor descriptor;

    public EditTaskDescriptorBuilder() {
        descriptor = new EditTaskDescriptor();
    }

    public EditTaskDescriptorBuilder(EditTaskDescriptor descriptor) {
        this.descriptor = new EditTaskDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditTaskDescriptor} with fields containing {@code task}'s details
     */
    public EditTaskDescriptorBuilder(ReadOnlyTask task) {
        descriptor = new EditTaskDescriptor();
        descriptor.setTaskName(task.getTaskName());
        descriptor.setDescription(task.getDescription());
        descriptor.setDeadline(task.getDeadline());
        descriptor.setPriority(task.getPriority());
    }

    /**
     * Sets the {@code TaskName} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withTaskName(String name) {
        try {
            ParserUtil.parseTaskName(Optional.of(name)).ifPresent(descriptor::setTaskName);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("name is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withDescription(String description) {
        try {
            ParserUtil.parseDescription(Optional.of(description)).ifPresent(descriptor::setDescription);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("description is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Deadline} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withDeadline(String deadline) {
        try {
            ParserUtil.parseDeadline(Optional.of(deadline)).ifPresent(descriptor::setDeadline);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("deadline is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Priority} of the {@code EditTaskDescriptor} that we are building.
     */
    public EditTaskDescriptorBuilder withPriority(String priority) {
        try {
            ParserUtil.parsePriority(Optional.of(priority)).ifPresent(descriptor::setPriority);
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("priority is expected to be unique.");
        }
        return this;
    }

    public EditTaskDescriptor build() {
        return descriptor;
    }
}
```
###### \java\seedu\address\testutil\TypicalTasks.java
``` java
/**
 * A utility class containing a list of {@code Task} objects to be used in tests.
 */
public class TypicalTasks {
    public static final ReadOnlyTask ACCEPT = new TaskBuilder().withTaskName("Acceptance Testing")
            .withDescription("Perform acceptance testing on application")
            .withDeadline("04-04-2017").withPriority("3").build();
    public static final ReadOnlyTask BUY = new TaskBuilder().withTaskName("Buy pencil")
            .withDescription("Buy pencils for tomorrow's test")
            .withDeadline("04-04-2017").withPriority("5").build();
    public static final ReadOnlyTask COOK = new TaskBuilder().withTaskName("Cook Paella")
            .withDescription("Cook Paella for 4 people tonight")
            .withDeadline("11-04-2016").withPriority("5").build();
    public static final ReadOnlyTask DATE = new TaskBuilder().withTaskName("Date with Lucy")
            .withDescription("Sunday, 10am at Central Park")
            .withDeadline("21-05-2015").withPriority("5").build();

    public static final ReadOnlyTask ESCAPE = new TaskBuilder().withTaskName("Escape dungeon")
            .withDescription("Escape dungeon group formation")
            .withDeadline("04-04-2017").withPriority("1").build();
    public static final ReadOnlyTask FREE = new TaskBuilder().withTaskName("Free memory space")
            .withDescription("Implement new version of free()")
            .withDeadline("21-08-2019").withPriority("2").build();
    public static final ReadOnlyTask GRADLE = new TaskBuilder().withTaskName("Resolve gradle")
            .withDescription("Resolve gradle problems when building project")
            .withDeadline("06-06-2016").withPriority("5").build();

    // Manually added - Task's details found in {@code CommandTestUtil}
    public static final ReadOnlyTask PENCIL = new TaskBuilder().withTaskName(VALID_TASK_NAME_PENCIL)
            .withDescription(VALID_DESCRIPTION_PENCIL)
            .withDeadline(VALID_DEADLINE_PENCIL).withPriority(VALID_PRIORITY_PENCIL).build();
    public static final ReadOnlyTask PAPER = new TaskBuilder().withTaskName(VALID_TASK_NAME_PAPER)
            .withDescription(VALID_DESCRIPTION_PAPER)
            .withDeadline(VALID_DEADLINE_PAPER).withPriority(VALID_PRIORITY_PAPER).build();

    private TypicalTasks() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalTasksOnlyAddressBook() {
        AddressBook ab = new AddressBook();
        for (ReadOnlyTask task : getTypicalTasks()) {
            try {
                ab.addTask(task);
            } catch (DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
        return ab;
    }


    public static List<ReadOnlyTask> getTypicalTasks() {
        return new ArrayList<>(Arrays.asList(ACCEPT, BUY, COOK, DATE, ESCAPE, FREE, GRADLE));
    }
}
```
