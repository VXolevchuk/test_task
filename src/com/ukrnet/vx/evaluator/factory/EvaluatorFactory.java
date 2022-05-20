package com.ukrnet.vx.evaluator.factory;

import com.ukrnet.vx.exceptions.DataFormatException;
import com.ukrnet.vx.evaluator.StringEvaluator;

public abstract class EvaluatorFactory <T>{
    public abstract StringEvaluator<T> getEvaluator(String[] query) throws DataFormatException;
}
