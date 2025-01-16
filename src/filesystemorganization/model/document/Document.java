package filesystemorganization.model.document;

import filesystemorganization.model.tag.MultivaluedTag;
import filesystemorganization.model.tag.Tag;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a document in an input file.
 *
 * @author ujxbs
 */
public abstract class Document {

    protected static final String NUMERIC_VALUE_PATTERN = "^-?\\d+$";
    private static final String TAG_VALUE_UNDEFINED = "undefined";
    private static final String TAG_VALUE_EXPLICIT_UNDEFINED = "explicitUndefined!";
    private final String identifier;
    private final Set<Tag> documentsTags;
    private int accessCount;
    private boolean conflictExists = false;

    /**
     * Constructs a document with document identifier, access count and tags.
     *
     * @param identifier    the identifier of the document
     * @param accessCount   the accessCount of the document
     * @param documentsTags the tags of the document
     */
    protected Document(String identifier, int accessCount, Set<Tag> documentsTags) {
        this.identifier = identifier;
        this.accessCount = accessCount;
        this.documentsTags = documentsTags;
        setExplicitUndefinedTags();
    }

    /**
     * Handles the special tags for specific documents.
     */
    protected abstract void handleSpecialTags();

    /**
     * Checks if a tag with given tag identifier exists.
     *
     * @param tagIdentifier the identifier of the tag
     * @return true if a tag with given tag identifier exists, false otherwise
     */
    protected boolean tagExists(String tagIdentifier) {
        for (Tag tag : this.documentsTags) {
            if (tag.getIdentifier().equalsIgnoreCase(tagIdentifier)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value of the tag with the given identifier. If the tag doesn't exist, adds the tag with
     * the tag value "undefined" to the document's tags list.
     *
     * @param tagIdentifier the identifier of the tag
     * @return the value of the tag with the given identifier
     */
    public String getTagValue(String tagIdentifier) {
        if (!tagExists(tagIdentifier)) {
            documentsTags.add(new MultivaluedTag(tagIdentifier, TAG_VALUE_UNDEFINED));
        } else {
            for (Tag tag : this.documentsTags) {
                if (tag.getIdentifier().equalsIgnoreCase(tagIdentifier)) {
                    return tag.getValue();
                }
            }
        }
        return TAG_VALUE_UNDEFINED;
    }

    /**
     * Checks if a conflict occurs after the handling for special tags.
     *
     * @return true if a conflict exists, false otherwise
     */
    public boolean causesConflict() {
        return conflictExists;
    }

    /**
     * Sets the value of conflictExists true due to a conflict that's occurred during the handling for special tags.
     */
    protected void setConflictExists() {
        this.conflictExists = true;
    }

    /**
     * Returns the identifier of the document.
     *
     * @return the identifier of the document
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the access count of the document.
     *
     * @return the access count of the document
     */
    public int getAccessCount() {
        return accessCount;
    }

    /**
     * Sets the access count of the document.
     *
     * @param newAccessCount the new access count of the document
     */
    public void setAccessCount(int newAccessCount) {
        this.accessCount = newAccessCount;
    }

    /**
     * Returns the tags of the document.
     *
     * @return the tags of the document
     */
    protected Set<Tag> getDocumentsTags() {
        return new HashSet<>(documentsTags);
    }

    /**
     * Adds a new tag to the document's tags list.
     *
     * @param newTag a new tag that's going to be added
     */
    protected void addTag(Tag newTag) {
        documentsTags.add(newTag);
    }

    /**
     * Returns the tag identifiers of all the tags that exist in the document.
     *
     * @return the tag identifiers of all the tags in the document
     */
    public Set<String> getTagIdentifiers() {
        Set<String> tagIdentifiers = new HashSet<>();
        for (Tag tag : this.documentsTags) {
            tagIdentifiers.add(tag.getIdentifier());
        }
        return tagIdentifiers;
    }

    private void setExplicitUndefinedTags() {
        for (Tag tag : documentsTags) {
            if (tag.getValue().equalsIgnoreCase(TAG_VALUE_UNDEFINED)) {
                tag.setValue(TAG_VALUE_EXPLICIT_UNDEFINED);
            }
        }
    }

    /**
     * Checks if a tag with the given tag identifier in the document has the same value with given tag value.
     *
     * @param tagIdentifier the tag identifier to check
     * @param tagValue      the tag value to check
     * @return true if a tag's value with the given tag identifier in the document is same with given tag value, false otherwise
     */
    public boolean containsTagWithValue(String tagIdentifier, String tagValue) {
        for (Tag tag : this.documentsTags) {
            if (tag.getIdentifier().equalsIgnoreCase(tagIdentifier) && tag.getValue().equals(tagValue)) {
                return true;
            }
        }
        return false;
    }
}
