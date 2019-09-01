package com.zero.ci.base;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.zero.ci.tool.ActManager;
import com.zero.ci.tool.MPermissionUtil;
import java.lang.reflect.ParameterizedType;


/**
 * Author: Zero Yuan
 * Email: zero.yuan.xin@gmail.com
 * Description:
 * -------------------------------
 * 1.
 */

public class BaseActivity<V, P extends BasePresenter<V>> extends AppCompatActivity {
    protected P mPresenter;
   // private BaseNiceDialog mDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);

        ActManager.addActivity(this);
    }

    private void initPresenter() {
        if (this instanceof BaseView &&
                this.getClass().getGenericSuperclass()
                        instanceof ParameterizedType &&
                ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments().length > 0) {
            Class mPresenterClass = (Class) ((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[1];
            try {
                mPresenter = (P) mPresenterClass.newInstance();
               // Logger.d("PresenterClass : "+ mPresenterClass+"   Presenter : "+mPresenter);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (mPresenter != null)
                mPresenter.onAttach(this, (V) this);
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.onDetached();
        super.onDestroy();
        ActManager.addActivity(this);
    }

//
//    /**
//     * 显示loading
//     */
//    public void showLoading() {
//        NiceDialog.init()
//                .setLayoutId(R.layout.dialog_loading_layout)
//                .setConvertListener(new ViewConvertListener() {
//                    @Override
//                    protected void convertView(ViewHolder holder, BaseNiceDialog dialog) {
//                        mDialog = dialog;
//                    }
//                })
//                .setOutCancel(false)
//                .setWidth(48)
//                .setHeight(48)
//                .setShowBottom(false)
//                .show(getSupportFragmentManager());
//    }
//
//    /**
//     * 隐藏loading
//     */
//    public void hideLoading() {
//        if (mDialog != null) {
//            mDialog.dismiss();
//        }
//    }


}
