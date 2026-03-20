import java.util.*;

public class week1andweek2 {

    // Video Data
    static class Video {
        String id;
        String content;

        public Video(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    // LRU Cache using LinkedHashMap
    class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private int capacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75f, true); // access-order
            this.capacity = capacity;
        }

        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // Caches
    private LRUCache<String, Video> L1; // memory
    private LRUCache<String, Video> L2; // SSD
    private HashMap<String, Video> L3;  // database

    // Access count for promotion
    private HashMap<String, Integer> accessCount;

    // Statistics
    private int L1Hits = 0, L2Hits = 0, L3Hits = 0, totalRequests = 0;

    // Constructor
    public week1andweek2() {
        L1 = new LRUCache<>(10000);
        L2 = new LRUCache<>(100000);
        L3 = new HashMap<>();
        accessCount = new HashMap<>();
    }

    // Simulate adding data to database
    public void addToDatabase(String id, String content) {
        L3.put(id, new Video(id, content));
    }

    // Get video
    public Video getVideo(String videoId) {
        totalRequests++;

        // L1 Check
        if (L1.containsKey(videoId)) {
            L1Hits++;
            System.out.println("L1 HIT (0.5ms)");
            return L1.get(videoId);
        }

        System.out.println("L1 MISS");

        // L2 Check
        if (L2.containsKey(videoId)) {
            L2Hits++;
            System.out.println("L2 HIT (5ms)");

            Video v = L2.get(videoId);

            // Promote to L1
            L1.put(videoId, v);
            System.out.println("Promoted to L1");

            return v;
        }

        System.out.println("L2 MISS");

        // L3 Check (Database)
        if (L3.containsKey(videoId)) {
            L3Hits++;
            System.out.println("L3 HIT (150ms)");

            Video v = L3.get(videoId);

            // Add to L2
            L2.put(videoId, v);

            accessCount.put(videoId,
                    accessCount.getOrDefault(videoId, 0) + 1);

            return v;
        }

        System.out.println("Video not found");
        return null;
    }

    // Invalidate cache when content updates
    public void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        L3.remove(videoId);
        accessCount.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    // Get statistics
    public void getStatistics() {
        double L1Rate = (L1Hits * 100.0) / totalRequests;
        double L2Rate = (L2Hits * 100.0) / totalRequests;
        double L3Rate = (L3Hits * 100.0) / totalRequests;

        System.out.println("\n=== Cache Statistics ===");
        System.out.printf("L1 Hit Rate: %.2f%%\n", L1Rate);
        System.out.printf("L2 Hit Rate: %.2f%%\n", L2Rate);
        System.out.printf("L3 Hit Rate: %.2f%%\n", L3Rate);

        double overall = ((L1Hits + L2Hits) * 100.0) / totalRequests;
        System.out.printf("Overall Hit Rate: %.2f%%\n", overall);
    }

    // Main method
    public static void main(String[] args) {

        week1andweek2 cache = new week1andweek2();

        // Add videos to database
        cache.addToDatabase("video_123", "Movie A");
        cache.addToDatabase("video_999", "Movie B");

        // Access videos
        cache.getVideo("video_123"); // L3 → L2
        cache.getVideo("video_123"); // L2 → L1
        cache.getVideo("video_123"); // L1 hit

        cache.getVideo("video_999"); // L3

        // Invalidate
        cache.invalidate("video_999");

        // Stats
        cache.getStatistics();
    }
}