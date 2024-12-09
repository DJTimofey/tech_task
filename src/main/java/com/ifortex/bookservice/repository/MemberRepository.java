package com.ifortex.bookservice.repository;

import com.ifortex.bookservice.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MemberRepository extends JpaRepository<Member, Long> {
}