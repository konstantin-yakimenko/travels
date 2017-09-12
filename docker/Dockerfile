FROM tarantool/tarantool:1.7-centos7

ADD jdk-8u144-linux-x64.rpm /root/jdk-8u144-linux-x64.rpm
RUN yes | yum install /root/jdk-8u144-linux-x64.rpm
RUN rm /root/*.rpm

ENV SUPERVISOR_VERSION=3.3.0

RUN \
  yum update -y && \
  yum install -y epel-release && \
  yum install -y iproute python-setuptools hostname inotify-tools yum-utils which jq && \
  yum clean all && \
  easy_install supervisor

COPY supervisord.conf /etc/supervisord.conf
COPY app.lua /opt/tarantool
COPY travels-1.0.jar /opt/tarantool

ENV TARANTOOL_USER_NAME=test
ENV TARANTOOL_USER_PASSWORD=test

EXPOSE 80

#RUN mkdir -p /tmp/data
#COPY data.zip /tmp/data
#COPY options.txt /tmp/data

CMD ["supervisord", "-c", "/etc/supervisord.conf"]
