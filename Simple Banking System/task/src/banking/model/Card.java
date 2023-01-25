package banking.model;

public class Card {
    private String cardNumber;
    private String pin;
    private int balance;
    private boolean valid;

    public Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        balance = 0;
        valid = true;
    }

    // private constructor is only used by the Card class internally to produce an invalid card
    private Card() {
        valid = false;
    }

    public static Card createInvalidCard() {
        return new Card();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}
