package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PASSWORD;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import com.google.common.hash.Hashing;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.UserPrefs;

//@@author charlesgoh
/**
 * Sets lock in model to true or false, which allows or restricts the usage of all commands
 */
public class LockCommand extends Command {

    public static final String COMMAND_WORD = "lock";
    public static final String COMMAND_ALIAS = "lk";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Locks the addressbook. No commands can be executed\n"
            + "Parameters: "
            + PREFIX_PASSWORD + "PASSWORD";

    public static final String MESSAGE_SUCCESS = "Address++ locked successfully";
    public static final String MESSAGE_PASSWORD_INCORRECT = "Password is incorrect. Please try again";

    private final Logger logger = LogsCenter.getLogger(UnlockCommand.class);

    private String password;

    public LockCommand(String password) {
        this.password = password;
    }

    /**
     * Checks if input password matches the one saved in user prefs.
     */
    private boolean isPasswordCorrect() {
        UserPrefs userPrefs;
        try {
            userPrefs = storage.readUserPrefs().get();
            String hashedPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8).toString();
            if (hashedPassword.equals(userPrefs.getAddressBookEncryptedPassword())) {
                return true;
            } else {
                return false;
            }
        } catch (DataConversionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public CommandResult execute() {
        if (isPasswordCorrect()) {
            // Case where password is correct

            // Access model to lock
            model.lockAddressBook();

            // Logs current state
            logger.info("Lock state is now: " + Boolean.toString(model.getLockState()));

            //Return command result
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            // Case where password is incorrect
            return new CommandResult(MESSAGE_PASSWORD_INCORRECT);
        }
    }
}