
package nro.models.npc.specialnpc;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

public class EggLinhThu {

    private static final long DEFAULT_TIME_DONE = 2592000000L;

    private Player player;
    public long lastTimeCreate;
    public long timeDone;
    public int type;
    private final short id = 50;

    public EggLinhThu(Player player, long lastTimeCreate, long timeDone, int type) {
        this.player = player;
        this.lastTimeCreate = lastTimeCreate;
        this.timeDone = timeDone;
        this.type = type;
    }

    public static void createEggLinhThu(Player player, int type) {
        player.egglinhthu = new EggLinhThu(player, System.currentTimeMillis(), DEFAULT_TIME_DONE, type);
    }

    public void sendEggLinhThu() {
        Message msg;
        try {
            msg = new Message(-122);
            msg.writer().writeShort(this.id);
            msg.writer().writeByte(1);
            msg.writer().writeShort(type == 0 ? 15073 : 15074);
            msg.writer().writeByte(0);
            msg.writer().writeInt(this.getSecondDone());
            this.player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSecondDone() {
        int seconds = (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
        return seconds > 0 ? seconds : 0;
    }

    public int getTimeDone() {
        return (int) ((lastTimeCreate + timeDone - System.currentTimeMillis()) / 1000);
    }

    public void openEgg() {
        if (InventoryService.gI().getCountEmptyBag(this.player) > 0) {
            try {
                destroyEgg();
                Thread.sleep(4000);
                int[] list_linh_thu = new int[] { 2014, 2015, 2016, 2017, 2018 };
                Item linhThu = ItemService.gI()
                        .createNewItem((short) list_linh_thu[Util.nextInt(list_linh_thu.length)]);
                laychiso(player, linhThu, 0);
                InventoryService.gI().addItemBag(player, linhThu, 0);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                ChangeMapService.gI().changeMapInYard(this.player, this.player.gender * 7, -1, Util.nextInt(300, 500));
                player.egglinhthu = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Service.getInstance().sendThongBao(player, "Hành trang không đủ chỗ trống");
        }
    }

    public void destroyEgg() {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(101);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.player.egglinhthu = null;
    }

    public void subTimeDone(int d, int h, int m, int s) {
        this.timeDone -= ((d * 24 * 60 * 60 * 1000) + (h * 60 * 60 * 1000) + (m * 60 * 1000) + (s * 1000));
        this.sendEggLinhThu();
    }

    public void dispose() {
        this.player = null;
    }

    public void laychiso(Player player, Item linhthu, int lvnow) {

        if (Util.isTrue(25, 100)) {
            linhthu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
            linhthu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
            linhthu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
        } else {
            linhthu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 5)));
            linhthu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 5)));
            linhthu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 5)));
        }
        switch (linhthu.template.id) {
            case 2021:
                linhthu.itemOptions.add(new ItemOption(97, 5));
                linhthu.itemOptions.add(new ItemOption(109, 5));
                break;
            case 2024:
                linhthu.itemOptions.add(new ItemOption(14, 2));
                linhthu.itemOptions.add(new ItemOption(8, 5));
                break;
            case 2019:
                linhthu.itemOptions.add(new ItemOption(231, 2));
                linhthu.itemOptions.add(new ItemOption(101, 10));
                break;
            case 2020:
                linhthu.itemOptions.add(new ItemOption(14, 2));
                break;
            case 2025:
                linhthu.itemOptions.add(new ItemOption(108, 3));
                linhthu.itemOptions.add(new ItemOption(233, 2));
                break;
            case 2023:
                linhthu.itemOptions.add(new ItemOption(94, 2));
                linhthu.itemOptions.add(new ItemOption(5, 2));
                break;
            case 2022:
                linhthu.itemOptions.add(new ItemOption(14, 1));
                linhthu.itemOptions.add(new ItemOption(232, 3));
                break;
            case 2026:
                linhthu.itemOptions.add(new ItemOption(106, 1));
                break;
        }
    }
}
