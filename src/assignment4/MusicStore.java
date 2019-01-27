//Alexis Franche (260791358)
package assignment4;

import java.util.ArrayList;

public class MusicStore {

	private MyHashTable<String, Song> Title;
	private MyHashTable<String, ArrayList<Song>> Artist;
	private MyHashTable<Integer, ArrayList<Song>> Year;

	public MusicStore(ArrayList<Song> songs) {
		Title = new MyHashTable<String, Song>(songs.size());
		for (Song song : songs) {
			Title.put(song.getTitle(), song);
		}

		Artist = new MyHashTable<String, ArrayList<Song>>(songs.size());
		for (Song song : songs) {
			if ((Artist.get(song.getArtist()) == null) || (Artist.get(song.getArtist()).isEmpty())) {
				ArrayList<Song> listOfArtist = new ArrayList<Song>();
				listOfArtist.add(song);
				Artist.put(song.getArtist(), listOfArtist);
			} else {
				Artist.get(song.getArtist()).add(song);
			}
		}

		Year = new MyHashTable<Integer, ArrayList<Song>>(songs.size());
		for (Song song : songs) {
			if ((Year.get(song.getYear()) == null) || (Year.get(song.getYear()).isEmpty())) {
				ArrayList<Song> listOfYear = new ArrayList<Song>();
				listOfYear.add(song);
				Year.put(song.getYear(), listOfYear);
			} else {
				Year.get(song.getYear()).add(song);
			}
		}
	}

	/**
	 * Add Song s to this MusicStore
	 */
	public void addSong(Song s) {

		if ((Year.get(s.getYear()).isEmpty() || Year.get(s.getYear()) == null)
				&& (Artist.get(s.getArtist()).isEmpty() || Artist.get(s.getArtist()) == null)) {
			ArrayList<Song> byYear = new ArrayList<Song>();
			ArrayList<Song> byArtist = new ArrayList<Song>();

			byArtist.add(s);
			byYear.add(s);
			Artist.put(s.getArtist(), byArtist);
			Year.put(s.getYear(), byYear);
		} else if ((Year.get(s.getYear()).isEmpty() || Year.get(s.getYear()) == null)
				&& (!Artist.get(s.getArtist()).isEmpty() || Artist.get(s.getArtist()) != null)) {
			ArrayList<Song> newYear = new ArrayList<>();
			newYear.add(s);
			Artist.get(s.getArtist()).add(s);
			Year.put(s.getYear(), newYear);
		} else if ((Artist.get(s.getArtist()).isEmpty() || Artist.get(s.getArtist()) == null)
				&& (!Year.get(s.getYear()).isEmpty() || Year.get(s.getYear()) != null)) {
			ArrayList<Song> byArtist2 = new ArrayList<Song>();
			byArtist2.add(s);
			Year.get(s.getYear()).add(s);
			Artist.put(s.getArtist(), byArtist2);
		} else {
			Artist.get(s.getArtist()).add(s);
			Year.get(s.getYear()).add(s);
		}
		Title.put(s.getTitle(), s);
	}

	/**
	 * Search this MusicStore for Song by title and return any one song by that
	 * title
	 */
	public Song searchByTitle(String title) {
		return Title.get(title);
	}

	/**
	 * Search this MusicStore for song by `artist' and return an ArrayList of all
	 * such Songs.
	 */
	public ArrayList<Song> searchByArtist(String artist) {
		return Artist.get(artist);
	}

	/**
	 * Search this MusicSotre for all songs from a `year' and return an ArrayList of
	 * all such Songs
	 */
	public ArrayList<Song> searchByYear(Integer year) {
		return Year.get(year);
	}
}
