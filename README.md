
## Motivation
### Chatbot with Langchain4j
https://javaetmoi.com/2024/11/integrer-un-chatbot-dans-une-webapp-java-avec-langchain4j/

## Introduction
- Chatbot Backend System have been using Langchain4j to communicate with the backend Ollama local AI system
- In order to generate responses to users, application use Langchain4j Tool(Function Calling) and RAG (Retrieval Augmented Generation/ https://docs.langchain4j.dev/tutorials/rag#easy-rag).

## Frameworks
- Spring Boot
- Langchain4j
- Lombok
- JUnit

## 4. Docker container creation and execution command
```
docker build -t demo-chatbot:local .  && docker run -p 9090:8080  -e"SPRING_PROFILES_ACTIVE=local" -e"APPLICATION_PDF=pdf"  demo-chatbot:local
```