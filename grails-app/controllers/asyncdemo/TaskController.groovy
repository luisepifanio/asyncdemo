package asyncdemo

import com.google.common.util.concurrent.ListenableFuture
import grails.converters.JSON
import org.springframework.http.HttpStatus
import tasks.MockExecutor
import tasks.TimeConsumingTask

import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

class TaskController {

    static responseFormats = ['json']

    def create() {
        String requestId = request.getParameter('rqId') ?: getRandomStringOfLength(20)
        String durationStr = request.getParameter('timeout') // in seconds
        Integer timeoutIn = durationStr.isInteger() ? durationStr as Integer : 15

        Integer duration = getRandomNumberInRange(timeoutIn - 5 > 0 ? timeoutIn - 5 : 5 , timeoutIn + 8 )

        Callable<Object> task = new TimeConsumingTask(
                id: requestId,
                duration: duration,
                expected: timeoutIn,
                unit: TimeUnit.SECONDS
        )

        ListenableFuture<Object> future = MockExecutor.getInstance().submit(task,task.id);
        sleep(TimeUnit.SECONDS.toMillis(3))
        if(future.isDone()){
            render( future.get() as JSON )
            return;
        }
        header 'Location', "$controllerName/lookup?rqId=${task.id}"
        render(
                contentType: "application/json" ,
                status: HttpStatus.ACCEPTED.value(),
                text: ''
        )

    }

    def lookup(){
        String requestId = request.getParameter('rqId') ?: null
        render( MockExecutor.getInstance().lookUpFor(requestId) as JSON )
    }

    private long getRandomNumberInRange(long _min, long _max) {
        final long min = Math.min(_min, _max)
        final long max = Math.max(_min, _max)
        return new Random().nextInt( (max - min + 1) as Integer) + min
    }

    private String getRandomStringOfLength(long size) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString()
    }
}
