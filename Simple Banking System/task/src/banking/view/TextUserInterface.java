package banking.view;

import banking.model.Card;
import banking.model.Database;

import java.util.Scanner;

import static banking.model.Card.isLuhnValid;

public class TextUserInterface {
    private final Scanner scan;

    public TextUserInterface() {
        scan = new Scanner(System.in);
    }

    public void printMainMenu() {
        System.out.println("""
                1. Create an account
                2. Log into account
                0. Exit""");
    }

    public void printLoggedInUserMenu() {
        System.out.println("""
                1. Balance
                2. Add income
                3. Do transfer
                4. Close account
                5. Log out
                0. Exit
                """);
    }

    public int getMenuChoice() {
        int menuChoice = Integer.parseInt(scan.nextLine());
        System.out.println();
        return menuChoice;
    }

    public void printBalance(int balance) {
        System.out.println();
        System.out.printf("Balance: %d\n", balance);
        System.out.println();
    }

    public void printCreatedCardDetails(String cardNumber, String pin) {
        System.out.printf("""
                Your card has been created
                Your card number:
                %s
                Your card PIN:
                %s
                """, cardNumber, pin);
        System.out.println();
    }

    public void printAccountCloseSuccess() {
        System.out.println();
        System.out.println("The account has been closed!");
        System.out.println();
    }

    public void printLoginSuccess() {
        System.out.println();
        System.out.println("You have successfully logged in!");
        System.out.println();
    }

    public void printLoginError() {
        System.out.println();
        System.out.println("Wrong card number or PIN!");
        System.out.println();
    }

    public String getCardNumberFromUser() {
        System.out.println("Enter your card number:");
        //return scan.nextLong();
        return scan.nextLine();
    }

    public String getPinFromUser() {
        System.out.println("Enter your PIN:");
        //return scan.nextInt();
        return scan.nextLine();
    }

    public void addIncomeWorkflow(Database db, Card card) {
        System.out.println("Enter income:");
        int income = Integer.parseInt(scan.nextLine());
        card.setBalance(card.getBalance() + income);
        db.updateCardData(card.getCardNumber(), card.getBalance());
        System.out.println("Income was added!");
        System.out.println();
    }

    public void transferWorkflow(Database db, Card card) {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String otherCardNumber = scan.nextLine();
        if (!isLuhnValid(otherCardNumber)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return;
        }
        if (!db.doesCardExist(otherCardNumber)) {
            System.out.println("Such a card does not exist.");
            return;
        }

        System.out.println("Enter how much money you want to transfer:");
        int transferAmount = Integer.parseInt(scan.nextLine());
        if (card.getBalance() < transferAmount) {
            System.out.println("Not enough money!");
            return;
        }

        db.transact(card.getCardNumber(), otherCardNumber, transferAmount);
        System.out.println("Success!");
    }
}
