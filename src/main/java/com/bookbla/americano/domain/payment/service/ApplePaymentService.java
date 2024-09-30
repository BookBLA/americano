package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.controller.dto.request.ApplePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.exception.PaymentExceptionType;
import com.bookbla.americano.domain.payment.infrastructure.apple.ApplePaymentStrategy;
import com.bookbla.americano.domain.payment.repository.PaymentNotificationRepository;
import com.bookbla.americano.domain.payment.repository.PaymentRepository;
import com.bookbla.americano.domain.payment.repository.entity.Payment;
import com.bookbla.americano.domain.payment.repository.entity.PaymentNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ApplePaymentService {

    private final PaymentNotificationRepository paymentNotificationRepository;
    private final ApplePaymentStrategy applePaymentStrategy;
    private final PaymentRepository paymentRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;
    private final MemberRepository memberRepository;

    public PaymentPurchaseResponse orderBookmarkForApple(ApplePaymentInAppPurchaseRequest request, Long memberId) {

        Payment payment = applePaymentStrategy.getPaymentInformation(request.getTransactionId());
        payment.updateMemberId(memberId);
        paymentRepository.save(payment);

        MemberBookmark memberBookmark = findMemberBookmarkByMemberId(memberId);

        int updateCount = payment.getBookmark();
        memberBookmark.addBookmark(updateCount);

        return PaymentPurchaseResponse.from(payment);
    }

    // https://developer.apple.com/documentation/storekit/in-app_purchase/original_api_for_in-app_purchase/handling_refund_notifications
    @Transactional(noRollbackFor = {BaseException.class, Exception.class})
    public void receiveAppleNotification(String signedPayload) {
        PaymentNotification paymentNotification = applePaymentStrategy.getNotificationInformation(signedPayload);
        paymentNotificationRepository.save(paymentNotification);

        if (paymentNotification.isRefund()) {
            handleRefund(paymentNotification);
        }
    }

    private void handleRefund(PaymentNotification paymentNotification) {
        Payment payment = findPaymentByReceipt(paymentNotification.getReceipt());
        if (!memberRepository.existsById(payment.getMemberId())) {
            return;
        }

        MemberBookmark memberBookmark = findMemberBookmarkByMemberId(payment.getMemberId());

        if (payment.canRefund(memberBookmark)) {
            memberBookmark.refundBookmark(payment.getBookmark());
        }
    }

    private Payment findPaymentByReceipt(String receipt) {
        return paymentRepository.findByReceipt(receipt)
                .orElseThrow(() -> new BaseException(PaymentExceptionType.PAYMENT_TYPE_NOT_FOUND));
    }


    private MemberBookmark findMemberBookmarkByMemberId(Long payment) {
        return memberBookmarkRepository.findMemberBookmarkByMemberId(payment)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));
    }
}
