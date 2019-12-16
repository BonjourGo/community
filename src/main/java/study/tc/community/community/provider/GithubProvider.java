package study.tc.community.community.provider;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import study.tc.community.community.dto.AccessTokenDto;
import study.tc.community.community.dto.GithubUser;

import java.io.IOException;

@Component   // 把当前类作为路由的承载者
public class GithubProvider {
    public String getAccessToken(AccessTokenDto accessTokenDto) { // 获取accesstoken
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();


        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDto));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            // 拆分token
//               String[] split = string.split("&");
//               String tokenStr = split[0];
            String token = string.split("&")[0].split("=")[1];
            return token;
//               System.out.println(string);
//               return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    // 得到用户token
    public GithubUser githubUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//将json数据转换为Java类对象
            return githubUser;
        } catch (IOException e) {

        }
        return null;

    }
}
