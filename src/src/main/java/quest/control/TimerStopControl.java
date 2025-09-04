
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TimerStopControl extends QuestControl {
    
    public static String NAME = "timer-stop";
    
    public TimerStopControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("TimerStopControl: onExecute: tag=" + tag);
        String timerName = getTagToken(tag, 1, true);

        String eventName = Quest.TIMER_EVENT_PREFIX + ":" + timerName;
        this.quest.appController.removeTimer(eventName);
        
        return "";
    }
    
}
