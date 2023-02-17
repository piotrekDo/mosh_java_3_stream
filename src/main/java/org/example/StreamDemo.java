package org.example;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

    void show() {
        List<Movie> movies = List.of(
                new Movie("a", 10, Genre.ACTION),
                new Movie("b", 15, Genre.COMEDY),
                new Movie("c", 20, Genre.COMEDY)
        );

        Map<Genre, Optional<Movie>> collect = movies.stream()
                .collect(Collectors.groupingBy(Movie::getGenre, Collectors.maxBy(Comparator.comparing(Movie::getLikes))));

        Map<Genre, Movie> collect1 = movies.stream()
                .collect(Collectors.toMap(Movie::getGenre, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(Movie::getLikes))));

        Map<Genre, Long> collect2 = movies.stream()
                .collect(Collectors.groupingBy(
                        Movie::getGenre, Collectors.counting()
                ));

        Map<Boolean, List<Movie>> collect3 = movies.stream()
                .collect(Collectors.partitioningBy(m -> m.getLikes() > 15));

        System.out.println(collect3);
    }
}
