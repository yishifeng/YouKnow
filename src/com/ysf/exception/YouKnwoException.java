package com.ysf.exception;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public class YouKnwoException {


    @RequestMapping("error")
    public String error404(){

        return "error";
    }

    @RequestMapping("error")
    public String error500(){

        return "error";
    }

}
