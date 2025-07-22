
package nro.models.npc.NpcForge;

import nro.consts.ConstNpc;
import nro.jdbc.DBService;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.map.mabu.MabuWar;
import nro.models.map.mabu.MabuWar14h;
import nro.models.npc.Npc;
import nro.models.phuban.MabuBoss.PhubanMabu;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.TaskService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author Administrator
 */
public class Osin extends Npc {

    public Osin(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ồ ! Cậu cũng là 1 nhà sưu tầm sao ?\n"
                        + "Thẻ sưu tầm ngoài giá trị sưu tập ra còn ẩn chứa những sức mạnh ngẫu nhiên!\n"
                        + "Tôi cũng là 1 nhà sưu tầm nhưng tuy nhiên, cần phải trả 1 trang bị Thần linh và 1 thỏi vàng nhé !",
                        "Đổi ngay !", "Đóng");
            } else if (this.mapId == 120 || this.mapId == 180) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào ? Ngươi đang làm gì ở đây thế !", "Về nhà");
            } else if (this.mapId == 50) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                        "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
            } else if (this.mapId == 154) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                        "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
            } else if (this.mapId == 155) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                        "Quay về", "Từ chối");
            } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                if (MabuWar.gI().isTimeMabuWar()) {
                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                    + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                            "Giải trừ\nphép thuật\n50Tr Vàng",
                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
                } else if (MabuWar14h.gI().isTimeMabuWar()) {
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Ta sẽ phù hộ cho ngươi bằng nguồn sức mạnh của Thần Kaiô\n+1 triệu HP, +1 triệu MP, +10k Sức đánh\nLưu ý: sức mạnh sẽ biến mất khi ngươi rời khỏi đây",
                            "Phù hộ\n55 hồng ngọc", "Từ chối", "Về\nĐại Hội\nVõ Thuật");
                }
            } else {
                super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (this.mapId == 5) {
                if (select == 0) {
                    DoiTheSuuTam(player);
                }
            } else if (this.mapId == 120 || this.mapId == 180) {
                ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, -1);
            } else if (this.mapId == 50) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                            break;
                        case 1:
                            ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                            break;
                    }
                }
            } else if (this.mapId == 52) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMap(player, 114, Util.nextInt(0, 24), 354, 240);
                            break;
                    }
                }
            } else if (this.mapId == 154) {
                if (player.iDMark.isBaseMenu()) {
                    switch (select) {
                        case 0:
                            ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                            break;
                        case 1:
                            if (!DBService.gI().getGameConfig().isOpenPrisonPlanet()) {
                                Service.getInstance().sendThongBao(player,
                                        "Lối vào hành tinh ngục tù chưa mở");
                                return;
                            }
                            if (player.nPoint.power < 60000000000L) {
                                Service.getInstance().sendThongBao(player,
                                        "Yêu cầu tối thiếu 60tỷ sức mạnh");
                                return;
                            }
                            if (player.playerTask.taskMain.id < 22) {
                                this.npcChat(player, "Bạn phải hoàn thành xong nhiệm vụ Fide mới được qua!!!");
                                return;
                            }
                            ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                            break;
                    }
                }
            } else if (this.mapId == 155) {
                if (player.iDMark.isBaseMenu()) {
                    if (select == 0) {
                        ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                    }
                }
            } else if (MapService.gI().isMapMabuWar(this.mapId)) {
                if (player.iDMark.isBaseMenu()) {
                    if (MabuWar.gI().isTimeMabuWar()) {
                        switch (select) {
                            case 0:
                                if (player.inventory.getGold() >= 50000000) {
                                    Service.getInstance().changeFlag(player, 9);
                                    player.inventory.subGold(50000000);

                                } else {
                                    Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                }
                                break;
                            case 1:
                                if (player.zone.map.mapId == 120) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player,
                                            player.gender + 21, -1, 250);
                                }
                                if (player.cFlag == 9) {
                                    if (player.getPowerPoint() >= 20) {
                                        if (!(player.zone.map.mapId == 119)) {
                                            int idMapNextFloor = player.zone.map.mapId == 115
                                                    ? player.zone.map.mapId + 2
                                                    : player.zone.map.mapId + 1;
                                            ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                    354, 240);
                                        } else {
                                            Zone zone = MabuWar.gI().getMapLastFloor(120);
                                            if (zone != null) {
                                                ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                            }
                                        }
                                        player.resetPowerPoint();
                                        player.sendMenuGotoNextFloorMabuWar = false;
                                        Service.getInstance().sendPowerInfo(player, "%",
                                                player.getPowerPoint());
                                        if (Util.isTrue(1, 30)) {
                                            player.inventory.ruby += 1;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn nhận được 1 Hồng Ngọc");
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                        }
                                    } else {
                                        this.npcChat(player,
                                                "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                    }
                                    break;
                                } else {
                                    this.npcChat(player,
                                            "Ngươi đang theo phe Babiđây,Hãy qua bên đó mà thể hiện");
                                }
                        }
                    } else if (MabuWar14h.gI().isTimeMabuWar()) {
                        switch (select) {
                            case 0:
                                if (player.effectSkin.isPhuHo) {
                                    this.npcChat("Con đã mang trong mình sức mạnh của thần Kaiô!");
                                    return;
                                }
                                if (player.inventory.ruby < 55) {
                                    Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                } else {
                                    player.inventory.ruby -= 55;
                                    player.effectSkin.isPhuHo = true;
                                    Service.getInstance().point(player);
                                    this.npcChat("Ta đã phù hộ cho con hãy giúp ta tiêu diệt Mabư!");
                                }
                                break;
                            case 2:
                                ChangeMapService.gI().changeMapBySpaceShip(player, 52, -1, 250);
                                break;
                        }
                    }
                }
            }
        }

    }

    void DoiTheSuuTam(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item tl = InventoryService.gI().findGodClothes(player);
            Item tv = InventoryService.gI().findItemBag(player, 457);
            if (tl != null && tv != null) {
                Item the = ItemService.gI().createNewItem((short) Util.nextInt(828, 830));
                InventoryService.gI().subQuantityItemsBag(player, tl, 1);
                InventoryService.gI().subQuantityItemsBag(player, tv, 1);
                InventoryService.gI().addItemBag(player, the, 999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được : " + the.template.name);
            } else {
                this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ Trang bị thần linh hoặc thỏi vàng !",
                        "Đóng");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang đã đầy !");
        }
    }
}
