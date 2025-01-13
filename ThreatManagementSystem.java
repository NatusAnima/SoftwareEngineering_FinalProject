
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ThreatManagementSystem {
    private List<Citizen> citizens;
    private List<Facility> facilities;
    private Manager manager;

    public ThreatManagementSystem(Manager manager) {
        this.citizens = new ArrayList<>();
        this.facilities = new ArrayList<>();
        this.manager = manager;
    }

    // Start periodic threat level updates

    // Refresh threat levels for all citizens
    public void refreshThreatLevels() {
        for (Citizen citizen : citizens) {
            citizen.updateThreatLevel();
        }
    }

    public double getStaticThreshold() {
        return 350.0; // Static threat level threshold example
    }

    // Assign a citizen to the best available facility
    public void assignCitizenToFacility(Citizen citizen) {
        for (Facility facility : facilities) {
            if (facility.addCitizen(citizen)) {
                System.out.println("Citizen " + citizen.getName() + " assigned to facility " + facility.getName());
                return;
            }
        }
        System.out.println("No available space for citizen " + citizen.getName());
    }

    // Export the history of all citizens to a file
    public void exportCitizenHistory(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write("Citizen History Export - " + LocalDateTime.now() + "\n");
            for (Citizen citizen : citizens) {
                writer.write("Citizen Name: " + citizen.getName() + ", Citizen ID: " + citizen.getId() + ", Threat Level: " + citizen.getThreatLevel() + ", In Detention: " + citizen.isInDetention() + "\n");
            }
        }
    }

    // Add a new citizen to the system
    public void addCitizen(Citizen citizen) {
        citizens.add(citizen);
        refreshThreatLevels();
        manageDetention(); // ביצוע איוס מחדש של הכלואים
        System.out.println("Citizen " + citizen.getName() + " (ID: " + citizen.getId() + ") added to the system.");
    }



   public void removeCitizen(Citizen citizen) {
    if (citizens.remove(citizen)) {
        citizen.setInDetention(false);
        System.out.println("Citizen " + citizen.getName() + " removed from the system.");
        manageDetention(); // Reassess detention after removal
    } else {
        System.out.println("Citizen " + citizen.getName() + " does not exist in the system.");
    }
}
   public void addFacility(Facility facility) {
	    facilities.add(facility);
	    manageDetention(); // Reassign citizens after adding a new facility
	}
   
   public void removeFacility(Facility facility) {
	    if (!facilities.remove(facility)) {
	        System.out.println("Facility " + facility.getName() + " does not exist in the system.");
	        return;
	    }

	    System.out.println("Facility " + facility.getName() + " removed.");

	    // Handle detained citizens in the removed facility
	    List<Citizen> detainedCitizens = facility.getDetainedCitizens();
	    for (Citizen citizen : detainedCitizens) {
	        citizen.setInDetention(false); // Release them temporarily
	        assignCitizenToFacility(citizen); // Try reassigning to another facility
	    }

	    // Reassess detention for all citizens
	    manageDetention();
	}

   
    // Print all citizens in descending order of threat level
    public void printAllCitizensInThreatLevelOrder() {
        citizens.stream()
                .sorted(Comparator.comparingDouble(Citizen::getThreatLevel).reversed())
                .forEach(citizen -> System.out.println("Name: " + citizen.getName() + ", ID: " + citizen.getId() + ", Threat Level: " + citizen.getThreatLevel()));
    }

 // Print all facilities with detailed information
    public void printAllFacilities() {
        facilities.forEach(facility -> {
            double occupancyRate = ((double) facility.getCurrentOccupancy() / facility.getCapacity()) * 100;
            System.out.printf(
                "Facility Name: %s (ID: %s) | Capacity: %d | Occupancy: %d/%d (%.2f%%)%n",
                facility.getName(),
                facility.getFacilityId(),
                facility.getCapacity(),
                facility.getCurrentOccupancy(),
                facility.getCapacity(),
                occupancyRate
            );
        });
    }


    // Get a citizen by name
    public Citizen getCitizenByName(String name) {
        for (Citizen citizen : citizens) {
            if (citizen.getName().equals(name)) {
                return citizen;
            }
        }
        return null;
    }

    // Get a facility by name
    public Facility getFacilityByName(String name) {
        for (Facility facility : facilities) {
            if (facility.getName().equals(name)) {
                return facility;
            }
        }
        return null;
    }

    // Search for a citizen by ID
    public Citizen findCitizenById(String id) {
        if (id == null || !id.matches("\\d+")) {
            System.out.println("\u05E9\u05D2\u05D9\u05D0\u05D4: \u05DE\u05D6\u05D4\u05D4 \u05D7\u05D9\u05D9\u05D1 \u05DC\u05D4\u05D9\u05D5\u05EA \u05DE\u05D5\u05E8\u05DB\u05D1 \u05DE\u05DE\u05E1\u05E4\u05E8\u05D9\u05DD \u05D1\u05DC\u05D1\u05D3. \u05D7\u05D6\u05D5\u05E8 \u05E9\u05E0\u05D9\u05EA.");
            return null;
        }
        return citizens.stream()
                .filter(citizen -> id.equals(citizen.getId()))
                .findFirst()
                .orElse(null);
    }

    // Getter and setters
    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
