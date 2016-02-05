package com.mushroom.hui;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yihui on 16/1/16.
 */
@Controller
public class Index {
    /**
     * 返回字符串 index
     * @param request
     * @return
     */
    @RequestMapping(value = "/")
    public String index(HttpServletRequest request) {
        return "index";
    }


    /**
     * 返回网页，找名为 index.vm的模板返回
     * @param request
     * @return
     */
    @RequestMapping(value = "/index")
    public String index2(HttpServletRequest request) {
        return "index";
    }


    @RequestMapping(value = "/view")
    public ModelAndView getView(HttpServletRequest request) {
        String id = request.getParameter("id");
        if(StringUtils.isBlank(id)) {
            id = "None";
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("view");
        modelAndView.addObject("id", id);
        return modelAndView;
    }
}
