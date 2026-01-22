package com.mindskip.wdd.schedule;

import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.repository.UserCredentialMapper;
import com.mindskip.wdd.service.CredentialService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
@AllArgsConstructor
public class CredentialScheduleTask {

    private final UserCredentialMapper userCredentialMapper;
    private final CredentialService credentialService;
    private final static Integer pageSize = 500;
    private static final Logger logger = LoggerFactory.getLogger(CredentialScheduleTask.class);


    /**
     * 证书生成
     */
    @Scheduled(cron = "0 0 3 * * ?")
    private void buildTasks() {
        Integer pageIndex = 0;
        Integer waitBuildCredentialCount = userCredentialMapper.waitBuildCredentialCount();
        if (null != waitBuildCredentialCount) {
            int pageCount = waitBuildCredentialCount / pageSize + ((waitBuildCredentialCount % pageSize == 0) ? 0 : 1);
            for (int i = 0; i < pageCount; i++) {
                try {
                    pageIndex = i * pageSize;
                    List<UserCredential> userCredentialList = userCredentialMapper.waitBuildCredentialPage(pageIndex, pageSize);
                    userCredentialList.forEach(item -> {
                        try {
                            credentialService.buildCredential(item);
                        } catch (IOException e) {
                            logger.error(e.getMessage(), e);
                        }
                    });
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }


}
