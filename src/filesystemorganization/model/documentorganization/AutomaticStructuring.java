package filesystemorganization.model.documentorganization;

import filesystemorganization.model.FileSystemOrganization;
import filesystemorganization.model.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * This class represents an automatic structuring for a specific input file.
 *
 * @author ujxbs
 */
public class AutomaticStructuring {
    private static final String INFORMATION_GAINS_DOCUMENTS_SEPARATOR = "---";
    private static final int LAST_INDEX_DIFFERENCE = 1;
    private static final int STARTING_COUNT = 0;
    private static final int DEFAULT_VALUE = 0;
    private static final double DEFAULT_LOG_BASE = 2;
    private final List<Document> inputFile = new ArrayList<>();
    private final Tree documentTree;

    /**
     * Constructs an automatic structuring with organization model and an entry ID.
     *
     * @param model   the model of file system organization
     * @param entryId entry ID
     */
    public AutomaticStructuring(FileSystemOrganization model, int entryId) {
        this.inputFile.addAll(model.getSpecificInputFile(entryId));
        this.documentTree = new Tree();
        buildDocumentTree();
    }

    /**
     * Finds the value of a specified tag for a document.
     *
     * @param tagIdentifier      the identifier of the tag
     * @param documentIdentifier the identifier of the document
     * @return the value of the tag, or null if the tag was not found
     */
    protected String tagsValueFinder(String tagIdentifier, String documentIdentifier) {
        for (Document document : this.inputFile) {
            if (document.getIdentifier().equals(documentIdentifier)) {
                return document.getTagValue(tagIdentifier);
            }
        }
        return null;
    }

    private int accessCountFinder(String documentIdentifier) {
        for (Document document : this.inputFile) {
            if (document.getIdentifier().equals(documentIdentifier)) {
                return document.getAccessCount();
            }
        }

        return DEFAULT_VALUE;
    }

    /**
     * Filters documents by a specific tag and tag value, and then returns a list of these documents.
     *
     * @param documents     the superset list of documents
     * @param tagIdentifier the tag identifier
     * @param tagValue      the tag value
     * @return a list of filtered subset documents
     */
    protected List<Document> subsetGivenTag(List<Document> documents, String tagIdentifier, String tagValue) {
        List<Document> wantedSubset = new ArrayList<>();
        for (Document document : documents) {
            if (tagsValueFinder(tagIdentifier, document.getIdentifier()).equals(tagValue)) {
                wantedSubset.add(document);
            }
        }
        return wantedSubset;
    }

    private double calculateDocumentAccessProbability(String targetDocumentIdentifier, List<Document> subsetList) {
        int totalSubsetValue = STARTING_COUNT;
        for (Document document : subsetList) {
            totalSubsetValue += accessCountFinder(document.getIdentifier());
        }
        if (totalSubsetValue == STARTING_COUNT) {
            return DEFAULT_VALUE;
        }
        return (double) accessCountFinder(targetDocumentIdentifier) / totalSubsetValue;
    }

    private double calculateUncertainty(List<Document> documentList) {
        double totalUncertaintyValue = STARTING_COUNT;
        for (Document document : documentList) {
            double probability = calculateDocumentAccessProbability(document.getIdentifier(), documentList);

            totalUncertaintyValue += probability * (Math.log(probability) / Math.log(DEFAULT_LOG_BASE));
        }
        return -totalUncertaintyValue;
    }

    private double calculateSubsetProbability(List<Document> documents, String tagIdentifier, String tagValue) {
        List<Document> subset = subsetGivenTag(documents, tagIdentifier, tagValue);
        double totalSubsetProbability = STARTING_COUNT;

        for (Document document : subset) {
            double documentProbability = calculateDocumentAccessProbability(document.getIdentifier(), documents);
            totalSubsetProbability += documentProbability;
        }
        return totalSubsetProbability;
    }

