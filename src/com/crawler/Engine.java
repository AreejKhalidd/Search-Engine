package com.crawler;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.*;
import static java.lang.Thread.sleep;


public class Engine implements Runnable{
    private Database db;
    private CopyOnWriteArrayList <Seeds> seedsList;
    private AtomicInteger doneCrawledPages; private int Crawling_Update;;  //Backup pages
    private int threadsNum;


    /**
     *
     * @param num
     * @param seedList
     * @throws SQLException
     * @throws ClassNotFoundException
     */

    public Engine(int num,CopyOnWriteArrayList<Seeds> seedList) throws SQLException, ClassNotFoundException {

        db = new Database();
        this.threadsNum=num;
        int backedUp = db.UrlsTableCount(); int pages = db.countPagesInPagesTable(); Crawling_Update = backedUp + pages;
        doneCrawledPages=new AtomicInteger(db.countPagesInPagesTable());
        try {
            ResultSet output_set  = db.GetPageFromUrlsTable();
            if (output_set.next()) {
                this.seedsList = new CopyOnWriteArrayList<>();
                this.seedsList.add(new Seeds(output_set .getString("url")));
                while (output_set.next()) {this.seedsList.add(new Seeds(output_set .getString("url"))); } }
            else{ this.seedsList = seedList; }
        } catch (SQLException e) {System.out.println("Error in constructor"); }
    }
    //End of constructor
//Functions

    /**
     *
     * @return
     */
    private static CopyOnWriteArrayList<Seeds> links (){
        CopyOnWriteArrayList<Seeds> seedList = new CopyOnWriteArrayList<>();
        seedList.add(new Seeds("https://twitter.com/"));
        seedList.add(new Seeds("https://www.google.com/"));
        seedList.add(new Seeds("https://www.facebook.com/"));
        seedList.add(new Seeds("https://www.linkedin.com/"));
        seedList.add(new Seeds("https://www.amazon.com/"));
        seedList.add(new Seeds("https://en.wikipedia.org/wiki/Main_Page"));
        seedList.add(new Seeds("https://stackoverflow.com/"));
        seedList.add(new Seeds("https://www.reddit.com/"));
        seedList.add(new Seeds("https://www.coursera.org/"));
        seedList.add(new Seeds("https://www.dictionary.com/"));
        seedList.add(new Seeds("https://trello.com/"));
        seedList.add(new Seeds("https://github.com/"));
        seedList.add(new Seeds("https://www.ted.com/"));
        seedList.add(new Seeds("https://www.udemy.com/"));
        seedList.add(new Seeds("https://www.trivago.com/"));
        seedList.add(new Seeds("https://www.ebay.com/"));
        seedList.add(new Seeds("https://stackexchange.com/"));
        seedList.add(new Seeds("https://www.tumblr.com/"));
        return seedList;

    }


    /**
     *
     * @param link
     * @return
     */
    private boolean Nothing_crawled(String link){ return db.PageInUrlsTable(link); }

