package filesystemorganization.model.document;

import filesystemorganization.model.tag.BinaryTag;
import filesystemorganization.model.tag.Tag;

import java.util.Set;

/**
 * This class represents a program document, a special type of document.
 *
 * @author ujxbs
 */
public class ProgramDocument extends Document {

    private static final String TAG_IDENTIFIER_EXECUTABLE = "executable";

    /**
     * Constructs a program document with document identifier, access count and tags.
     *
     * @param identifier    the identifier of the document
     * @param accessCount   the accessCount of the document
     * @param documentsTags the tags of the document
     */
    protected ProgramDocument(String identifier, int accessCount, Set<Tag> documentsTags) {
        super(identifier, accessCount, documentsTags);
        handleSpecialTags();
    }

    @Override
    public void handleSpecialTags() {
        for (Tag tag : getDocumentsTags()) {
            if (TAG_IDENTIFIER_EXECUTABLE.equalsIgnoreCase(tag.getIdentifier())) {
                setConflictExists();
                return;
            }
        }
        addTag(new BinaryTag(TAG_IDENTIFIER_EXECUTABLE));
    }

}
