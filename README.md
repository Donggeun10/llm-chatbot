
## Motivation
### Chatbot with Langchain4j
https://javaetmoi.com/2024/11/integrer-un-chatbot-dans-une-webapp-java-avec-langchain4j/

## Introduction
- Chatbot Backend System have been using Spring boot with Langchain4j to communicate with the backend Ollama local AI system docker container.
- In order to enhance answer, application is used Langchain4j Tool(Function Calling) and RAG (Retrieval Augmented Generation/https://docs.langchain4j.dev/tutorials/rag#easy-rag).
- Because Some models such as EXAONE are not supported Tools.
- Queries should be routed to the appropriate RAG document.
- However, Combining Tools and RAG is hard to develop. so, it is not fully implemented yet. It is under development.(12/18/2024)

## Frameworks
- Spring Boot
- Langchain4j
- Vaadin Hilla React (Frontend)
- Lombok
- JUnit

## 4. Docker container creation and execution command
```
docker build -t demo-chatbot:local .  && docker run -p 9090:8080  -e"SPRING_PROFILES_ACTIVE=exaone" -e"APPLICATION_PDF=pdf"  demo-chatbot:local
```