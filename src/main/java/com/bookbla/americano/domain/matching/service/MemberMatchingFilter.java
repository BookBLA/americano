package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberMatchingFilter {

    private final MemberVerifyRepository memberVerifyRepository;
    private final PostcardRepository postcardRepository;

    /**
     * 학생증 상태 확인 필터링
     */
    public List<Map<Long, Long>> memberVerifyFiltering(List<Map<Long, Long>> matchingMembers) {
        Set<Long> filteringMemberIds = new HashSet<>();

        // 중복 제외한 matchingMemberId (한명의 회원당 여러 책을 동록했을 수 있기때문에 중복 member id 값이 있을 수 있음)
        for (Map<Long, Long> matchingMemberId : matchingMembers) {
            filteringMemberIds.addAll(matchingMemberId.keySet());
        }
        // filteringMemberIds 필터링 -> filteredMatchMemberIds
        List<Long> filteredMatchMemberIds = memberVerifyRepository.getMemberIdsByStudentIdVerify(filteringMemberIds);

        return matchingMembers.stream()
                .filter(map -> map.keySet().stream().anyMatch(filteredMatchMemberIds::contains))
                .collect(Collectors.toList());
    }

    /**
     * 엽서 거절 조건 필터링
     */
    public List<Map<Long, Long>> memberRefusedAtFiltering(Long sendMemberId, List<Map<Long, Long>> matchingMembers) {
        Set<Long> filteringMemberIds = new HashSet<>();

        // 중복 제외한 matchingMemberId (한명의 회원당 여러 책을 동록했을 수 있기때문에 중복 member id 값이 있을 수 있음)
        for (Map<Long, Long> matchingMemberId : matchingMembers) {
            filteringMemberIds.addAll(matchingMemberId.keySet());
        }

        List<Long> filteredMatchMemberIds = postcardRepository.getReceiveIdsRefusedAt(sendMemberId, filteringMemberIds);

        return matchingMembers.stream()
                .filter(map -> map.keySet().stream().anyMatch(filteredMatchMemberIds::contains))
                .collect(Collectors.toList());
    }
}
