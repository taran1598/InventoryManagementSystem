package com.example.inventorymanagementsystem.genericPages;

import com.example.inventorymanagementsystem.thymeleafAttributes.ThymeleafAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final static String ERROR = "/error";

    @GetMapping("/error")
    public String genericErrorPage(Model model) {
        model.addAttribute(ThymeleafAttributes.error.toString(), "No custom error found");
        return "errorPage";
    }


}
