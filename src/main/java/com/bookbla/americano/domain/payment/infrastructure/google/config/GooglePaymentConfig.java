package com.bookbla.americano.domain.payment.infrastructure.google.config;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payments.google")
@Slf4j
public class GooglePaymentConfig {

    private String applicationName;
    private String jsonPath;
    private String packageName;

    public AndroidPublisher getAndroidPublisher() throws IOException, GeneralSecurityException {
        ClassPathResource resource = new ClassPathResource("bookbla-google-play-console.json");

        log.info("getAndroidPublisher resource : " + resource.getInputStream());
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream())
            .createScoped(Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER));

        return new AndroidPublisher.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            new HttpCredentialsAdapter(credentials))
            .setApplicationName(applicationName)
            .build();
    }
}
