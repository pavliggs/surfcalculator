package com.khovaylo.surf.service.util;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import org.springframework.stereotype.Service;

/**
 * @author Pavel Khovaylo
 */
@Service
public class Calculation {

    public Double calculate(String expression) {
        return new DoubleEvaluator().evaluate(expression);
    }
}
