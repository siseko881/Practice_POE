/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.practice_poe;

/**
 *
 * @author RC_Student_lab
 */

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Practice_POE {


    private static List<String> sentMessages = new ArrayList<>();
    private static List<String> disregardedMessages = new ArrayList<>();
    private static List<String> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<Long> messageIDs = new ArrayList<>();
    private static List<String> recipients = new ArrayList<>();

    public static boolean isValidPhoneNumber(String phone) {
        if (!phone.startsWith("+")) return false;
        String digits = phone.substring(1);
        return digits.matches("\\d{10,14}");
    }

    public static void main(String[] args) {
        Login objlogin = new Login();
        Random rand = new Random();
        Map<Long, MessageData> messageMap = new HashMap<>();
        int messageCounter = 1;

        // === Registration ===
        String username = JOptionPane.showInputDialog("=== Register ===\nEnter a username:");
        if (username == null || !objlogin.isValidUsername(username)) {
            JOptionPane.showMessageDialog(null, "Invalid username.\nIt must contain '_' and be at least 5 characters.");
            return;
        }

        String password = JOptionPane.showInputDialog("Enter a password:");
        if (password == null || !objlogin.isValidPassword(password)) {
            JOptionPane.showMessageDialog(null, "Invalid password.\nMust be 8+ characters, include a capital letter and a special character.");
            return;
        }

        String phoneNumber = JOptionPane.showInputDialog("Enter your phone number (e.g. +27123456789):");
        if (phoneNumber == null || !isValidPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(null, "Invalid phone number.\nMust include '+' and 10–14 digits.");
            return;
        }

        // === Login ===
        String loginUsername = JOptionPane.showInputDialog("=== Login ===\nEnter your username:");
        String loginPassword = JOptionPane.showInputDialog("Enter your password:");

        if (!username.equals(loginUsername) || !password.equals(loginPassword)) {
            JOptionPane.showMessageDialog(null, "Login failed. Username or password incorrect.");
            return;
        }

        // Show user details
        String userInfo = objlogin.verifyDetails(username, password, phoneNumber);
        int confirm = JOptionPane.showConfirmDialog(null, userInfo + "\n\nIs this information correct?", "Verify Info", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "Login cancelled.");
            return;
        }

        JOptionPane.showMessageDialog(null, "Login successful!\nWelcome " + username + " to QuickChat!");

        // === Main QuickChat Loop ===
        while (true) {
            String[] options = {"Send Message", "View History", "Advanced Features", "Quit"};
            int choice = JOptionPane.showOptionDialog(null, "Choose an option:", "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) { // Send message
                String input = JOptionPane.showInputDialog("How many messages would you like to send?");
                if (input == null) continue;

                int numMessages;
                try {
                    numMessages = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid number.");
                    continue;
                }

                for (int i = 0; i < numMessages; i++) {
                    String message = JOptionPane.showInputDialog("Your Message " + (i + 1) + " (type 'exit' to cancel):");
                    if (message == null || message.equalsIgnoreCase("exit")) return;

                    if (!objlogin.checkMessage(message)) {
                        JOptionPane.showMessageDialog(null, "Message exceed 250 characters.");
                        continue;
                    }

                    String recipient = JOptionPane.showInputDialog("Enter recipient phone number (e.g. +27611234567):");
                    if (recipient == null || !isValidPhoneNumber(recipient)) {
                        JOptionPane.showMessageDialog(null, "Cell phone is incorrectly formatted or does not contain international code. Please correct the number and try again");
                        continue;
                    }

                    String[] actions = {"Send Message", "Disregard", "Store Only"};
                    int actionChoice = JOptionPane.showOptionDialog(null, "Choose what to do with the message:",
                            "Message Action", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                            null, actions, actions[0]);

                    // Generate ID and Hash
                    Long messageId;
                    do {
                        messageId = 1_000_000_000L + (long) (rand.nextDouble() * 9_999_999_999L);
                    } while (messageMap.containsKey(messageId));

                    String[] words = message.trim().split("\\s+");
                    String firstWord = words.length > 0 ? words[0] : "";
                    String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;
                    String hash = messageId.toString().substring(0, 2) + ":" + messageCounter + ":" + firstWord + "-" + lastWord;

                    // Handle message based on action
                    if (actionChoice == 1) { // Disregard
                        disregardedMessages.add(message);
                        JOptionPane.showMessageDialog(null, "Message disregarded.");
                        continue;
                    } else if (actionChoice == 0) { // Send
                        sentMessages.add(message);
                        recipients.add(recipient);
                        messageIDs.add(messageId);
                        messageHashes.add(hash);
                        messageMap.put(messageId, new MessageData(message, hash, recipient, true));
                        JOptionPane.showMessageDialog(null, String.format("Message Sent\n\nID: %d\nHash: %s\nRecipient: %s",
                                messageId, hash, recipient));
                    } else { // Store Only
                        storedMessages.add(message);
                        recipients.add(recipient);
                        messageIDs.add(messageId);
                        messageHashes.add(hash);
                        messageMap.put(messageId, new MessageData(message, hash, recipient, false));
                        JOptionPane.showMessageDialog(null, String.format("Message Stored Only\n\nID: %d\nHash: %s\nRecipient: %s",
                                messageId, hash, recipient));
                    }
                    messageCounter++;
                }

            } else if (choice == 1) { // View History
                if (messageMap.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No messages in history.");
                } else {
                    StringBuilder history = new StringBuilder("---- Message History ----\n\n");
                    for (Map.Entry<Long, MessageData> entry : messageMap.entrySet()) {
                        MessageData data = entry.getValue();
                        history.append("ID: ").append(entry.getKey()).append("\n")
                               .append("Message: ").append(data.getText()).append("\n")
                               .append("Hash: ").append(data.getHash()).append("\n")
                               .append("Recipient: ").append(data.getRecipient()).append("\n")
                               .append("Status: ").append(data.isSent() ? "Sent" : "Stored").append("\n\n");
                    }

                    JTextArea area = new JTextArea(history.toString());
                    area.setEditable(false);
                    JScrollPane scroll = new JScrollPane(area);
                    scroll.setPreferredSize(new java.awt.Dimension(450, 300));
                    JOptionPane.showMessageDialog(null, scroll, "Message History", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (choice == 2) { // Advanced Features
                showAdvancedFeatures();

            } else { // Quit
                JOptionPane.showMessageDialog(null, "Goodbye!");
                return;
            }
        }
    }

    private static void showAdvancedFeatures() {
        String[] advancedOptions = {
            "Display Sent Messages Summary",
            "Find Longest Message",
            "Search by Message ID",
            "Search by Recipient",
            "Delete Message by Hash",
            "Full Report of Sent Messages",
            "Back to Main Menu"
        };

        int choice = JOptionPane.showOptionDialog(null, "Choose an advanced feature:", "Advanced Features",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, advancedOptions, advancedOptions[0]);

        switch (choice) {
            case 0: displaySentMessagesSummary(); break;
            case 1: displayLongestMessage(); break;
            case 2: searchByMessageID(); break;
            case 3: searchByRecipient(); break;
            case 4: deleteMessageByHash(); break;
            case 5: displayFullReport(); break;
            default: return;
        }
    }

    private static void displaySentMessagesSummary() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
            return;
        }

        StringBuilder summary = new StringBuilder("=== Sent Messages Summary ===\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            summary.append("Message ").append(i + 1).append(":\n")
                   .append("Recipient: ").append(recipients.get(i)).append("\n")
                   .append("Message: ").append(sentMessages.get(i)).append("\n\n");
        }

        JTextArea area = new JTextArea(summary.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(450, 300));
        JOptionPane.showMessageDialog(null, scroll, "Sent Messages Summary", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
            return;
        }

        String longestMessage = "";
        int maxLength = 0;

        for (String message : sentMessages) {
            if (message.length() > maxLength) {
                maxLength = message.length();
                longestMessage = message;
            }
        }

        JOptionPane.showMessageDialog(null,
            "=== Longest Sent Message ===\n\n" +
            "Length: " + maxLength + " characters\n" +
            "Message: " + longestMessage);
    }

    private static void searchByMessageID() {
        String idInput = JOptionPane.showInputDialog("Enter Message ID to search:");
        if (idInput == null) return;

        try {
            Long searchId = Long.parseLong(idInput);
            int index = messageIDs.indexOf(searchId);

            if (index != -1) {
                String message;
                if (index < sentMessages.size()) {
                    message = sentMessages.get(index);
                } else {
                    message = storedMessages.get(index - sentMessages.size());
                }
                JOptionPane.showMessageDialog(null,
                    "=== Message Found ===\n\n" +
                    "ID: " + searchId + "\n" +
                    "Recipient: " + recipients.get(index) + "\n" +
                    "Message: " + message);
            } else {
                JOptionPane.showMessageDialog(null, "Message ID not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid Message ID format.");
        }
    }

    private static void searchByRecipient() {
        String searchRecipient = JOptionPane.showInputDialog("Enter recipient phone number:");
        if (searchRecipient == null) return;

        StringBuilder results = new StringBuilder("=== Messages to " + searchRecipient + " ===\n\n");
        boolean found = false;

        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).equals(searchRecipient)) {
                found = true;
                String message;
                if (i < sentMessages.size()) {
                    message = sentMessages.get(i);
                } else {
                    message = storedMessages.get(i - sentMessages.size());
                }
                results.append("ID: ").append(messageIDs.get(i)).append("\n")
                       .append("Message: ").append(message).append("\n")
                       .append("Hash: ").append(messageHashes.get(i)).append("\n\n");
            }
        }
        

        if (!found) {
            JOptionPane.showMessageDialog(null, "No messages found for this recipient.");
        } else {
            JTextArea area = new JTextArea(results.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new java.awt.Dimension(450, 300));
            JOptionPane.showMessageDialog(null, scroll, "Messages by Recipient", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public static String findLongestMessage(List<String> messages) {
    return messages.stream().max(Comparator.comparingInt(String::length)).orElse("");
}

public static boolean deleteByHash(String hash,
                                   List<String> hashes,
                                   List<Long> ids,
                                   List<String> recipients,
                                   List<String> sent,
                                   List<String> stored) {
    int index = hashes.indexOf(hash);
    if (index == -1) return false;

    hashes.remove(index);
    ids.remove(index);
    recipients.remove(index);

    if (index < sent.size()) {
        sent.remove(index);
    } else {
        stored.remove(index - sent.size());
    }

    return true;
}

public static List<String> findMessagesByRecipient(String recipient, List<String> recipients, List<String> sent, List<String> stored) {
    List<String> results = new ArrayList<>();
    for (int i = 0; i < recipients.size(); i++) {
        if (recipients.get(i).equals(recipient)) {
            if (i < sent.size()) {
                results.add(sent.get(i));
            } else {
                results.add(stored.get(i - sent.size()));
            }
        }
    }
    return results;
}

public static String findMessageByID(Long id, List<Long> ids, List<String> sent, List<String> stored) {
    int index = ids.indexOf(id);
    if (index == -1) return null;

    return index < sent.size() ? sent.get(index) : stored.get(index - sent.size());
}


    private static void deleteMessageByHash() {
        String searchHash = JOptionPane.showInputDialog("Enter message hash to delete:");
        if (searchHash == null) return;

        int index = messageHashes.indexOf(searchHash);
        if (index != -1) {
            messageHashes.remove(index);
            messageIDs.remove(index);
            recipients.remove(index);

            if (index < sentMessages.size()) {
                sentMessages.remove(index);
            } else {
                storedMessages.remove(index - sentMessages.size());
            }

            JOptionPane.showMessageDialog(null, "Message deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Message hash not found.");
        }
    }

    private static void displayFullReport() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages found.");
            return;
        }

        StringBuilder report = new StringBuilder("=== FULL SENT MESSAGES REPORT ===\n\n");
        report.append("Total Sent Messages: ").append(sentMessages.size()).append("\n");
        report.append("Total Stored Messages: ").append(storedMessages.size()).append("\n");
        report.append("Total Disregarded Messages: ").append(disregardedMessages.size()).append("\n\n");

        report.append("=== DETAILED SENT MESSAGES ===\n\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            report.append("Message #").append(i + 1).append("\n")
                  .append("ID: ").append(messageIDs.get(i)).append("\n")
                  .append("Hash: ").append(messageHashes.get(i)).append("\n")
                  .append("Recipient: ").append(recipients.get(i)).append("\n")
                  .append("Message: ").append(sentMessages.get(i)).append("\n")
                  .append("Length: ").append(sentMessages.get(i).length()).append(" characters\n")
                  .append("-------------------\n\n");
        }

        JTextArea area = new JTextArea(report.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new java.awt.Dimension(500, 400));
        JOptionPane.showMessageDialog(null, scroll, "Full Messages Report", JOptionPane.INFORMATION_MESSAGE);
    }
}


