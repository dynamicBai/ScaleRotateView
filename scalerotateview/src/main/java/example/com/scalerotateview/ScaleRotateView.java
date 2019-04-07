package example.com.scalerotateview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import example.com.scalerotateview2.R;

/**
 * @author: dynamic
 * email:  dynamic@gmail.com
 * date:   on 2019/3/28
 */
public class ScaleRotateView extends RelativeLayout {
  /**
   * 负责中心旋转的矩阵
   */
  private Matrix mRotateMatrix;

  private float mRotation = 0;

  /**
   * 负责矩形实际位置、大小
   */
  private RectF mRect;

  /**
   * 临时的recF 目的是减少对象创建
   */
  private RectF tempRect;

  private Paint mPaint;

  private Drawable mainDrawable;

  /**
   * 角图标
   */
  Drawable rotateDrawable;
  Drawable scaleDrawable;
  Drawable stretchDrawable;

  /**
   * 角标宽高的一半  默认所有角标都相同
   */
  private int drawableWidth;
  private int drawableHeight;

  /**
   * 图片的比例
   */
  private float mRatio = 1;

  /**
   * gesture
   */
  private GestureDetector mGestureDetector;

  /**
   * 是否点到了控件
   */
  private boolean hasFocus;

  private @HitModes.HitMode int mHitMode = HitModes.NONE;

  public ScaleRotateView(Context context) {
    super(context);
    init();
  }

  public ScaleRotateView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ScaleRotateView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    mGestureDetector = new GestureDetector(getContext(), simpleOnGestureListener);
    mRotateMatrix = new Matrix();
    mPaint = new Paint();
    mPaint.setColor(Color.RED);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(4);
    mPaint.setPathEffect(new DashPathEffect(new float[] { 6, 6 }, 0));
    DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
    int width = dm.widthPixels;
    int height = dm.heightPixels;

