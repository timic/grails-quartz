package grails.plugins.quartz

import org.quartz.CalendarIntervalTrigger
import org.quartz.CronScheduleBuilder
import org.quartz.CronTrigger
import org.quartz.DailyTimeIntervalTrigger
import org.quartz.SimpleScheduleBuilder
import org.quartz.SimpleTrigger
import org.quartz.Trigger
import org.quartz.TriggerBuilder

/**
 * Helps to build triggers for schedule methods.
 *
 * @author Vitalii Samolovskikh aka Kefir
 */
class TriggerUtils {

    private static String generateTriggerName() {
        "GRAILS_" + UUID.randomUUID()
    }

    static Trigger buildDateTrigger(String jobName, String jobGroup, Date scheduleDate) {
        return TriggerBuilder.newTrigger()
            .withIdentity(generateTriggerName(), GrailsJobClassConstants.DEFAULT_TRIGGERS_GROUP)
            .withPriority(6)
            .forJob(jobName, jobGroup)
            .startAt(scheduleDate)
            .build()
    }

    static SimpleTrigger buildSimpleTrigger(String jobName, String jobGroup, long repeatInterval, int repeatCount) {
        return TriggerBuilder.newTrigger()
            .withIdentity(generateTriggerName(), GrailsJobClassConstants.DEFAULT_TRIGGERS_GROUP)
            .withPriority(6)
            .forJob(jobName, jobGroup)
            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(repeatInterval).withRepeatCount(repeatCount))
            .build()
    }

    static CronTrigger buildCronTrigger(String jobName, String jobGroup, String cronExpression) {
        return TriggerBuilder.newTrigger()
            .withIdentity(generateTriggerName(), GrailsJobClassConstants.DEFAULT_TRIGGERS_GROUP)
            .withPriority(6)
            .forJob(jobName, jobGroup)
            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
            .build()
    }

    static boolean triggersEqual(Trigger trigger1, Trigger trigger2) {
        false
    }

    static boolean triggersEqual(SimpleTrigger trigger1, SimpleTrigger trigger2) {
        trigger1 == trigger2 && fieldsEqual(trigger1, trigger2, ["repeatCount", "repeatInterval"])
    }

    static boolean triggersEqual(CronTrigger trigger1, CronTrigger trigger2) {
        trigger1 == trigger2 && fieldsEqual(trigger1, trigger2, ["cronExpression", "timeZone"])
    }

    static boolean triggersEqual(DailyTimeIntervalTrigger trigger1, DailyTimeIntervalTrigger trigger2) {
        trigger1 == trigger2 && fieldsEqual(trigger1, trigger2,
                ["repeatCount", "daysOfWeek", "endTimeOfDay", "startTimeOfDay", "repeatInterval", "repeatIntervalUnit"])
    }

    static boolean triggersEqual(CalendarIntervalTrigger trigger1, CalendarIntervalTrigger trigger2) {
        trigger1 == trigger2 && fieldsEqual(trigger1, trigger2,
                ["timeZone", "preserveHourOfDayAcrossDaylightSavings",
                 "repeatInterval", "repeatIntervalUnit", "skipDayIfHourDoesNotExist"])
    }

    private static fieldsEqual(obj1, obj2, Collection<String> fields) {
        [obj1, obj2].collect { obj -> fields.collect { f -> obj."$f" } }.transpose().every { Collection l -> l[0] == l[1] }
    }

}
