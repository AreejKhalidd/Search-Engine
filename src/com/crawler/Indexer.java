/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crawler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 *
 * @author ecs
 */
public class Indexer {
    static int allDocuments;
    //Database db;
    public Indexer() throws InterruptedException, SQLException, ClassNotFoundException {
        //db= new Database();
        int No = 1;
        Thread index1 = new Index(No);
        index1.start();

        No++;
        Thread index2 = new Index(No);
        index2.start();

        No++;
        Thread index3 = new Index(No);
        index3.start();

        No++;
        Thread index4 = new Index(No);
        index4.start();

        No++;
        Thread index5 = new Index(No);
        index5.start();

        No++;
        Thread index6 = new Index(No);
        index6.start();

        index1.join();
        index2.join();
        index3.join();
        index4.join();
        index5.join();
        index6.join();

        Database.incID();
    }

    public static void main(String[] args) throws SQLException, IOException, InterruptedException, ClassNotFoundException {
       // Database.Connect();
        Database db=new Database();
        //db.removeUnIndexed();

        // May need to add a re-indexing
        Parser.getStoppers();
        Indexer indexer = new Indexer();

    }
}

class Index extends Thread {
    int Num;

    public Index(int N) {
        Num = N;
    }

    @Override
    public void run() {
        String url = null;
        Document doc = null;
        int i=0;

        try {
            url = Database.get1stUnindex();

        } catch(SQLException ex) {
            System.out.print(ex);
        }


        while( url != null) {
           // i++;

            if (url != null) {
                try {
                    Database.set1stIndex(url); //
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
                String get_doc = Database.getByDoc(url); //
                doc=Jsoup.parse(get_doc);

           // org.jsoup.nodes.Document doc = Jsoup.parse(Database.getByDoc(url));

        /* try {
             doc = Jsoup.connect(url).timeout(0).get();
            } catch (IOException ex) {
                System.out.print(ex);
            }*/
            if(doc == null) return;
//Database.insertWords("searchList", url, searchWords.select("title").text(), searchWords.select("body").text(), wordDocumentCounter);
            if(doc != null) {
                Elements searchWords = doc.select("h1, h2, h3, h4, h5, h6, p, "
                        + "title, body, b, i");
                String title = searchWords.select("title").text();
                String desc = searchWords.select("body").text();
                if(desc.length()>2040)
                    desc = desc.substring(0, 100);
                //desc.substring(0, 200)
                //long r = desc.length();
                ArrayList<Word> searchList = new ArrayList();
                Integer wordDocumentCounter = 0;
                try {
                    //Elements x1Tag = searchWords.select("header1");
                    List<String> x1List = searchWords.select("h1").eachText();
                    if(!x1List.isEmpty()){
                        ArrayList<String> x1Search = new ArrayList();
                        for(int j=0;j<x1List.size();j++) {
                            String[] searchListWords = x1List.get(j).split("[^a-zA-Z0-9'-]");
                            x1Search.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+x1Search.size();
                        removeStemWords(x1Search,url,"h1",searchList);
                    }

                    //Elements x2Tag = searchWords.select("x2");
                    List<String> x2List = searchWords.select("h2").eachText();
                    if(!x2List.isEmpty()){
                        ArrayList<String> x2Search = new ArrayList();
                        for(int j=0;j<x2List.size();j++) {
                            String[] searchListWords = x2List.get(j).split("[^a-zA-Z0-9'-]");
                            x2Search.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+x2Search.size();
                        removeStemWords(x2Search,url,"h2",searchList);
                    }

                    //Elements x3Tag = searchWords.select("x3");
                    List<String> x3List = searchWords.select("h3").eachText();
                    if(!x3List.isEmpty()){
                        ArrayList<String> x3Search = new ArrayList();
                        for(int j=0;j<x3List.size();j++) {
                            String[] searchListWords = x3List.get(j).split("[^a-zA-Z0-9'-]");
                            x3Search.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+x3Search.size();
                        removeStemWords(x3Search,url,"h3",searchList);
                    }

                    //Elements x4Tag = searchWords.select("x4");
                    List<String> x4List = searchWords.select("h4").eachText();
                    if(!x4List.isEmpty()){
                        ArrayList<String> x4Search = new ArrayList();
                        for(int j=0;j<x4List.size();j++) {
                            String[] searchListWords = x4List.get(j).split("[^a-zA-Z0-9'-]");
                            x4Search.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+x4Search.size();
                        removeStemWords(x4Search,url,"h4",searchList);
                    }

                    //Elements x5Tag = searchWords.select("x5");
                    List<String> x5List = searchWords.select("h5").eachText();
                    if(!x5List.isEmpty()){
                        ArrayList<String> x5Search = new ArrayList();
                        for(int j=0;j<x5List.size();j++) {
                            String[] searchListWords = x5List.get(j).split("[^a-zA-Z0-9'-]");
                            x5Search.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+x5Search.size();
                        removeStemWords(x5Search,url,"h5",searchList);
                    }

                    //Elements x6Tag = searchWords.select("x6");
                    List<String> x6List = searchWords.select("h6").eachText();
                    if(!x6List.isEmpty()){
                        ArrayList<String> x6Search = new ArrayList();
                        for(int j=0;j<x6List.size();j++) {
                            String[] searchListWords = x6List.get(j).split("[^a-zA-Z0-9'-]");
                            x6Search.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+x6Search.size();
                        removeStemWords(x6Search,url,"h6",searchList);
                    }

                    //Elements x6Tag = searchWords.select("x6");
                    List<String> pList = searchWords.select("p").eachText();
                    if(!pList.isEmpty()){
                        ArrayList<String> pSearch = new ArrayList();
                        for(int j=0;j<pList.size();j++) {
                            String[] searchListWords = pList.get(j).split("[^a-zA-Z0-9'-]");
                            pSearch.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+pSearch.size();
                        removeStemWords(pSearch,url,"p",searchList);
                    }

                    // Elements tTag = searchWords.select("t");
                    List<String> tList = searchWords.select("title").eachText();
                    if(!tList.isEmpty()){
                        ArrayList<String> tSearch = new ArrayList();
                        for(int j=0;j<tList.size();j++) {
                            String[] searchListWords = tList.get(j).split("[^a-zA-Z0-9'-]");
                            tSearch.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+tSearch.size();
                        removeStemWords(tSearch,url,"title",searchList);
                    }

                    //Elements bTag = searchWords.select("b");
                    List<String> bList = searchWords.select("b").eachText();
                    if(!bList.isEmpty()){
                        ArrayList<String> bSearch = new ArrayList();
                        for(int j=0;j<bList.size();j++) {
                            String[] searchListWords = bList.get(j).split("[^a-zA-Z0-9'-]");
                            bSearch.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+bSearch.size();
                        removeStemWords(bSearch,url,"b",searchList);
                    }

                    //Elements iTag = searchWords.select("i");
                    List<String> iList = searchWords.select("i").eachText();
                    if(!iList.isEmpty()){
                        ArrayList<String> iSearch = new ArrayList();
                        for(int j=0;j<iList.size();j++) {
                            String[] searchListWords = iList.get(j).split("[^a-zA-Z0-9'-]");
                            iSearch.addAll(Arrays.asList(searchListWords));
                        }
                        wordDocumentCounter = wordDocumentCounter+iSearch.size();
                        removeStemWords(iSearch,url,"i",searchList);
                    }
                } catch (IOException ex) {
                    System.out.print(ex);
                }

                try {
                    if(!searchList.isEmpty())
                        //String url2= HTML;

                        Database.insertWords(searchList, url, title, desc, wordDocumentCounter);
                } catch (SQLException ex) {
                    System.out.println(ex);
                }

                try {
                    Database.setLastIndex(url);
                } catch (SQLException ex) {
                    System.out.print(ex);
                }
            }

            try {
                url = Database.get1stUnindex();

            } catch(SQLException ex) {
                System.out.print(ex);
            }
        }
    }

//    public void stringTags(List<String> list,
//            ArrayList<String> search, Integer wordDocumentCounter,
//            String[] listWords, Document doc, Elements searchWords, int index) {
//        //Tag.set(index, searchWords.get(index));
//        list.set(index, searchWords.select("x1").get(0));
//        if(!list.isEmpty()) {
//            for(int j=0;j<list.size();j++)
//
//        }
//
//
//    }

    public void removeStemWords(ArrayList<String> tagSearch, String url, String tag,
                                ArrayList<Word> searchList) throws IOException {

        ArrayList<String> stemmers = Parser.parsing(tagSearch);
        int checker = 0;
        for(int i=0;i<stemmers.size();i++) {
            for(int j=0;j<searchList.size();j++) {
                if(stemmers.get(i).equals(searchList.get(j).word)==true) {
                    searchList.get(j).occur(tag);
                    checker = 1;
                    break;
                }
            }

            if(checker == 0) {
                Word searchListWords = new Word(stemmers.get(i));
                searchListWords.occur(tag);
                searchList.add(searchListWords);
            }
            checker = 0;
        }
    }
}
