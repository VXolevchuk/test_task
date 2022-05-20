package com.ukrnet.vx;

import com.ukrnet.vx.evaluator.factory.EvaluatorFactory;
import com.ukrnet.vx.evaluator.factory.WTEvaluatorFactory;
import com.ukrnet.vx.parser.StringDataParser;
import com.ukrnet.vx.parser.realisationV1.StringDataParserRunnable;
import com.ukrnet.vx.parser.realisationV2.StringDataParserCallable;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String... args) {
        ArrayList<String> strings = new ArrayList<>();
        try(BufferedReader f = new BufferedReader(new
                FileReader("input.txt"))){

            String str = "";
            for(;( str = f.readLine()) != null;) {
                strings.add(str);
            }

        } catch(IOException e){
            System.out.println("ERROR");
        }

        EvaluatorFactory<Integer> factory = new WTEvaluatorFactory();

        StringDataParser<String> waitingTimeDataParserCallable = new StringDataParserCallable(factory, strings);
        List<String> list = (List<String>) waitingTimeDataParserCallable.parseData();
        for (String str : list) {
            System.out.println(str);
        }

        StringDataParser<String> waitingTimeDataParserRunnable = new StringDataParserRunnable(factory, strings);
        List<String> list1 = (List<String>) waitingTimeDataParserRunnable.parseData();
        for (String s1 : list1) {
            System.out.println(s1);
        }
    }
}
