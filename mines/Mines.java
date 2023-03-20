package mines;

import java.util.Random;

//This class is responsible for creating and managing the minesweeper game.
public class Mines {
	// It has a 2D array of location objects called minesmap,
	// which represents the map of the game.
	// It also has the height, width, and showAll properties.
	private location[][] minesmap;
	private int height, width;
	private boolean showAll = false;

	/*
	 * The constructor takes in the height, width, and number of mines as parameters
	 * and initializes the minesmap array, with all location objects initialized as
	 * non-mines. It then uses a random number generator to randomly place the mines
	 * on the map.
	 */
	public Mines(int height, int width, int numMines) {
		this.height = height;
		this.width = width;
		minesmap = new location[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				minesmap[i][j] = new location();
			}
		}
		Random rd = new Random();
		int randheight, randwidth;
		for (int n = 0; n < numMines; n++) {
			do {
				randheight = rd.nextInt(height);
				randwidth = rd.nextInt(width);

			} while (minesmap[randheight][randwidth].mine);
			addMine(randheight, randwidth);
		}
	}

	/*
	 * The addMine method is used to add a mine to a specified location on the
	 * minesmap and increases the count of neighboring mines for all surrounding
	 * locations.
	 */
	public boolean addMine(int i, int j) {
		for (int row = i - 1; row <= i + 1; row++)
			for (int col = j - 1; col <= j + 1; col++)
				if (row >= 0 && row < height && col >= 0 && col < width)
					if (!(row == i && col == j))
						minesmap[row][col].nighbormine++;
		return minesmap[i][j].mine = true;
	}

	/*
	 * The location class is an inner class that contains properties for each
	 * location such as if it is a mine, open, and if it has a flag.
	 */
	class location {
		private boolean mine, open, flag;
		private int nighbormine;

		public location() {
			mine = false;
			open = false;
			flag = false;
			nighbormine = 0;
		}
	}

	/*
	 * The open method is used to open a location on the minesmap and if the
	 * location is a mine, it returns false. If the location is not a mine and has
	 * no neighboring mines, it recursively opens all neighboring locations.
	 */
	public boolean open(int i, int j) {
		if (minesmap[i][j].open)
			return true;
		if (minesmap[i][j].mine)
			return false;
		minesmap[i][j].open = true;
		if (minesmap[i][j].nighbormine == 0) {
			for (int row = i - 1; row <= i + 1; row++) {
				for (int col = j - 1; col <= j + 1; col++) {
					if (!(row == i && col == j)) {
						if (row >= 0 && row < minesmap.length && col >= 0 && col < minesmap[row].length) {
							open(row, col);
						}
					}
				}
			}
		}
		return true;
	}

	/*
	 * The toggleFlag method is used to toggle the flag property of a location on
	 * the minesmap.
	 */
	public void toggleFlag(int x, int y) {
		if (minesmap[x][y].flag)
			minesmap[x][y].flag = false;
		else {
			minesmap[x][y].flag = true;
		}
	}

	/*
	 * The isDone method checks if all non-mine locations on the minesmap are open,
	 * returning true if so and false otherwise.
	 */
	public boolean isDone() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if (!minesmap[i][j].mine) {
					if (!minesmap[i][j].open)
						return false;
					continue;
				}
				continue;
			}
		return true;
	}

	/*
	 * The get method is used to get the value of a location on the minesmap and
	 * returns "F" if it is flagged, "." if it is not open and not flagged, "X" if
	 * it is a mine, and the number of neighboring mines if it is open.
	 */
	public String get(int i, int j) {
		if (!showAll && !minesmap[i][j].open) {
			if (minesmap[i][j].flag)
				return "F";
			else
				return ".";
		} else if (minesmap[i][j].mine)
			return "X";
		else
			return minesmap[i][j].nighbormine > 0 ? Integer.toString(minesmap[i][j].nighbormine) : " ";
	}

	/*
	 * The showAll method sets the showAll property to true, revealing all locations
	 * on the minesmap.
	 */
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	/*
	 * The getWidth and getHeight methods return the width and height of the
	 * minesmap respectively.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(get(i, j));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
