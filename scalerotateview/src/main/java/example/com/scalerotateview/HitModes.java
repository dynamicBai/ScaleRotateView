package example.com.scalerotateview;

import android.support.annotation.IntDef;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: dynamic
 * email:  dynamic@gmail.com
 * date:   on 2019/3/29
 */
public class HitModes {

  public static final int NONE = -1;

  public static final int MOVE = 0;
  public static final int ROTATE = 1;
  public static final int SCALE = 2;

  public static final int LEFT_STRETCH = 10;
  public static final int RIGHT_STRETCH = 11;
  public static final int TOP_STRETCH = 12;
  public static final int BOTTOM_STRETCH = 13;

  @IntDef({ NONE, MOVE, ROTATE, SCALE, LEFT_STRETCH, RIGHT_STRETCH, TOP_STRETCH, BOTTOM_STRETCH })
  @Retention(RetentionPolicy.SOURCE) @Target({ ElementType.FIELD, ElementType.PARAMETER })
  public @interface HitMode {
  }
}
