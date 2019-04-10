package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bsw.mydemo.widget.BswRecyclerView.BswFilterDataCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.BswFilterLayoutFilter;
import com.bsw.mydemo.widget.BswRecyclerView.BswLayoutItem;
import com.bsw.mydemo.widget.BswRecyclerView.BswRecyclerView;
import com.bsw.mydemo.widget.BswRecyclerView.ConvertViewCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.LimitAnnotation;
import com.bsw.mydemo.widget.BswRecyclerView.RecyclerViewHolder;
import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class BswRecycleViewActivity extends BaseActivity {

    private final int LAYOUT_TAG_FIRST = 50;
    private final int LAYOUT_TAG_RIGHT = 51;
    private final int LAYOUT_TAG_LEFT = 52;
    private final int LAYOUT_TAG_BOTH = 53;

    private BswRecyclerView<User> demoRv;
    private EditText demoFilterEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_BswRecyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bsw_recycle_view;
    }

    @Override
    protected void findViews() {
        demoRv = findViewById(R.id.demo_rv);
        demoFilterEt = findViewById(R.id.demo_filter_et);
    }

    @Override
    protected void formatViews() {
        demoRv.initAdapter(R.layout.user_item_layout, convertViewCallBack)
                .setDecoration()
                .setLayoutManager()
                .setLayoutFilterCallBack(bswFilterLayoutFilter)
                .setFilterEt(demoFilterEt, bswFilterDataCallBack);
    }

    @Override
    protected void formatData() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new User("张" + i, i));
        }
        demoRv.setData(users);
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    private ConvertViewCallBack<User> convertViewCallBack = new ConvertViewCallBack<User>() {
        @Override
        public User convert(RecyclerViewHolder holder, User user, int position, int layoutTag) {
                if (layoutTag == LAYOUT_TAG_FIRST) {
                    holder.setText(R.id.user_name, user.name + "马上出生了呢");
                } else {
                    holder.setText(R.id.user_name, "我叫" + user.name)
                            .setText(R.id.user_age, "今年" + user.age + "岁");
                    switch (layoutTag) {
                        case LAYOUT_TAG_RIGHT:
                            holder.setClickListener(R.id.user_right, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toast("点了右边");
                                }
                            });
                            break;

                        case LAYOUT_TAG_LEFT:
                            holder.setClickListener(R.id.user_left, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toast("点了左边");
                                }
                            });
                            break;

                        case LAYOUT_TAG_BOTH:
                            holder.setClickListener(R.id.user_right, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toast("点了右边");
                                }
                            }).setClickListener(R.id.user_right, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    toast("点了右边");
                                }
                            });
                            break;
                    }
                }
            return user;
        }
    };

    private BswFilterLayoutFilter<User> bswFilterLayoutFilter = new BswFilterLayoutFilter<User>() {
        @Override
        public void performFilter(User user, BswLayoutItem layoutItem) {

            if (user.age == 0) {
                layoutItem.setLayoutTag(LAYOUT_TAG_FIRST)
                        .put(LimitAnnotation.LAYOUT_MAIN, R.layout.user_first_layout);
            } else if (user.age % 5 == 1) {
                layoutItem.setLayoutTag(LAYOUT_TAG_RIGHT)
                        .put(LimitAnnotation.LAYOUT_RIGHT, R.layout.user_right_layout);
            } else if (user.age % 5 == 2) {
                layoutItem.setLayoutTag(LAYOUT_TAG_LEFT)
                        .put(LimitAnnotation.LAYOUT_LEFT, R.layout.user_left_layout);
            } else if (user.age % 5 == 3) {
                layoutItem.setLayoutTag(LAYOUT_TAG_BOTH)
                        .put(LimitAnnotation.LAYOUT_RIGHT, R.layout.user_right_layout)
                        .put(LimitAnnotation.LAYOUT_LEFT, R.layout.user_left_layout);
            }
        }
    };

    private BswFilterDataCallBack<User> bswFilterDataCallBack = new BswFilterDataCallBack<User>() {
        @Override
        public boolean filter(User data, CharSequence constraint) {
            return data.name.contains(constraint);
        }
    };

    class User {
        User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        private String name;
        private int age;

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
