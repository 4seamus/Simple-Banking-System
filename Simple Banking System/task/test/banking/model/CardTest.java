package banking.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void extractAcctNoFromCardNoTest() {
        assertEquals("844943340", Card.extractAcctNoFromCardNo("4000008449433403"));
    }

    @Test
    void calculateChecksumTest() {
        assertEquals("3", Card.calculateChecksum("400000844943340"));
    }

    @Test
    void IsLuhnValidTestTrue() {
        assertTrue(Card.isLuhnValid("4000003477268243"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "1245", "4005003477262247" })
    void IsLuhnValidTestFalse(String arg) {
        assertFalse(Card.isLuhnValid(arg));
    }
}