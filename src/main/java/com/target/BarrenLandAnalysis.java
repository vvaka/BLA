package com.target;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by vvaka on 6/5/16.
 * <p>
 * This Class provides implementation to analyze land area for Fertile land provided , barren land areas as set of rectangle formats
 * refer to READEME.md for complete problem definition
 */
public class BarrenLandAnalysis {

    // 2 dimensional array to represent the whole land area
    private int[][] matrix;

    private int ROWS;
    private int COLS;
    private List<int[]> barrenLandRectangles;


    /**
     * Class to hold the co-ordinates
     */
    private class Tuple {

        private int x;
        private int y;

        Tuple(int x, int y) {
            //(x,y) are start's at 0 , making sure x,y will not overflow
            assert (x >= 0 && x < ROWS) && y >= 0 && y < COLS : String.format("(%s,%s) value should not be greater than (%s,%s) ", x, y, ROWS - 1, COLS - 1);
            this.x = x;
            this.y = y;
        }

        boolean canFormValidRectangle(Tuple t2) {
            assert (t2.x - this.x >= 1 && t2.y - this.y >= 1) : String.format("Can not form a rectangle with %s, %s", this, t2);
            return true;
        }

        @Override
        public String toString() {
            return "(" + "x=" + x + ", y=" + y + ')';
        }
    }


    /**
     * Private Constructor to used in the public constructor BrandAnalysis(rows,cols,barrenLandRectanglesInputString)
     * <p>
     * This method prepares the matrix to represent the total land area and fill it all with 1's to signify normal land
     *
     * @param rows number of total rows in the land area
     * @param cols number of total columns in the land area
     */

    private BarrenLandAnalysis(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.matrix = new int[rows][cols];
        this.barrenLandRectangles = new ArrayList<>();
        //fill in all the land with 1's
        for (int[] row : matrix) {
            Arrays.fill(row, 1);
        }
    }

    /**
     * Constructor to create BrandAnalysis
     *
     * @param rows                            number of total rows in the land area
     * @param cols                            number of total columns in the land area
     * @param barrenLandRectanglesInputString set of rectangles to represent barren land areas
     * @throws AssertionError when the inputString can't form a valid rectangle or co-ordinates are out of bounds
     */
    BarrenLandAnalysis(int rows, int cols, String barrenLandRectanglesInputString) {

        this(rows, cols);
        this.processInputString(barrenLandRectanglesInputString);
        if (barrenLandRectangles != null)
            //mark barren lands with 1's
            for (int[] barrenLand : barrenLandRectangles) {
                Tuple t1 = new Tuple(barrenLand[0], barrenLand[1]);
                Tuple t2 = new Tuple(barrenLand[2], barrenLand[3]);

                t1.canFormValidRectangle(t2);

                markBarrenLand(t1, t2);
            }
    }

    /**
     * This method marks the BarrenLand area with 0's in the matrix with co ordinates specified in t1,t2
     *
     * @param t1 lower left corner of the rectangle
     * @param t2 top right corner of the rectangle
     */
    private void markBarrenLand(Tuple t1, Tuple t2) {
        //indices starts at 0
        //(x1,y1) , (x2,y2)
        for (int i = t1.x; i <= t2.x; i++) {
            for (int j = t1.y; j <= t2.y; j++)
                this.matrix[i][j] = 0;
        }
    }

    /**
     * This method navigates through each co-ordinate of the land given represented in this.matrix
     * and count the fertile land area excluding the parts of barren land rectangles marked by 0
     *
     * @implNote We are using BFS to exploration of all the nodes with 1's and mark them  as visited by adding fertile land marker i.e >1 to further exploration.
     */

    public List<Integer> findFertileLand() {

        Map<Integer, Integer> landAreas = new HashMap<>();
        Deque<Tuple> queue = new ArrayDeque<>();

        int fertileLandRegion = 1;
        int i = 0, j = 0;
        while (i < ROWS && j < COLS) {

            if (!queue.isEmpty()) {
                Tuple t = queue.poll();
                if (this.matrix[t.x][t.y] == 1) {
                    // mark the node visited
                    this.matrix[t.x][t.y] = fertileLandRegion;
                    //add the current Node to the area count
                    landAreas.put(fertileLandRegion, landAreas.get(fertileLandRegion) + 1);

                    //add the 4 nodes surrounding to the queue for exploration
                    if (t.x < ROWS - 1)
                        queue.offer(new Tuple(t.x + 1, t.y));
                    if (t.x > 0)
                        queue.push(new Tuple(t.x - 1, t.y));
                    if (t.y < COLS - 1)
                        queue.offer(new Tuple(t.x, t.y + 1));
                    if (t.y > 0)
                        queue.offer(new Tuple(t.x, t.y - 1));
                }
            } else {
                //Queue is empty , initialize a new Land region
                if (this.matrix[i][j] == 1) {
                    queue.offer(new Tuple(i, j));
                    fertileLandRegion++;
                    landAreas.put(fertileLandRegion, 0);
                }
                if (j == COLS - 1) {
                    i++;
                    j = 0;
                } else {
                    j++;
                }
            }
        }
        //returning the area's only in sorted order
        return landAreas.values().stream().sorted().collect(Collectors.toList());
    }


    void printGrid() {
        for (int[] aMatrix : matrix) {
            for (int j = 0; j < matrix[0].length; j++)
                System.out.printf("%2d ", aMatrix[j]);
            System.out.print("\n");
        }
    }


    /**
     * This method processes the inputString and parses coverts them to (x,y) co-ordinates to form Tuple objects
     * Set of barren land rectangles are defined in a string, which consists of four integers separated by single spaces,
     * with no additional spaces in the string. The first two integers are the coordinates of the bottom left corner in the given rectangle,
     * and the last two integers are the coordinates of the top right corner.
     *
     * @param barrenLandRectanglesInputString holds the input string
     */

    private void processInputString(String barrenLandRectanglesInputString) {
        if (barrenLandRectanglesInputString == null || barrenLandRectanglesInputString.length() == 0)
            return;
        // remove { } from input
        barrenLandRectanglesInputString = barrenLandRectanglesInputString.replaceAll("\\{|\\}", "");
        //split string at , and extra white space around , are ignored
        String[] rects = barrenLandRectanglesInputString.split("\\s*,\\s*");

        for (String r : rects) {
            //replace quotes
            r = r.replaceAll("\"|“|”", "");
            //split each rectangle at space
            String coordinates[] = r.split(" ");
            if (coordinates.length == 4) {
                this.barrenLandRectangles.add(new int[]{
                        Integer.valueOf(coordinates[0]),
                        Integer.valueOf(coordinates[1]),
                        Integer.valueOf(coordinates[2]),
                        Integer.valueOf(coordinates[3])
                });
            }
        }
    }


    public static void main(String[] args) {
        System.out.println(getInstanceFromStdIn().findFertileLand());
    }

    public static BarrenLandAnalysis getInstanceFromStdIn() {
        System.out.println("Enter coordinates for barren land rectangles ");
        try (Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine();
            return new BarrenLandAnalysis(400, 600, input);

        }
    }
}
