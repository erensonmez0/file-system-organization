package filesystemorganization.model;

import filesystemorganization.model.document.Document;
import filesystemorganization.model.document.DocumentFactory;
import filesystemorganization.model.document.DocumentType;
import filesystemorganization.model.tag.BinaryTag;
import filesystemorganization.model.tag.MultivaluedTag;
import filesystemorganization.model.tag.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;


/**
 * This class represents the facade of file system organization.
 *
 * @author ujxbs
 */
public class FileSystemOrganization {

    private static final int DOCUMENT_IDENTIFIER_INDEX = 0;
    private static final int DOCUMENT_TYPE_INDEX = 1;
    private static final int ACCESS_COUNT_INDEX = 2;
    private static final int TAGS_STARTING_INDEX = 3;
    private static final String DOCUMENTS_ELEMENTS_SEPARATOR = ",";
    private static final String TAG_IDENTIFIER_VALUE_SEPARATOR = "=";
    private static final int TAG_IDENTIFIER_INDEX = 0;
    private static final int TAG_VALUE_INDEX = 1;
    private final DocumentFactory documentFactory;
    private final Map<Integer, List<Document>> inputFiles = new HashMap<>();
    private int idCounter = 0;

    /**
     * Constructs an instance of file system organization.
     */
    public FileSystemOrganization() {
        documentFactory = new DocumentFactory();
    }

    /**
     * Loads all the documents of an input file to the system.
     *
     * @param documentsList the list of the documents which are going to be added
     * @return true if all the documents were successfully added, false otherwise
     */
    public boolean loadDocuments(List<String> documentsList) {
        List<Document> documentList = new ArrayList<>();

        for (String document : documentsList) {
            String[] elements = document.split(DOCUMENTS_ELEMENTS_SEPARATOR);
            String identifier = elements[DOCUMENT_IDENTIFIER_INDEX];
            DocumentType documentType = DocumentType.valueOf(elements[DOCUMENT_TYPE_INDEX].toUpperCase().trim());
            int accessCount = Integer.parseInt(elements[ACCESS_COUNT_INDEX].trim());
            Set<Tag> documentTags = new HashSet<>();

            for (int i = TAGS_STARTING_INDEX; i < elements.length; i++) {
                String tagEntry = elements[i];
                if (tagEntry.contains(TAG_IDENTIFIER_VALUE_SEPARATOR)) {
                    String[] multivaluedTagElements = tagEntry.split(TAG_IDENTIFIER_VALUE_SEPARATOR);
                    String tagIdentifier = multivaluedTagElements[TAG_IDENTIFIER_INDEX];
                    String tagValue = multivaluedTagElements[TAG_VALUE_INDEX];

                    documentTags.add(new MultivaluedTag(tagIdentifier, tagValue));
                } else {
                    documentTags.add(new BinaryTag(tagEntry));
                }
            }

            Document newDocument = documentFactory.createDocument(identifier, documentType, accessCount, documentTags);
            if (newDocument.causesConflict()) {
                return false;
            }
            documentList.add(newDocument);
        }
        inputFiles.put(idCounter, documentList);
        idCounter++;
        return true;
    }


    /**
     * Returns the ID counter of the entries.
     *
     * @return the ID counter
     */
    public int getIdCounter() {
        return idCounter;
    }

    /**
     * Checks if an entry with given entry ID exists.
     *
     * @param entryId entry ID
     * @return true if an entry with given entry ID was found, false otherwise
     */
    public boolean entryDoesNotExist(int entryId) {
        return !inputFiles.containsKey(entryId);
    }

    /**
     * Finds a specific document with given entry ID and document identifier.
     *
     * @param entryId            entry ID of the needed document
     * @param documentIdentifier identifier of the needed document
     * @return the needed document with given entry ID and document identifier
     */
    public Document findDocument(int entryId, String documentIdentifier) {
        List<Document> documentsEntry = inputFiles.get(entryId);

        for (Document neededDocument : documentsEntry) {
            if (neededDocument.getIdentifier().equals(documentIdentifier)) {
                return neededDocument;
            }
        }
        return null;
    }

    /**
     * Returns the map containing all input files loaded into the system.
     *
     * @return the map containing all input files loaded into the system
     */
    public Map<Integer, List<Document>> getInputFiles() {
        return new HashMap<>(inputFiles);
    }

    /**
     * Returns the list of documents of the input file with given entry ID.
     *
     * @param entryId entry ID
     * @return the list of documents of the input file with given entry ID
     */
    public List<Document> getSpecificInputFile(int entryId) {
        return this.inputFiles.get(entryId);
    }
}
