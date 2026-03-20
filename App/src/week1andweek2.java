import java.util.*;

public class week1andweek2 {

    // Stores username -> userId
    private HashMap<String, Integer> userMap;

    // Stores username -> number of attempts
    private HashMap<String, Integer> attemptCount;

    // Constructor
    public week1andweek2() {
        userMap = new HashMap<>();
        attemptCount = new HashMap<>();
    }

    // Add existing user
    public void addUser(String username, int userId) {
        userMap.put(username, userId);
    }

    // Check availability in O(1)
    public boolean checkAvailability(String username) {
        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);
        return !userMap.containsKey(username);
    }

    // Suggest alternatives
    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();

        if (!userMap.containsKey(username)) {
            suggestions.add(username);
            return suggestions;
        }

        // Add numbers
        for (int i = 1; i <= 5; i++) {
            String newName = username + i;
            if (!userMap.containsKey(newName)) {
                suggestions.add(newName);
            }
        }

        // Replace underscore with dot
        if (username.contains("_")) {
            String dotName = username.replace("_", ".");
            if (!userMap.containsKey(dotName)) {
                suggestions.add(dotName);
            }
        }

        // Prefix and suffix
        String prefix = "the_" + username;
        if (!userMap.containsKey(prefix)) {
            suggestions.add(prefix);
        }

        String suffix = username + "_official";
        if (!userMap.containsKey(suffix)) {
            suggestions.add(suffix);
        }

        return suggestions;
    }

    // Get most attempted username
    public String getMostAttempted() {
        String maxUser = "";
        int maxCount = 0;

        for (String user : attemptCount.keySet()) {
            int count = attemptCount.get(user);
            if (count > maxCount) {
                maxCount = count;
                maxUser = user;
            }
        }

        return maxUser + " (" + maxCount + " attempts)";
    }

    // Main method
    public static void main(String[] args) {
        week1andweek2 system = new week1andweek2();

        // Existing users
        system.addUser("john_doe", 1);
        system.addUser("admin", 2);

        // Availability check
        System.out.println(system.checkAvailability("john_doe"));   // false
        System.out.println(system.checkAvailability("jane_smith")); // true

        // Suggestions
        System.out.println(system.suggestAlternatives("john_doe"));

        // Simulate attempts
        system.checkAvailability("admin");
        system.checkAvailability("admin");
        system.checkAvailability("admin");

        // Most attempted
        System.out.println(system.getMostAttempted());
    }
}