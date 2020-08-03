// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.storage.file.share;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Map;

import com.azure.core.http.rest.PagedIterable;
import com.azure.core.util.Context;
import com.azure.core.util.polling.SyncPoller;
import com.azure.storage.file.share.models.HandleItem;
import com.azure.storage.file.share.models.ShareAccessPolicy;
import com.azure.storage.file.share.models.ShareFileCopyInfo;
import com.azure.storage.file.share.models.ShareFileHttpHeaders;
import com.azure.storage.file.share.models.ShareFileRange;
import com.azure.storage.file.share.models.ShareServiceProperties;
import com.azure.storage.file.share.models.ShareSignedIdentifier;
import com.azure.storage.file.share.models.ShareStorageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WARNING: MODIFYING THIS FILE WILL REQUIRE CORRESPONDING UPDATES TO README.md FILE. LINE NUMBERS 
 * ARE USED TO EXTRACT APPROPRIATE CODE SEGMENTS FROM THIS FILE. ADD NEW CODE AT THE BOTTOM TO AVOID CHANGING
 * LINE NUMBERS OF EXISTING CODE SAMPLES.
 * 
 * Code samples for the README.md
 */
public class ReadmeSamples {
    private static final String ACCOUNT_NAME = System.getenv("AZURE_STORAGE_ACCOUNT_NAME");
    private static final String SAS_TOKEN = System.getenv("PRIMARY_SAS_TOKEN");
    private static final String CONNECTION_STRING = System.getenv("AZURE_CONNECTION_STRING");

    ShareServiceClient shareServiceClient = new ShareServiceClientBuilder().buildClient();
    ShareClient shareClient = new ShareClientBuilder().buildClient();
    ShareDirectoryClient directoryClient = new ShareFileClientBuilder().buildDirectoryClient();
    ShareFileClient fileClient = new ShareFileClientBuilder().buildFileClient();

    private Logger logger = LoggerFactory.getLogger(ReadmeSamples.class);
    
    public void createShareSeviceClient() {
        String shareServiceURL = String.format("https://%s.file.core.windows.net", ACCOUNT_NAME);
        ShareServiceClient shareServiceClient = new ShareServiceClientBuilder().endpoint(shareServiceURL)
            .sasToken(SAS_TOKEN).buildClient();
    }

    public void createShareClient() {
        String shareName = "testshare";
        String shareURL = String.format("https://%s.file.core.windows.net", ACCOUNT_NAME);
        ShareClient shareClient = new ShareClientBuilder().endpoint(shareURL)
            .sasToken(SAS_TOKEN).shareName(shareName).buildClient();
    }

    public void createDirectoryClient() {
        String shareName = "testshare";
        String directoryPath = "directoryPath";
        String directoryURL = String.format("https://%s.file.core.windows.net", ACCOUNT_NAME);
        ShareDirectoryClient directoryClient = new ShareFileClientBuilder().endpoint(directoryURL)
            .sasToken(SAS_TOKEN).shareName(shareName).resourcePath(directoryPath).buildDirectoryClient();
    }

    public void createFileClient() {
        String shareName = "testshare";
        String directoryPath = "directoryPath";
        String fileName = "fileName";
        String fileURL = String.format("https://%s.file.core.windows.net", ACCOUNT_NAME);
        ShareFileClient fileClient = new ShareFileClientBuilder().connectionString(CONNECTION_STRING)
            .endpoint(fileURL).shareName(shareName).resourcePath(directoryPath + "/" + fileName).buildFileClient();
    }

    public void createShare() {
        String shareName = "testshare";
        shareServiceClient.createShare(shareName);
    }

    public void createSnapshotOnShare() {
        String shareName = "testshare";
        ShareClient shareClient = shareServiceClient.getShareClient(shareName);
        shareClient.createSnapshot();
    }

    public void createDirectory() {
        String dirName = "testdir";
        shareClient.createDirectory(dirName);
    }

    public void createSubDirectory() {
        String subDirName = "testsubdir";
        directoryClient.createSubdirectory(subDirName);
    }

    public void createFile() {
        String fileName = "testfile";
        long maxSize = 1024;
        directoryClient.createFile(fileName, maxSize);
    }

