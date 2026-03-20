import java.util.*;

public class week1andweek2 {

    // Trie Node
    class TrieNode {
        HashMap<Character, TrieNode> children;
        HashMap<String, Integer> queryMap; // query -> frequency

        public TrieNode() {
            children = new HashMap<>();
            queryMap = new HashMap<>();
        }
    }

    private TrieNode root;

    // Global query frequency
    private HashMap<String, Integer> frequencyMap;

    public week1andweek2() {
        root = new TrieNode();
        frequencyMap = new HashMap<>();
    }

    // Insert query into Trie
    public void insert(String query) {
        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);

        TrieNode node = root;

        for (char ch : query.toCharArray()) {
            node.children.putIfAbsent(ch, new TrieNode());
            node = node.children.get(ch);

            // Store query frequency at each prefix node
            node.queryMap.put(query, frequencyMap.get(query));
        }
    }

    // Get top 10 suggestions for prefix
    public List<String> search(String prefix) {
        TrieNode node = root;

        // Traverse prefix
        for (char ch : prefix.toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new ArrayList<>(); // no suggestions
            }
            node = node.children.get(ch);
        }

        // Min Heap for top 10
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> a.getValue() - b.getValue());

        for (Map.Entry<String, Integer> entry : node.queryMap.entrySet()) {
            pq.offer(entry);
            if (pq.size() > 10) {
                pq.poll(); // remove smallest
            }
        }

        // Extract results
        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll().getKey());
        }

        Collections.reverse(result); // highest first
        return result;
    }

    // Update frequency (new search)
    public void updateFrequency(String query) {
        insert(query);
    }

    // Simple typo suggestion (replace one character)
    public List<String> suggestTypos(String word) {
        List<String> suggestions = new ArrayList<>();

        for (String query : frequencyMap.keySet()) {
            if (isOneEditAway(word, query)) {
                suggestions.add(query);
            }
        }
        return suggestions;
    }

    // Check if two strings differ by one edit
    private boolean isOneEditAway(String a, String b) {
        if (Math.abs(a.length() - b.length()) > 1) return false;

        int i = 0, j = 0, count = 0;

        while (i < a.length() && j < b.length()) {
            if (a.charAt(i) != b.charAt(j)) {
                count++;
                if (count > 1) return false;

                if (a.length() > b.length()) i++;
                else if (a.length() < b.length()) j++;
                else {
                    i++;
                    j++;
                }
            } else {
                i++;
                j++;
            }
        }
        return true;
    }

    // Main method
    public static void main(String[] args) {

        week1andweek2 system = new week1andweek2();

        // Insert queries
        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");
        system.insert("java tutorial");
        system.insert("java tutorial");
        system.insert("java 21 features");

        // Search prefix
        System.out.println("Search results for 'jav':");
        List<String> results = system.search("jav");

        int rank = 1;
        for (String res : results) {
            System.out.println(rank + ". " + res + " (" +
                    system.frequencyMap.get(res) + " searches)");
            rank++;
        }

        // Update frequency
        system.updateFrequency("java 21 features");

        // Typo suggestions
        System.out.println("\nTypo suggestions for 'jvaa':");
        System.out.println(system.suggestTypos("jvaa"));
    }
}