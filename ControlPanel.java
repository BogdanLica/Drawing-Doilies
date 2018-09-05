/**
 * The panel which controls all settings and the gallery
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanel extends JPanel {
    private Container main = new Container();

    private Container pen = new Container();
    private Container states = new Container();
    private Container toggles = new Container();
    private Container togglesReflected = new Container();
    private Container togglesLines = new Container();
    private Container galleryControls = new Container();

    private int penSize;
    private Color penColor;

    private JSlider sectorNumber;
    private JLabel reflected;
    private JLabel linesShow;
    private JRadioButton reflectedYes;
    private JRadioButton reflectedNo;
    private ButtonGroup radioGroupReflection;

    private ButtonGroup radioGroupLines;
    private JRadioButton linesYes;
    private JRadioButton linesNo;

    private boolean sectorToggle;
    private boolean reflectedToggle;


    private enum mode {DRAWING,ERASING};
    private String currentMode;

    private JButton undo;
    private JButton redo;
    private JButton clear;

    private JButton save;
    private JButton remove;


    private JButton chooseColor;

    private Display myDisplay;
    private Gallery myGallery;

    private JLabel title;

    private JColorChooser penColorPallet;
    private JSpinner spinner;


    private static final int MIN_SECTORS = 1;
    private static final int MAX_SECTORS = 72;
    private static final int INIT_SECTORS = 6;


    private JLabel sizePen;

    private JButton erase;
    private JButton draw;
    private boolean erasing;

    private Container penSizeAndLabel;


/**
 * Create the layout of the the containers and initialise the components of 
 * each container
 */
    public ControlPanel(Display newDisplay,Gallery newGallery)
    {
        this.myDisplay = newDisplay;
        this.myGallery = newGallery;
        this.radioGroupReflection = new ButtonGroup();
        this.radioGroupLines = new ButtonGroup();
        //this.myGallery = newGallery;

        this.title = new JLabel("Sector Number:");
        this.sectorNumber = new JSlider(JSlider.HORIZONTAL,MIN_SECTORS,MAX_SECTORS,INIT_SECTORS);
        this.sectorNumber.setMajorTickSpacing(10);
        this.sectorNumber.setMinorTickSpacing(1);
        this.sectorNumber.setPaintTicks(true);
        this.sectorNumber.setPaintLabels(true);


        this.reflected = new JLabel("Reflect");
        this.reflectedYes = new JRadioButton("Yes");
        this.reflectedNo = new JRadioButton("No");
        reflectedNo.setSelected(true);
        radioGroupReflection.add(reflectedYes);
        radioGroupReflection.add(reflectedNo);

        this.linesShow = new JLabel("Toggle Lines");
        this.sectorToggle = false;
        this.linesYes = new JRadioButton("Yes");
        this.linesNo = new JRadioButton("No");
        linesNo.setSelected(true);
        radioGroupLines.add(linesYes);
        radioGroupLines.add(linesNo);

        togglesReflected.setLayout(new BoxLayout(togglesReflected,BoxLayout.Y_AXIS));
        togglesReflected.add(reflected);
        togglesReflected.add(reflectedYes);
        togglesReflected.add(reflectedNo);

        togglesLines.setLayout(new BoxLayout(togglesLines,BoxLayout.Y_AXIS));
        togglesLines.add(linesShow);
        togglesLines.add(linesYes);
        togglesLines.add(linesNo);


        toggles.setLayout(new BoxLayout(toggles,BoxLayout.X_AXIS));
        toggles.add(togglesReflected);
        toggles.add(Box.createRigidArea(new Dimension(250,0)));
        toggles.add(togglesLines);


        this.currentMode = mode.DRAWING.name();


        this.undo = new JButton("Undo");
        this.setImage(undo,"undo.png");
        this.redo = new JButton("Redo");
        this.setImage(redo,"redo.png");
        this.clear = new JButton("Clear");
        this.setImage(clear,"clear.png");



        this.save = new JButton("Save");
        this.setImage(save,"save.png");
        this.remove = new JButton("Remove");
        this.setImage(remove,"delete.png");

        chooseColor = new JButton("Choose a color");
        this.setImage(chooseColor,"color_picker.png");
        penColorPallet = new JColorChooser();




        penSizeAndLabel = new Container();
        penSizeAndLabel.setLayout(new BoxLayout(penSizeAndLabel,BoxLayout.PAGE_AXIS));

        sizePen = new JLabel("Size");
        SpinnerNumberModel model = new SpinnerNumberModel(10,5,80,5);
        spinner = new JSpinner();
        Dimension size = spinner.getPreferredSize();
        spinner.setModel(model);
        penSizeAndLabel.add(sizePen);
        sizePen.setAlignmentX(Component.CENTER_ALIGNMENT);
        penSizeAndLabel.add(spinner);
        spinner.setAlignmentX(Component.CENTER_ALIGNMENT);


        draw = new JButton("Draw");
        this.setImage(draw,"pencil.png");
        erase=new JButton("Erase");
        this.setImage(erase,"eraser.png");




    }


