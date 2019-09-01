package com.zero.ci.refresh.footer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.zero.ci.R;
import com.zero.ci.refresh.api.RefreshFooter;
import com.zero.ci.refresh.api.RefreshLayout;
import com.zero.ci.refresh.api.constant.RefreshState;
import com.zero.ci.refresh.api.constant.SpinnerStyle;
import com.zero.ci.refresh.internal.ArrowDrawable;
import com.zero.ci.refresh.internal.InternalClassics;
import com.zero.ci.refresh.internal.ProgressDrawable;
import com.zero.ci.tool.DensityUtil;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:经典上拉加载控件
 * -------------------------------
 */

public class ClassicsFooter extends InternalClassics<ClassicsFooter> implements RefreshFooter {
    public static String REFRESH_FOOTER_PULLING = null;//"上拉加载更多";
    public static String REFRESH_FOOTER_RELEASE = null;//"释放立即加载";
    public static String REFRESH_FOOTER_LOADING = null;//"正在加载...";
    public static String REFRESH_FOOTER_REFRESHING = null;//"正在刷新...";
    public static String REFRESH_FOOTER_FINISH = null;//"加载完成";
    public static String REFRESH_FOOTER_FAILED = null;//"加载失败";
    public static String REFRESH_FOOTER_NOTHING = null;//"没有更多数据了";
    protected boolean isNoMoreData = false;


    public ClassicsFooter(Context context) {
        this(context, null);
    }

    public ClassicsFooter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (REFRESH_FOOTER_PULLING == null) {
            REFRESH_FOOTER_PULLING = context.getString(R.string.zrl_footer_pulling);
        }

        if (REFRESH_FOOTER_RELEASE == null) {
            REFRESH_FOOTER_RELEASE = context.getString(R.string.zrl_footer_release);
        }

        if (REFRESH_FOOTER_LOADING == null) {
            REFRESH_FOOTER_LOADING = context.getString(R.string.zrl_footer_loading);
        }

        if (REFRESH_FOOTER_REFRESHING == null) {
            REFRESH_FOOTER_REFRESHING = context.getString(R.string.zrl_footer_refreshing);
        }

        if (REFRESH_FOOTER_FINISH == null) {
            REFRESH_FOOTER_FINISH = context.getString(R.string.zrl_footer_finish);
        }

        if (REFRESH_FOOTER_FAILED == null) {
            REFRESH_FOOTER_FAILED = context.getString(R.string.zrl_footer_failed);
        }

        if (REFRESH_FOOTER_NOTHING == null) {
            REFRESH_FOOTER_NOTHING = context.getString(R.string.zrl_footer_nothing);
        }

        final View thisView = this;
        final View arrowView = mArrowIv;
        final View progressView = mProgressIv;
        final DensityUtil density = new DensityUtil();

        mTitleTv.setTextColor(0xff666666);
        mTitleTv.setText(thisView.isInEditMode() ? REFRESH_FOOTER_LOADING : REFRESH_FOOTER_PULLING);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsFooter);

        LayoutParams lpArrow = (LayoutParams) arrowView.getLayoutParams();
        LayoutParams lpProgress = (LayoutParams) progressView.getLayoutParams();
        lpProgress.rightMargin = ta.getDimensionPixelSize(R.styleable.ClassicsFooter_zrlDrawableMarginRight, density.dip2px(20));
        lpArrow.rightMargin = lpProgress.rightMargin;

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableArrowSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableArrowSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableProgressSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableProgressSize, lpProgress.height);

        lpArrow.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableSize, lpArrow.width);
        lpArrow.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableSize, lpArrow.height);
        lpProgress.width = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableSize, lpProgress.width);
        lpProgress.height = ta.getLayoutDimension(R.styleable.ClassicsFooter_zrlDrawableSize, lpProgress.height);

        mFinishDuration = ta.getInt(R.styleable.ClassicsFooter_zrlFinishDuration, mFinishDuration);
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.ClassicsFooter_zrlClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        if (ta.hasValue(R.styleable.ClassicsFooter_zrlDrawableArrow)) {
            mArrowIv.setImageDrawable(ta.getDrawable(R.styleable.ClassicsFooter_zrlDrawableArrow));
        } else {
            mArrowDrawable = new ArrowDrawable();
            mArrowDrawable.setColor(0xff666666);
            mArrowIv.setImageDrawable(mArrowDrawable);
        }

        if (ta.hasValue(R.styleable.ClassicsFooter_zrlDrawableProgress)) {
            mProgressIv.setImageDrawable(ta.getDrawable(R.styleable.ClassicsFooter_zrlDrawableProgress));
        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressIv.setImageDrawable(mProgressDrawable);
        }

        if (ta.hasValue(R.styleable.ClassicsFooter_zrlTitleTextSize)) {
            mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.ClassicsFooter_zrlTitleTextSize, DensityUtil.dp2px(16)));
        } else {
            mTitleTv.setTextSize(16);
        }

        if (ta.hasValue(R.styleable.ClassicsFooter_zrlPrimaryColor)) {
            setPrimaryColor(ta.getColor(R.styleable.ClassicsFooter_zrlPrimaryColor, 0));
        }
        if (ta.hasValue(R.styleable.ClassicsFooter_zrlAccentColor)) {
            setAccentColor(ta.getColor(R.styleable.ClassicsFooter_zrlAccentColor, 0));
        }

        ta.recycle();

    }

//    @Override
//    protected ClassicsFooter self() {
//        myreturn this;
//    }

    //</editor-fold>

    //<editor-fold desc="RefreshFooter">

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if (!isNoMoreData) {
            super.onStartAnimator(refreshLayout, height, maxDragHeight);
        }
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (!isNoMoreData) {
            mTitleTv.setText(success ? REFRESH_FOOTER_FINISH : REFRESH_FOOTER_FAILED);
            return super.onFinish(layout, success);
        }
        return 0;
    }

    /**
     * ClassicsFooter 在(SpinnerStyle.FixedBehind)时才有主题色
     */
    @Override@Deprecated
    public void setPrimaryColors(@ColorInt int ... colors) {
        if (mSpinnerStyle == SpinnerStyle.FixedBehind) {
            super.setPrimaryColors(colors);
        }
    }

    /**
     * 设置数据全部加载完成，将不能再次触发加载功能
     */
    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (isNoMoreData != noMoreData) {
            isNoMoreData = noMoreData;
            final View arrowView = mArrowIv;
            if (noMoreData) {
                mTitleTv.setText(REFRESH_FOOTER_NOTHING);
                arrowView.setVisibility(GONE);
            } else {
                mTitleTv.setText(REFRESH_FOOTER_PULLING);
                arrowView.setVisibility(VISIBLE);
            }
        }
        return true;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowIv;
        if (!isNoMoreData) {
            switch (newState) {
                case None:
                    arrowView.setVisibility(VISIBLE);
                case PullUpToLoad:
                    mTitleTv.setText(REFRESH_FOOTER_PULLING);
                    arrowView.animate().rotation(180);
                    break;
                case Loading:
                case LoadReleased:
                    arrowView.setVisibility(GONE);
                    mTitleTv.setText(REFRESH_FOOTER_LOADING);
                    break;
                case ReleaseToLoad:
                    mTitleTv.setText(REFRESH_FOOTER_RELEASE);
                    arrowView.animate().rotation(0);
                    break;
                case Refreshing:
                    mTitleTv.setText(REFRESH_FOOTER_REFRESHING);
                    arrowView.setVisibility(GONE);
                    break;
            }
        }
    }
    //</editor-fold>

}
