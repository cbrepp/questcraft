
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public class TurnRightControl extends QuestControl {
    
    public static String NAME = "turn-right";
    
    public TurnRightControl(Quest quest) {
        super(quest);
    }
    
    @Override
    public String onExecute(String tag) {
        System.out.println("TurnRightControl: onExecute: tag=" + tag);
        
        switch (this.quest.getPlayerDirection().toUpperCase()) {
            case Quest.DIRECTION_EAST -> this.quest.setPlayerDirection(Quest.DIRECTION_SOUTH);
            case Quest.DIRECTION_NORTH -> this.quest.setPlayerDirection(Quest.DIRECTION_EAST);
            case Quest.DIRECTION_SOUTH -> this.quest.setPlayerDirection(Quest.DIRECTION_WEST);
            case Quest.DIRECTION_WEST -> this.quest.setPlayerDirection(Quest.DIRECTION_NORTH);
        }
        
        this.quest.display();
        
        return "";
    }
    
}
