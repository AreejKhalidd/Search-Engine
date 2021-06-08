package com.crawler;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


class Robots {

     CopyOnWriteArrayList <String> seeds_disallowed;
     CopyOnWriteArrayList <Seeds> seeds_allowed;
     CopyOnWriteArrayList <Seeds> maps;
     float delay_OfCrawler = 0;
     boolean AllSeeds_disallowed;
     Seeds seed;
     Document docRobot;
     String link;


    /**
     *
     * @param s
     */
    public Robots(Seeds s) {
        this.seeds_allowed = new CopyOnWriteArrayList<Seeds>();
        this.seeds_disallowed = new CopyOnWriteArrayList<String>();
        this.seed = s;
        this.link = s.Dir();
        this.maps = new CopyOnWriteArrayList<Seeds>();

    }

    /**
     *
     * @return
     */
    public boolean notAvailable() { return AllSeeds_disallowed; }

    /**
     *
     * @return
     */
    public CopyOnWriteArrayList<String> seedsDisallowed() { return seeds_disallowed; }

    /**
     *
     * @return
     * @throws IOException
     */
    public boolean robotProtocol() throws IOException {
        String robotBody;
        String mainBody;
        int beginning;
        int last;
        String notAllowed;
        this.docRobot = Jsoup.connect(this.link + "robots.txt").get();

        robotBody = this.docRobot.body().text();
         beginning = robotBody.indexOf("User-agent: *");
        if(beginning == -1) return false;
         last = robotBody.indexOf("User-agent:", beginning+"User-agent: *".length());
        if (last == -1 ) {
            last = robotBody.length();
        }
        mainBody = robotBody.substring(beginning, last);
        String[] array= mainBody.split(" ");
        for (int i=0;i<array.length;i++)
        {
            if(array[i].endsWith("User-Agent:"))
                break;
            if (array[i].equals("Disallow:")) {
                if ((i+1) == array.length)
                    return false;
                if(array[i + 1].equals("/"))
                {
                    this.AllSeeds_disallowed = true;
                }
                if( array[i + 1].equals(" ") || array[i + 1].equals("") || array[i + 1].equals("\n") || array[i + 1].equals(" \n"))  // allow all
                {
                    return false;
                }

                if(array[i+1].startsWith("/"))
                    notAllowed = new String(this.link + array[i+1].substring(1));
                else
                    notAllowed = new String(this.link + array[i + 1]);
                 this.seeds_disallowed.add(notAllowed);
            } else if (array[i].equals("Allow:")) {
                Seeds allowedURL = new Seeds(this.link + array[i + 1].substring(array[i + 1].indexOf("/")+1,array[i + 1].length()));
                this.seeds_allowed.add(allowedURL);
            } else if (array[i].equals("Sitemap:")) {
                Seeds map1 = new Seeds(this.link + array[i + 1].substring(array[i + 1].indexOf("/")+1,array[i + 1].length()));
                this.maps.add(map1);
            } else if (array[i].equals("Crawl-delay:")) {
                try {
                    this.delay_OfCrawler = Float.parseFloat(array[i + 1]);
                }catch (NumberFormatException m){
                }
            }
            else { }
        }
        return true;
    }

    public float retrieveDelay() { return delay_OfCrawler; }

}