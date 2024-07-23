package com.bookbla.americano.domain.payment.infrastructure.apple;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.payment.infrastructure.apple.exception.ApplePaymentExceptionType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class AppleCertificationProvider {

    private static final String CERTIFICATION_TYPE = "X.509";

    private final X509Certificate appleRootCert;

    public AppleCertificationProvider(
            @Value("${payments.apple.root-certification-x509-pem}") String appleRootCertification
    ) {
        this.appleRootCert = toX509Certificate(appleRootCertification);
    }

    private X509Certificate toX509Certificate(String rawCertification) {
        byte[] decoded = Base64.getDecoder().decode(rawCertification);

        try {
            CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATION_TYPE);
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(decoded));
        } catch (CertificateException e) {
            throw new RuntimeException("인증서를 역직렬하는데 실패했습니다.", e);
        }
    }

    public void validateCertificate(List<String> certificateChain) {
        validateRootCertificate(certificateChain);
        validateCertificateChain(certificateChain);
    }

    private void validateRootCertificate(List<String> certificateChain) {
        int lastIndex = certificateChain.size() - 1;
        String rootCertificate = certificateChain.get(lastIndex);
        X509Certificate x509RootCertificate = toX509Certificate(rootCertificate);

        if (!x509RootCertificate.equals(appleRootCert)) {
            throw new BaseException(ApplePaymentExceptionType.INVALID_APPLE_KEY);
        }
    }

    private void validateCertificateChain(List<String> certificateChain) {
        IntStream.range(0, certificateChain.size() - 1)
                .forEach(i -> verifyCertificateChain(certificateChain, i));
    }

    private void verifyCertificateChain(List<String> certificateChain, int i) {
        X509Certificate current = toX509Certificate(certificateChain.get(i));
        X509Certificate next = toX509Certificate(certificateChain.get(i + 1));
        try {
            current.verify(next.getPublicKey());
        } catch (Exception e) {
            throw new BaseException(ApplePaymentExceptionType.INVALID_APPLE_KEY);
        }
    }
}
