# Interfejsy funkcyjne

Interejs funkcyjny posiada tylko jedną metodę abstrakcyjną. Może posiadać metody domyślne oraz statyczne. Nazywane rónież
SAM Interfaces.

## Anonimmowe klasy wewnętrzne
Czasem, zamiast tworzenia klasy będącej implementacją interfejsu można stworzyć klasę lokalną będącą jednoraową implementacją.
Klasę nazywamy anonimową ponieważ nie posiada nawet nazwy, jest jedynie lokalną implementacją. Klasy takie mogą być
tworzone wewnątrz metod przyjmujących implementację interejsu jako parametr. Przykładowo możemy zdefioniować *interfejs 
funkcyjny*:
```
public interface Print {
    void print(String message);
}
```
Zamiast pisać jego implementację w postaci klasy:
```
public class ConsolePrinter  implements Print{
    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
```
Można wykorzystać klasę anonimową:
```
public class LambdaDemo {
    void show() {
        greet(new Print() {
            @Override
            public void print(String message) {
                System.out.println(message);
            }
        });
    }

    void greet(Print print) {
        print.print("Hello world");
    }
}
```
Wywołując metodę ``greet`` przyjmującą interfejs ``Print`` utworzyliśmy jego implementację.

## Wyrażenia lambda
Wyrażenia lambda pozwalają na uproszczony zapis klasy anonimowej. WYkorzystują tzw. *funkcję strzałkową* i reprezentują
paradygmat programowania funkcyjnego. Najprostrzy zapis wyglądałby następująco:
```
greet((String message) -> {
    System.out.println(message);
});
}

void greet(Print print) {
    print.print("Hello world");
}
```
Ponownie wywołujemy metodę ``greet``. Składnia wyrażenia lambda wymaga przekazania argumentów w nawiasach okrągłych.
W przypadku metody void będzie to pusty nawias. Nasępnie wykorzystujemy *funkcję strzałkową* -> i w nawiasach klamrowych
zapisujemy ciało nadpisywanej metody. 

### Lambda- zapis uproszczony
Powyższy przykład nie różni się specjalnie od zapisu kalsy anonimowej. W dalszym ciągu mamy kilka liniej kodu a celem
wyrażeń lambda jest uproszczenie zapisu, często do jednej linjki.  
- **Możemy pominąć typ argumentu** kompilator jest w stanie wywnioskować, że metoda przyjmuje *String* więc zapis ten jest
niepotrzebny i typ parametru wynika z sygnatury metody w interfejsie.  
- **W przypadku jenego argumentu nie musimy stosować nawiasów** nawiasy są konieczne w przypadku metod *void* lub gdy musimy
przekazać kilka argumentów.  
- **Nie potrzebujemy nawiasów klamrowych w przypadku jednej linijki kodu** nawiasy są konieczne jedynie przy kilkulinijkowej
implementacji.  
- **Nie musimy zapisywać *return* w przypadku jednej linijki** jak powyżej- w przypadku implementacji jednolinijkowych i metod
posiadających typ zwracany, *return* jest wymagany tylko w sytuacji gdy zapisujemy kilka lini kodu w nawiaach klamrowych.  
  
W rezultacie otrzymamy czytelny zapis w postaci jednej linijki:  
``greet(message -> System.out.println(message));``  
  

### this, różnice mięcy wyrażeniami lambda i klasami anonimowymi
W wyrażeniach lambda słowo kluczowe ``this`` odnosi się do klasy w której wyrażenie jest zapisywane. W przypadku klas anonimowych
``this`` osniesie się do samej siebie, konkretnej implementacji, nie interfejsu który klasa nadpisuje. Klasy anonimowe
mogą posiadać pola, wyrażenia lambda nie. Jeden i drugi zapis pozwala odnosić się do pól klasy w której się poruszamy.

## Referencje do metod
Z referencji do metod możemy skorzystać gdy w ramach implementacji interfejsu wzystko co robimy to przekazanie obiektu
do metody, jak w przykładnie z interfejsem ``Print``. Schemat takiego zapisu wygląda następująco: **klasa/obiekt::metoda** 
np. ``greet(System.out::println);`` gdzie ``System.out`` jest obiektem zawierającym metodę ``println`` oddzieloną dwoma dwukropkami.

## Referencje do konstruktorów
Dzialanie jest identyczne w przypadku konstruktorów wywoływanych jako ``obiekt::new``

## Zapis w postaci zmiennej
Istnieje jeszcze jedna forma zapisu lambdy w postaci zmiennych jak poniżej:  
``Consumer<String> print = System.out::println;``  
można tutaj stosować słowa kluczowe jak ``private`` czy ``final``. Taki zapis rónież pozwala na stosowanie referencji jako
skrótu do powtarzalnego, długiego zapisu, np:  
``private final Consumer<String> printUppercase = item -> System.out.println(item.toUpperCase());``

