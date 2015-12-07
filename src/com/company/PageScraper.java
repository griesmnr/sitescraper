package com.company;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * Created by nicoleg on 12/6/15.
 * Handles top level functions of scraping a page, including :
 *  url validation
 *  HTTP/HTTPS requests
 *  high level parsing of HTML
 */
public class PageScraper {
    public enum Protocol {
        HTTP, HTTPS
    }
    private URL url;
    private String originalUrlString;
    private Protocol protocol;
    private StringBuilder unfilteredSiteText;
    private StringBuilder filteredSiteText;


    public PageScraper(String urlString){
        this.originalUrlString = urlString;
        this.updateUrl(urlString);
    }

    private void updateUrl(String urlString){
        try {
            this.url = new URL(this.addProtocolToURL(urlString));
        } catch (MalformedURLException e){
            new ErrorHandler("The url is badly formed. Please try again.", e);
        }
    }

    private String addProtocolToURL(String urlString){
        String newUrlString;
        if (urlString.startsWith("http://")){
            this.setProtocol(Protocol.HTTP);
            newUrlString = urlString;
        } else {
            this.setProtocol(Protocol.HTTPS);
            if (urlString.startsWith("https://")) {
                newUrlString = urlString;
            } else {
                newUrlString = "https://" + urlString;
            }
        }
        return newUrlString;
    }

    private void getAllPageDataAsString(){
        if (this.getProtocol() == Protocol.HTTPS){
            this.tryGetDataViaHTTPS();
        }
        //we have the opportunity to retry with HTTP if HTTPS fails in a specific way.
        //its hard to tell by the user input if the protocol should be HTTP or HTTPS.
        //if a site is HTTPS, it seems to handle HTTP gracefully, but we get a specific
        //error if a site is HTTP and doesn't happen to support HTTPS. so we try in this
        //order.
        if (this.getProtocol() == Protocol.HTTP) {
            this.tryGetDataViaHTTP();
        }
    }

    private Protocol getProtocol(){
        return this.protocol;
    }

    private void setProtocol(Protocol protocol){
        this.protocol = protocol;
    }

    private void tryGetDataViaHTTP(){
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            this.populateUnfilteredSiteText(rd);
        } catch (IOException e){
            new ErrorHandler("Something went wrong connecting to the website.", e);
        }
    }

    private void tryGetDataViaHTTPS(){
        BufferedReader rd = null;
        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            rd = new BufferedReader(new InputStreamReader(is));
            this.populateUnfilteredSiteText(rd);
        } catch (ConnectException e){
            //perhaps retry with HTTP
            if (!this.originalUrlString.startsWith("https://")){
                this.updateUrl("http://" + this.originalUrlString);
            } else {
                new ErrorHandler("Something went wrong connecting to the website.", e);
            }
        } catch (IOException e){
            new ErrorHandler("Something went wrong connecting to the website.", e);
        } finally {
            closeReader(rd);
        }
    }

    private void populateUnfilteredSiteText(BufferedReader rd){
        unfilteredSiteText = new StringBuilder();
        try {
            String line;
            while ((line = rd.readLine()) != null) {
                unfilteredSiteText.append(line);
            }
        } catch (IOException e){
            new ErrorHandler("Something went wrong connecting to the website.", e);
        }

    }

    private void closeReader(BufferedReader rd){
        if (rd != null){
            try {
                rd.close();
            } catch (IOException e){
                new ErrorHandler("Something went wrong connecting to the website.", e);
            }
        }
    }

    private void stripOutTags(){
        filteredSiteText = new StringBuilder();
        TagFilterer filterer = new TagFilterer();
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

   private void breakIntoWords(){
        StringTokenizer st = new StringTokenizer(filteredSiteText.toString(), " ");
        WordHolder wordHolder = new WordHolder();
        while (st.hasMoreTokens()){
            wordHolder.processWord(st.nextToken());
        }
        //TODO remove
        System.out.println(wordHolder.getWordsAndCounts().toString());
    }

    public void scrapePage(){
        //You might be wondering why I loop through once without filtering, and then loop through
        //again. what a performance hit.
        //This is because as far as I understand it, tags can span lines, aka this <p\n> is valid
        //html, and bufferedReader, and many input items, go line by line.
        // So this would mess with my ability to understand what's between tags.
        this.getAllPageDataAsString();
        this.stripOutTags();
        this.breakIntoWords();
    }


}
