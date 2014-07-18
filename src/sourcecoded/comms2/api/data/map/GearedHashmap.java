package sourcecoded.comms2.api.data.map;

import java.util.HashMap;

/**
 * A reverse-able HashMap. Useful for projects that require 2 data types with unknown ordering. 
 * 
 * @author SourceCoded
 *
 * @param <T> The first data type to input into the map
 * @param <U> The second data type to input into the map
 */
public class GearedHashmap<T, U> {

	//HashMaps
	private HashMap<T, U> forward;
	private HashMap<U, T> reverse;
	
	/**
	 * Create a new GearedHashmap. 
	 */
	public GearedHashmap() {
		forward = new HashMap<T, U>();
		reverse = new HashMap<U, T>();
	}
	
	/**
	 * Put a new set of values into the GearedHashmap
	 * @param firstType The <T> type variable
	 * @param secondType The <U> type variable
	 */
	public void put(T firstType, U secondType) {
		forward.put(firstType, secondType);
		reverse.put(secondType, firstType);
	}
	
	/**
	 * Retrieve a variable from the map with the <T> type key
	 * @param key
	 * @return 
	 */
	public U getForward(T key) {
		return forward.get(key);
	}
	
	/**
	 * Retrieve a variable from the map with the <U> type key
	 * @param key
	 * @return 
	 */
	public T getReverse(U key) {
		return reverse.get(key);
	}
	
	/**
	 * @return The forward HashMap (T, U)
	 */
	public HashMap<T, U> getMapForward() {
		return forward;
	}
	
	/**
	 * @return The reverse HashMap (U, T)
	 */
	public HashMap<U, T> getMapReverse() {
		return reverse;
	}
}
