import java.io.IOException;
import java.io.*;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.net.ntp.NTPUDPClient; 
import org.apache.commons.net.ntp.TimeInfo;
import java.util.Scanner;
   
public class TimeClient
{
     
   public static void main(String[] args) throws Exception {
      
      Integer check = 0;
      Scanner scanner = new Scanner(System.in);
      String time = "129.6.15.30";
      String date = new String();
      Integer ms = new Integer(0);   
      timeNode<String, String, Integer> bam = new timeNode<String, String, Integer>(time,date,ms);
      new input(bam);
      check = (bam.getTime()/120000) + 30;
     // if( check > 360){
     // check = check -360;
      //}
      System.out.println(check);
      System.out.println(bam.getTime());
      new ClockUI(check);
        
      
   }
}
/*
*
*
*
*/
class input
{
   public input(timeNode<String, String, Integer> alan) throws Exception{
      
      
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
   }
 
   
}
class timeNode<Input, Date, Time> {

   protected Input input;
   protected Date date;
   protected Time time;
   public timeNode(Input input2, Date date2, Time time2) {
      input = input2;
      date = date2;
      time = time2;
   }// end constructor

   public String toString() {
      String timeNode = "Server input: " + input.toString() + ". \nDate of server reciept: " + date.toString() 
         +  ". \nTime in ms of reciept: " + time.toString();
      return timeNode;
   }// end toString()

   public Input getInput() {
      return input;
   }

   public Date getDate() {
      return date;
   }

   public Time getTime() {
      return time;
   }
   public void setDate(Date data) {
      date = data;
   }
   public void setTime(Time data) {
      time = data;
   }
}// end of class eventNode
