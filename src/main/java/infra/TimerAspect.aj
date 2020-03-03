package infra;

import io.restassured.response.Response;
import java.util.concurrent.TimeUnit;
import static infra.AbstractTest.childTest;

public aspect TimerAspect {
    pointcut callGetResponse(): execution(* infra.AbstractTest.getResponse(String, boolean ));
    after() returning(Response r): callGetResponse() {
        childTest.info("API response time: "+ r.getTimeIn(TimeUnit.MILLISECONDS));
    }
}