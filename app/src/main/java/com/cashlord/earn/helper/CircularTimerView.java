package com.cashlord.earn.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cashlord.earn.R;

public class CircularTimerView extends View {
    private Paint progressBarPaint;
    private Paint progressBarBackgroundPaint;
    private Paint backgroundPaint;
    private Paint textPaint;
    private float mRadius;
    private final RectF mArcBounds = new RectF();
    private CircularTimerListener circularTimerListener;
    private float drawUpto = 0f;
    private long remainingTime = 0;
    private long timeInterval = 0;
    private long maxTime = 0;
    private int progressColor;
    private int progressBackgroundColor;
    private int backgroundColor;
    private float strokeWidthDimension = 0f;
    private float backgroundWidth = 0f;
    private boolean roundedCorners = false;
    private float maxValue = 0f;
    private int progressTextColor = Color.BLACK;
    private float circularTextSize = 18f;
    private String text = "";
    private String suffix = "";
    private String prefix = "";
    private boolean isClockwise = true;
    private int startingAngle = 270;
    private int defStyleAttr = 0;
    private CountDownTimer countDownTimer;

    public CircularTimerView(Context context) {
        super(context);
        initPaints(context, null);
    }

    public CircularTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints(context, attrs);
    }

    public CircularTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.defStyleAttr = defStyleAttr;
        initPaints(context, attrs);
    }

    private void initPaints(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularTimerView, defStyleAttr, 0);
            progressColor = ta.getColor(R.styleable.CircularTimerView_progressColor, Color.BLUE);
            backgroundColor = ta.getColor(R.styleable.CircularTimerView_backgroundColor, Color.GRAY);
            progressBackgroundColor = ta.getColor(R.styleable.CircularTimerView_progressBackgroundColor, Color.GRAY);
            strokeWidthDimension = ta.getDimension(R.styleable.CircularTimerView_strokeWidthDimension, 10f);
            backgroundWidth = ta.getDimension(R.styleable.CircularTimerView_backgroundWidth, 10f);
            roundedCorners = ta.getBoolean(R.styleable.CircularTimerView_roundedCorners, false);
            maxValue = ta.getFloat(R.styleable.CircularTimerView_maxValue, 100f);
            progressTextColor = ta.getColor(R.styleable.CircularTimerView_progressTextColor, Color.BLACK);
            circularTextSize = ta.getDimension(R.styleable.CircularTimerView_textSize, 18f);
            suffix = ta.getString(R.styleable.CircularTimerView_suffix);
            prefix = ta.getString(R.styleable.CircularTimerView_prefix);
            text = ta.getString(R.styleable.CircularTimerView_progressText);
            isClockwise = ta.getBoolean(R.styleable.CircularTimerView_isClockwise, true);
            startingAngle = ta.getInt(R.styleable.CircularTimerView_startingPoint, 270);
            ta.recycle();
        }

        progressBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBarPaint.setStyle(Paint.Style.STROKE);
        progressBarPaint.setColor(progressColor);
        progressBarPaint.setStrokeWidth(strokeWidthDimension * getResources().getDisplayMetrics().density);
        progressBarPaint.setStrokeCap(roundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);

        progressBarBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressBarBackgroundPaint.setStyle(Paint.Style.STROKE);
        progressBarBackgroundPaint.setColor(progressBackgroundColor);
        progressBarBackgroundPaint.setStrokeWidth(backgroundWidth * getResources().getDisplayMetrics().density);
        progressBarBackgroundPaint.setStrokeCap(Paint.Cap.SQUARE);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setColor(backgroundColor);

        textPaint = new TextPaint();
        textPaint.setColor(progressTextColor);
        textPaint.setTextSize(circularTextSize);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = Math.min(w, h) / 2f;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float mouthInset = mRadius / 3;
        canvas.drawCircle(mRadius, mRadius, mouthInset * 2, backgroundPaint);
        mArcBounds.set(mouthInset, mouthInset, mRadius * 2 - mouthInset, mRadius * 2 - mouthInset);
        canvas.drawArc(mArcBounds, 0f, 360f, false, progressBarBackgroundPaint);

        float drawArcAngle = drawUpto / getMaxValue() * 360;
        if (isClockwise) {
            canvas.drawArc(mArcBounds, startingAngle, drawArcAngle, false, progressBarPaint);
        } else {
            canvas.drawArc(mArcBounds, startingAngle, -drawArcAngle, false, progressBarPaint);
        }

        String drawnText = prefix + text + suffix;
        if (!TextUtils.isEmpty(text)) {
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(drawnText, (getWidth() - textPaint.measureText(drawnText)) / 2.0f, (getHeight() - textHeight) / 2.0f, textPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDetachedFromWindow();
    }

    public float getProgress() {
        return drawUpto;
    }

    public void setProgress(float f) {
        drawUpto = f;
        invalidate();
    }

    public float getProgressPercentage() {
        return drawUpto / getMaxValue() * 100;
    }

    public void setProgressColor(int color) {
        progressColor = color;
        progressBarPaint.setColor(color);
        invalidate();
    }

    public void setProgressColor(String color) {
        progressBarPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    @Override
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        backgroundPaint.setColor(color);
        invalidate();
    }

    public void setBackgroundColor(String color) {
        backgroundPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public void setProgressBackgroundColor(int color) {
        progressBackgroundColor = color;
        progressBarBackgroundPaint.setColor(color);
        invalidate();
    }

    public void setProgressBackgroundColor(String color) {
        progressBarBackgroundPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    private float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float max) {
        maxValue = max;
        invalidate();
    }

    public void setStrokeWidthDimension(float width) {
        strokeWidthDimension = width;
        invalidate();
    }

    public float getStrokeWidthDimension() {
        return strokeWidthDimension;
    }

    public void setBackgroundWidth(float width) {
        backgroundWidth = width;
        invalidate();
    }

    public float getBackgroundWidth() {
        return backgroundWidth;
    }

    public void setText(String progressText) {
        text = progressText;
        invalidate();
    }

    public String getText() {
        return text;
    }

    public void setTextColor(String color) {
        textPaint.setColor(Color.parseColor(color));
        invalidate();
    }

    public int getTextColor() {
        return progressTextColor;
    }

    public void setTextColor(int color) {
        progressTextColor = color;
        textPaint.setColor(color);
        invalidate();
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        invalidate();
    }

    public String getSuffix() {
        return suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        invalidate();
    }

    public boolean getClockwise() {
        return isClockwise;
    }

    public void setClockwise(boolean clockwise) {
        isClockwise = clockwise;
        invalidate();
    }

    public int getStartingAngle() {
        return startingAngle;
    }

    public void setStartingAngle(int startingAngle) {
        this.startingAngle = startingAngle;
        invalidate();
    }

    public void setTimer(long time) {
        if (time <= 0) {
            return;
        }
        this.maxTime = time;
        this.remainingTime = time;
        countDownTimer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                remainingTime = millisUntilFinished;
                drawUpto = (maxTime - millisUntilFinished) / (float) maxTime * maxValue;
                invalidate();
                if (circularTimerListener != null) {
                    circularTimerListener.updateDataOnTick(remainingTime);
                }
            }

            public void onFinish() {
                drawUpto = maxValue;
                invalidate();
                if (circularTimerListener != null) {
                    circularTimerListener.onTimerFinished();
                }
            }
        };
        countDownTimer.start();
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    /*public void setCircularTimerListener(CircularTimerListener listener) {
        this.circularTimerListener = listener;
    }*/

    public void setCircularTimerListener(CircularTimerListener circularTimerListener, long time, TimeFormatEnum timeFormatEnum) {
        this.circularTimerListener = circularTimerListener;
        long timeInMillis = 0;
        final long intervalDuration = 1000;

        switch (timeFormatEnum) {
            case MILLIS:
                timeInMillis = time;
                break;
            case SECONDS:
                timeInMillis = time * 1000;
                break;
            case MINUTES:
                timeInMillis = time * 1000 * 60;
                break;
            case HOUR:
                timeInMillis = time * 1000 * 60 * 60;
                break;
            case DAY:
                timeInMillis = time * 1000 * 60 * 60 * 24;
                break;
            default:
                break;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        final long maxTime = timeInMillis;
        countDownTimer = new CountDownTimer(maxTime, intervalDuration) {
            @Override
            public void onTick(long l) {
                remainingTime = l;
                double percentTimeCompleted = (maxTime - l) / (double) maxTime;
                drawUpto = (float) (maxValue * percentTimeCompleted);
                text = circularTimerListener.updateDataOnTick(l);
                invalidate();
            }

            @Override
            public void onFinish() {
                double percentTimeCompleted = (maxTime - remainingTime) / (double) maxTime;
                drawUpto = (float) (maxValue * percentTimeCompleted);
                circularTimerListener.onTimerFinished();
                invalidate();
            }
        };
    }

    public void setCircularTimerListener(CircularTimerListener circularTimerListener, long time, TimeFormatEnum timeFormatEnum, long timeInterval) {
        this.timeInterval = timeInterval;
        this.circularTimerListener = circularTimerListener;
        long timeInMillis = 0;

        switch (timeFormatEnum) {
            case MILLIS:
                timeInMillis = time;
                break;
            case SECONDS:
                timeInMillis = time * 1000;
                break;
            case MINUTES:
                timeInMillis = time * 1000 * 60;
                break;
            case HOUR:
                timeInMillis = time * 1000 * 60 * 60;
                break;
            case DAY:
                timeInMillis = time * 1000 * 60 * 60 * 24;
                break;
            default:
                break;
        }

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        this.maxTime = timeInMillis;
        this.remainingTime = timeInMillis;
        setCountDownTimer();
    }

    private boolean startTimer() {
        if (countDownTimer == null) {
            return false;
        } else {
            countDownTimer.start();
            return true;
        }
    }

    public boolean pauseTimer() {
        if (countDownTimer == null) {
            return false;
        } else {
            countDownTimer.cancel();
            return true;
        }
    }

    public boolean resumeTimer() {
        if (countDownTimer == null) {
            return false;
        } else {
            setCountDownTimer();
            return true;
        }
    }

    public boolean stopTimer() {
        if (countDownTimer == null) {
            return false;
        } else {
            countDownTimer.cancel();
            return true;
        }
    }

    private void setCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(remainingTime, timeInterval) {
            @Override
            public void onTick(long l) {
                remainingTime = l;
                double percentTimeCompleted = (maxTime - l) / (double) maxTime;
                drawUpto = (float) (maxValue * percentTimeCompleted);
                if (circularTimerListener != null) {
                    text = circularTimerListener.updateDataOnTick(l);
                }
                invalidate();
            }

            @Override
            public void onFinish() {
                double percentTimeCompleted = (maxTime - remainingTime) / (double) maxTime;
                drawUpto = (float) (maxValue * percentTimeCompleted);
                if (circularTimerListener != null) {
                    text = circularTimerListener.updateDataOnTick(0);
                    circularTimerListener.onTimerFinished();
                }
                invalidate();
            }
        };

        startTimer();
    }
}
