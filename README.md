# PromptLab

PromptLab is a Java-based console application designed to explore and compare different prompt engineering techniques using a Large Language Model (LLM) API.

Instead of sending only one prompt to the model, PromptLab sends both:

- the user's **original prompt**
- an **engineered version** of the same prompt

to the same LLM and displays the responses separately.

The goal is to make the effects of prompt engineering observable rather than treating prompt design only as a theoretical concept.

---

## 🎯 Project Motivation

This project started as a practical follow-up to my Prompt Engineering studies.

Rather than learning techniques such as Role / Context / Goal, Constraints, and Output Formatting only theoretically, I wanted to apply them programmatically and observe how they influence LLM responses.

While developing PromptLab, I also practiced working with Java, HTTP, REST APIs, JSON, environment variables, and LLM API integration.

---

## ⚙️ How It Works

The application follows a simple comparison flow:

```text
User enters an original prompt
            |
            v
Select a prompt engineering technique
            |
            v
PromptGenerator creates an engineered prompt
            |
            +--------------------------+
            |                          |
            v                          v
   Original Prompt             Engineered Prompt
            |                          |
            v                          v
        LLM API                    LLM API
            |                          |
            v                          v
    Original Response          Engineered Response
            \                          /
             \                        /
              ------ Comparison ------
```

Both prompts are sent as separate requests to the same model.

This makes it possible to observe what changed after applying a specific prompt engineering technique.

---

## 🧠 Prompt Engineering Techniques

PromptLab currently supports three techniques.

### 1. Role / Context / Goal — RCG

Adds a defined role, context, and goal to guide how the model approaches the task.

Its main purpose is to improve **audience alignment, context awareness, and response framing**.

### 2. Constraints

Adds explicit limitations such as response length, scope, repetition, and number of examples.

Its main purpose is to create **more focused and controlled responses**.

### 3. Output Format

Defines the structure the model should follow when generating its response.

Its main purpose is to make outputs more **predictable and consistent**.

---

## 🧪 Experiments

To evaluate the techniques, I ran comparisons using the same model for both the original and engineered prompts.

The experiments below were executed using:

- **Model:** `qwen/qwen3.6-27b`
- **API:** Groq Chat Completions API
- **Application:** PromptLab
- **Requests per experiment:** 2
  - one original prompt request
  - one engineered prompt request

> **Note:** LLM responses are not deterministic. These experiments represent individual runs and should be interpreted as observations rather than fixed benchmark results.

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

Interestingly, the engineered response was **longer**, not shorter.

The original response was already a strong general explanation of abstraction. It included programming concepts, analogies, examples, and explanations of abstract classes and interfaces.

However, the engineered response changed the **way the topic was presented**.

It explicitly recognized the reader as a software engineering student, used a more instructional and step-by-step style, and even added a small practice exercise.

### Observation

The RCG technique did not automatically make the answer shorter or universally "better."

Its main observed effect was:

> **Better alignment with the intended audience and learning context.**

This experiment also demonstrates an important point: prompt engineering is not always about producing more content or fewer tokens.

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

The original response expanded into a detailed comparison containing:

- performance characteristics
- multiple code examples
- memory usage
- cache behavior
- common misconceptions
- decision recommendations
- additional data structure suggestions

The engineered response stayed focused on the main differences between `ArrayList` and `LinkedList`, explained when each should be used, and included only one compact example.

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

This experiment suggests that constraints can be especially useful when an application needs:

- shorter responses
- controlled scope
- less repetition
- lower token usage
- more predictable response length

However, natural-language constraints such as `"use no more than 300 words"` are still instructions to the model rather than hard programmatic limits.

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

The original response selected its own structure and included sections such as:

- Overview
- HTTP Request
- HTTP Response
- Step-by-Step Flow
- HTTP Status Codes
- Key Concepts

The engineered response instead followed the requested structure directly:

```text
1. Short Answer
2. Key Points
3. Example
4. Final Summary
```

### Observation

The most important result of this experiment was **not the reduction in tokens**.

The main effect was:

> **Structural control and predictability.**

