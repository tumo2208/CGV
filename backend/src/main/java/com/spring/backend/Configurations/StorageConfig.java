package com.spring.backend.Configurations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class StorageConfig {

//    @Value("${gcp.key.path}")
//    private String gcpKeyPath;
//
//    @Value("${gcp.project.id}")
//    private String gcpProjectId;
//
//    @Bean
//    public Storage storage() throws IOException {
//        GoogleCredentials credentials = GoogleCredentials.fromStream(
//                new ClassPathResource(gcpKeyPath).getInputStream()
//        );
//
//        return StorageOptions.newBuilder()
//                .setCredentials(credentials)
//                .setProjectId(gcpProjectId)
//                .build()
//                .getService();
//    }

    @Bean
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }
}
