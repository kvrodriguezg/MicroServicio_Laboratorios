FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

#Copiar JAR generado
COPY target/*.jar app.jar

#Copiar Wallet
COPY Wallet_CRYHZIE2RBKI7PDG /app/Wallet_CRYHZIE2RBKI7PDG

#Puerto
EXPOSE 9091
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-Dserver.port=9091", "-jar", "app.jar"]