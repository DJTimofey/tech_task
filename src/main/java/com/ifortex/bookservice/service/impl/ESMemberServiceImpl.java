package com.ifortex.bookservice.service.impl;

import com.ifortex.bookservice.model.Book;
import com.ifortex.bookservice.model.Member;
import com.ifortex.bookservice.repository.BookRepository;
import com.ifortex.bookservice.repository.MemberRepository;
import com.ifortex.bookservice.service.MemberService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.*;

// Attention! It is FORBIDDEN to make any changes in this file!
@Service
public class ESMemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;

  public ESMemberServiceImpl(MemberRepository memberRepository, BookRepository bookRepository) {
    this.memberRepository = memberRepository;
  }

  @Override
  public Member findMember() {
    List<Member> members = memberRepository.findAll();

    return members.stream()
            .filter(member -> member.getBorrowedBooks().stream()
                    .anyMatch(book -> book.getGenres().contains("Романтика")))  // Проверяем, есть ли романтические книги
            .min(Comparator.comparing(member ->
                    member.getBorrowedBooks().stream()  // Получаем книги пользователя
                            .filter(book -> book.getGenres().contains("Романтика"))  // Оставляем только романтические книги
                            .map(Book::getPublicationDate)  // Извлекаем дату публикации
                            .min(Comparator.naturalOrder())  // Находим самую старую книгу
                            .orElse(LocalDateTime.MAX)))  // Если нет книг, возвращаем максимально возможную дату
            .orElse(null);  // Если такой пользователь не найден, возвращаем null
  }

  @Override
  public List<Member> findMembers() {
    List<Member> members = memberRepository.findAll();
    return members.stream()
            .filter(member -> member.getMembershipDate().getYear() == 2023 && member.getBorrowedBooks().isEmpty())
            .collect(Collectors.toList());
  }
}