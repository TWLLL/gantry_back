package com.qljt.gantry.platform.shiro;

import com.qljt.gantry.platform.dept.bean.UserEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShiroFilter extends FormAuthenticationFilter
{
    //加密的字符串,相当于签名
    private static String SINGNATURE_TOKEN = "加密token";
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //这里只有返回false才会执行onAccessDenied方法,因为
        // return super.isAccessAllowed(request, response, mappedValue);
        // yf：全去onAccessDeined()里去处理
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        String token = getRequestToken((HttpServletRequest) request);
        String login = ((HttpServletRequest) request).getServletPath();

        //如果为登录,就放行
        if ("/sys/login".equals(login)){
            return true;
        }
        //yf：退出登录也放行
        if ("/sys/logout".equals(login)){
            return true;
        }
        if (StringUtils.isBlank(token)){
            logger.info("没有token");
            return false;
        }

        //从当前shiro中获得用户信息
        UserEntity user = (UserEntity)SecurityUtils.getSubject().getPrincipal();
        //yf：对salt重新赋值
        SINGNATURE_TOKEN = user.getSalt();
        //对当前ID进行SHA256加密
        String encryptionKey= DigestUtils.sha256Hex(SINGNATURE_TOKEN+user.getUserName());
        if (encryptionKey.equals(token)){
            return true;
        }else{
            logger.info("无效token");
        }
        return false;
    }
    private String getRequestToken(HttpServletRequest request){
        //默认从请求头中获得token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        return token;
    }
}
