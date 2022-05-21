package com.scloudic.rabbitframework.example.web.rest.test;

import com.scloudic.rabbitframework.security.authz.annotation.UserAuthentication;
import com.scloudic.rabbitframework.web.AbstractRabbitController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("freemarker")
public class FreemarkerResource extends AbstractRabbitController {
    @RequestMapping(path = "html", method = {RequestMethod.GET})
//    @UserAuthentication
    public ModelAndView freemarkerHtml(HttpServletRequest request) {
        return getFreemarker("/hello", request);
    }

    private ModelAndView getFreemarker(String path, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<String> lstValue = new ArrayList<String>();
        lstValue.add("item1");
        lstValue.add("item2");
        lstValue.add("item3");
        params.put("user", "Pavel");
        params.put("items", lstValue);
        return new ModelAndView(path, params);
    }
}
