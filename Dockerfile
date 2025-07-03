# Usa uma imagem Java leve
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o .jar para dentro da imagem
COPY target/AuthUserBackend-0.0.1-SNAPSHOT.jar app.jar

# Porta que a aplicação vai usar
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
