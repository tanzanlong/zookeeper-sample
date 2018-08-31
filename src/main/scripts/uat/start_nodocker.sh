#!/bin/sh
#变量设置
SERVICE_NAME=discrepancy-money-service
PROJECT_DIR=$(cd "$(dirname "$0")"; pwd)
PROJECT_DIR=$PROJECT_DIR/..
CONFIG_DIR=$PROJECT_DIR/config
LIB_DIR=$PROJECT_DIR/lib
LIB_JARS=`ls $LIB_DIR | grep .jar | awk '{print "'$LIB_DIR'/"$0}' | tr "\n" ":"`
mkdir -p /logs/$SERVICE_NAME/
MAIN_CLASS="com.baibei.wine.paycenter.Bootstrap"
# 设置classpath
nohup java -Dspring.profiles.active=uat -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS >> /logs/$SERVICE_NAME/std-out.log 2>&1 &
#由于使用docker，所以无法使用nohup  将进程挂起，也不能使用重定向等
#java -Dspring.profiles.active=test -classpath $CONFIG_DIR:$LIB_JARS $MAIN_CLASS
