/**
 * The class which holds the pictures saved by the user
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Gallery extends JPanel {
    private Container main = new Container();
    private int numberOfImages;
    private JLabel photography = new JLabel();
    private ButtonGroup buttons = new ButtonGroup();
    private JLabel photographLabel = new JLabel();
    private JToolBar buttonBar = new JToolBar();
    private JToggleButton tempButton = new JToggleButton();

/**
 * Constructor setting the layout of the panel
 */
    public Gallery()
    {
        numberOfImages = 0;

        main.setLayout(new FlowLayout());


    }

    /**
     * Take the image passed from the Control Panel and see if it can be added to the list 
     * of saved images.If there are 12 images already, override the first image
     * For every new created component which holds an image, set an action listener that 
     * will check when the component is clicked
     */
    public void addImage(BufferedImage newImage)
    {
        if(numberOfImages > 11)
        {
            numberOfImages = 0;

        }

        writeImage(newImage,numberOfImages);

        JToggleButton newButtonAdded = new JToggleButton();
        this.setImage(newButtonAdded, newImage);
        newButtonAdded.addActionListener(e->
        {
            tempButton = newButtonAdded;
        });
        buttons.add(newButtonAdded);


        main.add(newButtonAdded,numberOfImages);
        main.revalidate();
        main.repaint();
        numberOfImages++;


    }

    /**
     * Remove the component with the picture that is clicked by the user and redisplay all the 
     * avaiable images
     */
    public void removeImage()
    {
        if(tempButton != null)
        {
            main.remove(tempButton);
            main.revalidate();
            main.repaint();
        }

    }


    /**
     * Attach the image passed to the component passed
     * The image attached is scaled down and the color of the background of
     * the component is set to white.
     */
    public void setImage(JToggleButton buttonPassed,BufferedImage imageOnTop)
    {
        try
        {

            Image scaledDownImage = imageOnTop.getScaledInstance(100,100, Image.SCALE_DEFAULT);
            buttonPassed.setBackground(Color.WHITE);
            buttonPassed.setIcon(new ImageIcon(scaledDownImage));
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

/**
 * Take the image passed and write it to a file on the filesystem of the user.
 * Get the path of the Main class and find the folder named "userImages" where all
 * the images will be written.
 * The name of the newly created file is the number of the image in the gallery.
 */
    private void writeImage(BufferedImage newImage,int index)
    {
        String nameFoler = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String nameFile = index + ".png";
        String path = nameFoler + "/userImages/" + nameFile;

        try {
            ImageIO.write(newImage, "PNG", new File(path));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


/**
 * Return to the Control Panel, the container that holds all the components of the Gallery
 */
    public Container returnMain()
    {
        return main;
    }





}
