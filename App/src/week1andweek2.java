import java.util.*;

public class week1andweek2 {

    // pageUrl -> total visit count
    private HashMap<String, Integer> pageVisits;

    // pageUrl -> unique users
    private HashMap<String, Set<String>> uniqueVisitors;

    // source -> count
    private HashMap<String, Integer> trafficSources;

    // Constructor
    public week1andweek2() {
        pageVisits = new HashMap<>();
        uniqueVisitors = new HashMap<>();
        trafficSources = new HashMap<>();
    }

    // Process incoming event
    public void processEvent(String url, String userId, String source) {

        // Update page visit count
        pageVisits.put(url, pageVisits.getOrDefault(url, 0) + 1);

        // Update unique visitors
        uniqueVisitors.putIfAbsent(url, new HashSet<>());
        uniqueVisitors.get(url).add(userId);

        // Update traffic source count
        trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
    }

    // Get Top 10 pages using PriorityQueue (Max Heap)
    private List<Map.Entry<String, Integer>> getTopPages() {

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());

        pq.addAll(pageVisits.entrySet());

        List<Map.Entry<String, Integer>> topPages = new ArrayList<>();

        int count = 0;
        while (!pq.isEmpty() && count < 10) {
            topPages.add(pq.poll());
            count++;
        }

        return topPages;
    }

    // Display dashboard
    public void getDashboard() {

        System.out.println("\n===== REAL-TIME DASHBOARD =====");

        // Top Pages
        System.out.println("\nTop Pages:");
        List<Map.Entry<String, Integer>> topPages = getTopPages();

        int rank = 1;
        for (Map.Entry<String, Integer> entry : topPages) {
            String url = entry.getKey();
            int visits = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + visits + " views (" + unique + " unique)");
            rank++;
        }

        // Traffic Sources
        System.out.println("\nTraffic Sources:");
        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (String source : trafficSources.keySet()) {
            int count = trafficSources.get(source);
            double percent = (count * 100.0) / total;

            System.out.printf("%s: %.2f%%\n", source, percent);
        }

        System.out.println("================================\n");
    }

    // Simulate real-time updates every 5 seconds
    public void startDashboard() {
        while (true) {
            getDashboard();
            try {
                Thread.sleep(5000); // 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        week1andweek2 system = new week1andweek2();

        // Simulated events
        system.processEvent("/article/breaking-news", "user_123", "google");
        system.processEvent("/article/breaking-news", "user_456", "facebook");
        system.processEvent("/sports/championship", "user_789", "direct");
        system.processEvent("/article/breaking-news", "user_123", "google");
        system.processEvent("/sports/championship", "user_111", "google");
        system.processEvent("/tech/ai", "user_222", "direct");
        system.processEvent("/tech/ai", "user_333", "facebook");

        // Start dashboard (updates every 5 sec)
        system.startDashboard();
    }
}