package banking.controller;

import org.junit.jupiter.api.Test;

import static banking.controller.AppLogic.*;
import static org.junit.jupiter.api.Assertions.*;

class AppLogicTest {

    @Test
    void extractAcctNoFromCardNoTest() {
        assertEquals("844943340", extractAcctNoFromCardNo("4000008449433403"));
    }

    @Test
    void calculateChecksumTest() {
        assertEquals("3", calculateChecksum("400000844943340"));
    }
}