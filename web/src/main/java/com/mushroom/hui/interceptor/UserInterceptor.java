package com.mushroom.hui.interceptor;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 用户信息的拦截器
 * Created by yihui on 16/2/5.
 */
public class UserInterceptor extends HandlerInterceptorAdapter {

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * <p/>
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        String workId = (String) session.getAttribute("workId");
        if (StringUtils.isEmpty(workId)) {
            request.setAttribute("uid", "12345");
            return false; // 若会话中没有workId，则没法愉快的玩耍下去了
        } else {
            long userId = Long.valueOf(workId);
            request.setAttribute("uid", userId);
            return true;
        }

    }
}
