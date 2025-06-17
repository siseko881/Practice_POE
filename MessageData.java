/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.practice_poe;

/**
 *
 * @author RC_Student_lab
 */
class MessageData {
    private String text;
    private String hash;
    private String recipient;
    private boolean sent;

    public MessageData(String text, String hash, String recipient, boolean sent) {
        this.text = text;
        this.hash = hash;
        this.recipient = recipient;
        this.sent = sent;
    }

    public String getText() {
        return text;
    }

    public String getHash() {
        return hash;
    }

    public String getRecipient() {
        return recipient;
    }

    public boolean isSent() {
        return sent;
    }
}

