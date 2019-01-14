package zhr.com.switchbtn;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * TODO: document your custom view class.
 */
public class Switch extends View {
    private int width,height;
    private int oncolor,offcolor;
    private boolean checked = true;

    private float iconRadius = 0f;
    private float iconClipRadius = 0f;
    private float iconCollapsedRadius = 0f;


    private RectF switchRectF = new RectF(0,0,0,0);

    private RectF iconRectF = new RectF(0,0,0,0);
    private RectF iconClipRectF = new RectF(0,0,0,0);

    private float cornerRadius =0;
    private float iconTranslateX;
    private Paint switchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint iconClipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private AnimatorSet animatorSet;
    private float iconProgress = 1f;

    int currentColor;
    private int iconcolor;


    public Switch(Context context) {
        super(context);
        init(null, 0);
    }

    public Switch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Switch, defStyle, 0);
        width = a.getDimensionPixelSize(R.styleable.Switch_switcher_width,0);
        height = a.getDimensionPixelSize(R.styleable.Switch_switcher_height,0);

        oncolor = a.getColor(R.styleable.Switch_switcher_on_color,0);
        offcolor = a.getColor(R.styleable.Switch_switcher_off_color,0);
        iconcolor = a.getColor(R.styleable.Switch_switcher_icon_color,0);

        checked = a.getBoolean(R.styleable.Switch_android_checked,true);
        if (!checked) iconProgress = 1f;
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        a.recycle();

        iconPaint.setColor(iconcolor);
        switchPaint.setColor(oncolor);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                checked =!checked;
                switchAnimation();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthmode = MeasureSpec.getMode(widthMeasureSpec);
        int heightmode = MeasureSpec.getMode(heightMeasureSpec);
        int widthsize = MeasureSpec.getSize(widthMeasureSpec);
        int heightsize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthmode!=MeasureSpec.EXACTLY||heightmode!=MeasureSpec.EXACTLY){
            widthsize = width;
            heightsize = height;
        }
        setMeasuredDimension(widthsize,heightsize);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        switchRectF.right = width;
        switchRectF.bottom = height;

        cornerRadius = height/2;

        iconRadius = (float) (cornerRadius*0.6);
        iconClipRadius = iconRadius/2.25f;
        iconCollapsedRadius = iconRadius - iconClipRadius;

        iconRectF.set(width-cornerRadius-iconCollapsedRadius/2,(height-2*iconRadius)/2,width-cornerRadius+iconCollapsedRadius/2,height-(height-2*iconRadius)/2);

        if(!checked){
            iconRectF.left = width - cornerRadius - iconCollapsedRadius / 2 - (iconRadius - iconCollapsedRadius / 2);
            iconRectF.right = width - cornerRadius + iconCollapsedRadius / 2 + (iconRadius - iconCollapsedRadius / 2);

            iconClipRectF.set(
                    iconRectF.centerX() - iconClipRadius,
                    iconRectF.centerY() - iconClipRadius,
                    iconRectF.centerX() + iconClipRadius,
                    iconRectF.centerY() + iconClipRadius
            );

            iconTranslateX = -(width - cornerRadius * 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        switchPaint.setColor(oncolor);


        canvas.drawRoundRect(switchRectF,cornerRadius,cornerRadius,switchPaint);

        canvas.drawRoundRect(iconRectF,iconRadius,iconRadius,iconPaint);


        if(iconClipRectF.width()>iconCollapsedRadius)
            canvas.drawRoundRect(iconClipRectF,iconRadius,iconRadius,iconClipPaint);

    }


    public void switchAnimation(){
        if(animatorSet!=null){
            animatorSet.cancel();
        }
        animatorSet = new AnimatorSet();

        float amplitude = 0.2f;
        float frequency = 14.5f;
        float iconTranslateA = 0f;
        float iconTranslateB = -(width - cornerRadius * 2);
        float newProgress = 1f;

        if (!checked) {
            amplitude = 0.15f;
            frequency = 12.0f;
            iconTranslateA = iconTranslateB;
            iconTranslateB = 0f;
            newProgress = 0f;
        }


        ValueAnimator switchani = ValueAnimator.ofFloat(iconProgress,newProgress);

        switchani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
               iconProgress= (float) valueAnimator.getAnimatedValue();
                float iconOffset = lerp(0f, iconRadius - iconCollapsedRadius / 2, iconProgress);
                iconRectF.left = width - cornerRadius - iconCollapsedRadius / 2 - iconOffset;
                iconRectF.right = width - cornerRadius + iconCollapsedRadius / 2 + iconOffset;

                float clipOffset = lerp(0f, iconClipRadius, iconProgress);
                iconClipRectF.set(
                        iconRectF.centerX() - clipOffset,
                        iconRectF.centerY() - clipOffset,
                        iconRectF.centerX() + clipOffset,
                        iconRectF.centerY() + clipOffset
                );
                postInvalidateOnAnimation();

            }
        });
        switchani.setDuration(800);
        switchani.setInterpolator(new BounceInterceptor(amplitude,frequency));


        ValueAnimator transAni = ValueAnimator.ofFloat(0f,1f);
        final float finalIconTranslateA = iconTranslateA;
        final float finalIconTranslateB = iconTranslateB;
        transAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value= (float) valueAnimator.getAnimatedValue();

                iconTranslateX = lerp(finalIconTranslateA, finalIconTranslateB,value);
                iconRectF.set(iconRectF.left+iconTranslateX,iconRectF.top,iconRectF.right+iconTranslateX,iconRectF.bottom);
                iconClipRectF.set(iconClipRectF.left+iconTranslateX,iconClipRectF.top,iconClipRectF.right+iconTranslateX,iconClipRectF.bottom);
                postInvalidateOnAnimation();

            }
        });
        transAni.setDuration(800);

        int color = (!checked)?oncolor:offcolor;

        iconClipPaint.setColor(color);



        ValueAnimator colorAni = new ValueAnimator();
        colorAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentColor = (int) valueAnimator.getAnimatedValue();
                switchPaint.setColor(currentColor);
                iconClipPaint.setColor(currentColor);
                postInvalidateOnAnimation();
            }
        });

        colorAni.setIntValues(currentColor,color);
        colorAni.setEvaluator(new ArgbEvaluator());
        colorAni.setDuration(300);

        checked =!checked;
        animatorSet.playTogether(switchani,transAni,colorAni);
        animatorSet.start();



    }


    @Override
    public boolean callOnClick() {
        toggleChecked();
        return super.callOnClick();
    }

    public void toggleChecked(){
        checked = !checked;
        switchAnimation();
    }

    public  float lerp(float a,float b,float v){
        return a+(b-a)*v;
    }
}
