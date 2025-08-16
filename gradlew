#!/usr/bin/env sh
##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################
if [ -n "$JAVA_HOME" ] ; then
    JAVA="$JAVA_HOME/bin/java"
else
    JAVA="java"
fi

STARTER_MAIN_CLASS=org.gradle.wrapper.GradleWrapperMain
CLASSPATH=gradle/wrapper/gradle-wrapper.jar

exec "$JAVA" -cp "$CLASSPATH" $STARTER_MAIN_CLASS "$@"
