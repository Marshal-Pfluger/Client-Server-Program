//********************************************************************
//
//  Developer:     Marshal Pfluger
//
//  Project #:     Four
//
//  File Name:     Program4Server.java
//
//  Course:        COSC 4301 Modern Programming
//
//  Due Date:      03/03/2023
//
//  Instructor:    Prof. Fred Kumi 
//
//  Java Version:  17.0.4.1
//
//  Description:   Contains the Program4Server class for server side operations. 
//
//********************************************************************

// Import libraries
import java.io.*;
import java.net.*;
import java.util.*;
// Start Program5Server class
public class Program4Server {
	private boolean validationState;
	
	public Program4Server(){
		validationState = true;
	}
	
	//***************************************************************
    //
    //  Method:       main
    // 
    //  Description:  The main method of the program
    //
    //  Parameters:   String[] args
    //
    //  Returns:      N/A 
    //
    //**************************************************************
	public static void main(String[] args) {
		// create object of Program4Server to call non static method. 
		Program4Server obj = new Program4Server();
		obj.developerInfo();
		// Start try block to handle exceptions
		try {
			obj.runDemo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}// End main
	
	//***************************************************************
    //
    //  Method:       runDemo (Non Static)
    // 
    //  Description:  The main processor method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A 
    //
    //**************************************************************
    public void runDemo() throws IOException {
    	// Declare port number variable. 
		int port = 4301;
		// Create object of ServerSocket class. 
		ServerSocket serverSocket = null;
		
		try {
			// Create TCP Server Socket
	       	serverSocket = new ServerSocket(port);
	       	System.out.println("[TCP Server says] TCP Server created on port " + port + "\n");
		} catch (IOException e) {
			System.out.println("Could not establish connection with port: " + port + "\n");
			System.exit(1);
		}// End try/catch block 
	       	
		   while (true) {
			   // Reset validationState
			   validationState = true;
			   // Declare socket variable 
			   Socket clientSocket = null;
			   
               // Open connection with client
			   try {
				   System.out.println("waiting for connection...");
				   clientSocket = serverSocket.accept();
				   System.out.println("Connection established\n");
			   } catch (IOException exc) {
				   System.err.println("Cannot accept client request.");
				   System.exit(1);
			   }// End try/catch block
			   
			   try {
				   // Open input stream
				   BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				   // Open output stream
				   PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				
				   // Receive client package
				   String inputLine = in.readLine();
				   System.out.println("package received from client: " + inputLine);
				   
				   // Declare variable to hold list for processing
				   List<Integer> listHolder =  new ArrayList<>();
				   // Send client package to parsing method
				   listHolder = inputParsing(inputLine);
				   // Make sure Package is valid before resuming
				   if (validationState) {
					   // Pass list to input validation 
					   listHolder = inputValidation(listHolder, out);
					   // Pass integers to listMaker to make even/odd list
					   listHolder = listMaker(listHolder.get(0), listHolder.get(1), listHolder.get(2));
					   // Pass output to outputProcessor
					   outputProcessor(out, ("Sum:" + getSum(listHolder) + ";Mean:" + getMean(listHolder) + ";Standard-Deviation:" + getSTD(listHolder)));
					   } else {
						   // If initial package is not valid inform client by passing output to outputProcessor
						   outputProcessor(out, "Error: Please enter 3 Integers seperated by a space");
						   }// End if/else  
				   // Handle IOExceptions
				   } catch (IOException e) {
				   System.out.println("client server connection failed/ended");
				   serverSocket.close();
				   clientSocket.close();
				   System.exit(1);
			   }// End catch block
		}// End while loop	
	} // end runDemo method
    
	//***************************************************************
    //
    //  Method:       inputParsing
    // 
    //  Description:  method parses client package
    //
    //  Parameters:   String inputLine
    //
    //  Returns:      List<Integer>
    //
    //**************************************************************
    public List<Integer> inputParsing(String inputLine) {
    	// Create list to store parsed ints
    	List<Integer> itemList =  new ArrayList<>();
    	try {
    		// Split string Chars
    		String[] inputComponents = inputLine.split(" ");
    		itemList.add(Integer.parseInt(inputComponents[0]));
    		itemList.add(Integer.parseInt(inputComponents[1]));
    		itemList.add(Integer.parseInt(inputComponents[2]));
    	} catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException exc) {
    		validationState = false;
    	}// End try/catch block
		return itemList;	  
    }// End inputParsing
    
	//***************************************************************
    //
    //  Method:       inputValidation
    // 
    //  Description:  validates client package
    //
    //  Parameters:   List<Integer> oddEvenList, PrintWriter out
    //
    //  Returns:      List<Integer>
    //
    //**************************************************************
    	
