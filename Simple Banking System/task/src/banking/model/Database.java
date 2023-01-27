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

    // used for card create code path
    public void saveNewCardData(String cardNumber, String pin) {
        String sql = "INSERT INTO card(number, pin) VALUES(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, pin);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // used for balance update code path (add income, do transfer)
    public void updateCardData(String cardNumber, int balance) {
        String sql = "UPDATE card SET balance = ? WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, balance);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteCardData(String cardNumber) {
        String sql = "DELETE FROM card WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // expects that the DB doesn't have duplicate card numbers
    // returns the 1st card that matches, otherwise it returns an invalid card
    public Card getCardDetails(String cardNumber, String pin) {
        String sql = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";

        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, pin);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new Card(rs.getString("number"), rs.getString("pin"), rs.getInt("balance"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return Card.createInvalidCard();
    }

    public boolean doesCardExist(String cardNumber) {
        String sql = "SELECT number FROM card WHERE number = ?";

        try (PreparedStatement preparedStatement  = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, cardNumber);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next())
                    return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
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

    public void transact(String fromCardNumber, String toCardNumber, int transferAmount) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setInt(1, -transferAmount);
            preparedStatement.setString(2, fromCardNumber);
            preparedStatement.executeUpdate();
            preparedStatement.setInt(1, transferAmount);
            preparedStatement.setString(2, toCardNumber);
            preparedStatement.executeUpdate();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
