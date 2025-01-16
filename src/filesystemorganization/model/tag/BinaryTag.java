package filesystemorganization.model.tag;

/**
 * This class represents a binary tag, a special type of tag that a document can have.
 *
 * @author ujxbs
 */
public class BinaryTag extends Tag {

    private static final String TAG_VALUE_DEFINED = "defined";

    /**
     * Constructs a binary tag with given tag identifier.
     *
     * @param identifier the tag's identifier
     */
    public BinaryTag(String identifier) {
        super(identifier, TAG_VALUE_DEFINED);
    }
}
