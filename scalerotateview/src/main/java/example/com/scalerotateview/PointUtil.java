package example.com.scalerotateview;

/**
 * @author: dynamic
 * email:  dynamic@gmail.com
 * date:   on 2019/4/1
 */
public class PointUtil {

  public static float calculatePointDistance(float[] pt1, float[] pt2) {
    return (float) Math.sqrt(Math.pow(pt1[0] - pt2[0], 2) + Math.pow(pt1[1] - pt2[1], 2));
  }

  public static double calculateAngleBetweenPoints(float[] pt1, float[] pt2) {
    float dx = pt2[0] - pt1[0];
    float dy = pt2[1] - pt1[1];
    if (dx == 0 && dy == 0) {
      return 0d;
    }
    double angle;
    angle = Math.toDegrees(Math.atan2(dx, dy));
    if (angle < 0) {
      angle = 360d + angle % (-360d);
    } else {
      angle = angle % 360d;
    }
    return angle;
  }
}
