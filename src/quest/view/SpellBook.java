package quest.view;

import app.ApplicationController;
import app.Color;
import quest.model.Story;

/**
 *
 * @author repp
 */
public class SpellBook extends app.ApplicationView {
    
    public static final String CAST_SPELL = "cast-spell";
    public static final String EMOJI = "\uD83D\uDCD9"; // "orange book" Unicode emoji
    public ApplicationController appController;
    public Quest quest;
    
    public SpellBook(String name) {
        super(name);
        this.backgroundColor = new Color(0, 0, 0);
        this.backgroundImage = "/assets/images/spell-book.jpg";
        this.emoji = EMOJI;
    }
    
    @Override
    public void onEvent(String eventName, Object eventValue) {
        System.out.println("SpellBook: onEvent: eventName=" + eventName + ", eventValue=" + eventValue);
        
        switch(eventName) {
            case CAST_SPELL -> {
                if (!eventValue.equals("")) {
                    String spellName = ((String)eventValue).toUpperCase();
                    Story spell = this.quest.getSubpage(spellName, true);
                    if (spell == null) {
                        String[] responses = {"Well that was awkward.", "Nope.", "Pretty sure that's not a thing."};
                        int randomResponseIndex = (int) (Math.random() * responses.length);
                        String randomResponse = responses[randomResponseIndex];
                        this.appController.displayMessageBox(randomResponse, "You wait... and wait... and nothing happens.  The words in the book fade and disappear.  Perhaps you're trying the right spell at the wrong time?", app.Icon.ERROR);
                        return;
                    }
                    app.Utility.playSound("/assets/sounds/spell-cast.wav", false);
                    String[] responses = {"Success!", "Huzzah!", "Abracadabra!"};
                    int randomResponseIndex = (int) (Math.random() * responses.length);
                    String randomResponse = responses[randomResponseIndex];
                    this.appController.displayMessageBox(randomResponse, "You hear the crackle and spark of magic.  The spell has been written!  Return to your Quest to see if the spell has been cast.", app.Icon.INFORMATION);
                    System.out.println("SpellBook: onEvent: Executing spell: " + spellName);
                    this.quest.displayPage(spell.contents, true);
                }
            }
            default -> System.err.println("SpellBook: onEvent: Unsupported event");
        }
    }
    
    @Override
    public void onLoad(ApplicationController appController) {
        System.out.println("SpellBook: onLoad");
        
        this.appController = appController;
        this.appController.displayInputField(this.name, CAST_SPELL, "Enter spell here", 20, 3, 5, false, true, true, this);
    }

}
