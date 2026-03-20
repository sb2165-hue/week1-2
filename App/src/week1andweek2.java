import java.util.*;

public class week1andweek2 {

    // n-gram -> set of document IDs
    private HashMap<String, Set<String>> ngramIndex;

    // document -> list of n-grams
    private HashMap<String, List<String>> documentNgrams;

    private int N = 5; // size of n-gram (5-gram)

    // Constructor
    public week1andweek2() {
        ngramIndex = new HashMap<>();
        documentNgrams = new HashMap<>();
    }

    // Add document to database
    public void addDocument(String docId, String text) {
        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {
            ngramIndex.putIfAbsent(gram, new HashSet<>());
            ngramIndex.get(gram).add(docId);
        }
    }

    // Generate n-grams
    private List<String> generateNgrams(String text) {
        List<String> ngrams = new ArrayList<>();
        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder gram = new StringBuilder();
            for (int j = 0; j < N; j++) {
                gram.append(words[i + j]).append(" ");
            }
            ngrams.add(gram.toString().trim());
        }

        return ngrams;
    }

    // Analyze document for plagiarism
    public void analyzeDocument(String docId, String text) {
        List<String> newDocNgrams = generateNgrams(text);

        System.out.println("Extracted " + newDocNgrams.size() + " n-grams");

        HashMap<String, Integer> matchCount = new HashMap<>();

        // Find matching n-grams
        for (String gram : newDocNgrams) {
            if (ngramIndex.containsKey(gram)) {
                for (String existingDoc : ngramIndex.get(gram)) {
                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        // Calculate similarity
        for (String doc : matchCount.keySet()) {
            int matches = matchCount.get(doc);
            double similarity = (matches * 100.0) / newDocNgrams.size();

            System.out.println("→ Found " + matches +
                    " matching n-grams with \"" + doc + "\"");
            System.out.printf("→ Similarity: %.2f%% ", similarity);

            if (similarity > 60) {
                System.out.println("(PLAGIARISM DETECTED)");
            } else if (similarity > 15) {
                System.out.println("(suspicious)");
            } else {
                System.out.println("(safe)");
            }
        }
    }

    // Main method (testing)
    public static void main(String[] args) {
        week1andweek2 system = new week1andweek2();

        // Add existing documents
        system.addDocument("essay_089.txt",
                "data structures and algorithms are important in computer science");

        system.addDocument("essay_092.txt",
                "machine learning and data structures are important in computer science");

        // Analyze new document
        String newEssay = "data structures and algorithms are important in computer science and machine learning";

        system.analyzeDocument("essay_123.txt", newEssay);
    }
}