package com.zero.ci.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * ## 功能 ##
 * <p>
 * |methods|description
 * |---|---|
 * |enableAnimation|开启或关闭点击动画(文字放大效果和图片移动效果)。 默认为 true.
 * |enableItemShiftingMode|开始或关闭子菜单位移模式。 如果为 true，除了当前选中项，其他项的文本将会隐藏。 当菜单数大于3时，默认为 true。
 * |enableShiftingMode|开始或关闭导航条位移模式。如果为 true，选中项和其他项的宽度不一样。当菜单数大于3时，默认为 true。
 * |getBottomNavigationItemView|获取位于 position 的私有成员变量 mButton。
 * |getBottomNavigationItemViews|获取私有成员变量 mButtons。
 * |getCurrentItem|获取当前选中项的索引。
 * |getIconAt|获取位于 position 的图片。
 * |getItemCount|获取子项个数。
 * |getItemHeight|获取菜单高度。
 * |getLargeLabelAt|获取位于 position 的大标签. 每个子项包含两个标签，一个大的，一个小的。
 * |getSmallLabelAt|获取位于 position 的小标签. 每个子项包含两个标签，一个大的，一个小的。
 * |getMenuItemPosition|获取子菜单的索引。如果找不到，返回 -1。
 * |getOnNavigationItemSelectedListener|获取 OnNavigationItemSelectedListener。
 * |setCurrentItem|设置当前选中项。
 * |setIconMarginTop|设置 icon 的 MarginTop，用于调节图标垂直位置。
 * |setIconSize|设置所有的子项图标大小。
 * |setIconSizeAt|设置位于 position 的图标的大小。
 * |setIconsMarginTop|设置所有 icon 的 MarginTop，用于调节图标垂直位置。
 * |setIconTintList| 设置图片的渲染颜色列表(Selector)
 * |setIconVisibility|设置图片可见性。
 * |setItemBackground| 设置子项的背景。
 * |setItemHeight|设置子项高度。
 * |setLargeTextSize|设置所有子项的大标签文本大小。每个子项有两个标签，一个大的，一个小的。当子项未选中时，显示小标签；选中时，显示大标签。
 * |setSmallTextSize|设置所有子项的小标签文本大小。每个子项有两个标签，一个大的，一个小的。当子项未选中时，显示小标签；选中时，显示大标签。
 * |setTextSize|设置所有子项的大和小标签文本大小。
 * |setTextTintList|设置子项 TextView 的颜色。
 * |setTextVisibility|设置文本可见性。
 * |setTypeface|设置所有子项的 TextView 字体
 * |setupWithViewPager|和 ViewPager 绑定，当 任何一个选中项改变时，都会自动改变另一项。
 * <p>
 * 在 `xml` 布局中添加自定义控件:
 * ```xml
 * <com.zero.ci.navigation.BottomNavigationViewEx
 * android:id="@+id/bnve"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_alignParentBottom="true"
 * android:background="@color/colorPrimary"
 * app:itemIconTint="@color/selector_item_color"
 * app:itemTextColor="@color/selector_item_color"
 * app:menu="@menu/menu_navigation_with_view_pager" />
 * ```
 * <p>
 * 在 `Activity` 中绑定控件:
 * ```java
 * BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
 * ```
 * <p>
 * #### 禁止所有动画效果 ####
 * ```java
 * bnve.enableAnimation(false);
 * bnve.enableShiftingMode(false);
 * bnve.enableItemShiftingMode(false);
 * ```
 * <p>
 * #### 自定义图标和文本大小 ####
 * ```java
 * bnve.setIconSize(widthDp, heightDp);
 * bnve.setTextSize(sp);
 * ```
 * <p>
 * #### 和 ViewPager 绑定####
 * ```java
 * // set adapter
 * adapter = new VpAdapter(getSupportFragmentManager(), fragments);
 * bind.vp.setAdapter(adapter);
 * <p>
 * // binding with ViewPager
 * bind.bnve.setupWithViewPager(bind.vp);
 * ```
 * #### 添加带数字的小红点 ####
 * <p>
 * 1. Gradle 中加入 badge 库的依赖
 * ```
 * compile 'q.rorbin:badgeview:1.1.0'
 * ```
 * 2. 和底部控件绑定
 * ```
 * // add badge
 * addBadgeAt(2, 1);
 * <p>
 * private Badge addBadgeAt(int position, int number) {
 * // add badge
 * myreturn new QBadgeView(this)
 * .setBadgeNumber(number)
 * .setGravityOffset(12, 2, true)
 * .bindTarget(bind.bnve.getBottomNavigationItemView(position))
 * .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
 *
 * @Override public void onDragStateChanged(int dragState, Badge badge, View targetView) {
 * if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
 * Toast.makeText(BadgeViewActivity.this, R.string.tips_badge_removed, Toast.LENGTH_SHORT).show();
 * }
 * });
 * }
 */
public class BottomNavigationViewEx extends BottomNavigationViewInner {

    public BottomNavigationViewEx(Context context) {
        super(context);
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigationViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public BottomNavigationViewInner setIconVisibility(boolean visibility) {
        try {
            return super.setIconVisibility(visibility);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTextVisibility(boolean visibility) {
        try {
            return super.setTextVisibility(visibility);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableAnimation(boolean enable) {
        try {
            return super.enableAnimation(enable);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableShiftingMode(boolean enable) {
        try {
            return super.enableShiftingMode(enable);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableItemShiftingMode(boolean enable) {
        try {
            return super.enableItemShiftingMode(enable);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public int getCurrentItem() {
        try {
            return super.getCurrentItem();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getMenuItemPosition(MenuItem item) {
        try {
            return super.getMenuItemPosition(item);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public BottomNavigationViewInner setCurrentItem(int index) {
        try {
            return super.setCurrentItem(index);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        try {
            return super.getOnNavigationItemSelectedListener();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener listener) {
        try {
            super.setOnNavigationItemSelectedListener(listener);
        } catch (Exception e) {
        }
    }

    @Override
    public BottomNavigationMenuView getBottomNavigationMenuView() {
        return super.getBottomNavigationMenuView();
    }

    @Override
    public BottomNavigationViewInner clearIconTintColor() {
        try {
            return super.clearIconTintColor();
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationItemView[] getBottomNavigationItemViews() {
        try {
            return super.getBottomNavigationItemViews();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public BottomNavigationItemView getBottomNavigationItemView(int position) {
        try {
            return super.getBottomNavigationItemView(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ImageView getIconAt(int position) {
        try {
            return super.getIconAt(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public TextView getSmallLabelAt(int position) {
        try {
            return super.getSmallLabelAt(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public TextView getLargeLabelAt(int position) {
        try {
            return super.getLargeLabelAt(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        try {
            return super.getItemCount();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public BottomNavigationViewInner setSmallTextSize(float sp) {
        try {
            return super.setSmallTextSize(sp);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setLargeTextSize(float sp) {
        try {
            return super.setLargeTextSize(sp);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTextSize(float sp) {
        try {
            return super.setTextSize(sp);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconSizeAt(int position, float width, float height) {
        try {
            return super.setIconSizeAt(position, width, height);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconSize(float width, float height) {
        try {
            return super.setIconSize(width, height);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconSize(float dpSize) {
        try {
            return super.setIconSize(dpSize);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setItemHeight(int height) {
        try {
            return super.setItemHeight(height);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public int getItemHeight() {
        try {
            return super.getItemHeight();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public BottomNavigationViewInner setTypeface(Typeface typeface, int style) {
        try {
            return super.setTypeface(typeface, style);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTypeface(Typeface typeface) {
        try {
            return super.setTypeface(typeface);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setupWithViewPager(ViewPager viewPager) {
        try {
            return super.setupWithViewPager(viewPager);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setupWithViewPager(ViewPager viewPager, boolean smoothScroll) {
        try {
            return super.setupWithViewPager(viewPager, smoothScroll);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner enableShiftingMode(int position, boolean enable) {
        try {
            return super.enableShiftingMode(position, enable);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setItemBackground(int position, int background) {
        try {
            return super.setItemBackground(position, background);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconTintList(int position, ColorStateList tint) {
        try {
            return super.setIconTintList(position, tint);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setTextTintList(int position, ColorStateList tint) {
        try {
            return super.setTextTintList(position, tint);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconsMarginTop(int marginTop) {
        try {
            return super.setIconsMarginTop(marginTop);
        } catch (Exception e) {
            return this;
        }
    }

    @Override
    public BottomNavigationViewInner setIconMarginTop(int position, int marginTop) {
        try {
            return super.setIconMarginTop(position, marginTop);
        } catch (Exception e) {
            return this;
        }
    }
}