import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.time.LocalDateTime;
import java.io.*;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.net.ntp.NTPUDPClient; 
import org.apache.commons.net.ntp.TimeInfo;
import java.util.concurrent.*;


public class ClockUI extends TimeClient{
 /*
  *
  *
  *
  */

   private Point mCoords;
   
   public ClockUI(Integer value) {
      
      EventQueue.invokeLater(
            new Runnable() {
               @Override
               public void run() {
                  JFrame frame = new JFrame("Test");
                  mCoords = null;
                  frame.setUndecorated(true);
                  frame.setLayout(new FlowLayout());
                  frame.setBackground(new Color(0,0,0,0));
                  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
                  frame.add(new ClockPane(checkTime(sync(0))));
                  frame.pack();
                  frame.setLocationRelativeTo(null);
                  frame.setVisible(true);
                  frame.repaint();
                  frame.addMouseListener(
                        new MouseListener(){
                           public void mouseReleased(MouseEvent e) {
                              mCoords = null;
                           }
                           public void mousePressed(MouseEvent e) {
                              mCoords = e.getPoint();
                           }
                           public void mouseExited(MouseEvent e) {
                           }
                           public void mouseEntered(MouseEvent e) {
                           }
                           public void mouseClicked(MouseEvent e) {
                           }
                        });
               
                  frame.addMouseMotionListener(
                        new MouseMotionListener(){
                           public void mouseMoved(MouseEvent e) {
                           }
                        
                           public void mouseDragged(MouseEvent e) {
                              Point currCoords = e.getLocationOnScreen();
                              frame.setLocation(currCoords.x - mCoords.x, currCoords.y - mCoords.y);
                           }
                        });
               }
            });
   }
   public static Integer checkTime(Integer time){
      if (time >= 43200000){
         time = 450 - ((time - 43200000)/120000) ;
      }
      else{
         time = 450 - (time/120000) ;
         System.out.println("Finished!");
      }
      return time;
   }
   
   public static Integer sync(Integer time){
      Integer check = 0;
                  
      timeNode<String, String, Integer> bam = null;
                  
      ExecutorService executor = Executors.newSingleThreadExecutor();
      Future<timeNode> future = executor.submit(new input());
                  
      try {//trys to see if it will allow for internet data
         bam = future.get(1, TimeUnit.SECONDS);
         check = bam.getTime();
         System.out.println(bam.toString());
      } 
      catch (TimeoutException ex) {
                     //if it fails to get data from the net it gives the system time.
         future.cancel(true);
         String date = LocalDateTime.now().toString();
         check = dateToMS(date);
         JOptionPane.showMessageDialog(null, "NTP server unavailable at the moment, Now using system time!!");
      }
      catch(InterruptedException f){}
      catch(ExecutionException q){}
      executor.shutdownNow();
      
      return check;
   }
 
  /*
   * this translates the LocalDateTime.now() method into milliseconds
   *
   *
   */
   public static Integer dateToMS(String date){
      date = LocalDateTime.now().toString();
      date = date.substring(11,date.toString().length());
         
      Integer time = (Integer.parseInt(date.substring(0,2)) * 60 * 60 * 1000)
            + (Integer.parseInt(date.substring(3,5)) * 60 * 1000)
            + (Integer.parseInt(date.substring(6,7)) * 1000) 
            + (Integer.parseInt(date.substring(9, date.length())));
         
      return time;
            
   } 
   class ClockPane extends JLayeredPane {
   
      private BufferedImage circle;
      private BufferedImage indicator;
      private double temp = 0;
      private  Timer timer = null;
      private double angle = 0;
      private double diff = 0;
      private Integer counter = 0;
      private Integer time = 0;
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
            close = new JButton(myImage);
            myImage = new ImageIcon(imagec);
            set = new JButton(myImage);
         } 
         catch (IOException exp) {
            exp.printStackTrace();
         }
         
         //this sets up the initial load in to the UI
         angle = value;
         time = 60000;
         diff = .5;
         timer = new Timer(time, 
               new ActionListener() {
                  @Override
                     public void actionPerformed(ActionEvent e) {
                     angle -= diff;
                     if (angle > 360) {
                        angle -= 360;
                     }
                     repaint();
                  }
               });
         timer.setRepeats(true);
         timer.setCoalesce(true);
         timer.start();   
       
               
         // this sets up the custom red x we put in the UI
         //
         //
         //
         close.setBounds(620,665,55,55);
         close.setOpaque(false);
         close.setContentAreaFilled(false);
         close.setBorderPainted(false);
         close.addActionListener(
               new ActionListener(){
                  @Override
                           public void actionPerformed(ActionEvent e){
                     System.exit(0);
                  }
               });
               
         add(close,0);
               
               
        // this sets up the custom menu button
        //
        //
         JPopupMenu popup = new JPopupMenu();
         
