package filesystemorganization.command;

import filesystemorganization.model.FileSystemOrganization;
import filesystemorganization.model.document.DocumentType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * This command loads and reads an input file, that includes documents, and stores its content.
 *
 * @author ujxbs
 */
final class LoadCommand implements Command {

    private static final int PATH_INDEX = 0;
    private static final int NUMBER_OF_ARGUMENTS = 1;
    private static final int IDENTIFIER_ENTRY_INDEX = 0;
    private static final int LAST_INDEX_DIFFERENCE = 1;
    private static final int DOCUMENT_TYPE_INDEX = 1;
    private static final int ACCESS_COUNT_INDEX = 2;
    private static final int TAG_IDENTIFIER_INDEX = 0;
    private static final int TAG_VALUE_INDEX = 1;
    private static final int NUMBER_OF_NON_TAG_ENTRIES = 3;
    private static final String DIRECTORY_SEPARATOR = "/";
    private static final String DOCUMENTS_ELEMENTS_SEPARATOR = ",";
    private static final String ILLEGAL_IDENTIFIER_SEPARATOR = " ";
    private static final String NON_EXISTENT_FILE = "File %s does not exist!";
    private static final String INVALID_DOCUMENT_ENTRY = "Invalid number of entries for a document!";
    private static final String INVALID_DOCUMENT_IDENTIFIER = "Invalid identifier for a document!";
    private static final String INVALID_DOCUMENT_TYPE = "Invalid type for a document!";
    private static final String ERROR_DUPLICATE_IDENTIFIER = "Tag identifiers must be unique!";
    private static final String INVALID_TAG_IDENTIFIER = "Invalid identifier for a tag!";
    private static final String TAG_IDENTIFIER_VALUE_SEPARATOR = "=";
    private static final String INVALID_TAG_VALUE = "Invalid value for a multivalued or numeric tag!";
    private static final String TAG_CONFLICT_ERROR = "A conflict arises with the added tags of a document!";
    private static final String EMPTY_FILE_ERROR = "File %s is empty!";
    private static final String TAG_IDENTIFIER_PATTERN = "^[a-zA-Z][a-zA-Z0-9]*$";
    private static final String MULTIVALUED_VALUE_PATTERN = "^[a-zA-Z][a-zA-Z0-9 ]*$";
    private static final String NUMERIC_VALUE_PATTERN = "^-?\\d+$";
    private static final String ACCESS_COUNT_PATTERN = "[1-9]\\d*";
    private static final String SUCCESS_FORMAT = "Loaded %s with id: %d%n";
    private static final String INVALID_ACCESS_COUNT = "Total number of accesses can only be a non-negative integer value!";
    private final List<String> documentsList = new ArrayList<>();
    private final Set<String> binaryTags = new HashSet<>();
    private final Set<String> multivaluedTags = new HashSet<>();

    @Override
    public CommandResult execute(FileSystemOrganization model, String[] commandArguments) {
        String pathName = commandArguments[PATH_INDEX];
        Path pathToFile = Paths.get(pathName);
        if (!Files.exists(pathToFile)) {
            return new CommandResult(CommandResultType.FAILURE, String.format(NON_EXISTENT_FILE, getFileName(pathName)));
        }
        try {
            documentsList.addAll(Files.readAllLines(pathToFile));
            if (documentsList.isEmpty()) {
                return new CommandResult(CommandResultType.FAILURE, String.format(EMPTY_FILE_ERROR, getFileName(pathName)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String document : documentsList) {
            CommandResult checkResult = checkDocument(document);
            if (checkResult != null) {
                return checkResult;
            }
        }

        return processDocuments(model, documentsList, pathName);
    }

    @Override
    public int getNumberOfArguments() {
        return NUMBER_OF_ARGUMENTS;
    }

    private CommandResult checkDocument(String document) {
        String[] elements = document.split(DOCUMENTS_ELEMENTS_SEPARATOR);
        if (elements.length < NUMBER_OF_NON_TAG_ENTRIES) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_DOCUMENT_ENTRY);
        }

        if (!elements[ACCESS_COUNT_INDEX].matches(ACCESS_COUNT_PATTERN)) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_ACCESS_COUNT);
        }

