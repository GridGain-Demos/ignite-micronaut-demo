FROM openjdk:14-alpine
COPY target/ignite-micronaut-demo-*.jar ignite-micronaut-demo.jar
EXPOSE 8080
CMD ["java", "-Dcom.sun.management.jmxremote", "-Xmx128m", "-jar", "ignite-micronaut-demo.jar"]