         /* 
          *One minute timer button
          *
          */
         popup.add(new JMenuItem(
               new AbstractAction("One Minute Timer") {
                  public void actionPerformed(ActionEvent e) {
                     timer.stop();
                     temp = angle;
                     angle = 0;
                     time = 1000;
                     diff = 6;
                     
                     timer = new Timer(time, 
                           new ActionListener() {
                              @Override
                              public void actionPerformed(ActionEvent e) {
                                 angle -= diff;
                                 if (angle > 360) {
                                    angle -= 360;
                                 }
                                 repaint();
                                 counter = counter + time;
                                // System.out.println(counter);
                                 if( counter == 61000){
                                    timer.stop();
                                 // System.out.println(temp);
                                    angle = temp + (counter/120000);
                                    temp = 0;
                                    time = 60000;
                                    diff = .5;
                                    JOptionPane.showMessageDialog(null, "Alarm sounds AAAWOOOOGAAAAA #lazyProgramer!");
                                 
                                    timer = new Timer(time, 
                                          new ActionListener() {
                                             @Override
                                             public void actionPerformed(ActionEvent e) {
                                                angle -= diff;
                                                if (angle > 360) {
                                                   angle -= 360;
                                                }
                                                repaint();
                                                counter = counter + time;
                                                System.out.println(counter);
                                             }
                                          });
                                    timer.setRepeats(true);
                                    timer.setCoalesce(true);
                                    timer.start();
                                 
                                 }
                              }
                           });
                     timer.setRepeats(true);
                     timer.setCoalesce(true);
                     timer.start();
                     
                  }               
               }));
         
         /* 
          *Stop One minute timer button
          *
          */
         popup.add(new JMenuItem(
             
               new AbstractAction("Stop One Minute Timer") {
                  public void actionPerformed(ActionEvent e) {
                     if(temp != 0){
                        timer.stop();
                                // System.out.println(temp);
                        angle = temp + (counter/120000);
                        time = 60000;
                        diff = .5;
                                   
                        timer = new Timer(time, 
                              new ActionListener() {
                                 @Override
                                             public void actionPerformed(ActionEvent e) {
                                    angle -= diff;
                                    if (angle > 360) {
                                       angle -= 360;
                                    }
                                    repaint();
                                    counter = counter + time;
                                    System.out.println(counter);
                                 }
                              });
                        timer.setRepeats(true);
                        timer.setCoalesce(true);
                        timer.start();
                     }
                     else{
                        temp = 0;
                        JOptionPane.showMessageDialog(null, "No timer started!");
                     
                     }
                  }               
               }));
               
               
         /*
          *purpose of this button attemps to resync with the NTP server
          *
          */      
         popup.add(new JMenuItem(
             
               new AbstractAction("Sync With NTP server") {
                  public void actionPerformed(ActionEvent e) {
                     timer.stop();
                                // System.out.println(temp);
                     angle = checkTime(sync(0));
                     time = 60000;
                     diff = .5;
                                   
                     timer = new Timer(time, 
                           new ActionListener() {
                              @Override
                                             public void actionPerformed(ActionEvent e) {
                                 angle -= diff;
                                 if (angle > 360) {
                                    angle -= 360;
                                 }
                                 repaint();
                                 counter = counter + time;
                                 System.out.println(counter);
                              }
                           });
                     timer.setRepeats(true);
                     timer.setCoalesce(true);
                     timer.start();
                  }               
               }));
      
                  
      
         /* popup menu 
         *
         *
         */      
         set.setBounds(665,630,55,55);
         set.setOpaque(false);
         set.setContentAreaFilled(false);
         set.setBorderPainted(false);
         set.addMouseListener(
               new MouseAdapter(){
                  @Override
                           public void mousePressed(MouseEvent e){
                     popup.show(e.getComponent(), e.getX(), e.getY());
                  }
               });
               
         add(set,4);
         
      
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
      
         // Calculate the outer point of the line
         int xPosy = Math.round((float) (x + Math.cos(rads) * fullLength-10));
         int yPosy = Math.round((float) (y - Math.sin(rads) * fullLength));
      
         return new Point(xPosy, yPosy);
      
      }
   
      /*
       *this draws all the images for the clock
       *
       */
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
         g2d.drawImage(icon, x + 100,y +95, this);
         
         g2d.drawImage(circle, x, y, this);
         g2d.drawImage(doc, x + 80  ,y -120 , this);
         x = getWidth() / 2;
         y = getHeight() / 2;
      
         Point p = getCircleStart();
         //g2d.setColor(Color.WHITE);
         //g2d.drawLine(x, y, p.x, p.y);
         
         //rotational stuff
         AffineTransform at = AffineTransform.getTranslateInstance(p.x, p.y);
         at.rotate(Math.toRadians(-angle));
         g2d.setTransform(at);
         g2d.drawImage(indicator, 50,-25, this);
         g2d.dispose();
      }
   }
}
