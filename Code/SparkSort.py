from pyspark import SparkConf, SparkContext
import sys

#Set the Application Name
appName = "SparkSort"

#set SparkConf Configuration
conf = SparkConf().setAppName(appName)
sc = SparkContext(conf=conf)

#Give path of HDFS where Input file is present
inputFile = sc.textFile(sys.argv[0])

sortedOp = inputFile.flatMap(lambda line: line.split("\n"))\
    .map(lambda line: (line[:10], line[10:98]))\
    .sortByKey()\
    .map(lambda key, value: key+value+" ")

#Name of the Output Directory to Store the sorted chunks
sortedOp.saveAsTextFile(sys.argv[1])

