#!/bin/sh
#变量设置
SERVICE_NAME=wine_paycenter
PROJECT_DIR=$(cd "$(dirname "$0")"; pwd)
PROJECT_DIR=$PROJECT_DIR/..
CONFIG_DIR=$PROJECT_DIR/config
LIB_DIR=$PROJECT_DIR/lib
LIB_JARS=`ls $LIB_DIR | grep .jar | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":"`
MAIN_CLASS="com.baibei.wine.paycenter.Bootstrap"
# 设置classpath
#nohup java -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS &
java -Dspring.profiles.active=fat -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS