
package quest.control;

import quest.Book;

/**
 *
 * @author repp
 */
public abstract class QuestControl {
    
    public Book book;
    
    public QuestControl(Book book) {
        this.book = book;
    }
    
    public static String getTag(String text, int index) {
        String tagName = getTagName(text, index);
        
        if (tagName.length() > 0) {
            int lastChar = text.indexOf('>', index);
            if (lastChar > -1) {
                System.out.println("QuestControl: tagName=" + tagName + ", text=" + text + ", index=" + index + ", lastChar + 1=" + lastChar + 1);
                return text.substring(index, lastChar + 1).toLowerCase();
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
    
    public static String getTagName(String tag, int index) {
        System.out.println("QuestControl: getTagName: tag=" + tag);
        
        String[] parts = tag.substring(index + 1).toLowerCase().split(" ");
        String tagName = parts[0];
        int closingTagPosition = tagName.indexOf('>');
        if (closingTagPosition != -1) {
            tagName = tagName.substring(0, closingTagPosition);
        }
        
        System.out.println("QuestControl: getTagName: tagName=" + tagName);
        
        return tagName;
    }
    
    public abstract String onExecute(String tag);

}
