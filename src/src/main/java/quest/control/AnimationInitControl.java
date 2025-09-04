
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class AnimationInitControl extends QuestControl {
    
    public static String NAME = "animation-init";
    
    public AnimationInitControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("AnimationInitControl: onExecute: tag=" + tag);
        
        this.quest.variables.put("animation-on", "false");
        this.quest.variables.put("animation-started", "false");
        this.quest.variables.put("animation-left", "false");
        this.quest.variables.put("animation-right", "false");
        
        return "";
    }
    
}
