package script.quest.varrockmuseum;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.utilities.Sleep;

import script.utilities.Dialoguez;
import script.utilities.Sleepz;

public class DialogueHandler {

    public static void solve(String... options) {
        if ((Dialogues.canContinue() && Dialogues.continueDialogue()) || (Dialogues.getOptions() != null && Dialoguez.chooseOption(options))) {
            Sleep.sleepUntil(Dialogues::isProcessing, Sleepz.interactionTiming());
        }
    }

}
