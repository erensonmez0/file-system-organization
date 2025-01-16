package filesystemorganization.model.document;

import filesystemorganization.model.tag.Tag;

import java.util.Set;

/**
 * This class represents an image document, a special type of document.
 *
 * @author ujxbs
 */
public class ImageDocument extends Document {

    private static final String TAG_IDENTIFIER_SIZE = "Size";
    private static final String TAG_IDENTIFIER_IMAGE_SIZE = "ImageSize";
    private static final String TAG_VALUE_ICON = "icon";
    private static final String TAG_VALUE_SMALL = "small";
    private static final String TAG_VALUE_MEDIUM = "medium";
    private static final String TAG_VALUE_LARGE = "large";
    private static final int ICON_SIZE_LIMIT = 10000;
    private static final int SMALL_SIZE_LIMIT = 40000;
    private static final int MEDIUM_SIZE_LIMIT = 800000;

    /**
     * Constructs an image document with document identifier, access count and tags.
     *
     * @param identifier    the identifier of the document
     * @param accessCount   the accessCount of the document
     * @param documentsTags the tags of the document
     */
    protected ImageDocument(String identifier, int accessCount, Set<Tag> documentsTags) {
        super(identifier, accessCount, documentsTags);
        handleSpecialTags();
    }

    @Override
    public void handleSpecialTags() {
        for (Tag tag : getDocumentsTags()) {
            if (TAG_IDENTIFIER_SIZE.equalsIgnoreCase(tag.getIdentifier()) && tag.getValue().matches(NUMERIC_VALUE_PATTERN)) {
                int sizeValue = Integer.parseInt(tag.getValue());

                if (tagExists(TAG_IDENTIFIER_IMAGE_SIZE)) {
                    setConflictExists();
                    return;
                }

                tag.setIdentifier(TAG_IDENTIFIER_IMAGE_SIZE);

                if (sizeValue < ICON_SIZE_LIMIT) {
                    tag.setValue(TAG_VALUE_ICON);
                } else if (sizeValue < SMALL_SIZE_LIMIT) {
                    tag.setValue(TAG_VALUE_SMALL);
                } else if (sizeValue < MEDIUM_SIZE_LIMIT) {
                    tag.setValue(TAG_VALUE_MEDIUM);
                } else {
                    tag.setValue(TAG_VALUE_LARGE);
                }
            }
        }
    }
}
