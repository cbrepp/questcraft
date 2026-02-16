
package quest.control;

import app.node.BaseNode;
import java.io.Serializable;
import quest.view.Quest;

/**
 *
 * @author repp
 */
public abstract class QuestControl implements Serializable {
    
    public Quest quest;
    public Boolean unspoolStoryText;
    
    public QuestControl(Quest quest) {
        this.quest = quest;
        this.unspoolStoryText = false;
    }   

    public BaseNode onDisplay() {return null;} // TODO - Make abstract
    public abstract String onExecute(String tag);
    
    public Boolean evalauteCondition(String condition) {
        System.out.println("QuestControl: evalauteCondition: condition=" + condition);
        
        String operator;
        if (condition.contains("!=")) {
            operator = "!=";
        } else if (condition.contains("=")) {  
            operator = "=";
        } else if (condition.contains("&gt;")) {  
            operator = "&gt;";
        } else if (condition.contains("&ge;")) {  
            operator = "&ge";
        } else if (condition.contains("&lt;")) {  
            operator = "&lt;";
        } else if (condition.contains("&le;")) {  
            operator = "&le;";
        } else {
            System.err.println("QuestControl: evalauteCondition: Condition does NOT contain a valid operator!");
            return false;
        }
        
        System.out.println("QuestControl: evalauteCondition: operator=" + operator);
        
        String[] conditionParts = condition.split(operator);
        System.out.println("QuestControl: evalauteCondition: conditions parts=" + conditionParts.length);
        if (conditionParts.length > 2) {
            System.err.println("QuestControl: evalauteCondition: Operator exists more than once in the condition!");
            return false;
        } else if (conditionParts.length == 0) {
            System.err.println("QuestControl: evalauteCondition: Operator does not exist in the condition!");
            return false;
        }
        
        String leftExpression = conditionParts[0];
        System.out.println("QuestControl: evalauteCondition: leftExpression raw=" + leftExpression);
        String leftValue = evaluateExpression(leftExpression);
        System.out.println("QuestControl: evalauteCondition: leftExpression translated=" + leftValue);
        
        String rightExpression;
        if (conditionParts.length == 1) {
            rightExpression = "";
        } else {
            rightExpression = conditionParts[1];
        }
        System.out.println("QuestControl: evalauteCondition: rightExpression raw=" + rightExpression);
        String rightValue = evaluateExpression(rightExpression);
        System.out.println("QuestControl: evalauteCondition: rightExpression translated=" + rightValue);
        
        System.out.println("QuestControl: evalauteCondition: translated condition=" + leftValue + operator + rightValue);
        
        Boolean isTrue;
        isTrue = switch (operator) {
            case "!=" -> !leftValue.equals(rightValue);
            case "=" -> leftValue.equals(rightValue);
            case "&gt;" -> Integer.parseInt(leftValue) > Integer.parseInt(rightValue);
            case "&ge;" -> Integer.parseInt(leftValue) >= Integer.parseInt(rightValue);
            case "&lt;" -> Integer.parseInt(leftValue) < Integer.parseInt(rightValue);
            case "&le;" -> Integer.parseInt(leftValue) <= Integer.parseInt(rightValue);
            default -> false;
        };
        
        return isTrue;
    }
    
    public String evaluateExpression(String expression) {
        System.out.println("QuestControl: evaluateExpression: expression=" + expression);
        
        String value;
        
        // Evaluate quest control
        String[] expressionTokens = expression.split(" ");
        if (expressionTokens.length >= 1) {
            String firstToken = expressionTokens[0];
            QuestControl control = this.quest.questControls.get(firstToken);
            if (control != null) {
                String expressionTag = '<' + expression + '>';
                System.out.println("QuestControl: evaluateExpression: Resolving tag " + expressionTag);
                value = control.onExecute(expressionTag);
                return value;
            }
        }
        
        // Evaluate variable
        if (this.quest.variables.containsKey(expression)) {
            System.out.println("QuestControl: evaluateExpression: Resolving variable " + expression);
            value = this.quest.variables.get(expression);
            return value;
        }
        
        // Literal string value
        System.out.println("QuestControl: evaluateExpression: Resolving literal string " + expression);
        value = expression;
        
        return value;
    }
    
    public String execute(String tag) {
        System.out.println("QuestControl: execute: tag=" + tag);
        
        String condition = getCondition(tag);
        if (!condition.equals("")) {
            if (!this.evalauteCondition(condition)) {
                System.out.println("QuestControl: execute: Failed condition, nothing to do");
                return "";
            }
            
            // Strip out the condition from the tag
            String fullCondition = "condition=" + "\"" + condition + "\" ";
            tag = tag.replaceAll(fullCondition, "");
        }
        
        String returnValue = this.onExecute(tag);
        return returnValue;
    }
    
    public static String getCondition(String tag) {
        System.out.println("QuestControl: getCondition: tag=" + tag);
        
        String args = getTagToken(tag, 1, true);
        
        // Confirm that the condition begins with condition="
        String condition = "";
        Boolean lengthCondition = (args.length() >= "condition=\"\"".length());
        Boolean matchCondition = null;
        if (lengthCondition) {
            matchCondition = (args.substring(0, "condition=\"".length()).equals("condition=\""));
            System.out.println("QuestControl: getCondition: needed=condition=\"");
            System.out.println("QuestControl: getCondition: has=" + args.substring(0, "condition=\"".length()));
        }
        if ((args.length() >= "condition=\"\"".length()) && (args.substring(0, "condition=\"".length()).equals("condition=\""))) {
            // Extract the condition encapsulated within double quotes
            String[] tokens = args.split("\"");
            condition = tokens[1];
        }
        
        System.out.println("QuestControl: getCondition: lengthCondition=" + lengthCondition + ", matchCondition=" + matchCondition);
        System.out.println("QuestControl: getCondition: condition=" + condition);
        
        return condition;
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
    
    public static String getTagToken(String tag, int tokenIndex, Boolean ignoreDelimiter) {
        System.out.println("QuestControl: getTagToken: tag=" + tag + ", tokenIndex=" + tokenIndex + ", ignoreDelimiter=" + ignoreDelimiter);
        
        String[] tagTokens = tag.split(" ");
        if (tagTokens.length == 1) {
            System.out.println("QuestControl: getTagToken: No tokens");
            return "";
        }
        
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
    
}
