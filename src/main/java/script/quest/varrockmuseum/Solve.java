package script.quest.varrockmuseum;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.script.Unobfuscated;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import script.framework.Leaf;

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
        MethodProvider.log("[SOLVING] -> " + question);

        WidgetChild widgetChild = getSolutionWidget();
        if (widgetChild != null) {
            MethodProvider.log("[ANSWER] -> " + (widgetChild.getText() != null ? widgetChild.getText() : ""));
            if (widgetChild.interact("Ok")) {
                MethodProvider.sleepUntil(Dialogues::inDialogue, 2500 + Timing.sleepLogNormalInteraction());
            }
            return Timing.sleepLogNormalSleep();
        }

        WidgetChild close = Museum.getQuizParent().getChild(32);
        if (close != null && close.interact("Close")) {
            MethodProvider.sleepUntil(() -> Museum.getQuizParent() == null, 1000 + Timing.sleepLogNormalInteraction());
        }

        return Timing.sleepLogNormalSleep();
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
