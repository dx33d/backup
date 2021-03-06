package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R;
import android.support.v7.internal.text.AllCapsTransformationMethod;
import android.support.v7.internal.widget.TintInfo;
import android.support.v7.internal.widget.TintManager;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelper {
    private static final int[] TEXT_APPEARANCE_ATTRS = new int[]{R.attr.textAllCaps};
    private static final int[] VIEW_ATTRS = new int[]{16842804, 16843119, 16843117, 16843120, 16843118};
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableTopTint;
    final TextView mView;

    static AppCompatTextHelper create(TextView textView) {
        if (VERSION.SDK_INT >= 17) {
            return new AppCompatTextHelperV17(textView);
        }
        return new AppCompatTextHelper(textView);
    }

    AppCompatTextHelper(TextView view) {
        this.mView = view;
    }

    /* Access modifiers changed, original: 0000 */
    public void loadFromAttributes(AttributeSet attrs, int defStyleAttr) {
        Context context = this.mView.getContext();
        TintManager tintManager = TintManager.get(context);
        TypedArray a = context.obtainStyledAttributes(attrs, VIEW_ATTRS, defStyleAttr, 0);
        int ap = a.getResourceId(0, -1);
        if (a.hasValue(1)) {
            this.mDrawableLeftTint = new TintInfo();
            this.mDrawableLeftTint.mHasTintList = true;
            this.mDrawableLeftTint.mTintList = tintManager.getTintList(a.getResourceId(1, 0));
        }
        if (a.hasValue(2)) {
            this.mDrawableTopTint = new TintInfo();
            this.mDrawableTopTint.mHasTintList = true;
            this.mDrawableTopTint.mTintList = tintManager.getTintList(a.getResourceId(2, 0));
        }
        if (a.hasValue(3)) {
            this.mDrawableRightTint = new TintInfo();
            this.mDrawableRightTint.mHasTintList = true;
            this.mDrawableRightTint.mTintList = tintManager.getTintList(a.getResourceId(3, 0));
        }
        if (a.hasValue(4)) {
            this.mDrawableBottomTint = new TintInfo();
            this.mDrawableBottomTint.mHasTintList = true;
            this.mDrawableBottomTint.mTintList = tintManager.getTintList(a.getResourceId(4, 0));
        }
        a.recycle();
        if (ap != -1) {
            TypedArray appearance = context.obtainStyledAttributes(ap, R.styleable.TextAppearance);
            if (appearance.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                setAllCaps(appearance.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
            }
            appearance.recycle();
        }
        a = context.obtainStyledAttributes(attrs, TEXT_APPEARANCE_ATTRS, defStyleAttr, 0);
        if (a.getBoolean(0, false)) {
            setAllCaps(true);
        }
        a.recycle();
    }

    /* Access modifiers changed, original: 0000 */
    public void onSetTextAppearance(Context context, int resId) {
        TypedArray appearance = context.obtainStyledAttributes(resId, TEXT_APPEARANCE_ATTRS);
        if (appearance.hasValue(0)) {
            setAllCaps(appearance.getBoolean(0, false));
        }
        appearance.recycle();
    }

    /* Access modifiers changed, original: 0000 */
    public void setAllCaps(boolean allCaps) {
        this.mView.setTransformationMethod(allCaps ? new AllCapsTransformationMethod(this.mView.getContext()) : null);
    }

    /* Access modifiers changed, original: 0000 */
    public void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
    }

    /* Access modifiers changed, original: final */
    public final void applyCompoundDrawableTint(Drawable drawable, TintInfo info) {
        if (drawable != null && info != null) {
            TintManager.tintDrawable(drawable, info, this.mView.getDrawableState());
        }
    }
}
