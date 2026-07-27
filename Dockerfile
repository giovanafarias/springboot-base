# Fase de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Fase de Execução
FROM eclipse-temurin:21-jre
WORKDIR /app

# 1. Criar o usuário e grupo (ainda como root)
RUN addgroup --system springboot && adduser --system --ingroup springboot springboot

# 2. Copiar o arquivo JAR (ainda como root)
COPY --from=build /app/target/*.jar app.jar

# 3. Ajustar as permissões da pasta /app para o novo usuário (ainda como root)
RUN chown -R springboot:springboot /app

# 4. SÓ AGORA mudar para o usuário sem privilégios
USER springboot

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]