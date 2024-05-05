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
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Bus");
                
                while (resultSet.next()) {
                    // String columnValue = resultSet.getString("BusID");
                    System.out.printf("id: %d model: %s year: %d \n", resultSet.getLong("busid"), resultSet.getString("model"), resultSet.getLong("year"));
                }
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
