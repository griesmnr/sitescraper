package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by nicoleg on 12/6/15.
 */
public class PageScraper {
    private URL url;
    private StringBuilder unfilteredSiteText;
    private StringBuilder filteredSiteText;


    public PageScraper(String urlString){
        try {
            this.url = new URL(urlString);
        } catch (MalformedURLException e){
            new ErrorHandler("badly formed URL.", e);
        }
    }

    public void getData(){
        //TODO class expects http, but requirements do not. handle.
        //TODO.. hmm... handle https.
        unfilteredSiteText = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                unfilteredSiteText.append(line);
            }
            rd.close();
        } catch (IOException e){
            new ErrorHandler("error connecting to website", e);
        }

    }

    public void stripOutTags(){
        filteredSiteText = new StringBuilder();
        TagFilterer filterer = new TagFilterer();
        //TODO we should probably definitely Java 8 this shit.
        int length = unfilteredSiteText.length();
        for (int i=0; i<length; i++){
            char currentChar = unfilteredSiteText.charAt(i);
            filterer.processChar(currentChar);
            if (filterer.shouldRecordChar()){
                filteredSiteText.append(currentChar);
            }
            if (filterer.getShouldDelimit()){
                filteredSiteText.append(' ');
            }
        }
    }

    public void breakIntoWords(){
        StringTokenizer st = new StringTokenizer(filteredSiteText.toString(), " ");
        WordHolder wordHolder = new WordHolder();
        while (st.hasMoreTokens()){
            wordHolder.processWord(st.nextToken());
        }
        System.out.println(wordHolder.getWordsAndCounts().toString());
    }

    public void scrapePage(){
        this.getData();
        this.stripOutTags();
        this.breakIntoWords();
    }
}
