package banking.view;

import java.util.Scanner;

public class TextUserInterface {
    private final Scanner scan;

    public TextUserInterface() {
        scan = new Scanner(System.in);
    }

    public void printMainMenu() {
        System.out.println("""
                1. Create an account
                2. Log into account
                0. Exit
                """);
    }

    public void printLoggedInUserMenu() {
        System.out.println("""
                1. Balance
                2. Log out
                0. Exit""");
    }

    public int getMenuChoice() { return Integer.parseInt(scan.nextLine()); }

    public void printBalance() {
        System.out.println("Balance: 0");
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

    public void printLoginSuccess() {
        System.out.println("\nYou have successfully logged in!\n");
    }

    public void printLoginError() {
        System.out.println("\nWrong card number or PIN!\n");
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
}
