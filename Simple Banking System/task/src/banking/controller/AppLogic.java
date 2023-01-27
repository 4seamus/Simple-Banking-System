package banking.controller;

import banking.model.Card;
import banking.view.TextUserInterface;
import banking.model.Database;

import java.util.Random;

public class AppLogic {
    private final TextUserInterface ui = new TextUserInterface();
    private boolean loggedIn;
    private final Random rng;
    private final Database db;

    public AppLogic(String databaseFileName) {
        loggedIn = false;
        rng = new Random();
        db = new Database(databaseFileName);
    }

    public void start() {
        int menuChoice;
        Card card = Card.createInvalidCard(); // just a placeholder card that will be replaced, but satisfies the compiler initialization checks
        while (true) {
            if (!loggedIn) {
                ui.printMainMenu();
                menuChoice = ui.getMenuChoice();
                switch (menuChoice) {
                    case 1 -> createAccount();
                    case 2 -> card = login();
                    case 0 -> { db.releaseResources(); return; }
                }
            } else {
                ui.printLoggedInUserMenu();
                menuChoice = ui.getMenuChoice();
                switch (menuChoice) {
                    case 1 -> ui.printBalance(db.getCardDetails(card.getCardNumber(), card.getPin()).getBalance());
                    case 2 -> ui.addIncomeWorkflow(db, card);
                    case 3 -> ui.transferWorkflow(db, card);
                    case 4 -> { db.deleteCardData(card.getCardNumber()); ui.printAccountCloseSuccess();}
                    case 5 -> loggedIn = false;
                    case 0 -> { return; }
                }
            }
        }
    }

    private Card login() {
        String cardNumber = ui.getCardNumberFromUser();
        String pin = ui.getPinFromUser();
        Card card = db.getCardDetails(cardNumber, pin);

        if (card.isValid() && card.getCardNumber().equals(cardNumber) && card.getPin().equals(pin)) {
            loggedIn = true;
            ui.printLoginSuccess();
        } else {
            ui.printLoginError();
        }

        return card;
    }

    void createAccount() {
        String cardNumber = createCardNumber();
        String pin = createPin();
        db.saveNewCardData(cardNumber, pin);
        ui.printCreatedCardDetails(cardNumber, pin);
    }

    String createCardNumber() {
        final String BIN = "400000";
        String accountNumber = generateRandomDigits(9);
        String checksum = Card.calculateChecksum(BIN + accountNumber);
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

    boolean isAcctNoUnique(String accountNumber) {
        return db.isAccountNumberUnique(accountNumber);
    }
}
