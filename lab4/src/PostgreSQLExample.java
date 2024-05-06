import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
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
            System.out.println("2: Edit Trip Offerings");
            System.out.println("3: Display the stops for a trip");
            System.out.println("4: View a driver's weekly schedule");
            System.out.println("5: Add a driver");
            System.out.println("6: Add a bus");
            System.out.println("7: Delete a bus");
            System.out.println("8: Add actual trip data to a trip offering");
            System.out.println("0: Exit the menu");

            //get user's menu choice
            Scanner scn = new Scanner(System.in);
            System.out.println("Choose a number from the menu: ");
            int menu_choice = scn.nextInt();

            if (menu_choice == 1){
                //Display schedule of all trips for a given StartLocationName and DestinationName, and Date
                Scanner scn1 = new Scanner(System.in);
                System.out.println("Enter your start destination: ");
                Statement start_options = connection.createStatement();
                ResultSet start_set = start_options.executeQuery("SELECT StartLocationName, DestinationName FROM Trip");
                
                while (start_set.next()) {
                    System.out.println(start_set.getString("startlocationname"));
                    System.out.println(start_set.getString("destinationname"));
                }

                String startLocation = scn1.nextLine();

                System.out.println("Enter your end destination: ");
                Statement destination_options = connection.createStatement();
                ResultSet destination_set = destination_options.executeQuery("SELECT StartLocationName, DestinationName FROM Trip");
                
                while (destination_set.next()) {
                    System.out.println(destination_set.getString("startlocationname"));
                    System.out.println(destination_set.getString("destinationname"));
                }

                String endLocation = scn1.nextLine();

                System.out.println("Enter the date of your trip in YYYY-MM-DD format: ");

                String tripDate = scn1.nextLine();

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT T1.ScheduledStartTime, T1.ScheduledArrivalTime, T1.DriverName, T1.BusID FROM TripOffering T1, Trip T2 WHERE T1.TripNumber = T2.TripNumber AND T2.StartLocationName = '" + startLocation + "' AND T2.DestinationName = '" + endLocation + "' AND T1.Date = '" + tripDate +"'");

                System.out.printf("----------------------------------------------------------------------------------%n");
                System.out.printf("| %-10s | %-8s | %-15s | %-8s | %n", "Scheduled Start Time", "Scheduled Arrival Time", "Driver Name", "BusID");
                // System.out.println("Date           Scheduled Start Time      Scheduled Arrival Time      Driver Name        BusID");
                while (resultSet.next()) {
                    System.out.printf("| %-10s | %-22s | %-11s | %-8s | %n", resultSet.getTime("ScheduledStartTime"), resultSet.getTime("ScheduledArrivalTime"), resultSet.getString("DriverName"), resultSet.getLong("BusID"));
                }
                System.out.printf("----------------------------------------------------------------------------------%n");

                statement.close();
                resultSet.close();
            }

            if (menu_choice == 2) {
                System.out.println("--CHOOSE HOW TO EDIT--");
                System.out.println("1: Add trip ");
                System.out.println("2: Delete Trip");
                System.out.println("3: Change driver for existing trip");
                System.out.println("4: Change bus for existing trip");
                System.out.println("Choose a number from the menu: ");
                int edit_choice = scn.nextInt();
                if (edit_choice == 1) {
                    while (true) {
                        System.out.println("--ADD A TRIP--");
                        System.out.println("Please enter the trip number: ");
                        int tripNum = scn.nextInt();
                        scn.nextLine();
                        System.out.println("Please enter the trip date in YYYY-MM-DD format: ");
                        String tripDate = scn.nextLine();
                        System.out.println("Please enter the scheduled start time in HH:MM:SS format: ");
                        String startTime = scn.nextLine();
                        System.out.println("Please enter the scheduled arrival time in HH:MM:SS format: ");
                        String arrivalTime = scn.nextLine();
                        System.out.println("Please enter the bus driver name: ");
                        String driverName = scn.nextLine();
                        System.out.println("Please enter the busID: ");
                        int busID = scn.nextInt();
                        try {
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
                        if (add_choice == 2) {
                            break;
                        }
                    }
                } else if (edit_choice == 2) {
                    System.out.println("--DELETE A TRIP--");
                    System.out.println("Please enter the trip number: ");
                    int tripNum = scn.nextInt();
                    scn.nextLine();
                    System.out.println("Please enter the trip date in YYYY-MM-DD format: ");
                    String tripDate = scn.nextLine();
                    System.out.println("Please enter the scheduled start time in HH:MM:SS format: ");
                    String startTime = scn.nextLine();
                    PreparedStatement tripCheck = connection.prepareStatement(
                            "SELECT * FROM tripoffering WHERE tripnumber=? AND date=? AND scheduledstarttime=?");
                    tripCheck.setInt(1, tripNum);
                    tripCheck.setDate(2, java.sql.Date.valueOf(tripDate));
                    tripCheck.setTime(3, java.sql.Time.valueOf(startTime));
                    ResultSet result = tripCheck.executeQuery();
                    if (result.next()) {
                        try {
                            PreparedStatement statement = connection.prepareStatement(
                                    "DELETE FROM tripoffering WHERE tripnumber=? AND date=? AND scheduledstarttime=?");
                            statement.setInt(1, tripNum);
                            statement.setDate(2, java.sql.Date.valueOf(tripDate));
                            statement.setTime(3, java.sql.Time.valueOf(startTime));
                            statement.executeUpdate();
                            System.out.println("Trip successfully deleted.");
                        } catch (SQLException ex) {
                            System.out.println("The trip could not be deleted: ");
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("The trip does not exist.");
                    }
                } else if (edit_choice == 3) {
                    System.out.println("--EDIT DRIVER--");
                    System.out.println("Please enter the trip number of the trip you want to edit: ");
                    int tripNum = scn.nextInt();
                    scn.nextLine();
                    System.out.println("Please enter the trip date in YYYY-MM-DD format: ");
                    String tripDate = scn.nextLine();
                    System.out.println("Please enter the scheduled start time in HH:MM:SS format: ");
                    String startTime = scn.nextLine();
                    System.out.println("Please enter the new driver name for the trip: ");
                    String newDriverName = scn.nextLine();
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE tripoffering SET drivername=? WHERE tripnumber=? AND date=? AND scheduledstarttime=?");
                        statement.setString(1, newDriverName);
                        statement.setInt(2, tripNum);
                        statement.setDate(3, java.sql.Date.valueOf(tripDate));
                        statement.setTime(4, java.sql.Time.valueOf(startTime));
                        statement.executeUpdate();
                        System.out.println("Trip successfully updated with new driver.");
                    } catch (SQLException ex) {
                        System.out.println("The trip could not be updated: ");
                        System.out.println(ex.getMessage());
                    }
                } else if (edit_choice == 4) {
                    System.out.println("--EDIT BUS--");
                    System.out.println("Please enter the trip number of the trip you want to edit: ");
                    int tripNum = scn.nextInt();
                    scn.nextLine();
                    System.out.println("Please enter the trip date in YYYY-MM-DD format: ");
                    String tripDate = scn.nextLine();
                    System.out.println("Please enter the scheduled start time in HH:MM:SS format: ");
                    String startTime = scn.nextLine();
                    System.out.println("Please enter the new bus ID for the trip: ");
                    int newBusID = scn.nextInt();
                    try {
                        PreparedStatement statement = connection.prepareStatement(
                                "UPDATE tripoffering SET busid=? WHERE tripnumber=? AND date=? AND scheduledstarttime=?");
                        statement.setInt(1, newBusID);
                        statement.setInt(2, tripNum);
                        statement.setDate(3, java.sql.Date.valueOf(tripDate));
                        statement.setTime(4, java.sql.Time.valueOf(startTime));
                        statement.executeUpdate();
                        System.out.println("Trip successfully updated with new bus ID.");
                    } catch (SQLException ex) {
                        System.out.println("The trip could not be updated: ");
                        System.out.println(ex.getMessage());
                    }
                }
            }

            if (menu_choice == 3){
                //Display all stops for a given trip
                Scanner scn3 = new Scanner(System.in);
        
                System.out.println("Enter your start destination: ");
                Statement start_options = connection.createStatement();
                ResultSet start_set = start_options.executeQuery("SELECT StartLocationName, DestinationName FROM Trip");
                
                while (start_set.next()) {
                    System.out.println(start_set.getString("startlocationname"));
                    System.out.println(start_set.getString("destinationname"));
                }

                String startLocation = scn3.nextLine();

                System.out.println("Enter your end destination: ");
                Statement destination_options = connection.createStatement();
                ResultSet destination_set = destination_options.executeQuery("SELECT StartLocationName, DestinationName FROM Trip");
                
                while (destination_set.next()) {
                    System.out.println(destination_set.getString("startlocationname"));
                    System.out.println(destination_set.getString("destinationname"));
                }
               
                String endLocation = scn3.nextLine();

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT S1.StopNumber, S2.StopAddress FROM TripStopInfo S1, Stop S2, Trip S3 WHERE S1.StopNumber = S2.StopNumber AND S1.TripNumber = S3.TripNumber AND S3.StartLocationName = '" + startLocation + "' AND S3.DestinationName = '" + endLocation + "'");

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
                Statement driver_options = connection.createStatement();
                ResultSet driver_set = driver_options.executeQuery("SELECT DriverName FROM Driver");
                
                while (driver_set.next()) {
                    System.out.println(driver_set.getString("drivername"));
                }

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

            // add a driver
            if (menu_choice == 5) {
                scn.nextLine();
                System.out.println("--ADD A DRIVER--");
                System.out.println("Please enter the name of the driver to be added: ");
                String driverName = scn.nextLine();
                System.out.println("Please the phone number of the driver in XXX-XXX-XXXX format: ");
                String driverPhone = scn.nextLine();
                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO driver (drivername, drivertelephonenumber) VALUES(?,?)");
                    statement.setString(1, driverName);
                    statement.setString(2, driverPhone);
                    statement.executeUpdate();
                    System.out.println("Driver successfully added.");
                } catch (SQLException ex) {
                    System.out.println("The driver could not be added: ");
                    System.out.println(ex.getMessage());
                }
            }

            // add a bus
            if (menu_choice == 6) {
                System.out.println("--ADD A BUS--");
                System.out.println("Please enter the bus ID to be added: ");
                int busID = scn.nextInt();
                scn.nextLine();
                System.out.println("Please enter the bus model to be added: ");
                String busModel = scn.nextLine();
                System.out.println("Please enter the bus year: ");
                int busYear = scn.nextInt();
                try {
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO bus (busid, model, year) VALUES(?,?,?)");
                    statement.setInt(1, busID);
                    statement.setString(2, busModel);
                    statement.setInt(3, busYear);
                    statement.executeUpdate();
                    System.out.println("Bus successfully added.");
                } catch (SQLException ex) {
                    System.out.println("The bus could not be added: ");
                    System.out.println(ex.getMessage());
                }
            }

            // delete a bus
            if (menu_choice == 7) {
                System.out.println("--DELETE A BUS--");
                System.out.println("Please enter the bus ID to be deleted: ");
                int busID = scn.nextInt();
                scn.nextLine();
                Statement check = connection.createStatement();
                // check if busid exists
                ResultSet resultSet = check.executeQuery("SELECT * from bus WHERE busID =" + busID);
                if (resultSet.next()) {
                    PreparedStatement statement = connection.prepareStatement(
                            "DELETE FROM bus WHERE busid=?");
                    statement.setInt(1, busID);
                    statement.executeUpdate();
                    System.out.println("Bus successfully deleted.");
                } else {
                    System.out.println("Bus does not exist.");
                }
            }

            if (menu_choice == 8){
                Scanner scn8 = new Scanner(System.in);
                System.out.println("Enter the trip number: ");

                int tripNum = scn8.nextInt();
                scn8.nextLine();

                System.out.println("Enter the date in YYYY-MM-DD format: ");
                String tripDate = scn8.nextLine();
                System.out.println("Enter the scheduled start time in HH:MM:SS format: ");
                String scheduled_start = scn8.nextLine();
                System.out.println("Enter the stop number: ");
                int stopNum = scn8.nextInt();
                scn8.nextLine();
                System.out.println("Please enter the actual start time in HH:MM:SS format: ");
                String actual_startTime = scn8.nextLine();
                System.out.println("Please enter the actual arrival time in HH:MM:SS format: ");
                String actual_arrivalTime = scn8.nextLine();
                System.out.println("Enter the number of passengers that went in the bus:");
                int passengerIn = scn8.nextInt();
                System.out.println("Enter the number of passengers that left the bus: ");
                int passengerOut = scn8.nextInt();

                Statement trip_offering = connection.createStatement();
                ResultSet offering_result = trip_offering.executeQuery("SELECT ScheduledArrivalTime FROM TripOffering WHERE TripNumber=" + tripNum + " AND Date = '" + tripDate + "' AND ScheduledStartTime = '" + scheduled_start + "'");
                // while (offering_result.next()) {
                //     Time scheduled_arrive = offering_result.getTime("scheduledarrivaltime");
                // }
                if (offering_result.next()){
                    try{

                        String scheduled_arrive = offering_result.getString("scheduledarrivaltime");
                        PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO ActualTripStopInfo(tripnumber, date, scheduledstarttime, stopnumber, scheduledarrivaltime, actualstarttime, actualarrivaltime, numberofpassengerin, numberofpassengerout) VALUES (?,?,?,?,?,?,?,?,?)");
                        statement.setInt(1, tripNum);
                        statement.setDate(2, java.sql.Date.valueOf(tripDate));
                        statement.setTime(3, java.sql.Time.valueOf(scheduled_start));
                        statement.setInt(4, stopNum);
                        statement.setTime(5, java.sql.Time.valueOf(scheduled_arrive));
                        statement.setTime(6, java.sql.Time.valueOf(actual_startTime));
                        statement.setTime(7, java.sql.Time.valueOf(actual_arrivalTime));
                        statement.setInt(8, passengerIn);
                        statement.setInt(9, passengerOut);
                        statement.executeUpdate();
                        System.out.println("Trip offering has been inserted into Actual Trip Stop Info.");
                    
                    } catch (SQLException ex){
                        System.out.println(ex.getMessage());
                    }
                } else {
                    System.out.println("The trip offering does not exist.");
                }

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
