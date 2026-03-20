import java.util.*;

public class week1andweek2 {

    // Transaction class
    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time; // in milliseconds

        public Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    private List<Transaction> transactions;

    public week1andweek2() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // 1. Classic Two-Sum
    public void findTwoSum(int target) {
        HashMap<Integer, Transaction> map = new HashMap<>();

        System.out.println("Two-Sum Results:");
        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                Transaction t2 = map.get(complement);
                System.out.println("(" + t2.id + ", " + t.id + ")");
            }
            map.put(t.amount, t);
        }
    }

    // 2. Two-Sum within 1 hour
    public void findTwoSumWithTime(int target) {
        HashMap<Integer, List<Transaction>> map = new HashMap<>();

        System.out.println("\nTwo-Sum within 1 hour:");
        for (Transaction t : transactions) {
            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                for (Transaction prev : map.get(complement)) {
                    if (Math.abs(t.time - prev.time) <= 3600000) { // 1 hour
                        System.out.println("(" + prev.id + ", " + t.id + ")");
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
    }

    // 3. K-Sum (recursive)
    public void findKSum(int k, int target) {
        System.out.println("\nK-Sum Results:");
        kSumHelper(0, k, target, new ArrayList<>());
    }

    private void kSumHelper(int start, int k, int target, List<Integer> path) {
        if (k == 0 && target == 0) {
            System.out.println(path);
            return;
        }

        if (k == 0 || target < 0) return;

        for (int i = start; i < transactions.size(); i++) {
            path.add(transactions.get(i).id);
            kSumHelper(i + 1, k - 1,
                    target - transactions.get(i).amount, path);
            path.remove(path.size() - 1);
        }
    }

    // 4. Duplicate Detection
    public void detectDuplicates() {
        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {
            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        System.out.println("\nDuplicate Transactions:");
        for (String key : map.keySet()) {
            List<Transaction> list = map.get(key);

            Set<String> accounts = new HashSet<>();
            for (Transaction t : list) {
                accounts.add(t.account);
            }

            if (accounts.size() > 1 && list.size() > 1) {
                System.out.println(key + " → Accounts: " + accounts);
            }
        }
    }

    // Main method
    public static void main(String[] args) {

        week1andweek2 system = new week1andweek2();

        // Sample transactions
        system.addTransaction(new Transaction(1, 500, "StoreA", "acc1", System.currentTimeMillis()));
        system.addTransaction(new Transaction(2, 300, "StoreB", "acc2", System.currentTimeMillis()));
        system.addTransaction(new Transaction(3, 200, "StoreC", "acc3", System.currentTimeMillis()));
        system.addTransaction(new Transaction(4, 500, "StoreA", "acc2", System.currentTimeMillis()));

        // Run features
        system.findTwoSum(500);
        system.findTwoSumWithTime(500);
        system.findKSum(3, 1000);
        system.detectDuplicates();
    }
}