    /**
     *
     * @param SeedList
     * @param check
     */
                            // add hyper links to url tablesssss
    private void Crawling_process(CopyOnWriteArrayList<Seeds> SeedList , int check) { // for each url in the list --Mariam
        Document doc;
        //Headers
        String header1;
        String header2;
        String header3;
        String header4;
        String header5;
        String header6;
        String title;
        String body;
        //If there's nothing to crawl
        if(doneCrawledPages.get() >= 5000 || SeedList.isEmpty()) return;

        for(Seeds sL : SeedList) {
            try {
                System.out.println("add");
                boolean visited =db.isAddedURLInPagesTable(sL.getSeed());
                System.out.println("add2222");
                if(!visited) { //not visited before
                    //Robots call
                    Robots robot_check = new Robots(sL);
                    CopyOnWriteArrayList <String> disallowedPivotList;
                    //check robots for this url
                    boolean protocol = robot_check.robotProtocol();

                    if(protocol)
                    {
                        sleep(Math.round(robot_check.retrieveDelay()));
                    }
                    disallowedPivotList = robot_check.seedsDisallowed();
                    //no disallowing
                    if(!robot_check.notAvailable() && !disallowedPivotList.contains(sL.getSeed())) {
                        doc = Jsoup.connect(sL.getSeed()).get();

                        String document= doc.toString();

                        boolean doneInserting = this.db.InsertPageToPagesTable(sL.getSeed(),document);
                        boolean inserturl= this.db.insert_url(sL.getSeed()); //insert urls
                        System.out.println("doneeeeeeeeeee Inserting");
                        if (doneInserting && inserturl ) {
                            doneCrawledPages.incrementAndGet();
                            if(doneCrawledPages.get() >= 5000 ) return;
                        }
                        else{ SeedList.remove(sL.getSeed());
                            return; }


                     //in each page get the hyperlinks
                        Elements links = doc.body().select("a[href]");
                        for (Element link : links) {
                            Seeds newSeed;
                            if(link.attr("href").startsWith("//")){
                                newSeed = new Seeds("https:"+link.attr("href"));
                            }
                            else if(link.attr("href").startsWith("/")){
                                newSeed = new Seeds(sL.Dir()+link.attr("href").substring(1));
                            }
                            else {
                                newSeed = new Seeds(link.attr("href"));
                            }
                            if(!newSeed.getSeed().startsWith("http"))
                                continue;
                            if(!disallowedPivotList.contains(newSeed.getSeed()))
                            {
                                SeedList.add(newSeed);
                                if(Crawling_Update < 3*5000) {
                                    db.InsertUrlsTable(newSeed.getSeed());
                                Crawling_Update++;
                                }
                            }

                        }
                        //Craling then remove from db
                        db.DeleteUrlFromUrlsTable(sL.getSeed());
                    }
                    ///////////////////////////////////*************************end if not visited
                }else if(Nothing_crawled(sL.getSeed())){ // is in backup "url table"
                    //Robots call
                    Robots robot_check = new Robots(sL);
                    CopyOnWriteArrayList <String> disallowedPivotList;
                    //check robots for this url
                    boolean protocol = robot_check.robotProtocol();
                    if(protocol)
                    {
                        sleep(Math.round(robot_check.retrieveDelay()));
                    }
                    disallowedPivotList = robot_check.seedsDisallowed();
                   //no disallowing
                    if(!robot_check.notAvailable() && !disallowedPivotList.contains(sL.getSeed())) {
                        doc = Jsoup.connect(sL.getSeed()).get();
                        Elements links = doc.body().select("a[href]");
                        for (Element link : links) {
                            Seeds crawled;
                            if(link.attr("href").startsWith("//")){
                                crawled = new Seeds("https:"+link.attr("href"));
                            }
                            else if(link.attr("href").startsWith("/")){
                                crawled = new Seeds(sL.Dir()+link.attr("href").substring(1));
                            }
                            else {
                                crawled = new Seeds(link.attr("href"));
                            }
                            if(!crawled.getSeed().startsWith("http"))
                                continue;
                            if (!disallowedPivotList.contains(crawled.getSeed())) {
                                SeedList.add(crawled);

                                if(Crawling_Update < 3*5000) {
                                    db.InsertUrlsTable(crawled.getSeed());
                                    Crawling_Update++;
                                }
                            }
                        }
                    }
                    db.DeleteUrlFromUrlsTable(sL.getSeed());  //After finish crawling delete the url
                }
                SeedList.remove(sL);
            }
            catch (MalformedURLException e) {
                System.err.println("Not a strong URL..");
                SeedList.remove(sL);
            }
            catch (HttpStatusException e) { SeedList.remove(sL); }
            catch (SocketException e ) {}
            catch (UnknownHostException e) { System.err.println("Week Connection.."); }
            catch (Exception e){ SeedList.remove(sL); }
        }
        Crawling_process(SeedList,check); //recursive call
    }


    @Override
    public void run() {
        System.out.println("in run");
        int num_parallel = Integer.parseInt(Thread.currentThread().getName());
        int Seed_Thread = seedsList.size() / threadsNum;
        int i=Seed_Thread * num_parallel;
        int s=seedsList.size();
        int j=Seed_Thread * (num_parallel + 1);

        CopyOnWriteArrayList<Seeds> mySeeds = new CopyOnWriteArrayList<>();
        if(num_parallel == threadsNum-1){
            for (int index1 = i; index1 < s; index1++) { mySeeds.add(seedsList.get(index1)); }
        } else {
            for (int index2 = i; index2< j; index2++) { mySeeds.add(seedsList.get(index2)); }
        }
        // System.out.println("hello i'm here");
        Crawling_process(mySeeds,0); //Crawl call
    }

    public static void main(String[] args) throws InterruptedException, SQLException, ClassNotFoundException {
        //Class.forName("com.mysql.jdbc.Driver");
        CopyOnWriteArrayList<Seeds> seedListGetter=links();

        ArrayList<Thread> array=new ArrayList<>();
        int threadNum =0;
        Scanner input = new Scanner(System.in);
        System.out.print("Enter threads count: ");
        threadNum = input.nextInt();
        input.close();
        Runnable crawler = new Engine(threadNum,seedListGetter);
        for(int i=0;i<threadNum;i++){
            array.add(new Thread(crawler));
            array.get(i).setName(Integer.toString(i));
        }
        for(int i=0;i<threadNum;i++){
            array.get(i).start();
        }
        for(int i=0;i<threadNum;i++){
            array.get(i).join();
        }
    }
}
