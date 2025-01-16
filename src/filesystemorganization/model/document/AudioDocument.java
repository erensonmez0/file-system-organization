package filesystemorganization.model.document;

import filesystemorganization.model.tag.Tag;
import java.util.Set;

/**
 * This class represents an audio document, a special type of document.
 *
 * @author ujxbs
 */
public class AudioDocument extends Document {

    private static final String TAG_IDENTIFIER_LENGTH = "Length";
    private static final String TAG_IDENTIFIER_AUDIO_LENGTH = "AudioLength";
    private static final String TAG_IDENTIFIER_GENRE = "Genre";
    private static final String TAG_IDENTIFIER_AUDIO_GENRE = "AudioGenre";
    private static final String TAG_VALUE_SAMPLE = "sample";
    private static final String TAG_VALUE_SHORT = "short";
    private static final String TAG_VALUE_NORMAL = "normal";
    private static final String TAG_VALUE_LONG = "long";
    private static final int SAMPLE_LENGTH_LIMIT = 10;
    private static final int SHORT_LENGTH_LIMIT = 60;
    private static final int NORMAL_LENGTH_LIMIT = 300;

    /**
     * Constructs an audio document with document identifier, access count and tags.
     *
     * @param identifier    the identifier of the document
     * @param accessCount   the accessCount of the document
     * @param documentsTags the tags of the document
     */
    protected AudioDocument(String identifier, int accessCount, Set<Tag> documentsTags) {
        super(identifier, accessCount, documentsTags);
        handleSpecialTags();
    }

    @Override
    public void handleSpecialTags() {
        for (Tag tag : getDocumentsTags()) {
            if (TAG_IDENTIFIER_LENGTH.equalsIgnoreCase(tag.getIdentifier()) && tag.getValue().matches(NUMERIC_VALUE_PATTERN)) {
                int lengthValue = Integer.parseInt(tag.getValue());

                if (tagExists(TAG_IDENTIFIER_AUDIO_LENGTH)) {
                    setConflictExists();
                    return;
                }

                tag.setIdentifier(TAG_IDENTIFIER_AUDIO_LENGTH);

                if (lengthValue < SAMPLE_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_SAMPLE);
                } else if (lengthValue < SHORT_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_SHORT);
                } else if (lengthValue < NORMAL_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_NORMAL);
                } else {
                    tag.setValue(TAG_VALUE_LONG);
                }
            }

            if (TAG_IDENTIFIER_GENRE.equalsIgnoreCase(tag.getIdentifier())) {
                if (tagExists(TAG_IDENTIFIER_AUDIO_GENRE)) {
                    setConflictExists();
                    return;
                }
                tag.setIdentifier(TAG_IDENTIFIER_AUDIO_GENRE);
            }
        }
    }
}
