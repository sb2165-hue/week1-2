import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class week1andweek2 {

    // Stores username -> userId
    private ConcurrentHashMap<String, Integer> userMap;

    // Tracks username attempt frequency
    private ConcurrentHashMap<String, Integer> attemptFrequency;

    public week1andweek2() {
        userMap = new ConcurrentHashMap<>();
        attemptFrequency = new ConcurrentHashMap<>();
    }

    // Register a user
    public void registerUser(String username, int userId) {
        userMap.put(username.toLowerCase(), userId);
    }

    // Check availability in O(1)
    public boolean checkAvailability(String username) {
        username = username.toLowerCase();

        // Track attempt frequency
        attemptFrequency.merge(username, 1, Integer::sum);

        return !userMap.containsKey(username);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        username = username.toLowerCase();

        // Append numbers
        for (int i = 1; i <= 5; i++) {
            String candidate = username + i;
            if (!userMap.containsKey(candidate)) {
                suggestions.add(candidate);
            }
        }

        // Replace underscore with dot
        if (username.contains("_")) {
            String candidate = username.replace("_", ".");
            if (!userMap.containsKey(candidate)) {
                suggestions.add(candidate);
            }
        }

        // Add random suffix
        String randomCandidate = username + new Random().nextInt(1000);
        if (!userMap.containsKey(randomCandidate)) {
            suggestions.add(randomCandidate);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String result = null;
        int max = 0;

        for (Map.Entry<String, Integer> entry : attemptFrequency.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                result = entry.getKey();
            }
        }

        return result + " (" + max + " attempts)";
    }

    // Demo
    public static void main(String[] args) {
        week1andweek2 checker = new week1andweek2();

        // Preload some users
        checker.registerUser("john_doe", 1);
        checker.registerUser("admin", 2);

        // Availability check
        System.out.println(checker.checkAvailability("john_doe"));   // false
        System.out.println(checker.checkAvailability("jane_smith")); // true

        // Suggestions
        System.out.println(checker.suggestAlternatives("john_doe"));

        // Simulate attempts
        for (int i = 0; i < 100; i++) {
            checker.checkAvailability("admin");
        }

        System.out.println(checker.getMostAttempted());
    }
}