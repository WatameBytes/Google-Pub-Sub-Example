package com.business.calculator;

import org.springframework.stereotype.Service;

@Service   // auto-detectable bean
public class CalculatorService {

    public int add(int a, int b)  { return a + b; }

    public int sub(int a, int b)  { return a - b; }
}
