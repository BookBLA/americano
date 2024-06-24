package com.bookbla.americano.domain.scheduler;

import java.time.LocalDateTime;

import com.bookbla.americano.base.log.discord.BookblaLogDiscord;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberPostcardRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.notification.service.MailService;
import com.bookbla.americano.base.log.discord.dto.AdminLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.scheduler.Crons.EVERY_4_AM;
import static com.bookbla.americano.domain.scheduler.Crons.EVERY_6_AM;

@RequiredArgsConstructor
@Service
@Slf4j
public class SchedulerService {

    private final MemberRepository memberRepository;
    private final MemberPostcardRepository memberPostcardRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MailService mailService;
    private final BookblaLogDiscord bookblaLogDiscord;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @Scheduled(cron = EVERY_6_AM, zone = "Asia/Seoul")
    public void initMemberFreePostcardSchedule() {
        try {
            memberPostcardRepository.initMemberFreePostcardCount();
            applicationEventPublisher.publishEvent(new AdminLogEvent("무료 엽서 초기화 작업에 성공하였습니다"));
        } catch (Exception e) {
            String txName = SchedulerService.class.getName() + "(initMemberFreePostcardSchedule)";
            String message = "무료 엽서 초기화 작업이 실패하였습니다. 확인 부탁드립니다.";

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaLogDiscord.sendMessage(message);
            log.debug("Exception in {}", SchedulerService.class.getName());
            log.error(e.toString());
        }
    }

    @Transactional
    @Scheduled(cron = EVERY_4_AM, zone = "Asia/Seoul")
    public void deleteMemberEmailSchedule() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime twoDaysAgo = currentDate.minusDays(2);
            memberEmailRepository.deleteMemberEmailSchedule(twoDaysAgo);

            String message = LocalDateTime.now() + "부터 2일 사이의 임시 메일 테이블 삭제 작업이 성공하였습니다~.";
            applicationEventPublisher.publishEvent(new AdminLogEvent(message));
        } catch (Exception e) {
            String txName = SchedulerService.class.getName() + "(deleteMemberEmailSchedule)";
            String message = "임시 메일 테이블 초기화 작업이 실패하였습니다. 확인 부탁드립니다.";

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaLogDiscord.sendMessage(message);
            log.debug("Exception in {}", SchedulerService.class.getName());
            log.error(e.toString());
        }
    }

    @Transactional
    @Scheduled(cron = EVERY_4_AM, zone = "Asia/Seoul")
    public void deleteMemberSchedule() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime thirtyDaysAgo = currentDate.minusDays(30);

            memberRepository.deleteAllByDeletedAtBeforeAndMemberStatus(thirtyDaysAgo);

            String message = LocalDateTime.now() + "부터 30일 사이의 탈퇴한 멤버 삭제 작업이 성공하였습니다";
            applicationEventPublisher.publishEvent(new AdminLogEvent(message));
        } catch (Exception e) {
            String txName = SchedulerService.class.getName() + "(deleteMemberSchedule)";
            String message = "멤버 테이블의 탈퇴한 멤버 삭제 작업이 실패하였습니다. 확인 부탁드립니다.";

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaLogDiscord.sendMessage(message);
            log.debug("Exception in {}", SchedulerService.class.getName());
            log.error(e.toString());
        }
    }
}
