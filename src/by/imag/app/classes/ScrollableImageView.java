package by.imag.app.classes;


import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.OverScroller;

public class ScrollableImageView extends ImageView{
    private GestureDetectorCompat gestureDetector;
    private OverScroller overScroller;

    private final int screenW;
    private final int screenH;

    private int positionX = 0;
    private int positionY = 0;

    public ScrollableImageView(Context context) {
        super(context);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenW = dm.widthPixels;
        screenH = dm.heightPixels;

        gestureDetector = new GestureDetectorCompat(context, gestureListener);
        overScroller = new OverScroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (overScroller.computeScrollOffset()) {
            positionX = overScroller.getCurrX();
            positionY = overScroller.getCurrY();
            scrollTo(positionX, positionY);
        } else {
            overScroller.springBack(positionX, positionY, 0,
                    getMaxHorizontal(), 0, getMaxVertical());
        }
    }

    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            overScroller.forceFinished(true);
            ViewCompat.postInvalidateOnAnimation(ScrollableImageView.this);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            overScroller.forceFinished(true);
            overScroller.fling(positionX, positionY, (int) -velocityX,
                    (int) -velocityY, 0, getMaxHorizontal(), 0,
                    getMaxVertical());
            ViewCompat.postInvalidateOnAnimation(ScrollableImageView.this);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            overScroller.forceFinished(true);
            // normalize scrolling distances to not overscroll the image
            int dx = (int) distanceX;
            int dy = (int) distanceY;
            int newPositionX = positionX + dx;
            int newPositionY = positionY + dy;
            if (newPositionX < 0) {
                dx -= newPositionX;
            } else if (newPositionX > getMaxHorizontal()) {
                dx -= (newPositionX - getMaxHorizontal());
            }
            if (newPositionY < 0) {
                dy -= newPositionY;
            } else if (newPositionY > getMaxVertical()) {
                dy -= (newPositionY - getMaxVertical());
            }
            overScroller.startScroll(positionX, positionY, dx, dy, 0);
            ViewCompat.postInvalidateOnAnimation(ScrollableImageView.this);
            return true;
        }
    };

    private int getMaxHorizontal() {
        return (getDrawable().getBounds().width() - screenW);
    }

    private int getMaxVertical() {
        return (getDrawable().getBounds().height() - screenH);
    }
}
