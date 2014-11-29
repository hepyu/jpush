#!/bin/sh

VERSION=v0.0.1;
#the char '`' means set the result of the commond to the variable
BUILD_NUMBER=`date +%s`;

function usage()
{
echo "$0: Options:";
echo;
echo "--type    the type of deploy [WebSocketServerMaxConnsTest]";
echo "--help    print the help message";
}

function remote_copy()
{
echo "remove -e ssh -p $REMOTE_PORT -avz --progress --delete --include target/tpushserver-0.0.3 target $REMOTE_USER@$REMOTE_ADDR:$REMOTE_BASE";
rsync -e "ssh -p $REMOTE_PORT" -avz $JAR_PATH $REMOTE_USER@$REMOTE_ADDR:$REMOTE_BASE;
}

#----------WebSocketServerMaxConnsTest begin------------#

function WebSocketServerMaxConnsTest_env()
{
    REMOTE_PORT="22";
    REMOTE_USER="root";
    REMOTE_ADDR="0.0.0.0";
    #rember backup before this script running
    REMOTE_BASE="/Users/hpy";
    JAR_PATH=`pwd`"/WebSocketServerMaxConnsTest.jar";
}

function WebSocketServerMaxConnsTest_deploy()
{
    echo "start deploy WebSocketServerMaxConnsTest";
    WebSocketServerMaxConnsTest_env;
    remote_copy;
}

#----------WebSocketServerMaxConnsTest end------------#


while [ $# -gt 0 ]
    do
        case $1 in
            --type)
                shift
                BUILD_TYPE=$1
                ;;
            --help)
                usage;
                exit 0;
                ;;
            *)
         echo "Unknow option $1";
            usage;
            exit 1;
            ;;
         esac;
         shift;
done

function deploy()
{
    if [ "${BUILD_TYPE}" = "WebSocketServerMaxConnsTest" ]
    then
        echo "start deploy for WebSocketServerMaxConnsTest env.";
        WebSocketServerMaxConnsTest_deploy;
    fi;
}

deploy;
