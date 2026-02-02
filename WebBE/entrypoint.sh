#!/bin/sh

# Default to user-web if not set
MODULE_NAME=${MODULE_NAME:-ueit-user-web}

echo "Starting module: $MODULE_NAME"

JAR_FILE="/app/${MODULE_NAME}.jar"

if [ -f "$JAR_FILE" ]; then
    exec java -jar "$JAR_FILE"
else
    echo "Error: Jar file $JAR_FILE not found!"
    ls -l /app
    exit 1
fi
