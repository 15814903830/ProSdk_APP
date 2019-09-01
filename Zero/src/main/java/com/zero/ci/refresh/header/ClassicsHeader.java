package com.zero.ci.refresh.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.ci.R;
import com.zero.ci.refresh.api.RefreshHeader;
import com.zero.ci.refresh.api.RefreshLayout;
import com.zero.ci.refresh.api.constant.RefreshState;
import com.zero.ci.refresh.api.constant.SpinnerStyle;
import com.zero.ci.refresh.internal.ArrowDrawable;
import com.zero.ci.refresh.internal.InternalClassics;
import com.zero.ci.refresh.internal.ProgressDrawable;
import com.zero.ci.tool.DensityUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:经典下来刷新
 * -------------------------------
 */

public class ClassicsHeader extends InternalClassics<ClassicsHeader> implements RefreshHeader {
    public static final byte ID_TEXT_UPDATE = 4;

    public static String REFRESH_HEADER_PULLING = null;//"下拉可以刷新";
    public static String REFRESH_HEADER_REFRESHING = null;//"正在刷新...";
    public static String REFRESH_HEADER_LOADING = null;//"正在加载...";
    public static String REFRESH_HEADER_RELEASE = null;//"释放立即刷新";
    public static String REFRESH_HEADER_FINISH = null;//"刷新完成";
    public static String REFRESH_HEADER_FAILED = null;//"刷新失败";
    public static String REFRESH_HEADER_UPDATE = null;//"上次更新 M-d HH:mm";
    public static String REFRESH_HEADER_SECONDARY = null;//"释放进入二楼";

    protected String KEY_LAST_UPDATE_TIME = "LAST_UPDATE_TIME";

    protected Date mLastTime;
    protected TextView mLastUpdateTv;
    protected SharedPreferences mShared;
    protected DateFormat mLastUpdateFormat;
    protected boolean isEnableLastTime = true;

    public ClassicsHeader(Context context) {
        this(context, null);
    }

    public ClassicsHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassicsHeader(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (REFRESH_HEADER_PULLING == null) {
            REFRESH_HEADER_PULLING = context.getString(R.string.zrl_header_pulling);
        }
        if (REFRESH_HEADER_REFRESHING == null) {
            REFRESH_HEADER_REFRESHING = context.getString(R.string.zrl_header_refreshing);
        }
        if (REFRESH_HEADER_LOADING == null) {
            REFRESH_HEADER_LOADING = context.getString(R.string.zrl_header_loading);
        }
        if (REFRESH_HEADER_RELEASE == null) {
            REFRESH_HEADER_RELEASE = context.getString(R.string.zrl_header_release);
        }
        if (REFRESH_HEADER_FINISH == null) {
            REFRESH_HEADER_FINISH = context.getString(R.string.zrl_header_finish);
        }
        if (REFRESH_HEADER_FAILED == null) {
            REFRESH_HEADER_FAILED = context.getString(R.string.zrl_header_failed);
        }
        if (REFRESH_HEADER_UPDATE == null) {
            REFRESH_HEADER_UPDATE = context.getString(R.string.zrl_header_update);
        }
        if (REFRESH_HEADER_SECONDARY == null) {
            REFRESH_HEADER_SECONDARY = context.getString(R.string.zrl_header_secondary);
        }


        mLastUpdateTv = new TextView(context);
        mLastUpdateTv.setTextColor(0xff7c7c7c);

        mLastUpdateFormat = new SimpleDateFormat(REFRESH_HEADER_UPDATE, Locale.getDefault());

        final View thisView = this;
        final View arrowView = mArrowIv;
        final View updateView = mLastUpdateTv;
        final View progressView = mProgressIv;
        final ViewGroup conterLayout = mCenterLayout;
        final DensityUtil density = new DensityUtil();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClassicsHeader);

