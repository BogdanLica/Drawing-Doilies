/**
 * The panel where all the drawing happens
 */
import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;


public class Display extends JPanel {
    private Stack<List<CustomPoint>> myRotatedPoints = new Stack<>();
    private Stack<List<CustomPoint>> deletedRotatedPoints = new Stack<>();
    private List<CustomPoint> tempRotated;
    private List<CustomPoint> tempReflected;
    private Stack<List<CustomPoint>> myReflectedPoints = new Stack<>();
    private Stack<List<CustomPoint>> deletedReflectedPoints = new Stack<>();

    private Stack<Integer> orderAdded = new Stack<>();
    private Stack<Integer> deletedOrderAdded = new Stack<>();

    private Stack<Integer> settingsAdded = new Stack<>();
    private Stack<Integer> deletedSettingsAdded = new Stack<>();


    private static final int ROTATE=1;
    private static final int REFLECT = 2;

 
    private int reflectedListPrevSize;
    private int draggingActive = 0;
    private Color penColor = Color.WHITE;
    private int penSize = 10;
    private int sectorNumber;
    private double angle;
    private boolean reflect;
    private boolean lines;

    /**
     * Constructor initialising the number of sectors and setting
     * the color of the background of the panel.
     */
    public Display(int sectorNumber)
    {
        this.setSectorNumber(sectorNumber);
        setBackground(Color.BLACK);


    }


    /**
     * A BufferedImage object is created from the current state of the graphics objects
     * which displays all the drawings made by the user
     */
    public BufferedImage getImage()
    {
        int w = this.getWidth();
        int h = this.getHeight();
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        this.print(g);

        return img;
    }

/**
 * Change the color of the pen used to draw
 */
    public void setPenColor(Color newColor)
    {
        this.penColor=newColor;
    }

/**
 * Change the size of the pen used to draw
 */
    public void setPenSize(int newSize)
    {
        this.penSize = newSize;
    }


/**
 * Change the state check if the mouse is dragged
 */
    public void setDraggingActive(int a)
    {
        draggingActive = a;
    }


/**
 * Create a list where the rotated points drawn by the user are saved
 */
    public void createListRotated()
    {
        tempRotated = new ArrayList<>();
    }


/**
 * Create a list where the reflected points drawn by the user are saved
 */
    public void createListReflected()
    {
        tempReflected = new ArrayList<>();
    }


/**
 * Add the points drawn by the used to the appropriate list based 
 * on the reflection components and save the order of the list of points
 */
    public void addPoints(boolean flag)
    {

        myRotatedPoints.push(tempRotated);
        if(reflect)
        {
            myReflectedPoints.push(tempReflected);
            orderAdded.push(REFLECT);
        }
        else
        {
            orderAdded.push(ROTATE);
        }

        clearTemp(flag);
    }

/**
 * Remove the last list(all the points since the user presses the mouse until the user releases
 * the mouse) of points from the complete list of points.
 * Delete the order of the list and save it in a different list to be able to recover it
 * in a further call of the method
 */
    public void undo()
    {

        if(!orderAdded.isEmpty())
        {
            int status = orderAdded.pop();

            if(status == REFLECT)
            {
                deletedReflectedPoints.push(myReflectedPoints.pop());
                reflectedListPrevSize--;

                deletedOrderAdded.push(REFLECT);

            }
            else
            {
                deletedOrderAdded.push(ROTATE);
            }
                deletedRotatedPoints.push(myRotatedPoints.pop());

        }



    }


/**
 * Add the last deletead list(all the points since the user presses the mouse until the user releases
 * the mouse) of points to the complete list of points.
 * Save the order of the list to be able to recover it in a further call of the method
 */

    public void redo()
    {
        if(!deletedOrderAdded.isEmpty())
        {
            int status = deletedOrderAdded.pop();

            if(status == REFLECT)
            {
                myReflectedPoints.push(deletedReflectedPoints.pop());
                reflectedListPrevSize++;
                orderAdded.push(REFLECT);
            }
            else
            {
                orderAdded.push(ROTATE);
            }
            myRotatedPoints.push(deletedRotatedPoints.pop());
        }

    }



/**
 * Change the state of reflect based on the component Reflect of the Frame
 */
    public void switchReflected(boolean flag)
    {
        this.reflect=flag;


    }

/**
 * Change the state of toggle lines based on the component Toggle of the Frame
 */
    public void switchLines(boolean flag)
    {
        this.lines=flag;
    }



