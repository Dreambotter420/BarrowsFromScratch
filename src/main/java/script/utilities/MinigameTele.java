package script.utilities;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import static org.dreambot.api.Client.getLocalPlayer;
//*********************************************************************************
// CLASS: MinigameTeleporter
// The purpose of this class is to allow the local player to access and
// utilise the minigame teleports.
// AUTHOR: Hmm
//*********************************************************************************
public class MinigameTele {

    private static final int GROUPINGBITVALUE = 3; //Bit value of Grouping tab is 3.
    private static final int PLAYERSETTINGSINDEX = 13071; //Index 13071 checks which tab is currently selected in the clan tab.

    private static final int GROUPINGTABPARENT = 707; //Parent of the grouping tab icon.
    private static final int GROUPINGTABCHILD = 6; //Child of the grouping tab icon.
    private static final int GROUPINGTABGRANDCHILD = 3; //Grandchild of the grouping tab icon.

    private static final int ACTIVITYSELECTPARENT = 76; //Parent of the activity selection dropdown menu.
    private static final int ACTIVITYSELECTCHILD = 8; //Child of the activity selection dropdown menu.

    private static final int ACTIVITYPANELPARENT = 76; //Parent of the activity selection panel.
    private static final int ACTIVITYPANELCHILD = 18; //Child of the activity selection panel.

    private static final int TELEPORTBUTTONPARENT = 76; //Parent of the teleport button.
    private static final int TELEPORTBUTTONCHILD = 26; //Child of the teleport button.

    private static final int SCOLLBARPARENT = 76; //Parent of the scroll bar.
    private static final int SCOLLBARCHILD = 19; //Child of the scroll bar.
    private static final int SCOLLBARGRANDCHILD = 0; //Grandchild of the scroll bar.

    private static final int SCOLLBARTHUMBPARENT = 76; //Parent of the scroll bar thumb.
    private static final int SCOLLBARTHUMBCHILD = 19; //Child of the scroll bar thumb.
    private static final int SCOLLBARTHUMBGRANDCHILD = 1; //Grandchild of the scroll bar thumb.

    //*********************************************************************************
    // BOOLEAN METHOD: isMinigameTabOpen()
    // Checks if the minigame tab is open.
    //*********************************************************************************
    private static boolean isMinigameTabOpen() {
        return Tabs.isOpen(Tab.CLAN) && PlayerSettings.getBitValue(PLAYERSETTINGSINDEX) == GROUPINGBITVALUE;
    }

    //*********************************************************************************
    // VOID METHOD: selectAnActivity()
    // Opens the activity panel.
    //*********************************************************************************
    private static void selectAnActivity(WidgetChild activitySelect, WidgetChild activityPanel) {
        if (activitySelect != null && !activityPanel.isVisible()) {
            activitySelect.interact();
        }
    }

    //*********************************************************************************
    // VOID METHOD: resetScrollBar()
    // Resets the scroll bar.
    //*********************************************************************************
    private static void resetScrollBar() {
        WidgetChild activityPanel = Widgets.getWidgetChild(ACTIVITYPANELPARENT, ACTIVITYPANELCHILD);
        WidgetChild scrollBar = Widgets.getWidgetChild(SCOLLBARPARENT, SCOLLBARCHILD, SCOLLBARGRANDCHILD);
        WidgetChild scrollBarThumb = Widgets.getWidgetChild(SCOLLBARTHUMBPARENT, SCOLLBARTHUMBCHILD, SCOLLBARTHUMBGRANDCHILD);
        if (Mouse.move(activityPanel.getRectangle()) && scrollBarThumb.getRectangle().getY() != scrollBar.getRectangle().getY()) {
            Mouse.scrollUpUntil(1000, () -> scrollBarThumb.getRectangle().getY() == scrollBar.getRectangle().getY());
            MethodProvider.sleepUntil(() -> scrollBarThumb.getRectangle().getY() == scrollBar.getRectangle().getY(), 5000);
        }
    }

    //*********************************************************************************
    // BOOLEAN METHOD: isScrollBarThumbAtTop()
    // Checks if the thumb of the scroll bar is at the top.
    //*********************************************************************************
    private static boolean isScrollBarThumbAtTop() {
        WidgetChild scrollBar = Widgets.getWidgetChild(SCOLLBARPARENT, SCOLLBARCHILD, SCOLLBARGRANDCHILD);
        WidgetChild scrollBarThumb = Widgets.getWidgetChild(SCOLLBARTHUMBPARENT, SCOLLBARTHUMBCHILD, SCOLLBARTHUMBGRANDCHILD);
        return scrollBar.getRectangle().getY() == scrollBarThumb.getRectangle().getY();
    }

    //*********************************************************************************
    // VOID METHOD: openMinigameTab()
    // Opens minigame tab.
    //*********************************************************************************
    public static void openMinigameTab() {
        if (Tabs.open(Tab.CLAN)) {
            if (!isMinigameTabOpen()) {
                WidgetChild groupingTab = Widgets.getWidgetChild(GROUPINGTABPARENT, GROUPINGTABCHILD, GROUPINGTABGRANDCHILD);
                groupingTab.interact();
                MethodProvider.sleepUntil(() -> isMinigameTabOpen(), Calculations.random(5000, 6000));
            }
        }
    }

    //*********************************************************************************
    // VOID METHOD: teleportMinigame()
    // Teleports player to desired minigame location.
    //*********************************************************************************
    public static void teleportMinigame(String minigameName) {
        WidgetChild activitySelect = Widgets.getWidgetChild(ACTIVITYSELECTPARENT, ACTIVITYSELECTCHILD);
        WidgetChild activityPanel = Widgets.getWidgetChild(ACTIVITYPANELPARENT, ACTIVITYPANELCHILD);
        if (!isMinigameTabOpen()) {
            openMinigameTab();
            MethodProvider.sleepUntil(() -> activitySelect != null && activitySelect.isVisible(), 10000);
        } else {
            if (!activitySelect.getText().equals(minigameName)) {
                selectAnActivity(activitySelect, activityPanel);
                WidgetChild nameOfMinigame = Widgets.getMatchingWidget(w -> w != null && w.getText().equals(minigameName));
                if (isScrollBarThumbAtTop()) {
                    Mouse.move(activityPanel.getRectangle());
                    if (!activityPanel.getRectangle().contains(nameOfMinigame.getRectangle())) {
                        Mouse.scrollDownUntil(1000, () -> activityPanel.getRectangle().contains(nameOfMinigame.getRectangle()));
                    }
                    nameOfMinigame.interact();
                    MethodProvider.sleepUntil(() -> activitySelect.getText().equals(minigameName), 1500);
                } else {
                    resetScrollBar();
                }
            } else {
                WidgetChild teleportButton = Widgets.getWidgetChild(TELEPORTBUTTONPARENT, TELEPORTBUTTONCHILD);
                if (teleportButton != null) {
                    if (teleportButton.interact()) {
                        MethodProvider.sleepUntil(() -> getLocalPlayer().isAnimating(), 15000);
                        MethodProvider.sleepWhile(() -> getLocalPlayer().isAnimating(), 15000);
                    }
                }
            }
        }
    }
}