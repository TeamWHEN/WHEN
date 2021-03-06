package com.teamw.ysm0622.app_when.group;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamw.ysm0622.app_when.R;
import com.teamw.ysm0622.app_when.global.Gl;
import com.teamw.ysm0622.app_when.object.User;

import java.util.ArrayList;

public class UserDataAdapter extends ArrayAdapter<User> {

    // TAG
    private static final String TAG = UserDataAdapter.class.getName();

    // Const
    private static final int COUNT = 2;

    // Intent
    private Intent mIntent;

    // Context
    private final Context mContext;

    // Data
    private ArrayList<User> values = new ArrayList<>();


    public UserDataAdapter(Context context, int resource, ArrayList<User> values) {
        super(context, resource, values);
        this.mContext = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.member_item, null);
        }
        final User u = values.get(position);
        if (u != null) {
            TextView mTextView[] = new TextView[COUNT];
            ImageView mImageViewProfile;

            mTextView[0] = (TextView) v.findViewById(R.id.TextView0);
            mTextView[1] = (TextView) v.findViewById(R.id.TextView1);

            mImageViewProfile = (ImageView) v.findViewById(R.id.ImageView0);

            if (!u.getImageFilePath().equals("")) {//유저 이미지가 존재
                Log.e("userDate",u.getId()+"");
                mImageViewProfile.clearColorFilter();
                Bitmap temp = BitmapFactory.decodeFile(Gl.ImageFilePath + u.getId() + ".jpg");
                mImageViewProfile.setImageBitmap(Gl.getCircleBitmap(temp));
            } else {//default 이미지 사용
                mImageViewProfile.clearColorFilter();
                Bitmap temp = Gl.getDefaultImage(u.getId());
                mImageViewProfile.setImageBitmap(Gl.getCircleBitmap(temp));
            }
            mTextView[0].setText(u.getName());
            mTextView[1].setText(u.getEmail());
        }
        return v;
    }
}
