package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class InternalCallV2Test {

    @Autowired
    CallService callService;


    @Test
    void printProxy(){
        log.info("call Service class={}",callService.getClass());
    }

    @Test
    void externalCallV2(){
        callService.external();
    }


    @TestConfiguration
    static class InternalCallV1TestConfig{
        @Bean
        CallService callService(){
            return new CallService(internalService());
        }

        @Bean
        InternalService internalService(){
            return new InternalService();
        }
    }



    @Slf4j
    @RequiredArgsConstructor
    static class CallService {



        private final InternalService internalService;

        public void external() {
            //외부 호출
            log.info("call external");
            internalService.printTxInfo();
            internalService.internal();
        }




    }


    static class InternalService{
        @Transactional
        public void internal() {
            log.info("call internal");
            printTxInfo();
        }

        private void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isSynchronizationActive();
            log.info("tx active={}", txActive);
            boolean currentTransactionReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", currentTransactionReadOnly);


        }

    }
}
