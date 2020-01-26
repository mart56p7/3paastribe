package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TTTController {
    @GetMapping({"index.html", ""})
    public String get_index(Model model){
        model.addAttribute("winnertext", new String("Keep playing to become more awesome!111"));
        model.addAttribute("loosertext", new String("You can still win, just continue playing! Only 2c per game!"));
        return "index.html";
    }
}
