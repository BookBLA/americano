package com.bookbla.americano.domain.member.service.impl;

import java.time.LocalDateTime;

import com.bookbla.americano.base.log.discord.BookblaDiscord;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.service.MailService;
import com.bookbla.americano.domain.member.service.MemberSchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberSchedulerServiceImpl implements MemberSchedulerService {

    private final MemberPostcardRepository memberPostcardRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MailService mailService;
    private final BookblaDiscord bookblaDiscord;

    @Transactional
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @Override
    public void initMemberFreePostcardSchedule() {
        try {
            memberPostcardRepository.initMemberFreePostcardCount();
        } catch (Exception e) {
            String txName = MemberSchedulerService.class.getName() + "(initMemberFreePostcardSchedule)";
            String message = "무료 엽서 초기화 작업이 실패하였습니다. 확인 부탁드립니다.";

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaDiscord.sendMessage(message);
            log.debug("Exception in {}", MemberSchedulerService.class.getName());
            log.error(e.toString());
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
    @Override
    public void deleteMemberEmailSchedule() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime twoDaysAgo = currentDate.minusDays(2);
            memberEmailRepository.deleteMemberEmailSchedule(twoDaysAgo);

        } catch (Exception e) {
            String txName = MemberSchedulerService.class.getName() + "(deleteMemberEmailSchedule)";
            String message = "임시 메일 테이블 초기화 작업이 실패하였습니다. 확인 부탁드립니다.";

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaDiscord.sendMessage(message);
            log.debug("Exception in {}", MemberSchedulerService.class.getName());
            log.error(e.toString());
        }
    }
}