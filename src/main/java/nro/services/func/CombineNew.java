package nro.services.func;

import nro.models.item.Item;
import nro.models.npc.Npc;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class CombineNew {

    public long lastTimeCombine;

    public List<Item> itemsCombine;
    public int typeCombine;
    public Npc npcCombine;

    public int goldCombine;
    public int gemCombine;
    public int rubyCombine;

    public float ratioCombine;
    public int countDaNangCap;

    public short countDaBaoVe;

    public CombineNew() {
        this.itemsCombine = new ArrayList<>();
    }

    public void setNpc(Npc npc) {
        this.npcCombine = npc;
    }

    public void setTypeCombine(int type) {
        this.typeCombine = type;
    }

    public void clearItemCombine() {
        this.itemsCombine.clear();
    }

    public void clearParamCombine() {
        this.goldCombine = 0;
        this.gemCombine = 0;
        this.ratioCombine = 0;
        this.countDaNangCap = 0;
        this.countDaBaoVe = 0;
        this.rubyCombine = 0;
    }

    public void dispose() {
        this.itemsCombine = null;
    }
}
