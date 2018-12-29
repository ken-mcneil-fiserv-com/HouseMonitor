import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import twitter4j.*;
import java.util.Date;

/**
  Montion Sensor code
 */
public class HouseMonitor {

   private static Twitter twitter;

    public static void main(String args[]) throws Exception {
        System.out.println("HouseMonitor ... starting.");
        twitter = setupTwitter();
        notifyTwitter("HouseMonitor ... starting.", twitter);
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalInput motionsensor = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        configureSenssor(motionsensor);
         notifyTwitter("HouseMonitor ... started.", twitter);
        sleep();
      
    }
    
    
  private static void configureSenssor(final GpioPinDigitalInput sensor) {
    sensor.addListener(new GpioPinListenerDigital() {
      @Override
      public void handleGpioPinDigitalStateChangeEvent(final GpioPinDigitalStateChangeEvent event) {
        handleSensorInput(event);
      }

      private void handleSensorInput( final GpioPinDigitalStateChangeEvent event) {
        if (event.getState().isHigh()) {
          notifyTwitter("Montion Detected: ", twitter);
        }
      }
      
    
    });
  }
   
   private static void sleep() throws InterruptedException {
    for (;;) {
      Thread.sleep(1000);
    }
  }
    
   private static void notifyTwitter(String message, Twitter twitter) {
    if (twitter != null ) {
      try {
        twitter.updateStatus(message + new Date());
       
      } catch (TwitterException e) {
        throw new RuntimeException(e);
      }
    }
  }

 
  private static Twitter setupTwitter() throws TwitterException {
    Twitter twitter = new TwitterFactory().getInstance();
      return twitter;
  }


}
