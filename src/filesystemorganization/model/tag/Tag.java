package filesystemorganization.model.tag;

/**
 * This class represents a tag that a document can have.
 *
 * @author ujxbs
 */
public abstract class Tag {
    private String identifier;
    private String value;

    /**
     * Constructs a tag with a tag identifier and a value.
     *
     * @param identifier the identifier of the tag
     * @param value      the value of the tag
     */
    protected Tag(String identifier, String value) {
        this.identifier = identifier;
        this.value = value;
    }

    /**
     * Returns the tag's identifier.
     *
     * @return the tag's identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the tag's identifier.
     *
     * @param identifier the tag's identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the tag's value.
     *
     * @return the tag's value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the tag's value.
     *
     * @param value the tag's value
     */
    public void setValue(String value) {
        this.value = value;
    }

}
