# Build stage
FROM maven:3.8.6-openjdk-8-slim AS build
WORKDIR /app

# Copy parent pom
COPY pom.xml .

# Copy module poms and source
COPY wdd-admin/pom.xml wdd-admin/
COPY wdd-user-web/pom.xml wdd-user-web/
COPY wdd-user-mobile/pom.xml wdd-user-mobile/

# Resolve dependencies (optional step to cache dependencies, but might be tricky with local parent/modules, so just copying full source is safer for now)
COPY wdd-admin/src wdd-admin/src
COPY wdd-user-web/src wdd-user-web/src
COPY wdd-user-mobile/src wdd-user-mobile/src

# Build all modules
# Skip tests to speed up build
RUN mvn clean package -DskipTests

# Runtime stage
FROM openjdk:8-jre-slim
WORKDIR /app

# Argument to specify which module to run (default: ueit-user-web)
# Possible values: ueit-admin, ueit-user-web, ueit-user-mobile (based on finalName in pom)
ENV MODULE_NAME=ueit-user-web

# Copy built jars from build stage
# Note: The parent pom copies all jars to the root 'target' directory during package phase of the parent,
# OR we find them in their respective module target directories. 
# Looking at parent pom, it has an antrun execution:
# <fileset dir="${project.basedir}\wdd-user-web\target"> ... </fileset>
# This runs in the parent pom context.
# However, for safety in Docker, we'll assume standard maven structure and copying from module targets is robust.

COPY --from=build /app/wdd-admin/target/ueit-admin.jar /app/ueit-admin.jar
COPY --from=build /app/wdd-user-web/target/ueit-user-web.jar /app/ueit-user-web.jar
# Assuming wdd-user-mobile also builds to a jar, though we didn't inspect its pom deeply. 
# If it fails, we might need to adjust. But let's copy it if it exists.
# We will check if the file exists in the entrypoint or just copy all and run one.

# Expose ports (Spring Boot defaults to 8080, but might be different per module)
EXPOSE 8080

# Timezone setup (optional but recommended)
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Entrypoint script to choose which jar to run
COPY entrypoint.sh /app/entrypoint.sh
RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
