package com.bookbla.americano.domain.scheduler;

import com.bookbla.americano.base.log.discord.BookblaLogDiscord;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.notification.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.bookbla.americano.domain.scheduler.Crons.EVERY_4_AM;

@RequiredArgsConstructor
@Service
@Slf4j
public class SchedulerService {

    private final MemberRepository memberRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MailService mailService;
    private final BookblaLogDiscord bookblaLogDiscord;

    @Transactional
    @Scheduled(cron = EVERY_4_AM, zone = "Asia/Seoul")
    public void deleteMemberEmailSchedule() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime twoDaysAgo = currentDate.minusDays(2);
            memberEmailRepository.deleteMemberEmailSchedule(twoDaysAgo);

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
