package com.bookbla.americano.domain.payment.infrastructure.google;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.enums.PurchaseState;
import com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response.GooglePaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.infrastructure.google.config.GooglePaymentConfig;
import com.bookbla.americano.domain.payment.infrastructure.google.exception.GooglePaymentExceptionType;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import java.io.IOException;
import java.security.GeneralSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleCertificationProvider {

    private final GooglePaymentConfig googlePaymentConfig;

    public GooglePaymentPurchaseResponse getPurchaseReceipt(String productId, String purchaseToken) {
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

    public void verifyPurchaseState(int purchaseState) {
        if (PurchaseState.from(purchaseState) != PurchaseState.PURCHASED) {
            throw new BaseException(GooglePaymentExceptionType.NOT_PURCHASE);
        }
    }

}
