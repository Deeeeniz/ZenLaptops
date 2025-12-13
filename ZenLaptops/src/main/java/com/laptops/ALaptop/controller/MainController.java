package com.laptops.ALaptop.controller;

import com.laptops.ALaptop.model.Account;
import com.laptops.ALaptop.model.Laptops;
import com.laptops.ALaptop.service.MainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/laptop-shop")
@AllArgsConstructor
public class MainController {
    @Autowired
    private final MainService mainService;

    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("laptop_list", mainService.getLaptopList());
        return "shop";
    }
    @GetMapping("/signup")
    public String getSignUpPage(Model model) {
        model.addAttribute("account", new Account());
        return "signup";
    }

    @PostMapping
    public String saveAccount(
            @Valid @RequestBody @ModelAttribute("account") Account theAccount, BindingResult bindingResult
        )
        {
        if(bindingResult.hasErrors()){
            return "signup";
        }
        mainService.saveAccount(theAccount);
        return "redirect:/login";
    }

    @GetMapping("/info/{id}")
    public String getInfoPage(
            @PathVariable("id") long id, Model model) {
        Laptops theLaptops = mainService.getLaptopById(id);
        model.addAttribute("laptop_info",theLaptops);
        return "info-page";
    }

    @GetMapping("/details1")
    public String getDefaultDetailsOne() {
        return "details1";
    }
}

