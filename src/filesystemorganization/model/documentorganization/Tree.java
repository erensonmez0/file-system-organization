package filesystemorganization.model.documentorganization;

import filesystemorganization.model.document.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * This class represents a tree that organizes, sorts and calculates the documents and their tags with their information gains.
 *
 * @author ujxbs
 */
public class Tree {
    private static final String TAG_VALUE_SEPARATOR = "=";
    private static final String TAG_SEPARATOR = "/";
    private static final String STARTING_TAG_IDENTIFIER = "";
    private static final double STARTING_INFORMATION_GAIN = 0;
    private static final double INFORMATION_GAIN_LIMIT = 0.001;
    private static final char DOCUMENT_IDENTIFIER_AFFIX = '"';
    private static final String LAST_TWO_DIGIT_FORMAT = "%.2f";
    private static final String TAG_VALUE_UNDEFINED = "undefined";
    private static final String TAG_VALUE_EXPLICIT_UNDEFINED = "explicitUndefined!";
    private final Map<String, Double> tagsInformationGainList = new HashMap<>();
    private final List<String> sortedTagsInformationGains = new ArrayList<>();
    private final List<String> sortedDocuments = new ArrayList<>();

    /**
     * Builds a tree structure that organizes the documents and their tags.
     *
     * @param structuring automatic structuring for a specific input file of the file organisation system
     */
    protected void buildTree(AutomaticStructuring structuring) {
        branchTree(structuring.getInputFile(), structuring, STARTING_TAG_IDENTIFIER);
    }

    private void branchTree(List<Document> documents, AutomaticStructuring structuring, String tagSoFar) {
        if (documents.isEmpty()) {
            return;
        }

        Set<String> tags = structuring.getUniqueTags(documents);
        String tagWithHighestIG = STARTING_TAG_IDENTIFIER;
        double highestInformationGain = STARTING_INFORMATION_GAIN;

        // Information gains get calculated
        for (String tag : tags) {
            double informationGain = structuring.calculateInformationGain(documents, tag);
            if (informationGain >= INFORMATION_GAIN_LIMIT) {
                this.tagsInformationGainList.put(tag, informationGain);
                if (informationGain > highestInformationGain) {
                    highestInformationGain = informationGain;
                    tagWithHighestIG = tag;
                }
            }
        }

        List<String> sortedIGList = sortInformationGains(this.tagsInformationGainList);
        for (String igValue : sortedIGList) {
            this.sortedTagsInformationGains.add(tagSoFar + TAG_SEPARATOR + igValue);
        }

        this.tagsInformationGainList.clear();
        if (highestInformationGain < INFORMATION_GAIN_LIMIT) {
            List<String> documentIdentifiers = documents.stream()
                    .map(Document::getIdentifier)
                    .sorted()
                    .toList();

            for (String identifier : documentIdentifiers) {
                this.sortedDocuments.add(tagSoFar + TAG_SEPARATOR
                        + DOCUMENT_IDENTIFIER_AFFIX + identifier + DOCUMENT_IDENTIFIER_AFFIX);
            }
            return;
        }

        Set<String> uniqueValues = new HashSet<>();
        for (Document document : documents) {
            String value = structuring.tagsValueFinder(tagWithHighestIG, document.getIdentifier());
            uniqueValues.add(value);
        }
        List<String> sortedValues = structuring.sortTagValues(tagWithHighestIG, uniqueValues, documents);

        // New branches for each unique tag value get created recursively
        for (String value : sortedValues) {
            String tagValue;
            if (value.equalsIgnoreCase(TAG_VALUE_EXPLICIT_UNDEFINED)) {
                tagValue = TAG_VALUE_UNDEFINED;
            } else {
                tagValue = value;
            }
            Node childNode = new Node(tagWithHighestIG.toLowerCase() + TAG_VALUE_SEPARATOR + tagValue);
            List<Document> subset = structuring.subsetGivenTag(documents, tagWithHighestIG, value);
            branchTree(subset, structuring, tagSoFar + TAG_SEPARATOR + childNode.nodeIdentifier());
        }
    }

    private List<String> sortInformationGains(Map<String, Double> tagsIGList) {
        List<Map.Entry<String, Double>> sortedInformationGains = new ArrayList<>(tagsIGList.entrySet());
        sortedInformationGains.sort((entry1, entry2) -> {
            int valueComparison = entry2.getValue().compareTo(entry1.getValue());
            if (valueComparison == 0) {
                return entry1.getKey().compareToIgnoreCase(entry2.getKey());
            }
            return valueComparison;
        });

        List<String> sortedTags = new ArrayList<>();
        for (Map.Entry<String, Double> tag : sortedInformationGains) {
            String formattedValue = String.format(LAST_TWO_DIGIT_FORMAT, tag.getValue());
            String tagsDisplay = tag.getKey().toLowerCase() + TAG_VALUE_SEPARATOR + formattedValue;
            sortedTags.add(tagsDisplay);
        }
        return sortedTags;
    }

    /**
     * Returns the information gains of the tags in a sorted way.
     *
     * @return the information gains of the tags
     */
    protected List<String> getSortedTagsInformationGains() {
        return new ArrayList<>(this.sortedTagsInformationGains);
    }

    /**
     * Returns the documents in a sorted way.
     *
     * @return the documents
     */
    protected List<String> getSortedDocuments() {
        return new ArrayList<>(this.sortedDocuments);
    }
}
