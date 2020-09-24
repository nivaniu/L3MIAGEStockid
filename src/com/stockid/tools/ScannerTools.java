package com.stockid.tools;

import java.util.Scanner;

public class ScannerTools {
    public static Integer requestInt(Scanner scanner){
        Integer choise = null;
        while (choise==null){
        try {
            choise = Integer.parseInt(scanner.next());
        } catch (Exception e) {
            System.out.println("Error: Integer expected.\n");
        }
        }
        return choise;
    }
}
