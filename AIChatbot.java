import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class AIChatbot {
    static class ChatbotEngine {

        Map<String, String> knowledgeBase = new HashMap<>();

        ChatbotEngine() {
            loadTrainingData();
        }
        void loadTrainingData() {
            File file = new File("training_data.txt");
            if (!file.exists()) {
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write("hi=Hello! How can I assist you?\n");
                    fw.write("hello=Hi! What can I do for you today?\n");
                    fw.write("your name=I am an AI Chatbot developed in Java.\n");
                    fw.write("what is java=Java is an object-oriented programming language.\n");
                    fw.write("what is ai=AI enables machines to simulate human intelligence.\n");
                    fw.write("what is internship=An internship provides real-world industry experience.\n");
                    fw.write("bye=Thank you for chatting. Have a great day!\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split("=");
                    knowledgeBase.put(data[0], data[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String preprocess(String text) {
            return text.toLowerCase()
                       .replaceAll("[^a-z ]", "")
                       .trim();
        }
        String getResponse(String userInput) {
            userInput = preprocess(userInput);

            for (String key : knowledgeBase.keySet()) {
                if (userInput.contains(key)) {
                    return knowledgeBase.get(key);
                }
            }

            return "Sorry, I didn't understand that. Please ask something else.";
        }
    }
    static class ChatbotGUI extends JFrame {

        JTextArea chatArea;
        JTextField inputField;
        JButton sendButton;
        ChatbotEngine engine = new ChatbotEngine();

        ChatbotGUI() {
            setTitle("AI Chatbot - Internship Project");
            setSize(500, 550);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            chatArea = new JTextArea();
            chatArea.setEditable(false);
            chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            JScrollPane scrollPane = new JScrollPane(chatArea);

            inputField = new JTextField();
            sendButton = new JButton("Send");

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(inputField, BorderLayout.CENTER);
            bottomPanel.add(sendButton, BorderLayout.EAST);

            add(scrollPane, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            chatArea.append("Bot: Hello! I'm your AI Chatbot \n\n");

            sendButton.addActionListener(e -> sendMessage());
            inputField.addActionListener(e -> sendMessage());

            setVisible(true);
        }

        void sendMessage() {
            String userText = inputField.getText();
            if (userText.isEmpty()) return;

            chatArea.append("You: " + userText + "\n");
            String reply = engine.getResponse(userText);
            chatArea.append("Bot: " + reply + "\n\n");
            inputField.setText("");
        }
    }
    public static void main(String[] args) {
        new ChatbotGUI();
    }
}
