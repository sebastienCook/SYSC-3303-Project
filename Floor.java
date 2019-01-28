// This class is the floor for the project
// Last edited January 21st, 2016

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class Floor {

   DatagramPacket sendPacket, receivePacket;
   DatagramSocket sendReceiveSocket;

   static String fileName = ".//input.txt";
   static int numOfFloors = 10;
   
   List<String> allLines;       // input file content
   Floor Floors[];
   
   int currentFloor;            // the floor of the current object
   int currentTime;             // xx:xx:xx:xxx 
   
   int elevatorDirection; 		// 0 is stop, 1 up, 2 down
   int requestFloorButton;      // 1 is going up, 0 is going down
   int floorLamp;               // 1 is going up, 0 is going down 	WAITING LIGHT
   int directionLamp;           // 1 is for going up, 0 is going down

   int[] arrivalSensors;        // 0 is no elevator, 1 is elevator arriving. Each index of the array contains a sensor for its corresponding elevator shaft
   // port 219 - scheduler
   
   // Constructor with custom floor level
   public Floor(int currFloor) {
      try {
         // Construct a datagram socket and bind it to any available port on the local host machine
         sendReceiveSocket = new DatagramSocket();
         currentFloor = currFloor;
         System.out.println("Floor " + currentFloor + " created.");
      } catch (SocketException se) {   // Can't create the socket.
         se.printStackTrace();
         System.exit(1);
      }
   }
   
   // Reads input files for elevator calls
   public void readFile(String fileName) {
      Path path = Paths.get(fileName); 
      try {
    	  allLines = Files.readAllLines(path);
    	  for (String line : allLines) {
  			String temp = line;
  			sendInstructions(temp);
        }
      } catch (IOException e) {
			e.printStackTrace();  
      }
   }

   public void sendInstructions(String line) {
	   System.out.println(line);
	   byte msg[] = line.getBytes();
   }
   
   public void sendAndReceive(int n)
   {
//      // Prepare a DatagramPacket and send it via sendReceiveSocket
//
//      System.out.println("Floor: sending a packet containing:\n" + requestFloorButton);
//
//      byte msg[] = allLines.getBytes();
//
//      // Construct a datagram packet that is to be sent to a specified port 
//      // on a specified host.
//      try {
//         sendPacket = new DatagramPacket(msg, msg.length,
//                                       InetAddress.getLocalHost(), 5000);
//      } catch (UnknownHostException e) {
//         e.printStackTrace();
//         System.exit(1);
//      }
//
//      System.out.println("Client: Sending packet:");
//      System.out.println("To Scheduler: " + sendPacket.getAddress());
//      System.out.println("Destination host port: " + sendPacket.getPort());
//      int len = sendPacket.getLength();
//      System.out.println("Length: " + len);
//      System.out.print("Containing: ");
//      System.out.println(new String(sendPacket.getData(),0,len)); // or could print "s"
//
//      // Send the datagram packet to the server via the send/receive socket. 
//
//      try {
//         sendReceiveSocket.send(sendPacket);
//      } catch (IOException e) {
//         e.printStackTrace();
//         System.exit(1);
//      }
//
//      System.out.println("Client: Packet sent.\n");
//      elevatorLight = true; // light switches on
//      
//      // Construct a DatagramPacket for receiving packets up 
//      // to 100 bytes long (the length of the byte array).
//
//      byte data[] = new byte[100];
//      receivePacket = new DatagramPacket(data, data.length);
//
//      try {
//         // Block until a datagram is received via sendReceiveSocket.  
//         sendReceiveSocket.receive(receivePacket);
//      } catch(IOException e) {
//         e.printStackTrace();
//         System.exit(1);
//      }
//
//      // Process the received datagram.
//      System.out.println("Client: Packet received:");
//      System.out.println("From host: " + receivePacket.getAddress());
//      System.out.println("Host port: " + receivePacket.getPort());
//      len = receivePacket.getLength();
//      System.out.println("Length: " + len);
//      System.out.print("Containing: ");
//
//      // Form a String from the byte array.
//      String received = new String(data,0,len);   
//      System.out.println(received);
//
//      // We're finished, so close the socket.
//      sendReceiveSocket.close();
   }

   public static void main(String args[])
   {
	  Floor Floors[] = new Floor[numOfFloors]; 
	  for (int i = 1; i <= numOfFloors; i++) {
		  Floors[i-1] = new Floor(i);
	  }
      Floors[0].readFile(fileName);
   }
}

