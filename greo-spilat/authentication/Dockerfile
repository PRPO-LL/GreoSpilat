# syntax=docker/dockerfile:1

# ---- build stage: compile this service from the full Maven reactor ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# The whole reactor is the build context (parent pom + every module).
# .dockerignore keeps target/, .git, etc. out so the context stays lean.
COPY . .

# Build ONLY this service's -api module and the modules it depends on (-am = also-make).
# The BuildKit cache mount persists ~/.m2 across builds so deps aren't re-downloaded.
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -e -DskipTests -pl authentication/auth-api -am clean package

# ---- runtime stage: slim JRE, non-root ----
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --home-dir /app app

# KumuluzEE repackages an executable uber-jar; copy just that.
COPY --from=build /build/authentication/auth-api/target/auth-api-*.jar /app/app.jar

USER app
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
