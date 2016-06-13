README.TXT

FOLDER STRUCTURE:

Code -> Code contains all the code & Scripts
Output Text	 -> Output Text contains the First and Last 10 Lines of Sort Results
Configuration Files	 -> contains all the Hadoop Config files 
README.txt -> ReadMe file
prog2-report.pdf -> Report & Design Doc for PA2 implementation
prog2_sourcecode.txt -> All the source code and Scripts in .txt file


HOW TO RUN:

1. Shared Memory

	Open the folder "Code" which contains the Scripts to implement Shared Memory Sort
	Launch an instance of type c3.large, (Assuming java v.7 is already installed)
	Install gensort by typing onto instance terminal, 
		wget http://www.ordinal.com/try.cgi/gensort-linux-1.5.tar.gz
		tar xvf gensort-linux-1.5.tar.gz
		rm gensort-linux-1.5.tar.gz  
	goto folder named "64" created, cd 64
	execute the Script, for 1GB
		sh SharedMemoryScript.sh -> will execute the script for 1 thread, 
		for more threads edit the number of threads passed as a parameter to the java command to 2,4,8
	a final MergedOutput.txt file will be created which is the sorted output file, hasn't removed it for verification purpose
	to remove it do, rm MergedOutput.txt
	Similarly for 10GB,
		sh SharedMemoryScript10GB.sh -> will execute the script for 1 thread
	
2. HADOOP
	
	1 Node Configuration:
		Open the folder "Code" which contains the Scripts to implement HADOOP Sort
		Launch an instance of type c3.large, and mount an EBS storage of 50GB to it
		
		then download hadoop, version 2.7.2 and extract it
		
		Create raid using following command,
		sudo apt-get install mdadm, press OK
		sudo mdadm --create --verbose /dev/md0 --level=0 --name=MY_RAID --raid-devices=2 /dev/xvdb /dev/xvdc
		if more devices are present increment the raid-devices and add the name at end
		
		then type,
		sudo mkfs.ext2 -L MY_RAID /dev/md0
		sudo mkdir -p /mnt/raid
		sudo mount LABEL=MY_RAID /mnt/raid

		Copy the pem file onto the instance and do,
		sudo eval $(ssh-agent)
		sudo chmod 600 CloudSorting.pem
		sudo ssh-add CloudSorting.pem
		
		move the Hadoop folder into new folder named hadoop,
		sudo mv hadoop-2.7.2.tar.gz /usr/local/hadoop
		
		change the ~/.bashrc file to setup Hadoop Paths
		sudo vi ~/.bashrc
		
		paste the following lines at the end of the file:
			export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
			export HADOOP_HOME=/usr/local/hadoop
			export PATH=$PATH:$HADOOP_HOME/bin
			export PATH=$PATH:$HADOOP_HOME/sbin
			export HADOOP_MAPRED_HOME=$HADOOP_HOME
			export HADOOP_COMMON_HOME=$HADOOP_HOME
			export HADOOP_HDFS_HOME=$HADOOP_HOME
			export YARN_HOME=$HADOOP_HOME
			export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
			export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"

		restart .bashrc, by issuing the command: source ~/.bashrc
		Now, goto folder mv /usr/local/hadoop/etc/hadoop/
		
		change the config files here
		copy the 2 files ConnectToEC2.py and HadoopScript.py here
		edit the ConnectToEC2.py file by putting in the security key
		
		then type, cp mapred-site.xml.template mapred-site.xml
		run the python script here to change all the configuration files,
		python HadoopScript.py 
		will make changes to all the files
		
		Now, to run on 16 Nodes prepare an AMI and launch 16 slave instances from this AMI
		
		To make slaves file, which contains all the slaves IP
		edit the ConnectToEC2.py file by putting in the security key
		remove the break i.e. the last line in the code
		run python ConnectToEC2.py
		It will print all the DNS copy them and paste onto the slaves file
		
		To run HadoopSort Code:
		Generate the dataset file in mnt/raid/, 
		Format the namenode
		hadoop namenode -format
		hadoop star-all.sh
		
		type,
		hadoop fs -put /mnt/raid/Input.txt /Input
		hadoop jar /home/ubunut/Code/HadoopTeraSort.jar /Input /Output
		get the sorted output as,
		hadoop fs -get /Output /mnt/raid
		
		hadoop stop-all.sh
		
		Hadoop Execution Complete
		
3. SPARK

	To run spark, install SPARK v. 1.6.0
	Now type,
	export AWS_ACCESS_KEY_ID=<ACCESS_KEY>
	export AWS_SECRET_KEY=<SECRET_KEY>
	
	goto folder, /spark/ec2 in your spark downloaded folder
	type, the command to launch master and slave instances
	./spark-ec2 –k CloudSorting -i /home/ubuntu/CloudSorting.pem  -s 16 --instance-type=c3.large –ebs-vol-size=50 --spot-price=0.025 -r us-east-1 -m c3.4xlarge launch sparkInstances
	
	This will launch 1 master and 16 slaves nodes,
	now log into master node, via Terminal and do the following,
	generate the data for dataset size accdn to requirement,
	put data onto /mnt2/Input.txt
	cd /ephemeral-hdfs/bin/
	./hadoop fs -put /mnt2/Input.txt /Input
	rm /mnt2/Input.txt
	
	cd ~
	cd spark-ec2/bin
	./spark-shell
	will launch the scala shell to type in the code,
	copy the code from prog2_sourcecode.txt file named SCALA SPARK
	paste line by line, and execute 
	finally on saveAsTextFile("/Output") cmd, will give the sorted values as chunks in the /Output folder
	do, cd ~
	goto cd /ephemeral-hdfs/bin/
	./hadoop fs -get /Output /mnt2/
	you will find sorted values in this folder
	
	SPARK EXECUTION COMPLETED
	

