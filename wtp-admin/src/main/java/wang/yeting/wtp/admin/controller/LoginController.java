package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.vo.LoginVo;
import wang.yeting.wtp.admin.service.LoginService;

/**
 * @author : weipeng
 * @date : 2020-07-27 20:04
 */
@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        return loginService.login(loginVo);
    }


    @GetMapping("/info")
    public Result info(String token) {
        return loginService.info(token);
    }

    @PostMapping("/logout")
    public String logout() {
        String q = "{\"code\":20000,\"data\":\"success\"}";
        return q;
    }

}
