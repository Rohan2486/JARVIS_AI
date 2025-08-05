# 🧠 JarvisAssistant – Java Voice AI Assistant

JarvisAssistant is a simple voice-activated AI assistant built in **Java**, using:
- 🎤 [Vosk](https://alphacephei.com/vosk/) for offline speech recognition
- 🗣️ [FreeTTS](https://freetts.sourceforge.net/) for text-to-speech
- 🤖 OpenAI (ChatGPT API) for intelligent responses
- ⚙️ System command execution (open browser, notepad, etc.)

---

## 🎯 Features

- Voice-command recognition using Vosk
- Offline text-to-speech with FreeTTS
- Ask anything via ChatGPT (`gpt-3.5-turbo`)
- Execute desktop tasks
- Java + Maven based

---

## 🧰 Requirements

- Java 11 or higher
- Maven
- Vosk model
- OpenAI API key

---

## 📦 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/JarvisAssistant.git
cd JarvisAssistant
```

### 2. Download Vosk Model

Download a model from: [https://alphacephei.com/vosk/models](https://alphacephei.com/vosk/models)  
Unzip it into a folder named `model/` at the root of the project:

```
JarvisAssistant/
└── model/
    └── vosk-model-small-en-us-0.15/
```

### 3. Add Libraries to `lib/`

Place these files into the `lib/` folder:

- `vosk.jar` from [Vosk GitHub Releases](https://github.com/alphacep/vosk-api/releases)
- Native binaries (according to OS):
  - Windows: `libvosk.dll`
  - Linux: `libvosk.so`
  - macOS: `libvosk.dylib`
- FreeTTS:
  - `freetts.jar`
  - `cmu_us_kal.jar` (voice jar)

---

## 🔑 API Key

Create a file called `apikey.txt` in your root folder and paste your OpenAI API key inside:

```
sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

---

## 🚀 Run the Assistant

```bash
mvn package
java -Djava.library.path=lib -cp "target/JarvisAssistant-1.0-SNAPSHOT.jar;lib/*" com.yourdomain.jarvis.JarvisAssistant
```

---

## 🗣️ Example Commands

- `"open browser"`
- `"what time is it"`
- `"shutdown"` / `"restart"`
- `"open notepad"`
- Ask anything: `"what is AI?"`

---

## 📁 Project Structure

```
JarvisAssistant/
├── src/
│   └── main/java/com/yourdomain/jarvis/JarvisAssistant.java
├── lib/
│   ├── vosk.jar
│   ├── freetts.jar
│   ├── cmu_us_kal.jar
│   ├── libvosk.dll (or .so/.dylib depending on OS)
├── model/
│   └── vosk-model-small-en-us-0.15/
├── apikey.txt
├── pom.xml
└── README.md
```

---

## 🧠 Credits

- [Vosk Speech Recognition](https://github.com/alphacep/vosk-api)
- [FreeTTS Text-to-Speech](https://freetts.sourceforge.net/)
- [OpenAI GPT-3.5 API](https://platform.openai.com/)

