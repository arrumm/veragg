package com.veragg.website.jobs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.veragg.website.domain.Document;
import com.veragg.website.services.DocumentService;
import com.veragg.website.services.DownloadService;

@Component
public class DownloadDocumentsJob {

    private final DocumentService documentService;

    private final DownloadService downloadService;

    @Autowired
    public DownloadDocumentsJob(DocumentService documentService, DownloadService downloadService) {
        this.documentService = documentService;
        this.downloadService = downloadService;
    }

    @Async
    @Scheduled(cron = "0 30 10 * * *")
    public void run() {
        List<Document> documents = documentService.getToDownload();
        documents.forEach(document -> {
            document.setFilePath(downloadService.downloadFile(document));
            documentService.save(document);
        });
    }

}
