package sourcecoded.comms2.api.data.buffer;

/**
 * A buffer used with Generic Types. Allows you to temporarily store data for comparison later.
 * @author SourceCoded
 *
 * @param <T> The type of data stored.
 */
public class GenericBuffer<T> {

	private T value;
	
	/**
	 * Create a new Generic Types buffer
	 */
	public GenericBuffer() {
	}
	
	/**
	 * Create a new Generic Types buffer
	 * @param data The data to store in the buffer.
	 */
	public GenericBuffer(T data) {
		this.value = data;
	}
	
	/**
	 * Is this buffer empty?
	 * @return true if null, false if used
	 */
	public boolean isEmpty() {
		return value == null;
	}
	
	/**
	 * Override the data stored in the buffer with param data
	 * @param data
	 */
	public void putData(T data) {
		value = data;
	}
	
	/**
	 * Get the data stored in the buffer
	 * @return the data
	 */
	public T getData() {
		return value;
	}
	
	/**
	 * Dump the data in the array. 
	 */
	public void dumpData() {
		value = null;
	}
	
	/**
	 * Returns true if the stored value differs from the comparative value
	 * @param compare The data to compare
	 * @return changed
	 */
	public boolean hasChanged(T compare) {
		return !value.equals(compare);
	}
	
	/**
	 * Get the data stored in the buffer. 
	 * @return the data. Null if empty.
	 */
	public T getStored() {
		return value;
	}
}
