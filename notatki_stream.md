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

## Slicing- ograniczenie strumienia
Istnieje możliwość ograniczenia / wycięcia fragmenu strumienia. Umożliwoają to metody:
- ``limit(n)``
- ``skip(n)``
- ``takeWhile(predicate)``
- ``dropWhie(predicate)``
  
**limit** pozwala ograniczyć długość strumienia. Jest metodą pośrednią.
```
someCollection.stream()
    .limit(2)
    .forEach(System.out::println);
```
pozwoli ograniczyć caly strumień do 2 elementów.   
  
**skip** pozwala pominąć określoną, początkową liczbę elementów. Jest metodą pośrednią.
```
someCollection.stream()
    .skip(2)
    .forEach(System.out::println);
```
wydrukuje wszystkie elemeny, zaczynając od trzeciego.
**Powyższe metody mogą być użyteczne przy paginacji**
```
// 1000 obiektów
// 10 obiektów na stronę
// 3cia strona
// skip(20) = skip((page - 1) * pageSize)
// limit(10) = limit(pageSize)

movies.stream()
    .skip(20)
    .limit(10)
    .forEach(System.out::println);
```
  
**takeWhile(predicate)** pozwala określić warunek poprzez który uzupełniamy strumień. W odróżnieniu od metody ``filter``
metoda kończy działanie w momencie gdy zwraca ``false``. Metoda ``filter`` przejdzie po całym strumieniu zwracając obiekty
spełniające warunek.  
  
**dropWhile(predicate)** przeciwność metody ``takeWhile``. Pominie wszystkie elementy spełniające warunek przekazany jako
argument. 


## Operacje pośrednie
Operacje pośrednie zwracają nowy strumień, są one też przetwarzane w sposób *lazy*. Zaliczamy do nich m.in:
- ``filter``
- ``map``
- ``flatMap``
- ``peek``
- ``distinct``
- ``sorted``
- ``limit``
  
**filter** metoda umożliwiająca usunięcie ze strumienia elementów, które nie spełniają przekazanego predykatu będącego
argumentem metody. Przykład pokazuje w jaki sposób usunąć nieparzyste liczby ze strumienia liczb:
```
final int[] idx = { 0 };
Stream.generate(() -> idx[0]++)
    .limit(10)
    .filter(elem -> elem % 2 == 0); //w strumieniu zostają: 0, 2, 4, 6, 8
```
    
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
każdej listy. Interfejs funkcyjny ``Comparator`` udostępnia użyteczną metodę ``comparing`` pozwalającą na przekazanie 
porównywanych obiektów np.
```
movies.stream()
    .sorted(Comparator.comparing(Movie::getTitle))
    .forEach(m -> System.out.println(m.getTitle()));
```
W taki sposób przekazujemy tytuł filmu do porównania, jako że klasa String implementuje interfejs ``Comparable``. 
  
**sorted** pozwala uporządkować kolejność emelentów w strumieniu. Metoda isnieje w 2 przeciążeniach. Jeżeli nie przekażemy
komparatora ``sorted`` wykorzysta naturany porządek określony przez implementację interfejsu ``Comparable``. Jeżeli chcemy
odwrócić kolejność sortowania, możemy wykorzystać metodę ``reversed`` wywoływaną na komparatorze, za ``comparing``  
  
**peek** to unikalna metoda pośrednia przyjmująca interfes ``Consumer`` pozwaljąca podejrzeć strumień w trakcie procesowania
jest użyteczna przy debugowaniu.  
  
**W celu uzyskania unikalnych danych, bez duplikatów wywołujemy metodę pośrednią ``distinct``.** 

## Operacje terminalne
Operacje terminalne zwracają końcowy wynik. Ich wywołanie powoduje wykonanie się wszystkich poprzedzających funkcji 
pośrednich. Do operacji terminalnych zaliczamy:
- ``toArray``
- ``collect``
- ``reduce``
- ``forEach``
- ``forEachOrdered``
- ``count``
- ``min``
- ``max``
- ``anyMatch``
- ``allMatch``
- ``noneMatch``
- ``findAny``
- ``findFirst``

