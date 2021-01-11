package com.jiay.eurekaprovider1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/getHi")
    public String getHi() {
        System.out.println("provider");
        return "hi_provider1";
    }
}
