import java.io.*;
import java.util.*;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static HashMap<String, String> consumerDB = new HashMap<>();
    static HashMap<String, String> userDB = new HashMap<>();
    static String lastBill = "";

    public static void main(String[] args) {

        loadUsers();
        loadConsumers();
        login();

        while (true) {
            System.out.println("\n==================== TNEB BILLING SYSTEM ====================");
            System.out.println("1. Calculate Bill (Meter Reading Mode)");
            System.out.println("2. Calculate Bill (Multiple Appliance Mode)");
            System.out.println("3. Add New Consumer");
            System.out.println("4. View All Consumers");
            System.out.println("5. Save Last Bill to File");
            System.out.println("6. Exit");
            System.out.println("=============================================================");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> meterReadingMode();
                case 2 -> applianceMode();
                case 3 -> addConsumer();
                case 4 -> viewConsumers();
                case 5 -> saveBillToFile();
                case 6 -> {
                    System.out.println("Exiting Program. Thank You!");
                    return;
                }
                default -> System.out.println("Invalid choice! Try again.");
            }
        }
    }
    static void login() {
        System.out.println("\n========== LOGIN REQUIRED ==========");
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();

        if (!userDB.containsKey(user) || !userDB.get(user).equals(pass)) {
            System.out.println("Invalid Login! Exiting...");
            System.exit(0);
        }

        System.out.println("Login Successful!");
    }

    static void loadUsers() {
        try {
            File file = new File("users.txt");
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                userDB.put(parts[0], parts[1]);
            }
            br.close();

        } catch (Exception e) {
            System.out.println("Error loading users!");
        }
    }

    static void loadConsumers() {
        try {
            File file = new File("consumers.txt");
            if (!file.exists()) return;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                consumerDB.put(parts[0], parts[1]);
            }
            br.close();

        } catch (Exception e) {
            System.out.println("Error loading consumers!");
        }
    }

    static void addConsumer() {
        System.out.print("Enter Consumer Number: ");
        String cno = sc.nextLine();
        System.out.print("Enter Consumer Name: ");
        String name = sc.nextLine();

        consumerDB.put(cno, name);

        try {
            FileWriter fw = new FileWriter("consumers.txt", true);
            fw.write(cno + "," + name + "\n");
            fw.close();
        } catch (Exception ignored) {}

        System.out.println("Consumer Added Successfully!");
    }
    static void viewConsumers() {
        System.out.println("\n------- CONSUMERS LIST -------");
        for (String key : consumerDB.keySet()) {
            System.out.println(key + " → " + consumerDB.get(key));
        }
    }

    static void meterReadingMode() {

        System.out.print("Enter Consumer Number: ");
        String cno = sc.nextLine();

        if (!consumerDB.containsKey(cno)) {
            System.out.println("Consumer Not Found!");
            return;
        }

        System.out.println("Consumer Name: " + consumerDB.get(cno));

        System.out.print("Enter Previous Reading: ");
        double prev = sc.nextDouble();

        System.out.print("Enter Current Reading: ");
        double curr = sc.nextDouble();

        double units = curr - prev;

        generateBill(cno, consumerDB.get(cno), "Meter Reading Mode", units);
    }

    static void applianceMode() {

        System.out.print("Enter Consumer Number: ");
        String cno = sc.nextLine();

        if (!consumerDB.containsKey(cno)) {
            System.out.println("Consumer Not Found!");
            return;
        }

        System.out.println("Consumer Name: " + consumerDB.get(cno));

        System.out.print("How many appliances? ");
        int count = sc.nextInt();

        double totalUnits = 0;

        for (int i = 1; i <= count; i++) {
            sc.nextLine();
            System.out.print("Appliance " + i + " Name: ");
            String app = sc.nextLine();

            System.out.print("Watt: ");
            double watt = sc.nextDouble();

            System.out.print("Hours per day: ");
            double hrs = sc.nextDouble();

            System.out.print("Days used: ");
            int days = sc.nextInt();

            totalUnits += (watt / 1000.0) * hrs * days;
        }

        generateBill(cno, consumerDB.get(cno), "Multiple Appliances", totalUnits);
    }

       static void generateBill(String cno, String cname, String mode, double units) {

        double u = units;
        double bill = 0;

        double s101_200 = 0, s201_400 = 0, s401_500 = 0;
        double s501_600 = 0, s601_800 = 0, s801_1000 = 0, s1000Plus = 0;
        if (u <= 100) u = 0;
        else u -= 100;

        
        if (u > 0) {
            double x = Math.min(u, 100);
            s101_200 = x * 2.25;
            bill += s101_200;
            u -= x;
        }

                if (u > 0) {
            double x = Math.min(u, 200);
            s201_400 = x * 4.50;
            bill += s201_400;
            u -= x;
        }

        
        if (u > 0) {
            double x = Math.min(u, 100);
            s401_500 = x * 6.00;
            bill += s401_500;
            u -= x;
        }

              if (u > 0) {
            double x = Math.min(u, 100);
            s501_600 = x * 8.00;
            bill += s501_600;
            u -= x;
        }
        if (u > 0) {
            double x = Math.min(u, 200);
            s601_800 = x * 9.00;
            bill += s601_800;
            u -= x;
        }

             if (u > 0) {
            double x = Math.min(u, 200);
            s801_1000 = x * 10.00;
            bill += s801_1000;
            u -= x;
        }

          if (u > 0) {
            s1000Plus = u * 11.00;
            bill += s1000Plus;
        }

        double fixedCharge = 30;
        double electricityDuty = bill * 0.05;
        double totalBill = bill + fixedCharge + electricityDuty;

        StringBuilder sb = new StringBuilder();

        sb.append("\n=============== TNEB BILL ===============\n");
        sb.append("Consumer No  : " + cno + "\n");
        sb.append("Name         : " + cname + "\n");
        sb.append("Mode         : " + mode + "\n");
        sb.append(String.format("Units Used   : %.2f\n", units));
        sb.append("-----------------------------------------\n");
        sb.append("Slab Details:\n");
        sb.append("0–100 : FREE\n");

        if (s101_200 > 0) sb.append(String.format("101–200 : ₹%.2f\n", s101_200));
        if (s201_400 > 0) sb.append(String.format("201–400 : ₹%.2f\n", s201_400));
        if (s401_500 > 0) sb.append(String.format("401–500 : ₹%.2f\n", s401_500));
        if (s501_600 > 0) sb.append(String.format("501–600 : ₹%.2f\n", s501_600));
        if (s601_800 > 0) sb.append(String.format("601–800 : ₹%.2f\n", s601_800));
        if (s801_1000 > 0) sb.append(String.format("801–1000 : ₹%.2f\n", s801_1000));
        if (s1000Plus > 0) sb.append(String.format("1000+ : ₹%.2f\n", s1000Plus));

        sb.append("-----------------------------------------\n");
        sb.append(String.format("Energy Charge : ₹%.2f\n", bill));
        sb.append(String.format("Fixed Charge  : ₹%.2f\n", fixedCharge));
        sb.append(String.format("Electric Duty : ₹%.2f\n", electricityDuty));
        sb.append("-----------------------------------------\n");
        sb.append(String.format("TOTAL BILL    : ₹%.2f\n", totalBill));
        sb.append("=========================================\n");

        lastBill = sb.toString();
        System.out.println(lastBill);
    }
    static void saveBillToFile() {
        if (lastBill.equals("")) {
            System.out.println("No bill generated yet!");
            return;
        }
        try {
            FileWriter fw = new FileWriter("LastBill.txt");
            fw.write(lastBill);
            fw.close();
            System.out.println("Bill saved as LastBill.txt");
        } catch (Exception e) {
            System.out.println("Error saving bill!");
        }
    }
}
