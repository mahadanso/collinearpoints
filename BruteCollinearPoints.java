/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments;
    private int nOfSegements = 0;

    public BruteCollinearPoints(Point[] points) {
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

        lineSegments = new LineSegment[points.length];

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        double slope = points[i].slopeTo(points[j]);
                        if (slope == points[i].slopeTo(points[k])
                                && points[i].slopeTo(points[m]) == slope) {
                            lineSegments[nOfSegements++] = new LineSegment(points[i], points[m]);
                        }
                    }
                }
            }
        }

    }   // finds all line segments containing 4 points

    public int numberOfSegments() {
        return nOfSegements;
    }     // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[nOfSegements];
        for (int k = 0; k < nOfSegements; k++) {
            segments[k] = lineSegments[k];
        }
        return segments;
    }

    public static void main(String[] args) {
        Point[] points = new Point[] { new Point(1, 2), null };
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);


        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        // Point[] points = new Point[n];
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
        // BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
