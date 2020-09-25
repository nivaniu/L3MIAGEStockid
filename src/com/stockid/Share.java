package com.stockid;

import com.stockid.tools.FileTools;
//import com.sun.tools.javac.util.List;
import java.util.List;

import java.io.File;
import java.util.ArrayList;

class Share {
    private static ArrayList<String> medias = new ArrayList<>(List.of("Facebook","Instaram", "Mail","Gmail", "SMS", "Go back"));
    static void document(String path)
    {
        int choise = -1;
        File file = new File(path);
        String fileName = FileTools.getFileName(path, Main.directorySeparator);
        if (!file.exists())
        {
            System.out.println("Error: File " + fileName +" does not exist.\n");
            return;
        }
        if(file.isDirectory())
        {
            System.out.println("Error: File " + fileName+" is a directory.\n");
            return;
        }
        System.out.println("Sharing " + fileName+"\nPick a Media:");
        for (String s : medias) {
            System.out.println(medias.indexOf(s)+") " + s);
        }
        while (choise>medias.size() || choise < 0){
            try {
                choise = Integer.parseInt(Main.scanner.next());
                if(choise>medias.size() || choise < 0){
                    System.out.println("Error: option "+choise+" does not exist\n");
                }
            } catch (Exception e) {
                System.out.println("Error: Integer Expected.\n");
            }
        }
        if (choise!= medias.size()-1)
            System.out.println("Shared "+fileName+" on "+medias.get(choise)+" with success.");
        else
            System.out.println("Aborting");
    }
}
