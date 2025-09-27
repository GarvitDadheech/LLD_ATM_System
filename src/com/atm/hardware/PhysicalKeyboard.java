package com.atm.hardware;

import java.util.Scanner;

public class PhysicalKeyboard implements Keyboard {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getInput() {
        System.out.println("Reading input from physical keyboard...");
        return scanner.nextLine();
    }
}
