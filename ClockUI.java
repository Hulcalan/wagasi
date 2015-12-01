import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ClockUI extends TimeClient{



   public ClockUI(Integer value) {
      EventQueue.invokeLater(
            new Runnable() {
               @Override
               public void run() {
                    JFrame frame = new JFrame("Test");
               
                              frame.setUndecorated(true);
                  frame.setLayout(new FlowLayout());
                  
                 
               //makes frame translusent
                  frame.setBackground(new Color(0,0,0,0));
                
                  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                  frame.setLayout(new BorderLayout());
                  
                  frame.add(new ClockPane(value));
                  frame.setAlwaysOnTop( false );
                  frame.pack();
                  frame.setLocationRelativeTo(null);
                  frame.setVisible(true);
                  frame.toFront();
                  frame.repaint();
                 
               }
            });
   }

   public class ClockPane extends JLayeredPane {
   
      private BufferedImage circle;
      private BufferedImage indicator;
      private double angle = 0;
      private JButton close;
      private BufferedImage images;
      private BufferedImage imagec;
      private JButton set;
      private JLabel world = new JLabel();
      
      
      public ClockPane(Integer value) {
         try {
            circle = ImageIO.read(new File("./images/night.png"));
            indicator = ImageIO.read(new File("./images/LT.png"));
            images = ImageIO.read(new File("./images/x.png"));
            imagec = ImageIO.read(new File("./images/settings.png"));
            ImageIcon myImage = new ImageIcon(images);
            set = new JButton(myImage);
            myImage = new ImageIcon(imagec);
            close = new JButton(myImage);
         } 
         catch (IOException exp) {
            exp.printStackTrace();
         }
                        
        // ImageIcon ii = new ImageIcon(this.getClass().getResource("./images/day.gif"));
         //world.setIcon(ii);
         //world.setBounds(320,320,268,250);
        // add(world, 0);
               
               // this sets up the custom red x we put in the UI
               //
               //
               //
         set.setBounds(620,665,55,55);
         set.setOpaque(false);
         set.setContentAreaFilled(false);
         set.setBorderPainted(false);
         set.addActionListener(
               new ActionListener(){
                  @Override
                           public void actionPerformed(ActionEvent e){
                     System.exit(0);
                  }
               });
               
         add(set,0);
               
               
               // this sets up the custom menu button
               //
               //
         JPopupMenu popup = new JPopupMenu();
         popup.add(new JMenuItem(
               new AbstractAction("One Minute Timer") {
                  public void actionPerformed(ActionEvent e) {
                     JOptionPane.showMessageDialog(null, "Option 1 selected");
                  }
               }));
         close.setBounds(665,630,55,55);
         close.setOpaque(false);
         close.setContentAreaFilled(false);
         close.setBorderPainted(false);
         close.addMouseListener(
               new MouseAdapter(){
                  @Override
                           public void mousePressed(MouseEvent e){
                     popup.show(e.getComponent(), e.getX(), e.getY());
                  }
               });
               
         add(close,4);
         angle = value;
         Timer timer = new Timer(60000, 
               new ActionListener() {
                  @Override
                           public void actionPerformed(ActionEvent e) {
                     angle -= .5;
                     if (angle > 360) {
                        angle -= 360;
                     }
                     repaint();
                  }
               });
         timer.setRepeats(true);
         timer.setCoalesce(true);
         timer.start();
      }
   
   
               
   
      @Override
      public Dimension getPreferredSize() {
         int width = circle.getHeight() + indicator.getWidth();
         int height = circle.getHeight() + indicator.getWidth();
      
         return new Dimension(width * 2, height * 2);
      }
   
      protected Point getCircleStart() {
      
         int radius = 150; // This is the height of the circle...
      
         int x = Math.round(getWidth() / 2);
         int y = Math.round(getHeight() / 2);
      
         double rads = Math.toRadians(angle); // Make 0 point out to the right...
         // If you add indicator.getWidth, you might be able to change the above...
         int fullLength = Math.round((radius / 2)) ;
      
         // Calculate the outter point of the line
         int xPosy = Math.round((float) (x + Math.cos(rads) * fullLength-10));
         int yPosy = Math.round((float) (y - Math.sin(rads) * fullLength));
      
         return new Point(xPosy, yPosy);
      
      }
   
      @Override
      protected void paintComponent(Graphics g) {
         Image icon = new ImageIcon(getClass().getResource("./images/day.gif")).getImage();
         Image ten = new ImageIcon(getClass().getResource("./images/ten.png")).getImage();
         Image leven = new ImageIcon(getClass().getResource("./images/leven.png")).getImage();
         Image doc = new ImageIcon(getClass().getResource("./images/doc.png")).getImage();
         super.paintComponent(g);
      
         Graphics2D g2d = (Graphics2D) g.create();
      
         int x = (getWidth() - circle.getWidth()) / 2;
         int y = (getHeight() - circle.getHeight()) / 2;
         g2d.drawImage(ten, x -65 ,y -100 , this);
         g2d.drawImage(leven, x +300 ,y -140 , this);
         g2d.drawImage(icon, x + 100,y +90, this);
         
         g2d.drawImage(circle, x, y, this);
      g2d.drawImage(doc, x + 80  ,y -120 , this);
         x = getWidth() / 2;
         y = getHeight() / 2;
      
         Point p = getCircleStart();
         g2d.setColor(Color.WHITE);
         g2d.drawLine(x, y, p.x, p.y);
      
         AffineTransform at = AffineTransform.getTranslateInstance(p.x, p.y);
         at.rotate(Math.toRadians(-angle));
         g2d.setTransform(at);
         g2d.drawImage(indicator, 50,-25, this);
         
      
         g2d.dispose();
      
      }
   }
}