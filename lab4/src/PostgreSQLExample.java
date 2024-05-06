import java.sql.*;
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
            System.out.println("2: Edit Trip Offerings");
            System.out.println("0: Exit the menu");

            //get user's menu choice
            Scanner scn = new Scanner(System.in);
            System.out.println("Choose a number from the menu: ");
            int menu_choice = scn.nextInt();

            if(menu_choice ==2){
                System.out.println("--CHOOSE HOW TO EDIT--");
                System.out.println("1: Add trip ");
                int edit_choice = scn.nextInt();
                if(edit_choice == 1){
                    while (true) {
                        System.out.println("--ADD A TRIP--");
                        System.out.println("Please enter the trip number: ");
                        int tripNum = scn.nextInt();
                        scn.nextLine();
                        System.out.println("Please enter the trip date in YYYY-MM-DD format: ");
                        String tripDate = scn.nextLine();
                        System.out.println("Please enter the scheduled start time in HH:MM::SS format: ");
                        String startTime = scn.nextLine();
                        System.out.println("Please enter the scheduled arrival time in HH:MM::SS format: ");
                        String arrivalTime = scn.nextLine();
                        System.out.println("Please enter the bus driver name: ");
                        String driverName = scn.nextLine();
                        System.out.println("Please enter the busID: ");
                        int busID = scn.nextInt();
                        try{
                        PreparedStatement statement = connection.prepareStatement(
                                "INSERT INTO tripoffering (tripnumber, date, scheduledstarttime, scheduledarrivaltime, drivername, busid) VALUES(?,?,?,?,?,?)");
                        statement.setInt(1, tripNum);
                        statement.setDate(2, java.sql.Date.valueOf(tripDate));
                        statement.setTime(3, java.sql.Time.valueOf(startTime));
                        statement.setTime(4, java.sql.Time.valueOf(arrivalTime));
                        statement.setString(5, driverName);
                        statement.setInt(6, busID);
                        statement.executeUpdate();
                            System.out.println("Trip successfully added.");
                        } catch (SQLException ex) {
                            System.out.println("The trip could not be added: ");
                            System.out.println(ex.getMessage());
                        }
                        System.out.println("Would you like to add another trip?");
                        System.out.println("1: Add another trip");
                        System.out.println("2: Exit back to menu");
                        int add_choice = scn.nextInt();
                        if (add_choice == 2){
                            break;
                        }
                    }

                }
            }

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
