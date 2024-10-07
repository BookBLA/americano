package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update member_book set is_deleted = true where id = ?")
@Where(clause = "is_deleted = false")
@NamedEntityGraph(name = "WithBookAndAuthors", attributeNodes = {
        @NamedAttributeNode("book"),
        @NamedAttributeNode(value = "book", subgraph = "authors")
}, subgraphs = {
        @NamedSubgraph(name = "authors", attributeNodes = @NamedAttributeNode("authors"))
})
public class MemberBook extends BaseEntity {

    public static final int MEMBER_BOOK_REMOVABLE_COUNT = 2;
    public static final int MAX_MEMBER_BOOK_COUNT = 3;
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

    @Builder.Default
    private boolean isDeleted = false;

    public void validateOwner(Member other) {
        if (!this.member.equals(other)) {
            throw new BaseException(MemberBookExceptionType.MEMBER_BOOK_NOT_FOUND);
        }
    }

    public boolean isOwner(Member other) {
        return this.member.equals(other);
    }

    public void updateReview(String review) {
        if (review.length() > MAX_REVIEW_LENGTH) {
            throw new BaseException(MemberBookExceptionType.REVIEW_LENGTH_NOT_VALID);
        }
        this.review = review;
    }
}
