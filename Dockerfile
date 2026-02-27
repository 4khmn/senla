FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jre17-temurin-jammy
WORKDIR /usr/local/tomcat/webapps/

RUN rm -rf ROOT && rm -rf examples && rm -rf docs

COPY --from=build /app/target/*.war ./ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]