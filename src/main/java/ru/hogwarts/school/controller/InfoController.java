package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.InfoService;

@RestController
@RequestMapping("/info")
public class InfoController {
    @Value("${server.port}")
    private int serverPort;

    @Autowired
    private InfoService infoService;

    @GetMapping
    public Integer getPort() {
        return serverPort;
    }

    @GetMapping("/integerValue")
    public String getIntegerValue() {
        return infoService.returnIntegerValue();
    }
}
