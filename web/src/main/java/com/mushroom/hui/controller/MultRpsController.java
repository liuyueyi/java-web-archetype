package com.mushroom.hui.controller;

import com.alibaba.fastjson.JSON;
import com.mushroom.hui.common.adt.AdtUtil;
import com.mushroom.hui.common.adt.entity.ResultCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 测试各种不同放回方式的controller
 * Created by yihui on 16/1/17.
 */
@RequestMapping(value = "/multrps")
@Controller
public class MultRpsController {

    /**
     * 返回Json字符串， 作为正文返回
     * @param request
     * @return
     */
    @RequestMapping(value = "/string")
    @ResponseBody
    public String resStr(HttpServletRequest request) {
        String id = request.getParameter("id");
        String ans = "The id is: " + id;

        Object obj;
        if (StringUtils.isBlank(id)) {
            obj = AdtUtil.returnErrorResult(ResultCode.PARAMETER_ERROR, ans, "id", id);
        }else {
            obj = AdtUtil.returnResult(ans);
        }

        return JSON.toJSONString(obj);
    }

    /**
     * 返回void，表示响应的视图页面对应为访问地址, 本例子就是返回 multrps/view.vm 模板对应的视图
     * 其中 request.setAttribtue的参数，可以在模板中直接获取到 (通过 $id)
     * @param request
     */
    @RequestMapping(value = "/view")
    public void retView(HttpServletRequest request) {
        String id = request.getParameter("id");
        String ans = "The id is: " + id;
        request.setAttribute("id", ans);
    }

    /**
     * 这个同上面有相通之处，返回的也是页面请求对应的URI，只是传递给对应模板的参数，是以 Map形式进行的
     * @param request
     * @return
     */
    @RequestMapping(value = "/view3")
    public Map<String, String> retView3(HttpServletRequest request) {
        String id = request.getParameter("id");
        String ans = "The id is: " + id;
        Map<String, String> map = new HashMap<>();
        map.put("id", ans);
        return map;
    }


    /**
     * 返回视图
     * @param request
     * @return
     */
    @RequestMapping(value = "/view1")
    public ModelAndView retView1(HttpServletRequest request) {
        String id = request.getParameter("id");
        String ans = "The id is: " + id;
        ModelAndView view = new ModelAndView("view");
        // view.setViewName("view"); 这个与上面的构造函数效果相同
        view.addObject("id", ans);
        return view;
    }

    /**
     * 和上面异曲同工
     * @param request
     * @param model
     * @return path 为返回的视图名
     */
    @RequestMapping(value = "/view2")
    public String retView2(HttpServletRequest request, Model model) {
        String path = "view";
        String ans = "The id is xxx";
        model.addAttribute("id", ans);
        return path;
    }
}