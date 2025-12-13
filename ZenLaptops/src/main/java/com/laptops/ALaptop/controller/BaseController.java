package com.laptops.ALaptop.controller;

import com.laptops.ALaptop.model.Laptops;
import com.laptops.ALaptop.service.MainService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/laptop-base")
@AllArgsConstructor
public class BaseController {
    @Autowired
    public MainService mainService;

    @GetMapping
    public String getPage(Model model) {
        List<Laptops> laptopList = mainService.getMyLaptopsList();
        model.addAttribute("laptop_list",mainService.getMyLaptopsList());
        return "mycomputers";

    }
    @GetMapping("/add-laptop")
    public String getAddLaptopPage(Model model){
        model.addAttribute("laptops",new Laptops());
        return "addproduct";
    }

    @PostMapping("/save")
    public String saveLaptops(@ModelAttribute("laptops") Laptops theLaptop, Errors errors, @RequestParam("image")MultipartFile multipartFile) {
        System.out.println(multipartFile);
       mainService.addLaptop(theLaptop, multipartFile);
        return "redirect:/laptop-base";
    }
    @GetMapping("/delete/{id}")
    public String deleteLaptopById(@PathVariable("id") long id){
        System.out.println(id);
        mainService.deleteLaptopById(id);
        return "redirect:/laptop-base";
    }
    @GetMapping("/edit/{id}")
    public String editLaptopById(@PathVariable("id") long id, Model model){
        Laptops laptops = mainService.getLaptopById(id);

        model.addAttribute("laptops",laptops);
        return "addproduct";
    }
}
