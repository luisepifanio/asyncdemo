package tasks

import groovy.util.logging.Slf4j

import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

/**
 * @author luis
 * Date 17/08/15 19:19
 * Project: asyncdemo
 */
@Slf4j
class TimeConsumingTask implements Callable<Object>{
    Long duration
    Long expected
    TimeUnit unit
    String id

    @Override
    Object call() throws Exception {
        Map object = [started:new Date()]
        Boolean canNotRun = [duration , unit].any { !it }
        if(canNotRun){
            throw new IllegalStateException("duration: $duration and unit: $unit should be set up! ")
        }

        try {
            Thread.sleep(unit.toMillis(duration));
        } catch (InterruptedException e) {
            log.error('Not supposed to happens an InterruptedException!')
        }
        object.finished = new Date()
        object.duration = getDateDiff(object.started as Date,object.finished as Date,TimeUnit.SECONDS)
        object.id = getId()
        object.unit = getUnit().name()
        object.expected = expected;
        return object;
    }

    /**
     * Get a diff between two dates
     * @param date1 the oldest date
     * @param date2 the newest date
     * @param timeUnit the unit in which you want the diff
     * @return the diff value, in the provided unit
     */
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long millies = date2.getTime() - date1.getTime();
        return timeUnit.convert(millies,TimeUnit.MILLISECONDS);
    }
}
