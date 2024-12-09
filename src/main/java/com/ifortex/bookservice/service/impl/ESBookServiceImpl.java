package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.dto.SearchCriteria;
import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.service.BookService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.*;

// Attention! It is FORBIDDEN to make any changes in this file!
@Service
public class ESBookServiceImpl implements BookService {

  private final BookRepository bookRepository;

  public ESBookServiceImpl(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  @Override
  public Map<String, Long> getBooks() {
    List<Book> books = bookRepository.findAll();

    Map<String, Long> genreCount = books.stream()
            .flatMap(book -> book.getGenres().stream())  // Распаковываем жанры
            .collect(Collectors.groupingBy(genre -> genre, Collectors.counting()));  // Подсчитываем количество книг для каждого жанра

    // Сортируем по убыванию и собираем результат в LinkedHashMap
    return genreCount.entrySet().stream()
            .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue())) // Сортируем по убыванию
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)); // Используем LinkedHashMap
  }

  @Override
  public List<Book> getAllByCriteria(SearchCriteria searchCriteria) {
    List<Book> books = bookRepository.findAll();

    // Фильтрация по критериям
    return books.stream()
            .filter(book -> (searchCriteria.getTitle() == null || book.getTitle().contains(searchCriteria.getTitle())))
            .filter(book -> (searchCriteria.getAuthor() == null || book.getAuthor().contains(searchCriteria.getAuthor())))
            .filter(book -> (searchCriteria.getGenre() == null || book.getGenres().contains(searchCriteria.getGenre())))
            .filter(book -> (searchCriteria.getDescription() == null || book.getDescription().contains(searchCriteria.getDescription())))
            .filter(book -> (searchCriteria.getYear() == null || book.getPublicationDate().getYear() == searchCriteria.getYear()))
            .sorted(Comparator.comparing(Book::getPublicationDate))  // Сортировка по дате публикации
            .collect(Collectors.toList());
  }
}