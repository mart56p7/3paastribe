package com.example.demo;

import org.springframework.beans.factory.annotation.Value;


public class TTTConfig {
    @Value("${ttt.debugmode}")
    private boolean debug;

    public boolean getDebugMode(){
        return debug;
    }
}
