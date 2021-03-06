
// Scheduler.java
// Maveric Garde 101031617
// This class is the Intermediate of a Client/Server UDP client on
// UDP/IP. The server receives from a client (elevator button/user) or server (Elevator) a packet 
// containing a data array with floor and direction, then forwards it to the other client or server.
// Last edited Feb 9th 2019

import java.io.*;
import java.net.*;
import java.util.*;

public class Scheduler {

	DatagramPacket sendPacket, receivePacket;
	DatagramSocket sendSocket, receiveSocket;
	Date currentDate;
	int elevatorState1, elevatorState2, elevatorState3, //will have to turn these into thread safe
		elevatorFloor1, elevatorFloor2, elevatorFloor3; //collections, ArrayList? 
	Collection<Integer> c = Collections.synchronizedCollection(new ArrayList<Integer>(6)); //this will eventually be used to synch states (0-2) and current floors (3-5)	
	static int ELEVATOR1PORT = 69, PACKETSIZE = 25, CLIENTPORT, SELFPORT = 219;

	public Scheduler()
	{
		elevatorState1 = 0; elevatorState2 = 0; elevatorState3 = 0; elevatorFloor1 = 0; elevatorFloor2 = 0; elevatorFloor3 = 0; //all elevators should be idle at startup
		try {
			// Construct a datagram socket and bind it to any available 
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();

			// Construct a datagram socket and bind it to port 23 
			// on the local host machine. This socket will be used to
			// receive UDP Datagram packets from the client
			receiveSocket = new DatagramSocket(SELFPORT);

			// to test socket timeout (2 seconds)
			//receiveSocket.setSoTimeout(2000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} 
	}

	private int getBestElevator() {
		//from current data which elevator is best to send
		//return its number
		return -1;
	}

	private void sendElevator(int elev, int floor, byte msg[]) {
		//from the best elevator create the correct packet and send 
		//correct data
		int toPort;
		//assign proper port
		switch(elev) {
		default:
			toPort = ELEVATOR1PORT;
		}
		sendPacket = new DatagramPacket(msg, msg.length,
				receivePacket.getAddress(), toPort);
		// Send the datagram packet to the client via the send socket. 
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Server: packet sent");

	}
	public void receiveAndSend()
	{
		// Construct a DatagramPacket for receiving packets up 
		// to 100 bytes long (the length of the byte array).
		updateDate();
		int toFloor = 0;
		byte data[] = new byte[PACKETSIZE];
		receivePacket = new DatagramPacket(data, data.length);
		System.out.println("Scheduler: Waiting for Packet from User.\n");

		// Block until a datagram packet is received from receiveSocket.
		try {        
			System.out.println("Waiting..."); // so we know we're waiting
			receiveSocket.receive(receivePacket);

			//catch IO exception and print stack trace
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		System.out.println("Scheduler: Packet received:");
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		int fromPort = receivePacket.getPort();
		int len = receivePacket.getLength();
		System.out.println("Length: " + len);
		System.out.print("Containing: " );

		//decode request and assign toFloor as the floor that will be sent to elevator

		if(fromPort == 1) { //1 is arbitrary (from client/Button)
			byte msg[] = new byte[PACKETSIZE];
			msg[0] = data[0]; //direction
			int floorRequest0 = data[1];
			int floorRequest1 = data[2];
			if(floorRequest0 == 0) {
				toFloor = floorRequest1;
			}
			else {
				toFloor = floorRequest1 + 10;
			}
			msg[1] = data[1];
			msg[2] = data[2];
			
			int toElevator = getBestElevator();
			sendElevator(toElevator, toFloor, msg);
			System.out.println( "Server: Sending packet:");
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
			len = sendPacket.getLength();
			System.out.println("Length: " + len);
			System.out.print("Containing: ");
			System.out.println(new String(sendPacket.getData(),0,len));
			// or (as we should be sending back the same thing)
			// System.out.println(received);
		}
		//decode data packet from elevator and update status bars for elevators
		if(fromPort == 2c) { //received from elevator
			int elevatorNumber = data[0];
			int floorDecode = data[2];
			int currFloor;
			if(floorDecode == 0) {
				currFloor = data[3];
			}
			else {
				currFloor = data[3] + 10;
			}
			//update the correct elevator
			switch(elevatorNumber) {
			default:
				//direction: 0 = stop; 1 = up; 2 = down
				elevatorState1 = data[1];
				elevatorFloor1 = currFloor;
			case 2: 
				elevatorState2 = data[1];
				elevatorFloor2 = currFloor;
			case 3:
				elevatorState3 = data[1];
				elevatorFloor3 = currFloor;
			}



		}

		// Slow things down (wait 2 seconds)
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e ) {
			e.printStackTrace();
			System.exit(1);
		}




		//Identify and get best elevators port for send packet









		// We're finished, so close the sockets.
		sendSocket.close();
		receiveSocket.close();
	}

	void updateDate() {
		currentDate = new Date();
	}

	public static void main( String args[] )
	{
		Scheduler a = new Scheduler();
		while(true) {
			a.receiveAndSend();
		}
	}
}
