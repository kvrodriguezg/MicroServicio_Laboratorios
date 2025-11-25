#Etapa 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build

#Establecer directorio de trabajo
WORKDIR /app

#Copiar archivos de configuración de Maven
COPY pom.xml .

#Descargar dependencias (esto se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

#Copiar el código fuente
COPY src ./src

#Compilar la aplicación
RUN mvn clean package -DskipTests

#Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine

#Instalar herramientas necesarias
RUN apk add --no-cache bash curl

#Crear directorio para el wallet de Oracle
RUN mkdir -p /Wallet_CRYHZIE2RBKI7PDG

#Establecer directorio de trabajo
WORKDIR /app

#Copiar el JAR compilado desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

#Crear un usuario no-root para ejecutar la aplicación
RUN addgroup -S spring && adduser -S spring -G spring
RUN chown -R spring:spring /app /Wallet_CRYHZIE2RBKI7PDG
USER spring:spring

#Exponer el puerto de la aplicación
EXPOSE 9090

#Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

#Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:9090/actuator/health || exit 1

#Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
