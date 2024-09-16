package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.request.PaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.infrastructure.google.GooglePaymentStrategy;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class PaymentService {

    private final PaymentStrategies paymentStrategies;
    private final GooglePaymentStrategy googlePaymentStrategy;
    private final PaymentRepository paymentRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    public PaymentPurchaseResponse orderBookmarkForApple(PaymentInAppPurchaseRequest request, Long memberId) {
        PaymentStrategy paymentStrategy = paymentStrategies.find("apple");

        Payment payment = paymentStrategy.getPaymentInformation(request.getTransactionId());
        payment.updateMemberId(memberId);
        paymentRepository.save(payment);

        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));

        int updateCount = payment.getBookmark();
        memberBookmark.addBookmark(updateCount);

        return PaymentPurchaseResponse.from(payment);
    }

    public PaymentPurchaseResponse orderBookmarkForGoogle(GooglePaymentInAppPurchaseRequest request, Long memberId) {

        Payment payment = googlePaymentStrategy.getPaymentInformation(request);
        payment.updateMemberId(memberId);
        paymentRepository.save(payment);

        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
            .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));

        int updateCount = payment.getBookmark();
        memberBookmark.addBookmark(updateCount);

        return PaymentPurchaseResponse.from(payment);
    }
}
