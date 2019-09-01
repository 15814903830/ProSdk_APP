package com.zero.ci.base.adapter.listener;

import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import com.zero.ci.base.adapter.BaseRVAdapter;
import com.zero.ci.base.adapter.BaseViewHolder;

import java.util.Set;


import static com.zero.ci.base.adapter.BaseRVAdapter.EMPTY_VIEW;
import static com.zero.ci.base.adapter.BaseRVAdapter.FOOTER_VIEW;
import static com.zero.ci.base.adapter.BaseRVAdapter.HEADER_VIEW;
import static com.zero.ci.base.adapter.BaseRVAdapter.LOADING_VIEW;


/**
 * This can be useful for applications that wish to implement various forms of click and longclick and childView click
 * manipulation of item views within the RecyclerView. SimpleClickListener may intercept
 * a touch interaction already in progress even if the SimpleClickListener is already handling that
 * gesture stream itself for the purposes of scrolling.
 *
 * @see RecyclerView.OnItemTouchListener
 */
public abstract class SimpleClickListener implements RecyclerView.OnItemTouchListener {
    public static String TAG = "SimpleClickListener";

    private GestureDetectorCompat mGestureDetector;
    private RecyclerView recyclerView;
    private BaseRVAdapter mBaseRVAdapter;
    private boolean isRepressed = false;
    private boolean isShowPress = false;
    private View mPressedView = null;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (recyclerView == null) {
            this.recyclerView = rv;
            this.mBaseRVAdapter = (BaseRVAdapter) recyclerView.getAdapter();
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener(recyclerView));
        } else if (recyclerView != rv) {
            this.recyclerView = rv;
            this.mBaseRVAdapter = (BaseRVAdapter) recyclerView.getAdapter();
            mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener(recyclerView));
        }
        if (!mGestureDetector.onTouchEvent(e) && e.getActionMasked() == MotionEvent.ACTION_UP && isShowPress) {
            if (mPressedView != null) {
                BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(mPressedView);
                if (vh == null || !isHeaderOrFooterView(vh.getItemViewType())) {
                    mPressedView.setPressed(false);
                }
            }
            isShowPress = false;
            isRepressed = false;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    private class ItemTouchHelperGestureListener implements GestureDetector.OnGestureListener {

        private RecyclerView recyclerView;

        @Override
        public boolean onDown(MotionEvent e) {
            isRepressed = true;
            mPressedView = recyclerView.findChildViewUnder(e.getX(), e.getY());
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            if (isRepressed && mPressedView != null) {
//                mPressedView.setPressed(true);
                isShowPress = true;
            }
        }

        ItemTouchHelperGestureListener(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isRepressed && mPressedView != null) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    return false;
                }
                final View pressedView = mPressedView;
                BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(pressedView);

                if (isHeaderOrFooterPosition(vh.getLayoutPosition())) {
                    return false;
                }
                Set<Integer> childClickViewIds = vh.getChildClickViewIds();
                Set<Integer> nestViewIds = vh.getNestViews();
                if (childClickViewIds != null && childClickViewIds.size() > 0) {
                    for (Integer childClickViewId : childClickViewIds) {
                        View childView = pressedView.findViewById(childClickViewId);
                        if (childView != null) {
                            if (inRangeOfView(childView, e) && childView.isEnabled()) {
                                if (nestViewIds != null && nestViewIds.contains(childClickViewId)) {
                                    return false;
                                }
                                setPressViewHotSpot(e, childView);
                                childView.setPressed(true);
                                onItemChildClick(mBaseRVAdapter, childView, vh.getLayoutPosition() - mBaseRVAdapter.getHeaderLayoutCount());
                                resetPressedView(childView);
                                return true;
                            } else {
                                childView.setPressed(false);
                            }
                        }
                    }
                    setPressViewHotSpot(e, pressedView);
                    mPressedView.setPressed(true);
                    for (Integer childClickViewId : childClickViewIds) {
                        View childView = pressedView.findViewById(childClickViewId);
                        if (childView != null) {
                            childView.setPressed(false);
                        }
                    }
                    onItemClick(mBaseRVAdapter, pressedView, vh.getLayoutPosition() - mBaseRVAdapter.getHeaderLayoutCount());
                } else {
                    setPressViewHotSpot(e, pressedView);
                    mPressedView.setPressed(true);
                    if (childClickViewIds != null && childClickViewIds.size() > 0) {
                        for (Integer childClickViewId : childClickViewIds) {
                            View childView = pressedView.findViewById(childClickViewId);
                            if (childView != null) {
                                childView.setPressed(false);
                            }
                        }
                    }
                    onItemClick(mBaseRVAdapter, pressedView, vh.getLayoutPosition() - mBaseRVAdapter.getHeaderLayoutCount());
                }
                resetPressedView(pressedView);

            }
            return true;
        }

        private void resetPressedView(final View pressedView) {
            if (pressedView != null) {
                pressedView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (pressedView != null) {
                            pressedView.setPressed(false);
                        }

                    }
                }, 50);
            }

            isRepressed = false;
            mPressedView = null;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            boolean isChildLongClick = false;
            if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                return;
            }
            if (isRepressed && mPressedView != null) {
                mPressedView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                BaseViewHolder vh = (BaseViewHolder) recyclerView.getChildViewHolder(mPressedView);
                if (!isHeaderOrFooterPosition(vh.getLayoutPosition())) {
                    Set<Integer> longClickViewIds = vh.getItemChildLongClickViewIds();
                    Set<Integer> nestViewIds = vh.getNestViews();
                    if (longClickViewIds != null && longClickViewIds.size() > 0) {
                        for (Integer longClickViewId : longClickViewIds) {
                            View childView = mPressedView.findViewById(longClickViewId);
                            if (inRangeOfView(childView, e) && childView.isEnabled()) {
                                if (nestViewIds != null && nestViewIds.contains(longClickViewId)) {
                                    isChildLongClick = true;
                                    break;
                                }
                                setPressViewHotSpot(e, childView);
                                onItemChildLongClick(mBaseRVAdapter, childView, vh.getLayoutPosition() - mBaseRVAdapter.getHeaderLayoutCount());
                                childView.setPressed(true);
                                isShowPress = true;
                                isChildLongClick = true;
                                break;
                            }
                        }
                    }
                    if (!isChildLongClick) {
                        onItemLongClick(mBaseRVAdapter, mPressedView, vh.getLayoutPosition() - mBaseRVAdapter.getHeaderLayoutCount());
                        setPressViewHotSpot(e, mPressedView);
                        mPressedView.setPressed(true);
                        if (longClickViewIds != null) {
                            for (Integer longClickViewId : longClickViewIds) {
                                View childView = mPressedView.findViewById(longClickViewId);
                                if (childView != null) {
                                    childView.setPressed(false);
                                }
                            }
                        }
                        isShowPress = true;
                    }
                }
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private void setPressViewHotSpot(final MotionEvent e, final View mPressedView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            /**
             * when   click   Outside the region  ,mPressedView is null
             */
            if (mPressedView != null && mPressedView.getBackground() != null) {
                mPressedView.getBackground().setHotspot(e.getRawX(), e.getY() - mPressedView.getY());
            }
        }
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     *
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    public abstract void onItemClick(BaseRVAdapter adapter, View view, int position);

    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param view     The view whihin the AbsListView that was clicked
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    public abstract void onItemLongClick(BaseRVAdapter adapter, View view, int position);

    public abstract void onItemChildClick(BaseRVAdapter adapter, View view, int position);

    public abstract void onItemChildLongClick(BaseRVAdapter adapter, View view, int position);

    public boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        if (view == null || !view.isShown()) {
            return false;
        }
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x
                || ev.getRawX() > (x + view.getWidth())
                || ev.getRawY() < y
                || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    private boolean isHeaderOrFooterPosition(int position) {
        /**
         *  have a headview and EMPTY_VIEW FOOTER_VIEW LOADING_VIEW
         */
        if (mBaseRVAdapter == null) {
            if (recyclerView != null) {
                mBaseRVAdapter = (BaseRVAdapter) recyclerView.getAdapter();
            } else {
                return false;
            }
        }
        int type = mBaseRVAdapter.getItemViewType(position);
        return (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW);
    }

    private boolean isHeaderOrFooterView(int type) {
        return (type == EMPTY_VIEW || type == HEADER_VIEW || type == FOOTER_VIEW || type == LOADING_VIEW);
    }
}


