package com.qljt.gantry.login.controller;

import com.qljt.gantry.login.service.LoginService;
import com.qljt.gantry.platform.base.bean.CodeMsg;
import com.qljt.gantry.platform.base.bean.ResultJson;
import com.qljt.gantry.platform.base.controller.BaseController;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.shiro.ShiroUtils;
import io.swagger.annotations.Api;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;



/**
 * @author liuliangliang
 * @create 2020-03-02 14:39
 */
@RestController
@Api(tags="登录相关接口")
public class LoginController extends BaseController{

    @Autowired
    LoginService loginService;

    @RequestMapping(value = {"/sys/login"}, method = {RequestMethod.POST})
    @ResponseBody
    public ResultJson login(@RequestBody Map<String, Object> paramsMap)
    {
        UsernamePasswordToken token = null;
        //String username, String password, String captcha
        String userName = "";
        try{
            Subject subject = ShiroUtils.getSubject();
            userName = (String) paramsMap.get("userName");
            String password = (String) paramsMap.get("password");
            token = new UsernamePasswordToken(userName, password);
            subject.login(token);

        }catch (UnknownAccountException e){
            return  ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }catch (IncorrectCredentialsException e) {
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs("账号或密码不正确"));
        } catch (LockedAccountException e) {
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs("账号已被锁定,请联系管理员"));
        } catch (AuthenticationException e) {
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs("账户验证失败"));
        }

        return ResultJson.success(loginService.getLoginInfo(userName));
    }

    @RequestMapping(value = {"/sys/token"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResultJson queryUserInfo(String token)
    {
        if (token == null) {
            return ResultJson.error(CodeMsg.SESSION_ERROR);
        }

        UserEntity userEntity = (UserEntity) ShiroUtils.getSubject().getPrincipal();
        String encryptionKey= DigestUtils.sha256Hex(userEntity.getSalt()+userEntity.getUserName());

        if (encryptionKey.equals(token)){
            return ResultJson.success(loginService.getRoleInfo(userEntity.getUserId()));
        }else{
            logger.error("无效token");
            return ResultJson.error(CodeMsg.SESSION_ERROR);
        }

    }


    @RequestMapping(value = {"/sys/test"}, method = {RequestMethod.GET})
    @ResponseBody
    public ResultJson test()
    {
        return ResultJson.success(CodeMsg.SUCCESS);
    }



}
