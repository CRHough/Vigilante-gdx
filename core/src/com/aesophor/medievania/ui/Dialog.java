package com.aesophor.medievania.ui;

public class Dialog {

    private final String speakerName;
    private final String dialog;

    public Dialog(String speakerName, String dialog) {
        this.speakerName = speakerName;
        this.dialog = dialog;
    }


    public String getSpeakerName() {
        return speakerName;
    }

    public String getDialog() {
        return dialog;
    }

}