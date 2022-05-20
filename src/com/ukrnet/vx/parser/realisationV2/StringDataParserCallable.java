package com.ukrnet.vx.parser.realisationV2;

import com.ukrnet.vx.parser.StringDataParser;
import com.ukrnet.vx.exceptions.DataFormatException;
import com.ukrnet.vx.evaluator.factory.EvaluatorFactory;
import com.ukrnet.vx.evaluator.StringEvaluator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class StringDataParserCallable implements StringDataParser<String> {
    private static final String QUERY_ID = "D";
    private static final String DATA_ID = "C";
    private static final int EXP_QUERY_LENGTH = 5;
    private static final int EXP_DATA_LENGTH = 6;
    private static final String DATA_SPLITTER = " ";
    private static final String DATA_EX = "Invalid data on position ";
    private static final String VERT_DOTS = ": ";

    private String[][] data;
    private final EvaluatorFactory<Integer> factory;

    public StringDataParserCallable(EvaluatorFactory<Integer> factory, List<String> data) {
        this.factory = factory;
        initQueriesAndData(data);
    }

    @Override
    public List<String> parseData() {
        ArrayList<Future<String>> result = new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            try {
                if (data[i][0].equals(QUERY_ID)) {
                    FutureTask<String> res = new FutureTask<>(new QueryHandlerCallable(getEvaluator(data[i]), i,
                            data) );
                    result.add(res);
                    Thread thread = new Thread(res);
                    thread.start();
                }
            } catch (DataFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        ArrayList<String> list = new ArrayList<>();
        for (Future<String> future : result) {
            try {
                list.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    private void initQueriesAndData(List<String> data) {
        String[][] dataArray = new String[data.size() - 1][];
        for (int i = 1; i < data.size(); i++) {
            try {
                String[] arr = data.get(i).split(DATA_SPLITTER);
                if(arr[0].startsWith(DATA_ID) && arr.length == EXP_DATA_LENGTH) {
                    dataArray[i - 1] = arr;
                } else if (arr[0].startsWith(QUERY_ID) && arr.length == EXP_QUERY_LENGTH) {
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
