import java.io.IOException;

public class President extends Manager {
    private String key; // Unique key for the president
    private int failedAttempts; // Counter for failed key attempts
    private static final int MAX_ATTEMPTS = 3; // Maximum allowed attempts

    // Constructor
    public President(String username, String password, String key, ThreatManagementSystem threatManagementSystem) {
        super(username, password, threatManagementSystem);
        this.key = key;
        this.failedAttempts = 0;
    }
 // Grant pardon to a citizen
    public void grantPardon(Citizen citizen) {
        boolean isPardoned = false; // משתנה לסימון אם האזרח חנון

        // מציאת המתקן שבו האזרח כלוא ושחרורו
        for (Facility facility : getThreatManagementSystem().getFacilities()) {
            if (facility.getDetainedCitizens().contains(citizen)) {
                boolean removed = facility.removeCitizen(citizen); // הסרת האזרח מבית הכלא
                if (removed) {
                    citizen.setInDetention(false); // עדכון סטטוס האזרח
                    citizen.updateThreatLevelToMinimum(); // שינוי מדד האיום ל-1
                    System.out.println("Citizen " + citizen.getName() + " has been pardoned and released from facility " + facility.getName() + ".");
                    isPardoned = true;
                    break; // אין צורך להמשיך לחפש
                }
            }
        }

        // אם האזרח לא נמצא במעצר
        if (!isPardoned) {
            System.out.println("Citizen " + citizen.getName() + " is not currently detained.");
            return;
        }

        // הפעלת manageDetention לעדכון מצב הכלואים
        getThreatManagementSystem().manageDetention();
    }
    public void emergencyDetention(Citizen citizen) {
        if (citizen.isInDetention()) {
            System.out.println("Citizen " + citizen.getName() + " (ID: " + citizen.getId() + ") is already detained.");
            return;
        }

        citizen.setThreatLevelToMax(); // העלת מדד האיום למקסימום
        System.out.println("Emergency detention initiated for Citizen " + citizen.getName() + " (ID: " + citizen.getId() + ").");

        // הפעלת manageDetention כדי לעדכן את מצב הכלואים
        getThreatManagementSystem().manageDetention();
    }

    // Authenticate the president with key
    public boolean verifyCredentials(String username, String password, String key) {
        if (this.failedAttempts >= MAX_ATTEMPTS) {
            System.out.println("Account locked due to too many failed attempts.");
            return false;
        }

        if (super.verifyCredentials(username, password) && this.key.equals(key)) {
            this.failedAttempts = 0; // Reset failed attempts on success
            return true;
        } else {
            this.failedAttempts++;
            System.out.println("Invalid key. Attempts left: " + (MAX_ATTEMPTS - this.failedAttempts));
            return false;
        }
    }

    // Getters and setters for key
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // Override some manager actions with president's authority
    @Override
    public void removeCitizen(Citizen citizen) {
        getThreatManagementSystem().removeCitizen(citizen);
        System.out.println("Citizen " + citizen.getName() + " removed by president.");
    }

    @Override
    public void addFacility(Facility facility) {
        getThreatManagementSystem().addFacility(facility);
        System.out.println("Facility " + facility.getName() + " added by president.");
    }

    @Override
    public void exportCitizenHistory(String fileName) {
        try {
            getThreatManagementSystem().exportCitizenHistory(fileName);
            System.out.println("Citizen history exported to " + fileName + " by president.");
        } catch (IOException e) {
            System.out.println("Error exporting citizen history: " + e.getMessage());
        }
    }

 
}
