package com.zero.ci.network.zrequest.conver.loading;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zero.ci.R;
import com.zero.ci.network.zrequest.conver.INetDialog;


public class ZLoadingDialog implements INetDialog {
    private int mLoadingBuilderColor;
    private String mHintText;
    private float mHintTextSize = -1;
    private int mHintTextColor = -1;
    private boolean mCancelable = true;
    private boolean mCanceledOnTouchOutside = true;
    private double mDurationTimePercent = 1.0f;
    private int mDialogBackgroundColor = -1;
    private Dialog mZLoadingDialog;
    Context mContext;

    public ZLoadingDialog(Context context) {
        mContext = context;
    }

    public ZLoadingDialog setLoadingColor(@ColorInt int color) {
        this.mLoadingBuilderColor = color;
        return this;
    }

    public ZLoadingDialog setHintText(String text) {
        this.mHintText = text;
        return this;
    }

    /**
     * 设置了大小后，字就不会有动画了。
     *
     * @param size 大小
     * @return
     */
    public ZLoadingDialog setHintTextSize(float size) {
        this.mHintTextSize = size;
        return this;
    }

    public ZLoadingDialog setHintTextColor(@ColorInt int color) {
        this.mHintTextColor = color;
        return this;
    }

    @Override
    public void setCancelable(boolean flag) {
        if (!flag) {
            mZLoadingDialog.setCancelable(false);
            mZLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog,
                                     int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                        dismiss();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    public ZLoadingDialog setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mCanceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public ZLoadingDialog setDurationTime(double percent) {
        this.mDurationTimePercent = percent;
        return this;
    }

    public ZLoadingDialog setDialogBackgroundColor(@ColorInt int color) {
        this.mDialogBackgroundColor = color;
        return this;
    }



    @Override
    public void show() {
        if (mZLoadingDialog != null && !isShowing()) {
            mZLoadingDialog.show();
        }
    }

    @Override
    public void dismiss() {
        if (mZLoadingDialog != null && isShowing()) {
            mZLoadingDialog.dismiss();
        }
    }

    @Override
    public boolean isShowing() {
        return mZLoadingDialog.isShowing();
    }








    @Override
    public void init(Context context) {
        if (context!= null)
            mContext = context;
        mZLoadingDialog = new Dialog(mContext, R.style.alert_dialog);
        View contentView = View.inflate(mContext, R.layout.dialog_z_loading, null);
        LinearLayout zLoadingRootView = (LinearLayout) contentView.findViewById(R.id.z_loading);

        // init color
        if (this.mDialogBackgroundColor != -1) {
            final Drawable drawable = zLoadingRootView.getBackground();
            if (drawable != null) {
                drawable.setAlpha(Color.alpha(this.mDialogBackgroundColor));
                drawable.setColorFilter(this.mDialogBackgroundColor, PorterDuff.Mode.SRC_ATOP);
            }
        }

        ZLoadingView zLoadingView = (ZLoadingView) contentView.findViewById(R.id.z_loading_view);
        ZLoadingTextView zTextView = (ZLoadingTextView) contentView.findViewById(R.id.z_text_view);
        TextView zCustomTextView = (TextView) contentView.findViewById(R.id.z_custom_text_view);
        if (this.mHintTextSize > 0 && !TextUtils.isEmpty(mHintText)) {
            zCustomTextView.setVisibility(View.VISIBLE);
            zCustomTextView.setText(mHintText);
            zCustomTextView.setTextSize(this.mHintTextSize);
            zCustomTextView.setTextColor(this.mHintTextColor == -1 ? this.mLoadingBuilderColor : this.mHintTextColor);
        } else if (!TextUtils.isEmpty(mHintText)) {
            zTextView.setVisibility(View.VISIBLE);
            zTextView.setText(mHintText);
            zTextView.setColorFilter(this.mHintTextColor == -1 ? this.mLoadingBuilderColor : this.mHintTextColor, PorterDuff.Mode.SRC_ATOP);
        }
        zLoadingView.setLoadingBuilder(Z_TYPE.LEAF_ROTATE);
        // 设置间隔百分比
        if (zLoadingView.mZLoadingBuilder != null) {
            zLoadingView.mZLoadingBuilder.setDurationTimePercent(this.mDurationTimePercent);
        }
        zLoadingView.setColorFilter(this.mLoadingBuilderColor, PorterDuff.Mode.SRC_ATOP);
        mZLoadingDialog.setContentView(contentView);
        mZLoadingDialog.setCancelable(this.mCancelable);
        mZLoadingDialog.setCanceledOnTouchOutside(this.mCanceledOnTouchOutside);
    }



    @Override
    public void setMessage(String... msg) {

    }
}
