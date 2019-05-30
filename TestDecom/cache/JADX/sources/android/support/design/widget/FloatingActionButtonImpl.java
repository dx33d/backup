package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.view.View;

abstract class FloatingActionButtonImpl {
    static final int[] EMPTY_STATE_SET = new int[0];
    static final int[] FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
    static final int[] PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
    static final int SHOW_HIDE_ANIM_DURATION = 200;
    final ShadowViewDelegate mShadowViewDelegate;
    final View mView;

    interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    public abstract void hide(@Nullable InternalVisibilityChangedListener internalVisibilityChangedListener);

    public abstract void jumpDrawableToCurrentState();

    public abstract void onDrawableStateChanged(int[] iArr);

    public abstract void setBackgroundDrawable(Drawable drawable, ColorStateList colorStateList, Mode mode, int i, int i2);

    public abstract void setBackgroundTintList(ColorStateList colorStateList);

    public abstract void setBackgroundTintMode(Mode mode);

    public abstract void setElevation(float f);

    public abstract void setPressedTranslationZ(float f);

    public abstract void setRippleColor(int i);

    public abstract void show(@Nullable InternalVisibilityChangedListener internalVisibilityChangedListener);

    FloatingActionButtonImpl(View view, ShadowViewDelegate shadowViewDelegate) {
        this.mView = view;
        this.mShadowViewDelegate = shadowViewDelegate;
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable createBorderDrawable(int borderWidth, ColorStateList backgroundTint) {
        Resources resources = this.mView.getResources();
        CircularBorderDrawable borderDrawable = newCircularDrawable();
        borderDrawable.setGradientColors(resources.getColor(R.color.design_fab_stroke_top_outer_color), resources.getColor(R.color.design_fab_stroke_top_inner_color), resources.getColor(R.color.design_fab_stroke_end_inner_color), resources.getColor(R.color.design_fab_stroke_end_outer_color));
        borderDrawable.setBorderWidth((float) borderWidth);
        borderDrawable.setTintColor(backgroundTint.getDefaultColor());
        return borderDrawable;
    }

    /* Access modifiers changed, original: 0000 */
    public CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }
}
