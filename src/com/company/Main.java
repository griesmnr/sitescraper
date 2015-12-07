package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Please Enter a Url you want to scrape:");
        Scanner keyboard = new Scanner(System.in);
        String urlString = keyboard.next();

        PageScraper pageScraper = new PageScraper(urlString);
        pageScraper.scrapePage();
    }
}