//////////////////////////// פונקציה החשובה בקוד
    public void manageDetention() {
        List<Citizen> sortedCitizens = getSortedCitizensByThreat(); // מיון אזרחים לפי רמת איום
        double dynamicThreshold = areFacilitiesFull() ? getDynamicThresholdForAllFacilities() : getStaticThreshold(); // קביעת רף דינמי או סטטי

        for (Citizen citizen : sortedCitizens) {
            // עדכון הרף הדינמי לאחר כל שינוי במצבת הכלואים
            dynamicThreshold = areFacilitiesFull() ? getDynamicThresholdForAllFacilities() : getStaticThreshold();

            if (citizen.isInDetention()) {
         
                continue;
            }
            // עצירה אם האזרח מתחת לרף
            if (citizen.getThreatLevel() < dynamicThreshold) {
               
                break;
            }

            // ניסיון להוסיף את האזרח למתקן עם אחוז התפוסה הנמוך ביותר
            boolean assigned = tryAssignCitizenToFacility(citizen);
            if (!assigned) {
                System.out.println("Citizen " + citizen.getName() + " could not be assigned to any facility.");
                break; // אין מקום ואין אפשרות להחליף
            }
        }
    }

   public double getDynamicThresholdForAllFacilities() {
	    return facilities.stream()
	            .flatMap(facility -> facility.getDetainedCitizens().stream()) // שילוב כל הכלואים מכל המתקנים
	            .mapToDouble(Citizen::getThreatLevel) // המרת רמת האיום ל-double
	            .min() // מציאת הערך המינימלי
	            .orElse(getStaticThreshold()); // אם אין כלואים, חזור לרף הסטטי
	}

    private List<Citizen> getSortedCitizensByThreat() {
        return citizens.stream()
                .sorted(Comparator.comparingDouble(Citizen::getThreatLevel).reversed())
                .collect(Collectors.toList());
    }

 

    private boolean tryAssignCitizenToFacility(Citizen citizen) {
        // ניסיון להוסיף למתקן עם אחוז התפוסה הנמוך ביותר
        Facility facility = getFacilityWithLowestOccupancy();
        if (facility != null && facility.addCitizen(citizen)) {
            citizen.setInDetention(true);
            facility.refreshFacilityData();
            System.out.println("Citizen " + citizen.getName() + " assigned to facility " + facility.getName());
            return true;
        }

        // אם המתקנים מלאים, ניסיון להחליף אזרח עם רמת איום נמוכה יותר
        return replaceCitizenInFacility(citizen);
    }


    private boolean replaceCitizenInFacility(Citizen newCitizen) {
    for (Facility facility : facilities) {
        Citizen citizenToReplace = facility.getDetainedCitizens().stream()
                .min(Comparator.comparingDouble(Citizen::getThreatLevel))
                .orElse(null);

        if (citizenToReplace != null && newCitizen.getThreatLevel() > citizenToReplace.getThreatLevel()) {
            facility.removeCitizen(citizenToReplace); // הסרת האזרח הקיים
            citizenToReplace.setInDetention(false); // עדכון סטטוס מעצר

            boolean added = facility.addCitizen(newCitizen); // הוספת האזרח החדש
            if (added) {
                newCitizen.setInDetention(true); // עדכון סטטוס מעצר
                facility.refreshFacilityData(); // רענון נתוני המתקן
                System.out.println("Replaced citizen " + citizenToReplace.getName() +
                        " with citizen " + newCitizen.getName() + " in facility " + facility.getName());
                return true;
            }
        }
    }
    return false; // לא ניתן להחליף
}

    // Check if all facilities are full
    public boolean areFacilitiesFull() {
        return facilities.stream().allMatch(f -> f.getCurrentOccupancy() == f.getCapacity());
    }

    // Check if any facility is not full
    public boolean areFacilitiesNotFull() {
        return facilities.stream().anyMatch(f -> f.getCurrentOccupancy() < f.getCapacity());
    }

    // Get the dynamic threshold based on facility's current occupancy
    public double getDynamicThreshold(Facility facility) {
        return facility.getDetainedCitizens().stream()
                .min(Comparator.comparingDouble(Citizen::getThreatLevel))
                .map(Citizen::getThreatLevel)
                .orElse(0.0);
    }

    // Get facility with the lowest occupancy
    public Facility getFacilityWithLowestOccupancy() {
        return facilities.stream()
                .min(Comparator.comparingDouble(f -> (double) f.getCurrentOccupancy() / f.getCapacity()))
                .orElse(null);
    }
}
