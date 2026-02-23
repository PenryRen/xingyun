#!/bin/bash

#java容器优化参数
JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

java $JAVA_OPTS -jar wdd-admin.jar & admin=$!
java $JAVA_OPTS -jar wdd-user-web.jar & user=$!
java $JAVA_OPTS -jar wdd-user-mobile.jar & mobile=$!

wait $admin $user $mobile