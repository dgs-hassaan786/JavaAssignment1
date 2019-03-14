package hassaan.bus.simulator;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import hassaan.bus.simulator.commons.Helpers;
import hassaan.bus.simulator.models.Bus;
import hassaan.bus.simulator.models.Stations;
import hassaan.bus.simulator.services.BusStationServiceProvider;

/**
 * 
 * @author Muhammad Hassaan Khan - 2995341
 * <p>
 * The solution is designed keeping in the mind the real-time scenario of the bus station.
 * We have 3 main entities
 * <br>
 * 1. Station
 * <br>
 * 2. Passenger
 * <br>
 * 3. Bus
 * 
 * <h2>1. Station</h2>
 * Station hold the passengers. There is a concurrent queue at each station. 
 * New coming passengers are waiting on the stations. 
 * 
 * <h2>2. Passenger</h2>
 * A passenger is an entity which has the following attributes
 * <br>
 * 1. Arrival Station (The station at which a passenger come to pick the bus)
 * <br>
 * 2. Arrival Time at that Station
 * <br>
 * 3. OnBoarding time on the bus
 * <br>
 * 4. Departure Station (The station at which the passenger left the bus)
 * <br>
 * 5. Departure Time 
 * 
 * <h2>3. Bus</h2>
 * Bus is the entity which is providing the transactions from station to station.
 * <br>
 * 1. It has a certain limit to entertain the passengers
 * <br>
 * 2. A passenger can on-board on it
 * <br>
 * 3. A passenger can left the bus
 * </p>
 *
 * <h1>Bus Service</h1>
 * There is a service which is providing the Bus transaction from stations to stations.
 * The service is performing the following functions:
 * <br>
 * 1. Moving a bus from one station to another station
 * <br>
 * 2. Departing Passenger from the bus
 * <br>
 * 3. OnBoarding Passenger on the bus
 * <br>
 * 4. Checking the capacity inside the bus
 *
 */

public class App {

	public static void main(String[] args){

		//We have a total station count of 6
		int totalStations = 6;

		//The Max Passenger which can travel in the bus is 50
		int maxPassenger = 50;

		//One single instance of the bus
		Bus bus = new Bus(maxPassenger);

		//Boolean variable which is passed as a shared context 
		//Threads will continue processing until the value is true
		boolean continueProcess = true;

		//creating a fixed thread pool of 7 
		//I have implemented the logic in such a way that 
		//each station is working as independently 
		//having it's own concurrent queue of waiting passengers 
		//and when a bus arrives to that station
		//first priority will be given to the leaving passenger
		//then the onboarding passenger
		//therefore I need threadpool of 7 to handle the 6 stations and 1 more thread for processing 
		int fixedThreadPool = 7;			
		ExecutorService executor = Executors.newFixedThreadPool(fixedThreadPool);

		//array of the Threads of stations
		Thread[] stations = new Thread[totalStations]; 

		//Initiating the Thread station and submitting to the ExecutorService
		for (int i = 0; i < totalStations; i++) {
			Stations s = new Stations(i+1, Helpers.getRandomNumber(5000, 3000), bus, continueProcess);
			executor.submit(s);
			stations[i] = s;
		}

		//created the instance of BusStationServiceProvider and sumbitted to the executor service for processing 
		BusStationServiceProvider serviceRunner  = new BusStationServiceProvider(stations, continueProcess);
		executor.submit(serviceRunner);

		//Here taking output from the system admin, if he/she wishes to close the system
		//simple press the ENTER key
		System.out.println("Press (return) to close the system......");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();

		//changing the variable value to false
		//then signaling the threads to stop the processing
		//and effectively closing the system.
		continueProcess = false;
		executor.shutdown();

		for(int i = 0; i < stations.length; i++ ){
			Stations st = (Stations)stations[i];
			st.shutdown();
		}

		serviceRunner.shutdown();

		try {
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}

		scanner.close();

		//After the proper closing of the system 
		//Printing the Bus Completed message
		System.out.println("Bus completed");
	}

}