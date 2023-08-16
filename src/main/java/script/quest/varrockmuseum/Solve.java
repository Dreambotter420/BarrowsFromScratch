package script.quest.varrockmuseum;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.script.Unobfuscated;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.framework.Leaf;
import script.utilities.Sleepz;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Unobfuscated
public class Solve extends Leaf {
    @Override
    public boolean isValid() {
        return Museum.getQuizParent() != null && Museum.getQuizParent().isVisible();
    }

    @Override
    public int onLoop() {

        String question = getQuestionText();
        Logger.log("[SOLVING] -> " + question);

        WidgetChild widgetChild = getSolutionWidget();
        if (widgetChild != null) {
            Logger.log("[ANSWER] -> " + (widgetChild.getText() != null ? widgetChild.getText() : ""));
            if (widgetChild.interact("Ok")) {
                Sleep.sleepUntil(Dialogues::inDialogue, 2500 + Sleepz.interactionTiming());
            }
            return Sleepz.sleepTiming();
        }

        WidgetChild close = Museum.getQuizParent().getChild(32);
        if (close != null && close.interact("Close")) {
            Sleep.sleepUntil(() -> Museum.getQuizParent() == null, 1000 + Sleepz.interactionTiming());
        }

        return Sleepz.sleepTiming();
    }



    private WidgetChild getSolutionWidget() {
        Answer answer = Arrays.stream(Answer.values()).filter(answers -> answers.question.equals(getQuestionText())).findFirst().orElse(null);
        return answer != null ? getAnswerWidgets().stream().filter(w -> w.getText() != null && w.getText().equals(answer.answer)).findFirst().orElse(null) : null;
    }

    private List<WidgetChild> getAnswerWidgets() {
        int[] childIds = new int[]{29, 30, 31};
        return Arrays.stream(childIds).mapToObj(i -> Museum.getQuizParent().getChild(i)).collect(Collectors.toList());
    }

    private String getQuestionText() {
        WidgetChild widgetChild = Museum.getQuizParent().getChild(28);
        return widgetChild != null ? widgetChild.getText() : "";
    }
}
