
class GGWPAPI {
    public string session_key;
    private HttpClient httpClient;
    private string url = 'http://gg-wp.cloudapp.net:5000/api/';
    
    public GGWPAPI() {
        this.httpClient = new DefaultHttpClient();
    }
    
    public GGWPAPI(string session_key) {
        this.session_key = session_key;
        this.httpClient = new DefaultHttpClient();
    }
    
    private string post(string path, List<NameValuePair> nameValuePair) {
        HttpPost httpPost = new HttpPost(this.url + path);
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        }
        
        try {
            HttpResponse response = this.httpClient.execute(httpPost);
            Log.d("Http Post Response:", response.toString());
            return response.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private string post_file(string path, InputStreamEntity reqEntity) {
        HttpPost httpPost = new HttpPost(this.url + path);
        
        try {
            httppost.setEntity(reqEntity);
        } catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        }
        
        try {
            HttpResponse response = this.httpClient.execute(httpPost);
            Log.d("Http Post Response:", response.toString());
            return response.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private string get(string path) {
        HttpGet httpGet = new HttpGet(this.url + path);
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        }
        
        try {
            HttpResponse response = this.httpClient.execute(httpGet);
            Log.d("Http Post Response:", response.toString());
            return response.toString();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private Bitmap get_file(string path) {
        HttpGet httpGet = new HttpGet(this.url + path);
        
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        }
        
        try {
            HttpResponse response = this.httpClient.execute(httpGet);
            
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
    }    
    
    public int register(string firstname, string lastname, string email, string password) {
        path = 'register';
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("firstname", firstname));
        nameValuePair.add(new BasicNameValuePair("lastname", lastname));
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("firstname", password));
        
        return Integer.parseInt(this.post(path, nameValuePair));
    }
    
    public string login(string email, string password) {
        path = 'login';
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("email", email));
        nameValuePair.add(new BasicNameValuePair("firstname", password));
        
        return this.post(path, nameValuePair);
    }
    
    public ArrayList<int> get_user_groups() {
        path = session_key + '/group';
        string[] parts = this.get(path).split(",");
        
        ArrayList<int> group_ids = new ArrayList<int>();
        for (string group_id : parts) {
            group_ids.Add(Integer.parseInt(group_id.trim()));
        }
        
        return group_ids;
    }
    
    public string get_group_members(int group_id) {
        path = session_key + '/group/' + str(group_id);
        string[] parts = this.get(path).split(",");
        
        ArrayList<string> user_emails = new ArrayList<string>();
        for (string user_email : parts) {
            user_emails.Add(user_email.trim());
        }
        
        return user_emails;
    }
    
    public int add_group(string name) {
        path = session_key + '/group/';
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("name", name));
        
        return Integer.parseInt(this.post(path, nameValuePair));
    }
    
    public boolean add_group_member(int group_id, string email) {
        path = session_key + '/group/' + str(group_id) + '/members';
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("email", email));
        
        return this.post(path, nameValuePair) == 'Success';
    }
    
    public string get_shares(int group_id) {
        path = session_key + '/group/' + str(group_id) + '/shares';
        
        string[] parts = this.get(path).split(",");
        ArrayList<int> photo_ids = new ArrayList<int>();
        for (string photo_id : parts) {
            photo_ids.Add(Integer.parseInt(photo_id.trim()));
        }
        
        return photo_ids;
    }
    
    public string add_share(int photo_id, int group_id) {
        path = session_key + '/group/' + str(group_id) + '/shares';
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
        nameValuePair.add(new BasicNameValuePair("photo_id", photo_id));
        
        return this.post(path, nameValuePair) == 'Success';
    }
    
    public int add_photo(string directory, string filename) {
        path = session_key + '/photo/';
        
        File file = new File(directory, filename);
        InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true);
        
        return Integer.parseInt(this.post_file(path, reqEntity));
    }
    
    public Bitmap get_photo(int photo_id) {
        path = session_key + '/photo/' + str(photo_id);
        
        return get_file(path);
    }
}

