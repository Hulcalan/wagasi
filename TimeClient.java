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
      
      Integer check = 1;
      Scanner scanner = new Scanner(System.in);
      String time = "";
      String date = new String();
      Integer ms = new Integer(0);   
      timeNode<String, String, Integer> bam = null;
     
   //driver
      while(check != 3){
         System.out.println("1. enter an NTP server.\n2. Print out the Time node. \n3. Exit.");
         check = scanner.nextInt();
         switch(check){
            case 1:
               System.out.println("Enter your server: ");
               time = scanner.next();
               bam = new timeNode<String, String, Integer>(time,date,ms);
               System.out.println("here");
               new input(bam);
               break; //Here I want to break the while loop
            case 2:
               System.out.println("\nFull toString: \n" + bam.toString() 
                  + "\nserver name tostring: " + bam.getInput() + "\nserver date tostring: " + bam.getDate() 
                  + "\nTime in miliseconds of reciept name tostring: " + bam.getTime()+ "\n");
               break;
            case 3:
               System.exit(0);  
            default:
               System.out.println("No such choice");
         }
      }
         
      
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
         System.out.println("sending input to InetAddress");
         System.out.println(alan.getInput());
         InetAddress inetAddress = InetAddress.getByName(alan.getInput());
         System.out.println("Sending InetAddress to getTime");
         
         System.out.println(inetAddress.toString());
         
         TimeInfo timeInfo = timeClient.getTime(inetAddress);
         System.out.println("retrieved the pack and then setting it to milliseconds");
         long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
         System.out.println("success!");
         Date time = new Date(returnTime);
      
         System.out.println("Time from " + alan.getInput() + ": " + time);
        
      //sets the date field in the time node
         String date = time.toString().substring(0, 10) + time.toString().substring(23, time.toString().length());
         alan.setDate(date);
      
      //sets the time field in milliseconds
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
