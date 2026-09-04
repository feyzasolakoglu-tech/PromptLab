# PromptLab

PromptLab is a Java-based console application for comparing different prompt engineering techniques using a Large Language Model (LLM) API.

For each experiment, the application sends both:

- the user's **original prompt**
- an **engineered version** of the same prompt

to the same model and displays the results separately.

PromptLab also measures token usage and latency, and performs basic programmatic validation for measurable prompt requirements.

---

## 🎯 Project Motivation

This project started as a practical follow-up to my Prompt Engineering studies.

I wanted to move beyond theory and experiment with how techniques such as Role / Context / Goal, Constraints, and Output Formatting influence LLM responses while also practicing Java, HTTP, REST APIs, JSON, and LLM API integration.

---

## ⚙️ How It Works

```text
Original Prompt
      |
      v
Select Technique
      |
      v
PromptGenerator
      |
      +----------------------+
      |                      |
      v                      v
Original Prompt       Engineered Prompt
      |                      |
      v                      v
    LLM API                LLM API
      |                      |
      v                      v
Original Response     Engineered Response
      \                      /
       \                    /
          --- Comparison ---
```

Both prompts are sent as separate requests to the same model so their outputs can be compared under the same conditions.

---

## 🧠 Prompt Engineering Techniques

PromptLab currently supports three techniques:

### 1. Role / Context / Goal — RCG

Adds a defined role, context, and goal to guide how the model approaches the task.

Main purpose:

> **Audience alignment, context awareness, and response framing**

### 2. Constraints

Adds explicit limitations such as response length, scope, repetition, and number of examples.

Main purpose:

> **More focused and controlled responses**

### 3. Output Format

Defines the structure the model should follow when generating its response.

Main purpose:

> **More predictable and consistent outputs**

---

# 🧪 Experiments

To evaluate the techniques, I ran comparisons using the same model for both the original and engineered prompts.

- **Model:** `qwen/qwen3.6-27b`
- **API:** Groq Chat Completions API
- **Requests per experiment:** 2
  - one original prompt request
  - one engineered prompt request

> **Note:** LLM responses are not deterministic. These results represent individual runs and should be interpreted as observations rather than fixed benchmarks.

---

## Experiment 1 — Role / Context / Goal

### Original Prompt

```text
Explain abstraction.
```

### Engineered Prompt

```text
Role:
You are an experienced and patient instructor.

Context:
The user is a software engineering student who is learning the topic.

Goal:
Explain the requested topic clearly and at a beginner-friendly level.

User Task:
Explain abstraction.
```

### Results

| Metric | Original | Engineered |
|---|---:|---:|
| Completion Tokens | 884 | 1511 |
| HTTP Status | 200 | 200 |
| Completion Status | Completed | Completed |

The engineered response was longer than the original.

However, the main difference was not response length. The engineered version used a more instructional and step-by-step style and adapted the explanation to the intended audience.

### Observation

The main observed effect was:

> **Better alignment with the intended audience and learning context.**

This experiment showed that prompt engineering is not always about reducing tokens.

Sometimes its value is in controlling **how the model approaches the user and the task**.

---

## Experiment 2 — Constraints

### Original Prompt

```text
Compare ArrayList and LinkedList in Java and explain when each should be used.
```

### Engineered Prompt

```text
User Task:
Compare ArrayList and LinkedList in Java and explain when each should be used.

Constraints:
- Keep the response concise and focused.
- Use no more than 300 words.
- Avoid unnecessary repetition.
- Include only the most important information.
- Use at most one short example if needed.
```

### Results

| Metric | Original | Engineered |
|---|---:|---:|
| Completion Tokens | 1435 | 427 |
| HTTP Status | 200 | 200 |
| Completion Status | Completed | Completed |

The original response expanded into a detailed comparison with multiple examples and additional technical details.

The engineered response stayed focused on the main differences, explained when each structure should be used, and used only one compact example.

The completion output decreased from:

```text
1435 tokens → 427 tokens
```

This represents approximately a:

```text
70% reduction
```

in completion tokens for this particular run.

### Observation

The Constraints technique produced the clearest reduction in unnecessary detail.

The core information was still preserved while the response became significantly more focused.

Natural-language constraints such as `"use no more than 300 words"` are still instructions to the model rather than hard generation limits.

---

## Experiment 3 — Output Format

### Original Prompt

```text
Explain how an HTTP request and response work.
```

### Engineered Prompt

```text
User Task:
Explain how an HTTP request and response work.

Output Format:
1. Short Answer:
2. Key Points:
3. Example:
4. Final Summary:
```

### Results

| Metric | Original | Engineered |
|---|---:|---:|
| Completion Tokens | 992 | 608 |
| HTTP Status | 200 | 200 |
| Completion Status | Completed | Completed |

