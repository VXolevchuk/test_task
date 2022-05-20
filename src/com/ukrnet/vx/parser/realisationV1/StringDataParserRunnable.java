package com.ukrnet.vx.parser.realisationV1;

import com.ukrnet.vx.parser.StringDataParser;
import com.ukrnet.vx.exceptions.DataFormatException;
import com.ukrnet.vx.evaluator.factory.EvaluatorFactory;
import com.ukrnet.vx.evaluator.StringEvaluator;

import java.util.*;

public class StringDataParserRunnable implements StringDataParser<String> {
    private static final String QUERY_ID = "D";
    private static final String DATA_ID = "C";
    private static final int EXP_QUERY_LENGTH = 5;
    private static final int EXP_DATA_LENGTH = 6;
    private static final String DATA_SPLITTER = " ";
    private static final String DATA_EX = "Invalid data on position ";
    private static final String VERT_DOTS = ": ";

    private String[][] data;
    private final EvaluatorFactory<Integer> factory;

    private int resSize = 0;

    public StringDataParserRunnable(EvaluatorFactory<Integer> factory, List<String> data) {
        this.factory = factory;
        initQueriesAndData(data);
    }

    @Override
    public List<String> parseData() {
        String[] result = new String[resSize];
        int resPos = 0;
        System.out.println(data.length);
        for (int i = 0; i < data.length; i++) {
           try {
               if (data[i][0].equals(QUERY_ID)) {
                   new Thread(new QueryHandlerRunnable(data, result, i,
                           resPos++, getEvaluator(data[i]))).start();
               }
           } catch (DataFormatException e) {
               System.out.println(e.getMessage());
           }
        }
        while (QueryHandlerRunnable.atomicInteger.get() != resPos) {
        }
        return List.of(result);
    }

    private void initQueriesAndData(List<String> data) {
        String[][] dataArray = new String[data.size() - 1][];
        for (int i = 1; i < data.size(); i++) {
            try {
                String[] arr = data.get(i).split(DATA_SPLITTER);
                if(arr[0].startsWith(DATA_ID) && arr.length == EXP_DATA_LENGTH) {
                    dataArray[i - 1] = arr;
                } else if (arr[0].startsWith(QUERY_ID) && arr.length == EXP_QUERY_LENGTH) {
                    resSize++;
                    dataArray[i - 1] = arr;
                } else {
                    throw new DataFormatException(DATA_EX + i + VERT_DOTS + Arrays.toString(arr));
                }
            } catch (DataFormatException e) {
                System.out.println(e.getMessage());
            }

        }
        this.data = dataArray;
    }

    private StringEvaluator<Integer> getEvaluator(String[] query) throws DataFormatException {
        return factory.getEvaluator(query);
    }

}
