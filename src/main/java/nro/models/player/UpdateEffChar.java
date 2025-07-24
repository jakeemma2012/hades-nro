package nro.models.player;

import nro.manager.TopWhis;
import nro.services.Service;
import nro.utils.TimeUtil;

public class UpdateEffChar {

    private static UpdateEffChar i;

    public static UpdateEffChar getInstance() {
        if (i == null) {
            i = new UpdateEffChar();
        }
        return i;
    }

    public static int idDanhHieu1 = 1007;
    public static int idDanhHieu2 = 1016;
    public static int idDanhHieu3 = 1003;

    public void updateEff(Player player) {
        try {
            if (player.isPlnPet()) {
//                UpdateEffItemTime(player);
                updateDanhHieu(player);
                if (player.itemTime.isActive(999)) {
                    Service.getInstance().addEffectChar(player, 2220, 0, -1, 50, -1, 0, 0);
                } else {
                    RemoveEFfCharFollowId(player, 2220);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void updateDanhHieu(Player player) {
        if (player.isPlnPet()) {
            if (player.setClothes.set7Star == 5 || (player.setClothes.set7Star == 4 && player.setClothes.set8Star == 1)) {
                Service.getInstance().addEffectChar(player, 2216, 0, -1, 50, -1, 0, -43);
            } else {
                UpdateEffChar.getInstance().RemoveEFfCharFollowId(player, 2216);
            }

            if (player.setClothes.set8Star == 5) {
                Service.getInstance().addEffectChar(player, 2217, 0, -1, 50, -1, 0, -43);
            } else {
                UpdateEffChar.getInstance().RemoveEFfCharFollowId(player, 2217);
            }

            if (player.setClothes.setLevel7 == 5 || (player.setClothes.setLevel7 == 4 && player.setClothes.setLevel8 == 1)) {
                Service.getInstance().addEffectChar(player, 2215, 1, -1, 50, -1, 0, -63);
            } else {
                UpdateEffChar.getInstance().RemoveEFfCharFollowId(player, 2215);
            }
            if (player.setClothes.setLevel8 == 5) {
                Service.getInstance().addEffectChar(player, 2218, 1, -1, 50, -1, 0, -63);
            } else {
                UpdateEffChar.getInstance().RemoveEFfCharFollowId(player, 2218);
            }
        }
        
        if(player.isPl()) {
             switch (player.name) {
                case "tienkosai": // top 1
                case "duckdie":
                    Service.getInstance().addEffectChar(player, 83, 1, -1, 50, -1, 0, -50);
                    break;
                case "hungcon999": // top 2
                case "dolar":
                    case "zinvinia":
                    Service.getInstance().addEffectChar(player, 84, 1, -1, 50, -1, 0, -50);
                    break;
                case "piccolo": // top 3
                case "banvangsao":
                       case "ghohan":
                    Service.getInstance().addEffectChar(player, 85, 1, -1, 50, -1, 0, -50);
                    break;
                default:
                    break;
            }
        }
    }

    public void UpdateEffItemTime(Player player) {
        if (player.itemTime != null) {
            if (player.itemTime.isUseDanhHieu1) {
                Service.getInstance().addEffectChar(player, this.idDanhHieu1, 1, -1, 50, -1, 0, 30);
            }
            if (player.itemTime.isUseDanhHieu2) {
                Service.getInstance().addEffectChar(player, this.idDanhHieu2, 1, -1, 50, -1, 5, -40);
            }
            if (player.itemTime.isUseDanhHieu3) {
                Service.getInstance().addEffectChar(player, this.idDanhHieu3, 1, -1, 50, -1, 3, 0);
            }

        }
    }

    public void RemoveEFfCharFollowId(Player player, int id) {
        if (player != null && id > 0) {
            if (player.idEffChar.contains(id)) {
                player.idEffChar.remove(Integer.valueOf(id));
                Service.getInstance().RemoveEffPlayer(player, id);
            }
        }
    }

}
