#!/bin/sh

# Start Nginx in the background
echo "Starting Nginx..."
nginx -g "daemon off;" &
NGINX_PID=$!

# Start User-Web Backend
echo "Starting User-Web Backend (Port 16000)..."
java -jar /app/ueit-user-web.jar > /app/user-web.log 2>&1 &
USER_WEB_PID=$!

# Start Admin Backend
echo "Starting Admin Backend (Port 16002)..."
java -jar /app/ueit-admin.jar > /app/admin.log 2>&1 &
ADMIN_PID=$!

# Start Mobile Backend
echo "Starting Mobile Backend (Port 6007)..."
java -jar /app/ueit-user-mobile.jar > /app/mobile.log 2>&1 &
MOBILE_PID=$!

echo "Processes started: Nginx($NGINX_PID), User-Web($USER_WEB_PID), Admin($ADMIN_PID), Mobile($MOBILE_PID)"

# Wait for any process to exit
wait -n

echo "One of the processes has exited. Stopping all..."
kill $NGINX_PID $USER_WEB_PID $ADMIN_PID $MOBILE_PID
exit 1
