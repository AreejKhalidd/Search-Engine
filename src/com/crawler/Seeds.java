package com.crawler;

//seed set
public class Seeds {
    private String seed;
    private int ID;

    public Seeds(String se) {
        this.seed=se;
    }

    public String getSeed() {
        return seed;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public String Dir() { //pivotRootDirectory
        int i = seed.indexOf('/',"https://".length());
        if(i == -1){
            return seed+"/";
        }
        else{
            return seed.substring(0,i) + "/";
        }
    }
}
