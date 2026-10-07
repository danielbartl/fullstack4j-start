# fullstack4j: container image for start.fullstack4j.dev (built and run by Railway, or any
# container platform). The client is built by the frontend-maven-plugin, which downloads its
# own Node.js and Yarn, so the build stage needs nothing but a JDK.
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
COPY . .
# The build context has no .git directory, so the git details for /actuator/info are left out.
RUN ./mvnw -B -ntp package -DskipTests -Dmaven.gitcommitid.skip=true

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/start-site/target/start-site-exec.jar app.jar
# A fixed heap keeps memory (and the bill) predictable; generating projects needs little.
ENV JAVA_TOOL_OPTIONS="-Xmx512m"
EXPOSE 8080
# Railway injects PORT; fall back to 8080 everywhere else.
ENTRYPOINT ["sh", "-c", "exec java -Dserver.port=${PORT:-8080} -jar app.jar"]
