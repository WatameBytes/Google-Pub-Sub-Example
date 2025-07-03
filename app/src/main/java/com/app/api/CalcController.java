package com.app.api;

import com.business.calculator.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calc")
@RequiredArgsConstructor
public class CalcController {

    private final CalculatorService calc;

    @GetMapping("/add")
    public int add(@RequestParam int a, @RequestParam int b) {
        return calc.add(a, b);
    }

    @GetMapping("/sub")
    public int sub(@RequestParam int a, @RequestParam int b) {
        return calc.sub(a, b);
    }
}
