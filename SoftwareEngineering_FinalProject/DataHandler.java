import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataHandler {

    private static final Logger logger = Logger.getLogger(DataHandler.class.getName());
    private static final String DEFAULT_DATA_FOLDER = "Data";
    private static final String DATA_FOLDER = System.getProperty("data.folder", DEFAULT_DATA_FOLDER);

    static {
        // Ensure the "Data" folder exists
        try {
            Files.createDirectories(Paths.get(DATA_FOLDER));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create data folder: " + DATA_FOLDER, e);
        }
    }

    public static void saveCitizensToFile(List<Citizen> citizens, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            for (Citizen citizen : citizens) {
                writer.write(citizenToFileString(citizen));
                writer.newLine();
            }
            logger.info("Citizens saved successfully to " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving citizens to " + fullPath, e);
        }
    }

    public static List<Citizen> loadCitizensFromFile(ThreatManagementSystem system, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        List<Citizen> citizens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Citizen citizen = citizenFromFileString(line);
                    citizens.add(citizen);
                    system.addCitizen(citizen);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Invalid citizen data: " + line, e);
                }
            }
            logger.info("Citizens loaded successfully from " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading citizens from " + fullPath, e);
        }
        return citizens;
    }

    public static void saveFacilitiesToFile(List<Facility> facilities, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            for (Facility facility : facilities) {
                writer.write(facilityToFileString(facility));
                writer.newLine();
            }
            logger.info("Facilities saved successfully to " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving facilities to " + fullPath, e);
        }
    }
    public static List<Facility> loadFacilitiesFromFile(ThreatManagementSystem system, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        List<Facility> facilities = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Facility facility = facilityFromFileString(line, system);
                    facilities.add(facility);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.WARNING, "Invalid facility data: " + line, e);
                }
            }
            logger.info("Facilities loaded successfully from " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading facilities from " + fullPath, e);
        }
        return facilities;
    }
    public static void saveCitizenHistoryToFile(String history, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writer.write(history);
            logger.info("Citizen history saved successfully to " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving citizen history to " + fullPath, e);
        }
    }

    private static String citizenToFileString(Citizen citizen) {
        return String.format("%s,%s,%s,%d,%d,%d,%s,%s,%b", //%b for boolean
                citizen.getName(), citizen.getBirthDate(), citizen.getOrigin(),
                citizen.getEconomicPercentile(), citizen.getGovernmentSupportLevel(),
                citizen.getPublicImpactScore(), citizen.isInDetention(), citizen.getId(),
                citizen.calculateThreatLevel());
    }



    private static Citizen citizenFromFileString(String data) {
        try {
            String[] parts = data.split(",");
            if (parts.length != 9) {
                throw new IllegalArgumentException("Invalid citizen data format: " + data);
            }
            return new Citizen(parts[0], LocalDate.parse(parts[1]), parts[2],
                    Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                    Boolean.parseBoolean(parts[7]), parts[8]);
        } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid citizen data format: " + data, e);
        }
    }


    private static String facilityToFileString(Facility facility) {
        // Existing code ... (modify to include detained citizens)
        List<Citizen> detainedCitizens = facility.getDetainedCitizens();
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%s,%s,%d", facility.getFacilityId(), facility.getName(), facility.getCapacity()));
        builder.append(","); // separator
        for (Citizen citizen : detainedCitizens) {
            builder.append(citizen.getId()); // Assuming citizen ID is unique identifier
            builder.append(",");
        }
        return builder.toString();
    }

    private static Facility facilityFromFileString(String data, ThreatManagementSystem system) {
        // Existing code ... (modify to handle detained citizens)
        String[] parts = data.split(",");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid facility data format: " + data);
        }

        Facility facility = new Facility(parts[0], parts[1], Integer.parseInt(parts[2]));

        // Extract detained citizen IDs
        if (parts.length > 3) {
            List<Citizen> detainedCitizens = new ArrayList<>();
            for (int i = 3; i < parts.length; i++) {
                String citizenId = parts[i];
                Citizen citizen = system.findCitizenById(citizenId); // Assuming system can find citizen by ID
                if (citizen != null) {
                    detainedCitizens.add(citizen);
                } else {
                    logger.warning("Citizen with ID " + citizenId + " not found while loading facility");
                }
            }
            facility.setDetainedCitizens(detainedCitizens);
        }

        return facility;
    }

    
    
    public static void saveManagerToFile(Manager manager, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
             FileChannel channel = new RandomAccessFile(fullPath, "rw").getChannel();
             FileLock lock = channel.lock()) {
            writer.write(managerToFileString(manager));
            writer.newLine();
            logger.info("Manager saved successfully to " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving manager to " + fullPath, e);
        }
    }

    public static Manager loadManagerFromFile(ThreatManagementSystem system, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (RandomAccessFile raf = new RandomAccessFile(fullPath, "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
             BufferedReader reader = new BufferedReader(new FileReader(raf.getFD()))) {

            String line = reader.readLine();
            if (line != null) {
                return managerFromFileString(line, system);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading manager from " + fullPath, e);
        }
        return null;
    }

    public static void savePresidentToFile(President president, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath));
             FileChannel channel = new RandomAccessFile(fullPath, "rw").getChannel();
             FileLock lock = channel.lock()) {
            writer.write(presidentToFileString(president));
            writer.newLine();
            logger.info("President saved successfully to " + fullPath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving president to " + fullPath, e);
        }
    }

    public static President loadPresidentFromFile(ThreatManagementSystem system, String filename) {
        String fullPath = DATA_FOLDER + File.separator + filename;
        try (RandomAccessFile raf = new RandomAccessFile(fullPath, "rw");
             FileChannel channel = raf.getChannel();
             FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
             BufferedReader reader = new BufferedReader(new FileReader(raf.getFD()))) {

            String line = reader.readLine();
            if (line != null) {
                return presidentFromFileString(line, system);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading president from " + e.getMessage());
        }
        return null;
    }

    private static String managerToFileString(Manager manager) {
        return String.format("%s,%s", manager.getUsername(), manager.getHashedPassword());
    }

    private static Manager managerFromFileString(String data, ThreatManagementSystem system) {
        String[] parts = data.split(",", -1);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid manager data format: " + data);
        }
        return new Manager(parts[0], parts[1], system);
    }

    private static String presidentToFileString(President president) {
        return String.format("%s,%s,%s", president.getUsername(), president.getHashedPassword(), president.getKey());
    }

    private static President presidentFromFileString(String data, ThreatManagementSystem system) {
        String[] parts = data.split(",", -1);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid president data format: " + data);
        }
        return new President(parts[0], parts[1], parts[2], system);
    }
}