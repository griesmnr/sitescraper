package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("hello. welcome to url parser. please enter a url:");
        Scanner keyboard = new Scanner(System.in);
        String urlString = keyboard.next();

        PageScraper pageScraper = new PageScraper(urlString);
        pageScraper.scrapePage();
    }
}
