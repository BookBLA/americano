package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberBookRepository extends JpaRepository<MemberBook, Long> {

    default MemberBook getByIdOrThrow(Long memberBookId) {
        return findById(memberBookId)
                .orElseThrow(() -> new BaseException(MemberBookExceptionType.MEMBER_BOOK_NOT_FOUND));
    }

    Optional<MemberBook> findById(Long memberBookId);

    boolean existsByMemberAndBook(Member member, Book book);

    long countByMember(Member member);

    List<MemberBook> findByMemberOrderByCreatedAt(Member member);
}
