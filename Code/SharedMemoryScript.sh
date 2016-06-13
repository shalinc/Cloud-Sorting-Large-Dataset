#!/bin/sh

echo 'Generating Un-Sorted file of 1GB'
time ./gensort -a 10000000 InputFile.txt
echo 'Creating file Chunks'
time split -n 80 -d --additional-suffix=.txt InputFile.txt chunk- 
echo 'File Chunks Created generated'
javac *.java
echo 'STARTING SORT'
time java -Xmx2048m SharedMemoryTeraSort 1
echo 'FINISHED SORTING'

echo 'Checking Sorted File using Valsort'
time ./valsort MergedOutput.txt
echo 'Deleting Temporary Chunks'
rm chunk*
