//Alexis Franche (260791358)
package assignment4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MyHashTable<K, V> implements Iterable<HashPair<K, V>> {
	// num of entries to the table
	private int numEntries;
	// num of buckets
	private int numBuckets;
	// load factor needed to check for rehashing
	private static final double MAX_LOAD_FACTOR = 0.75;
	// ArrayList of buckets. Each bucket is a LinkedList of HashPair
	private ArrayList<LinkedList<HashPair<K, V>>> buckets;

	// constructor
	public MyHashTable(int initialCapacity) {
		// ADD YOUR CODE BELOW THIS
		numEntries = 0;
		numBuckets = initialCapacity;
		buckets = new ArrayList<LinkedList<HashPair<K, V>>>(numBuckets);
		for (int z = 0; z < numBuckets; z++) {
			LinkedList<HashPair<K, V>> list = new LinkedList<HashPair<K, V>>();
			buckets.add(z, list);
		}

		// ADD YOUR CODE ABOVE THIS
	}

	public int size() {
		return this.numEntries;
	}

	public int numBuckets() {
		return this.numBuckets;
	}

	/**
	 * Returns the buckets vairable. Usefull for testing purposes.
	 */
	public ArrayList<LinkedList<HashPair<K, V>>> getBuckets() {
		return this.buckets;
	}

	/**
	 * Given a key, return the bucket position for the key.
	 */
	public int hashFunction(K key) {
		int hashValue = Math.abs(key.hashCode()) % this.numBuckets;
		return hashValue;
	}

	/**
	 * Takes a key and a value as input and adds the corresponding HashPair to this
	 * HashTable. Expected average run time O(1)
	 */
	public V put(K key, V value) {
		// ADD YOUR CODE BELOW HERE
		int bucketPos;
		boolean add = true;
		HashPair<K, V> newPair = new HashPair<K, V>(key, value);
		V val = null;
		bucketPos = hashFunction(key);
		double curLoad = this.numEntries / this.numBuckets;

		if (!key.equals(null)) {
			if (this.buckets.get(bucketPos).size() == 0) {
				buckets.get(bucketPos).add(newPair);
				this.numEntries = this.numEntries + 1;
				if (curLoad > MAX_LOAD_FACTOR) {
					this.rehash();
				}

			} else {

				for (HashPair<K, V> mypair : this.buckets.get(bucketPos)) {

					if (mypair.getKey().equals(key)) {
						val = mypair.getValue();
						mypair.setValue(value); // set new value
						add = false;
					}

				}
				if (add) {
					this.buckets.get(bucketPos).add(newPair);
					this.numEntries = this.numEntries + 1;
					if (curLoad > MAX_LOAD_FACTOR) {
						this.rehash();
					}

				}

			}

		}
		return val;
		// ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Get the value corresponding to key. Expected average runtime = O(1)
	 */

	@SuppressWarnings("unchecked")
	public V get(K key) {
		// ADD YOUR CODE BELOW HERE
		try {
			V itemVal = null;
			int location = Math.abs(key.hashCode()) % this.numBuckets;
			if (!key.equals(null)) {
				for (HashPair<K, V> pair : buckets.get(location)) {
					if (pair.getKey().equals(key)) {
						itemVal = pair.getValue();
						return itemVal;
					}
				}
			}

			return itemVal;
		} catch (NullPointerException e) {
			String empty = "";
			return new HashPair<K, V>(key, (V) empty).getValue();
		}

		// ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Remove the HashPair corresponding to key . Expected average runtime O(1)
	 */
	public V remove(K key) {
		// ADD YOUR CODE BELOW HERE
		int location = Math.abs(key.hashCode()) % this.numBuckets;
		V val = null;
		int where = 0;
		if (!key.equals(null)) {
			for (HashPair<K, V> pair : buckets.get(location)) {
				if (pair.getKey().equals(key)) {
					val = pair.getValue();
					buckets.get(location).remove(where);
					break;
				}
				where++;
			}
		}
		return val;// remove
		// ADD YOUR CODE ABOVE HERE
	}

	// Method to double the size of the hashtable if load factor increases
	// beyond MAX_LOAD_FACTOR.
	// Made public for ease of testing.

	public void rehash() {
		// ADD YOUR CODE BELOW HERE

		ArrayList<LinkedList<HashPair<K, V>>> tmp = this.buckets;
		this.numBuckets = 2 * this.numBuckets;
		this.buckets = new ArrayList<LinkedList<HashPair<K, V>>>(this.numBuckets);

		for (int z = 0; z < numBuckets; z++) {
			LinkedList<HashPair<K, V>> list = new LinkedList<HashPair<K, V>>();
			this.buckets.add(z, list);
		}

		for (LinkedList<HashPair<K, V>> mylists : tmp) {
			for (HashPair<K, V> pair : mylists) {
				int loc = hashFunction(pair.getKey());
				this.buckets.get(loc).add(pair);
			}

		}

		// ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Return a list of all the keys present in this hashtable.
	 */

	public ArrayList<K> keys() {
		// ADD YOUR CODE BELOW HERE
		ArrayList<K> keyList = new ArrayList<K>();

		for (LinkedList<HashPair<K, V>> mylists : this.buckets) {
			for (HashPair<K, V> pair : mylists) {
				keyList.add(pair.getKey());
			}
		}
		return keyList;// remove
		// ADD YOUR CODE ABOVE HERE
	}

	/**
	 * Returns an ArrayList of unique values present in this hashtable. Expected
	 * average runtime is O(n)
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<V> values() {
		// ADD CODE BELOW HERE
		ArrayList<V> valList = new ArrayList<V>();
		MyHashTable<K, V> buck = new MyHashTable<K, V>(numEntries);
		for (LinkedList<HashPair<K, V>> mylists : this.buckets) {
			for (HashPair<K, V> pair : mylists) {
				buck.put((K) pair.getValue(), (V) pair.getKey());
			}
		}
		for (LinkedList<HashPair<K, V>> mylists : buck.buckets) {
			for (HashPair<K, V> pair : mylists) {
				valList.add((V) pair.getKey());
			}
		}
		return valList;// remove
		// ADD CODE ABOVE HERE
	}

	@Override
	public MyHashIterator iterator() {
		return new MyHashIterator();
	}

	private class MyHashIterator implements Iterator<HashPair<K, V>> {
		private LinkedList<HashPair<K, V>> entries;

		Iterator<HashPair<K, V>> itr;

		private MyHashIterator() {
			// ADD YOUR CODE BELOW HERE
			for (int bucket = 0; bucket < MyHashTable.this.buckets.size(); bucket++) {
				for (int i = 0; i < MyHashTable.this.buckets.get(bucket).size(); i++) {
					entries.add(MyHashTable.this.buckets.get(bucket).removeFirst());
				}

			}
			itr = entries.iterator();
			// ADD YOUR CODE ABOVE HERE
		}

		@Override
		public boolean hasNext() {
			// ADD YOUR CODE BELOW HERE
			if (itr.hasNext()) {
				return true;
			} else {
				return false;
			}
			// ADD YOUR CODE ABOVE HERE
		}

		@Override
		public HashPair<K, V> next() {
			// ADD YOUR CODE BELOW HERE
			if (itr.hasNext()) {
				return itr.next();
			} else {
				return null;
			}
			// ADD YOUR CODE ABOVE HERE
		}

	}
}
