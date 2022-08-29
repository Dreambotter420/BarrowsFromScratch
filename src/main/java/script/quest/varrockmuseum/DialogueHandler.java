package script.quest.varrockmuseum;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.dialogues.Dialogues;

import script.utilities.Dialoguez;

public class DialogueHandler {

    public static void solve(String... options) {
        if ((Dialogues.canContinue() && Dialogues.continueDialogue()) || (Dialogues.getOptions() != null && Dialoguez.chooseOption(options))) {
            MethodProvider.sleepUntil(Dialogues::isProcessing, Timing.sleepLogNormalInteraction());
        }
    }

}
