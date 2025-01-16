package filesystemorganization.model.document;

import filesystemorganization.model.tag.Tag;

import java.util.Set;

/**
 * This factory class represents a document factory for creating documents of various types.
 *
 * @author ujxbs
 */
public class DocumentFactory {

    /**
     * Creates a document based on the given document type.
     *
     * @param identifier   the identifier of the document
     * @param documentType the type of the document
     * @param accessCount  the accessCount of the document
     * @param documentTags the tags of the document
     * @return a document instance of the specified type
     */
    public Document createDocument(String identifier, DocumentType documentType, int accessCount, Set<Tag> documentTags) {
        switch (documentType) {
            case IMAGE -> {
                return new ImageDocument(identifier, accessCount, documentTags);
            }
            case AUDIO -> {
                return new AudioDocument(identifier, accessCount, documentTags);
            }
            case VIDEO -> {
                return new VideoDocument(identifier, accessCount, documentTags);
            }
            case TEXT -> {
                return new TextDocument(identifier, accessCount, documentTags);
            }
            case PROGRAM -> {
                return new ProgramDocument(identifier, accessCount, documentTags);
            }
            default -> {
                return null;
            }
        }
    }
}
