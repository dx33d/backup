package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

class FloatingActionButtonEclairMr1 extends FloatingActionButtonImpl {
    private int mAnimationDuration;
    private Drawable mBorderDrawable;
    private float mElevation;
    private boolean mIsHiding;
    private float mPressedTranslationZ;
    private Drawable mRippleDrawable;
    ShadowDrawableWrapper mShadowDrawable;
    private Drawable mShapeDrawable;
    private StateListAnimator mStateListAnimator = new StateListAnimator();

    private abstract class BaseShadowAnimation extends Animation {
        private float mShadowSizeDiff;
        private float mShadowSizeStart;

        public abstract float getTargetShadowSize();

        private BaseShadowAnimation() {
        }

        /* synthetic */ BaseShadowAnimation(FloatingActionButtonEclairMr1 x0, AnonymousClass1 x1) {
            this();
        }

        public void reset() {
            super.reset();
            this.mShadowSizeStart = FloatingActionButtonEclairMr1.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeDiff = getTargetShadowSize() - this.mShadowSizeStart;
        }

        /* Access modifiers changed, original: protected */
        public void applyTransformation(float interpolatedTime, Transformation t) {
            FloatingActionButtonEclairMr1.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + (this.mShadowSizeDiff * interpolatedTime));
        }
    }

    private class ElevateToTranslationZAnimation extends BaseShadowAnimation {
        private ElevateToTranslationZAnimation() {
            super(FloatingActionButtonEclairMr1.this, null);
        }

        /* synthetic */ ElevateToTranslationZAnimation(FloatingActionButtonEclairMr1 x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation + FloatingActionButtonEclairMr1.this.mPressedTranslationZ;
        }
    }

    private class ResetElevationAnimation extends BaseShadowAnimation {
        private ResetElevationAnimation() {
            super(FloatingActionButtonEclairMr1.this, null);
        }

        /* synthetic */ ResetElevationAnimation(FloatingActionButtonEclairMr1 x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation;
        }
    }

    FloatingActionButtonEclairMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        this.mAnimationDuration = view.getResources().getInteger(17694720);
        this.mStateListAnimator.setTarget(view);
        this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, setupAnimation(new ElevateToTranslationZAnimation(this, null)));
        this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, setupAnimation(new ElevateToTranslationZAnimation(this, null)));
        this.mStateListAnimator.addState(EMPTY_STATE_SET, setupAnimation(new ResetElevationAnimation(this, null)));
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundDrawable(Drawable originalBackground, ColorStateList backgroundTint, Mode backgroundTintMode, int rippleColor, int borderWidth) {
        Drawable[] layers;
        this.mShapeDrawable = DrawableCompat.wrap(mutateDrawable(originalBackground));
        DrawableCompat.setTintList(this.mShapeDrawable, backgroundTint);
        if (backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, backgroundTintMode);
        }
        GradientDrawable touchFeedbackShape = new GradientDrawable();
        touchFeedbackShape.setShape(1);
        touchFeedbackShape.setColor(-1);
        touchFeedbackShape.setCornerRadius(this.mShadowViewDelegate.getRadius());
        this.mRippleDrawable = DrawableCompat.wrap(touchFeedbackShape);
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(rippleColor));
        DrawableCompat.setTintMode(this.mRippleDrawable, Mode.MULTIPLY);
        if (borderWidth > 0) {
            this.mBorderDrawable = createBorderDrawable(borderWidth, backgroundTint);
            layers = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
        } else {
            this.mBorderDrawable = null;
            layers = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
        }
        this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getResources(), new LayerDrawable(layers), this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ);
        this.mShadowDrawable.setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
        updatePadding();
    }

    private static Drawable mutateDrawable(Drawable drawable) {
        return (VERSION.SDK_INT >= 14 || !(drawable instanceof GradientDrawable)) ? drawable.mutate() : drawable;
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundTintList(ColorStateList tint) {
        DrawableCompat.setTintList(this.mShapeDrawable, tint);
        if (this.mBorderDrawable != null) {
            DrawableCompat.setTintList(this.mBorderDrawable, tint);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundTintMode(Mode tintMode) {
        DrawableCompat.setTintMode(this.mShapeDrawable, tintMode);
    }

    /* Access modifiers changed, original: 0000 */
    public void setRippleColor(int rippleColor) {
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(rippleColor));
    }

    /* Access modifiers changed, original: 0000 */
    public void setElevation(float elevation) {
        if (this.mElevation != elevation && this.mShadowDrawable != null) {
            this.mShadowDrawable.setShadowSize(elevation, this.mPressedTranslationZ + elevation);
            this.mElevation = elevation;
            updatePadding();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setPressedTranslationZ(float translationZ) {
        if (this.mPressedTranslationZ != translationZ && this.mShadowDrawable != null) {
            this.mPressedTranslationZ = translationZ;
            this.mShadowDrawable.setMaxShadowSize(this.mElevation + translationZ);
            updatePadding();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onDrawableStateChanged(int[] state) {
        this.mStateListAnimator.setState(state);
    }

    /* Access modifiers changed, original: 0000 */
    public void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }

    /* Access modifiers changed, original: 0000 */
    public void hide(@Nullable final InternalVisibilityChangedListener listener) {
        if (!this.mIsHiding && this.mView.getVisibility() == 0) {
            Animation anim = AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.design_fab_out);
            anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(200);
            anim.setAnimationListener(new AnimationListenerAdapter() {
                public void onAnimationStart(Animation animation) {
                    FloatingActionButtonEclairMr1.this.mIsHiding = true;
                }

                public void onAnimationEnd(Animation animation) {
                    FloatingActionButtonEclairMr1.this.mIsHiding = false;
                    FloatingActionButtonEclairMr1.this.mView.setVisibility(8);
                    if (listener != null) {
                        listener.onHidden();
                    }
                }
            });
            this.mView.startAnimation(anim);
        } else if (listener != null) {
            listener.onHidden();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void show(@Nullable final InternalVisibilityChangedListener listener) {
        if (this.mView.getVisibility() != 0 || this.mIsHiding) {
            this.mView.clearAnimation();
            this.mView.setVisibility(0);
            Animation anim = AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.design_fab_in);
            anim.setDuration(200);
            anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setAnimationListener(new AnimationListenerAdapter() {
                public void onAnimationEnd(Animation animation) {
                    if (listener != null) {
                        listener.onShown();
                    }
                }
            });
            this.mView.startAnimation(anim);
        } else if (listener != null) {
            listener.onShown();
        }
    }

    private void updatePadding() {
        Rect rect = new Rect();
        this.mShadowDrawable.getPadding(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private Animation setupAnimation(Animation animation) {
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration((long) this.mAnimationDuration);
        return animation;
    }

    private static ColorStateList createColorStateList(int selectedColor) {
        states = new int[3][];
        int[] colors = new int[3];
        states[0] = FOCUSED_ENABLED_STATE_SET;
        colors[0] = selectedColor;
        int i = 0 + 1;
        states[i] = PRESSED_ENABLED_STATE_SET;
        colors[i] = selectedColor;
        i++;
        states[i] = new int[0];
        colors[i] = 0;
        i++;
        return new ColorStateList(states, colors);
    }
}
