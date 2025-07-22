package nro.consts;

import nro.models.boss.Boss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.utils.Util;

public class SettingGame {
    
    private static SettingGame i ;
    
    public static SettingGame gI(){
        if(i == null){
            i = new SettingGame();
        }
        return i;
    }
    
    public short rac[] = {189,457,861};
    
    
    
}
