package study.tc.community.community.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.tc.community.community.dto.AccessTokenDto;
import study.tc.community.community.dto.GithubUser;
import study.tc.community.community.provider.GithubProvider;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")  // 把配置文件里面的注入进代码
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();  //alt + ctrl + v
        accessTokenDto.setRedirect_uri(redirectUri);
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);

       // GithubProvider githubProvider = this.githubProvider;
        //得到 User
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        GithubUser user = githubProvider.githubUser(accessToken);
        if(user != null) {
            // 将user对象放入session
            request.getSession().setAttribute("user", user);
            // 登录成功跳转回首页
            return "redirect:/";
        } else {
            return "redirect:/";

        }
        //System.out.println(user.getId());
       // return "index";
    }
}
