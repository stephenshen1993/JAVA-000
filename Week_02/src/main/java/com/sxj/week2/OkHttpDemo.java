package com.sxj.week2;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @ClassName : com.sxj.week2.OkHttpDemo  //类名
 * @Description : 使用 OkHttp 访问 http://localhost:8801  //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-10-28 23:21  //时间
 */
public class OkHttpDemo {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://localhost:8801").build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