# Typy interfejsów funkcyjnych
Pakiet ``java.util.function`` dostarcza wiele interfejsów funkcyjnych, jednak możemy je pogrupować na 4 typy:
- **Consumer** przyjmująpojedyńczy argument, nie zwracają wyniku, są void.
- **Supplier** nie przyjmuje argumantu, zwraca obiekt.
- **Function** przyjmuje argument i zwraca wartośc, mappuje ją.
- **Predicate** przyjmuje argumet i zwraca wartość ``boolean``.
  
**Consumer** istnieje w różnych implementacjach, przyjmujących kilka argumentów lub wartośći prymitywne co pozwala uniknąć
autoboxingu. Przykładem może być metoda ``forEach`` pozwalająca wykonać jakąś operacę na każdym obiektcie kolekcji.  
Interejs funkcyjny ``consumer`` zawiera metodą abstrakcyjną ``accept`` a także przydatną metodę domyślną ``andThen`` 
pozwalającą na wykonanie łańcucha wywołań.
```
public class ConsumerDemo {
    private final Consumer<String> print = System.out::print;
    private final Consumer<String> printUppercase = item -> System.out.print(item.toUpperCase());
    private final Consumer<String> printLowercase = item -> System.out.print(item.toLowerCase());

    void show() {
        List<String> list = List.of("a", "b", "c");
        list.forEach(print.andThen(printUppercase).andThen(printLowercase));
    }
}

REZULTAT: aAabBbcCc
```
  
**Supplier** zawiera metodę abstrakcyjną ``get``. Funkcja nie zostanie wykonana bez wywołania tej metody. Podobnie jak Consumer,
Supplier występuje w odpowiednich wersjach dla typów prymitywnych jak np. ``IntSupplier``. 
```
public class SupplierDemo {
    private final Supplier<Double> getRandom = Math::random;
    void show(){
        System.out.println(getRandom.get());
    }
}
```
  
**Function** przyjmuje dwa argumenty- obiekt przekazywany i typ zwracany. Istnije również ``BiFunction`` przyjmujący dwa
obiekty i zwracający jeden rezultat. Istnieją rónież wersje dla typów prymitywnych jak ``IntFunction<R>`` dla których
deklarujemy tylko typ zwracany. Istnieją także wersje zwracające typ prymitywny przyjmując obiekt jak ``ToIntFunction<T>``,
tutaj podajemy obiekt, który pozwoli nam zwrócić ``int``. Istnieje też specjalna wesja zwracająca inne typy primitywne z
przekazanego jak ``IntToLongFunction`` przyjmująca ``int`` jako argument a zwracająca ``long``.
```
public class FunctionDemo {
    private final Function<String, Integer> mapStrToInt = String::length;
    void show(){
        mapStrToInt.apply("Sky");
    }
}
```
  
Łańcuch wywołań. Podobnie jak w przypadku Supplier'a, dla Function możemy zastosować metodę ``andThen`` pozwalającą
na złączenie kilku Function w jeden.
```
public class FunctionDemo {
    private final Function<String, String> replaceColon = str -> str.replace(":", "=");
    private final Function<String, String> addBraces = str -> "{" + str + "}";

    void show(){
        System.out.println(replaceColon.andThen(addBraces).apply("key:value"));
    }
}
```
  
Alternatywnie można wykorzystać metodę ``compose`` działającą w taki sam sposób, ale w odwrotnej kolejności  
``System.out.println(addBraces.compose(replaceColon).apply("key:value"));``  
Podajemy więc kolejne funkcje od końca.   
  
**Predicate** stosowany głównie przy filtrowaniu, w ciele metody sprawdzamy czy przekazany obiekt spełnia jakieś kryteria.
Podobnie jak w poprzednich typach, Predicate również można łączyć aby otrzymać bardziej złożony warunek z wykorzystaniem
metod ``.and``, ``.or``
```
    void show(){
        Predicate<String> hasLeftBrace = str -> str.startsWith("{");
        Predicate<String> hasRightBrace = str -> str.endsWith("}");
        System.out.println(hasLeftBrace.and(hasRightBrace).test("{LOL}"));;
    }
```
W przyapadku Predicate możemy rónwnież zastosować metodę ``.negate``: ``hasLeftBrace.negate()``.

#### Binary Operator
Specjalny typ Funkcion. Pozwalają na funkcyjny zapis funkcji artmetycznych. Pozwala to tworzyć funkce matematyczne.
```
public class BinaryOperatorDemo {
    BinaryOperator<Integer> add = (a, b) -> a + b;
    Function<Integer, Integer> square = a -> a * a;

    BiFunction<Integer, Integer, Integer> addAndSquare = add.andThen(square);

    void show() {
        System.out.println(add.andThen(square).apply(1,2));
        System.out.println(addAndSquare.apply(1,2));
    }
}
```

#### Unary Operator
Kolejny specjalny typ interfeju Function. Od samego Function różni się tym, że wymaga aby obikety przekazany i zwrócony
był tego samego typu. Nie posłuży nam do mapowania obiektów na inny. Można w ten sposób wymusić pewne zachowania, np. 
możemy posłużyć się takim interfejsem w celu zmiany np. nazwiska jakiejś osoby, wówczas na wejściu i wyjściu mamy ten sam
obiekt typu Person. Może również posłużyć do klonowania obiektów. 