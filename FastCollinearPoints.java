/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LS[] lineSegments;
    private int nOfSegements = 0;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    if (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }
        Arrays.sort(points);

        lineSegments = new LS[points.length];

        for (int i = 0; i < points.length; i++) {
            Point[] sortedPoints = new Point[points.length - 1];
            for (int k = 1; k < points.length; k++) {
                sortedPoints[k - 1] = points[k];
            }
            if (i != 0) {
                sortedPoints[i - 1] = points[0];
            }

            Arrays.sort(sortedPoints, points[i].slopeOrder());

            int count = 1;
            for (int j = 1; j < sortedPoints.length; j++) {
                if (points[i].slopeTo(sortedPoints[j - 1]) == points[i].slopeTo(sortedPoints[j])) {
                    ++count;
                }
                else {
                    if (count >= 3) {
                        boolean add = true;
                        LS segment = new LS(points[i], sortedPoints[j - 1]);
                        // segment.draw();
                        for (int k = 0; k < nOfSegements; k++) {
                            if (lineSegments[k].slope(points[i]) == segment.slope(points[i])
                                    && lineSegments[k].slope(points[i], "other point") == segment
                                    .slope(points[i])) {
                                add = false;
                            }
                        }

                        if (add) {
                            lineSegments[nOfSegements++] = segment;
                        }
                    }
                    count = 1;
                }
            }
            if (count >= 3) {
                boolean add = true;
                LS segment = new LS(points[i],
                                    sortedPoints[sortedPoints.length - 1]);
                // segment.draw();
                for (int k = 0; k < nOfSegements; k++) {
                    if (lineSegments[k].slope(points[i]) == segment.slope(points[i])
                            && lineSegments[k].slope(points[i], "other point")
                            == segment.slope(points[i])) {
                        add = false;
                    }
                }

                if (add) {
                    lineSegments[nOfSegements++] = segment;
                }
            }
        }
    }     // finds all line segments containing 4 or more points

    public int numberOfSegments() {
        return nOfSegements;
    }       // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[nOfSegements];
        for (int k = 0; k < nOfSegements; k++) {
            segments[k] = new LineSegment(lineSegments[k].p, lineSegments[k].q);
        }
        return segments;
    }               // the line segments

    private static class LS {
        private final Point p;   // one endpoint of this line segment
        private final Point q;   // the other endpoint of this line segment

        /**
         * Initializes a new line segment.
         *
         * @param p one endpoint
         * @param q the other endpoint
         * @throws NullPointerException if either <tt>p</tt> or <tt>q</tt>
         *                              is <tt>null</tt>
         */
        public LS(Point p, Point q) {
            if (p == null || q == null) {
                throw new NullPointerException("argument is null");
            }
            this.p = p;
            this.q = q;
        }

        public double slope(Point point) {
            if (point.slopeTo(q) == Double.NEGATIVE_INFINITY) {
                return point.slopeTo(p);
            }
            return point.slopeTo(q);
        }

        public double slope(Point point, String s) {
            if (point.slopeTo(p) == Double.NEGATIVE_INFINITY) {
                return point.slopeTo(q);
            }
            return point.slopeTo(p);
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        // In in = new In("input56.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
