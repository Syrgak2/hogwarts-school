package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class InfoService {
    public String returnIntegerValue() {
        return baseMethod() + parallel();
    }

    private String baseMethod() {
        long startBaseMethod = System.currentTimeMillis();
        int sum = Stream.iterate(1, a -> a +1) .limit(1_000_000) .reduce(0, (a, b) -> a + b );
        long timeBaseMethod = System.currentTimeMillis() - startBaseMethod;
        return "Base: Время " + timeBaseMethod + " значение " + sum;
    }

    private String parallel() {
        long start = System.currentTimeMillis();
        int sum = IntStream.rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
        long time = System.currentTimeMillis() - start;
        return "Parallel: Time= " + time + " value= " + sum;
    }
}
