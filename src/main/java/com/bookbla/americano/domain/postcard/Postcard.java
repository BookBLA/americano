package com.bookbla.americano.domain.postcard;

import com.bookbla.americano.domain.memberask.MemberReply;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
public class Postcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_reply_id")
    private MemberReply memberReply;

    @ManyToOne
    @JoinColumn(name = "postcard_type_id")
    private PostcardType postcardType;

    private String answerContent;

    private String imageURL;

    private PostcardStatus postcardStatus;

}
