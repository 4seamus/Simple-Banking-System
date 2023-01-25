package banking.model;

import java.sql.*;

// implemented using:
// https://www.sqlitetutorial.net/sqlite-java/
public class Database {
    private Connection connection;

    public Database(String fileName) {
        try {
            // db parameters
            String url = "jdbc:sqlite:" + fileName;
            // create a connection to the database
            connection = DriverManager.getConnection(url);
            //System.out.println("## DIAG ## Connection to SQLite has been established.");
            if (connection != null) {
                DatabaseMetaData metaData = connection.getMetaData();
                //System.out.println("## DIAG ## The driver name is " + meta.getDriverName());
                //System.out.println("## DIAG ## A new database has been created.");
                createTables(); // create app tables upon initialization
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void releaseResources() {
        try {
                if (connection != null) {
                    connection.close();
                }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void createTables() {
        // SQL statement for creating a new table
        String sql = """
                CREATE TABLE IF NOT EXISTS card (
                	id INTEGER PRIMARY KEY,
                	number TEXT NOT NULL,
                	pin TEXT NOT NULL,
                	balance INTEGER DEFAULT 0
                );""";

        try {
            Statement stmt = connection.createStatement();
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void persistCardData(String cardNumber, String pin) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, pin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // expects that the DB doesn't have duplicate card numbers
    // returns the 1st card that matches, otherwise it returns an invalid card
    public Card getCardDetails(String cardNumber, String pin) {
        String sql = "SELECT number, pin FROM card WHERE number = ? AND pin = ?";

        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, pin);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Card(rs.getString("number"), rs.getString("pin"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Card.createInvalidCard();
    }

    public boolean isAccountNumberUnique(String accountNumber) {
        String sql = "SELECT number, pin FROM card WHERE substr(number, 7 , 9) = ?";
        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accountNumber);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }
}
