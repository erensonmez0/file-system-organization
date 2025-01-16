package filesystemorganization.model.tag;

/**
 * This class represents a multivalued tag, a special type of tag that a document can have.
 *
 * @author ujxbs
 */
public class MultivaluedTag extends Tag {

    /**
     * Constructs a multivalued tag with given tag identifier and value.
     *
     * @param identifier the tag's identifier
     * @param value the tag's value
     */
    public MultivaluedTag(String identifier, String value) {
        super(identifier, value);
    }
}