    public void getShareList() {
        shareServiceClient.listShares();
    }
    
    public void getSubDirectoryAndFileList() {
        directoryClient.listFilesAndDirectories();
    }

    public void getRangeList() {
        fileClient.listRanges();
    }

    public void deleteShare() {
        shareClient.delete();
    }

    public void deleteDirectory() {
        String dirName = "testdir";
        shareClient.deleteDirectory(dirName);
    }

    public void deleteSubDirectory() {
        String subDirName = "testsubdir";
        directoryClient.deleteSubdirectory(subDirName);
    }

    public void deleteFile() {
        String fileName = "testfile";
        directoryClient.deleteFile(fileName);
    }

    public void copyFile() {
        String sourceURL = "https://myaccount.file.core.windows.net/myshare/myfile";
        Duration pollInterval = Duration.ofSeconds(2);
        SyncPoller<ShareFileCopyInfo, Void> poller = fileClient.beginCopy(sourceURL, null, pollInterval);
    }

    public void abortCopyFile() {
        fileClient.abortCopy("copyId");
    }

    public void uploadDataToStorage() {
        String uploadText = "default";
        InputStream data = new ByteArrayInputStream(uploadText.getBytes(StandardCharsets.UTF_8));
        fileClient.upload(data, uploadText.length());
    }

    public void uploadFileToStorage() {
        String filePath = "${myLocalFilePath}";
        fileClient.uploadFromFile(filePath);
    }

    public void downloadDataFromFileRange() {
        ShareFileRange fileRange = new ShareFileRange(0L, 2048L);
        OutputStream stream = new ByteArrayOutputStream();
        fileClient.downloadWithResponse(stream, fileRange, false, null, Context.NONE);
    }

    public void downloadFileFromFileRange() {
        String filePath = "${myLocalFilePath}";
        fileClient.downloadToFile(filePath);
    }
    
    public void getShareServiceProperties() {
        shareServiceClient.getProperties();
    }

    public void setShareServiceProperties() {
        ShareServiceProperties properties = shareServiceClient.getProperties();

        properties.getMinuteMetrics().setEnabled(true).setIncludeApis(true); 
        properties.getHourMetrics().setEnabled(true).setIncludeApis(true);

        shareServiceClient.setProperties(properties);
    }

    public void setShareMetadata() {
        Map<String, String> metadata = Collections.singletonMap("directory", "metadata");
        shareClient.setMetadata(metadata);
    }

    public void getAccessPolicy() {
        shareClient.getAccessPolicy();
    }

    public void setAccessPolicy() {
        ShareAccessPolicy accessPolicy = new ShareAccessPolicy().setPermissions("r")
            .setStartsOn(OffsetDateTime.now(ZoneOffset.UTC))
            .setExpiresOn(OffsetDateTime.now(ZoneOffset.UTC).plusDays(10));
        ShareSignedIdentifier permission = new ShareSignedIdentifier().setId("mypolicy").setAccessPolicy(accessPolicy);
        shareClient.setAccessPolicy(Collections.singletonList(permission));
    }

    public void getHaHandleList() {
        PagedIterable<HandleItem> handleItems = directoryClient.listHandles(null, true, Duration.ofSeconds(30), Context.NONE);
    }

    public void forceCloseHandleWithResponse() {
        PagedIterable<HandleItem> handleItems = directoryClient.listHandles(null, true, Duration.ofSeconds(30), Context.NONE);
        String handleId = handleItems.iterator().next().getHandleId();
        directoryClient.forceCloseHandleWithResponse(handleId, Duration.ofSeconds(30), Context.NONE);
    }

    public void setQuotaOnShare() {
        int quotaOnGB = 1;
        shareClient.setQuota(quotaOnGB);
    }

    public void setFileHttpHeaders() {
        ShareFileHttpHeaders httpHeaders = new ShareFileHttpHeaders().setContentType("text/plain");
        fileClient.setProperties(1024, httpHeaders, null, null);
    }

    public void handleException() {
        try {
            shareServiceClient.createShare("myShare");
        } catch (ShareStorageException e) {
            logger.error("Failed to create a share with error code: " + e.getErrorCode());
        }
    }
}