    /**
    * Draw all the points drawn by the user so far,reflected or no, draw the current points
    * that are being drawn by the user and draw the sector lines 
    */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;




        drawRotated(graphics);

        if(reflect || reflectedListPrevSize == myReflectedPoints.size())
        {
            drawReflected(graphics);
        }


        // draw where the mouse currently is
        if(draggingActive == 1)
        {
            draw(graphics, tempRotated);

            if(reflect)
            {
                draw(graphics,tempReflected);
            }
        }



        if(lines)
        {
            drawLines(graphics);
        }



    }

    /**
    * Draw the sector lines by creating a line and rotating it around the
    centre of the canvas 
    */
    private void drawLines(Graphics2D g)
    {
        g.setColor(Color.ORANGE);
        for(int s=1;s<=sectorNumber;s++) {
            g.setStroke(new BasicStroke(5F));
            g.drawLine(getWidth()/2,20,getWidth()/2,getHeight()/2);
            g.rotate(angle,getWidth()/2,getHeight()/2);
        }

        g.setColor(penColor);

    }

    /**
    * Draw all the rotated points
    */
    private void drawRotated(Graphics2D g)
    {

        linesBetweenPoints(g,myRotatedPoints);

    }

    /**
    * Draw all the reflected points
    */
    private void drawReflected(Graphics2D g)
    {

        linesBetweenPoints(g,myReflectedPoints);
        reflectedListPrevSize = myReflectedPoints.size();



    }

    /**
     * Clear all the current points currently drawn
     */
    public void clearTemp(boolean flag)
    {
        if(flag)
        {
            tempRotated=null;
            tempReflected=null;
        }
    }

    /**
     * Given a list of lists of points,draw each of the lists
     */
    private void linesBetweenPoints(Graphics2D g, Stack<List<CustomPoint>> list)
    {

        for(List<CustomPoint> eachList:list)
        {
            draw(g,eachList);
        }
    }

    /**
     * Given a list of points, create a line between 2 adjacent points and rotate each
     * of those newly created lines 
     */
    private void draw(Graphics2D g,List<CustomPoint> list)
    {
        for(int i=0;i<list.size()-1;i++)
        {
            StrokeLine currentLine = new StrokeLine();
            CustomPoint first = list.get(i);
            CustomPoint second = list.get(i+1);

            currentLine.addFirstPoint(first.getX(),first.getY());
            currentLine.addLastPoint(second.getX(),second.getY());
            currentLine.setThickness(first.getSizeDraw());
            currentLine.setColor(first.getColorDraw());
            for(int s=1;s<=sectorNumber;s++) {
                currentLine.drawLine(g);
                g.rotate(this.angle, this.getWidth()/2, this.getHeight()/2);
            }

        }
    }

/**
 * Delete all the points drawn by the user so far
 */
    public void clear()
    {
        myReflectedPoints.clear();
        myRotatedPoints.clear();
    }

/**
 * Based on the components of the point drawn by the user,
 * create a custom point with the specified attibutes and add it to
 * the appropriate list of points
 */
    protected void addPointRotated(double xNew,double yNew,int size,Color color)
    {
        if((myRotatedPoints!=null) || !myRotatedPoints.isEmpty()) {
            tempRotated.add(new CustomPoint(xNew, yNew,color,size));
        }


    }

/**
 * Based on the components of the point drawn by the user,
 * create a custom point with the specified attibutes and add it to
 * the appropriate list of points
 */
    protected void addPointReflected(double xNew,double yNew,int size,Color color)
    {
        if((myReflectedPoints!=null) || !myReflectedPoints.isEmpty()) {
            tempReflected.add(new CustomPoint(xNew, yNew,color,size));
        }
    }

/**
 * Change the number of sector lines
 */
    public void setSectorNumber(int sector)
    {
        this.sectorNumber = sector;
        this.calculateAngle();
    }

/**
 * Compute the angle between each point based on the number of sector lines
 */
    private void calculateAngle()
    {
        this.angle = Math.toRadians((float)360/this.sectorNumber);
    }


}
