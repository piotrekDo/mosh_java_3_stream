package org.example;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

    void show() {
        List<Movie> movies = List.of(
                new Movie("a", 10),
                new Movie("b", 15),
                new Movie("c", 20)
        );

        Stream.iterate(1, n -> n + 2).limit(100).forEach(System.out::println);

        Integer reduce = movies.stream()
                .map(movie -> movie.getLikes())
                .reduce(0, (x, y) -> x + y);

        Map<String, Movie> collect = movies.stream()
                .collect(Collectors.toMap(Movie::getTitle, Function.identity()));

        IntSummaryStatistics collect1 = movies.stream()
                .collect(Collectors.summarizingInt(Movie::getLikes));

        System.out.println(collect1);
    }
}
