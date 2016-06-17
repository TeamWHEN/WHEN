package com.example.ysm0622.app_when.server;

import android.os.AsyncTask;
import android.util.Log;

import com.example.ysm0622.app_when.global.Gl;
import com.example.ysm0622.app_when.object.Group;
import com.example.ysm0622.app_when.object.Meet;
import com.example.ysm0622.app_when.object.MeetDate;
import com.example.ysm0622.app_when.object.Time;
import com.example.ysm0622.app_when.object.User;
import com.example.ysm0622.app_when.object.UserGroup;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ServerConnection extends AsyncTask<String, String, String> {

    public static final String TAG = ServerConnection.class.getName();

    public String TYPE;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        Log.d("Gl", args[0]);
        TYPE = args[0];
        switch (TYPE) {
            case Gl.SELECT_ALL_USER:
            case Gl.SELECT_ALL_GROUP:
            case Gl.SELECT_ALL_USERGROUP:
                return getStringFromServer(new ArrayList<NameValuePair>(), args[0]);
            default:
                return getStringFromServer(getNameValuePair(args[1]), args[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("Gl", result);
        switch (TYPE) {
            case Gl.SELECT_ALL_USER:
                SelectAllUser(result);
                break;
            case Gl.SELECT_ALL_GROUP:
                SelectAllGroup(result);
                break;
            case Gl.SELECT_ALL_USERGROUP:
                SelectAllUserGroup(result);
                break;
            case Gl.SELECT_MEET_BY_GROUP:
                SelectMeetByGroup(result);
                break;
            case Gl.SELECT_TIME_BY_MEET:
                SelectTimeByMeet(result);
                break;
            case Gl.SELECT_MEETDATE_BY_MEET:
                SelectMeetDateByMeet(result);
                break;
            default:
        }
    }

    public ArrayList<NameValuePair> getNameValuePair(String index) {
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        switch (TYPE) {
            case Gl.SELECT_MEET_BY_GROUP:
//                arrayList = SelectMeetByGroup(Integer.parseInt(index));
                break;
            case Gl.SELECT_MEETDATE_BY_MEET:
                arrayList = SelectMeetDateByMeet(Integer.parseInt(index));
                break;
            case Gl.SELECT_TIME_BY_MEET:
                arrayList = SelectTimeByMeet(Integer.parseInt(index));
                break;
            case Gl.INSERT_USER:
//                arrayList = InsertUser(Integer.parseInt(index));
                break;
            case Gl.DELETE_USER:
//                arrayList = DeleteUser(Integer.parseInt(index));
                break;
            case Gl.UPDATE_USER:
                arrayList = UpdateUser(Integer.parseInt(index));
                break;
            case Gl.INSERT_GROUP:
//                arrayList = InsertGroup(Integer.parseInt(index));
                break;
            case Gl.DELETE_GROUP:
                arrayList = DeleteGroup(Integer.parseInt(index));
                break;
//            case Gl.UPDATE_GROUP:
//                arrayList = UpdateGroup(Integer.parseInt(index));
//                break;
            case Gl.INSERT_USERGROUP:
//                arrayList = InsertUserGroup(Integer.parseInt(index));
                break;
            case Gl.DELETE_USERGROUP:
//                arrayList = DeleteUserGroup(Integer.parseInt(index));
                break;
            case Gl.INSERT_MEET:
                arrayList = InsertMeet(Integer.parseInt(index));
                break;
            case Gl.DELETE_MEET:
                arrayList = DeleteMeet(Integer.parseInt(index));
                break;
//            case Gl.UPDATE_MEET:
//                arrayList = UpdateMeet(Integer.parseInt(index));
//                break;
            case Gl.INSERT_MEETDATE:
                arrayList = InsertMeetDate(Integer.parseInt(index));
                break;
            case Gl.INSERT_TIME:
                arrayList = InsertTime(Integer.parseInt(index));
                break;
            case Gl.DELETE_TIME:
                arrayList = DeleteTime(Integer.parseInt(index));
                break;

        }
        return arrayList;
    }

    public static String getStringFromServer(ArrayList<NameValuePair> post, String url) {
        String result = "";

        // 연결 HttpClient 객체 생성
        HttpClient client = new DefaultHttpClient();

        // 객체 연결 설정 부분, 연결 최대시간 등등
        HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 5000);
        HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성
        HttpPost httpPost = new HttpPost(url);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(post, HTTP.UTF_8);
            httpPost.setEntity(entity);
//            client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
            HttpResponse responsePOST = client.execute(httpPost);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity);
            }
            Log.d("Gl", result);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void SelectAllUser(String result) {
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("user");
            ArrayList<User> arrayList = new ArrayList<>();
            Log.d("Gl", data.toString());
            for (int i = 0; i < data.length(); i++) {
                User u = new Gson().fromJson(data.getJSONObject(i).toString(), User.class);
                Date d = new Date(u.getJoinDate());
                u.setJoined(d);
                arrayList.add(u);
            }
            Gl.setUsers(arrayList);
            Gl.LogAllUser();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void SelectAllGroup(String result) {
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("user");
            ArrayList<Group> arrayList = new ArrayList<>();
            Log.d("Gl", data.toString());
            for (int i = 0; i < data.length(); i++) {
                Log.d("Gl", data.getJSONObject(i).toString());
                Group g = new Gson().fromJson(data.getJSONObject(i).toString(), Group.class);
                arrayList.add(g);
                User u = Gl.getUserById(g.getMasterId());
                g.setMaster(u);
            }
            Gl.setGroups(arrayList);
            Gl.LogAllGroup();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void SelectAllUserGroup(String result) {
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("user");
            ArrayList<UserGroup> arrayList = new ArrayList<>();
            Log.d("Gl", data.toString());
            for (int i = 0; i < data.length(); i++) {
                UserGroup ug = new Gson().fromJson(data.getJSONObject(i).toString(), UserGroup.class);
                arrayList.add(ug);
            }
            Gl.setUserGroup(arrayList);

            for (int i = 0; i < Gl.getUserGroup().size(); i++) {
                UserGroup ug = Gl.getUserGroup().get(i);
                Group g = Gl.getGroupById(ug.getGroupId());
                User u = Gl.getUserById(ug.getUserId());
                g.addMember(u);
            }

            Gl.LogAllGroup();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void SelectMeetByGroup(String result) {
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("user");
            ArrayList<Meet> arrayList = new ArrayList<>();
            Log.d("Gl", data.toString());
            for (int i = 0; i < data.length(); i++) {
                Meet m = new Gson().fromJson(data.getJSONObject(i).toString(), Meet.class);
                m.setGroup(Gl.getGroupById(m.getGroupId()));
                m.setMaster(Gl.getUserById(m.getMasterId()));
                Date d = new Date(m.getCreateDate());
                m.setCreated(d);
                arrayList.add(m);
            }
            Gl.setMeets(arrayList);
            Gl.LogAllMeet();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static ArrayList<NameValuePair> SelectMeetByGroup(Group g) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("GroupId", String.valueOf(g.getId())));
        Log.d("Gl", "SelectMeetByGroup(" + g.getId() + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static void SelectMeetDateByMeet(String result) {
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("user");
            ArrayList<MeetDate> arrayList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                MeetDate m = new Gson().fromJson(data.getJSONObject(i).toString(), MeetDate.class);
                arrayList.add(m);
            }
            Gl.setMeetDate(arrayList);
            Gl.LogAllMeet();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static ArrayList<NameValuePair> SelectMeetDateByMeet(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getMeet(index).getId())));
        Log.d("Gl", "SelectMeetDateByMeet(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static void SelectTimeByMeet(String result) {
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("user");
            ArrayList<Time> arrayList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                Time t = new Gson().fromJson(data.getJSONObject(i).toString(), Time.class);
                arrayList.add(t);
            }
            Gl.setTime(arrayList);
//            int n = Gl.getTime(0).getMeetId();
//            ArrayList<DateTime> dateTime = new ArrayList<>();
//            ArrayList<ArrayList<Calendar>> arrayLists = new ArrayList<>();
//            ArrayList<Calendar> arrayList1 = new ArrayList<>();
//            DateTime dt = new DateTime();
//            for (int i = 0; i < arrayList.size(); i++) {
//                if (i + 1 < arrayList.size() && arrayList.get(i).getUserId() == arrayList.get(i + 1).getUserId()) {
//                    dt.setUser(Gl.getUserById(arrayList.get(i).getUserId()));
//
//                } else {
//                    dateTime.add(dt);
//                    dt = new DateTime();
//                }
//            }
//
//
//            Gl.getMeet(n).setDateTime(dateTime);
//
//
//            ArrayList<ArrayList<Calendar>> selectTime = new ArrayList<>();
//            while (true) {
//                ArrayList<Calendar> select = null;
//                for (int i = 0; i < arrayList.size(); i++) {
//                    select = new ArrayList<>();
//                    Date D = new Date(arrayList.get(i).getTime());
//                    Calendar C = new GregorianCalendar();
//                    C.setTime(D);
//                    select.add(C);
//                }
//                if (select != null) selectTime.add(select);
//                else break;
//            }
//            Gl.LogAllTimeByMeet(Gl.getMeet(arrayList.get(0).getMeetId()));
//
//            int n = 0;
//            for (int i = 0; i < arrayList.size(); i++) {
//                arrayList.get(i).setMeetId(Gl.getMeetById(arrayList.get(i).getMeetId()));
//                arrayList.get(i).setUserId(Gl.getUserById(arrayList.get(i).getUserId()));
//                arrayList.get(i).setTime(arrayList.get(i).getTime());
//            }
//            ArrayList<Calendar> select = new ArrayList<>();
//            for (int i = 0; i < arrayList.size(); i++) {
//                Date D1 = new Date(arrayList.get(i).getTime());
//                Calendar C1 = new GregorianCalendar();
//                C1.setTime(D1);
//                select.add(C1);
//            }
//            ArrayList<ArrayList<Calendar>> selectTime = new ArrayList<>();
//            ArrayList<Calendar> tmp = new ArrayList<>();
//            for (int i = 0; i < select.size(); i++) {
//                if (isEqual(select.get(i), select.get(i + 1))) {
//                    tmp.add(select.get(i));
//                } else {
//                    selectTime.add(tmp);
//                    tmp = new ArrayList<>();
//                }
//            }
            Gl.setTime(arrayList);
            Gl.LogAllTimeByMeet(Gl.getMeet(arrayList.get(0).getMeetId()));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static ArrayList<NameValuePair> SelectTimeByMeet(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getMeet(index).getId())));
        Log.d("Gl", "SelectTimeByMeet(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> InsertUser(User u) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Name", u.getName()));
        post.add(new BasicNameValuePair("Email", u.getEmail()));
        post.add(new BasicNameValuePair("Password", u.getPassword()));
        Log.d("Gl", "InsertUser(" + u.getId() + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> DeleteUser(User u) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Id", String.valueOf(u.getId())));
        Log.d("Gl", "DeleteUser(" + u.getId() + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> UpdateUser(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Name", Gl.getUser(index).getName()));
        post.add(new BasicNameValuePair("Password", Gl.getUser(index).getPassword()));
        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getUser(index).getId())));
        Log.d("Gl", "UpdateUser(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> InsertGroup(Group g) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Title", g.getTitle()));
        post.add(new BasicNameValuePair("Desc", g.getDesc()));
        post.add(new BasicNameValuePair("MasterId", String.valueOf(g.getMaster().getId())));
        Log.d("Gl", "InsertGroup(" + g.getId() + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> DeleteGroup(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getGroup(index).getId())));
        Log.d("Gl", "DeleteGroup(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

//    public ArrayList<NameValuePair> UpdateGroup(int index) {
//        ArrayList<NameValuePair> post = new ArrayList<>();
//        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getGroup(index).getId())));
//        Log.d("Gl", "UpdateGroup(" + index + ")");
//        for (int i = 0; i < post.size(); i++)
//            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
//        return post;
//    }

    public static ArrayList<NameValuePair> InsertUserGroup(Group g) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        for (int i = 0; i < g.Member.size(); i++) {
            post.add(new BasicNameValuePair("GroupId", String.valueOf(g.getId())));
            post.add(new BasicNameValuePair("UserId", String.valueOf(g.Member.get(i).getId())));
        }
        Log.d("Gl", "InsertUserGroup(" + g.getId() + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> DeleteUserGroup(Group g) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("GroupId", String.valueOf(g.getId())));
        post.add(new BasicNameValuePair("UserId", String.valueOf(Gl.MyUser.getId())));
        Log.d("Gl", "DeleteUserGroup(" + g.getId() + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public static ArrayList<NameValuePair> InsertMeet(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("GroupId", String.valueOf(Gl.getMeet(index).getGroup().getId())));
        post.add(new BasicNameValuePair("MasterId", String.valueOf(Gl.getMeet(index).getMaster().getId())));
        post.add(new BasicNameValuePair("Title", Gl.getMeet(index).getTitle()));
        post.add(new BasicNameValuePair("Desc", Gl.getMeet(index).getDesc()));
        post.add(new BasicNameValuePair("Location", Gl.getMeet(index).getLocation()));
        Log.d("Gl", "InsertMeet(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public ArrayList<NameValuePair> DeleteMeet(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getMeet(index).getId())));
        Log.d("Gl", "DeleteMeet(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

//    public ArrayList<NameValuePair> UpdateMeet(int index) {
//        ArrayList<NameValuePair> post = new ArrayList<>();
//        post.add(new BasicNameValuePair("Id", String.valueOf(Gl.getMeet(index).getId())));
//        Log.d("Gl", "UpdateMeet(" + index + ")");
//        for (int i = 0; i < post.size(); i++)
//            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
//        return post;
//    }

    public ArrayList<NameValuePair> InsertMeetDate(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("MeetId", String.valueOf(Gl.getMeetDate(index).getMeetId())));
        post.add(new BasicNameValuePair("Date", String.valueOf(Gl.getMeetDate(index).getDate())));
        Log.d("Gl", "InsertMeetDate(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public ArrayList<NameValuePair> InsertTime(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("MeetId", String.valueOf(Gl.getTime(index).getMeetId())));
        post.add(new BasicNameValuePair("UserId", String.valueOf(Gl.getTime(index).getUserId())));
        post.add(new BasicNameValuePair("Time", String.valueOf(Gl.getTime(index).getTime())));
        Log.d("Gl", "InsertTime(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }

    public ArrayList<NameValuePair> DeleteTime(int index) {
        ArrayList<NameValuePair> post = new ArrayList<>();
        post.add(new BasicNameValuePair("UserId", String.valueOf(Gl.getTime(index).getUserId())));
        Log.d("Gl", "DeleteTime(" + index + ")");
        for (int i = 0; i < post.size(); i++)
            Log.d("Gl", "post.get(" + i + ") : " + post.get(i).toString());
        return post;
    }


    public boolean isEqual(Calendar A, Calendar B) {
        if (A.get(Calendar.YEAR) == B.get(Calendar.YEAR) && A.get(Calendar.MONTH) == B.get(Calendar.MONTH) && A.get(Calendar.DATE) == B.get(Calendar.DATE)) {
            return true;
        } else return false;
    }

}