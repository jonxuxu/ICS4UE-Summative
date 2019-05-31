package client;

public class Inventory {
    private Item[] invSlots;
    private int[] invNum;

    public Inventory(){
        invSlots = new Item[9];
        invNum = new int[9];
    }

    public Inventory(Item[] inventory, int[] stacks){
        invSlots = inventory;
        invNum = stacks;
    }

    public void swapItems(int slot1, int slot2){
        Item tempItem = invSlots[slot2];
        int tempNum = invNum[slot2];
        invSlots[slot2] = invSlots[slot1];
        invNum[slot2] = invNum[slot1];
        invSlots[slot1] = tempItem;
        invNum[slot1] = tempNum;
    }

    public Item useItem(int slot){
        if (invNum[slot] == 0){
            return null;
        } else {
            Item temp = invSlots[slot];
            removeItem(slot);
            return temp;
        }
    }

    public Item getItem(int slot){
        return invSlots[slot];
    }

    public boolean addItem(Item item){
        for (int i = 0; i < 9; i++) {
            if (invSlots[i] == item && invNum[i] <= 10) {
                invNum[i] += 1;
                return true;
            } else if (invSlots[i] == null){
                invSlots[i] = item;
                invNum[i] = 1;
            }
        }
        return false;
    }

    public void removeItem(int slot){
        if (invNum[slot] > 1) {
            invNum[slot] -= 1;
        } else {
            invSlots[slot] = null;
            invNum[slot] = 0;
        }
    }
}
