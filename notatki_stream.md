# Stream API- strumienie
Strumienie są mechanizmem pozwalającym na przetwarzanie danych w sposób deklaratywny, z wykorzystaniem interfejsów
funkcyjnych. Pozwalają uprościć skomplikowane operacje skupiając się na tym co chcemy osiągnąć, nie w jaki sposób.  
Strumienie moża stworzyć z:
- kolekcji
- tablic
- dowolnej ilości obiektów z pomocą metody ``Stream.of();``
Można również stworzyć nieskończone strumienie metodą ``Stream.generate``. Tak utworzoy strumień można ograniczyć metodą
``limit`` np. 
```
var stream = Stream.generate(()-> Math.random());
stream.limit(3).forEach(System.out::println);
```
Innym sposobem jest ``Stream.iterate``. Metoda przyjmuje 2 argumenty- *seed* czyli wartość początkowa, następny argument 
to UnaryOperator zwracający nasępny element, np. ``Stream.iterate(1, n -> n+2).limit(100).forEach(System.out::println);``
wyprintuje 100 nieparzystych liczb.

## Operacje pośrednie
Operacje pośrednie zwracają nowy strumień, są one też przetwarzane w sposób *lazy*. Zaliczamy do nich m.in:
- ``filter``
- ``map``
- ``flatMap``
- ``peek``
- ``distinct``
- ``sorted``
- ``limit``
  
**map** metoda oczekująca na wejściu obiektu ``Function<T, R>``. Jej zadaniem jest przekonwertowanie elementu strumienia
na nowy element, który dodatkowo może mieć inny typ niż wejściowy.  
  
**flatMap** metoda umożliwiająca spłaszczenie zagnieżdżonej struktury danych. Onzcza to, że jeżeli każdy przetwarzany
element posiada element z którego jesteśmy w stanie stworzyć nowy strumień, to wynikiem operacji ``flatMap`` będzie nowy,
pojedyńczy strumień powstały przez ich połączenie w jeden. Operacja ``flatMap`` przyjmuje w parametrze interfejs funkcyjny
``Function<T, ? extends Stream<? extends R>>``. Przykład pokazuje w jaki sposób z obiektów ``Statistics`` i jego pól 
``values`` stworzyć pojedyńczy strumień. 
```
public class FlatMapDemo {

  public static void main(String[] args) {
    final Statistics statisticsA = new Statistics(2.0, List.of(1, 2, 3));
    final Statistics statisticsB = new Statistics(2.5, List.of(2, 3, 2, 3));
    Stream.of(statisticsA, statisticsB)
        .flatMap(statistics -> statistics.getValues().stream()); // Otrzymujemy stream wartości 1, 2, 3, 2, 3, 2, 3
  }
}

class Statistics {
  private double average;
  private List<Integer> values;

  public Statistics(final double average, final List<Integer> values) {
    this.average = average;
    this.values = values;
  }

  public double getAverage() {
    return average;
  }

  public List<Integer> getValues() {
    return values;
  }
}
```
``.flatMap(statistics -> statistics.getValues().stream());`` z obiektów ``Statistics`` pobieramy listy. Otrzymalibyśmy 
w ten sposób strumień list, po wywołaniu metory ``stream()`` dostaniemy jeden strumień wszystkich liczb 'wyciągniętych' z
każdej listy. 