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

  /**
   * 负责矩形实际位置、大小
   */
  private RectF mRect;

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
    mRect = new RectF((width >> 1) - (mainWidth >> 1), (height >> 1) - (mainHeight >> 1),
        (width >> 1) + (mainWidth >> 1), (height >> 1) + (mainHeight >> 1));
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
    return super.dispatchTouchEvent(ev);
  }

  @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
    return super.onInterceptTouchEvent(ev);
  }

  @Override public boolean onTouchEvent(MotionEvent event) {
    return super.onTouchEvent(event);
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
      stretchDrawable.setBounds(left - drawableWidth, centerY - drawableHeight, left + drawableWidth,
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
}
