FROM ubuntu:xenial


RUN apt-get update
RUN apt-get upgrade -y


RUN apt-get install -y curl


RUN apt-get install -y openjdk-8-jdk
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH ${PATH}:${JAVA_HOME}/bin:${JAVA_HOME}/sbin


RUN curl http://ftp.jaist.ac.jp/pub/apache/hbase/1.2.6/hbase-1.2.6-bin.tar.gz | tar xzf - -C /opt
ENV HBASE_HOME /opt/hbase-1.2.6
ENV PATH ${PATH}:${HBASE_HOME}/bin:${HBASE_HOME}/sbin

RUN sed -i 's!^# export JAVA_HOME=/usr/java/jdk1.6.0/$!export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64!g' ${HBASE_HOME}/conf/hbase-env.sh

COPY conf/hbase-site.xml ${HBASE_HOME}/conf/hbase-site.xml

RUN mkdir ${HBASE_HOME}/etc
COPY etc/start.sh ${HBASE_HOME}/etc/start.sh
RUN chmod 755 ${HBASE_HOME}/etc/start.sh


EXPOSE 16010 16012 16013 16015 16020 16022 16023 16025 16030 16032 16033 16035
EXPOSE 16200 16202 16300 16302


ENTRYPOINT ["bash", "-c", "${HBASE_HOME}/etc/start.sh"]

