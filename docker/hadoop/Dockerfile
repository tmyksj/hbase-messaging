FROM ubuntu:xenial


RUN apt-get update
RUN apt-get upgrade -y


RUN apt-get install -y curl openssh-server pdsh


RUN ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
RUN cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
RUN chmod 0600 ~/.ssh/authorized_keys
RUN sed -i 's/#   StrictHostKeyChecking ask/    StrictHostKeyChecking no/g' /etc/ssh/ssh_config
RUN service ssh restart


RUN apt-get install -y openjdk-8-jdk
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
ENV PATH ${PATH}:${JAVA_HOME}/bin:${JAVA_HOME}/sbin


RUN curl http://ftp.jaist.ac.jp/pub/apache/hadoop/common/hadoop-2.7.3/hadoop-2.7.3.tar.gz | tar xzf - -C /opt
ENV HADOOP_HOME /opt/hadoop-2.7.3
ENV PATH ${PATH}:${HADOOP_HOME}/bin:${HADOOP_HOME}/sbin

RUN sed -i 's!^export JAVA_HOME=${JAVA_HOME}$!export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64!g' ${HADOOP_HOME}/etc/hadoop/hadoop-env.sh

COPY etc/hadoop/core-site.xml ${HADOOP_HOME}/etc/hadoop/core-site.xml
COPY etc/hadoop/hdfs-site.xml ${HADOOP_HOME}/etc/hadoop/hdfs-site.xml
COPY etc/hadoop/mapred-site.xml ${HADOOP_HOME}/etc/hadoop/mapred-site.xml
COPY etc/hadoop/yarn-site.xml ${HADOOP_HOME}/etc/hadoop/yarn-site.xml

RUN hdfs namenode -format

COPY etc/start.sh ${HADOOP_HOME}/etc/start.sh
RUN chmod 755 ${HADOOP_HOME}/etc/start.sh


EXPOSE 8020 9000 50010 50020 50070 50075 50090
EXPOSE 10020 19888
EXPOSE 8030 8031 8032 8033 8040 8042 8088
EXPOSE 49707 2122  


ENTRYPOINT ["bash", "-c", "${HADOOP_HOME}/etc/start.sh"]

