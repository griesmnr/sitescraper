package com.company;

/**
 * Created by nicoleg on 12/6/15.
 */
public class TagFilterer {
    private boolean shouldRecord;
    private boolean shouldSkipThenRecord;
    private boolean shouldDelimit;

    public TagFilterer(){
        this.shouldRecord = true;
        this.shouldSkipThenRecord = false;
        this.shouldDelimit = false;
    }

    public void processChar(char c){
        if (this.getShouldSkipThenRecord()){
            this.setShouldSkipThenRecord(false);
            this.setShouldRecord(true);
        }
        if (this.getShouldDelimit()){
            this.setShouldDelimit(false);
        }
        if (c == '<'){
            this.setShouldRecord(false);
            this.setShouldDelimit(true);
        } else if (c == '>') {
            this.setShouldSkipThenRecord(true);
        }
    }

    public boolean getShouldDelimit() {
        return shouldDelimit;
    }

    public boolean getShouldRecord() {
        return shouldRecord;
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

    private void setShouldSkipThenRecord(boolean shouldSkipThenRecord) {
        this.shouldSkipThenRecord = shouldSkipThenRecord;
    }

    public boolean shouldRecordChar(){
        return getShouldRecord() && !getShouldSkipThenRecord();
    }
}
