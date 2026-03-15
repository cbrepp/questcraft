package quest.view;

import app.controller.BaseController;
import app.Color;
import app.HorizontalAlignment;
import app.Icon;
import app.Layout;
import app.RelativeCoordinates;
import app.VerticalAlignment;
import static app.controller.BaseController.logger;
import app.dialog.Alert;
import app.node.InputField;
import app.node.effect.Glow;
import java.util.List;
import java.util.logging.Level;
import quest.model.Story;

/**
 *
 * @author repp
 */
public class SpellBook extends app.view.BaseView {
    
    public static final String CAST_SPELL = "cast-spell";
    public static final String EMOJI = "\uD83D\uDCD9"; // "orange book" Unicode emoji
    public BaseController appController;
    
    public SpellBook(String name) {
        super(name);
        this.backgroundColor = new Color(0, 0, 0);
        this.backgroundImage = "/assets/images/spell-book.jpg";
        this.emojis.add(EMOJI);
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        logger.log(Level.INFO, "Entered: eventName={0}, eventValue={1}", new Object[]{eventName, eventValue});
        
        switch(eventName) {
            case CAST_SPELL -> {
                if (!eventValue.toString().isEmpty()) {
                    String spellName = ((String)eventValue).toUpperCase();
                    Story spell = Quest.quest.getSubpage(spellName, true);
                    if (spell == null) {
                        logger.log(Level.INFO, "Spell not found");
                        String[] responses = {"Well that was awkward.", "Nope.", "Pretty sure that's not a thing."};
                        int randomResponseIndex = (int) (Math.random() * responses.length);
                        String randomResponse = responses[randomResponseIndex];
                        Alert alert = new Alert(this.name, "You wait... and wait... and nothing happens.  The words in the book fade and disappear.");
                        alert.icon = Icon.ERROR;
                        alert.header = randomResponse;
                        this.appController.newDialog(alert);
                        return;
                    }
                    if ((spell.condition != null) && (!spell.condition.evaluate()) ){
                        logger.log(Level.INFO, "Spell's condition failed");
                        String[] responses = {"Almost?", "Just missing that certain something.", "Very nearly worked."};
                        int randomResponseIndex = (int) (Math.random() * responses.length);
                        String randomResponse = responses[randomResponseIndex];
                        Alert alert = new Alert(this.name, "You wait... and all you get is a snap, crackle, and pop.  Seems like it should be a thing but it isn't.  Perhaps you're trying the right spell at the wrong time?");
                        alert.icon = Icon.QUESTION;
                        alert.header = randomResponse;
                        this.appController.newDialog(alert);
                        return;
                    }
                    if (spell.mpCost > 0) {
                        if (Quest.quest.getPlayerMP() < spell.mpCost) {
                            logger.log(Level.INFO, "Not enough MP");
                            String[] responses = {"Almost?", "Just missing that certain something.", "Very nearly worked."};
                            int randomResponseIndex = (int) (Math.random() * responses.length);
                            String randomResponse = responses[randomResponseIndex];
                            Alert alert = new Alert(this.name, "It looks like the ink is trying its hardest to stay on the page but failing.  The words fade and disappear.  Perhaps you're lacking in magic points?");
                            alert.icon = Icon.CANCEL;
                            alert.header = randomResponse;
                            this.appController.newDialog(alert);
                            return;
                        } else {
                            Quest.quest.setPlayerMP(spell.mpCost * -1, false);
                        }
                    } else {
                        this.appController.playSound("/assets/sounds/spell-cast.wav", false);
                    }
                    String[] responses = {"Success!", "Huzzah!", "Abracadabra!"};
                    int randomResponseIndex = (int) (Math.random() * responses.length);
                    String randomResponse = responses[randomResponseIndex];
                    Alert alert = new Alert(this.name, "You hear the crackle and spark of magic.  The spell has been written!  The fresh words on the page emit a purple glow.");
                    alert.icon = Icon.WORKING;
                    alert.header = randomResponse;
                    this.appController.newDialog(alert);
                    logger.log(Level.INFO, "Executing spell '{0}'", spellName);
                    Quest.quest.displayStory(spell, true);
                }
            }
            default -> System.err.println("SpellBook: onEvent: Unsupported event");
        }
    }
    
    @Override
    public void onLoad(BaseController appController) {
        logger.log(Level.INFO, "Entered: appController={0}", appController);        
        InputField field = new InputField(CAST_SPELL);
        field.buttonBackgroundColor = Color.DARK_MAGENTA;
        field.buttonBorderWidth = 1;
        field.buttonText = "Cast Spell";
        field.eventListener = this;
        field.isMultiUse = true;
        field.isUpperCase = true;
        field.label = "Enter spell here";
        field.length = 50;
        field.fieldDisplayLength = 25;
        field.buttonEffects = List.of(new Glow(Color.DARK_MAGENTA));
        appController.addNode(this.name, this.name, field, new Layout(new RelativeCoordinates(0.05, 0.05), HorizontalAlignment.LEFT, VerticalAlignment.TOP));
        this.appController = appController;
    }

}
