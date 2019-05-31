package client;

public class Inventory {
    private int[] invSlots = new int[9];
    private int[] invNum = new int[9];

    public void swapItems(int slot1, int slot2){
        int tempItem = invSlots[slot2];
        int tempNum = invNum[slot2];
        invSlots[slot2] = invSlots[slot1];
        invNum[slot2] = invNum[slot1];
        invSlots[slot1] = tempItem;
        invNum[slot1] = tempNum;
    }

    public boolean useItem(int slot){
        if (invNum[slot] == 0){
            return false;
        } else {
            //Use item
            //Remove item
            return true;
        }
    }

    public int getItem(int slot){
        return invSlots[slot];
    }

    public boolean addItem(int slot, int item){
        if (invSlots[slot] == 0 && invNum[slot] <= 10){
            invSlots[slot] = item;
            invNum[slot] += 1;
            return true;
        }
        return false;
    }

    public void removeItem(int slot){
        if (invNum[slot] > 1) {
            invNum[slot] -= 1;
        } else {
            invSlots[slot] = 0;
            invNum[slot] = 0;
        }
    }
}
