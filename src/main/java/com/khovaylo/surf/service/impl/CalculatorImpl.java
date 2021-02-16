package com.khovaylo.surf.service.impl;

import com.khovaylo.surf.service.Calculator;
import org.springframework.stereotype.Service;

/**
 * @author Pavel Khovaylo
 */
@Service
public class CalculatorImpl implements Calculator {

    @Override
    public double sum(double val1, double val2) {
        double result = val1 + val2;
        if (result < Double.MIN_VALUE || result > Double.MAX_VALUE)
            throw new ArithmeticException("The result is out of bounds");
        return result;
    }

    @Override
    public double diff(double val1, double val2) {
        double result = val1 - val2;
        if (result < Double.MIN_VALUE || result > Double.MAX_VALUE)
            throw new ArithmeticException("The result is out of bounds");
        return result;
    }

    @Override
    public double mul(double val1, double val2) {
        double result = val1 * val2;
        if (result < Double.MIN_VALUE || result > Double.MAX_VALUE)
            throw new ArithmeticException("The result is out of bounds");
        return result;
    }

    @Override
    public double div(double val1, double val2) {
        if (val2 == 0)
            throw new ArithmeticException("/0 is no correct operation");
        return val1 / val2;
    }
}