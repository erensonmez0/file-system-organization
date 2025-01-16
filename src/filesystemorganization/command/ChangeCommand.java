package filesystemorganization.command;

import filesystemorganization.model.FileSystemOrganization;
import filesystemorganization.model.document.Document;

/**
 * This command changes the access number of a specific document within a loaded input file with given entry ID number.
 *
 * @author ujxbs
 */
final class ChangeCommand implements Command {

    private static final int ENTRY_ID_INDEX = 0;
    private static final int DOCUMENT_IDENTIFIER_INDEX = 1;
    private static final int NEW_ACCESS_COUNT_INDEX = 2;
    private static final int NUMBER_OF_ARGUMENTS = 3;
    private static final String SUCCESS_FORMAT = "Change %d to %d for %s";
    private static final String INVALID_ENTRY = "An entry with given id number does not exist.";
    private static final String INVALID_ACCESS_COUNT = "An access count can only be a non negative integer value.";
    private static final String INVALID_ID_OR_NUMBER_VALUE = "given access number or id can only be an integer value.";
    private static final String INVALID_IDENTIFIER = "A document with given identifier does not exist in given id's entry.";
    private static final int LOWEST_ACCESS_COUNT = 0;

    @Override
    public CommandResult execute(FileSystemOrganization model, String[] commandArguments) {
        String documentIdentifier = commandArguments[DOCUMENT_IDENTIFIER_INDEX];

        int entryId;
        int newAccessCount;
        try {
            entryId = Integer.parseInt(commandArguments[ENTRY_ID_INDEX]);
            newAccessCount = Integer.parseInt(commandArguments[NEW_ACCESS_COUNT_INDEX]);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ID_OR_NUMBER_VALUE);
        }

        if (model.entryDoesNotExist(entryId)) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ENTRY);
        }

        if (newAccessCount < LOWEST_ACCESS_COUNT) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ACCESS_COUNT);
        }

        Document wantedDocument;
        if (model.findDocument(entryId, documentIdentifier) == null) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_IDENTIFIER);
        } else {
            wantedDocument = model.findDocument(entryId, documentIdentifier);
        }

        int oldAccessCount = wantedDocument.getAccessCount();
        wantedDocument.setAccessCount(newAccessCount);

        return new CommandResult(CommandResultType.SUCCESS, SUCCESS_FORMAT.formatted(oldAccessCount, newAccessCount, documentIdentifier));
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }
}
