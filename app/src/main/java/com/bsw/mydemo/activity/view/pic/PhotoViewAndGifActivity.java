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
    private String bg = "http://xf.tandatech.com:1080/fileUpload/upload/file/2018-09-07/1b545c45-67cb-4349-9520-3cd7e0960603.png";
    private String imgPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530278301610&di=f09a4c1eb4436d128f3e49220f4244d0&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161115%2F6163765431c44d538b37d6efb32ee885_th.jpg";

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
        pointBeans.add(new PointBean(0.5, 0.4, imgPath, PointBean.POSITION_CENTER));
        pointBeans.add(new PointBean(0.3, 0.7, imgPath, PointBean.POSITION_CENTER));
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
