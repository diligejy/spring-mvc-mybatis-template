FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/spring-mvc-mybatis-template.jar /spring-mvc-mybatis-template/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/spring-mvc-mybatis-template/app.jar"]
