package com.company;

/**
 * Created by nicoleg on 12/6/15.
 */
public class TagFilterer {
    private boolean shouldRecord;
    private boolean shouldSkip;
    private boolean shouldSkipThenRecord;
    private boolean shouldDelimit;

    public TagFilterer(){
        this.shouldRecord = true;
        this.shouldSkip = false;
        this.shouldDelimit = false;
    }

    public void processChar(char c){
        if (this.getShouldSkipThenRecord()){
            this.setShouldSkipThenRecord(false);
            this.setShouldRecord(true);
        }
        if (this.getShouldSkip()){
            this.setShouldSkip(false);
        }
        if (this.getShouldDelimit()){
            this.setShouldDelimit(false);
        }
        if (c == '<'){
            this.setShouldRecord(false);
            this.setShouldDelimit(true);
        } else if (c == '>') {
            this.setShouldSkipThenRecord(true);
        } else if (!Character.isLetter(c)){
            this.setShouldSkip(true);
            this.setShouldDelimit(true);
        }

    }


    public boolean getShouldDelimit() {
        return shouldDelimit;
    }

    public boolean getShouldRecord() {
        return shouldRecord;
    }

    private boolean getShouldSkip() {
        return shouldSkip;
    }

    private boolean getShouldSkipThenRecord() {
        return shouldSkipThenRecord;
    }

    public void setShouldDelimit(boolean shouldDelimit) {
        this.shouldDelimit = shouldDelimit;
    }

    public void setShouldRecord(boolean shouldRecord) {
        this.shouldRecord = shouldRecord;
    }

    private void setShouldSkip(boolean shouldSkip) {
        this.shouldSkip = shouldSkip;
    }

    private void setShouldSkipThenRecord(boolean shouldSkipThenRecord) {
        this.shouldSkipThenRecord = shouldSkipThenRecord;
    }

    public boolean shouldRecordChar(){
        return getShouldRecord() && !getShouldSkip() && !getShouldSkipThenRecord();
    }
}
