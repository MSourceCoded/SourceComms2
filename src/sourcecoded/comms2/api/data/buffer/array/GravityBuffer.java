package sourcecoded.comms2.api.data.buffer.array;

import java.util.ArrayList;

/**
 * Gravity buffer. Sort of like a queue, first in, first served.
 * 
 * @author SourceCoded
 *
 * @param <T> The type of data used
 */
public class GravityBuffer<T> {

	private ArrayList<T> values;
	
	public GravityBuffer() {
		values = new ArrayList<T>();
	}
	
	/**
	 * Append data to the end of the buffer
	 * @param data
	 */
	public void append(T data) {
		values.add(data);
	}
	
	/**
	 * Retrieve the data at the bottom of the stack
	 * @return
	 */
	public T retrieve() {
		return values.get(0);
	}

	/**
	 * Delete the data at the bottom of the stack
	 */
	@SuppressWarnings("unused")
	public void delete() {
		for (int i = 0; i < values.size(); i++) {
			
			if (i != values.size() - 1) {
				values.set(i, values.get(i + 1));
			}
		}
		
		T dummy = values.remove(values.size() - 1);				//For some stupid reason it reads 'i' as an object when in an int array
	}

	/**
	 * Size of the array
	 * @return
	 */
	public int size() {
		return values.size();
	}
	
	/**
	 * Get the raw arraylist used in the buffer
	 * @return
	 */
	public ArrayList<T> getArrayList() {
		return values;
	}
	
	public String toString() {
		return values.toString();
	}
}