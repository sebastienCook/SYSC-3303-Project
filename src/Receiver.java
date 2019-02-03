import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

public class Receiver extends Thread {
	
	private int portNum;
	DatagramSocket receiveSocket;
	DatagramPacket receivePacket;
	
	ArrayList<ElevatorFloor> newFloor;
	
	public Receiver(int port) {
		portNum = port;
		newFloor = new ArrayList<ElevatorFloor>();
	}
	
	
	
	public void createPort() {
		try {
			receiveSocket = new DatagramSocket(portNum);
		}
		catch (SocketException se) {
	         se.printStackTrace();
	         System.exit(1);
		}
	}
	public ArrayList<ElevatorFloor> getQueue(){
		return newFloor;
	}
	
	public void run() {
		try {
			while(true) {
				this.createPort();
				byte[] message = new byte[3];
				receivePacket = new DatagramPacket(message, message.length);
				try {        
			         System.out.println("Waiting..."); // so we know we're waiting
			         receiveSocket.receive(receivePacket);
			      } catch (IOException e) {
			         System.out.print("IO Exception: likely:");
			         System.out.println("Receive Socket Timed Out.\n" + e);
			         e.printStackTrace();
			         System.exit(1);
			      }
				//temp format: t[0]=direction t[1 and 2]=floor
				byte[] temp = receivePacket.getData();
				int dir = temp[0];
				int floor;
				if(temp[1]==0) {floor = temp[2];}
				else {floor = temp[2] + 10;};
				ElevatorFloor newRequest = new Floor(floor,dir);
				//add to queue
				newFloor.add(newRequest);
				receiveSocket.close();
			}
		}
		catch (Exception e) {
			System.out.println("Exception caught: Or is it...");
			
		}
	}
	
	
}
