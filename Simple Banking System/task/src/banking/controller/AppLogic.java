package banking.controller;

import banking.model.Card;
import banking.view.TextUserInterface;
import banking.model.Database;

import java.util.Random;

public class AppLogic {
    // private final Map<String, String> cardData;
    private final TextUserInterface ui = new TextUserInterface();
    private boolean loggedIn;
    private final Random rng;
    private final Database db;

    public AppLogic(String databaseFileName) {
        // cardData = new HashMap<>();
        loggedIn = false;
        rng = new Random();
        db = new Database(databaseFileName);
    }

    public void start() {
        int menuChoice;
        while (true) {
            if (!loggedIn) {
                ui.printMainMenu();
                menuChoice = ui.getMenuChoice();
                switch (menuChoice) {
                    case 1 -> createAccount();
                    case 2 -> login();
                    case 0 -> { db.releaseResources(); return; }
                }
            } else {
                ui.printLoggedInUserMenu();
                menuChoice = ui.getMenuChoice();
                switch (menuChoice) {
                    case 1 -> ui.printBalance();
                    case 2 -> loggedIn = false;
                    case 0 -> { return; }
                }
            }
        }
    }

    private void login() {
        String cardNumber = ui.getCardNumberFromUser();
        String pin = ui.getPinFromUser();
        Card card = db.getCardDetails(cardNumber, pin);

        if (card.isValid() && card.getCardNumber().equals(cardNumber) && card.getPin().equals(pin)) {
            loggedIn = true;
            ui.printLoginSuccess();
        } else {
            ui.printLoginError();
        }
    }

    void createAccount() {
        String cardNumber = createCardNumber();
        String pin = createPin();
        db.persistCardData(cardNumber, pin);
        ui.printCreatedCardDetails(cardNumber, pin);
    }

    String createCardNumber() {
        final String BIN = "400000";
        String accountNumber = generateRandomDigits(9);
        String checksum = calculateChecksum(BIN + accountNumber);
        return BIN + accountNumber + checksum;
    }

    String createPin() {
        String pinBase = String.valueOf(rng.nextInt(10000));
        return String.format("%04d", Integer.parseInt(pinBase)); // pad with zeroes to make 4 chars/
    }

    String generateRandomDigits(int length) {
        String newAccountNumber;
        do {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < length; i++) {
                int digit = rng.nextInt(10);
                sb.append(digit);
            }
            newAccountNumber = sb.toString();
        } while (!isAcctNoUnique(newAccountNumber));
        return newAccountNumber;
    }

    public static String extractAcctNoFromCardNo(String cardNumber) {
        return cardNumber.substring(6, 15);
    }

    boolean isAcctNoUnique(String accountNumber) {
        return db.isAccountNumberUnique(accountNumber);
    }

    static String calculateChecksum(String cardNoWithoutChecksum) {
        int[] accountNumberDigits = new int[cardNoWithoutChecksum.length()];
        int sumOfLuhnAccountNumberDigits = 0;
        for (int i = 0; i < cardNoWithoutChecksum.length(); i++) {
            accountNumberDigits[i] = Character.getNumericValue(cardNoWithoutChecksum.charAt(i));
            if (i % 2 == 0) {
                accountNumberDigits[i] *= 2;
                if (accountNumberDigits[i] > 9) {
                    accountNumberDigits[i] -= 9;
                }
            }
            sumOfLuhnAccountNumberDigits += accountNumberDigits[i];
        }
        return String.valueOf(10 - (sumOfLuhnAccountNumberDigits % 10));
    }
}
