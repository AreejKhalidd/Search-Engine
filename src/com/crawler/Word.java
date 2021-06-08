/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crawler;

/**
 *
 * @author ecs
 */
public class Word {
    int header1 = 0;
    int header2 = 0;
    int header3 = 0;
    int header4 = 0;
    int header5 = 0;
    int header6 = 0;
    int par = 0;
    int title = 0;
    int bold = 0;
    int italic = 0;
    String word;

    public Word(String W) {
        word = W;
        header1 = header2 = header3 = header4 = header5 = header6 = title = bold = italic = par = 0;
    }

    public void occur(String pos) {
        switch(pos) {
            case "h1":
                header1++;
                break;
            case "h2":
                header2++;
                break;
            case "h3":
                header3++;
                break;
            case "h4":
                header4++;
                break;
            case "h5":
                header5++;
                break;
            case "h6":
                header6++;
                break;
            case "title":
                title++;
                break;
            case "b":
                bold++;
                break;
            case "i":
                italic++;
                break;
            case "p":
                par++;
                break;
        }
    }
}
