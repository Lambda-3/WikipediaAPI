#!/bin/sh

ROOTDIR=`pwd`
CONFDIR="${ROOTDIR}/conf"
LIBDIR="${ROOTDIR}/lib"

JAVA_OPTS=""

cmd="java -jar -server ${JAVA_OPTS} \
	-Djava.net.preferIPv4Stack=true \
	-Dfile.encoding=UTF-8 \
	-Dlogback.configurationFile=${CONFDIR}/logback.xml \
	-Dconfig.file=${CONFDIR}/wikipedia.local.conf \
	${LIBDIR}/${project.artifactId}-${project.version}.jar"

exec ${cmd}
