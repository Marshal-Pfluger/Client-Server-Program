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
//  Description:   Contains the Program4Client class for client side operations. 
//
//********************************************************************

// Import libraries
import java.io.*;
import java.net.*;
import java.util.Scanner;

// Start Program4Client class
public class Program4Client {
	
	
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
		Program4Client obj = new Program4Client();
		obj.developerInfo();
		//Start try block to handle exceptions
		try {
			obj.runDemo2();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}// End main method
	
	//***************************************************************
    //
    //  Method:       runDemo2 (Non Static)
    // 
    //  Description:  The main processor method of the program
    //
    //  Parameters:   N/A
    //
    //  Returns:      N/A 
    //
    //**************************************************************
	public void runDemo2() throws IOException {
		// create string object for loop back address. 
		String serverHost = new String("127.0.0.1");
		// create variable for port number
		int port = 4301;
		try {

			String userInput;
			do {
				// Create Socket variable for clientSocket
				Socket clientSocket = new Socket(serverHost, port);
				// Create outputStream
				OutputStream out = clientSocket.getOutputStream();
				// Create InputStream
				InputStream in = clientSocket.getInputStream();
				// Create PrintStream
				PrintStream printStream = new PrintStream(out);
				// Open stream reading for input
				InputStreamReader inputStream = new InputStreamReader(in);
				// Open Buffered reader to read client message
				BufferedReader bufferedReader = new BufferedReader(inputStream);
				
				// Get user info and remove spaces
				userInput = userChoice();
	
				// If user selects "Bye" do not send to server
			    if (!(userInput.equalsIgnoreCase("Bye"))) {
			    	// Send user package
			    	printStream.println(userInput);
			    	// Receive package from server
					String message = bufferedReader.readLine().replaceAll(";", "\n");
					// Display message from server
					System.out.println(message + "\n");
			    }
			    // Close clientSocket 
				clientSocket.close();
				// Close printStream
				printStream.close();
				}while(!(userInput.equalsIgnoreCase("Bye"))); // End loop if user enters Bye
			
			// Inform client of successful program exit
			System.out.println("The connection has been terminated have a great day");
			System.exit(1);
		} catch (IOException exc) {
			System.out.println("The connection was interrupted");
		}// End try/catch block
	}// End runDemo2

    //***************************************************************
	//
	//  Method:       userChoice
	// 
	//  Description:  gets input from user, closes scanner when program exits 
	//
	//  Parameters:   N/A
	//
	//  Returns:      String file
	//
	//**************************************************************	
	public String userChoice() {
		String userChoice;
		// Use Scanner to receive user input
		Scanner userInput = new Scanner(System.in);
		// Inform Client to input desired integers
		System.out.println("Please enter three integers (or type 'Bye' to exit): ");
		// Store user choice
		userChoice = userInput.nextLine();
		
		// close scanner when program exits.
		if (userChoice.equalsIgnoreCase("Bye")) {
			userInput.close();
		}
		System.out.println();
		return userChoice;
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
}// End Program4Client