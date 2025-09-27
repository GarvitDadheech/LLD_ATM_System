package com.atm.hardware;

import java.util.Scanner;

public class DigitalKeyboard implements Keyboard {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getInput() {
        return scanner.nextLine();
    }
}
