package banking.controller;

import static banking.controller.AppLogic.*;
import static org.junit.jupiter.api.Assertions.*;

class AppLogicTest {

    @org.junit.jupiter.api.Test
    void createCardNumber() {
    }

    @org.junit.jupiter.api.Test
    void extractAcctNoFromCardNoTest() {
        assertEquals("844943340", extractAcctNoFromCardNo("4000008449433403"));
    }

    @org.junit.jupiter.api.Test
    void calculateChecksumTest() {
        assertEquals("3", calculateChecksum("400000844943340"));
    }
}