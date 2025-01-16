public class AutosaveThread extends Thread {
    private final ThreatManagementSystem system;
    private volatile boolean running = true; // To control thread stopping
    private final int intervalMillis; // Interval for autosave in milliseconds

    public AutosaveThread(ThreatManagementSystem system, int intervalMillis) {
        this.system = system;
        this.intervalMillis = intervalMillis;
    }

    @Override
    public void run() {
        while (running) {
            try {
                // Save citizens, facilities, manager, and president
                DataHandler.saveCitizensToFile(system.getCitizens(), "citizen.txt");
                DataHandler.saveFacilitiesToFile(system.getFacilities(), "facilities.txt");
                if (system.getManager() != null) {
                    DataHandler.saveManagerToFile(system.getManager(), "manager.txt");
                }
                if (system.getPresident() != null) {
                    DataHandler.savePresidentToFile(system.getPresident(), "president.txt");
                }

                // Log autosave event
                System.out.println("Data autosaved at " + java.time.LocalTime.now());

                // Sleep for the specified interval
                Thread.sleep(intervalMillis);
            } catch (InterruptedException e) {
                // Handle interruption (could occur during system shutdown)
                running = false;
                System.out.println("Autosave thread interrupted");
            }
        }
    }

    // Method to stop the autosave thread
    public void stopAutosave() {
        running = false;
        this.interrupt(); // Interrupts the sleep if the thread is sleeping
    }
}
