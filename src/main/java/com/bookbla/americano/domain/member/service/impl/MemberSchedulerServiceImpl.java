package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.service.MemberSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSchedulerServiceImpl implements MemberSchedulerService {
    private final MemberPostcardRepository memberPostcardRepository;

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Seoul")
    public void initMemberFreePostcardSchedule(){
        memberPostcardRepository.initMemberFreePostcardCount();
    }
}