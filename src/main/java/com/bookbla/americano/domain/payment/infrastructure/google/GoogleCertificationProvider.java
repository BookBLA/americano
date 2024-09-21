package com.bookbla.americano.domain.payment.infrastructure.google;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.payment.enums.PurchaseState;
import com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response.GooglePaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.infrastructure.google.config.GooglePaymentConfig;
import com.bookbla.americano.domain.payment.infrastructure.google.exception.GooglePaymentExceptionType;
import com.bookbla.americano.domain.payment.repository.PaymentRepository;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.api.services.androidpublisher.model.VoidedPurchase;
import com.google.api.services.androidpublisher.model.VoidedPurchasesListResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleCertificationProvider {

    private final GooglePaymentConfig googlePaymentConfig;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final MemberBookmarkRepository memberBookmarkRepository;

    public GooglePaymentPurchaseResponse getPurchaseReceipt(String productId,
        String purchaseToken) {
        try {
            AndroidPublisher publisher = googlePaymentConfig.getAndroidPublisher();

            // Google Play Developer API Method: purchases.products.get
            // https://developers.google.com/android-publisher/api-ref/rest/v3/purchases.products/get?hl=ko
            AndroidPublisher.Purchases.Products.Get get = publisher.purchases().products()
                .get(googlePaymentConfig.getPackageName(), productId, purchaseToken);

            ProductPurchase purchase = get.execute();

            verifyPurchaseState(purchase.getPurchaseState());

            return GooglePaymentPurchaseResponse.from(purchase);

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BaseException(GooglePaymentExceptionType.JSON_IO_ERROR);
        } catch (GeneralSecurityException e) {
            log.error(e.getMessage());
            throw new BaseException(GooglePaymentExceptionType.SECURITY_ERROR);
        }
    }

    public void refundVoidedPurchase(long startIndex, long maxResults) {
        try {
            AndroidPublisher publisher = googlePaymentConfig.getAndroidPublisher();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime oneDayAgo = now.minusDays(1);
            Long oneDayAgoMillis = oneDayAgo.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            String nextPageToken = null;

            while(true)  {
                AndroidPublisher.Purchases.Voidedpurchases.List request = publisher.purchases()
                    .voidedpurchases()
                    .list(googlePaymentConfig.getPackageName())
                    .setStartTime(oneDayAgoMillis);

                request.setStartIndex(startIndex);
                request.setMaxResults(maxResults);
                request.setToken(nextPageToken);

                VoidedPurchasesListResponse response = request.execute();

                log.info("response.getVoidedPurchases() length : " + response.getVoidedPurchases().size());
                for (VoidedPurchase voidedPurchase : response.getVoidedPurchases()) {
                    paymentRepository.findByOrderId(voidedPurchase.getOrderId())
                    .ifPresent(payment -> {
                        MemberBookmark memberBookmark = memberBookmarkRepository.findMemberBookmarkByMemberId(payment.getMemberId())
                            .orElseThrow(() -> new BaseException(MemberBookmarkExceptionType.ADMOB_TYPE_NOT_FOUND));

                        if (payment.canRefund(memberBookmark)) {
                            memberBookmark.refundBookmark(payment.getBookmark());
                        }
                        paymentRepository.delete(payment);
                    });

                }

                if (response.getTokenPagination() != null) {
                    nextPageToken = response.getTokenPagination().getNextPageToken();
                    log.info("nextPageToken: " + nextPageToken);
                } else {
                    break;
                }

                startIndex += maxResults;

            }

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BaseException(GooglePaymentExceptionType.JSON_IO_ERROR);
        } catch (GeneralSecurityException e) {
            log.error(e.getMessage());
            throw new BaseException(GooglePaymentExceptionType.SECURITY_ERROR);
        }
    }

    private void verifyPurchaseState(int purchaseState) {
        if (PurchaseState.from(purchaseState) != PurchaseState.PURCHASED) {
            throw new BaseException(GooglePaymentExceptionType.NOT_PURCHASE);
        }
    }

}
