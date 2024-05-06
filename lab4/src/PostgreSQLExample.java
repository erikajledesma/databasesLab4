import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PostgreSQLExample {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        while (true){
            String jdbcUrl = "jdbc:postgresql://localhost:5432/lab4";
            String username = "postgres";
            String password = "snapSh00t!";

            // Register the PostgreSQL driver

            Class.forName("org.postgresql.Driver");

            // Connect to the database
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            //Test if connection has been made
            // System.out.println("Connection" + connection + " has been connected.");

            // Perform desired database operations

            //Main Menu
            System.out.println("--- POMONA TRANSIT SYSTEM ---");
            System.out.println("1: Display schedule for a trip");
            System.out.println("0: Exit the menu");

            //get user's menu choice
            Scanner scn = new Scanner(System.in);
            System.out.println("Choose a number from the menu: ");
            int menu_choice = scn.nextInt();

            if (menu_choice == 1){
                //Display schedule of all trips for a given StartLocationName and DestinationName, and Date
                Scanner scn1 = new Scanner(System.in);
                System.out.println("Select your start --> end destination: ");
                System.out.println("1: City A --> City B");
                System.out.println("2: City B --> City C");
                System.out.println("3: Sin City --> Cos City");
                System.out.println("4: Havenbrook --> Willow Springs");
                System.out.println("5: Brooksville --> Clearwater");

                int tripNum = scn1.nextInt();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT Date, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID FROM TripOffering WHERE tripnumber=" + tripNum);

                System.out.printf("----------------------------------------------------------------------------------%n");
                System.out.printf("| %-10s | %-8s | %-8s | %-15s | %-8s | %n", "Date", "Scheduled Start Time", "Scheduled Arrival Time", "Driver Name", "BusID");
                // System.out.println("Date           Scheduled Start Time      Scheduled Arrival Time      Driver Name        BusID");
                while (resultSet.next()) {
                    System.out.printf("| %-10s | %-20s | %-22s | %-11s | %-8s | %n", resultSet.getDate("Date"), resultSet.getTime("ScheduledStartTime"), resultSet.getTime("ScheduledArrivalTime"), resultSet.getString("DriverName"), resultSet.getLong("BusID"));
                }
                System.out.printf("----------------------------------------------------------------------------------%n");

                statement.close();
                resultSet.close();
            }

            if (menu_choice == 0){
                System.out.println("Exiting program...");
                System.exit(0);
            }

            // Close the connection
            connection.close();
        }
    }
}
