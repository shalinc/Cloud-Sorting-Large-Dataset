import boto.ec2
from pyspark import SparkContext

AWS_ACCESS_KEY_ID = 'AKIAIQ4SFJJ5IVIJQ6RA'#'AKIAINPIOQ3LBPI3YOUQ'
AWS_SECRET_ACCESS_KEY = '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'#'OiNblL4idyhpD7Cea0wkhBCHJG9ERj2w9Nqw05+y'

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