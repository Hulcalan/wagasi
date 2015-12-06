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
import java.lang.Math;

public class UI2 extends TimeClient{
 /*
  *
  *
  *
  */

   private Point mCoords;
   
   public UI2(Integer value) {
      
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
                  Integer call = new Integer(sync(0));
                  frame.add(new ClockPane(getHour(call), getQtrMin(call), sync(call)));
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
    /*
    *turns ms into hour
    */
   
   public static Integer getHour(Integer time){
     
      time = (int)((time /(60*60*1000)));
   
      if (time >= 13){
         time = time - 12 ;
      }
   
      return time;
   }
   /*
    *turns ms into qtr hour
    */
   public static Integer getQtrMin(Integer time){
      int hours = getHour(time);
      
      if ( time  >= 46800000){
      
         hours =(hours + 12)*(60*60*1000);
      }
      else{
         hours = hours*(60*60*1000);
      }
      time = (((time - hours)/(60000)));
      time = (int)((time/10));
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
   
      private BufferedImage mm;
      private BufferedImage pm;
      private BufferedImage images;
      private BufferedImage imagec;
      private Timer timer = null;
      private Integer tempH = 0;
      private Integer tempM = 0;
      private Integer tempMs = 0;
      private Integer count1 = 0;
      private Integer count2 = 0;
      private Integer counter = 0;
      private Integer time = 0;
      private JButton close;
      private JButton set;
      private JLabel[] mmArray;
      private JLabel[] pmArray;
      private JLabel world = new JLabel();
      
   
      public ClockPane(Integer value, Integer min, Integer micro) {
         try {
            mm = ImageIO.read(new File("./images/mm.png"));
            pm = ImageIO.read(new File("./images/pm.png"));
            images = ImageIO.read(new File("./images/x.png"));
            imagec = ImageIO.read(new File("./images/etank.png"));
            ImageIcon myImage = new ImageIcon(images);
            close = new JButton(myImage);
            myImage = new ImageIcon(imagec);
            set = new JButton(myImage);
            mmArray = new JLabel[12];
            pmArray = new JLabel[10];
            for(int i = 0; i < 12; i++){
             //ImageIcon mmd = new ImageIcon("mm.gif");
               myImage = new ImageIcon(this.getClass().getResource("./images/wave.gif"));
               mmArray[i] = new JLabel(myImage);
               mmArray[i].setBounds((50)*i,100,55,55);
               mmArray[i].setOpaque(false);
               mmArray[i].setVisible(false);
               add(mmArray[i],12-i);
            }
            for(int i = 0; i < 10; i++){
            
              myImage = new ImageIcon(this.getClass().getResource("./images/om.gif"));
               pmArray[i] = new JLabel(myImage);
               pmArray[i].setBounds((50)*i,150,55,55);
               pmArray[i].setOpaque(false);
               pmArray[i].setVisible(false);
               add(pmArray[i],10-i);
            }
         } 
         catch (IOException exp) {
            exp.printStackTrace();
         }
         
         time = 60000;
         count1 = value;
         count2 = min;
         counter = micro;
         
         ActionListener action = 
            new ActionListener()
            {   
               @Override
               public void actionPerformed(ActionEvent event)
               {
                  for(int j = 0; j < 12; j++){
                  
                     mmArray[j].setVisible(false);
                  }
                  for(int i = 0; i < count1; i++){
                  
                     mmArray[i].setVisible(true);
                  }
                  for(int k = 0; k < 10; k++){
                  
                     pmArray[k].setVisible(false);
                  }
                  for(int l = 0; l < count2; l++){
                  
                     pmArray[l].setVisible(true);
                  }
                  counter = counter + time;
                  count1 = getHour(counter);
                  count2 = getQtrMin(counter); 
                  
                 
                 
               }
            };
          
         timer = new Timer(time, action);
         timer.setInitialDelay(0);
         timer.setRepeats(true);
         timer.setCoalesce(true);
         timer.start();
      
        
      
      
        
       
               
         // this sets up the custom red x we put in the UI
         //
         //
         //
         close.setBounds(0,35,55,55);
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
          *purpose of this button attemps to resync with the NTP server
          *
          */      
         popup.add(new JMenuItem(
             
               new AbstractAction("Sync With NTP server") {
                  public void actionPerformed(ActionEvent e) {
                     timer.stop();
                     time = 600000;
                     count1 = value;
                     count2 = min;
                     counter = micro;
                    
                     ActionListener action = 
                        new ActionListener()
                        {   
                           @Override
                           public void actionPerformed(ActionEvent event)
                           {
                              for(int j = 0; j < 12; j++){
                              
                                 mmArray[j].setVisible(false);
                              }
                              for(int i = 0; i < count1; i++){
                              
                                 mmArray[i].setVisible(true);
                              }
                              for(int k = 0; k < 10; k++){
                              
                                 pmArray[k].setVisible(false);
                              }
                              for(int l = 0; l < count2; l++){
                              
                                 pmArray[l].setVisible(true);
                              }
                              counter = counter + time;
                              count1 = getHour(counter);
                              count2 = getQtrMin(counter); 
                           
                           
                           
                           }
                        };
                  
                     timer = new Timer(time, action);
                     timer.setInitialDelay(0);
                     timer.setRepeats(true);
                     timer.setCoalesce(true);
                     timer.start();
                  
                  }               
               }));
      
      
      
                 /*
          *purpose of this button attemps to resync with the NTP server
          *
          */      
         popup.add(new JMenuItem(
             
               new AbstractAction("10 second timer") {
                  public void actionPerformed(ActionEvent e) {
                     timer.stop();
                     time = 1000;
                     count1 = value;
                     count2 = min;
                     counter = micro;
                     
                     tempH = 0;
                     ActionListener action = 
                        new ActionListener()
                        {   
                           @Override
                           public void actionPerformed(ActionEvent event)
                           {
                              tempH = tempH + time;
                             for(int i = 0; i < 12; i++){
                              
                                 mmArray[i].setVisible(false);
                              }
                              for(int i = 0; i < (tempH/1000); i++){
                              
                                 mmArray[i].setVisible(true);
                              }
                              for(int k = 0; k < 10; k++){
                                       
                                 pmArray[k].setVisible(false);
                              }
                              
                              if(tempH == 10000){
                                 timer.stop();
                                 time = 60000;
                                 counter = micro + tempH;
                                 Toolkit.getDefaultToolkit().beep();
                                 JOptionPane.showMessageDialog(null, "TIMER COMPLETE!");
                                 ActionListener action = 
                                    new ActionListener()
                                    {   
                                       @Override
                                       public void actionPerformed(ActionEvent event)
                                       {
                                          for(int j = 0; j < 12; j++){
                                          
                                             mmArray[j].setVisible(false);
                                          }
                                          for(int i = 0; i < count1; i++){
                                          
                                             mmArray[i].setVisible(true);
                                          }
                                          for(int k = 0; k < 10; k++){
                                          
                                             pmArray[k].setVisible(false);
                                          }
                                          for(int l = 0; l < count2; l++){
                                          
                                             pmArray[l].setVisible(true);
                                          }
                                          counter = counter + time;
                                          count1 = getHour(counter);
                                          count2 = getQtrMin(counter); 
                                      }
                                    };
                              
                                 timer = new Timer(time, action);
                                 timer.setInitialDelay(0);
                                 timer.setRepeats(true);
                                 timer.setCoalesce(true);
                                 timer.start();
                              }//if
                           }
                        };
                  
                     timer = new Timer(time, action);
                     timer.setInitialDelay(0);
                     timer.setRepeats(true);
                     timer.setCoalesce(true);
                     timer.start();
                  
                  }               
               }));
      
            /* popup menu 
         *
         *
         */      
         set.setBounds(50 ,35,55,55);
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
         int width = 600;
         int height = 600;
         return new Dimension(width, height);
      
      }
   
   
      
   
   
   
   }
   
}
