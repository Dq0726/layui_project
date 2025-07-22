package com.zkyx.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.zkyx.common.config.ZkyxConfig;

/**
 * 首页
 *
 * @author zkyx
 */
@Controller
public class SysIndexController
{
    /** 系统基础配置 */
    @Autowired
    private ZkyxConfig zkyxConfig;

    /**
     * 访问首页，提示语
     */
    @GetMapping("/")
    public String index()
    {
    	return "index";
       // return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", zkyxConfig.getName(), zkyxConfig.getVersion());
    } 
}
