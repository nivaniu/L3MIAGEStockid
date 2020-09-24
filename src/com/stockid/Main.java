package com.stockid;

import com.stockid.os.OSValidator;
import com.stockid.tools.FileTools;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    private static Scanner scanner = new Scanner(System.in);

    private static String directorySeparator;
    private static String currentDirectory;
    private static String rootDirectory;

    private static int exitCode = 0;

    private static HashMap<Integer, String> choices;
    private static HashMap<Integer, Consumer<String>> actions;

    private static boolean init() {
        choices = new HashMap<>();
        actions = new HashMap<>();

        OSValidator.dosplayOS();
        if (OSValidator.isWindows()) {
            directorySeparator = "\\";
        } else if (OSValidator.isUnix() || OSValidator.isMac()) {
            directorySeparator = "/";
        } else {
            System.out.println("Error: Your os is not supported by application.");
            return false;
        }

        currentDirectory = directorySeparator;
        rootDirectory = System.getProperty("user.dir") + directorySeparator + "root";
        File file = new File(rootDirectory);
        boolean bool = file.mkdir();
        if (bool) {
            System.out.println("Root directory created successfully");
        } else {
            System.out.println("Root directory already exist");
        }
        choices.put(exitCode++, "Display Files");
        actions.put(exitCode - 1, __ -> displayFiles(rootDirectory + currentDirectory));
        choices.put(exitCode++, "Share File");
        choices.put(exitCode++, "Create Folder");
        actions.put(exitCode - 1, __ -> createFolder());
        choices.put(exitCode++, "Take a picture");
        choices.put(exitCode++, "Upload Document");
        choices.put(exitCode++, "Enter a key");
        choices.put(exitCode++, "Enter directory");
        actions.put(exitCode - 1, __ -> enterDirectory());
        choices.put(exitCode, "Exit");
        return true;
    }

    public static void main(String[] args) {
        if (!init()) {
            return;
        }
        System.out.println(" ______       _________   ______       ______       ___   ___      ________      ______      \n"
                           + "/_____/\\     /________/\\ /_____/\\     /_____/\\     /___/\\/__/\\    /_______/\\    /_____/\\     \n"
                           + "\\::::_\\/_    \\__.::.__\\/ \\:::_ \\ \\    \\:::__\\/     \\::.\\ \\\\ \\ \\   \\__.::._\\/    \\:::_ \\ \\    \n"
                           + " \\:\\/___/\\      \\::\\ \\    \\:\\ \\ \\ \\    \\:\\ \\  __    \\:: \\/_) \\ \\     \\::\\ \\      \\:\\ \\ \\ \\   \n"
                           + "  \\_::._\\:\\      \\::\\ \\    \\:\\ \\ \\ \\    \\:\\ \\/_/\\    \\:. __  ( (     _\\::\\ \\__    \\:\\ \\ \\ \\  \n"
                           + "    /____\\:\\      \\::\\ \\    \\:\\_\\ \\ \\    \\:\\_\\ \\ \\    \\: \\ )  \\ \\   /__\\::\\__/\\    \\:\\/.:| | \n"
                           + "    \\_____\\/       \\__\\/     \\_____\\/     \\_____\\/     \\__\\/\\__\\/   \\________\\/     \\____/_/ \n"
                           + "                                                                                             ");
        int opCount = 0;
        while (pickAction(displayMenu()))
            opCount++;
        System.out.println("You have performed "+opCount+" operations.");
        scanner.close();
    }

    private static int displayMenu() {
        int choise;
        do {
            System.out.println("List of available actions :");
            for (Integer key : choices.keySet()) {
                System.out.println(key + ") " + choices.get(key));
            }
            choise = scanner.nextInt();
        } while (choise > exitCode);
        return choise;
    }

    private static boolean pickAction(int userChoise) {
        System.out.print("You have chosen : ");
        System.out.println(choices.get(userChoise));
        if (userChoise == exitCode) {
            return false;
        }
        if (actions.get(userChoise) != null) {
            actions.get(userChoise).accept(currentDirectory);
        } else {
            System.out.println("Error: Chosen function has not been implemented yet.\n");
        }
        return true;
    }

    private static void displayFiles(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            System.out.println("Error, " + FileTools.getFileName(path, directorySeparator) + " is not a directory\n");
            return;
        }
        System.out.println("\nPath: " + currentDirectory + "\nFile list:\n".replace(directorySeparator, "/"));
        String[] files = file.list();
        for (String f : Objects.requireNonNull(files)) {
            f = "\t" + f;
            if (!f.contains(".")) {
                f = "d" + f;
            }
            System.out.println(f);
        }
        System.out.println();
    }

    private static void createFolder() {
        System.out.println("Enter the name of the desired a directory: ");
        String path = rootDirectory + currentDirectory + scanner.next();
        File file = new File(path);
        boolean bool = file.mkdir();
        if (bool) {
            System.out.println("Directory created successfully\n");
        } else {
            System.out.println("Directory already exist");
        }
        displayFiles(file.getParent());
    }

    private static void enterDirectory() {
        System.out.println("Enter directory to enter: ");
        String dirName = scanner.next();
        File file = new File(rootDirectory + currentDirectory + dirName);
        if (!file.exists()) {
            System.out.println("Error: Directory " + dirName + " does not exist.\n");
            return;
        }
        currentDirectory += dirName + directorySeparator;
    }
}
