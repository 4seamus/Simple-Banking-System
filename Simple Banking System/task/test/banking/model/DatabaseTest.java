package banking.model;

import org.junit.jupiter.api.Test;

import static banking.model.Card.extractAcctNoFromCardNo;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    @Test
    void isAccountNumberUniqueTest() {
        Database db = new Database("testDb.s3db");
        db.createTables();
        String cardNumber = "4000009901741192";
        String pin = "6752";
        db.saveNewCardData(cardNumber, pin);
        db.saveNewCardData(cardNumber, pin);
        assertEquals(false, db.isAccountNumberUnique(extractAcctNoFromCardNo(cardNumber)));
    }
}