package com.xins.offline;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.apache.flink.util.ParameterTool;

/**
 * @Author xinsong
 * @Date 2025/7/21 16:07
 * @description
 */
public class WordCount {

    public static String[] WORDS = new String[]{"To be, or not to be,--that is the question:--", "Whether 'tis nobler in the mind to suffer", "The slings and arrows of outrageous fortune", "Or to take arms against a sea of troubles,"};

    public static void main(String[] args) throws Exception {
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setRuntimeMode(RuntimeExecutionMode.AUTOMATIC);
        env.getConfig().setGlobalJobParameters(parameterTool);

        DataStreamSource<String> text = env.fromData(WORDS);

        // 2. 单词拆分并映射为 (word, 1)
        DataStream<WordWithCount> mappedStream = text
                .flatMap((String line, Collector<String> out) -> {
                    for (String word : line.split("\\W+")) {
                        if (!word.isEmpty()) {
                            out.collect(word.trim().toLowerCase());
                        }
                    }
                })
                .returns(Types.STRING)
                .map(word -> new WordWithCount(word, 1))
                .returns(Types.POJO(WordWithCount.class));

        mappedStream
                .keyBy((KeySelector<WordWithCount, String>) value -> value.word)
                .sum("count")
                .print();
        env.execute();
    }


    // POJO 类：用于存储单词和计数
    public static class WordWithCount {
        public String word;
        public int count;

        public WordWithCount() {}

        public WordWithCount(String word, int count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }

}