        LayoutParams lpArrow = (LayoutParams) arrowView.getLayoutParams();
        LayoutParams lpProgress = (LayoutParams) progressView.getLayoutParams();
        LinearLayout.LayoutParams lpUpdateText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpUpdateText.topMargin = typedArray.getDimensionPixelSize(R.styleable.ClassicsHeader_zrlTopTextTimeMargin, density.dip2px(0));
        lpProgress.rightMargin = typedArray.getDimensionPixelSize(R.styleable.ClassicsHeader_zrlDrawableMarginRight, density.dip2px(20));
        lpArrow.rightMargin = lpProgress.rightMargin;

        lpArrow.width = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableArrowSize, lpArrow.width);
        lpArrow.height = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableArrowSize, lpArrow.height);

        lpProgress.width = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableProgressSize, lpProgress.width);
        lpProgress.height = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableProgressSize, lpProgress.height);

        lpArrow.width = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableSize, lpArrow.width);
        lpArrow.height = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableSize, lpArrow.height);

        lpProgress.width = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableSize, lpProgress.width);
        lpProgress.height = typedArray.getLayoutDimension(R.styleable.ClassicsHeader_zrlDrawableSize, lpProgress.height);

        mFinishDuration = typedArray.getInt(R.styleable.ClassicsHeader_zrlFinishDuration, mFinishDuration);
        isEnableLastTime = typedArray.getBoolean(R.styleable.ClassicsHeader_zrlEnableLastTime, isEnableLastTime);
        mSpinnerStyle = SpinnerStyle.values()[typedArray.getInt(R.styleable.ClassicsHeader_zrlClassicsSpinnerStyle, mSpinnerStyle.ordinal())];


        if (typedArray.hasValue(R.styleable.ClassicsFooter_zrlDrawableArrow)) {
            mArrowIv.setImageDrawable(typedArray.getDrawable(R.styleable.ClassicsHeader_zrlDrawableArrow));
        } else {
            mArrowDrawable = new ArrowDrawable();
            mArrowDrawable.setColor(0xff666666);
            mArrowIv.setImageDrawable(mArrowDrawable);
        }


        if (typedArray.hasValue(R.styleable.ClassicsHeader_zrlDrawableProgress)) {
            mProgressIv.setImageDrawable(typedArray.getDrawable(R.styleable.ClassicsHeader_zrlDrawableProgress));
        } else {
            mProgressDrawable = new ProgressDrawable();
            mProgressDrawable.setColor(0xff666666);
            mProgressIv.setImageDrawable(mProgressDrawable);
        }

        if (typedArray.hasValue(R.styleable.ClassicsFooter_zrlTitleTextSize)) {
            mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ClassicsHeader_zrlTimeTextSize, DensityUtil.dp2px(16)));
        } else {
            mTitleTv.setTextSize(16);
        }

        if (typedArray.hasValue(R.styleable.ClassicsHeader_zrlTimeTextSize)) {
            mLastUpdateTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, typedArray.getDimensionPixelSize(R.styleable.ClassicsHeader_zrlTimeTextSize, DensityUtil.dp2px(12)));
        } else {
            mLastUpdateTv.setTextSize(12);
        }

        if (typedArray.hasValue(R.styleable.ClassicsHeader_zrlPrimaryColor)) {
            setPrimaryColor(typedArray.getColor(R.styleable.ClassicsHeader_zrlPrimaryColor, 0));
        }

        if (typedArray.hasValue(R.styleable.ClassicsHeader_zrlAccentColor)) {
            setAccentColor(typedArray.getColor(R.styleable.ClassicsHeader_zrlAccentColor, 0));
    }

        typedArray.recycle();

        updateView.setId(ID_TEXT_UPDATE);
        updateView.setVisibility(isEnableLastTime ? VISIBLE : GONE);
        conterLayout.addView(updateView, lpUpdateText);

        mTitleTv.setText(thisView.isInEditMode() ? REFRESH_HEADER_REFRESHING : REFRESH_HEADER_PULLING);

        try {//try 不能删除-否则会出现兼容性问题
            if (context instanceof FragmentActivity) {
                FragmentManager manager = ((FragmentActivity) context).getSupportFragmentManager();
                if (manager != null) {
                    @SuppressLint("RestrictedApi")
                    List<Fragment> fragments = manager.getFragments();
                    if (fragments != null && fragments.size() > 0) {
                        setLastUpdateTime(new Date());
                        return;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        KEY_LAST_UPDATE_TIME += context.getClass().getName();
        mShared = context.getSharedPreferences("ClassicsHeader", Context.MODE_PRIVATE);
        setLastUpdateTime(new Date(mShared.getLong(KEY_LAST_UPDATE_TIME, System.currentTimeMillis())));

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            mTitleTv.setText(REFRESH_HEADER_FINISH);
            if (mLastTime != null) {
                setLastUpdateTime(new Date());
            }
        } else {
            mTitleTv.setText(REFRESH_HEADER_FAILED);
        }
        return super.onFinish(layout, success);//延迟500毫秒之后再弹回
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        final View arrowView = mArrowIv;
        final View updateView = mLastUpdateTv;
        switch (newState) {
            case None:
                updateView.setVisibility(isEnableLastTime ? VISIBLE : GONE);
            case PullDownToRefresh:
                mTitleTv.setText(REFRESH_HEADER_PULLING);
                arrowView.setVisibility(VISIBLE);
                arrowView.animate().rotation(0);
                break;
            case Refreshing:
            case RefreshReleased:
                mTitleTv.setText(REFRESH_HEADER_REFRESHING);
                arrowView.setVisibility(GONE);
                break;
            case ReleaseToRefresh:
                mTitleTv.setText(REFRESH_HEADER_RELEASE);
                arrowView.animate().rotation(180);
                break;
            case ReleaseToTwoLevel:
                mTitleTv.setText(REFRESH_HEADER_SECONDARY);
                arrowView.animate().rotation(0);
                break;
            case Loading:
                arrowView.setVisibility(GONE);
                updateView.setVisibility(isEnableLastTime ? INVISIBLE : GONE);
                mTitleTv.setText(REFRESH_HEADER_LOADING);
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="API">

    public ClassicsHeader setLastUpdateTime(Date time) {
        final View thisView = this;
        mLastTime = time;
        mLastUpdateTv.setText(mLastUpdateFormat.format(time));
        if (mShared != null && !thisView.isInEditMode()) {
            mShared.edit().putLong(KEY_LAST_UPDATE_TIME, time.getTime()).apply();
        }
        return this;
    }

    public ClassicsHeader setTimeFormat(DateFormat format) {
        mLastUpdateFormat = format;
        if (mLastTime != null) {
            mLastUpdateTv.setText(mLastUpdateFormat.format(mLastTime));
        }
        return this;
    }

    public ClassicsHeader setLastUpdateText(CharSequence text) {
        mLastTime = null;
        mLastUpdateTv.setText(text);
        return this;
    }

    public ClassicsHeader setAccentColor(@ColorInt int accentColor) {
        mLastUpdateTv.setTextColor(accentColor & 0x00ffffff | 0xcc000000);
        return super.setAccentColor(accentColor);
    }

    public ClassicsHeader setEnableLastTime(boolean enable) {
        final View updateView = mLastUpdateTv;
        isEnableLastTime = enable;
        updateView.setVisibility(enable ? VISIBLE : GONE);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return this;
    }

    public ClassicsHeader setTextSizeTime(float size) {
        mLastUpdateTv.setTextSize(size);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestRemeasureHeightFor(this);
        }
        return this;
    }


    public ClassicsHeader setTextTimeMarginTop(float dp) {
        final View updateView = mLastUpdateTv;
        MarginLayoutParams lp = (MarginLayoutParams) updateView.getLayoutParams();
        lp.topMargin = DensityUtil.dp2px(dp);
        updateView.setLayoutParams(lp);
        return this;
    }


}
