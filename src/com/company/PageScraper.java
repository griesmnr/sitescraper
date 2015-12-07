package com.company;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.net.ConnectException;

/**
 * Created by nicoleg on 12/6/15.
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
            new ErrorHandler("badly formed URL.", e);
        }
    }

    private String addProtocolToURL(String urlString){
        String newUrlString;
        if (urlString.startsWith("http://")){
            this.protocol = Protocol.HTTP;
            newUrlString = urlString;
        } else {
            this.protocol = Protocol.HTTPS;
            if (urlString.startsWith("https://")) {
                newUrlString = urlString;
            } else {
                newUrlString = "https://" + urlString;
            }
        }
        return newUrlString;
    }

    private void getPageData(){
        if (this.getProtocol() == Protocol.HTTPS){
            this.tryGetDataViaHTTPS();
        }
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
            System.out.println(unfilteredSiteText.toString());
        } catch (IOException e){
            new ErrorHandler("error connecting to website", e);
        }
    }

    private void tryGetDataViaHTTPS(){
        unfilteredSiteText = new StringBuilder();
        BufferedReader rd = null;

        try {
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                unfilteredSiteText.append(line);
            }
            System.out.println(unfilteredSiteText.toString());
        } catch (ConnectException e){
            if (!this.originalUrlString.startsWith("https://")){
                this.updateUrl("http://" + this.originalUrlString);
            } else {
                new ErrorHandler("error connecting to website", e);
            }
        } catch (IOException e){
            new ErrorHandler("error connecting to website", e);
        } finally {
            if (rd != null){
                try {
                    rd.close();
                } catch (IOException e){
                    new ErrorHandler("error connecting to website", e);
                }

            }
        }
    }

    private void stripOutTags(){
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

   private void breakIntoWords(){
        StringTokenizer st = new StringTokenizer(filteredSiteText.toString(), " ");
        WordHolder wordHolder = new WordHolder();
        while (st.hasMoreTokens()){
            wordHolder.processWord(st.nextToken());
        }
        System.out.println(wordHolder.getWordsAndCounts().toString());
    }

    public void scrapePage(){
        this.getPageData();
        this.stripOutTags();
        this.breakIntoWords();
    }


}
