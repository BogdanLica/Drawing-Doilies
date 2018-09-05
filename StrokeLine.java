/**
 * The interface that it used to create the line connecting 2 points specified
 */


import java.awt.*;
import java.awt.geom.Line2D;


public class StrokeLine {
    private int thickness;
    private Color color;
    private double x1;
    private double y1;
    private double x2;
    private double y2;


/**
 * The coordinates of the first point passed are stored
 */
    public void addFirstPoint(double x1,double y1)
    {
        this.x1=x1;
        this.y1=y1;
    }


/**
 * The coordinates of the second point passed are stored
 */
    public void addLastPoint(double x2,double y2)
    {
        this.x2=x2;
        this.y2=y2;
    }


/**
 * The thickness passed is stored
 */
    public void setThickness(int thickness)
    {
        this.thickness=thickness;

    }


/**
 * The color passed is stored
 */
    public void setColor(Color color)
    {
        this.color=color;

    }



/**
 * A line is created using the coordinates of the 2 points stored
 * To make the line to have different sizes,a stroke is used based on the value of the thickness
 * After setting the stroke, the color of the line is used to change the color of the stroke and
 * the final shape is drawn on the screen.
 */
    public void drawLine(Graphics2D g)
    {

        g.setStroke(new BasicStroke(thickness/5));

        Line2D shape = new Line2D.Double(x1, y1, x2, y2);
        g.draw(shape);
        g.setColor(color);
        g.fill(shape);

    }
}
