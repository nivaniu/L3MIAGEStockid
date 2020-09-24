package com.stockid;

import com.stockid.os.OSValidator;
import com.stockid.tools.FileTools;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Main {

    public static Scanner scanner = new Scanner(System.in);

    public static String directorySeparator;
    private static String currentDirectory;
    private static String rootDirectory;
    private static final String defPhotoExtension = ".jpg";
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
        choices.put(exitCode++, "Enter directory");
        actions.put(exitCode - 1, __ -> enterDirectory());
        choices.put(exitCode++, "Parent directory");
        actions.put(exitCode - 1, __ -> parentDirectory());
        choices.put(exitCode++, "Take a picture");
        actions.put(exitCode - 1, __ -> createFile());
        choices.put(exitCode++, "Create Folder");
        actions.put(exitCode - 1, __ -> createFolder());
        choices.put(exitCode++, "Delete");
        actions.put(exitCode - 1, __ -> deleteFile());
        choices.put(exitCode++, "Share File");
        actions.put(exitCode - 1, __ -> shareFile());
        choices.put(exitCode++, "Upload Document");
        choices.put(exitCode++, "Enter a key");
        choices.put(exitCode, "Exit");
        return true;
    }

    private static void deleteFile() {
        System.out.print("Enter file name :");
        String fileName = scanner.next();
        File file = new File(rootDirectory + currentDirectory + fileName);
        if (!file.exists()) {
            System.out.println("Error: File " + fileName + " does not exist");
            return;
        }
        if (file.isDirectory()) {
            println("File " + fileName + " is a directory,\nAre you sure you want to delete it along with "
                    + countFiles(file) + " objects inside? (yes/n)");
            String answer = "";
            while (!answer.equals("yes") && !answer.equals("n")) {
                answer = scanner.next();
            }
            if (answer.equals("n")) {
                println("Aborting");
                return;
            }
           deleteDir(file);
           return;
        }
        if (!file.delete()) {
            println("Error: unable to delete " + fileName);
        }
    }

    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null)
            for (File f : contents)
                deleteDir(f);
        if (!file.delete())
         println("Error: unable delete "+FileTools.getFileName(file.getPath(),directorySeparator));
    }
    private static int countFiles(File file)
    {
        int i = 0;
        File[] contents = file.listFiles();
        if (contents != null)
            for (File f : contents)
            {
                deleteDir(f);
                i++;
            }
        return i;
    }
    private static void createFile() {
        System.out.print("Pick a name for your new document: ");
        String fileName = scanner.next() + defPhotoExtension;
        File file = new File(rootDirectory, currentDirectory + directorySeparator + fileName);
        if (file.exists()) {
            System.out.println("Error: File already exist");
            return;
        }
        try {
            if (file.createNewFile()) {
                System.out.println("File " + fileName + " created with success");
            }
        } catch (IOException e) {
            System.out.println("Error: unable to create " + fileName);
        }
    }

    private static void parentDirectory() {
        currentDirectory = FileTools.removeLastDirectory(currentDirectory, directorySeparator);
        displayFiles(rootDirectory + currentDirectory);
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
        while (pickAction(displayMenu())) {
            opCount++;
        }
        System.out.println("You have performed " + opCount + " operations.");
        scanner.close();
    }

    private static int displayMenu() {
        int choise = 0;
        do {
            System.out.println("\nList of available actions :");
            for (Integer key : choices.keySet()) {
                System.out.println(key + ") " + choices.get(key));
            }
            try {
                choise = Integer.parseInt(scanner.next());
            } catch (Exception e) {
                System.out.println("Error: Integer expected.\n");
            }
        } while (choise > exitCode);
        return choise;
    }

    private static void shareFile() {
        displayFiles(rootDirectory + currentDirectory);
        System.out.println("Chose a file");
        Share.document(rootDirectory + currentDirectory + directorySeparator + scanner.next());
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
            } else {
                f = "f" + f;
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

    private static void println(String message) {
        System.out.println(message);
    }
}
