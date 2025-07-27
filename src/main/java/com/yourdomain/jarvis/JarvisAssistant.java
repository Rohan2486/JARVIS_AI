package com.yourdomain.jarvis;

import org.vosk.Model;
import org.vosk.Recognizer;
import org.vosk.LibVosk;
import org.vosk.Microphone;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.awt.Desktop;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

public class JarvisAssistant {

    static Voice voice;

    public static void main(String[] args) throws Exception {
        // Setup voice
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
        } else {
            System.out.println("Voice not found!");
            return;
        }

        speak("Hello, I am Jarvis. How can I help you?");

        LibVosk.setLogLevel(0);
        Model model = new Model("model"); // Ensure Vosk model is downloaded to ./model
        Microphone mic = new Microphone(16000);
        Recognizer recognizer = new Recognizer(model, 16000.0f);

        if (!mic.startRecording()) {
            System.out.println("Cannot start microphone.");
            return;
        }

        while (true) {
            byte[] data = mic.read();
            if (recognizer.acceptWaveForm(data, data.length)) {
                String command = recognizer.getResult().replaceAll(".*\"text\":\"|\"}.*", "").trim();
                System.out.println("Heard: " + command);

                if (command.contains("open browser")) {
                    speak("Opening browser");
                    Desktop.getDesktop().browse(new URI("https://www.google.com"));
                } else if (command.contains("what time is it")) {
                    speak("The time is " + LocalTime.now().getHour() + " " + LocalTime.now().getMinute());
                } else if (command.contains("shutdown")) {
                    speak("Shutting down the system");
                    Runtime.getRuntime().exec("shutdown -s -t 0");
                } else if (command.contains("restart")) {
                    speak("Restarting the system");
                    Runtime.getRuntime().exec("shutdown -r -t 0");
                } else if (command.contains("open notepad")) {
                    speak("Opening Notepad");
                    Runtime.getRuntime().exec("notepad");
                } else {
                    speak("Let me think...");
                    String response = getChatGPTResponse(command);
                    speak(response);
                }
            }
        }
    }

    public static void speak(String text) {
        System.out.println("Jarvis: " + text);
        voice.speak(text);
    }

    public static String getChatGPTResponse(String userInput) {
        try {
            String apiKey = Files.readString(Paths.get("apikey.txt")).trim();
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            HttpURLConnection conn;
            int retries = 3;
            int responseCode = 429;

            for (int attempt = 0; attempt < retries; attempt++) {
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String body = "{" +
                        "\"model\":\"gpt-3.5-turbo\"," +
                        "\"messages\":[{" +
                        "\"role\":\"user\",\"content\":\"" + userInput + "\"}]}";

                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = body.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                responseCode = conn.getResponseCode();
                if (responseCode == 429) {
                    System.out.println("Rate limited. Retrying...");
                    Thread.sleep(3000);
                } else {
                    break;
                }
            }

            if (responseCode != 200) {
                return "Sorry, I couldn't contact the AI service.";
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line.trim());
            }

            String full = response.toString();
            int start = full.indexOf("\"content\":\"") + 11;
            int end = full.indexOf("\"", start);
            if (start > 10 && end > start) {
                return full.substring(start, end);
            } else {
                return "Sorry, I couldn't understand the AI's response.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, an error occurred.";
        }
    }
}
