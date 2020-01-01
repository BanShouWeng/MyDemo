package com.bsw.mydemo.widget.BswRecyclerView;

/**
 * RecyclerView布局过滤筛选回调
 *
 * @author 半寿翁
 * @date 2019/3/12.
 */
public interface BswFilterLayoutFilter<T> {
    /**
     * 筛选方法
     *
     * @param t          被筛选数据
     * @param layoutItem 布局条目
     */
    void performFilter(T t, BswLayoutItem layoutItem);
}
