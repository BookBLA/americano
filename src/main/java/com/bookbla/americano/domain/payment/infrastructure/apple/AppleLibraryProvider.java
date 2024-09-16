package com.bookbla.americano.domain.payment.infrastructure.apple;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import com.apple.itunes.storekit.client.APIException;
import com.apple.itunes.storekit.client.AppStoreServerAPIClient;
import com.apple.itunes.storekit.model.Environment;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.apple.itunes.storekit.verification.SignedDataVerifier;
import com.apple.itunes.storekit.verification.VerificationException;
import com.bookbla.americano.AmericanoApplication;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.infrastructure.apple.config.ApplePaymentsConfig;
import com.bookbla.americano.domain.payment.infrastructure.apple.exception.ApplePaymentExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
class AppleLibraryProvider {

    private final ApplePaymentsConfig applePaymentsConfig;

    public String getSignedTransactionInfo(String transactionId) {
        String issuerId = applePaymentsConfig.getIssuerId();
        String keyId = applePaymentsConfig.getKeyId();
        String bundleId = applePaymentsConfig.getBundleId();
        String signedKey = applePaymentsConfig.getKeyFile();
        Environment environment = Environment.fromValue(applePaymentsConfig.getEnviornment());

        try {
            AppStoreServerAPIClient client = new AppStoreServerAPIClient(signedKey, keyId, issuerId, bundleId, environment);
            return client.getTransactionInfo(transactionId).getSignedTransactionInfo();
        } catch (APIException | IOException e) {
            log.error(e.getMessage());
            throw new BaseException(ApplePaymentExceptionType.API_EXCEPTION, e);
        }
    }

    public void validateTransaction(String transactionId) {
        SignedDataVerifier signedDataVerifier = createSignedDataVerifier();
        try {
            signedDataVerifier.verifyAndDecodeTransaction(transactionId);
        } catch (VerificationException e) {
            log.error(e.getMessage());
            throw new BaseException(ApplePaymentExceptionType.INVALID_APPLE_KEY, e);
        }
    }

    private Set<InputStream> readCAS() {
        return Set.of(
                AmericanoApplication.class.getResourceAsStream("/AppleRootCA-G2.cer"),
                AmericanoApplication.class.getResourceAsStream("/AppleRootCA-G3.cer"),
                AmericanoApplication.class.getResourceAsStream("/AppleComputerRootCertificate.cer"),
                AmericanoApplication.class.getResourceAsStream("/AppleIncRootCertificate.cer")
        );
    }

    public ResponseBodyV2DecodedPayload getNotificationPayload(String signedPayload) {
        SignedDataVerifier signedDataVerifier = createSignedDataVerifier();
        try {
            return signedDataVerifier.verifyAndDecodeNotification(signedPayload);
        } catch (VerificationException e) {
            log.error(e.getMessage());
            throw new BaseException(ApplePaymentExceptionType.INVALID_APPLE_KEY, e);
        }
    }

    private @NotNull SignedDataVerifier createSignedDataVerifier() {
        String bundleId = applePaymentsConfig.getBundleId();
        Environment environment = Environment.fromValue(applePaymentsConfig.getEnviornment());
        long appId = applePaymentsConfig.getAppId();

        Set<InputStream> rootCAS = readCAS();

        return new SignedDataVerifier(rootCAS, bundleId, appId, environment, true);
    }
}
