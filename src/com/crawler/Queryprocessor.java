package com.crawler;

import java.io.File; // for file reading
import java.io.FileNotFoundException; // for scanner try&catch block
import java.sql.SQLException;
import java.util.Scanner; // for input query
import java.util.*; // for HashSet among other things

public class Queryprocessor {
    static ArrayList<String> finalResult;
    static String inputQuery;


    public static ArrayList<String> getInput(String query) throws SQLException, ClassNotFoundException {
        // Scanner scanner = new Scanner(System.in);
        // System.out.println("Enter query:");
        // String query = scanner.nextLine();
        // scanner.close();


        ArrayList<String> queryList = process(query);
        if (queryList != null) {
            queryList = dbProcess(queryList);
        }
        // System.out.println("Final result = " + queryList);
        return queryList;
    }


    public static ArrayList<String> process(String query) throws SQLException {
        if (query == "") {
            System.out.println("Query is empty at input");
            return null;
        }
       // Database.insertNewSugg(query);////////////////////
        query = query.toLowerCase(); // Lowercase
        query = query.replaceAll("[^a-zA-Z0-9\\s+]", ""); // Or use "[^a-zA-Z\\s+]" to remove numbers.
        System.out.println("Query is: " + query);

        // I made it a list without noticing that an announcement mentioned there would only be one-word searches.
        // It still works, so I decided to just send the first string in the list to the database.
        ArrayList<String> splitQuery = new ArrayList<String>(Arrays.asList(query.split(" ")));

        System.out.println("Queries list is: " + splitQuery);

        // Get stop words
        File filepath = new File("stop_words_english.txt").getAbsoluteFile();
        Scanner s = null;
        try {
            s = new Scanner(filepath);
        } catch (FileNotFoundException e) {
            System.out.println("file not found: " + filepath);
        }
        ArrayList<String> stopWords = new ArrayList<String>();
        while (s.hasNextLine()) {
            stopWords.add(s.nextLine());
        }

        // Remove stop words
        for (int i = 0; i < stopWords.size(); i++) {
            if (splitQuery.contains(stopWords.get(i))) {
                splitQuery.removeAll(Arrays.asList(null, stopWords.get(i)));
            }
        }

        // Remove spaces
        splitQuery.removeAll(Arrays.asList(null, ""));

        System.out.println("Queries list after removing stop words is: " + splitQuery);

        // Stemming
        ArrayList<String> stemQuery = new ArrayList<String>();

        Stemmer_Query porterStemmer = new Stemmer_Query();
        for (int j = 0; j < splitQuery.size(); j++) {
            for (int i = 0; i < splitQuery.get(j).length(); i++) {
                porterStemmer.add((splitQuery.get(j).charAt(i)));
            }
            porterStemmer.stem(); // void fn
            stemQuery.add(porterStemmer.toString());
        }

        if (stemQuery.isEmpty()) {
            System.out.println("Query is empty after pre-processing");
            return null;
        }

        System.out.println("After stemming: " + stemQuery);
        return stemQuery;

    }

    public static ArrayList<String> dbProcess(ArrayList<String> stemQuery) throws SQLException, ClassNotFoundException {
        //Database.DatabaseConnect();
        Database db=new Database();
        ArrayList<String> result = new ArrayList();
        if(Database.isSearchQueryExist(stemQuery) == false){ // If query does not exist
            System.out.println("Query not found in the database");
        }
        else // If query exists
            result = Database.getQueryResult(stemQuery);
        return result;
    }
}
