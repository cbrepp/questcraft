
package quest.control;

import quest.view.Quest;

/**
 *
 * @author repp
 */
public abstract class QuestControl {
    
    public Quest quest;
    
    public QuestControl(Quest quest) {
        this.quest = quest;
    }
    
    public static String getTag(String text, int index) {
        String tagName = getTagName(text, index);
        
        if (tagName.length() > 0) {
            int lastChar = text.indexOf('>', index);
            if (lastChar > -1) {
                System.out.println("QuestControl: tagName=" + tagName + ", text=" + text + ", index=" + index + ", lastChar + 1=" + lastChar + 1);
                return text.substring(index, lastChar + 1);
            }
        }
        return null;
    }
    
    public static String getTagArgument(String tag, int arg) {
        System.out.println("QuestControl: getTagArgument: tag=" + tag + ", arg=" + arg);
        
        String[] tagParts = tag.split(" ");
        String[] argParts = tagParts[1].substring(0, tagParts[1].length() - 1).split("\\+");    // The plus character needs to be escaped
        if (argParts.length < arg) {
            System.out.println("QuestControl: getTagArgument: No value");
            return null;
        }
        String value = argParts[arg - 1];
        
        System.out.println("QuestControl: getTagArgument: value=" + value);
        
        return value;
    }
    
    public static String getTagToken(String tag, int tokenIndex, Boolean ignoreDelimiter) {
        System.out.println("QuestControl: getTagToken: tag=" + tag + ", tokenIndex=" + tokenIndex + ", ignoreDelimiter=" + ignoreDelimiter);
        
        String[] tagTokens = tag.split(" ");
        String tokenValue = "";
        if (!ignoreDelimiter) {
            tokenValue = tagTokens[tokenIndex];
        } else {
            String[] combinedTokens = new String[tagTokens.length - tokenIndex];
            System.arraycopy(tagTokens, tokenIndex, combinedTokens, 0, combinedTokens.length);
            for (int i = 0; i < combinedTokens.length; i++) {
                String currentTokenValue = combinedTokens[i];
                if (i == 0) {
                    tokenValue = currentTokenValue;
                } else {
                    tokenValue = tokenValue + " " + currentTokenValue;
                }
            }
        }
        
        if (tokenIndex == 0) {
            // Trim the "<" off the start of the first token
            tokenValue = tokenValue.substring(1, tokenValue.length());
        } else if ((tokenIndex == (tagTokens.length - 1)) || ignoreDelimiter) {
            // Trim the ">" off the end of the last token
            tokenValue = tokenValue.substring(0, tokenValue.length() - 1);
        }
        
        return tokenValue;
    }
    
    public static String getTagName(String tag, int index) {
        System.out.println("QuestControl: getTagName: tag=" + tag);
        
        String[] parts = tag.substring(index + 1).toLowerCase().split(" ");
        String tagName = parts[0];
        int closingTagPosition = tagName.indexOf('>');
        if (closingTagPosition != -1) {
            tagName = tagName.substring(0, closingTagPosition);
        }
        
        tagName = tagName.toLowerCase();
        System.out.println("QuestControl: getTagName: tagName=" + tagName);
        
        return tagName;
    }
    
    public abstract String onExecute(String tag);

}
