package com.ukrnet.vx.parser.realisationV1;

import com.ukrnet.vx.exceptions.DataFormatException;
import com.ukrnet.vx.evaluator.StringEvaluator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class QueryHandlerRunnable implements Runnable{
    private static final String WAITING_TIME_CONST = "-";
    private static final String DATA_ID = "C";
    private static final String DATA_EX_P1 = "Exception in ";
    private static final String DATA_EX_P2 = " string: ";

    public static AtomicInteger atomicInteger = new AtomicInteger(0);
    private final StringEvaluator<Integer> evaluator;
    private final String[][] data;
    private final int pos;
    private final String[] resultArr;
    private final int resultPos;

    public QueryHandlerRunnable(String[][] data, String[] resultArr,
                                int pos, int resultPos, StringEvaluator<Integer> evaluator) {
        this.evaluator = evaluator;
        this.data = data;
        this.resultArr = resultArr;
        this.pos = pos;
        this.resultPos = resultPos;
    }

    @Override
    public void run() {
        boolean isMatched = false;
        int result = 0;
        int counter = 0;

        for (int i = 0; i < pos - 1; i++) {
            String [] str = data[i];
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
            resultArr[resultPos] = String.valueOf(result/counter);
        } else {
            resultArr[resultPos] = WAITING_TIME_CONST;
        }
        atomicInteger.getAndIncrement();
    }
}