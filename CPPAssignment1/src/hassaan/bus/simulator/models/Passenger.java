package hassaan.bus.simulator.models;

import java.util.Date;
/**
 * It contains the definition for the information holding related the Passenger. The class is a thread-safe.
 */
public class Passenger {	

	/**
	 * Arrival DateTime of the passenger
	 */
	Date arrivalDateTime;
	/**
	 * OnBoard DateTime of the passenger
	 */
	Date onBoardDateTime;
	/**
	 * Departure DateTime of the passenger 
	 */
	Date departureDateTime;
	/**
	 * OnBoarding Station number
	 */
	int onBoardStation;
	/**
	 * Departure Station number
	 */
	int departureStation;

	/**
	 * The constructor for the Passenger class 
	 * @param obSt - The onBoarding Station number
	 * @param dpSt - departure Station number
	 */
	public Passenger(int obSt, int dpSt){
		onBoardStation = obSt;
		departureStation = dpSt;
		arrivalDateTime = new Date();
	}

	/**
	 * setter property for onBoardDateTime
	 */
	public synchronized void setOnBoardDate(){
		onBoardDateTime = new Date();
	}

	/**
	 * setter property for departureDateTime
	 */
	public synchronized void setDepartureDate(){
		departureDateTime = new Date();
	}

	/**
	 * getter property for departureDateTime
	 */
	public synchronized int getDepartureStation(){
		return departureStation;
	}


	/**
	 * overrides the default functionality of Passenger class
	 * @return sample string - "Passenger arrived at station no: 1 at datetime and onboard on bus at datetime and departure at datetime at station no: 4"
	 */
	@Override
	public synchronized String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Passenger arrived at station no: ");
		builder.append(onBoardStation);
		builder.append(" at ");
		builder.append(arrivalDateTime);
		builder.append(" and onboarded on bus at: ");
		builder.append(onBoardDateTime);
		builder.append(" and departure at: ");
		builder.append(departureDateTime);
		builder.append(" at station no: ");
		builder.append(departureStation);		
		return builder.toString();
	}

}
