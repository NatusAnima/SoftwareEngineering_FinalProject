import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Logger;

public class DataHandler {

    private static final Logger logger = Logger.getLogger(DataHandler.class.getName());
    private static final String DEFAULT_DATA_FOLDER = "Data";
    private static final String DATA_FOLDER = System.getProperty("data.folder", DEFAULT_DATA_FOLDER);

    static {
        // Ensure the "Data" folder exists
        File folder = new File(DATA_FOLDER);
        if (!folder.exists() && !folder.mkdir()) {
            logger.warning("Failed to create data folder: " + DATA_FOLDER);
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
    
    // Save managers to a file
    public static void saveManagerToFile(Manager manager, String filename) {
        File file = new File(DATA_FOLDER, filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));
             FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
             FileLock lock = channel.lock()) {

            writer.write(managerToFileString(manager));
            writer.newLine();
            logger.info("Manager saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.severe("Error saving manager: " + e.getMessage());
        }
    }

    // Load manager from a file
    public static Manager loadManagerFromFile(ThreatManagementSystem system, String filename) {
        File file = new File(DATA_FOLDER, filename);
        if (!file.exists()) {
            logger.warning("File not found: " + file.getAbsolutePath());
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true)) {

            String line = reader.readLine();
            if (line != null) {
                return managerFromFileString(line, system);
            }
        } catch (IOException e) {
            logger.severe("Error loading manager: " + e.getMessage());
        }
        return null;
    }

    // Save president to a file
    public static void savePresidentToFile(President president, String filename) {
        File file = new File(DATA_FOLDER, filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));
             FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
             FileLock lock = channel.lock()) {

            writer.write(presidentToFileString(president));
            writer.newLine();
            logger.info("President saved successfully to " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.severe("Error saving president: " + e.getMessage());
        }
    }

    // Load president from a file
    public static President loadPresidentFromFile(ThreatManagementSystem system, String filename) {
        File file = new File(DATA_FOLDER, filename);
        if (!file.exists()) {
            logger.warning("File not found: " + file.getAbsolutePath());
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             FileChannel channel = new RandomAccessFile(file, "rw").getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true)) {

            String line = reader.readLine();
            if (line != null) {
                return presidentFromFileString(line, system);
            }
        } catch (IOException e) {
            logger.severe("Error loading president: " + e.getMessage());
        }
        return null;
    }

    // Convert Manager to a file-friendly string
    private static String managerToFileString(Manager manager) {
        return String.format("%s,%s",
                manager.getUsername(),
                manager.getPassword());
    }

    // Parse Manager from a file string
    private static Manager managerFromFileString(String data, ThreatManagementSystem system) {
        String[] parts = data.split(",", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid manager data format: " + data);
        }
        return new Manager(parts[0], parts[1], system);
    }

    // Convert President to a file-friendly string
    private static String presidentToFileString(President president) {
        return String.format("%s,%s,%s",
                president.getUsername(),
                president.getPassword(),
                president.getKey());
    }

    // Parse President from a file string
    private static President presidentFromFileString(String data, ThreatManagementSystem system) {
        String[] parts = data.split(",", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid president data format: " + data);
        }
        return new President(parts[0], parts[1], parts[2], system);
    }
}
