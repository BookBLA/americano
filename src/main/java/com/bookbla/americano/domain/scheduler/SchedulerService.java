package com.bookbla.americano.domain.scheduler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import com.bookbla.americano.base.log.discord.BookblaLogDiscord;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberEmailRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.notification.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bookbla.americano.domain.scheduler.Crons.EVERY_0_AM;
import static com.bookbla.americano.domain.scheduler.Crons.EVERY_4_AM;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class SchedulerService {

    private static final String CLRF = "\n\n";

    private final MemberRepository memberRepository;
    private final MemberEmailRepository memberEmailRepository;
    private final MailService mailService;
    private final BookblaLogDiscord bookblaLogDiscord;
    private final MemberBookmarkRepository memberBookmarkRepository;

    @Scheduled(cron = EVERY_4_AM, zone = "Asia/Seoul")
    public void deleteMemberEmailSchedule() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime twoDaysAgo = currentDate.minusDays(2);
            memberEmailRepository.deleteMemberEmailSchedule(twoDaysAgo);

        } catch (Exception e) {
            String txName = SchedulerService.class.getName() + "(deleteMemberEmailSchedule)";
            String message = "임시 메일 테이블 초기화 작업이 실패하였습니다. 확인 부탁드립니다." + CLRF
                    + e.getMessage() + CLRF
                    + stackTraceToString(e);

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaLogDiscord.sendMessage(message);

            log.debug("Exception in {}", SchedulerService.class.getName());
            log.error(e.toString());
            log.error(stackTraceToString(e));
        }
    }

    @Scheduled(cron = EVERY_4_AM, zone = "Asia/Seoul")
    public void deleteMemberSchedule() {
        try {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime thirtyDaysAgo = currentDate.minusDays(30);

            memberRepository.deleteAllByDeletedAtBeforeAndMemberStatus(thirtyDaysAgo);
        } catch (Exception e) {
            String txName = SchedulerService.class.getName() + "(deleteMemberSchedule)";
            String message = "멤버 테이블의 탈퇴한 멤버 삭제 작업이 실패하였습니다. 확인 부탁드립니다. " + CLRF
                    + e.getMessage() + CLRF
                    + stackTraceToString(e);

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaLogDiscord.sendMessage(message);

            log.debug("Exception in {}", SchedulerService.class.getName());
            log.error(e.toString());
            log.error(stackTraceToString(e));
        }
    }

    @Scheduled(cron = EVERY_0_AM, zone = "Asia/Seoul")
    public void resetAdmobCount() {
        try {
            memberBookmarkRepository.resetAdmobCount(2);
        } catch (Exception e) {
            String txName = SchedulerService.class.getName() + "(resetAdmobCount)";
            String message = "애드몹 시청 횟수 초기화 기능 실패  " + CLRF
                    + e.getMessage() + CLRF
                    + stackTraceToString(e);

            mailService.sendTransactionFailureEmail(txName, message);
            bookblaLogDiscord.sendMessage(message);

            log.debug("Exception in {}", SchedulerService.class.getName());
            log.error(e.toString());
            log.error(stackTraceToString(e));
        }
    }

    private String stackTraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
