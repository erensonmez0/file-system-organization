package filesystemorganization.command;

import filesystemorganization.model.FileSystemOrganization;

/**
 * This interface represents an executable command.
 *
 * @author Programmieren-Team
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param model            the model to execute the command on
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    CommandResult execute(FileSystemOrganization model, String[] commandArguments);

    /**
     * Returns the number of arguments that the command expects.
     *
     * @return the number of arguments that the command expects
     */
    int getNumberOfArguments();
}

