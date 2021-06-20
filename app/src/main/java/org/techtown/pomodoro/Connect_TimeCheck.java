package org.techtown.pomodoro;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connect_TimeCheck extends AsyncTask<String, Long, Boolean> {
    private Context mContext = null;
    private int responseCode;
    private String num_user;
    private String date;
    private int time;

    MainActivity mainActivity;
    public Connect_TimeCheck(Context context){
        mContext = context;
        mainActivity = (MainActivity) MainActivity.context;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String url1= "http://192.168.0.2:8080/pomodoro/index.php/timecheck";
        String charset = "UTF-8";

        try {
            //통신을 위함 객체 생성과 통신 설정
            URL url= new URL(url1);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);

            //통신 시 헤더이외에 전송할 데이터(json형태) 만들기
            //현재는 로그인 이기에 아이디 비밀번호만 입력 edittext에서 받아온 값을 저 부분에 넣으면 됨
            String json = "{'num_user' : '"+ strings[0]+"', 'date' : '"+strings[1]+ "'}";
            //json 형태는 각 개체들이 큰 따옴표로 감싸져 있어야하는데 java에선 그렇게 안되는듯 그래서 변경해주는것
            json = json.replace('\'', '\"');
            byte[] input = json.getBytes("utf-8");


            //출력 버퍼를 만들어서 위의 데이터와함께 출력,전송
            OutputStream os = connection.getOutputStream();
            os.write(input, 0, input.length);
            os.flush();
            os.close();

            //통신이 성공했는지는 여기서 알수있음 Logcat으로 N_test 확인해 보면
            //responsecode와 responseMessage를 받을 수 있는데 쉽게 200, OK가 오면 통신 성공
            if(connection.getResponseCode()==connection.HTTP_OK) {
                //입력 버퍼를 만들어서 데이터를 받음, 서버쪽에서 입력해주는 데이터가 없으면 오류가 남
                //서버쪽에서 전송할 데이터가 없다면 이부분은 없애야할듯
                InputStream is = connection.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result + "\n");
                }

                result = sb.toString();

                //받아온 데이터 확인, 로그인 성공 시에는 200코드와 유저의 고유 번호가 넘어옴
                //200코드는 따로 헤더에만 넘어오게끔 변경할 예정이고 유저의 고유 번호는 받아서
                //저장해 뒀다가 서버와 다른 기능(시간확인, 시간저장)을 이용할 때 함께 전송함으로써
                //유저 식별 가능

                JSONObject jObject = new JSONObject(result);
                responseCode = jObject.getInt("result_code");
                if(responseCode==200) {
                    time = Integer.parseInt(jObject.getString("sum(time)"));
                    Log.d("N_test", String.valueOf(time));
                    return true;
                }
                else if(responseCode==400)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        } catch (MalformedURLException e){
            Log.d("N_test","fail Mal");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("N_test","fail IO");
            e.printStackTrace();
        } catch (JSONException e){
            Log.d("N_test","Json parsing");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        MainActivity.total_time = time;
        mainActivity.setMyTime();
    }
}
