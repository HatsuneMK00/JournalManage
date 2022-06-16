package edu.ecnu.journalmanage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ArticleController {
    @RequestMapping("/cs")
    public String cs(){
        return "article/cs";
    }

    @RequestMapping("/ws")
    public String ws(){
        return "article/ws";
    }

    @RequestMapping("/zs")
    public String zs(){
        return "article/zs";
    }

    @RequestMapping("/accept")
    public String accept(){
        return "article/accept";
    }

    @RequestMapping("/cstx")
    public String cstx(){
        return "article/cs_tx";
    }

    @RequestMapping("/wstx")
    public String wstx(){
        return "article/ws_tx";
    }

    @RequestMapping("/zstx")
    public String zstx(){
        return "article/zs_tx";
    }
}
