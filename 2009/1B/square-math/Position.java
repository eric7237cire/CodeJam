import java.util.ArrayList;
import java.util.List;



import com.google.common.base.Objects;


	public class Position {
		private int row;
		private int col;

		private static int maxRow;
		private static int maxCol;
		
		public Position(int index) {
			super();
			this.row = index / maxCol;
			this.col = index % maxCol; 
		}

		public Position(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}

		/**
		 * @return the row
		 */
		public int getRow() {
			return row;
		}

		/**
		 * @param row
		 *            the row to set
		 */
		public void setRow(int row) {
			this.row = row;
		}

		/**
		 * @return the col
		 */
		public int getCol() {
			return col;
		}

		/**
		 * @param col
		 *            the col to set
		 */
		public void setCol(int col) {
			this.col = col;
		}

		/**
		 * @return the maxRow
		 */
		public static int getMaxRow() {
			return maxRow;
		}

		/**
		 * @param maxRow
		 *            the maxRow to set
		 */
		public static void setMaxRow(int maxRow) {
			Position.maxRow = maxRow;
		}

		/**
		 * @return the maxCol
		 */
		public static int getMaxCol() {
			return maxCol;
		}

		/**
		 * @param maxCol
		 *            the maxCol to set
		 */
		public static void setMaxCol(int maxCol) {
			Position.maxCol = maxCol;
		}

		public List<Position> getAdjPositions() {
			List<Position> ret = new ArrayList<>();

			if (row > 0) {
				ret.add(new Position(row - 1, col));
			}

			if (col > 0) {
				ret.add(new Position(row, col - 1));
			}
			if (row < maxRow - 1) {
				ret.add(new Position(row + 1, col));
			}
			if (col < maxCol - 1) {
				ret.add(new Position(row, col + 1));
			}

			return ret;
		}

		public int toInt() {
			return col + row * maxCol;
		}

		@Override
		public String toString() {

			return Objects.toStringHelper(this).add("row", row).add("col", col)
					.toString();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}

			Position rhs = (Position) obj;

			return Objects.equal(rhs.row, row) && Objects.equal(rhs.col, col);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(row, col);
		}

	}