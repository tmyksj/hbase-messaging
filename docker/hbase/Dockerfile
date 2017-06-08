FROM ubuntu:xenial


RUN apt-get update
RUN apt-get upgrade -y


RUN ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
RUN cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
RUN chmod 0600 ~/.ssh/authorized_keys
RUN sed -i 's/#   StrictHostKeyChecking ask/    StrictHostKeyChecking no/g' /etc/ssh/ssh_config
RUN service ssh restart


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


EXPOSE 16010 16020 16030
EXPOSE 16012 16022 16032 16013 16023 16033 16015 16025 16035
EXPOSE 16200 16300
EXPOSE 16202 16302


ENTRYPOINT ["bash", "-c", "${HBASE_HOME}/etc/start.sh"]

