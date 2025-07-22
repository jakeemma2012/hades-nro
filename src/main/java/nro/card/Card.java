/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.card;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

/**
 *
 *
 */
@Getter
@Setter
public class Card {

    @SerializedName("id")
    private int id;
    @SerializedName("amount")
    private int amount;
    @SerializedName("level")
    private int level;
    @SerializedName("use")
    private boolean isUse;
    private transient CardTemplate cardTemplate;

    public void addAmount(int amount) {
        this.amount += amount;
        if (this.amount >= getAmountLevel(level)) {
            levelUp();
        }
    }
    
    

    private void levelUp() {
        this.amount = 0;
        this.level++;
    }

    public void setTemplate() {
        cardTemplate = CardManager.getInstance().find(id);
    }

    int getAmountLevel(int level) {
        {
            switch (level) {
                case 0:
                    return 3;
                case 1:
                    return 6;
                case 2:
                    return 12;
                case 3:
                    return 24;
                case 4:
                    return 48;
                case 5:
                    return 96;
                case 6:
                    return 200;
                default:
                    return 999;
            }
        }

    }
}
