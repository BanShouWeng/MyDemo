package com.bsw.mydemo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.Utils.TxtUtils;
import com.bsw.mydemo.base.BaseActivity;

import java.util.List;

public class LinkmanActivity extends BaseActivity {

    private Button insertLinkman;
    private Button getLinkman;
    private EditText linkmanName;
    private EditText linkmanPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_linkman;
    }

    @Override
    protected void findViews() {
        getLinkman = getView(R.id.get_linkman);
        insertLinkman = getView(R.id.insert_linkman);
        linkmanName = getView(R.id.linkman_name);
        linkmanPhone = getView(R.id.linkman_phone);
    }

    @Override
    protected void formatViews() {
        getLinkman.setText("获取联系人");
    }

    @Override
    protected void formatData() {
        setOnClickListener(R.id.get_linkman, R.id.insert_linkman);
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_linkman:
                PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
                    @Override
                    public Integer[] onPermissionGranted() {
                        return new Integer[]{PermissionUtils.CODE_READ_CONTACTS};
                    }

                    @Override
                    public void onRequestResult(List<String> deniedPermission) {
                        if (Const.judgeListNull(deniedPermission) == 0) {
                            startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    ContactsContract.Contacts.CONTENT_URI), 0);
                        } else {
                            toast("已拒绝获取联系人权限");
                        }
                    }
                });
                break;

            case R.id.insert_linkman:
                PermissionUtils.setRequestPermissions(activity, new PermissionUtils.PermissionGrant() {
                    @Override
                    public Integer[] onPermissionGranted() {
                        return new Integer[]{PermissionUtils.CODE_WRITE_CONTACTS};
                    }

                    @Override
                    public void onRequestResult(List<String> deniedPermission) {
                        if (Const.judgeListNull(deniedPermission) == 0) {
                            addContact(TxtUtils.getText(linkmanName), TxtUtils.getText(linkmanPhone));
                        } else {
                            toast("已拒绝添加联系人权限");
                        }
                    }
                });
                break;

        }
    }

    // 一个添加联系人信息的例子
    public void addContact(String name, String phoneNumber) {
        // 创建一个空的ContentValues
        ContentValues values = new ContentValues();

        // 向RawContacts.CONTENT_URI空值插入，
        // 先获取Android系统返回的rawContactId
        // 后面要基于此id插入值
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();

        values.put(Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        // 联系人名字
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        // 向联系人URI添加联系人名字
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        // 联系人的电话号码
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        // 电话类型
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        // 向联系人电话号码URI添加电话号码
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();

        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        // 联系人的Email地址
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
        // 电子邮件的类型
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        // 向联系人Email URI添加Email数据
        getContentResolver().insert(Data.CONTENT_URI, values);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            CursorLoader cursorLoader = new CursorLoader(context, contactData, null, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            cursor.moveToFirst();
            // 获得DATA表中的名字
            String username = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 条件为联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            @SuppressLint("Recycle") Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            if (phone != null) {
                while (phone.moveToNext()) {
                    String usernumber = phone
                            .getString(phone
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    getLinkman.setText(usernumber + " (" + username + ")");
                }
                phone.close();
            } else {
                toast("联系人获取失败，请重新获取");
            }

        }
    }
}
