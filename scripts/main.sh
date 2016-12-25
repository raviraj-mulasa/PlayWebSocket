#!/usr/bin/env bash
# Author : RaviRaj Mulasa

# Search under /usr/java for the latest update of version $1.
function get_java_home() {
    local VER=$1
    ROOT=/usr/java
    FOUND=0
    UPDATE=0

    if [ ! -d $ROOT ] ; then
        return 1
    fi

    for JAVA_HOME in $( find $ROOT -maxdepth 1 -type d -name jdk\* -o -name jre\* ) ; do
        JAVA="$JAVA_HOME/bin/java"
        if [ -x $JAVA ] ; then
            _v=$( $JAVA -version 2>&1 | grep '^java version' | sed -e 's/"//g' )
            if ! echo $_v | grep -q ${VER}_ ; then
                continue
            fi

            _u=$( echo $_v | cut -d_ -f2 )
            if [ $_u -gt $UPDATE ] ; then
                UPDATE=$_u
                FOUND=$JAVA_HOME
            fi
        fi
    done

    [ $FOUND != "0" ] && echo $FOUND
}

export JAVA_HOME=${JAVA_HOME:=$( get_java_home 1.8.0 )}
if [ -z "$JAVA_HOME" ] ; then
    echo "unable to find java"
    exit 1
fi
export PATH=$PATH:$HOME/bin:$JAVA_HOME/bin

BASEDIR="$(dirname $0)/.."
cd $BASEDIR && BASEDIR="`pwd`"
libraries=`find lib -name *.jar | tr "\n" ":"`
libraries="${libraries}config"
echo BASEDIR: $BASEDIR
echo libraries: $libraries

export PROCESS_NAME=PlayWebSocket

GC_OPTS="
-XX:MaxMetaspaceSize=<metaspace size>[g|m|k] \
-Xmn<young size>[g|m|k] \
-XX:SurvivorRatio=<ratio> \
-XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
-XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=<percent> \
-XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
"

JAVA_OPTS="
-server \
-verbose:gc \
-XX:+PrintGCDateStamps \
-XX:+PrintGCDetails \
-Xloggc:gc.log \
-XX:+UseGCLogFileRotation \
-XX:NumberOfGCLogFiles=10 \
-XX:GCLogFileSize=100M \
-Dfile.encoding=UTF-8 \
-Djava.awt.headless=true \
-Duser.timezone=GMT \
"

MISC_OPTS="
-XX:+HeapDumpOnOutOfMemoryError \
-XX:HeapDumpPath=logs/`date`.hprof \
-Dsun.net.inetaddr.ttl=300 \
-Djava.rmi.server.hostname=<external IP> \
"

JMX_OPTS="
-Dcom.sun.management.jmxremote.port=<port> \
-Dcom.sun.management.jmxremote.authenticate=false \
-Dcom.sun.management.jmxremote.ssl=false \
"

nohup java -Dprocess.name=$PROCESS_NAME -Xms1024m -Xmx1024m $JAVA_OPTS -Dlogback.configurationFile=file://$BASEDIR/config/logback.xml -Dserver.home=$BASEDIR -cp $libraries net.geekscore.db.Main &