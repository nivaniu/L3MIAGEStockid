package com.stockid.tools;

import java.util.ArrayList;
import java.util.Arrays;

public class FileTools {
    public static String getFileName(String path, String directorySeparator){
        String split = "/";
        if (!split.equals(directorySeparator))
            split = "\\\\";
        String[] dirs = path.split(split);
        return dirs[dirs.length-1];
    }

    public static String removeLastDirectory(String path, String directorySeparator)
    {
        String sep = "/";
        if (directorySeparator.equals("\\"))
            sep = "\\\\";
        ArrayList<String>dirs = new ArrayList<>(Arrays.asList(path.split(sep)));
        if(dirs.isEmpty())
        {
            System.out.println("You are at root directory.\n");
        }else {
            System.out.println(dirs.toString());
            System.out.println("separator "+directorySeparator);
            System.out.println("path "+path);

            dirs.remove(dirs.size()-1);
        }
        return dirs.stream().reduce((s1, s2)-> s1 + directorySeparator + s2).orElse("") + directorySeparator;
    }
}
