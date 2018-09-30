package com.aesophor.medievania.ui;

public class Dialog {

    private final String speakerName;
    private final String message;

    public Dialog(String speakerName, String message) {
        this.speakerName = speakerName;
        this.message = message;
    }


    public String getSpeakerName() {
        return speakerName;
    }

    public String getMessage() {
        return message;
    }

}