import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class week1andweek2 {

    // productId -> stock count
    private ConcurrentHashMap<String, Integer> stockMap;

    // productId -> waiting list (FIFO)
    private ConcurrentHashMap<String, Queue<Integer>> waitingListMap;

    public week1andweek2() {
        stockMap = new ConcurrentHashMap<>();
        waitingListMap = new ConcurrentHashMap<>();
    }

    // Add product with stock
    public void addProduct(String productId, int stock) {
        stockMap.put(productId, stock);
        waitingListMap.put(productId, new ConcurrentLinkedQueue<>());
    }

    // Check stock (O(1))
    public String checkStock(String productId) {
        int stock = stockMap.getOrDefault(productId, 0);
        return stock + " units available";
    }

    // Purchase item (thread-safe)
    public String purchaseItem(String productId, int userId) {

        // Ensure product exists
        stockMap.putIfAbsent(productId, 0);
        waitingListMap.putIfAbsent(productId, new ConcurrentLinkedQueue<>());

        synchronized (productId.intern()) {
            int currentStock = stockMap.get(productId);

            if (currentStock > 0) {
                stockMap.put(productId, currentStock - 1);
                return "Success, " + (currentStock - 1) + " units remaining";
            } else {
                Queue<Integer> queue = waitingListMap.get(productId);
                queue.add(userId);
                return "Added to waiting list, position #" + queue.size();
            }
        }
    }

    // View waiting list
    public List<Integer> getWaitingList(String productId) {
        Queue<Integer> queue = waitingListMap.get(productId);
        return new ArrayList<>(queue);
    }

    // Demo
    public static void main(String[] args) {
        week1andweek2 system = new week1andweek2();

        // Add product with 100 units
        system.addProduct("IPHONE15_256GB", 100);

        // Check stock
        System.out.println(system.checkStock("IPHONE15_256GB"));

        // Simulate purchases
        System.out.println(system.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(system.purchaseItem("IPHONE15_256GB", 67890));

        // Simulate stock exhaustion
        for (int i = 0; i < 100; i++) {
            system.purchaseItem("IPHONE15_256GB", i);
        }

        // Now goes to waiting list
        System.out.println(system.purchaseItem("IPHONE15_256GB", 99999));

        // View waiting list
        System.out.println(system.getWaitingList("IPHONE15_256GB"));
    }
}