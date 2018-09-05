/**
 * Each point drawn by the user uses this class,an extension of the Point2d.Double class
 */

import java.awt.*;
import java.awt.geom.Point2D;

public class CustomPoint extends Point2D.Double {
    private Color color;
    private int size;
    /**
     * Contructor initialising the coordinates of the point,
     * the color of the point and the size of the point
     */
    public CustomPoint(double xNew,double yNew,Color nColor,int nSize)
    {
        super(xNew,yNew);
        this.color=nColor;
        this.size=nSize;
    }


    /**
     * Change the color of the point with the new value
     */
    public void setColorDraw(Color newC)
    {
        this.color=newC;
    }

    /**
     * Change the size of the point with the new value
     */
    public void setSizeDraw(int newS)
    {
        this.size = newS;
    }

    /**
     * Return the size of the point
     */
    public int getSizeDraw()
    {
        return this.size;
    }

    /**
     * Return the color of the point
     */
    public Color getColorDraw()
    {
        return this.color;
    }
}
