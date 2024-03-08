package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSchedulerService {
    private final MemberPostcardRepository memberPostcardRepository;

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Seoul")
    public void initMemberFreePostcardSchedule(){
        memberPostcardRepository.initMemberFreePostcardCount();
    }
}
