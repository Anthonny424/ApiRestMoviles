FROM eclipse-temurin:21.0.3_9-jdk
EXPOSE 8080
# CREAR CARPETA DE MI CONTENEDOR
WORKDIR /root
# METER EL POM, EL MAVEN Y EL MAVEN EXECUTABLE EN NUESTRA CARPETA
COPY ./pom.xml /root
COPY ./.mvn /root/.mvn
COPY ./mvnw /root

# DESCARGAR LAS DEPENDENCIAS
RUN ./mvnw dependency:go-offline
# METER EL CODIGO FUENTE
COPY ./src /root/src

#CONSTRIUR APLICACION
RUN ./mvnw clean install -DskipTest

ENTRYPOINT ["java","-jar","/root/target/demo-0.0.1-SNAPSHOT.jar"]