The original response selected its own structure.

The engineered response instead followed the requested format directly:

```text
1. Short Answer
2. Key Points
3. Example
4. Final Summary
```

### Observation

The main effect was:

> **Structural control and predictability.**

The predefined format made the response easier to anticipate and potentially easier for another program to process.

---

## 📊 Experiment Summary

| Technique | Main Observed Effect |
|---|---|
| **Role / Context / Goal** | Better audience and context alignment |
| **Constraints** | Better control over scope and response length |
| **Output Format** | More predictable response structure |

These experiments showed that an engineered prompt does not automatically produce a universally "better" answer.

Different prompt techniques solve different problems, so their effectiveness should be evaluated according to the goal of the prompt.

---

## 📏 Measurement & Validation

PromptLab also measures and validates selected response characteristics.

| Feature | Purpose |
|---|---|
| **Token Usage** | Parses prompt, completion, and total tokens from the API response |
| **Latency** | Measures elapsed time for original and engineered requests |
| **Constraint Validation** | Checks whether the engineered response stays within the 300-word limit |
| **Output Format Validation** | Checks whether all required sections are present |

Token usage is parsed directly from the API response.

Latency is measured using Java's `System.nanoTime()`.

For the Constraints technique, PromptLab counts the words in the engineered response and reports:

```text
Word Limit: PASSED
```

or:

```text
Word Limit: VIOLATED
```

For the Output Format technique, the application checks whether these sections are present:

```text
Short Answer:
Key Points:
Example:
Final Summary:
```

and reports whether the requested structure was followed.

These checks validate measurable requirements only; they do not evaluate factual correctness or overall response quality.

---

## 🏗️ Project Structure

```text
PromptLab
│
└── src
    └── promptlab
        ├── PromptLabApp.java
        ├── PromptGenerator.java
        └── TokenUsage.java
```

- **`PromptLabApp`** — handles console interaction, API communication, measurements, validation, and response display.
- **`PromptGenerator`** — creates engineered prompts for each supported technique.
- **`TokenUsage`** — stores parsed prompt, completion, and total token usage values.

---

## 🌐 API Flow

PromptLab uses Java's built-in `HttpClient`.

```text
Prompt
  ↓
Create JSON Request Body
  ↓
Create HTTP Request
  ↓
POST Request to LLM API
  ↓
HTTP Response
  ↓
Parse LLM Response
  ↓
Parse Token Usage
  ↓
Measure / Validate
  ↓
Display Results
```

During development, the application also displays the status code, raw JSON response, and parsed LLM response to make the API flow easier to observe and debug.

---

## 🛠️ Technologies Used

- Java
- Java `HttpClient`
- HTTP / REST API
- JSON / `org.json`
- Groq API
- Qwen LLM
- Git & GitHub

---

## 🔐 API Key Security

The Groq API key is not hardcoded in the source code.

Instead, PromptLab reads it from an environment variable:

```java
String apiKey = System.getenv("GROQ_API_KEY");
```

This prevents the API key from being committed to the repository.

---

## 🚀 Running the Project

Before running PromptLab:

1. Configure the `GROQ_API_KEY` environment variable.
2. Make sure the `org.json` library is available.
3. Run the application.
4. Select a prompt engineering technique and enter a prompt.
5. Compare the generated responses and measurements.

---

## 📚 What I Learned

Through PromptLab, I practiced:

- designing and comparing reusable prompt engineering techniques
- sending HTTP requests and processing JSON responses with Java
- working with REST API concepts, headers, status codes, and environment variables
- parsing token usage, measuring latency, and validating measurable response requirements
- refactoring repeated logic and separating prompt generation from API communication

The project also helped me understand the difference between **asking an LLM to follow an instruction** and **checking programmatically whether that instruction was actually followed**.

---

## ⚠️ Limitations

Current limitations include:

- only three prompt engineering techniques and one comparison per program run
- no persistent experiment history or multi-run statistics
- validation is limited to measurable requirements such as word limits and required sections
- factual correctness and semantic response quality are not automatically evaluated
- LLM responses and latency may vary between runs

---

## 🔮 Future Improvements

Possible future improvements include:

- multi-run experiments with aggregate statistics and result exporting
- improved API error handling and configurable models
- additional prompt techniques, technique combinations, and parameter experiments
- quality and cost evaluation, including possible LLM-as-a-Judge experiments

---

## 💡 Final Takeaway

PromptLab evolved from a small prompt engineering exercise into a Java application that combines LLM API integration with response measurement and basic programmatic validation.

Its main goal is to make the effects of different prompt engineering techniques **observable and measurable**.
