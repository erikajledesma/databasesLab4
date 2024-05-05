import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLExample {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        String jdbcUrl = "jdbc:postgresql://localhost:5432/lab4";
        String username = "postgres";
        String password = "snapSh00t!";

            // Register the PostgreSQL driver

            Class.forName("org.postgresql.Driver");

            // Connect to the database
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);

            //Test if connection has been made
            //System.out.println("Connection" + connection + " has been connected.");

            // Perform desired database operations

            //Print all values from Bus Table
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Bus");
            
            while (resultSet.next()) {
                // String columnValue = resultSet.getString("BusID");
                System.out.printf("id: %d model: %s year: %d \n", resultSet.getLong("busid"), resultSet.getString("model"), resultSet.getLong("year"));
            }

            // Close the connection
            statement.close();
            resultSet.close();
            connection.close();
        }
}
