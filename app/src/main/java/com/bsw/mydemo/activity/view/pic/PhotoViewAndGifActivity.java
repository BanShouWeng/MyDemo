package com.bsw.mydemo.activity.view.pic;

import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.BswFloorPoint.BswFloorPointView;
import com.bsw.mydemo.widget.BswFloorPoint.PointBean;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewAndGifActivity extends BaseActivity {

    //    private String bg = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1535451667055&di=8612f9af78a2021234bdf572fdef13b6&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01485c59587d97a8012193a32e9dce.png";
    private String bg = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1575024541194&di=54090294f137c29aff53bb16117aa7c5&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20170614%2F2e1d710bf3b2415286f37d8834dd27bc.jpg";
            private String imgPath = "https://upload-images.jianshu.io/upload_images/2680888-7d44a92155197066.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/60";
//    private String imgPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1545215030027&di=46741dc4c9ec3aaf5cef35b600a20b85&imgtype=0&src=http%3A%2F%2Fpic.58pic.com%2F58pic%2F14%2F87%2F42%2F10v58PICSug_1024.jpg";

    private BswFloorPointView imgFloorPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_view_and_gif;
    }

    @Override
    protected void findViews() {
        imgFloorPoint = getView(R.id.img_floor_point);
    }

    @Override
    protected void formatViews() {
        List<PointBean> pointBeans = new ArrayList<>();
        pointBeans.add(new PointBean(PointBean.TYPE_REAL, 149.5, 123, imgPath, PointBean.POSITION_CENTER));
        pointBeans.add(new PointBean(PointBean.TYPE_REAL, 436, 444, imgPath, PointBean.POSITION_CENTER));
        try {
            imgFloorPoint.setPointList(pointBeans)
                    .setFloorBackground(bg)
                    .setSize(BswFloorPointView.KEEP_SIZE)
                    .paint();
        } catch (NullPointerException e) {
            Logger.e(getName(), e);
        }
    }

    @Override
    protected void formatData() {
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}
