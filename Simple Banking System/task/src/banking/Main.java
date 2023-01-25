package banking;

import banking.controller.AppLogic;

public class Main {
    public static void main(String[] args) {
        AppLogic appLogic = new AppLogic(args[1]);
        appLogic.start();
    }
}