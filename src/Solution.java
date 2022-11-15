import java.util.*;

/**
 * Leetcode 1926. Nearest Exit from Entrance in Maze (plus path finding).
 * <p>
 * You are given an m x n matrix maze (0-indexed) with empty cells (represented as '.') and walls
 * (represented as '+'). You are also given the entrance of the maze, where entrance = [entrancerow,
 * entrancecol] denotes the row and column of the cell you are initially standing at.
 * <p>
 * In one step, you can move one cell up, down, left, or right. You cannot step into a cell with a
 * wall, and you cannot step outside the maze. Your goal is to find the nearest exit from the
 * entrance. An exit is defined as an empty cell that is at the border of the maze. The entrance
 * does not count as an exit.
 * <p>
 * Return the number of steps in the shortest path from the entrance to the nearest exit, or -1 if
 * no such path exists.
 * <p>
 * Complexity: T - O(M * N)  |  S - O(M * N)
 */
class Solution {

    public static PathInfo nearestExit(char[][] maze, int[] entrance) {
        // Creates arrays to store visiting history and path history
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] isVisited = new boolean[rows][cols];
        char[][] path = new char[rows][cols];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Conduct BFS with a queue
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(entrance);
        isVisited[entrance[0]][entrance[1]] = true;
        path[entrance[0]][entrance[1]] = 'S';

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                for (int j = 0; j < directions.length; j++) {
                    int[] dir = directions[j];

                    int row = cur[0] + dir[0];
                    int col = cur[1] + dir[1];

                    // This is the case where the neighbor is out of bound
                    if (row < 0 || row >= rows || col < 0 || col >= cols) {
                        continue;
                    }

                    // This is the case where the neighbor is part of a wall
                    if (maze[row][col] == '+' || isVisited[row][col] == true) {
                        continue;
                    }

                    // This is the case where the neighbor is valid, and the visiting history
                    // and path history array are updated.
                    if (j == 0) {
                        path[row][col] = 'D';
                    } else if (j == 1) {
                        path[row][col] = 'U';
                    } else if (j == 2) {
                        path[row][col] = 'R';
                    } else if (j == 3) {
                        path[row][col] = 'L';
                    }
                    isVisited[row][col] = true;

                    // If the valid neighbor is on the border, it means that an exit is found.
                    // Note that this condition must be placed here since only valid neighbor
                    // on the border can be counted as exit.
                    if (row == 0 || row == rows - 1 || col == 0 || col == cols - 1) {
                        // Creates path information and returns it.
                        PathInfo pathInfo = new PathInfo(entrance, new int[]{row, col}, path);
                        return pathInfo;
                    }

                    queue.offer(new int[]{row, col});
                }
            }
        }

        return null;
    }

    /**
     * The main method for simple testing of the program.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        char maze[][] = {
            {'+', '+', '.', '+', '+', '+', '+'},
            {'+', '.', '.', '.', '.', '.', '.'},
            {'+', '.', '.', '.', '.', '.', '+'},
            {'+', '.', '.', '.', '.', '.', '+'},
            {'+', '+', '+', '+', '+', '+', '+'}};
        int[] entrance = new int[]{3, 1};
        PathInfo pathInfo = Solution.nearestExit(maze, entrance);
        List<int[]> findPath = pathInfo.findPath();
        System.out.println("Path: ");
        for (int[] cur : findPath) {
            System.out.print(cur[0] + " ");
            System.out.println(cur[1]);
        }
    }
}

/**
 * A class that helps find the path from the exit to the entrance in a maze.
 */
class PathInfo {

    int[] entrance;
    int[] exit;
    char[][] path;

    public PathInfo(int[] entrance, int[] exit, char[][] path) {
        this.entrance = entrance;
        this.exit = exit;
        this.path = path;
    }

    public List<int[]> findPath() {
        // Note: do not create an int[] cur = exit and manipulate the
        // path points via updating cur. This is because int[] data type
        // is passed by reference and the change to cur will affect the result.
        int row = exit[0];
        int col = exit[1];

        List<int[]> list = new ArrayList<>();
        list.add(new int[]{row, col});
        // The loop ends when the point is the entrance
        while (!(entrance[0] == row && entrance[1] == col)) {
            if (path[row][col] == 'U') {
                row--;
            } else if (path[row][col] == 'D') {
                row++;
            } else if (path[row][col] == 'L') {
                col--;
            } else if (path[row][col] == 'R') {
                col++;
            }

            // The final row, col is the pair that hits the entrance
            list.add(new int[]{row, col});
        }

        return list;
    }
}
