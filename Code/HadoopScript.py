#!/usr/bin/env python

import ConnectToEC2

#hadoop-env.sh
readData = open("hadoop-env.sh").read()
hdp_env = '''export JAVA_HOME="/usr/lib/jvm/java-7-openjdk-amd64" '''
updated = readData.replace('export JAVA_HOME=${JAVA_HOME}')
fd = open('hadoop-env.sh','w')
fd.write(updated)

#core-site.xml
readData = open("core-site.xml").read()
hdp_coresite = '''        <property>
                <name>fs.default.name</name>
                <value>hdfs://'''+ConnectToEC2.ip+''':8020</value>
        </property>
</configuration>
'''
updated = readData.replace('</configuration>',hdp_coresite)
fd = open('core-site.xml','w')
fd.write(updated)

#yarn-site.xml
readData = open("yarn-site.xml").read();
hdp_yarnsite = '''          <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>
        <property>
                <name>yarn.resourcemanager.scheduler.address</name>
                <value>'''+ConnectToEC2.ip+''':8030</value>
        </property>
        <property>
                <name>yarn.resourcemanager.address</name>
                <value>'''+ConnectToEC2.ip+''':8032</value>
        </property>
        <property>
                <name>yarn.resourcemanager.webapp.address</name>
                <value>'''+ConnectToEC2.ip+''':8088</value>
        </property>
        <property>
                <name>yarn.resourcemanager.resource-tracker.address</name>
                <value>'''+ConnectToEC2.ip+''':8031</value>
        </property>
        <property>
                <name>yarn.resourcemanager.admin.address</name>
                <value>'''+ConnectToEC2.ip+''':8033</value>
        </property>
</configuration>
'''
updated = readData.replace('</configuration>',hdp_yarnsite)
fd = open('yarn-site.xml','w')
fd.write(updated)

#mapred-site.xml
readData = open("mapred-site.xml").read();
hdp_mapred_site = '''       <property>
        <name>mapreduce.jobtracker.address</name>
        <value>'''+ConnectToEC2.ip+''':8021</value>
</property>
<property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
</property>
</configuration>'''

updated = readData.replace('</configuration>',hdp_mapred_site)
fd = open('mapred-site.xml','w')
fd.write(updated)

#hdfs-site.xml
readData = open('hdfs-site.xml').read();
hdp_hdfs_site = '''        <property>
                <name>dfs.replication</name>
                <value>1</value>
        </property>
        <property>
                <name>dfs.permissions</name>
                <value>false</value>
        </property>
</configuration>'''
updated = readData.replace('</configuration>',hdp_hdfs_site)
fd = open('hdfs-site.xml','w')
fd.write(updated)
