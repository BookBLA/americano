package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberPostcardService {

    private final PostcardRepository postcardRepository;

    public List<Long> getReceiveByIds(Long sendMemberId, PostcardStatus postcardStatus) {
        return postcardRepository.findReceiveByIdsWithPostcardStatus(sendMemberId, postcardStatus);
    }

    public List<Long> getReceiveByIdsRefused(Long sendMemberId, List<Postcard> postcards) {
        return postcardRepository.findReceiveByIdsRefused(sendMemberId, postcards);
    }
}
