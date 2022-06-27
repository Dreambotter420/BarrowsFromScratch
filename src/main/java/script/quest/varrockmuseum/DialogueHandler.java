package script.quest.varrockmuseum;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;

public class DialogueHandler {

    public static void solve(String... options) {
        if ((Dialogues.canContinue() && Dialogues.continueDialogue()) || (Dialogues.getOptions() != null && Dialogues.chooseFirstOption(options))) {
            MethodProvider.sleepUntil(Dialogues::isProcessing, Timing.sleepLogNormalInteraction());
        }
    }

}
