#!/bin/sh

VERSION=v0.0.1;
#the char '`' means set the result of the commond to the variable
BUILD_NUMBER=`date +%s`;

function usage()
{
    echo "$0: Options:";
    echo;
    echo "--type    the type of deploy [localhost,test116,production]";
    echo "--help    print the help message";
}

function rebuild()
{
    mvn clean;
    echo;
    echo;
    echo "mvn package -P $1";
    mvn package -P $1 -Dtongbupan.version=$VERSION-$BUILD_NUMBER;
    chmod +x target/*.sh
}

function remote_copy()
{
    echo "remove -e ssh -p $REMOTE_PORT -avz --progress --delete --include target/tpushserver-0.0.3.4 target $REMOTE_USER@$REMOTE_ADDR:$REMOTE_BASE";
    rsync -e "ssh -p $REMOTE_PORT" -avz target/tpushserver-0.0.3.4 $REMOTE_USER@$REMOTE_ADDR:$REMOTE_BASE;
}

#----localhost deploy start----#
function localhost_env()
{
    TARGET_PATH=`pwd`"/target";
}

function localhost_deploy()
{
    localhost_env;
    echo "$TARGET_PATH";
    rm -rf $TARGET_PATH/*;
    echo "finished clean $TARGET_PATH";
    rebuild localhost;
}
#----localhost deploy end----#

#----test116 deploy start----#
function test116_env()
{
    REMOTE_PORT="2312";
    REMOTE_USER="emacle";
    REMOTE_ADDR="0.0.0.0";
    #rember backup before this script running
    REMOTE_BASE="/home/emacle/jetty/webapps";
}

function test116_deploy()
{
    echo "start deploy test116";
    test116_env;
    rebuild test116;
    remote_copy;
}
#----test116 deploy end----#

function performance_env()
{
    REMOTE_PORT="22";
    REMOTE_USER="root";
    REMOTE_ADDR="0.0.0.0";
    #rember backup before this script running
    REMOTE_BASE="/home/emacle/jetty/webapps";
}

function performance_deploy()
{
    echo "start deploy performance";
    performance_env;
    rebuild performance;
    remote_copy;
}


#----production deploy start----#
function production_env()
{
    REMOTE_PORT="22";
    REMOTE_USER="emacle";
    REMOTE_ADDR="0.0.0.0";
    REMOTE_BASE="/home/emacle/jetty_8.1.3_tpush/webapps";
}

function production_deploy()
{
    echo "start deploy production";
    production_env;
    rebuild production;
    remote_copy;
}

#----private cloud deploy start----#
function privateCloud_env()
{
    REMOTE_PORT="22";
    REMOTE_USER="emacle";
    REMOTE_ADDR="0.0.0.0";
    REMOTE_BASE="/home/emacle/tpush/jetty/webapps";
}

function privateCloud_deploy()
{
	echo "start deploy privateCloud";
	privateCloud_env;
	rebuild privateCloud;
	remote_copy;
}

#----production deploy end----#

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
    if [ "${BUILD_TYPE}" = "localhost" ]
    then
        echo "start deploy for localhost env.";
        localhost_deploy;
    elif [ "${BUILD_TYPE}" = "test116" ]
    then
        echo "start deploy for test116 env.";
        test116_deploy;
    elif [ "${BUILD_TYPE}" = "production" ]
    then
        echo "start deploy for production env.";
        production_deploy;
    elif [ "${BUILD_TYPE}" = "performance" ]
    then
        echo "start deploy for performance env. ";
        performance_deploy;
    elif [ "${BUILD_TYPE}" = "privateCloud" ]
    then
    	echo "start deploy for private clooud";
    	privateCloud_deploy;
    fi;
}

deploy
