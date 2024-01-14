package code;

import java.util.Scanner;

public class Main {
	
	
	//⬜ = blank space
	//🧍 = start point
	//🚩 = end point
	//⬛ = wall
	
	public static int g(int x1, int y1, int x2, int y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}
	
	public static boolean allowed(int n, int m, int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < m;
	}
	
	public static void display(int[][] grid) {
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < grid[i].length; j++) {
				if(grid[i][j] == 0) System.out.print("⬜");
				else if(grid[i][j] == 1) System.out.print("🧍");
				else if(grid[i][j] == 2) System.out.print("🚩");
				else if(grid[i][j] == 3) System.out.print("⬆️");
				else if(grid[i][j] == 4) System.out.print("➡️");
				else if(grid[i][j] == 5) System.out.print("⬇️");
				else if(grid[i][j] == 6) System.out.print("⬅️");
				else if(grid[i][j] == -1) System.out.print("⬛");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		int[] dx = {0, 1, 0, -1};
		int[] dy = {1, 0, -1, 0};
		
		int n = 20, m = 20, start_x, start_y, end_x, end_y, ptr = 1;
		
		int[][] grid = new int[n][m];
		int[][] queue = new int[n * m][2];
		boolean[][] seen = new boolean[n][m];
		int[][] f_cost = new int[n][m];
		int[][][] parent = new int[n][m][2];
		
		
		System.out.print("Enter starting point X and Y: ");
		start_y = input.nextInt() - 1;
		start_x = 20 - input.nextInt();
		grid[start_x][start_y] = 1;
		display(grid);
		
		
		System.out.print("Enter ending point X and Y: ");
		end_y = input.nextInt() - 1;
		end_x = 20 - input.nextInt();
		grid[end_x][end_y] = 2;
		display(grid);
		
		
		int wall_x, wall_y;
		do {
			System.out.print("Add a wall point X and Y (enter 0 for both to stop): ");
			wall_y = input.nextInt() - 1;
			wall_x = 20 - input.nextInt();
			if(allowed(n, m, wall_x, wall_y) && grid[wall_x][wall_y] == 0) {
				grid[wall_x][wall_y] = -1;
				display(grid);
			} 
		} while(!(wall_x == 20 && wall_y == -1));
		
		
		for(int i = 0; i < n * m; i++) {
			if(i == 0) {
				queue[i][0] = start_x;
				queue[i][1] = start_y;
			} else {
				queue[i][0] = -1;
				queue[i][1] = -1;
			}
		}
		
		
		f_cost[start_x][start_y] = g(start_x, start_y, end_x, end_y);
		
		while(true) {
			
			//find node in OPEN with the lowest cost
			int lowest_f_cost = Integer.MAX_VALUE, idx = -1, x = -1, y = -1;
			for(int i = 0; i < n * m; i++) {
				if(queue[i][0] != -1 && (f_cost[queue[i][0]][queue[i][1]] < lowest_f_cost || (f_cost[queue[i][0]][queue[i][1]] == lowest_f_cost && g(queue[i][0], queue[i][1], end_x, end_y) < g(x, y, end_x, end_y)))) {
					lowest_f_cost = f_cost[queue[i][0]][queue[i][1]];
					idx = i;
					x = queue[i][0];
					y = queue[i][1];
				}
			}
			
			if(idx == -1) {
				System.out.println("😔 It's impossible to go from the start to end!");
				display(grid);
				break;
			}
			
			if(x == end_x && y == end_y) {
				System.out.println("Found! The minimum distance is " + f_cost[end_x][end_y]);
				while(x != 0 || y != 0) {
					if(grid[x][y] == 0) {
						grid[x][y] = 3;
					}
					int original_x = x, original_y = y;
					x = parent[original_x][original_y][0];
					y = parent[original_x][original_y][1];
					if(!(x == start_x && y == start_y)) {
						if(x == original_x + 1) 
							grid[x][y] = 3;
						else if(x == original_x - 1)
							grid[x][y] = 5;
						else if(y == original_y + 1)
							grid[x][y] = 6;
						else if(y == original_y - 1)
							grid[x][y] = 4;
					}
					
				}
				display(grid);
				break;
			}
	
			//remove node from OPEN
			queue[idx][0] = -1;
			queue[idx][1] = -1;
			
			//add node to CLOSED
			seen[x][y] = true;
			
			for(int i = 0; i < 4; i++) {
				if(!allowed(n, m, x + dx[i], y + dy[i]) || grid[x + dx[i]][y + dy[i]] == -1 || seen[x + dx[i]][y + dy[i]]) {
					continue;
				}
				int new_cost = (lowest_f_cost - g(x, y, end_x, end_y) + 1) + g(x + dx[i], y + dy[i], end_x, end_y);
				
				boolean inOpen = false;
				for(int j = 0; j < n * m; j++) {
					if(queue[j][0] == x + dx[i] && queue[j][1] == y + dy[i]) {
						inOpen = true;
					}
				}
				if(new_cost < f_cost[x + dx[i]][y + dy[i]] || !inOpen) {
					f_cost[x + dx[i]][y + dy[i]] = new_cost;
					parent[x + dx[i]][y + dy[i]][0] = x;
					parent[x + dx[i]][y + dy[i]][1] = y;
	
					if(!inOpen) {
						queue[ptr][0] = x + dx[i];
						queue[ptr][1] = y + dy[i];
						ptr++;
					}
				}
			}
			
		}
	}

}