/**
 * Set the appropriate image for the appropriate button by finding the image based on the
 * location of the folder "images", scaling it down and setting the background of the 
 * component to white
 */
    public void setImage(JButton buttonPassed,String path)
    {
        String fullPath = "images/" + path;
        try
        {
            Image img = ImageIO.read(getClass().getResource(fullPath));
            Image scaledDownImage = img.getScaledInstance(50,50, Image.SCALE_SMOOTH);
            buttonPassed.setBackground(Color.WHITE);
            buttonPassed.setIcon(new ImageIcon(scaledDownImage));
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }



/**
 * Create the necessary listeners for the components in the container
 */
    public void addListeners()
    {

        save.addActionListener(e->
        {

            myGallery.addImage(myDisplay.getImage());

        });



        remove.addActionListener(e->
        {
            myGallery.removeImage();
        });

        erase.addActionListener(e->{
            penColor=myDisplay.getBackground();
            penSize= (int) (1.5*penSize);
            myDisplay.setPenColor(penColor);
            erasing=true;
            myDisplay.repaint();
        });

        draw.addActionListener(e->{
            penColor = penColorPallet.showDialog(main,"Choose a color",Color.CYAN);
            penSize=(int)spinner.getValue();
            myDisplay.setPenColor(penColor);
            erasing=false;
        });

        undo.addActionListener(e ->{
            myDisplay.undo();
            myDisplay.repaint();
        });

        redo.addActionListener(e ->{
            myDisplay.redo();
            myDisplay.repaint();
        });


        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSpinner a = (JSpinner) e.getSource();
                penSize = (int)a.getValue();
                myDisplay.setPenSize(penSize);
            }
        });




        chooseColor.addActionListener(e->{

           penColor = penColorPallet.showDialog(main,"Choose a color",Color.CYAN);
            myDisplay.setPenColor(penColor);
            myDisplay.repaint();

        });



        sectorNumber.addChangeListener(e -> {
            myDisplay.setSectorNumber(sectorNumber.getValue());
            myDisplay.repaint();
        });

        myDisplay.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                myDisplay.createListRotated();
                if(reflectedToggle)
                {
                    myDisplay.createListReflected();
                }
                myDisplay.setPenColor(penColor);
                myDisplay.repaint();


            }

            @Override
            public void mouseReleased(MouseEvent e) {

                myDisplay.setPenColor(penColor);
                myDisplay.addPoints(true);
                myDisplay.setDraggingActive(0);
                myDisplay.repaint();
            }
        });

        myDisplay.addMouseMotionListener(new MouseAdapter() {



            @Override
            public void mouseDragged(MouseEvent e) {

                myDisplay.addPointRotated(e.getX(),e.getY(),penSize,penColor);
                if(reflectedToggle)
                {
                    myDisplay.addPointReflected(myDisplay.getWidth() - e.getX(),e.getY(),penSize,penColor);

                }
                myDisplay.setDraggingActive(1);
                myDisplay.setPenColor(penColor);
                myDisplay.repaint();



            }


        });



        reflectedYes.addActionListener(e -> {
            myDisplay.switchReflected(true);
            reflectedToggle = true;
            myDisplay.repaint();

        });

        reflectedNo.addActionListener(e -> {
            myDisplay.switchReflected(false);
            sectorToggle = false;
            myDisplay.repaint();

        });

        linesYes.addActionListener(e -> {
            myDisplay.switchLines(true);
            sectorToggle = true;
            myDisplay.repaint();

        });

        linesNo.addActionListener(e -> {
            myDisplay.switchLines(false);
            sectorToggle = false;
            myDisplay.repaint();

        });



        clear.addActionListener(e -> {
            myDisplay.clear();
            myDisplay.repaint();
        });


    }

/**
 * Create the layout of each container which is part of the main component
 */
    public void setLayout()
    {

        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
        main.add(title);
        main.add(sectorNumber);
        main.add(Box.createRigidArea(new Dimension(0,40)));
        main.add(toggles);
        main.add(Box.createRigidArea(new Dimension(0,40)));
        pen.setLayout(new FlowLayout());
        pen.add(chooseColor);
        pen.add(Box.createRigidArea(new Dimension(20,0)));
        pen.add(draw);
        pen.add(Box.createRigidArea(new Dimension(20,0)));
        pen.add(erase);
        pen.add(Box.createRigidArea(new Dimension(20,0)));
        pen.add(penSizeAndLabel);
        pen.add(Box.createRigidArea(new Dimension(20,0)));
        main.add(pen);





        this.states.setLayout(new FlowLayout());
        states.add(undo);
        states.add(Box.createRigidArea(new Dimension(40,0)));
        states.add(redo);
        states.add(Box.createRigidArea(new Dimension(40,0)));
        states.add(clear);





        main.add(states);
        this.galleryControls.setLayout(new FlowLayout());
        galleryControls.add(save);
        galleryControls.add(Box.createRigidArea(new Dimension(30,0)));
        galleryControls.add(remove);
        main.add(galleryControls);
        main.add(Box.createVerticalGlue());
        main.add(myGallery.returnMain());
      

    }

    /**
     * Return the container which hass all the components to the frame
     */
    public Container returnCont()
    {
        return main;
    }

}
