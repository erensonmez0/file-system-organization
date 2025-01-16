package filesystemorganization.command;

import filesystemorganization.model.documentorganization.AutomaticStructuring;
import filesystemorganization.model.FileSystemOrganization;

/**
 * This command executes automatic structuring on a specific loaded content of an input file, lists a tags list sorted
 * by information gains and displays calculated directory tree of the documents in the file.
 *
 * @author ujxbs
 */
final class RunCommand implements Command {
    private static final int INPUT_FILE_INDEX = 0;
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final String INVALID_ID_VALUE = "Given id can only be an integer value!";
    private static final String INVALID_ENTRY = "An entry with given id number does not exist!";
    private static final String ERROR_NO_FILE_LOADED = "No file was loaded!";

    @Override
    public CommandResult execute(FileSystemOrganization model, String[] commandArguments) {
        int entryId;
        try {
            entryId = Integer.parseInt(commandArguments[INPUT_FILE_INDEX]);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ID_VALUE);
        }

        if (model.getInputFiles().isEmpty()) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_NO_FILE_LOADED);
        }

        if (model.entryDoesNotExist(entryId)) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ENTRY);
        }

        AutomaticStructuring structuring = new AutomaticStructuring(model, entryId);

        return new CommandResult(CommandResultType.SUCCESS, structuring.toString());
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }
}
