package hassaan.bus.simulator.commons;

import java.util.Random;

public class Helpers {

	/** 
	 * Generates the Random Number between the upper and lower limits
	 * @param upperBound - the upper limit
	 * @param lowerBound - the lower limit
	 */
	public static int getRandomNumber(int upperBound, int lowerBound){
		return	lowerBound + (int)(Math.random() * ((upperBound - lowerBound) + 1));
	}

	/** 
	 * Selects a random index from the array
	 * @param arr - Expecting the array
	 * @param lowerBound - the lower limit
	 */
	public static <T> T getRandomElementFromArray(T[] arr) 
	{ 
		Random rand = new Random(); 
		return (T)arr[rand.nextInt(arr.length)]; 
	}

	/** 
	 * Generates a random number from the given limit
	 * @param limit - the limit of the number
	 */
	public static int getRandomNumber(int limit){
		return (int)(Math.random()*limit);
	}
}
