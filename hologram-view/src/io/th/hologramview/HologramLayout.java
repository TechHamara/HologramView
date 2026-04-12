package io.th.hologramview;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class HologramLayout extends LinearLayout {

    private final Context context;
    private SkewFrameLayout middleContainer;
    private View lightView;
    private TextView nameText, titleText, statsText;
    private Button followBtn;
    private ImageView profileImg;

    private boolean isOpen = false;
    private ValueAnimator openAnimator;
    private ValueAnimator holoAnimator;
    private ValueAnimator lightAnimator;

    private int maxHoloHeight;
    private int holoDuration = 15000;
    private HologramListener listener;
    private CompanionHelper companionHelper;

    public interface HologramListener {
        void onOpened();
        void onClosed();
        void onFollowClicked();
        void onProfileImageClicked();
        void onProfileNameClicked();
        void onProfileTitleClicked();
        void onProfileStatsClicked();
    }

    public HologramLayout(Context context, HologramListener listener, CompanionHelper helper) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.companionHelper = helper;

        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);

        // Container background matching HTML body background
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.BL_TR,
                new int[] { Color.rgb(10, 10, 17), Color.rgb(50, 50, 57) });
        setBackground(bg);

        maxHoloHeight = dp(400);

        initTopBar();
        initMiddle();
        initBottomBar();

        startLightAnimation();

        // Enforce initial fully-closed layout state
        post(new Runnable() {
            @Override
            public void run() {
                if (!isOpen) {
                    ViewGroup.LayoutParams p = middleContainer.getLayoutParams();
                    p.height = 0;
                    middleContainer.setLayoutParams(p);
                    middleContainer.setAlpha(0f);
                }
            }
        });
    }

    private void initTopBar() {
        FrameLayout top = new FrameLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(300), dp(30));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        top.setLayoutParams(lp);

        GradientDrawable topBg = new GradientDrawable();
        topBg.setColor(Color.rgb(80, 80, 87));
        topBg.setCornerRadii(new float[] { dp(15), dp(15), dp(15), dp(15), dp(3), dp(3), dp(3), dp(3) });
        top.setBackground(topBg);

        lightView = new View(context);
        FrameLayout.LayoutParams lightLp = new FrameLayout.LayoutParams(dp(20), dp(5));
        lightLp.gravity = Gravity.END | Gravity.TOP;
        lightLp.setMargins(0, dp(13), dp(15), 0);
        lightView.setLayoutParams(lightLp);

        GradientDrawable lightBg = new GradientDrawable();
        lightBg.setColor(Color.rgb(10, 10, 15));
        lightBg.setCornerRadius(dp(3));
        lightView.setBackground(lightBg);

        top.addView(lightView);

        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOpen();
            }
        });

        addView(top);
    }

    private void initMiddle() {
        middleContainer = new SkewFrameLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(290), 0);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        middleContainer.setLayoutParams(lp);

        // Middle hologram gradient
        GradientDrawable midBg = new GradientDrawable(GradientDrawable.Orientation.BL_TR,
                new int[] { Color.rgb(30, 80, 180), Color.rgb(90, 140, 240) });
        middleContainer.setBackground(midBg);

        // Add 3 decorative circles
        addCircles();

        // Inner content Layout
        LinearLayout contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        FrameLayout.LayoutParams contentLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        contentLp.setMargins(dp(5), dp(5), dp(5), dp(5));
        contentLayout.setLayoutParams(contentLp);

        // Profile Image
        profileImg = new ImageView(context);
        LinearLayout.LayoutParams imgLp = new LinearLayout.LayoutParams(dp(90), dp(90));
        imgLp.setMargins(0, dp(25), 0, dp(15));
        profileImg.setLayoutParams(imgLp);
        profileImg.setScaleType(ImageView.ScaleType.CENTER_CROP);

        GradientDrawable imgBorder = new GradientDrawable();
        imgBorder.setShape(GradientDrawable.OVAL);
        imgBorder.setStroke(dp(3), Color.WHITE);
        imgBorder.setColor(Color.LTGRAY); // Placeholder background
        profileImg.setBackground(imgBorder);
        profileImg.setClipToOutline(true); // Circles the image cleanly on modern Android
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            profileImg.setElevation(dp(8));
        }
        
        profileImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onProfileImageClicked();
            }
        });

        nameText = new TextView(context);
        nameText.setTextSize(22);
        nameText.setTextColor(Color.WHITE);
        nameText.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        nameText.setShadowLayer(4, 0, dp(2), Color.argb(128, 0, 0, 0));
        nameText.setGravity(Gravity.CENTER);
        nameText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onProfileNameClicked();
            }
        });

        titleText = new TextView(context);
        titleText.setTextSize(14);
        titleText.setTextColor(Color.WHITE);
        titleText.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC));
        titleText.setGravity(Gravity.CENTER);
        titleText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onProfileTitleClicked();
            }
        });

        statsText = new TextView(context);
        statsText.setTextSize(14);
        statsText.setTextColor(Color.rgb(200, 200, 205));
        statsText.setPadding(0, dp(10), 0, dp(20));
        statsText.setGravity(Gravity.CENTER);
        statsText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onProfileStatsClicked();
            }
        });

        followBtn = new Button(context);
        followBtn.setTextColor(Color.WHITE);
        followBtn.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        followBtn.setPadding(dp(30), dp(10), dp(30), dp(10));
        GradientDrawable btnBg = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.rgb(60, 110, 210), Color.rgb(100, 150, 250) });
        btnBg.setCornerRadius(dp(30));
        followBtn.setBackground(btnBg);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            followBtn.setElevation(dp(5));
        }

        followBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onFollowClicked();
            }
        });

        contentLayout.addView(profileImg);
        contentLayout.addView(nameText);
        contentLayout.addView(titleText);
        contentLayout.addView(statsText);
        contentLayout.addView(followBtn);

        middleContainer.addView(contentLayout);
        addView(middleContainer);
    }

    private void addCircles() {
        View c1 = new View(context);
        FrameLayout.LayoutParams c1Lp = new FrameLayout.LayoutParams(dp(200), dp(200));
        c1Lp.setMargins(-dp(100), -dp(100), 0, 0);
        c1.setLayoutParams(c1Lp);
        ShapeDrawable sd1 = new ShapeDrawable(new OvalShape());
        sd1.getPaint().setColor(Color.rgb(90, 140, 240));
        c1.setBackground(sd1);
        middleContainer.addView(c1);

        View c2 = new View(context);
        FrameLayout.LayoutParams c2Lp = new FrameLayout.LayoutParams(dp(40), dp(40));
        c2Lp.setMargins(dp(90), dp(110), 0, 0);
        c2.setLayoutParams(c2Lp);
        ShapeDrawable sd2 = new ShapeDrawable(new OvalShape());
        sd2.getPaint().setColor(Color.rgb(90, 140, 240));
        c2.setBackground(sd2);
        middleContainer.addView(c2);

        View c3 = new View(context);
        FrameLayout.LayoutParams c3Lp = new FrameLayout.LayoutParams(dp(200), dp(200));
        c3Lp.setMargins(-dp(100), dp(310), 0, 0);
        c3.setLayoutParams(c3Lp);
        ShapeDrawable sd3 = new ShapeDrawable(new OvalShape());
        sd3.getPaint().setColor(Color.argb(191, 90, 140, 240)); // 0.75 opacity approx
        c3.setBackground(sd3);
        middleContainer.addView(c3);
    }

    private void initBottomBar() {
        FrameLayout bottom = new FrameLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dp(300), dp(30));
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        bottom.setLayoutParams(lp);

        GradientDrawable botBg = new GradientDrawable();
        botBg.setColor(Color.rgb(80, 80, 87));
        botBg.setCornerRadii(new float[] { dp(3), dp(3), dp(3), dp(3), dp(15), dp(15), dp(15), dp(15) });
        bottom.setBackground(botBg);

        bottom.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleOpen();
            }
        });

        addView(bottom);
    }

    private void startLightAnimation() {
        lightAnimator = ValueAnimator.ofObject(new ArgbEvaluator(),
                Color.rgb(10, 10, 15),
                Color.rgb(50, 100, 200),
                Color.rgb(10, 60, 160),
                Color.rgb(90, 140, 240));
        lightAnimator.setDuration(1500);
        lightAnimator.setStartDelay(2000);
        lightAnimator.setRepeatCount(ValueAnimator.INFINITE);
        lightAnimator.setRepeatMode(ValueAnimator.REVERSE);
        lightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                GradientDrawable bg = (GradientDrawable) lightView.getBackground();
                bg.setColor((int) animation.getAnimatedValue());
            }
        });
        lightAnimator.start();
    }

    private void toggleOpen() {
        if (openAnimator != null && openAnimator.isRunning()) {
            openAnimator.cancel();
        }

        final int startHeight = middleContainer.getHeight();
        final int endHeight = isOpen ? 0 : maxHoloHeight;
        final float startAlpha = middleContainer.getAlpha();
        final float endAlpha = isOpen ? 0f : 0.9f;

        openAnimator = ValueAnimator.ofFloat(0f, 1f);
        openAnimator.setDuration(1000); // 1s
        openAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = middleContainer.getLayoutParams();
                lp.height = (int) (startHeight + (endHeight - startHeight) * fraction);
                middleContainer.setLayoutParams(lp);
                middleContainer.setAlpha(startAlpha + (endAlpha - startAlpha) * fraction);
            }
        });
        openAnimator.start();

        isOpen = !isOpen;

        if (isOpen) {
            startHoloAnimation();
            if (listener != null)
                listener.onOpened();
        } else {
            // Delay cancelling skew animation until close finishes for smoothness
            openAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    if (!isOpen && holoAnimator != null)
                        holoAnimator.cancel();
                }
            });
            if (listener != null)
                listener.onClosed();
        }
    }

    private void startHoloAnimation() {
        if (holoAnimator != null && holoAnimator.isRunning())
            return;

        holoAnimator = ValueAnimator.ofFloat(1f, 0f, 0.5f, -1f, 0.5f, 0f);
        holoAnimator.setDuration(holoDuration);
        holoAnimator.setRepeatCount(ValueAnimator.INFINITE);
        holoAnimator.setInterpolator(new LinearInterpolator());
        holoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float angle = (float) animation.getAnimatedValue();
                middleContainer.setSkewX(angle);
            }
        });
        holoAnimator.start();
    }

    public void open() {
        if (!isOpen)
            toggleOpen();
    }

    public void close() {
        if (isOpen)
            toggleOpen();
    }

    public void toggle() {
        toggleOpen();
    }

    public void setProfileName(String text) {
        nameText.setText(text);
    }

    public void setTitle(String text) {
        titleText.setText(text);
    }

    public void setStats(String text) {
        statsText.setText(text);
    }

    public void setButtonText(String text) {
        followBtn.setText(text);
    }

    public void setHologramSpeed(int durationMs) {
        this.holoDuration = durationMs;
        if (holoAnimator != null && holoAnimator.isRunning()) {
            holoAnimator.setDuration(durationMs);
        }
    }

    public void setHologramColors(int startColor, int endColor) {
        if (middleContainer != null && middleContainer.getBackground() instanceof GradientDrawable) {
            GradientDrawable gd = (GradientDrawable) middleContainer.getBackground();
            gd.setColors(new int[]{startColor, endColor});
            
            if (middleContainer.getChildCount() > 2) {
                View c1 = middleContainer.getChildAt(0);
                View c2 = middleContainer.getChildAt(1);
                View c3 = middleContainer.getChildAt(2);
                if(c1.getBackground() instanceof ShapeDrawable) ((ShapeDrawable) c1.getBackground()).getPaint().setColor(endColor);
                if(c2.getBackground() instanceof ShapeDrawable) ((ShapeDrawable) c2.getBackground()).getPaint().setColor(endColor);
                if(c3.getBackground() instanceof ShapeDrawable) ((ShapeDrawable) c3.getBackground()).getPaint().setColor(Color.argb(191, Color.red(endColor), Color.green(endColor), Color.blue(endColor)));
            }
        }
    }

    public void setProjectorColor(int color) {
        if (getChildCount() > 2) {
            View top = getChildAt(0);
            if (top != null && top.getBackground() instanceof GradientDrawable) {
                ((GradientDrawable) top.getBackground()).setColor(color);
            }
            View bottom = getChildAt(2);
            if (bottom != null && bottom.getBackground() instanceof GradientDrawable) {
                ((GradientDrawable) bottom.getBackground()).setColor(color);
            }
        }
    }

    public void setButtonColors(int startColor, int endColor) {
        if (followBtn != null && followBtn.getBackground() instanceof GradientDrawable) {
            GradientDrawable gd = (GradientDrawable) followBtn.getBackground();
            gd.setColors(new int[]{startColor, endColor});
        }
    }

    public void setTextColor(int nameColor, int titleColor) {
        if (nameText != null) nameText.setTextColor(nameColor);
        if (titleText != null) titleText.setTextColor(titleColor);
    }

    public void setStatsColor(int color) {
        if (statsText != null) statsText.setTextColor(color);
    }

    @SuppressWarnings("deprecation")
    public void loadProfileImage(final String urlString) {
        if (urlString == null || urlString.isEmpty())
            return;

        if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        InputStream in = new URL(urlString).openStream();
                        return BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("HologramLayout", "Image load error", e);
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) {
                        profileImg.setImageBitmap(bitmap);
                    }
                }
            }.execute();
        } else {
            // Local asset loading via CompanionHelper
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return companionHelper != null ? companionHelper.loadImageFromAssets(urlString) : null;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) {
                        profileImg.setImageBitmap(bitmap);
                    } else {
                        Log.e("HologramLayout", "Failed to load local asset: " + urlString);
                    }
                }
            }.execute();
        }
    }

    public void setBackgroundGradient(int startColor, int endColor) {
        GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{startColor, endColor});
        setBackground(bg);
    }

    @SuppressWarnings("deprecation")
    public void setBackgroundImage(final String urlString) {
        if (urlString == null || urlString.isEmpty()) {
            return;
        }

        if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        InputStream in = new URL(urlString).openStream();
                        return BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("HologramLayout", "Bg load error", e);
                        return null;
                    }
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) setBackground(new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap));
                }
            }.execute();
        } else {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return companionHelper != null ? companionHelper.loadImageFromAssets(urlString) : null;
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) setBackground(new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap));
                }
            }.execute();
        }
    }

    @SuppressWarnings("deprecation")
    public void setHologramBackgroundImage(final String urlString) {
        if (urlString == null || urlString.isEmpty()) {
            return;
        }

        if (urlString.startsWith("http://") || urlString.startsWith("https://")) {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    try {
                        InputStream in = new URL(urlString).openStream();
                        return BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("HologramLayout", "Hologram Bg load error", e);
                        return null;
                    }
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) middleContainer.setBackground(new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap));
                }
            }.execute();
        } else {
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... voids) {
                    return companionHelper != null ? companionHelper.loadImageFromAssets(urlString) : null;
                }
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    if (bitmap != null) middleContainer.setBackground(new android.graphics.drawable.BitmapDrawable(context.getResources(), bitmap));
                }
            }.execute();
        }
    }

    private int dp(float px) {
        return (int) (px * context.getResources().getDisplayMetrics().density);
    }

    private class SkewFrameLayout extends FrameLayout {
        private float skewX = 0f;

        public SkewFrameLayout(Context context) {
            super(context);
        }

        public void setSkewX(float angleDegrees) {
            this.skewX = (float) Math.tan(Math.toRadians(angleDegrees));
            invalidate();
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (skewX != 0f) {
                canvas.save();
                Matrix matrix = new Matrix();
                matrix.setSkew(skewX, 0, getWidth() / 2f, getHeight() / 2f);
                canvas.concat(matrix);
                super.dispatchDraw(canvas);
                canvas.restore();
            } else {
                super.dispatchDraw(canvas);
            }
        }
    }
}
