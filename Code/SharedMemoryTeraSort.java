import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SharedMemoryTeraSort 
{

	private static int threadCount;
	private static int noOfChunks = 80;
	private static List <String> fileList = new ArrayList<String>();
	private static long totalLines;

	public static void main(String[] args) throws Exception 
	{
		// TODO Auto-generated method stub
		threadCount = Integer.parseInt(args[0]);
		System.out.println("Running Threads: "+threadCount);
		String filepath = "InputFile.txt";
		
		//final List <String> copyFileData = new ArrayList<String>();
		double startTime = System.currentTimeMillis();

		createFileList();
		final List<Integer> fileIndexes = new ArrayList<Integer>();
		
		for(int i=0;i<noOfChunks;i++)
			fileIndexes.add(i);
	
		File file = new File(filepath);
		totalLines = file.length();
		
		file.delete();
		
		//Threads
		Thread [] thread = new Thread[threadCount];
		
		for (int i= 0 ;i <threadCount; i++)
		{
			
			thread[i] = new Thread(new Runnable()
			{	
				@Override
				public void run()
				{
					try
					{
						for(int i=0;i<noOfChunks/threadCount;i++)
						{
							
							int fileIndex = ((Integer.parseInt(Thread.currentThread().getName())*noOfChunks)/(threadCount))+i;
							implementSorting(fileIndexes.get(fileIndex));
						}
					}
					catch(Exception e)
					{
					}
				}
			});
			
			thread[i].setName(Integer.toString(i));
			thread[i].start();
		}
	
		//wait for all the threads to finish, before starting merge phase
		for(int j =0;j<threadCount;j++)
		{
			thread[j].join();
		}
		
		System.out.println("Sorted Chunks Created, Final Merge Phase Starts");
		
		mergeSortedChunks();

		System.out.println("Time Taken in millis: "+(System.currentTimeMillis()-startTime));
	}

	private static void implementSorting(int index) throws Exception
	{
		//System.out.println("Thread: -->"+Thread.currentThread().getName());
		//System.out.println("Index:--->"+index);
		
		List<String> retList = null;
		
		retList = readDataFile(fileList.get(index));
		retList = QuickSort(retList,0,retList.size()-1);
		writeSortedFile(retList,index);
		retList.clear();
		
	}
	
	//Do k-way external Mergesort to merge all the sorted chunks into one big file
	private static void mergeSortedChunks() throws IOException 
	{
		// TODO Auto-generated method stub
		
		BufferedReader[] bufReaderArray = new BufferedReader[noOfChunks];
		List <String> mergeList = new ArrayList<String>();
		List <String> keyValueList = new ArrayList<String>();
		
		
		//Copy first line from all the chunks and find minimum value among these chunks
		for (int i=0; i<noOfChunks; i++)
		{
			bufReaderArray[i] = new BufferedReader(new FileReader(fileList.get(i)));

			String fileLine = bufReaderArray[i].readLine();

			if(fileLine != null)
			{
				mergeList.add(fileLine.substring(0, 10));
				keyValueList.add(fileLine);
			}
		}

		//Final Output File
		BufferedWriter bufw = new BufferedWriter(new FileWriter("MergedOutput.txt"));

		//Merge & Sort the files

		for(long j=0; j<totalLines;j++)
		{
			String minString = mergeList.get(0);
			int minFile = 0;

			for (int k = 0; k< noOfChunks; k++)
			{
				if (minString.compareTo(mergeList.get(k)) > 0)
				{
					minString = mergeList.get(k);
					minFile = k;
				}
			}

			//System.out.println("Minimum file is: "+minFile);
			bufw.write(keyValueList.get(minFile)+"\n");
			mergeList.set(minFile,"-1");
			keyValueList.set(minFile,"-1");

			String temp = bufReaderArray[minFile].readLine();

			if (temp != null)
			{
				mergeList.set(minFile,temp.substring(0, 10));
				keyValueList.set(minFile, temp);
			}
			else
			{
				//if one of the files get finished earlier than others, copy other values and Quick Sort over them
				//write the output to the final sorted file
				j = totalLines;

				List <String> tempList = new ArrayList<String>();

				for(int i = 0;i< mergeList.size();i++)
				{
					if(keyValueList.get(i)!="-1")
						tempList.add(keyValueList.get(i));

					while((minString = bufReaderArray[i].readLine())!=null)
					{
						tempList.add(minString);
					}
				}

				tempList = QuickSort(tempList, 0, tempList.size()-1);
				int i =0;
				while(i < tempList.size())
				{
					bufw.write(tempList.get(i)+"\n");
					i++;
				}
				
				//System.out.println("Time taken for last phase when file read finished: "+(System.currentTimeMillis()-startTime));
			}
		}
		bufw.close();
		//System.out.println("Copied values are: "+copiedValue);
		for(int i=0; i<noOfChunks;i++)
			bufReaderArray[i].close();	
	}

	//Method to create a FileList, which will include name of the file chunks to fetch
	private static void createFileList() 
	{
		for(int i = 0; i<noOfChunks; i++)
			fileList.add("chunk-"+String.format("%02d", i)+".txt");
		//System.out.println("The file List is: "+fileList);
	}

	//Read the File Path to read the File to sort
	static List<String> readDataFile(String filePath)throws Exception
	{
		List <String> dataToSort = new ArrayList <String> ();
		
		//File Reader to get the file to be read
		FileReader file = new FileReader(new File(filePath));
		//Using Buffered Reader to read the file
		BufferedReader bufRead = new BufferedReader(file);
		//Storing the File Content in an ArrayList

		//String readLine = null;
		dataToSort.clear();
		//int i=0;
		//Read the entire file while it is not NULL i.e. Reaches end of File
		
		String readline;
		
		while(true)
		{
			readline = null;

			if((readline = bufRead.readLine()) == null)
				break;
			
			dataToSort.add(readline);
		}
		/*
		while ((readLine = bufRead.readLine()) != null)
		{
			dataToSort.add(readLine);
			//System.out.println(i++);
		}*/

		bufRead.close();

		return dataToSort;	
	}

	//Quick Sort to Sort Files
	static List<String> QuickSort(List<String> dataToSort, int beginIndex, int endIndex) throws IOException
	{
		//Get the Lower Index and Higher Index
		int i = beginIndex;
		int j = endIndex;

		//Middle element Index is taken as the Pivot Index
		int pivotIndex = beginIndex + (endIndex-beginIndex)/2; 

		//Middle element becomes the pivot
		String pivot = dataToSort.get(pivotIndex).substring(0, 10);

		//For Swapping purpose
		//String temp = null;

		//Until i and j converge do
		while(i <= j)
		{
			String temp = null;
			//Check if elements in left side of pivot are less, if yes increment i
			while(dataToSort.get(i).substring(0, 10).compareTo(pivot) < 0)
				i++;

			//Check if elements to right of the pivot is greater than pivot, 
			//if yes decrement j as it is at last index
			while(dataToSort.get(j).substring(0, 10).compareTo(pivot) > 0)
				j--;

			//Found two elements Indexe's to Swap
			if(i<=j)
			{
				//Swap the elements
				temp = dataToSort.get(i);
				dataToSort.set(i, dataToSort.get(j));
				dataToSort.set(j,temp);

				//Increment i and decrement j
				i++;
				j--;
			}
		}

		//Recursive Call to Quick sort, using Divide and Conquer Approach

		//Sort left sub-ArrayList
		if (beginIndex < j)
			QuickSort(dataToSort,beginIndex, j);

		//Sort Right sub-ArrayList
		if (i < endIndex)
			QuickSort(dataToSort,i, endIndex);

		return dataToSort;
	}

	
	//Write Final Sorted Result in file
	static void writeSortedFile(List<String> dataToSort, int fileIndex) throws IOException, InterruptedException
	{
		//System.out.println("Thread "+Thread.currentThread().getName()+"-->"+fileList.get(fileIndex));
		
		//Write the result in an Output File
		FileWriter filewrite = new FileWriter(new File(fileList.get(fileIndex)));
		BufferedWriter bufw = new BufferedWriter(filewrite);

		//FileInputStream fis = new FileInputStream(new File(fileList.get(fileIndex)));
		
		int k = 0;
		
		while(k !=dataToSort.size()) 
		{
			bufw.write(dataToSort.get(k)+" "+"\n");
			k++;	
		}
		
		//filewrite.close();
		bufw.close();
	}
}
