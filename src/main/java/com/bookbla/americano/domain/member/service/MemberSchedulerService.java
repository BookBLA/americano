package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.repository.MemberCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberSchedulerService {
    private final MemberCoinRepository memberCoinRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void run(){
        memberCoinRepository.initMemberCoinCount();
    }
}
