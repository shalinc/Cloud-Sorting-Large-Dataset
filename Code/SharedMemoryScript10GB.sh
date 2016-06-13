#!/bin/sh

echo 'Generating Un-Sorted file of 10GB'
time ./gensort -a 100000000 InputFile.txt
echo 'Creating file Chunks'
time split -n 80 -d --additional-suffix=.txt InputFile.txt chunk- 
echo 'File Chunks Created'
javac *.java
echo 'STARTING SORT'
time java -Xms3200m SharedMemoryTeraSort 1
echo 'FINISHED SORTING'

echo 'Checking Sorted File using Valsort'
time ./valsort MergedOutput.txt

echo 'Deleting Temporary Chunks'
rm chunk*
