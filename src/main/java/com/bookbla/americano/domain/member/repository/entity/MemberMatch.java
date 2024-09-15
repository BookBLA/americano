package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.book.repository.entity.Book;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_member_id")
    private Member appMember; // 앱 사용 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_member_id")
    private Member matchedMember; // 매칭된 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matched_book_id")
    private Book matchedBook;  // 매칭된 회원 책

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passed_member_id")
    private Member passedMember; // 거절된 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passed_book_id")
    private Book passedBook;  // 거절된 회원의 책
}
