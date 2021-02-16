package com.khovaylo.surf.service;

/**
 * @author Pavel Khovaylo
 */
public interface Calculator {
    double sum(double val1, double val2);

    double diff(double val1, double val2);

    double mul(double val1, double val2);

    double div(double val1, double val2);
}