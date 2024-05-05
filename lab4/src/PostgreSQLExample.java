import java.sql.Connection;
import java.sql.DriverManager;
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
            System.out.println("Connection" + connection + " has been connected.");

            // Perform desired database operations

            //create TABLE
            // (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            //     Statement statement = connection.createStatement();
    
            //     String createTableSQL = "CREATE TABLE employees ("
            //             + "id serial PRIMARY KEY,"
            //             + "name VARCHAR(255),"
            //             + "age INT)";
    
            //     statement.execute(createTableSQL);
    
            //     System.out.println("Table created successfully.");
            // }

            // Close the connection
            connection.close();
        }
}
