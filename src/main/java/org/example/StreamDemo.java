package org.example;

import java.util.List;
import java.util.stream.Stream;

public class StreamDemo {

    void show() {
        List<Movie> movies = List.of(
                new Movie("a", 10),
                new Movie("b", 15),
                new Movie("c", 20)
        );

        Stream.iterate(1, n -> n+2).limit(100).forEach(System.out::println);
    }
}
