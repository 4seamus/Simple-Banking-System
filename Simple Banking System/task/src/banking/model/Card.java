package banking.model;

public class Card {
    private String cardNumber;
    private String pin;
    private int balance;
    private boolean valid;

    public Card(String cardNumber, String pin, int balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        valid = true;
    }

    // private constructor is only used by the Card class internally to produce an invalid card
    private Card() {
        valid = false;
    }

    public static Card createInvalidCard() {
        return new Card();
    }

    public static String extractAcctNoFromCardNo(String cardNumber) {
        return cardNumber.substring(6, 15);
    }

    public static String calculateChecksum(String cardNoWithoutChecksum) {
        int[] cardNoDigitsWithoutChecksum = new int[cardNoWithoutChecksum.length()];
        int sumOfLuhnAccountNumberDigits = 0;
        for (int i = 0; i < cardNoWithoutChecksum.length(); i++) {
            cardNoDigitsWithoutChecksum[i] = Character.getNumericValue(cardNoWithoutChecksum.charAt(i));
            if (i % 2 == 0) {
                cardNoDigitsWithoutChecksum[i] *= 2;
                if (cardNoDigitsWithoutChecksum[i] > 9) {
                    cardNoDigitsWithoutChecksum[i] -= 9;
                }
            }
            sumOfLuhnAccountNumberDigits += cardNoDigitsWithoutChecksum[i];
        }
        return String.valueOf(10 - (sumOfLuhnAccountNumberDigits % 10));
    }

    public static boolean isLuhnValid(String cardNumber) {
        String checksum = String.valueOf(Character.getNumericValue(cardNumber.charAt(cardNumber.length() - 1)));
        String cardNoWithoutChecksum = cardNumber.substring(0, cardNumber.length() - 1);
        return checksum.equals(calculateChecksum(cardNoWithoutChecksum));
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
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
}
