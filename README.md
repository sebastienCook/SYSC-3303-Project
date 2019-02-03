# SYSC-3303-Project
Group project for SYSC 3303

Running Instructions:
Run classes in order: Elevator.java->Scheduler.java->floor.java
To change test. Open test.txt and add or remove commants in same format as shown

Responsibilities:
Floor.java - Sean
This Class will create 10 Floor objects, read an input file, then send the appropriate commands to the scheduler. It will wait for the scheduler to respond and will display the response.

Scheduler.java - Mav

Standard Java Libraries Required;

Timing logic - Class will receive request from floor buttons. Then it will choose the best elevator avaible and send a request to be processed to the elevator. The class will then also wait for confirmation from the elevator on its current task, location and direction.
The class must be running before any other class begins as it handles all data being transmitted within the elevator system.

Elevator.java, Receiver.java, ElevatorFloor.java - Seb

Diagrams and small changes to all classes- Andrew

documentation - everyone


Errors: Occasionally the relay of messages from elevator->scheduler->floor doesnt work so scheduler's socket times out
 
