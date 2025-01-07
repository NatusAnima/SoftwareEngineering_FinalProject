package SE_Final;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Create the ThreatManagementSystem and initial manager
        ThreatManagementSystem system = new ThreatManagementSystem(null);
        Manager manager = new Manager("admin", "admin12345", system);
        system.setManager(manager);

        // Create president with higher authority
        President president = new President("1", "Aa123456", "12345678", system);
        
        // Create 3 detention facilities
        Facility facility1 = new Facility("F001", "Maximum Security Facility", 10);
        
        // Add facilities to the system
        system.addFacility(facility1);
       
        
        // Create and add citizens
        Citizen[] citizens = {
        	    new Citizen("John Doe", LocalDate.of(1990, 5, 15), "A", 7, 8, 3, false, "1001"),
        	    new Citizen("Jane Smith", LocalDate.of(1985, 8, 22), "B", 5, 3, 7, false, "1002"),
        	    new Citizen("Bob Johnson", LocalDate.of(1995, 3, 10), "C", 8, 2, 8, false, "1003"),
        	    new Citizen("Alice Brown", LocalDate.of(1988, 11, 30), "A", 6, 7, 4, false, "1004"),
        	    new Citizen("Charlie Wilson", LocalDate.of(1992, 7, 25), "B", 9, 1, 9, true, "1005"),
        	    new Citizen("Diana Clark", LocalDate.of(1987, 4, 18), "C", 4, 4, 6, false, "1006"),
        	    new Citizen("Edward Davis", LocalDate.of(1993, 9, 5), "A", 3, 9, 2, false, "1007"),
        	    new Citizen("Fiona White", LocalDate.of(1991, 1, 12), "B", 7, 2, 8, false, "1008"),
        	    new Citizen("George Miller", LocalDate.of(1986, 6, 28), "C", 8, 10, 9, false, "1009"),
        	    new Citizen("Helen Taylor", LocalDate.of(1994, 2, 15), "A", 5, 8, 3, false, "1010"),
        	    new Citizen("Ian Anderson", LocalDate.of(1989, 10, 8), "B", 6, 3, 7, false, "1011"),
        	    new Citizen("Julia Martin", LocalDate.of(1996, 12, 20), "C", 10, 10, 1, true, "1012"),
        	    new Citizen("Kevin Moore", LocalDate.of(1984, 7, 3), "A", 4, 7, 4, false, "1013"),
        	    new Citizen("Laura Hall", LocalDate.of(1993, 5, 17), "B", 7, 1, 9, false, "1014"),
        	    new Citizen("Michael King", LocalDate.of(1990, 8, 29), "C", 8, 4, 6, false, "1015"),
        	    new Citizen("Nancy Lee", LocalDate.of(1988, 3, 14), "A", 5, 9, 2, false, "1016"),
        	    new Citizen("Oscar Young", LocalDate.of(1995, 11, 26), "B", 6, 2, 8, false, "1017"),
        	    new Citizen("Patricia Adams", LocalDate.of(1987, 9, 9), "C", 9, 1, 9, false, "1018"),
        	    new Citizen("Quinn Roberts", LocalDate.of(1992, 4, 23), "A", 4, 8, 3, false, "1019"),
        	    new Citizen("Rachel Turner", LocalDate.of(1991, 2, 7), "B", 7, 3, 7, true, "1020")
        	};

        for (Citizen citizen : citizens) {
            system.addCitizen(citizen);
        }
        
        system.manageDetention(); // Initial detention management
        
        boolean running = true;
        while (running) {
            System.out.println("\n=== Threat Management System ===");
            System.out.println("1. Login as Manager");
            System.out.println("2. Login as President");
            System.out.println("3. View Facility Details");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    handleManagerLogin(scanner, manager, system);
                    break;
                case 2:
                    handlePresidentLogin(scanner, president, system);
                    break;
                case 3:
                    viewFacilityDetails(scanner, system);
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting system...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        
        scanner.close();
    }
    
    private static void handleManagerLogin(Scanner scanner, Manager manager, ThreatManagementSystem system) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        if (manager.verifyCredentials(username, password)) {
            System.out.println("Manager login successful!");
            managerMenu(scanner, manager, system);
        } else {
            System.out.println("Invalid credentials!");
        }
    }
    
    private static void handlePresidentLogin(Scanner scanner, President president, ThreatManagementSystem system) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter special key: ");
        String key = scanner.nextLine();
        
        if (president.verifyCredentials(username, password, key)) {
            System.out.println("President login successful!");
            presidentMenu(scanner, president, system);
        } else {
            System.out.println("Invalid credentials!");
        }
    }
    
    private static void managerMenu(Scanner scanner, Manager manager, ThreatManagementSystem system) {
        boolean managing = true;
        while (managing) {
            System.out.println("\n=== Manager Menu ===");
            System.out.println("1. View All Citizens");
            System.out.println("2. Add New Citizen");
            System.out.println("3. Update Citizen Government Support Level");
            System.out.println("4. Update Citizen Public Impact Score");
            System.out.println("5. Update Citizen Economic Percentile");
            System.out.println("6. View All Facilities");
            System.out.println("7. Start Threat Level Updater");
            System.out.println("8. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    manager.printAllCitizens();
                    break;
                case 2:
                    addNewCitizen(scanner, system);
                    break;
                case 3:
                    updateCitizenSupportLevel(scanner, manager, system);
                    break;
                case 4:
                    updateCitizenPublicImpactScore(scanner, manager, system);
                    break;
                case 5:
                    updateCitizenEconomicPercentile(scanner, manager, system);
                    break;
                case 6:
                    manager.printAllFacilities();
                    break;
                case 7:
                	System.out.println("הפעלת thread עדכון מקובץ כל זמן עוד לא כתבנו את הפונקציה");
                    break;
                case 8:
                    managing = false;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private static void updateCitizenSupportLevel(Scanner scanner, Manager manager, ThreatManagementSystem system) {
        System.out.print("Enter citizen ID: ");
        String id = scanner.nextLine();
        Citizen citizen = system.findCitizenById(id);

        if (citizen != null) {
            System.out.print("Enter new government support level (-1 to 10): ");
            int newSupportLevel = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            if (newSupportLevel < -1 || newSupportLevel > 10) {
                System.out.println("Error: Government support level must be between -1 and 10.");
                return;
            }

            manager.updateGovernmentSupportLevel(citizen, newSupportLevel);
        } else {
            System.out.println("Citizen not found!");
        }
    }

    private static void updateCitizenPublicImpactScore(Scanner scanner, Manager manager, ThreatManagementSystem system) {
        System.out.print("Enter citizen ID: ");
        String id = scanner.nextLine();
        Citizen citizen = system.findCitizenById(id);
        
        if (citizen != null) {
            System.out.print("Enter new public impact score (1-10): ");
            int newPublicImpactScore = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            manager.updatePublicImpactScore(citizen, newPublicImpactScore);
        } else {
            System.out.println("Citizen not found!");
        }
    }
    private static void updateCitizenEconomicPercentile(Scanner scanner, Manager manager, ThreatManagementSystem system) {
        System.out.print("Enter citizen ID: ");
        String id = scanner.nextLine();
        Citizen citizen = system.findCitizenById(id);
        
        if (citizen != null) {
            System.out.print("Enter new economic percentile (1-10): ");
            int newEconomicPercentile = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            manager.updateEconomicPercentile(citizen, newEconomicPercentile);
        } else {
            System.out.println("Citizen not found!");
        }
    }

    private static void presidentMenu(Scanner scanner, President president, ThreatManagementSystem system) {
        boolean presiding = true;
        while (presiding) {
            System.out.println("\n=== President Menu ===");
            System.out.println("1. View All Citizens");
            System.out.println("2. Grant Pardon to Citizen");
            System.out.println("3. Export Citizen History");
            System.out.println("4. Start Threat Level Updater");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer
            
            switch (choice) {
                case 1:
                    president.printAllCitizens();
                    break;
                case 2:
                    grantPardon(scanner, president);
                    break;
                case 3:
                    exportHistory(scanner, system);
                    break;
                case 4:
                	System.out.println("הפעלת thread עדכון מקובץ כל זמן עוד לא כתבנו את הפונקציה");
                    break;
                case 5:
                    presiding = false;
                    System.out.println("Logging out...");
                    break;
                case 6: // לדוגמה, מעצר חירום
                    emergencyDetentionForCitizen(scanner, president, system);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private static void addNewCitizen(Scanner scanner, ThreatManagementSystem system) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter birthdate (YYYY-MM-DD): ");
        LocalDate birthDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter origin (A, B, C): ");
        String origin = scanner.nextLine();
        System.out.print("Enter economic percentile (1-10): ");
        int economicPercentile = scanner.nextInt();
        System.out.print("Enter government support level (-1 to 10): ");
        int supportLevel = scanner.nextInt();
        System.out.print("Enter public impact score (1-10): ");
        int publicImpactScore = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        
        Citizen citizen = new Citizen(name, birthDate, origin, economicPercentile, supportLevel, publicImpactScore, false, id);
        system.addCitizen(citizen);
        system.manageDetention(); // Reassess detention
        System.out.println("Citizen added successfully!");
    }
    
  
    
    private static void grantPardon(Scanner scanner, President president) {
        System.out.print("Enter citizen ID to pardon: ");
        String id = scanner.nextLine();
        Citizen citizen = president.getThreatManagementSystem().findCitizenById(id);
        
        if (citizen != null) {
            president.grantPardon(citizen);
            System.out.println("Citizen pardoned successfully!");
        } else {
            System.out.println("Citizen not found!");
        }
    }
    
    private static void viewFacilityDetails(Scanner scanner, ThreatManagementSystem system) {
        System.out.println("Available facilities:");
        system.printAllFacilities();
        System.out.print("Enter facility ID to view details: ");
        String facilityId = scanner.nextLine();
        
        Facility facility = system.getFacilities().stream()
                                  .filter(f -> f.getFacilityId().equals(facilityId))
                                  .findFirst()
                                  .orElse(null);
        
        if (facility != null) {
            System.out.println("\nDetails of Facility: " + facility.getName());
            facility.printAllDetainedCitizens();
        } else {
            System.out.println("Facility not found!");
        }
    }
    
    private static void exportHistory(Scanner scanner, ThreatManagementSystem system) {
        System.out.print("Enter filename to export history: ");
        String filename = scanner.nextLine();
        try {
            system.exportCitizenHistory(filename);
            System.out.println("History exported successfully to " + filename);
        } catch (Exception e) {
            System.out.println("Error exporting history: " + e.getMessage());
        }
    }
    private static void emergencyDetentionForCitizen(Scanner scanner, President president, ThreatManagementSystem system) {
        System.out.print("Enter citizen ID for emergency detention: ");
        String id = scanner.nextLine();
        Citizen citizen = system.findCitizenById(id);

        if (citizen != null) {
            president.emergencyDetention(citizen);
        } else {
            System.out.println("Citizen not found!");
        }
    }

}
