package com.utmn.chamortsev.hw4.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StreamComparisonService {

    @PostConstruct
    public void compareStreams() {
        List<Integer> numbers = new Random()
                .ints(1000000, 1, 1000)
                .boxed()
                .collect(Collectors.toList());

        long start1 = System.currentTimeMillis();
        long sum1 = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .mapToLong(Long::valueOf)
                .sum();
        long time1 = System.currentTimeMillis() - start1;

        long start2 = System.currentTimeMillis();
        long sum2 = numbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 2)
                .mapToLong(Long::valueOf)
                .sum();
        long time2 = System.currentTimeMillis() - start2;

        System.out.println("Stream: " + time1 + " мс, сумма: " + sum1);
        System.out.println("ParallelStream: " + time2 + " мс, сумма: " + sum2);
    }
}