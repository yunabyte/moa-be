FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon || true

COPY . .
RUN ./gradlew clean build -x test

FROM eclipse-temurin:21-jdk
WORKDIR /app

ENV SERVER_PORT=8080
ENV DB_URL=${DB_URL}
ENV DB_USERNAME=${DB_USERNAME}
ENV DB_PASSWORD=${DB_PASSWORD}
ENV REDIS_HOST=${REDIS_HOST}
ENV REDIS_PORT=${REDIS_PORT}


COPY --from=builder /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]