The model followed the predefined format, making the response easier to anticipate and potentially easier for another program to process.

This can be especially useful in applications where LLM responses need to follow a consistent structure.

---

### 📊 Experiment Summary

| Technique | Main Observed Effect |
|---|---|
| **Role / Context / Goal** | Better audience and context alignment |
| **Constraints** | Better control over scope and response length |
| **Output Format** | More predictable response structure |

The experiments also showed that an engineered prompt does not automatically produce a universally "better" answer.

Different prompt techniques solve different problems.

For example:

- RCG may increase response length while improving audience alignment.
- Constraints can significantly reduce unnecessary detail.
- Output formatting can improve structural consistency without necessarily improving factual quality.

The effectiveness of a prompt should therefore be evaluated based on the **goal of the prompt**, not only by response length or complexity.

---

## 🏗️ Project Structure

```text
PromptLab
│
└── src
    └── promptlab
        ├── PromptLabApp.java
        └── PromptGenerator.java
```

- **`PromptLabApp`** — handles console interaction, API communication, JSON processing, and response display.
- **`PromptGenerator`** — creates engineered prompts for each supported technique.

Current prompt generation methods:

```text
createRoleContextGoalPrompt()
createConstraintsPrompt()
createOutputFormatPrompt()
```


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
Raw JSON Response
  ↓
Parse choices[0].message.content
  ↓
Display LLM Response
```
During development, the application displays the HTTP status code, raw JSON response, and parsed LLM response to make the API flow easier to observe and debug.

---
## 🛠️ Technologies Used

- Java
- Java `HttpClient`
- HTTP
- REST API
- JSON
- `org.json`
- Groq API
- Qwen LLM
- Git & GitHub

---

## 🔐 API Key Security

The Groq API key is **not hardcoded** in the source code.

Instead, PromptLab reads the key from an environment variable:

```java
String apiKey = System.getenv("GROQ_API_KEY");
```

This prevents the API key from being committed to the GitHub repository.

---

## 🚀 Running the Project

Before running PromptLab, make sure the `GROQ_API_KEY` environment variable is configured.

The project also requires the `org.json` library.

Then run the application and:

1. Select a prompt engineering technique.
2. Enter an original prompt.
3. PromptLab generates the engineered version.
4. Both prompts are sent separately to the LLM.
5. The responses are displayed for comparison.
   
---
## 📚 What I Learned
Developing PromptLab helped me connect prompt engineering concepts with practical Java and API development.
Through the project, I gained experience with:

- designing reusable prompt templates
- comparing original and engineered prompts
- understanding how different prompt techniques affect model behavior
- building and sending HTTP POST requests in Java
- creating and parsing JSON data
- working with API endpoints, headers, and status codes
- extracting LLM responses from nested JSON
- using environment variables for API key security
- refactoring repeated code into reusable methods
- separating prompt generation logic from API communication

One of the most important observations was that an engineered prompt is not automatically "better."

Effective prompt engineering depends on the goal: better context alignment, tighter scope, or a more predictable output structure.
---

## ⚠️ Limitations

PromptLab V1 is intentionally simple.
Current limitations include:

- only three prompt engineering techniques
- one console interaction per program run
- no persistent experiment history
- no automatic evaluation metric
- natural-language constraints are not hard limits
- LLM responses may vary between runs
- model responses may still contain factual or formatting errors

PromptLab evaluates how prompts influence model behavior; it does not automatically verify the factual correctness of generated responses.

---

## 🔮 Future Improvements

Possible future additions include:
- One-Shot and Few-Shot prompting
- Self-Critique and hallucination reduction techniques
- document-grounded prompts
- temperature and `top_p` experimentation
- configurable models
- experiment history and result exporting
- improved API error handling
- repeated experiments without restarting the application

---

## 💡 Final Takeaway

PromptLab started as a small prompt engineering exercise but evolved into a practical Java project combining prompt design, HTTP communication, REST APIs, JSON processing, and LLM integration.
The most valuable part of the project was not simply generating different responses.
It was being able to **observe and compare how specific prompt engineering decisions changed model behavior in practice.**
