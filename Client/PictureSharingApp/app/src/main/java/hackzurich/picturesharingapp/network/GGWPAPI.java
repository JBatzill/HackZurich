package hackzurich.picturesharingapp.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GGWPAPI {

    private static GGWPAPI instance;

    public static GGWPAPI getInstance() {
        if (GGWPAPI.instance == null) {
            GGWPAPI.instance = new GGWPAPI();
        }

        return GGWPAPI.instance;
    }

    static GGWPAPI getInstance(String session_key) {
        if (GGWPAPI.instance == null) {
            GGWPAPI.instance = new GGWPAPI(session_key);
        }

        return GGWPAPI.instance;
    }
    
    public String session_key;
    private String url = "http://gg-wp.cloudapp.net:5000/api/";

    public GGWPAPI() {
    }

    public GGWPAPI(String session_key) {
        this.session_key = session_key;
    }


    private String post(String path, List<NameValuePair> nameValuePair) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(this.url + path);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Post Response:", response.toString());
            return response.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String post_file(String path, InputStreamEntity reqEntity) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(this.url + path);

        httpPost.setEntity(reqEntity);

        try {
            HttpResponse response = httpClient.execute(httpPost);
            Log.d("Http Post Response:", response.toString());
            return response.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String get(String path) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(this.url + path);

        try {
            HttpResponse response = httpClient.execute(httpGet);
            Log.d("Http Post Response:", response.toString());
            return response.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Bitmap get_file(String path) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(this.url + path);

        try {
            HttpResponse response = httpClient.execute(httpGet);

            String entityContents="";
            HttpEntity responseEntity = response.getEntity();
            BufferedHttpEntity httpEntity = null;
            try {
                httpEntity = new BufferedHttpEntity(responseEntity);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            InputStream imageStream = null;
            try {
                imageStream = httpEntity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return BitmapFactory.decodeStream(imageStream);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int register(String firstname, String lastname, String email, String password) {
        String path = "register";

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("firstname", firstname));
        nameValuePair.add(new BasicNameValuePair("lastname", lastname));
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("firstname", password));

        return Integer.parseInt(this.post(path, nameValuePair));
    }

    public String login(String email, String password) {
        String path = "login";

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("password", password));

        return this.post(path, nameValuePair);
    }

    public ArrayList<Integer> get_user_groups() {
        String path = session_key + "/group";
        String[] parts = this.get(path).split(",");

        ArrayList<Integer> group_ids = new ArrayList<Integer>();
        for (String group_id : parts) {
            group_ids.add(Integer.parseInt(group_id.trim()));
        }

        return group_ids;
    }

    public ArrayList<String> get_group_members(int group_id) {
        String path = session_key + "/group/" + String.valueOf(group_id);
        String[] parts = this.get(path).split(",");

        ArrayList<String> user_emails = new ArrayList<String>();
        for (String user_email : parts) {
            user_emails.add(user_email.trim());
        }

        return user_emails;
    }

    public int add_group(String name) {
        String path = session_key + "/group/";

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("name", name));

        return Integer.parseInt(this.post(path, nameValuePair));
    }

    public boolean add_group_member(int group_id, String email) {
        String path = session_key + "/group/" + String.valueOf(group_id) + "/members";

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("email", email));

        return this.post(path, nameValuePair) == "Success";
    }

    public ArrayList<Integer> get_shares(int group_id) {
        String path = session_key + "/group/" + String.valueOf(group_id) + "/shares";

        String[] parts = this.get(path).split(",");
        ArrayList<Integer> photo_ids = new ArrayList<Integer>();
        for (String photo_id : parts) {
            photo_ids.add(Integer.parseInt(photo_id.trim()));
        }

        return photo_ids;
    }

    public boolean add_share(int photo_id, int group_id) {
        String path = session_key + "/group/" + String.valueOf(group_id) + "/shares";

        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("photo_id", String.valueOf(photo_id)));

        return this.post(path, nameValuePair) == "Success";
    }

    public int add_photo(String directory, String filename) {
        String path = session_key + "/photo/";

        File file = new File(directory, filename);
        InputStreamEntity reqEntity = null;
        try {
            reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true);

        return Integer.parseInt(this.post_file(path, reqEntity));
    }

    public Bitmap get_photo(int photo_id) {
        String path = session_key + "/photo/" + String.valueOf(photo_id);

        return get_file(path);
    }
}

