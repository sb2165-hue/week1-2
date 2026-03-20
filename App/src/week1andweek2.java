import java.util.*;

public class week1andweek2 {

    // Token Bucket class
    class TokenBucket {
        int tokens;
        int maxTokens;
        long lastRefillTime; // in milliseconds
        int refillRate; // tokens per hour

        public TokenBucket(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }

        // Refill tokens based on time passed
        public void refill() {
            long now = System.currentTimeMillis();
            long elapsedTime = now - lastRefillTime;

            // Convert time to hours
            double hoursPassed = elapsedTime / (1000.0 * 60 * 60);

            int tokensToAdd = (int) (hoursPassed * refillRate);

            if (tokensToAdd > 0) {
                tokens = Math.min(maxTokens, tokens + tokensToAdd);
                lastRefillTime = now;
            }
        }
    }

    // clientId -> TokenBucket
    private HashMap<String, TokenBucket> clientBuckets;

    private final int MAX_REQUESTS = 1000;
    private final int REFILL_RATE = 1000; // per hour

    // Constructor
    public week1andweek2() {
        clientBuckets = new HashMap<>();
    }

    // Check rate limit
    public String checkRateLimit(String clientId) {

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(MAX_REQUESTS, REFILL_RATE));

        TokenBucket bucket = clientBuckets.get(clientId);

        // Refill tokens
        bucket.refill();

        if (bucket.tokens > 0) {
            bucket.tokens--;
            return "Allowed (" + bucket.tokens + " requests remaining)";
        } else {
            long now = System.currentTimeMillis();
            long nextRefill = bucket.lastRefillTime + (60 * 60 * 1000);
            long retryAfter = (nextRefill - now) / 1000;

            return "Denied (0 requests remaining, retry after "
                    + retryAfter + "s)";
        }
    }

    // Get current status
    public String getRateLimitStatus(String clientId) {

        if (!clientBuckets.containsKey(clientId)) {
            return "No data for client";
        }

        TokenBucket bucket = clientBuckets.get(clientId);
        bucket.refill();

        int used = MAX_REQUESTS - bucket.tokens;

        long resetTime = bucket.lastRefillTime + (60 * 60 * 1000);

        return "{used: " + used +
                ", limit: " + MAX_REQUESTS +
                ", reset: " + (resetTime / 1000) + "}";
    }

    // Main method
    public static void main(String[] args) {

        week1andweek2 limiter = new week1andweek2();

        String client = "abc123";

        // Simulate requests
        for (int i = 0; i < 5; i++) {
            System.out.println(limiter.checkRateLimit(client));
        }

        // Simulate limit exceed
        for (int i = 0; i < 1000; i++) {
            limiter.checkRateLimit(client);
        }

        System.out.println(limiter.checkRateLimit(client)); // should deny

        // Get status
        System.out.println(limiter.getRateLimitStatus(client));
    }
}