import java.io.IOException;
import java.time.LocalDateTime;
import java.io.*;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.net.ntp.NTPUDPClient; 
import org.apache.commons.net.ntp.TimeInfo;
import java.util.concurrent.*;
public class TimeClient 
{
}
 class input implements Callable<timeNode>
{

   public timeNode input()  {
      String timea = "129.6.15.30";
      String datea = new String();
      Integer msa = new Integer(0);
      timeNode<String, String, Integer> alan = new timeNode<String, String, Integer>(timea,datea,msa);
      
      try{
         //grans information from the server then converts it to milliseconds
         NTPUDPClient timeClient = new NTPUDPClient();
         InetAddress inetAddress = InetAddress.getByName(alan.getInput());
         TimeInfo timeInfo = timeClient.getTime(inetAddress);
         long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
         Date time = new Date(returnTime);
         String date = time.toString().substring(0, 10) + time.toString().substring(23, time.toString().length());
         alan.setDate(date);
         Integer setTime = (Integer.parseInt(time.toString().substring(11,13)) * 60 * 60 * 1000)
            + (Integer.parseInt(time.toString().substring(14,16)) * 60 * 1000)
            + (Integer.parseInt(time.toString().substring(17,19)) * 1000);
         alan.setTime(setTime);
      }
      catch(Exception e){
         System.out.println("\n Not a valid NTP server");
      }
      return alan;
   }
   
   //redundancy for timeout method
   @Override
      public timeNode call() throws Exception {
   
      String timea = "129.6.15.30";
      String datea = new String();
      Integer msa = new Integer(0);
      timeNode<String, String, Integer> alan = new timeNode<String, String, Integer>(timea,datea,msa);
      
      try{
       
         NTPUDPClient timeClient = new NTPUDPClient();
         InetAddress inetAddress = InetAddress.getByName(alan.getInput());
         TimeInfo timeInfo = timeClient.getTime(inetAddress);
         long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
         Date time = new Date(returnTime);
         String date = time.toString().substring(0, 10) + time.toString().substring(23, time.toString().length());
         alan.setDate(date);
         Integer setTime = (Integer.parseInt(time.toString().substring(11,13)) * 60 * 60 * 1000)
            + (Integer.parseInt(time.toString().substring(14,16)) * 60 * 1000)
            + (Integer.parseInt(time.toString().substring(17,19)) * 1000);
         alan.setTime(setTime);
      }
      catch(Exception e){
         System.out.println("\n Not a valid NTP server");
      }
      return alan;
   }

}
/**
 * creates a time node
 * 
 * 
 */
class timeNode<Input, Date, Time> {

   protected Input input;
   protected Date date;
   protected Time time;
   public timeNode(Input input2, Date date2, Time time2) {
      input = input2;
      date = date2;
      time = time2;
   }// end constructor
/**
 * creates a string for time node information
 * 
 * @returns string
 */
   public String toString() {
      String timeNode = "Server input: " + input.toString() + ". \nDate of server reciept: " + date.toString() 
         +  ". \nTime in ms of reciept: " + time.toString();
      return timeNode;
   }// end toString()


/*
 *
 * allows user to get IP addressed used for NTP
 * 
 * @returns input data
 */
   public Input getInput() {
      return input;
   }
/*
 *
 * allows user to get date
 * 
 * @returns date
 */
   public Date getDate() {
      return date;
   }

/*
 *
 * allows user to get time
 * 
 * @returns time
 */
 
   public Time getTime() {
      return time;
   }
/*
 *
 * allows user to set date
 * 
 * 
 */
 
   public void setDate(Date data) {
      date = data;
   }
/*
 *
 * allows user to set time
 * 
 * 
 */
 
   public void setTime(Time data) {
      time = data;
   }
}// end of class eventNode
