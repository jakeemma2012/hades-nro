
package nro.models.minigames;

import java.util.ArrayList;
import java.util.List;
import nro.consts.ConstNpc;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author Administrator
 */
public class RandomDiemDanh implements Runnable {

    public long lastTimeEnd;

    private static RandomDiemDanh instance;

    public List<Player> listpl = new ArrayList<>();

    String nameLast = "";

    public static RandomDiemDanh gI() {
        if (instance == null) {
            instance = new RandomDiemDanh();
        }
        return instance;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (((lastTimeEnd - System.currentTimeMillis()) / 1000) <= 0) {
                    if (!listpl.isEmpty()) {
                        int i = Util.nextInt(0, listpl.size() - 1);
                        int idPl = (int) listpl.get(i).id;
                        nameLast = listpl.get(i).name;
                        RewardPlChoose(idPl);
                    }
                    ResetAll();
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void ResetAll() {
        listpl.clear();
        lastTimeEnd = System.currentTimeMillis() + 3600000;
    }

    public void addPlayerChar(Player player) {
        if (!listpl.contains(player)) {
            listpl.add(player);
            Service.getInstance().sendThongBao(player, "|2|Bạn vừa báo danh thành công ! Chúc bạn may mắn");
        } else {
            Service.getInstance().sendThongBao(player, "|7|Bạn đã báo danh rồi !");
        }
    }

    void RewardPlChoose(int idPL) {
        Item vp = ItemService.gI().createNewItem((short) 0);
        int rd = Util.nextInt(0, 10);
        switch (rd) {
            case 0:
                vp.template = ItemService.gI().getTemplate(861);
                vp.quantity = Util.nextInt(1000, 10000);
                break;
            case 1:
                vp.template = ItemService.gI().getTemplate(457);
                vp.quantity = 200;
                break;
            case 2:
                vp.template = ItemService.gI().getTemplate(1302);
                break;
            case 3:
                vp.template = ItemService.gI().getTemplate(1303);
                break;
            case 4:
                vp.template = ItemService.gI().getTemplate(17);
                break;
            case 5:
                vp.template = ItemService.gI().getTemplate(1150);
                break;
            case 6:
                vp.template = ItemService.gI().getTemplate(1151);
                break;
            case 7:
                vp.template = ItemService.gI().getTemplate(1152);
                break;
            case 8:
                vp.template = ItemService.gI().getTemplate(381);
                break;
            case 9:
                vp.template = ItemService.gI().getTemplate(382);
                break;
            case 10:
                vp.template = ItemService.gI().getTemplate(384);
                break;
            default:
                break;
        }
        nameLast += " \n|2|Nhận : " + vp.template.name + " - số lượng : " + vp.quantity;
        PlayerDAO.updatePlayerRandomDiemDanh(idPL, vp);
    }

    public void ShowMenu(Player player, Npc npc) {
        long timeNum = ((RandomDiemDanh.gI().lastTimeEnd - System.currentTimeMillis()) / 1000);
        StringBuilder menuBuilder = new StringBuilder("|8|Xin chào cậu !")
                .append("\n|6|Hiện tại tôi đang nắm giữ danh sách của Server")
                .append("cậu có muốn ghi danh để có cơ hội nhận được 1 trong những phần quà đặc biệt từ đội ngũ ADMIN không ?")
                .append("\nMỗi 1 tiếng , tôi sẽ random lấy 1 trong những player đã báo danh để nhận thưởng và phần thưởng được gửi vào hòm Item Reward tại nhà")
                .append("\nKhi nhận được vật phẩm , xin hãy lưu ý thoát trò chơi và đăng nhập lại")
                .append("\n|7|Phần thưởng vẫn sẽ được trao ngay cả khi bạn Offline")
                .append("\n|2|Chúc bạn may mắn !!!")
                .append("\n|4|Người chơi nhận được phiên trước : " + nameLast)
                .append("\n|7|Thòi gian còn lại quay thưởng : " + Util.convertSeconds(timeNum))
                .append("\n Số người tham gia : " + listpl.size());
        if (timeNum > 5) {
            npc.createOtherMenu(player, 2, menuBuilder.toString(),
                    "Update", "Báo danh", "Đóng");
        } else {
            npc.createOtherMenu(player, 2, menuBuilder.toString(), "Update");
        }
    }
}
