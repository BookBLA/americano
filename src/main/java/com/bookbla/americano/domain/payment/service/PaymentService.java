package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.controller.dto.request.ApplePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.exception.PaymentExceptionType;
import com.bookbla.americano.domain.payment.infrastructure.google.GooglePaymentStrategy;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.repository.PaymentNotification;
import com.bookbla.americano.domain.payment.repository.PaymentNotificationRepository;
import com.bookbla.americano.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final PaymentNotificationRepository paymentNotificationRepository;
    private final PaymentStrategies paymentStrategies;
    private final GooglePaymentStrategy googlePaymentStrategy;
    private final PaymentRepository paymentRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    public PaymentPurchaseResponse orderBookmarkForApple(ApplePaymentInAppPurchaseRequest request, Long memberId) {
        PaymentStrategy applePaymentStrategy = paymentStrategies.findApple();

        Payment payment = applePaymentStrategy.getPaymentInformation(request.getTransactionId());
        payment.updateMemberId(memberId);
        paymentRepository.save(payment);

        MemberBookmark memberBookmark = findMemberBookmarkByMemberId(memberId);

        int updateCount = payment.getBookmark();
        memberBookmark.addBookmark(updateCount);

        return PaymentPurchaseResponse.from(payment);
    }

    public PaymentPurchaseResponse orderBookmarkForGoogle(GooglePaymentInAppPurchaseRequest request, Long memberId) {
        Payment payment = googlePaymentStrategy.getPaymentInformation(request);
        payment.updateMemberId(memberId);
        paymentRepository.save(payment);

        MemberBookmark memberBookmark = findMemberBookmarkByMemberId(memberId);

        int updateCount = payment.getBookmark();
        memberBookmark.addBookmark(updateCount);

        return PaymentPurchaseResponse.from(payment);
    }

    // https://developer.apple.com/documentation/storekit/in-app_purchase/original_api_for_in-app_purchase/handling_refund_notifications
    public boolean receiveAppleNotification(String signedPayload) {
        PaymentStrategy applePaymentStrategy = paymentStrategies.findApple();
        PaymentNotification paymentNotification = applePaymentStrategy.getNotificationInformation(signedPayload);
        paymentNotificationRepository.save(paymentNotification);

        if (paymentNotification.isRefund()) {
            return handleRefund(paymentNotification);
        }

        return true;
    }

    private boolean handleRefund(PaymentNotification paymentNotification) {
        String receipt = paymentNotification.getReceipt();
        Payment payment = findPaymentByReceipt(receipt);

        MemberBookmark memberBookmark = findMemberBookmarkByMemberId(payment.getMemberId());

        if (payment.canRefund(memberBookmark)) {
            memberBookmark.addBookmark(payment.getBookmark());
            return true;
        }

        return false;
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
