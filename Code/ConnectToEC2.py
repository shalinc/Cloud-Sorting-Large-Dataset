import boto.ec2
from pyspark import SparkContext

AWS_ACCESS_KEY_ID = #Access Key Here
AWS_SECRET_ACCESS_KEY = #Key Here

#print (boto.ec2.get_regions())
region1 = boto.ec2.get_region('us-east-1')
#print(region1)
conn = boto.ec2.EC2Connection(AWS_ACCESS_KEY_ID,AWS_SECRET_ACCESS_KEY, region=region1)
#print (conn)

reser = conn.get_all_instances()
instances = [i for r in reser for i in r.instances]
for i in instances:
    ip = i.__dict__['dns_name']
    print(ip)
    break # remove this to list all instances