    public List<Integer> inputValidation(List<Integer> oddEvenList, PrintWriter out) {
    	 // Validation The first integer must be less than the second  
    	if(oddEvenList.get(0) <= 0 || oddEvenList.get(1) <= 0 || oddEvenList.get(2) <= 0) {
			   validationState = false;
			   outputProcessor(out, "Error: All digits must be greater than zero");
		   // Validation The third integer must be 1 or 2
		   } else if (oddEvenList.get(0) >= oddEvenList.get(1)) {
			   validationState = false;
			   outputProcessor(out, "Error: The First integer has to be less than the second.");
		   // Validation All the integers must be greater than zero
		   } else if (oddEvenList.get(2) != 2 && oddEvenList.get(2) != 1) {
			   validationState = false;
		       outputProcessor(out, "Error: The third Digit must be either 1, or 2");
		   }// End If/elif statement
		   return oddEvenList;
	   }// End inputValidation 
    
	//***************************************************************
    //
    //  Method:       outputProcessor
    // 
    //  Description:  Sends output to client
    //
    //  Parameters:   PrintWriter out, String outputString
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void outputProcessor(PrintWriter out, String outputString) {
    	out.println("***From Server***;" +outputString);
    }
    
	//***************************************************************
    //
    //  Method:       listMaker 
    // 
    //  Description:  creates either odd or even digit list
    //
    //  Parameters:   int firstDigit, int secondDigit, int thirdDigit
    //
    //  Returns:      List<Integer> valueList
    //
    //**************************************************************
    public List<Integer> listMaker(int firstDigit, int secondDigit, int thirdDigit) {
    	List<Integer> valueList = new ArrayList<>();
		   if (thirdDigit == 1) {
		        for (int i = firstDigit; i <= secondDigit; i++) {
		            // check even & odd using Bitwise AND operator
		            if ((i & 1) != 0)
		            	// Add odd int to list
		            	valueList.add(i);  
		            }// End for loop
		   } else {
			   for (int i = firstDigit; i <= secondDigit; i++) {
				// check even & odd using Bitwise AND operator
				   if ((i & 1) == 0)
					   // Add even int to list
					   valueList.add(i);
				   }// End for loop
			   }// End if/else
		   return valueList;
    }// End listMaker method
    
	//***************************************************************
    //
    //  Method:       getMean 
    // 
    //  Description:  Calculates the mean of the list of odd/even
    //
    //  Parameters:   List<Integer> oddEvenList
    //
    //  Returns:      double 
    //
    //**************************************************************
    public double getMean(List<Integer> oddEvenList) {
    	// Calculate mean and return
    	return (getSum(oddEvenList) / oddEvenList.size());    		
    	}// End getMean method
    
	//***************************************************************
    //
    //  Method:       getSum 
    // 
    //  Description:  Calculates the sum of the list of odd/even
    //
    //  Parameters:   List<Integer> oddEvenList
    //
    //  Returns:      double sum
    //
    //**************************************************************
    public double getSum(List<Integer> oddEvenList) {
    	int sum = 0;
    	// Iterate through list and add each element to sum
    	for(int i = 0; i < oddEvenList.size(); i++) { 
    		sum += oddEvenList.get(i);
    		}
    	return sum;
    }// End getSum method
    
	//***************************************************************
    //
    //  Method:       getSTD
    // 
    //  Description:  Calculates the standard deviation of the list of odd/even
    //
    //  Parameters:   List<Integer> oddEvenList
    //
    //  Returns:      double 
    //
    //**************************************************************
    public double getSTD(List<Integer> oddEvenList) {
    	double mean = getMean(oddEvenList);
    	double count = 0;
    	
    	for (int i = 0; i < oddEvenList.size(); i++) {
    		// sum the difference of the element and mean raised to 2
    		count += Math.pow(((double) oddEvenList.get(i)) - mean, 2);
    		}
    	// return the square root of the count/number of elements
    	return Math.sqrt(count / oddEvenList.size());
    }
	   //***************************************************************
	   //
	   //  Method:       developerInfo (Non Static)
	   // 
	   //  Description:  The developer information method of the program
	   //
	   //  Parameters:   None
	   //
	   //  Returns:      N/A 
	   //
	   //**************************************************************
	   public void developerInfo()
	   {
	      System.out.println("Name:    Marshal Pfluger");
	      System.out.println("Course:  COSC 4301 Modern Programming");
	      System.out.println("Project: Four\n\n");
	   } // End of the developerInfo method
	}// End project4Server class