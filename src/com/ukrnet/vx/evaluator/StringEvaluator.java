package com.ukrnet.vx.evaluator;

import com.ukrnet.vx.exceptions.DataFormatException;

public interface StringEvaluator<T> {
    public boolean evaluate(String[] s) throws DataFormatException;
    public T getValue();

}