    rotateDrawable = getResources().getDrawable(R.drawable.scalerotateview_rotate);
    scaleDrawable = getResources().getDrawable(R.drawable.scalerotateview_scale);
    stretchDrawable = getResources().getDrawable(R.drawable.scalerotaeview_stretch);
    mainDrawable = getResources().getDrawable(R.drawable.saclerotateview_like);
    int mainWidth = mainDrawable.getIntrinsicWidth();
    int mainHeight = mainDrawable.getIntrinsicHeight();
    mRatio = mainWidth / (float) mainHeight;
    mRect = new RectF((width >> 1) - (mainWidth >> 1), (height >> 1) - (mainHeight >> 1),
        (width >> 1) + (mainWidth >> 1), (height >> 1) + (mainHeight >> 1));
    tempRect = new RectF(mRect);
    drawableWidth = rotateDrawable.getIntrinsicWidth() / 2;
    drawableHeight = rotateDrawable.getIntrinsicHeight() / 2;
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
  }

  @Override protected void dispatchDraw(Canvas canvas) {
    canvas.save();
    canvas.concat(mRotateMatrix);
    mainDrawable.setBounds((int) mRect.left, (int) mRect.top, (int) mRect.right,
        (int) mRect.bottom);
    mainDrawable.draw(canvas);
    canvas.drawRect(mRect, mPaint);
    drawAnchors(canvas, mRect);
    canvas.restore();
    super.dispatchDraw(canvas);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    int action = ev.getAction();
    if (action == MotionEvent.ACTION_DOWN) {
      float[] points = new float[] { ev.getX(), ev.getY() };
      mRotateMatrix.mapPoints(points);
      //点击区域包含角标外面那一圈
      RectF rectF = new RectF(mRect);
      rectF.inset(-drawableWidth, -drawableHeight);
      hasFocus = mRect.contains(points[0], points[1]);
    }
    //if (!hasFocus) {
    //  Log.d("sssss","no focus");
    //  return false;
    //}
    return super.dispatchTouchEvent(ev);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    return super.onInterceptTouchEvent(ev);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    mGestureDetector.onTouchEvent(event);
    return true;
  }

  private void drawAnchors(Canvas canvas, RectF rectF) {
    int left = (int) rectF.left;
    int right = (int) rectF.right;
    int top = (int) rectF.top;
    int bottom = (int) rectF.bottom;
    int centerX = (int) rectF.centerX();
    int centerY = (int) rectF.centerY();
    if (rotateDrawable != null) {
      //右上角
      rotateDrawable.setBounds(right - drawableWidth, top - drawableHeight, right + drawableWidth,
          top + drawableHeight);
      rotateDrawable.draw(canvas);
    }

    if (scaleDrawable != null) {
      //右下角
      scaleDrawable.setBounds(right - drawableWidth, bottom - drawableHeight,
          right + drawableWidth, bottom + drawableHeight);
      scaleDrawable.draw(canvas);
    }

    //四个方向拉伸点
    if (stretchDrawable != null) {
      //左
      stretchDrawable.setBounds(left - drawableWidth, centerY - drawableHeight,
          left + drawableWidth,
          centerY + drawableHeight);
      stretchDrawable.draw(canvas);
      //上
      stretchDrawable.setBounds(centerX - drawableWidth, top - drawableHeight,
          centerX + drawableWidth, top + drawableHeight);
      stretchDrawable.draw(canvas);
      //右
      stretchDrawable.setBounds(right - drawableWidth, centerY - drawableHeight,
          right + drawableWidth, centerY + drawableHeight);
      stretchDrawable.draw(canvas);
      //下
      stretchDrawable.setBounds(centerX - drawableWidth, bottom - drawableHeight,
          centerX + drawableWidth, bottom + drawableHeight);
      stretchDrawable.draw(canvas);
    }
  }

  private GestureDetector.SimpleOnGestureListener simpleOnGestureListener =
      new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
          //借助onScroll回调回来的distance来算距离
          onMouseMove(mHitMode, e2.getX(), e2.getY(), distanceX, distanceY);
          return true;
        }

        @Override public boolean onDown(MotionEvent e) {
          //确定现在点到了哪个角标
          mHitMode = getHitMode(e.getX(), e.getY());
          return super.onDown(e);
        }
      };

  /**
   * 执行手指的移动
   * 后续可能会实现手势，这里取名mouseMove
   */
  private void onMouseMove(int mode, float triggerX, float triggerY, float dx, float dy) {
    Log.d("sssss", "onMouseMove mode: " + mode);
    if (mode == HitModes.NONE) {
      return;
    }
    if (mode == HitModes.MOVE) {
      onMove(dx, dy);
    } else if (mode == HitModes.ROTATE) {
      onRotate(triggerX, triggerY);
    } else if (mode == HitModes.SCALE) {
      onScale(dx, dy);
    } else if (mode == HitModes.LEFT_STRETCH
        || mode == HitModes.RIGHT_STRETCH
        || mode == HitModes.TOP_STRETCH
        || mode == HitModes.BOTTOM_STRETCH) {
      // TODO: 2019/4/7 onStretch
    }
  }

  /**
   * 点中了哪个角标
   * 优先旋转、缩放
   * 然后四个拉伸
   * 最后move(contain)
   */
  private int getHitMode(float eventX, float eventY) {

    float[] point = new float[] { eventX, eventY };

    final Matrix rotateMatrix = new Matrix();
    //反向旋转回去 抵消canvas的旋转
    rotateMatrix.postTranslate(-mRect.centerX(), -mRect.centerY());
    rotateMatrix.postRotate(-mRotation);
    rotateMatrix.postTranslate(mRect.centerX(), mRect.centerY());
    rotateMatrix.mapPoints(point);
    eventX = point[0];
    eventY = point[1];

    RectF rectF = mRect;
    //hit rotate  右上
    if (Math.abs(rectF.right - eventX) < drawableWidth * 2
        && Math.abs(rectF.top - eventY) < drawableHeight * 2) {
      return HitModes.ROTATE;
    }

    //hit scale 右下
    if (Math.abs(rectF.right - eventX) < drawableWidth * 2
        && Math.abs(rectF.bottom - eventY) < drawableHeight * 2) {
      return HitModes.SCALE;
    }

    //hit 左拉
    if (Math.abs(rectF.left - eventX) < drawableWidth
        && Math.abs(rectF.centerY() - eventY) < drawableHeight) {
      return HitModes.LEFT_STRETCH;
    }

    //hit 右拉
    if (Math.abs(rectF.right - eventX) < drawableWidth
        && Math.abs(rectF.centerY() - eventY) < drawableHeight) {
      return HitModes.RIGHT_STRETCH;
    }

    //hit 上拉
    if (Math.abs(rectF.centerX() - eventX) < drawableWidth
        && Math.abs(rectF.top - eventY) < drawableHeight) {
      return HitModes.TOP_STRETCH;
    }

    //hit 下拉
    if (Math.abs(rectF.centerX() - eventX) < drawableWidth
        && Math.abs(rectF.bottom - eventY) < drawableHeight) {
      return HitModes.BOTTOM_STRETCH;
    }

    //move
    if (rectF.contains(eventX, eventY)) {
      return HitModes.MOVE;
    }
    return HitModes.NONE;
  }

  private void onMove(float dx, float dy) {
    mRect.offset(-dx, -dy);
    invalidateMatrix();
    invalidate();
  }

  private void onScale(float dx, float dy) {
    // TODO: 2019/4/8 这里的dx,dy计算需要改进
    float[] pt1 = new float[] { mRect.centerX(), mRect.centerY() };
    float[] pt2 = new float[] { mRect.right, mRect.bottom };
    float[] pt3 = new float[] { mRect.right + dx, mRect.bottom + dy };
    float distance1 = getPointDistance(pt1, pt2);
    float distance2 = getPointDistance(pt1, pt3);
    float distance = distance1 - distance2;
    if (!checkCanScale(distance)) {
      return;
    }
    mRect.inset(-distance, -distance / mRatio);
    invalidateMatrix();
    invalidate();
  }

  private void onRotate(float triggerX, float triggerY) {
    // TODO: 2019/4/8 这里的dx,dy计算需要改进
    float[] pt1 = new float[] { mRect.centerX(), mRect.centerY() };
    float[] pt2 = new float[] { mRect.right, mRect.top };
    float[] pt3 = new float[] { triggerX, triggerY };
    double angel1 = PointUtil.calculateAngleBetweenPoints(pt2, pt1);
    double angel2 = PointUtil.calculateAngleBetweenPoints(pt3, pt1);
    mRotation = (float) (angel1 - angel2);
    invalidateMatrix();
    invalidate();
  }

  private void invalidateMatrix() {
    mRotateMatrix.reset();
    mRotateMatrix.postTranslate(-mRect.centerX(), -mRect.centerY());
    mRotateMatrix.postRotate(mRotation);
    mRotateMatrix.postTranslate(mRect.centerX(), mRect.centerY());
  }

  /**
   * 计算两个点之间的距离
   *
   * @param pt1 点1
   * @param pt2 点2
   */
  private float getPointDistance(float[] pt1, float[] pt2) {
    return PointUtil.calculatePointDistance(pt1, pt2);
  }

  /**
   * 设置一个缩放的最小阈值 低于阈值，不可继续缩小
   */
  private boolean checkCanScale(float distance) {
    tempRect.set(mRect);
    tempRect.inset(-distance, -distance / mRatio);
    if (tempRect.width() < drawableHeight * 3 || tempRect.height() < drawableHeight * 3) {
      return false;
    }
    return true;
  }
}
