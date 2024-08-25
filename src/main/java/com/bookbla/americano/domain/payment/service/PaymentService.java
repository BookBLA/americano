package com.bookbla.americano.domain.payment.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.controller.dto.request.PaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.enums.PaymentType;
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
    private final PaymentRepository paymentRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    public PaymentPurchaseResponse orderBookmark(String payType, PaymentInAppPurchaseRequest request, Long memberId) {
        PaymentType paymentType = PaymentType.from(payType);
        PaymentStrategy paymentStrategy = paymentStrategies.get(paymentType);

        Payment payment = paymentStrategy.getPaymentInformation(request.getTransactionId());
        payment.updateMemberId(memberId);
        paymentRepository.save(payment);

        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.MEMBER_ID_NOT_EXISTS));

        int updateCount = payment.getBookmarks();
        memberBookmark.addBookmark(updateCount);

        return PaymentPurchaseResponse.from(payment);
    }
}