### Reducers
Jest to grupa operacji terminalnych zwracających jeden obiekt. Zaliczamy do nich:  
**count** nie przyjmuje żadnych argumentów i zwraca long z liczbą elementów.  
**anyMatch**, **allMatch** oraz **noneMatch** przyjmują ``Predicate`` i zwracją wartość boolean.  
**findFirst** oraz **findAny** nie przyhmują argumentów i zwracają ``Optional``.  
**max** oraz **min** zwracają ``Optional`` i przyjmują argument w postaci ``Comparator``.  

### Reduce
Metoda ``reduce()`` pozwala zwrócić pojedyńczą wartość. Metoda istnieje w trzech przeciążeniach. Najczęściej wykorzystujemy tę
przyjmującą ``BinaryOperator``. Inna wersja przyjmuje dodatkowo parametr w postaci wartośći początkowej. ``BinaryOperator``
pozwala skumulować wartości. Korzystając z metody przyjmującą wartość początkową w wyniku dostaniemy już ``Integer`` zamiast
``Optional<Integer>``
```
Optional<Integer> reduce = movies.stream()
        .map(movie -> movie.getLikes())
        .reduce((x, y) -> x + y);
```
Poniżej przykład z ``identity`` oznaczającą wartość początkową. 
```
Integer reduce = movies.stream()
        .map(movie -> movie.getLikes())
        .reduce(0, (x, y) -> x + y);
```

### Collectors
Metoda ``collect`` pozwala przekształcić strumień w kolekcję. Istnieje w dwóch przeciążeniach. Pierwsza, prostrza przyjmuje
interfejs ``Collector`` pozwalający w wygodny sposób uzyskać potrzebną kolekcję, tablicę czy mapę. Drugi rodzaj przyjmuje
``Supplier``, ``BiConsumer``, jako kumulator oraz ``BiConsumer`` jako combiner. Metoda ``toMap`` wymaga podania 2 funkcji
określających klucz oraz wartość. Ciekawym zastosowaniem jest metoda ``Function.identity()`` zwracająca argument wejściowy
co jest odpowiednie zapisowi ``movie -> movie``
```
Map<String, Movie> collect = movies.stream()
        .collect(Collectors.toMap(Movie::getTitle, Function.identity()));
```
Inną metodą wartą uwagi jest ``Collectors.sumarizingInt`` lub inne wersje dla ``long`` czy ``double``. Metoda zwróci obiekt
``...SummaryStatistics`` zawierający użyteczne dane
```
IntSummaryStatistics collect1 = movies.stream()
        .collect(Collectors.summarizingInt(Movie::getLikes));
        
IntSummaryStatistics{count=3, sum=45, min=10, average=15,000000, max=20}
```

### Grupowanie danych
Dane możemy grupować z pomocą metody ``Collectors.groupingBy`` w wyniku otrzymamy mapę z kluczem będącym obiektem wg. 
którego grupujemy oraz wartością w postaci listy obiektów.
```
Map<Genre, List<Movie>> collect = movies.stream()
        .collect(Collectors.groupingBy(Movie::getGenre));
```
Możemy przekazać też drugi argument w postaci ``Collector`` co pozwli zmienić rodzaj kolekcji dla wartości
```
Map<Genre, Set<Movie>> collect = movies.stream()
        .collect(Collectors.groupingBy(Movie::getGenre, Collectors.toSet()));
```
Można również użyć również funkcji np. collect by uzyskać liczbę obiektów w danej kategorii.
```
Map<Genre, Long> collect2 = movies.stream()
        .collect(Collectors.groupingBy(
                Movie::getGenre, Collectors.counting()));
```
Inny ciekawy przykład pokazujący jak uzyskać film z największą ilością 'lajków' w swojej kategorii
```
Map<Genre, Movie> collect1 = movies.stream()
        .collect(Collectors.toMap(Movie::getGenre, Function.identity(),
                BinaryOperator.maxBy(Comparator.comparing(Movie::getLikes))));
```

### Dzielenie danych
Z pomocą metody ``Collectors.partitioningBy`` możemy usyskać 2 kolekcje, odpowiednio dla true i false.
```
Map<Boolean, List<Movie>> collect3 = movies.stream()
        .collect(Collectors.partitioningBy(m -> m.getLikes() > 15));
        
{false=[a, b], true=[c]}
```