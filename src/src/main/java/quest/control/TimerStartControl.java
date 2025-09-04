
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TimerStartControl extends QuestControl {
    
    public static String NAME = "timer-start";
    
    public TimerStartControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("TimerStartControl: onExecute: tag=" + tag);
        int seconds = Integer.parseInt(getTagToken(tag, 1, false));
        String timerName = getTagToken(tag, 2, true);

        String eventName = Quest.TIMER_EVENT_PREFIX + ":" + timerName;
        this.quest.appController.setTimer(eventName, seconds, this.quest);
        
        return "";
    }
    
}
