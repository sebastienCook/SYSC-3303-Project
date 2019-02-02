import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Elevator {

	private int topFloor;
	private int bottomFloor;
	private int[] buttons;
	private ArrayList<Integer> lamp;
	private int motor; //0==stop 1==up 2==down
	private boolean door; //false=closed true=open
	private int elevatorNumber;
	private int currentFloor; //2 is lobby
	private Receiver link;
	private int SEND_PORT_NUMBER = 219; //schedualer port
	
	private ArrayList<Integer> serviceQueue; //floors that will be serviced in organized order
	private ArrayList<Integer> tempQueue; //floors in opposite direction waiting to be serviced 
	
	public Elevator(int floors, int elevatorNum) {
		this.bottomFloor = 1;
		this.topFloor = floors;
		for(int i=1;i<=floors;i++) {
			buttons[i]=i;
			
		}
		this.elevatorNumber = elevatorNum;
		this.motor = 0x0;
		this.door = false;
		this.currentFloor = 0x2;	
		tempQueue = new ArrayList<Integer>();
		lamp = new ArrayList<Integer>();
	
	}
	
	public int getMotor() {return motor;}
	public boolean getDoor() {return door;}
	public int getElevatorNumber() {return elevatorNumber;}
	public int getFloor() {return currentFloor;}
	
	public void organizeQueue() { //organizes the queue in to a sequential servicing order 
		//ArrayList<Floor> temp = link.
		if(link.newFloor.isEmpty()) {
		/*	if(!tempQueue.isEmpty()) {						//this will only be needed when temp wueue logic is implemented
				this.serviceQueue = this.tempQueue;
				this.tempQueue.clear();
				if(this.tempQueue.get(0)>this.currentFloor) {
					this.motor = 1;
				}
				else {this.motor = 2;}
			}
			else {        */
				this.motor = 0;
			//	}
		
		}
		else {
			for(int i=0;i<link.newFloor.size();i++) {  //for now all request in newFloor are added to service
				this.serviceQueue.add(link.newFloor.get(i).getFloor());
			}
			
			if(this.serviceQueue.get(0)>this.currentFloor) {
				Collections.sort(serviceQueue); //sorts list from smallest to largest
			//	this.motor = 1;
			}
			else {
				Collections.sort(serviceQueue);
				Collections.reverse(serviceQueue); //sorts list from largest to smallest
			//	this.motor = 2;
				}
		}
	}
	
	public void service() throws IOException { //moves the elevator through queue to service requests 
		
		while(!this.serviceQueue.isEmpty()) {
			if(this.currentFloor == this.serviceQueue.get(0)) {
				this.motor = 0;
				this.door = true;
				this.displayButtons();
				//Thread.sleep(1000);
				this.door = false;
				this.serviceQueue.remove(0);
			}
			else if(this.serviceQueue.get(0)>this.currentFloor){
				this.currentFloor++;
			}
			else {this.currentFloor--;}
		}
		this.motor = 0;
	}
	
	public void displayButtons() throws IOException { //will display buttons for gui, but act as stud for new passengers boarding
		//display button as gui
		Random rand = new Random();
		int newF;
		int dir;
		if(this.serviceQueue.get(0)>this.currentFloor) {
		newF = rand.nextInt(this.topFloor-this.currentFloor+1)+this.currentFloor; //generate random int between current and top floor;
		dir = 1; //up
		}
		else {
		newF = rand.nextInt(currentFloor);
		dir = 2; //down 
		}
		this.sendRequest(newF, dir);
	}
	
	public void sendRequest(int chosenFloor,int direction) throws IOException { //send new internal requests to the scheduler data-> ID,direction,floor,floor
		
		byte data[] = new byte[4];
		data[0] = (byte) this.elevatorNumber;
		data[1] = (byte) direction;
		if(chosenFloor>10) {
			data[2]=(byte)1;
			data[3] = (byte) (chosenFloor-10);
		}
		else {
			data[2]=(byte)0;
			data[3] = (byte) (chosenFloor);
		}
		
		try {
	         
			DatagramSocket sendReceiveSocket = new DatagramSocket();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length,InetAddress.getLocalHost(), 23);
			sendReceiveSocket.send(sendPacket);
	      } catch (SocketException se) {   // Can't create the socket.
	         se.printStackTrace();
	         System.exit(1);
	      }
	}
	
	public static void main(String[] args) throws IOException {
		
		Receiver link = new Receiver(69);
		link.start();
		Elevator elevator = new Elevator(6,2);
		while(true) {
			elevator.organizeQueue();
			elevator.service();
		}

	}
		
	
}