        if (elements[IDENTIFIER_ENTRY_INDEX].contains(ILLEGAL_IDENTIFIER_SEPARATOR)) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_DOCUMENT_IDENTIFIER);
        }

        if (!isValidDocumentType(elements[DOCUMENT_TYPE_INDEX])) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_DOCUMENT_TYPE);
        }

        List<String> tagEntries = Arrays.stream(elements)
                .skip(NUMBER_OF_NON_TAG_ENTRIES)
                .collect(Collectors.toList());

        return isValidTag(tagEntries);
    }

    private CommandResult processDocuments(FileSystemOrganization model, List<String> documentsList, String pathName) {
        int inputId = model.getIdCounter();
        if (!model.loadDocuments(documentsList)) {
            return new CommandResult(CommandResultType.FAILURE, TAG_CONFLICT_ERROR);
        }

        String successMessage = String.format(SUCCESS_FORMAT, pathName, inputId)
                + String.join(System.lineSeparator(), documentsList);

        this.documentsList.clear();
        binaryTags.clear();
        multivaluedTags.clear();
        return new CommandResult(CommandResultType.SUCCESS, successMessage);
    }

    private String getFileName(String fullPath) {
        String[] parts = fullPath.split(DIRECTORY_SEPARATOR);
        return parts[parts.length - LAST_INDEX_DIFFERENCE];
    }

    private boolean isValidDocumentType(String givenDocumentType) {
        for (DocumentType documentType : DocumentType.values()) {
            if (documentType.name().toLowerCase().equals(givenDocumentType)) {
                return true;
            }
        }
        return false;
    }

    private CommandResult isValidTag(List<String> tagEntries) {
        Set<String> uniqueTag = new HashSet<>();

        for (String tagEntry : tagEntries) {
            if (tagEntry.contains(TAG_IDENTIFIER_VALUE_SEPARATOR)) {
                String[] multivaluedTagElements = tagEntry.split(TAG_IDENTIFIER_VALUE_SEPARATOR);
                String tagIdentifier = multivaluedTagElements[TAG_IDENTIFIER_INDEX].toLowerCase();
                String tagValue = multivaluedTagElements[TAG_VALUE_INDEX];

                if (!tagIdentifier.matches(TAG_IDENTIFIER_PATTERN)) {
                    return new CommandResult(CommandResultType.FAILURE, INVALID_TAG_IDENTIFIER);
                }

                boolean isMultivaluedValid = tagValue.matches(MULTIVALUED_VALUE_PATTERN);
                boolean isNumericValid = tagValue.matches(NUMERIC_VALUE_PATTERN);

                if (!(isMultivaluedValid || isNumericValid)) {
                    return new CommandResult(CommandResultType.FAILURE, INVALID_TAG_VALUE);
                }

                if (!uniqueTag.add(tagIdentifier)) {
                    return new CommandResult(CommandResultType.FAILURE, ERROR_DUPLICATE_IDENTIFIER);
                }

                if (binaryTags.contains(tagIdentifier)) {
                    return new CommandResult(CommandResultType.FAILURE, ERROR_DUPLICATE_IDENTIFIER);
                }

                multivaluedTags.add(tagIdentifier);

            } else {
                String tagIdentifier = tagEntry.toLowerCase();

                if (!tagIdentifier.matches(TAG_IDENTIFIER_PATTERN)) {
                    return new CommandResult(CommandResultType.FAILURE, INVALID_TAG_IDENTIFIER);
                }

                if (!uniqueTag.add(tagIdentifier)) {
                    return new CommandResult(CommandResultType.FAILURE, ERROR_DUPLICATE_IDENTIFIER);
                }

                if (multivaluedTags.contains(tagIdentifier)) {
                    return new CommandResult(CommandResultType.FAILURE, ERROR_DUPLICATE_IDENTIFIER);
                }

                binaryTags.add(tagIdentifier);
            }
        }
        return null;
    }
}
