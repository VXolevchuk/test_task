package com.ukrnet.vx.evaluator.factory;

import com.ukrnet.vx.exceptions.DataFormatException;
import com.ukrnet.vx.evaluator.StringEvaluator;
import com.ukrnet.vx.evaluator.WaitingTimelineEvaluator;

public class WTEvaluatorFactory extends EvaluatorFactory<Integer> {
    @Override
    public StringEvaluator<Integer> getEvaluator(String[] query) throws DataFormatException {
        return new WaitingTimelineEvaluator(query);
    }
}
