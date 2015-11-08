import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import org.apache.commons.net.ntp.NTPUDPClient; 
import org.apache.commons.net.ntp.TimeInfo;

   
public final class TimeClient
{
     
   public static void main(String[] args) throws Exception {
      String TIME_SERVER = "0.pool.ntp.org";   
      NTPUDPClient timeClient = new NTPUDPClient();
      InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
      TimeInfo timeInfo = timeClient.getTime(inetAddress);
      long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
      Date time = new Date(returnTime);
      System.out.println( "Time Server: " + TIME_SERVER + ": " + time + " " + "\n " + timeInfo.getMessage() );
   }
}