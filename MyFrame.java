/**
 * The custom frame which holds all the components
 */

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    private Container main;


    private ControlPanel myC;
    private Display myd;
    private Gallery myG;

    /**
     * Contructor initialising the size of the pen and the color
     * of the pen.
     * The layout of the whole frame is set.
     * A display and a control panel is created.
     */
    public MyFrame(int sectors)
    {
        main = this.getContentPane();
        main.setLayout(new GridLayout(1,2));


        myd = new Display(sectors);
        myd.setPenSize(10);
        myd.setPenColor(Color.WHITE);
        myG = new Gallery();
        myC = new ControlPanel(myd,myG);


    }


/**
 * The display and control panel are added to the frame and
 * the listeners of the control panel are initialised.
 */
    public void setPanels()
    {
        myC.setLayout();
        myd.setLayout(new GridLayout(1,1));

        main.add(myC.returnCont());
        main.add(myd);
        myC.addListeners();

        //this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
