package filesystemorganization.model.document;

import filesystemorganization.model.tag.Tag;

import java.util.Set;

/**
 * This class represents a text document, a special type of document.
 *
 * @author ujxbs
 */
public class TextDocument extends Document {

    private static final String TAG_IDENTIFIER_WORDS = "Words";
    private static final String TAG_IDENTIFIER_TEXT_LENGTH = "TextLength";
    private static final String TAG_IDENTIFIER_GENRE = "Genre";
    private static final String TAG_IDENTIFIER_TEXT_GENRE = "TextGenre";
    private static final String TAG_VALUE_SHORT = "short";
    private static final String TAG_VALUE_MEDIUM = "medium";
    private static final String TAG_VALUE_LONG = "long";
    private static final int SHORT_LENGTH_LIMIT = 100;
    private static final int MEDIUM_LENGTH_LIMIT = 1000;

    /**
     * Constructs a text document with document identifier, access count and tags.
     *
     * @param identifier    the identifier of the document
     * @param accessCount   the accessCount of the document
     * @param documentsTags the tags of the document
     */
    protected TextDocument(String identifier, int accessCount, Set<Tag> documentsTags) {
        super(identifier, accessCount, documentsTags);
        handleSpecialTags();
    }

    @Override
    public void handleSpecialTags() {
        for (Tag tag : getDocumentsTags()) {
            if (TAG_IDENTIFIER_WORDS.equalsIgnoreCase(tag.getIdentifier()) && tag.getValue().matches(NUMERIC_VALUE_PATTERN)) {
                int lengthValue = Integer.parseInt(tag.getValue());

                if (tagExists(TAG_IDENTIFIER_TEXT_LENGTH)) {
                    setConflictExists();
                    return;
                }

                tag.setIdentifier(TAG_IDENTIFIER_TEXT_LENGTH);

                if (lengthValue < SHORT_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_SHORT);
                } else if (lengthValue < MEDIUM_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_MEDIUM);
                } else {
                    tag.setValue(TAG_VALUE_LONG);
                }
            }

            if (TAG_IDENTIFIER_GENRE.equalsIgnoreCase(tag.getIdentifier())) {
                if (tagExists(TAG_IDENTIFIER_TEXT_GENRE)) {
                    setConflictExists();
                    return;
                }
                tag.setIdentifier(TAG_IDENTIFIER_TEXT_GENRE);
            }
        }
    }

}
