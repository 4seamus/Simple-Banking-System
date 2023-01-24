package banking.controller;

import banking.view.TextUserInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AppLogic {
    private final Map<Long, Integer> cardData;
    private final TextUserInterface ui = new TextUserInterface();
    private boolean loggedIn;
    private final Random rng;

    public AppLogic() {
        cardData = new HashMap<>();
        loggedIn = false;
        rng = new Random();
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
                    case 0 -> { return; }
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
        long cardNumber = ui.getCardNumberFromUser();
        int pin = ui.getPinFromUser();
        if (cardData.containsKey(cardNumber) && cardData.get(cardNumber) == pin) {
            loggedIn = true;
            ui.printLoginSuccess();
        } else {
            ui.printLoginError();
        }
    }

    private void createAccount() {
        long cardNumber = createCardNumber();
        int pin = createPin();
        cardData.put(cardNumber, pin);
        ui.printCreatedCardDetails(cardNumber, pin);
    }

    private long createCardNumber() {
        final String BIN = "400000";
        String accountNumber = generateRandomDigits(9);
        String checksum = generateRandomDigits(1);
        return Long.parseLong(BIN + accountNumber + checksum);
    }

    private int createPin() {
        return rng.nextInt(10000);
    }

    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = rng.nextInt(10);
            sb.append(digit);
        }
        return sb.toString();
    }
}
