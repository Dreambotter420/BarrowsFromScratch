package script.utilities.loadouts;

import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.utilities.impl.Condition;

public class EquipmentItem {
  private final String name;
  private final EquipmentSlot slot;
  private final Condition condition;

  /**
   * Creates an EquipmentItem with a condtional requirement before can be equipped
   * @param name
   * @param slot
   * @param condition
   */
  public EquipmentItem(
      String name,
      EquipmentSlot slot,
      Condition condition) {
    this.name = name;
    this.slot = slot;
    this.condition = condition;
  }

  /**
   * Creates a no-requirement EquipmentItem
   * @param name
   * @param slot
   */
  public EquipmentItem(String name, EquipmentSlot slot) {
    this.name = name;
    this.slot = slot;
    this.condition = () -> true;
  }

  public String getName() {
    return name;
  }

  public EquipmentSlot getSlot() {
    return slot;
  }

  public Condition getCondition() {
    return condition;
  }
}
