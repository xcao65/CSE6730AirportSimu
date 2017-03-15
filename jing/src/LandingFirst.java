/**
 * Author: Jing Gu (jgu47)
 * Date: 02/13/2017
*/

import java.util.Comparator;

public class LandingFirst implements Comparator<AirportEvent> {
  public int compare(AirportEvent e0, AirportEvent e1) {
    if(e0.getType() == e1.getType())
      if(e0.getTime() == e1.getTime())
        return e0.getPlane().getName().compareTo(e1.getPlane().getName());
      else
        return Double.compare(e0.getTime(), e1.getTime());
    else
      return Integer.compare(e0.getType(), e1.getType());
  }
  @Override
  public String toString() {
    return "LandingFirst";
  }
}