    private double calculateRemainingUncertainty(List<Document> documents, String tagIdentifier) {
        Set<String> uniqueTagValues = new HashSet<>();
        for (Document document : documents) {
            String tagValue = tagsValueFinder(tagIdentifier, document.getIdentifier());
            uniqueTagValues.add(tagValue);

        }

        double expectedRemainingUncertainty = STARTING_COUNT;
        for (String tagValue : uniqueTagValues) {
            List<Document> subset = subsetGivenTag(documents, tagIdentifier, tagValue);
            double subsetProbability = calculateSubsetProbability(documents, tagIdentifier, tagValue);
            double subsetUncertainty = calculateUncertainty(subset);

            expectedRemainingUncertainty += subsetProbability * subsetUncertainty;
        }
        return expectedRemainingUncertainty;
    }

    /**
     * Calculates the information gain of a specific tag.
     *
     * @param documents     the list of documents
     * @param tagIdentifier the tag identifier
     * @return the calculated information gain
     */
    protected double calculateInformationGain(List<Document> documents, String tagIdentifier) {
        return calculateUncertainty(documents) - calculateRemainingUncertainty(documents, tagIdentifier);
    }

    /**
     * Returns the unique tag identifiers.
     *
     * @param documents the list of documents
     * @return a set of unique tag identifiers
     */
    protected Set<String> getUniqueTags(List<Document> documents) {
        Set<String> uniqueTagIdentifiers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Document document : documents) {
            uniqueTagIdentifiers.addAll(document.getTagIdentifiers());
        }
        return uniqueTagIdentifiers;
    }

    /**
     * Returns the input file.
     *
     * @return the input file
     */
    protected List<Document> getInputFile() {
        return new ArrayList<>(inputFile);
    }

    private void buildDocumentTree() {
        documentTree.buildTree(this);
    }

    /**
     * Sorts the tag values by their information gains, if equal by their lexicographic order,
     * and returns a list of these values.
     *
     * @param tagIdentifier  tag identifier
     * @param uniqueValues   set of unique tag identifiers
     * @param documentSubset the superset list of documents
     * @return a list of sorted tag values
     */
    protected List<String> sortTagValues(String tagIdentifier, Set<String> uniqueValues, List<Document> documentSubset) {
        Map<String, Integer> tagValueCount = new HashMap<>();

        for (String tagValue : uniqueValues) {
            int totalCount = STARTING_COUNT;
            for (Document document : documentSubset) {
                if (document.containsTagWithValue(tagIdentifier, tagValue)) {
                    totalCount += document.getAccessCount();
                }
            }
            tagValueCount.put(tagValue, totalCount);
        }

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tagValueCount.entrySet());
        sortedEntries.sort((entry1, entry2) -> {
            int countCompare = entry2.getValue().compareTo(entry1.getValue());
            if (countCompare == STARTING_COUNT) {
                return entry1.getKey().compareTo(entry2.getKey());
            }
            return countCompare;
        });

        List<String> sortedTagValues = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sortedTagValues.add(entry.getKey());
        }

        return sortedTagValues;
    }

    private String printList(List<String> givenList) {
        StringBuilder documentsDisplay = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        for (int i = 0; i < givenList.size(); i++) {
            documentsDisplay.append(givenList.get(i));
            if (i < givenList.size() - LAST_INDEX_DIFFERENCE) {
                documentsDisplay.append(lineSeparator);
            }
        }

        return documentsDisplay.toString();
    }

    @Override
    public String toString() {
        StringBuilder documentsDisplay = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        String sortedTagsInformationGains = printList(documentTree.getSortedTagsInformationGains());
        String sortedDocuments = printList(documentTree.getSortedDocuments());

        if (!sortedTagsInformationGains.isEmpty()) {
            documentsDisplay.append(sortedTagsInformationGains)
                    .append(lineSeparator);
        }

        documentsDisplay.append(INFORMATION_GAINS_DOCUMENTS_SEPARATOR)
                .append(lineSeparator)
                .append(sortedDocuments);

        return documentsDisplay.toString();
    }
}

