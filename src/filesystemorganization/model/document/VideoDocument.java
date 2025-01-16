package filesystemorganization.model.document;

import filesystemorganization.model.tag.Tag;

import java.util.Set;

/**
 * This class represents a video document, a special type of document.
 *
 * @author ujxbs
 */
public class VideoDocument extends Document {

    private static final String TAG_IDENTIFIER_LENGTH = "Length";
    private static final String TAG_IDENTIFIER_VIDEO_LENGTH = "VideoLength";
    private static final String TAG_IDENTIFIER_GENRE = "Genre";
    private static final String TAG_IDENTIFIER_VIDEO_GENRE = "VideoGenre";
    private static final String TAG_VALUE_CLIP = "clip";
    private static final String TAG_VALUE_SHORT = "short";
    private static final String TAG_VALUE_MOVIE = "movie";
    private static final String TAG_VALUE_LONG = "long";
    private static final int CLIP_LENGTH_LIMIT = 300;
    private static final int SHORT_LENGTH_LIMIT = 3600;
    private static final int MOVIE_LENGTH_LIMIT = 7200;

    /**
     * Constructs a video document with document identifier, access count and tags.
     *
     * @param identifier    the identifier of the document
     * @param accessCount   the accessCount of the document
     * @param documentsTags the tags of the document
     */
    protected VideoDocument(String identifier, int accessCount, Set<Tag> documentsTags) {
        super(identifier, accessCount, documentsTags);
        handleSpecialTags();
    }

    @Override
    public void handleSpecialTags() {
        for (Tag tag : getDocumentsTags()) {
            if (TAG_IDENTIFIER_LENGTH.equalsIgnoreCase(tag.getIdentifier()) && tag.getValue().matches(NUMERIC_VALUE_PATTERN)) {
                int lengthValue = Integer.parseInt(tag.getValue());

                if (tagExists(TAG_IDENTIFIER_VIDEO_LENGTH)) {
                    setConflictExists();
                    return;
                }

                tag.setIdentifier(TAG_IDENTIFIER_VIDEO_LENGTH);

                if (lengthValue < CLIP_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_CLIP);
                } else if (lengthValue < SHORT_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_SHORT);
                } else if (lengthValue < MOVIE_LENGTH_LIMIT) {
                    tag.setValue(TAG_VALUE_MOVIE);
                } else {
                    tag.setValue(TAG_VALUE_LONG);
                }
            }

            if (TAG_IDENTIFIER_GENRE.equalsIgnoreCase(tag.getIdentifier())) {
                if (tagExists(TAG_IDENTIFIER_VIDEO_GENRE)) {
                    setConflictExists();
                    return;
                }
                tag.setIdentifier(TAG_IDENTIFIER_VIDEO_GENRE);
            }
        }
    }
}
