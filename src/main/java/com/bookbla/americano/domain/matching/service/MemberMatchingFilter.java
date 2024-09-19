package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberMatchingFilter {

    private final MemberVerifyRepository memberVerifyRepository;

    /**
     * 학생증 상태 확인 필터링
     */
    public List<Map<Long, Long>> MemberVerifyFiltering(List<Map<Long, Long>> matchingMembers) {
        Set<Long> filteringMemberIds = new HashSet<>();

        // 중복 제외한 matchingMemberId (한명의 회원당 여러 책을 동록했을 수 있기때문에 중복 member id 값이 있을 수 있음)
        for (Map<Long, Long> matchingMemberId : matchingMembers) {
            filteringMemberIds.addAll(matchingMemberId.keySet());
        }
        // filteringMemberIds 필터링 -> filteredMatchMemberIds
        List<Long> filteredMatchMemberIds = memberVerifyRepository.findStudentIdByVerify(filteringMemberIds);

        return matchingMembers.stream()
                .filter(map -> map.keySet().stream().anyMatch(filteredMatchMemberIds::contains))
                .collect(Collectors.toList());
    }
}
