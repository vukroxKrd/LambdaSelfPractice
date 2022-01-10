package ru.brown;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.brown.utils.AntiSpy;
import ru.brown.utils.RandomCharGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestLambdas {
    List<Book> books;

    @Before
    public void setUp() {
        ListOfBooks listOfBooks = new ListOfBooks();
        books = listOfBooks.testBooks;
    }

    /**
     * Задублировал 2 книги вначале списка, чтобы иметь разницу с тестом №2
     */
    @Test
    public void countCompleteCollectionOfBooksTest() {
        long count = books.stream().count();
        System.out.println(count);
        Assert.assertEquals(237, count);
    }

    /**
     * 1) отфильтровать - оставить только те элементы, длина строки которых более 20 символов
     */
    @Test
    public void filterBooksTitleLengthMoreThan20Test() {
        long count = books.stream().filter(book -> book.getTitle().length() > 20).count();
        List<Book> distinctBooks = books.stream().filter(book -> book.getTitle().length() > 20).collect(Collectors.toList());

        IntStream.range(0, distinctBooks.size())
                .mapToObj(index -> "" + (index + 1) + ". " + distinctBooks.get(index))
                .forEach(System.out::println);

        Assert.assertEquals(62, count);
    }

    /**
     * 2) отфильтровать дубликаты
     */
    @Test
    public void filterDuplicateBooksTest() {
        long count = books.stream().distinct().count();
        System.out.println(count);
        Assert.assertEquals(235, count);
    }

    /**
     * 3) посчитать хешсуммы алгоритмом sha и положить в Map<String, String> где ключ - это первоначальная string - а значение - sha сумма
     */
    @Test
    public void encodeBooks() {
        Map<Integer, String> results = books.stream().collect(Collectors.toMap(Book::getId, AntiSpy::encode));
        results.forEach((k, v) -> System.out.println(k + " : " + v));
    }

    /**
     * отфильтровать и оставить только те значения, в торых имеются подряд идущие повторяющиеся символы - например "asdff" <- здесь это ff
     */
    @Test
    public void repeatingSymbolsInBooks() {

        List<Book> newList = books.stream().filter(book -> {
            /**Не уверен что это самый эффективный способ, но это единственное что пришло мне в голову :) */
            char[] word = book.getTitle().toCharArray();
            for (int i = 0, j = 1; j < word.length; i++, j++) {
                if (word[i] == word[j]) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());

        newList.stream().forEach(book -> System.out.println(book.getId() + " : " + book));
    }

    @Test
    public void repeatingSymbolsInBooksAndAttachingOneMoreSymbolToIt() {


        List<Book> newList = books.stream().filter(book -> {
            char[] word = book.getTitle().toCharArray();
            for (int i = 0, j = 1; j < word.length; i++, j++) {
                if (word[i] == word[j]) {
                    return true;
                }
            }
            return false;
        }).map(book -> {
            String title = book.getTitle();
            book.setTitle(title + RandomCharGenerator.rndChar());
            return book;
        }).collect(Collectors.toList());

        newList.stream().forEach(book -> System.out.println(book.getId() + " : " + book.getTitle()));
    }


}
