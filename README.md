# Cloud-Sorting-Large-Dataset
Sorting of Large dataset (100 GB) using Shared Memory, Hadoop and Spark 

<h3>SHARED MEMORY SORT:</h3>
The code for Shared-Memory Sort is written in Java Programming Language.
The main aim of this code is to perform sorting on large datasets, considering
this I have opted for Quick Sort and k-way external merge sort.
The program performs sorting for 1GB and 10GB datasets, where key is of 10
Bytes and value of 90 Bytes.
 Sorting is performed based on the key’s ASCII value.
 I have divided the original dataset into 80 Chunks and applied Quick Sort on
these chunks, which provides me with sorted chunks as output. This part of the
program has threads applied to it to parallelize the process of sorting.
 Then using k-way external merge sort, I have merged all the Sorted Chunks of
earlier phase into a final sorted output file.
<h3>2. HADOOP SORT:</h3>
 The code for Hadoop based Sorting is written in Java Programming language.
 Here, I have used Hadoop’s Map-Reduce based implementation to sort large
datasets.
Hadoop has a Sort and Shuffle phase, which does the Sorting of data internally,
without writing any code particularly for sort.
I have utilized the same in my code, I have provided the mapper with input as
key (10 Bytes) and value(90 Bytes), the Sort and Shuffle phase will categorize
the data and sort them according to its Key.
The reducer here just has to do the work of emitting the output given by the
Sort and Shuffle phase, hence I have used the Identity reducer which will
perform the same job as reducer here will.
I have tested the performance of hadoop on 1-Node (1GB, 10GB) and 16-Node
(100GB).
Hadoop’s Configuration process is explained in the next document.
<h3>3. SPARK SORT:</h3>
The code for spark is written using spark shell, which uses Scala as a
programming language.
Spark also uses the same Map-Reduce based implementation as Hadoop does.
When installing Spark it installs various features and language packages, like
Scala, R-Studio, Python, specifically for Spark.
I have implemented sorting using scala’s sortByKey(), functionality, which
sorts the data based on Key value (10 Bytes). The sorted data obtained using
spark, is divided into chunks.
I have also implemented the same using Python programming and PySpark
package.
