package com.revencoft.connection_pool;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.revencoft.connection_pool.connection.sftp.SftpConnection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/application/spring-application.xml" })
public class TestSftpConnection {
    String                 relativePath = "ORG_MQY";

    @Autowired
    private SftpConnection sftpConnection;

    @Test
    public void testGetFtpConnection() throws IOException {
        String fileName = "postRecord1.zip";

        boolean fileExists = sftpConnection.isFileExists(relativePath, fileName);
        System.out.println("fileExists: " + fileExists);
        fileExists = sftpConnection.isFileExists(relativePath, fileName);
        System.out.println("fileExists: " + fileExists);
        fileExists = sftpConnection.isFileExists(relativePath, fileName);
        System.out.println("fileExists: " + fileExists);

    }
}
