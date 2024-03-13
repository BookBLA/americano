package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberBook extends BaseInsertEntity {

    private static final int MAX_REVIEW_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private String review;

    private boolean isRepresentative;

    public void validateOwner(Member other) {
        if (!this.member.equals(other)) {
            throw new BaseException(MemberBookExceptionType.MEMBER_BOOK_NOT_FOUND);
        }
    }

    public void updateReview(String review) {
        if (review.length() > MAX_REVIEW_LENGTH) {
            throw new BaseException(MemberBookExceptionType.REVIEW_LENGTH_NOT_VALID);
        }
        this.review = review;
    }
}
