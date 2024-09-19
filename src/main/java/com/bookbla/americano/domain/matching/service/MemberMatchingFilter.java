package com.bookbla.americano.domain.matching.service;

import com.bookbla.americano.domain.member.repository.MemberVerifyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberMatchingFilter {

    private final MemberVerifyRepository memberVerifyRepository;

    public List<Map<Long, Long>> MemberVerifyFiltering(List<Map<Long, Long>> matchingMembers) {

        /** 학생증 상태 필터링 **/

        Set<Long> filteringMemberIds = new HashSet<>();

        // 중복 제외한 matchingMemberId (한명의 회원당 여러 책을 동록했을 수 있기때문에 중복 member id 값이 있을 수 있음)
        for (Map<Long, Long> matchingMemberId : matchingMembers) {
            filteringMemberIds.addAll(matchingMemberId.keySet());
        }
        // filteringMemberIds 필터링 -> filteredMatchMemberIds
        List<Long> filteredMatchMemberIds = memberVerifyRepository.findStudentIdByVerify(filteringMemberIds);

/*        List<Map<Long, Long>> filteredMatchMembers = matchingMembers.stream()
                .filter(map -> map.keySet().stream().anyMatch(filteredMatchMemberIds::contains))
                .collect(Collectors.toList());
*/

        List<Map<Long, Long>> filteredMatchMembers = new ArrayList<>();
        for (Map<Long, Long> match : matchingMembers) {

            // filteredMatchMemberIds에 포함된 키를 가진 match만 추가
            for (Long key : match.keySet()) {
                if (filteredMatchMemberIds.contains(key)) {
                    filteredMatchMembers.add(match);
                    break;  // 하나라도 조건을 만족하면 해당 Map을 추가하고 반복 종료
                }
            }
        }

        return filteredMatchMembers;
    }
}
