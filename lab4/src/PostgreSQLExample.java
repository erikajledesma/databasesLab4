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
            System.out.println("3: Display the stops for a trip");
            System.out.println("4: View a driver's weekly schedule");
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

            if (menu_choice == 3){
                //Display all stops for a given trip
                Scanner scn3 = new Scanner(System.in);
                System.out.println("Select your start --> end destination to see the stops for this trip: ");
                System.out.println("1: City A --> City B");
                System.out.println("2: City B --> City C");
                System.out.println("3: Sin City --> Cos City");
                System.out.println("4: Havenbrook --> Willow Springs");
                System.out.println("5: Brooksville --> Clearwater");

                int tripNum = scn3.nextInt();

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT S1.StopNumber, S2.StopAddress FROM TripStopInfo S1, Stop S2 WHERE S1.StopNumber = S2.StopNumber AND S1.TripNumber = " + tripNum);

                System.out.printf("----------------------------------------------------------------------------------%n");
                System.out.printf("| %-10s | %-8s | %n", "Stop Number", "Stop Address");
                while (resultSet.next()) {
                    System.out.printf("| %-10s | %-8s | %n", resultSet.getLong("stopnumber"), resultSet.getString("stopaddress"));
                }
                System.out.printf("----------------------------------------------------------------------------------%n");

                statement.close();
                resultSet.close();
            }

            if (menu_choice == 4){
                System.out.println("Enter the name of the driver whose schedule you would like to view: ");
                Scanner scn4 = new Scanner(System.in);
                System.out.println("John Doe");
                System.out.println("Jane Smith");
                System.out.println("Anthony Orlando");
                System.out.println("Jungkook Jeon");

                String driverName = scn4.nextLine();

                System.out.println("Enter in YYYY-MM-DD format what date you want to view:");
                String driverDate = scn4.nextLine();

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT TripNumber, ScheduledStartTime, ScheduledArrivalTime, BusID FROM TripOffering WHERE DriverName = '" + driverName + "' AND Date = '" + driverDate + "'");

                System.out.printf("----------------------------------------------------------------------------------%n");
                System.out.printf("| %-10s | %n", "Weekly Schedule for " + driverName);
                System.out.printf("| %-10s | %-8s | %-8s | %-8s | %n", "Trip Number", "Scheduled Start Time", "Scheduled Arrival Time", "Bus ID");
                while (resultSet.next()) {
                    System.out.printf("| %-10s | %-8s | %-8s | %-8s | %n", resultSet.getLong("tripnumber"), resultSet.getTime("scheduledstarttime"), resultSet.getTime("scheduledarrivaltime"), resultSet.getLong("busid"));
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
