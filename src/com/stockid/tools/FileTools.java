package com.stockid.tools;

public class FileTools {
    public static String getFileName(String path, String directorySeparator){
        String split = "/";
        if (!split.equals(directorySeparator))
            split = "\\\\";
        String[] dirs = path.split(split);
        return dirs[dirs.length-1];
    }
}
