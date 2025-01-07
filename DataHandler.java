import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class DataHandler {

    private static final String DATA_FOLDER = "Data";

    static {
        // Ensure the "Data" folder exists
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    // Save citizens to a file
    public static void saveCitizensToFile(List<Citizen> citizens, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FOLDER + File.separator + filename))) {
            for (Citizen citizen : citizens) {
                writer.write(citizenToFileString(citizen));
                writer.newLine();
            }
            System.out.println("Citizens saved successfully to " + DATA_FOLDER + File.separator + filename);
        } catch (IOException e) {
            System.out.println("Error saving citizens: " + e.getMessage());
        }
    }

    // Load citizens from a file
    public static void loadCitizensFromFile(ThreatManagementSystem system, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FOLDER + File.separator + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Citizen citizen = citizenFromFileString(line);
                system.addCitizen(citizen);
            }
            System.out.println("Citizens loaded successfully from " + DATA_FOLDER + File.separator + filename);
        } catch (IOException e) {
            System.out.println("Error loading citizens: " + e.getMessage());
        }
    }

    // Save facilities to a file
    public static void saveFacilitiesToFile(List<Facility> facilities, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FOLDER + File.separator + filename))) {
            for (Facility facility : facilities) {
                writer.write(facilityToFileString(facility));
                writer.newLine();
            }
            System.out.println("Facilities saved successfully to " + DATA_FOLDER + File.separator + filename);
        } catch (IOException e) {
            System.out.println("Error saving facilities: " + e.getMessage());
        }
    }

    // Load facilities from a file
    public static void loadFacilitiesFromFile(ThreatManagementSystem system, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FOLDER + File.separator + filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Facility facility = facilityFromFileString(line);
                system.addFacility(facility);
            }
            System.out.println("Facilities loaded successfully from " + DATA_FOLDER + File.separator + filename);
        } catch (IOException e) {
            System.out.println("Error loading facilities: " + e.getMessage());
        }
    }

    // Save citizen history to a file
    public static void saveCitizenHistoryToFile(String history, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FOLDER + File.separator + filename))) {
            writer.write(history);
            System.out.println("Citizen history saved successfully to " + DATA_FOLDER + File.separator + filename);
        } catch (IOException e) {
            System.out.println("Error saving citizen history: " + e.getMessage());
        }
    }

    // Convert Citizen to a file-friendly string
    private static String citizenToFileString(Citizen citizen) {
        return String.format("%s,%s,%s,%d,%d,%d,%d,%b,%s",
                citizen.getName(),
                citizen.getBirthDate(),
                citizen.getOrigin(),
                citizen.getEconomicPercentile(),
                citizen.getGovernmentSupportLevel(),
                citizen.getPublicImpactScore(),
                citizen.getThreatLevel(),
                citizen.isInDetention(),
                citizen.getId());
    }

    // Parse Citizen from a file string
    private static Citizen citizenFromFileString(String data) {
        String[] parts = data.split(",");
        return new Citizen(
                parts[0],
                LocalDate.parse(parts[1]),
                parts[2],
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4]),
                Integer.parseInt(parts[5]),
                Boolean.parseBoolean(parts[6]),
                parts[7]
        );
    }

    // Convert Facility to a file-friendly string
    private static String facilityToFileString(Facility facility) {
        return String.format("%s,%s,%d",
                facility.getFacilityId(),
                facility.getName(),
                facility.getCapacity());
    }

    // Parse Facility from a file string
    private static Facility facilityFromFileString(String data) {
        String[] parts = data.split(",");
        return new Facility(parts[0], parts[1], Integer.parseInt(parts[2]));
    }
}
