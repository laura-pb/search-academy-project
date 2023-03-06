FROM openjdk:17-jdk
VOLUME /tmp
COPY target/*.jar search-academy-project.jar
ENTRYPOINT ["java", "-jar", "/search-academy-project.jar"]