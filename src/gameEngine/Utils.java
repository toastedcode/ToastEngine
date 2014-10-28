package gameEngine;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

public class Utils
{
   static Point2D translatePoint(
      Point2D point,
      Vector2D vector)
   {
      return (new Point2D.Double(
                 (point.getX() + vector.x),
                 (point.getY() + vector.y)));
   }
   
   
   public static double round(
      double d,
      int decimalPlaces)
   {
      BigDecimal bd = new BigDecimal(d);
      bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
      
      return (bd.doubleValue());
   }
}
