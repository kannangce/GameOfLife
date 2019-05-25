package Game.of.life;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.base.Functions;

public class GameOfLifeV3 {

	public GameOfLifeV3(Collection<Coordinate> coordinates) {
		this.liveSets = new HashSet<>(coordinates);
	}

	private Set<Coordinate> liveSets;

	private static class Coordinate {
		Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x;
		public int y;

		public String toString() {
			return "[" + x + "," + y + "]";
		}

		public int hashCode() {
			return toString().hashCode();
		}

		public boolean equals(Object o) {
			if (o instanceof Coordinate) {
				Coordinate coordinate = (Coordinate) o;
				return (coordinate.x == this.x) && (coordinate.y == this.y);
			}
			return false;
		}
	}

	private Set<Coordinate> next() {
		List<Coordinate> neighboursOfLivingCells = liveSets.stream()
				.map(a -> getNeighbours(a)) // Get all neighbours for live liveSets
				.flatMap(List::stream) // Flatten out the result
				.collect(Collectors.toList());

		Map<Coordinate, Long> frequency = getFrequency(neighboursOfLivingCells);

		Predicate<? super Coordinate> liveCondition = (c) -> {
			long activeNeighbours = frequency.get(c);
			return (liveSets.contains(c) && activeNeighbours == 2) || activeNeighbours == 3;
		};
		Set<Coordinate> nextSet = frequency.keySet()
				.stream()
				.filter(liveCondition)
				.collect(Collectors.toSet());

		liveSets = nextSet;
		return nextSet;
	}

	/**
	 * Gets the neighbors for the given co-ordinate.
	 * 
	 * @param position The point for which we have to get the neighbors.
	 * @return List of neighbor liveSets
	 */
	private List<Coordinate> getNeighbours(Coordinate position) {
		int x = position.x;
		int y = position.y;

		List<Coordinate> neighbours = new ArrayList<Coordinate>();

		int[] offsets = { -1, 0, 1 };

		for (int xoffsets : offsets) {
			for (int yoffsets : offsets) {
				if (xoffsets != 0 || yoffsets != 0) {
					neighbours.add(new Coordinate(x + xoffsets, y + yoffsets));
				}
			}
		}

		return neighbours;
	}

	public static void main(String[] args) throws InterruptedException {

		List<Coordinate> liveCoordinates = Arrays.asList(new Coordinate(2, 1), new Coordinate(2, 2),
				new Coordinate(2, 3));

		GameOfLifeV3 life = new GameOfLifeV3(liveCoordinates);

		repeat(5, () -> System.out.println(life.next()));

	}

	/**
	 * Repeats the given function to the given number of times.
	 * 
	 * @param n The number of times the given function to be executed.
	 * @param f The function to be repeated.
	 */
	private static void repeat(int n, Runnable f) {
		for (int i = 1; i <= n; i++)
			f.run();
	}

	/**
	 * Gets the frequency of each element in the collection.
	 * 
	 * @param collection The collection in which we have to calculate the frequency.
	 * @return Map with the key as the element and value as its frequency.
	 */
	private <T> Map<T, Long> getFrequency(Collection<T> collection) {
		return collection.stream()
				.collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()));
	}
}
