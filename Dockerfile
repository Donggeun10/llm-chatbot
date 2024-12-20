## build stage
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build
COPY pom.xml .
# 1) dependency caching
RUN mvn -B dependency:resolve

# 2) source 복사 & package
COPY src/ src/
#COPY frontend/ frontend/
#COPY node_modules/ node_modules/
#COPY package.json ./package.json
#COPY tsconfig.json ./tsconfig.json
#COPY mvnw ./mvnw
#COPY vite.config.ts ./vite.config.ts
#COPY types.d.ts ./types.d.ts
#RUN mvn package -DskipTests -Pproduction
RUN mkdir -p target
COPY target/chatbot.jar target/chatbot.jar
RUN java -Djarmode=tools -jar target/chatbot.jar extract --destination application

## run stage ubuntu, due to langchain4j dependency
FROM eclipse-temurin:21-jre-noble

## ubuntu
RUN addgroup appgroup && adduser appuser && adduser appuser appgroup
## alpine
#RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /home/appuser

# 2) Jar 복사
COPY --chown=appuser src/test/resources/pdf ./pdf
COPY --chown=appuser --from=builder /build/application/lib ./lib
COPY --chown=appuser --from=builder /build/application/chatbot.jar ./chatbot.jar

# 3) 실행
ENTRYPOINT ["java", "-jar", "chatbot.jar"]