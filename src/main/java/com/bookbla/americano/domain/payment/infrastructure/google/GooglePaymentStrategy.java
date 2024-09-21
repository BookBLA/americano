package com.bookbla.americano.domain.payment.infrastructure.google;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response.GooglePaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class GooglePaymentStrategy {

    private final GoogleCertificationProvider googleCertificationProvider;
    private final PaymentRepository paymentRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    public Payment getPaymentInformation(GooglePaymentInAppPurchaseRequest request, Long memberId) {
        GooglePaymentPurchaseResponse response = googleCertificationProvider.getPurchaseReceipt(
            request.getProductId(), request.getPurchaseToken());

        PaymentTable paymentTable = PaymentTable.from(request.getProductId());

        Optional<Payment> payment = paymentRepository.findByOrderId(response.getOrderId());

        return payment.orElseGet(() -> {
            MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(memberId)
                .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.ADMOB_TYPE_NOT_FOUND));
            int bookmarkCount = paymentTable.getCount();

            memberBookmark.addBookmark(bookmarkCount);

            return Payment.builder()
                .money(paymentTable.getPrice())
                .bookmark(bookmarkCount)
                .paymentType(PaymentType.GOOGLE)
                .orderId(response.getOrderId())
                .purchaseToken(request.getPurchaseToken())
                .build();
        });


    }

}
