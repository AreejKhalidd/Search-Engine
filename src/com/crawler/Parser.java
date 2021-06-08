/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ecs
 */
public class Parser {
    static private List<String> stoppers;
    private ArrayList<Character> punctuation = new ArrayList();
    private ArrayList<File> docList = new ArrayList();

    static public void getStoppers() throws IOException {
        String currDir = System.getProperty("user.dir");
        stoppers = Files.readAllLines(Paths.get(currDir+"\\stop_words_english.txt"));
    }

    static public ArrayList<String> parsing (ArrayList<String>searchList) throws IOException {
        ArrayList<String> stemmers = new ArrayList();
        for(String i:searchList) {
            if(i.isBlank()||i.isEmpty())
                continue;
            i = i.toLowerCase();
            if(stoppers.contains(i))
                continue;
            if(checkNum(i) || i.length() == 1)
                continue;
            else {
                String output = Stemmer.stem(i);
                if(!output.isEmpty() && !output.isBlank())
                    stemmers.add(output);
            }
        }
        return stemmers;
    }

    public static boolean checkNum(String args) {
        try {
            Integer.parseInt(args);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }
}
