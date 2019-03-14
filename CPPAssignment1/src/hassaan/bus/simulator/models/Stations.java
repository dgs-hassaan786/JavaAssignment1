package hassaan.bus.simulator.models;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import hassaan.bus.simulator.commons.Helpers;

/**
 * Class for simulating the scenario of the station.
 */
public class Stations extends Thread {
	/**
	 * Current Station Number
	 */
	int stationNumber;
	/**
	 * Delay factor for the arrival of passenger to the station
	 */
	int passengerArrivingDelay;

	/**
	 * Concurrent Queue which holds the information of the waiting passengers to the station
	 */
	BlockingQueue<Passenger> passengers;

	/**
	 * Bus class reference
	 */
	private Bus bus;

	/**
	 * volatile boolean variable used in the run method
	 */
	volatile boolean keepProcessing;

	/**
	 * constructor expecting the following parameters
	 * @param sn - Station Number
	 * @param pad - Passenger Arrival Delay (dynamic wait time in ms)
	 * @param Bus - reference of the Bus object
	 * @param keepProcessing - whether to run the thread or not
	 */
	public Stations(int sn, int pad, Bus bus, boolean keepProcessing){
		stationNumber = sn;
		passengerArrivingDelay = pad;
		passengers = new ArrayBlockingQueue<Passenger>(1024);
		this.bus = bus;
		this.keepProcessing = keepProcessing; 
	}	

	/**
	 * getter for the queue size
	 */
	public synchronized int getWaitingPassenger(){
		return passengers.size();
	}

	/**
	 * The method tries to onBoard the passengers of specific count on the bus. 
	 * If the bus is full or has no limit, the passengers have to wait for the next round of the bus.
	 * @param passengerCount - Total number of passenger to onboard on the bus  
	 */
	public synchronized void onBoardOnBus(int passengerCount){

		//System.out.println("passenger For OnBoarding: " + passengerCount);

		int i = 0,onBoarder = 0;
		for (i = 0; i < passengerCount; i++) {
			//first we are checking whether we have any queued passenger at the station
			if(passengers.size() > 0){				
				//if we have passenger waiting for onboarding on the bus 
				//we are then checking whether the bus is already overloaded or not
				if(bus.getPassengerCount() >= bus.getMaxPassengerAllowed()){				
					//Bus is overloaded					
					System.out.println("No more space available in the bus, wait for the next round.....");
					break;					
				}else{		

					//dequeue the passenger from the station and onBoard on the bus 
					Passenger p;
					try {
						p = passengers.take();
						bus.onBoard(p);
						onBoarder++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
			}else{
				System.out.println("No Passenger to onboard from station: " + stationNumber);
				break;
			}
		}

		if(i > 0 ){
			System.out.println("Total Passenger(s) "+ onBoarder + " onboarder from station " + stationNumber);
		}

	}

	/**
	 * Printing the Total Passengers inside the bus
	 */
	public synchronized void printCurrentPassengerCountInTheBus(){
		System.out.println("Total Passenger count in the bus: " + bus.getPassengerCount() + " of " + bus.getMaxPassengerAllowed());
	} 

	/**
	 * The method calls the bus.leave helper method and departure the passenger at the station  
	 * @param passengerCount - Total number of passenger to departure from the bus at the current station  
	 */
	public synchronized void leaveFromBus(int passengerCount){
		int i = 0, left = 0;
		for (i = 0; i < passengerCount; i++) {
			if(bus.getPassengerCount() > 0){
				if( bus.getPassengerByStation(stationNumber - 1) > 0){
					bus.leave(stationNumber - 1);
					left++;
				}else{
					break;
				}				
			}else{
				System.out.println("No Passenger to departure at station: " + stationNumber);
				break;
			}
		}

		if(i > 0 ){
			System.out.println("Total Passenger(s) "+ left + " left bus at station " + stationNumber);
		}

	}

	/**
	 * The method is used to update the keepProcessing variable  to false so that the thread processing shutdowns safely
	 */
	public synchronized void shutdown(){
		this.keepProcessing = false;
	}

	/**
	 * Override implementation of the Thread.run().   
	 * The method is simulating the scenario of random Passenger arriving at the stations
	 */
	@Override
	public void run() {


		// Generating random Station except the current station
		//We will be using it inside the random Departure Station
		Integer[] randomStationsExceptCurrent = new Integer[5];
		int iterator = 0;
		for (int i = 1; i <= 6; i++) {
			if(i == stationNumber)
				continue;

			randomStationsExceptCurrent[iterator++] = i; 					
		}

		//keep processing the loop unless the variable is not false
		while(keepProcessing){

			//first waiting for the delay arrival time of passenger
			try {
				Thread.sleep(passengerArrivingDelay);
			} catch (InterruptedException e) {			
				e.printStackTrace();
			}

			//generating the random passenger arriving at station;
			int randomPassenger = Helpers.getRandomNumber(45);				
			for (int i = 0; i < randomPassenger; i++) {
				//generating random stationNumber for departure other than the current one 
				int randomDepartureStation = Helpers.getRandomElementFromArray(randomStationsExceptCurrent);

				//simulating the scenario of passenger waiting at the station
				Passenger p = new Passenger(stationNumber, randomDepartureStation);
				//adding the passenger in the station queue
				passengers.add(p);
			}
		}
	}


}
