import java.util.Scanner;


public class Maze34 {
	private static final int[] DX = {0, -1, 0, 1};
	private static final int[] DY = {1, 0, -1, 0};
	private static final int[] FLAG = {1, 2, 0, 3};
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		int tests = scanner.nextInt();
		for (int test = 0; test < tests; test++) {
			String to = scanner.next();
			String from = scanner.next();
			
			int minX = 0;
			int maxX = 0;
			int maxY = 0;
			
			int direction = 0;
			int x = 0;
			int y = 0;
			for (char ch : to.substring(1).toCharArray()) {
				minX = Math.min(minX, x);
				maxX = Math.max(maxX, x);
				maxY = Math.max(maxY, y);
				switch (ch) {
				case 'W':
					x = x + DX[direction];
					y = y + DY[direction];
					break;
				case 'L':
					direction = (direction + 3) % 4;
					break;
				case 'R':
					direction = (direction + 1) % 4;
					break;
				}
			}
			direction = (direction + 2) % 4;
			x = x + DX[direction];
			y = y + DY[direction];
			for (char ch : from.substring(1).toCharArray()) {
				minX = Math.min(minX, x);
				maxX = Math.max(maxX, x);
				maxY = Math.max(maxY, y);
				switch (ch) {
				case 'W':
					x = x + DX[direction];
					y = y + DY[direction];
					break;
				case 'L':
					direction = (direction + 3) % 4;
					break;
				case 'R':
					direction = (direction + 1) % 4;
					break;
				}
			}

			byte[][] f = new byte[maxY + 1][maxX - minX + 1];
			direction = 0;
			x = -minX;
			y = 0;
			for (char ch : to.substring(1).toCharArray()) {
				switch (ch) {
				case 'W':
					f[y][x] |= (1 << FLAG[direction]);
					x = x + DX[direction];
					y = y + DY[direction];
					break;
				case 'L':
					direction = (direction + 3) % 4;
					break;
				case 'R':
					direction = (direction + 1) % 4;
					break;
				}
			}
			direction = (direction + 2) % 4;
			x = x + DX[direction];
			y = y + DY[direction];
			for (char ch : from.substring(1).toCharArray()) {
				switch (ch) {
				case 'W':
					f[y][x] |= (1 << FLAG[direction]);
					x = x + DX[direction];
					y = y + DY[direction];
					break;
				case 'L':
					direction = (direction + 3) % 4;
					break;
				case 'R':
					direction = (direction + 1) % 4;
					break;
				}
			}
			
			System.out.format("Case #%d:\n", test + 1);
			for (byte[] row : f) {
				for (byte value : row) {
					System.out.print(Integer.toHexString(value));
				}
				System.out.println();
			}
		}
	}
}
