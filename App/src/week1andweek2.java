import java.util.*;

public class week1andweek2 {

    // Parking spot structure
    class ParkingSpot {
        String licensePlate;
        long entryTime;
        String status; // EMPTY, OCCUPIED, DELETED

        public ParkingSpot() {
            status = "EMPTY";
        }
    }

    private ParkingSpot[] table;
    private int capacity = 500;
    private int size = 0;

    // Statistics
    private int totalProbes = 0;
    private int totalParks = 0;

    // Constructor
    public week1andweek2() {
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String licensePlate) {
        int hash = 0;
        for (char ch : licensePlate.toCharArray()) {
            hash = (hash * 31 + ch) % capacity;
        }
        return hash;
    }

    // Park vehicle using linear probing
    public void parkVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].status.equals("OCCUPIED")) {
            index = (index + 1) % capacity; // linear probing
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        size++;
        totalProbes += probes;
        totalParks++;

        System.out.println("Assigned spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {
            if (table[index].status.equals("OCCUPIED") &&
                    table[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = hours * 5.0; // $5 per hour

                table[index].status = "DELETED";
                size--;

                System.out.printf("Spot #%d freed, Duration: %.2f hours, Fee: $%.2f\n",
                        index, hours, fee);
                return;
            }

            index = (index + 1) % capacity;
            probes++;
        }

        System.out.println("Vehicle not found");
    }

    // Find nearest available spot from entrance (index 0)
    public int findNearestSpot() {
        for (int i = 0; i < capacity; i++) {
            if (!table[i].status.equals("OCCUPIED")) {
                return i;
            }
        }
        return -1;
    }

    // Get statistics
    public void getStatistics() {
        double occupancy = (size * 100.0) / capacity;
        double avgProbes = totalParks == 0 ? 0 : (double) totalProbes / totalParks;

        System.out.println("\n=== Parking Statistics ===");
        System.out.printf("Occupancy: %.2f%%\n", occupancy);
        System.out.printf("Average Probes: %.2f\n", avgProbes);
        System.out.println("Peak Hour: 2-3 PM (simulated)");
    }

    // Main method
    public static void main(String[] args) {

        week1andweek2 parking = new week1andweek2();

        parking.parkVehicle("ABC-1234");
        parking.parkVehicle("ABC-1235");
        parking.parkVehicle("XYZ-9999");

        parking.exitVehicle("ABC-1234");

        System.out.println("Nearest free spot: #" + parking.findNearestSpot());

        parking.getStatistics();
    }
}