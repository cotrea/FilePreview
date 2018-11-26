package cn.keking.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanting
 * @date 2018/11/8
 */
@Controller
public class ManageController {
    @RequestMapping(value = "manage", method = RequestMethod.POST)
    public String goManage(@RequestParam(value="password") String tunnel){
        String validation = "eim";
        String validations = "cl";
        if(validation.equals(tunnel)||validations.equals(tunnel)) {
            return "manage";
        }else{
            return "redirect:/login.html";
        }
    }

    /**
     * 项目主页定向至login.html
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "redirect:/login.html";
    }
}
