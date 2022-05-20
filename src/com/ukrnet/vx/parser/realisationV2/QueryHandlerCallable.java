package com.ukrnet.vx.parser.realisationV2;

import com.ukrnet.vx.exceptions.DataFormatException;
import com.ukrnet.vx.evaluator.StringEvaluator;

import java.util.concurrent.Callable;

class QueryHandlerCallable implements Callable<String> {
    private static final String WAITING_TIME_CONST = "-";
    private static final String DATA_ID = "C";
    private static final String DATA_EX_P1 = "Exception in ";
    private static final String DATA_EX_P2 = " string: ";

    private final StringEvaluator<Integer> evaluator;
    private final int pos;
    private final String[][] dataArray;

    public QueryHandlerCallable(StringEvaluator<Integer> evaluator, int pos, String[][] dataArray) {
        this.evaluator = evaluator;
        this.pos = pos;
        this.dataArray = dataArray;
    }

    @Override
    public String call() throws Exception {
        boolean isMatched = false;
        int result = 0;
        int counter = 0;

        for (int i = 0; i < pos - 1; i++) {
            String [] str = dataArray[i];
            try {
                if (str[0].equals(DATA_ID)) {
                    if (evaluator.evaluate(str)) {
                        result = result + evaluator.getValue();
                        counter++;
                        isMatched = true;
                    }
                }
            } catch (DataFormatException e) {
                System.out.println(DATA_EX_P1 + counter + DATA_EX_P2 + e.getMessage());
            }
        }
        if(isMatched) {
            return String.valueOf(result/counter);
        } else {
            return WAITING_TIME_CONST;
        }
    }
}
