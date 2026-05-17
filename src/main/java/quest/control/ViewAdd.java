
package quest.control;

import static app.controller.BaseController.logger;
import java.util.logging.Level;
import quest.control_deprecated.BaseQuestControl;
import quest.view.Quest;
import quest.view.Questcraft;
import quest.view.SceneMap;
import quest.view.SpellBook;

/**
 *
 * @author repp
 */
public class ViewAdd extends BaseQuestControl {
    
    public final Object viewName;
    
    public ViewAdd(Object viewName) {
        this.viewName = viewName;
    }

    @Override
    public void onExecute() {
        logger.log(Level.INFO, "Entered");
        
        String viewNameString = viewName.toString();
        
        // TODO - This needs to be refactored
        switch (viewNameString) {
            case Quest.MAP -> {
                    Quest.quest.map = new SceneMap(Quest.MAP);
                    Quest.quest.map.quest = Quest.quest;
                    Integer inventoryIndex = Quest.quest.appController.getTabIndex(Questcraft.INVENTORY);
                    Integer mapIndex = inventoryIndex + 1;
                    Quest.quest.appController.addView(Quest.quest.map, mapIndex, false);
            }
            case Quest.SPELL_BOOK -> {
                    Quest.quest.spellBook = new SpellBook(Quest.SPELL_BOOK);
                    Integer mapIndex = Quest.quest.appController.getTabIndex(Quest.MAP);
                    Integer spellBookIndex = mapIndex + 1;
                    Quest.quest.appController.addView(Quest.quest.spellBook, spellBookIndex, false);
            }
            default -> logger.log(Level.SEVERE, "Unsupported view '{0}'", viewNameString);
        }
    }
    
}
