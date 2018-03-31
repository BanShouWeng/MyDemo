package com.bsw.mydemo.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.db.SQLdm;
/**
 * @author 半寿翁
 */
public class DbActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //打开数据库输出流
        SQLdm s = new SQLdm();
        SQLiteDatabase db =s.openDatabase(getApplicationContext());

        TextView textv = (TextView) findViewById(R.id.textv);
        //查询数据库中testid=1的数据
        Cursor cursor = db.rawQuery("select * from testbiao where testid=?", new String[]{"3"});
        String name = null;
        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex("name"));
        }
        //这是一个TextView，把得到的数据库中的name显示出来.
        textv.setText(name);
        cursor.close();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_db;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void formatViews() {

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
