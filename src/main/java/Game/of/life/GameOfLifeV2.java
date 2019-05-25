package Game.of.life;

import java.util.ArrayList;
import java.util.List;

public class GameOfLifeV2{

	public GameOfLifeV2(boolean[][] grid) {
		this.grid = grid;
	}
	private boolean[][] grid;

	private static class Coordinate {
		Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int x;
		public int y;
		
		public String toString()
		{
			return "[" + x + "," + y + "]";
		}
	}

	private boolean applyRule(Coordinate position) {
		boolean alive = false;

		List<Coordinate> neighbours = getNeighbours(position);
		
		long liveCount = neighbours.stream()
				.filter((c) -> grid[c.x][c.y])
				.count();
		
		boolean isAlive = grid[position.x][position.y];
	
		if ((isAlive && liveCount == 2 )|| liveCount == 3) {
			alive = true;
		}
	

		return alive;
	}
	
	private boolean[][] next()
	{
		int xLength = grid.length;
		
		int yLength = grid[0].length;
		
		boolean[][] nextState = new boolean[grid.length][grid[0].length];
		
		for(int x=0; x < xLength; x++)
		{
			for(int y =0 ; y < yLength; y++)
			{
				nextState[x][y] = applyRule(new Coordinate(x, y));
			}
		}
		
		grid = nextState;
		return nextState;
	}
	
	/**
	 * Gets the neighbors for the given co-ordinate.
	 * 
	 * @param position The point for which we have to get the neighbors.
	 * @return List of neighbor coordinates
	 */
	private List<Coordinate> getNeighbours(Coordinate position) {
		int x = position.x;
		int y = position.y;

		List<Coordinate> neighbours = new ArrayList<GameOfLifeV2.Coordinate>();

		int[] offsets = { -1, 0, 1 };

		for (int xoffsets : offsets) {
			for (int yoffsets : offsets) {
				if ((xoffsets == 0 && yoffsets == 0) || // ignore returning the same co-ordinate
						(x == 0 && xoffsets == -1) || // Ignore returning negative x
						(y == 0 && yoffsets == -1) ||
						(x == grid.length -1) ||
						(y == grid[0].length -1))
				{
					// do nothing
				} else {
					neighbours.add(new Coordinate(x + xoffsets, y+yoffsets));
				}
			}
		}

		return neighbours;
	}
	
	public static void main(String[] args) throws InterruptedException {
		boolean[][] initial = new boolean[5][5];
		initial[2][1] = true;
		initial[2][2] = true;
		initial[2][3] = true;

		GameOfLifeV2 life = new GameOfLifeV2(initial);

		for (int i = 0; i < 5; i++) {
			boolean[][] currState = life.next();
			System.out.println(getLiveCoordinates(currState));
		}
	}

	/**
	 * Gets the live coordinates from the given bit matrix
	 * 
	 * @param cells The matrix that represents the coordinates with live states.
	 * @return List of corrdindates that are set in the given matrix.
	 */
	private static List<Coordinate> getLiveCoordinates(boolean[][] cells) {
		List<Coordinate> coordindates = new ArrayList<>();

		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[0].length; j++) {
				if (cells[i][j]) {
					coordindates.add(new Coordinate(i, j));
				}
			}
		}
		return coordindates;
	}
}
