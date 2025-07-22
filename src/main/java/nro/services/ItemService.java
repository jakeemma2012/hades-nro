package nro.services;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.models.kygui.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.utils.Log;
import nro.utils.Util;

/**
 *
 */
public class ItemService {

    public static final int TDST_NM_BOSS = 1;
    public static final int BOJACK_BOSS = 2;
    public static final int FUTURE_BOSS = 3;
    public static final int BIG_MOB_1 = 4;

    private static final short LIST_ITEM_BOSS[] = {18, 17, 19, 20,1355};

    private static final short LIST_ITEM_FOOD[] = {18, 17,  19, 20, 441, 442, 443, 444, 445, 446, 447,1355};

    private static final short LIST_ITEM_OEMGA[] = {18, 17,  19, 20, 441, 442, 443, 444, 445, 446, 447,1355};

    private static final short LIST_ITEM_BT[] = {19, 20, 933};

    private static final short LIST_ITEM_BDKB[] = {933};

    private static final short LIST_ITEM_TUIVANG[] = {19,18,380};
        
    private static final short LIST_ITEM_TDST[] = {18, 17,  19, 20, 441, 442, 443, 444, 445, 446, 447,1355};
    private static final short LIST_ITEM_BOJACK[] = {18, 17,  19, 20, 441, 442, 443, 444, 445, 446, 447,1355};
    private static final short LIST_ITEM_FUTURE[] = {190, 190, 190, 190, 190, 18, 19, 20, 757, 757, 757, 380, 380,
        380, 380, 380, 380,
        380, 380};
    private static final short LIST_ITEM_BIG_MOB_1[] = {190, 190, 190, 190, 190, 18, 19, 20, 757, 757, 757, 757,
        757, 757, 570};
    private static final short LIST_ITEM_EVENT[] = {190, 20, 589, 589};
    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (ItemOption io : itemShop.options) {
            item.itemOptions.add(new ItemOption(io));
        }
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        return it;
    }

    public Item otpKH(short tempId) {
        return otpKH(tempId, 1);
    }

    public Item Disguise() {
        Item it = ItemService.gI().createNewItem((short) 742);
        it.itemOptions.add(new ItemOption(50, 80));
        it.itemOptions.add(new ItemOption(77, 80));
        it.itemOptions.add(new ItemOption(103, 80));
        it.itemOptions.add(new ItemOption(228, 0));
        it.itemOptions.add(new ItemOption(230, 0));
        return it;
    }

    public Item otpKH(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        switch (item.template.type) {
            case 0:
                item.itemOptions.add(new ItemOption(47, Util.nextInt(1, 4)));
                break;
            case 1:
                item.itemOptions.add(new ItemOption(6, Util.nextInt(150, 200)));
                break;
            case 2:
                item.itemOptions.add(new ItemOption(0, Util.nextInt(7, 29)));
                break;
            case 3:
                item.itemOptions.add(new ItemOption(7, Util.nextInt(2, 12)));
                break;
            case 4:
                item.itemOptions.add(new ItemOption(14, 1));
                break;
            default:
                break;
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public void setSongoku(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(129, 0));//129
        quan.itemOptions.add(new ItemOption(129, 0));
        gang.itemOptions.add(new ItemOption(129, 0));
        giay.itemOptions.add(new ItemOption(129, 0));
        rd.itemOptions.add(new ItemOption(129, 0));
        ao.itemOptions.add(new ItemOption(141, 0));
        quan.itemOptions.add(new ItemOption(141, 0));
        gang.itemOptions.add(new ItemOption(141, 0));
        giay.itemOptions.add(new ItemOption(141, 0));
        rd.itemOptions.add(new ItemOption(141, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kame");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setThenXinHang(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(128, 0));
        quan.itemOptions.add(new ItemOption(128, 0));
        gang.itemOptions.add(new ItemOption(128, 0));
        giay.itemOptions.add(new ItemOption(128, 0));
        rd.itemOptions.add(new ItemOption(128, 0));
        ao.itemOptions.add(new ItemOption(139, 0));
        quan.itemOptions.add(new ItemOption(139, 0));
        gang.itemOptions.add(new ItemOption(139, 0));
        giay.itemOptions.add(new ItemOption(139, 0));
        rd.itemOptions.add(new ItemOption(139, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Thên Xin Hăng");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKaioKen(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 0);
        Item quan = ItemService.gI().otpKH((short) 6);
        Item gang = ItemService.gI().otpKH((short) 21);
        Item giay = ItemService.gI().otpKH((short) 27);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(128, 0));
        quan.itemOptions.add(new ItemOption(128, 0));
        gang.itemOptions.add(new ItemOption(128, 0));
        giay.itemOptions.add(new ItemOption(128, 0));
        rd.itemOptions.add(new ItemOption(128, 0));
        ao.itemOptions.add(new ItemOption(140, 0));
        quan.itemOptions.add(new ItemOption(140, 0));
        gang.itemOptions.add(new ItemOption(140, 0));
        giay.itemOptions.add(new ItemOption(140, 0));
        rd.itemOptions.add(new ItemOption(140, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kaioken");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setLienHoan(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(131, 0));
        quan.itemOptions.add(new ItemOption(131, 0));
        gang.itemOptions.add(new ItemOption(131, 0));
        giay.itemOptions.add(new ItemOption(131, 0));
        rd.itemOptions.add(new ItemOption(131, 0));

        ao.itemOptions.add(new ItemOption(143, 0));
        quan.itemOptions.add(new ItemOption(143, 0));
        gang.itemOptions.add(new ItemOption(143, 0));
        giay.itemOptions.add(new ItemOption(143, 0));
        rd.itemOptions.add(new ItemOption(143, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Liên hoàn");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPicolo(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(130, 0));
        quan.itemOptions.add(new ItemOption(130, 0));
        gang.itemOptions.add(new ItemOption(130, 0));
        giay.itemOptions.add(new ItemOption(130, 0));
        rd.itemOptions.add(new ItemOption(130, 0));

        ao.itemOptions.add(new ItemOption(142, 0));
        quan.itemOptions.add(new ItemOption(142, 0));
        gang.itemOptions.add(new ItemOption(142, 0));
        giay.itemOptions.add(new ItemOption(142, 0));
        rd.itemOptions.add(new ItemOption(142, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setPikkoroDaimao(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 1);
        Item quan = ItemService.gI().otpKH((short) 7);
        Item gang = ItemService.gI().otpKH((short) 22);
        Item giay = ItemService.gI().otpKH((short) 28);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(132, 0));
        quan.itemOptions.add(new ItemOption(132, 0));
        gang.itemOptions.add(new ItemOption(132, 0));
        giay.itemOptions.add(new ItemOption(132, 0));
        rd.itemOptions.add(new ItemOption(132, 0));

        ao.itemOptions.add(new ItemOption(144, 0));
        quan.itemOptions.add(new ItemOption(144, 0));
        gang.itemOptions.add(new ItemOption(144, 0));
        giay.itemOptions.add(new ItemOption(144, 0));
        rd.itemOptions.add(new ItemOption(144, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Picolo");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setKakarot(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(133, 0));
        quan.itemOptions.add(new ItemOption(133, 0));
        gang.itemOptions.add(new ItemOption(133, 0));
        giay.itemOptions.add(new ItemOption(133, 0));
        rd.itemOptions.add(new ItemOption(133, 0));

        ao.itemOptions.add(new ItemOption(136, 0));
        quan.itemOptions.add(new ItemOption(136, 0));
        gang.itemOptions.add(new ItemOption(136, 0));
        giay.itemOptions.add(new ItemOption(136, 0));
        rd.itemOptions.add(new ItemOption(136, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Kakarot");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setCadic(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(134, 0));
        quan.itemOptions.add(new ItemOption(134, 0));
        gang.itemOptions.add(new ItemOption(134, 0));
        giay.itemOptions.add(new ItemOption(134, 0));
        rd.itemOptions.add(new ItemOption(134, 0));

        ao.itemOptions.add(new ItemOption(137, 0));
        quan.itemOptions.add(new ItemOption(137, 0));
        gang.itemOptions.add(new ItemOption(137, 0));
        giay.itemOptions.add(new ItemOption(137, 0));
        rd.itemOptions.add(new ItemOption(137, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Ca đíc");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setNappa(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1228);
        Item ao = ItemService.gI().otpKH((short) 2);
        Item quan = ItemService.gI().otpKH((short) 8);
        Item gang = ItemService.gI().otpKH((short) 23);
        Item giay = ItemService.gI().otpKH((short) 29);
        Item rd = ItemService.gI().otpKH((short) 12);
        ao.itemOptions.add(new ItemOption(135, 0));
        quan.itemOptions.add(new ItemOption(135, 0));
        gang.itemOptions.add(new ItemOption(135, 0));
        giay.itemOptions.add(new ItemOption(135, 0));
        rd.itemOptions.add(new ItemOption(135, 0));

        ao.itemOptions.add(new ItemOption(138, 0));
        quan.itemOptions.add(new ItemOption(138, 0));
        gang.itemOptions.add(new ItemOption(138, 0));
        giay.itemOptions.add(new ItemOption(138, 0));
        rd.itemOptions.add(new ItemOption(138, 0));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        rd.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, rd, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set Kích hoạt Nappa");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public Item DoThienSu(int itemId, int gender, int perSuccess, int perLucky) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            switch (gender) {
                case 0:
                    dots.itemOptions.add(new ItemOption(47, Util.nextInt(120, 135) * 1_600 / 100)); // áo TD giáp
                    break;
                case 1:
                    dots.itemOptions.add(new ItemOption(47, Util.nextInt(120, 135) * 1_700 / 100)); // áo NM giáp
                    break;
                case 2:
                    dots.itemOptions.add(new ItemOption(47, Util.nextInt(120, 135) * 1_800 / 100)); // áo XD giáp
                    break;
            }
        }
        //quần
        if (Util.isTrue(80, 100)) {
            if (quan.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 130) * 104 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 130) * 16_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 130) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 130) * 14_000 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 130) * 96 / 100)); // quần XD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 130) * 12_000 / 100)); // quần XD hồi hp
                        break;
                }
            }
        } else {
            if (quan.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 135) * 104 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 135) * 16_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 135) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 135) * 14_000 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(22, Util.nextInt(120, 135) * 96 / 100)); // quần XD hp
                        dots.itemOptions.add(new ItemOption(27, Util.nextInt(120, 135) * 12_000 / 100)); // quần XD hồi hp
                        break;
                }
            }
        }
        //găng
        if (Util.isTrue(80, 100)) {
            if (gang.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 130) * 10_800 / 100)); // gang TD sd
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 130) * 10_800 / 100)); // gang NM sd
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 130) * 10_800 / 100)); // gang XD sd
                        break;
                }
            }
        } else {
            if (gang.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 135) * 10_800 / 100)); // gang TD sd
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 135) * 10_800 / 100)); // gang NM sd
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(0, Util.nextInt(120, 135) * 10_800 / 100)); // gang XD sd
                        break;
                }
            }
        }
        //giày
        if (Util.isTrue(80, 100)) {
            if (giay.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 130) * 96 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 130) * 12_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 130) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 130) * 12_800 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 130) * 92 / 100)); // quần XD hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 130) * 11_200 / 100)); // quần XD hồi hp
                        break;
                }
            }
        } else {
            if (giay.contains(itemId)) {
                switch (gender) {
                    case 0:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 135) * 96 / 100)); // quần TD hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 135) * 12_000 / 100)); // quần td hồi hp
                        break;
                    case 1:
                        dots.itemOptions.add(new ItemOption(23, Util.nextInt(120, 135) * 100 / 100)); // quần NM hp
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 135) * 12_800 / 100)); // quần NM hồi hp
                        break;
                    case 2:
                        dots.itemOptions.add(new ItemOption(28, Util.nextInt(120, 135) * 11_200 / 100)); // quần XD hồi hp
                        break;
                }
            }
        }
        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new ItemOption(14, Util.nextInt(120, 135) * 16 / 100)); // nhẫn 18-20%
        }
        dots.itemOptions.add(new ItemOption(21, 90));
        dots.itemOptions.add(new ItemOption(30, 0));
        if (perSuccess <= perLucky) {
            if (perSuccess >= (perLucky - 3)) {
                perLucky = 3;
            } else if (perSuccess <= (perLucky - 4) && perSuccess >= (perLucky - 10)) {
                perLucky = 2;
            } else {
                perLucky = 1;
            }
            dots.itemOptions.add(new ItemOption(41, perLucky));
            ArrayList<Integer> listOptionBonus = new ArrayList<>();
            listOptionBonus.add(42);
            listOptionBonus.add(43);
            listOptionBonus.add(44);
            listOptionBonus.add(45);
            listOptionBonus.add(46);
            listOptionBonus.add(197);
            listOptionBonus.add(198);
            listOptionBonus.add(199);
            listOptionBonus.add(200);
            listOptionBonus.add(201);
            listOptionBonus.add(202);
            listOptionBonus.add(203);
            listOptionBonus.add(204);
            for (int i = 0; i < perLucky; i++) {
                perSuccess = Util.nextInt(0, listOptionBonus.size() - 1);
                dots.itemOptions.add(new ItemOption(listOptionBonus.get(perSuccess), Util.nextInt(1, 6)));
                listOptionBonus.remove(perSuccess);
            }
        }
        return dots;
    }

    public ConsignmentItem convertToConsignmentItem(Item item) {
        ConsignmentItem it = new ConsignmentItem();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        it.setPriceGold(-1);
        it.setPriceGem(-1);
        return it;
    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public ConsignmentItem createNewConsignmentItem(short tempId, int quantity) {
        ConsignmentItem item = new ConsignmentItem();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }
    
   public ItemTemplate getTemplateByName (String name) {
       for(ItemTemplate it : Manager.ITEM_TEMPLATES){
           if(it.name.equals(name)) {
               return it;
           }
       }
     return null;
   }
    
    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                    return 10;
                case 530:
                case 535:
                    return 20;
                case 531:
                case 536:
                    return 30;
                case 1509:
                    return 40;
                default:
                    return 0;
            }
        } else {
            return 0;
        }
    }

    public boolean isTrainArmor(Item item) {
        if (item != null) {
            switch (item.template.id) {
                case 529:
                case 534:
                case 530:
                case 535:
                case 531:
                case 536:
                case 1509:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean isOutOfDateTime(Item item) {
        long now = System.currentTimeMillis();
        if (item != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                } else if (io.optionTemplate.id == 196) {
                    long e = io.param * 1000L;
                    if (e <= now) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isItemNoLimitQuantity(int id) {// item k giới hạn số lượng
        if (id >= 1066 && id <= 1070) {// mảnh trang bị thiên sứ
            return true;
        }
        return false;
    }

    public int randomSKHId(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[][] options = { { 129, 127 , 128 }, { 132, 131, 130 }, { 134, 133, 135 } };
        int skhv1 = 29;
        int skhv2 = 34;
        int skhc = 37;

        int skhId = 0;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }

    public void AddOptionSKH(ItemMap item, int skhId) {
        AddOptionSKHAll(item.options, skhId);
    }

    public void AddOptionSKH(Item item, int skhId) {
        AddOptionSKHAll(item.itemOptions, skhId);
    }

    private void AddOptionSKHAll(List<ItemOption> item, int skhId) {
        item.add(new ItemOption(skhId, 1));
        item.add(new ItemOption(optionIdSKH(skhId), 1));
        item.add(new ItemOption(30, 1));
    }

    public int optionIdSKH(int skhId) {
        switch (skhId) {
            case 129:
                return 141;
            case 127:
                return 139;
            case 128:
                return 140;
            case 131:
                return 143;
            case 132:
                return 144;
            case 130:
                return 142;
            case 135:
                return 138;
            case 133:
                return 136;
            case 134:
                return 137;
            case 210:
                return 222;
            case 211:
                return 223;
            case 216:
                return 219;
            case 217:
                return 220;
            case 213:
                return 225;
            case 214:
                return 226;
        }
        return 0;
    }

    public ItemMap BaseRewar(Zone zone, Player player, int x, int y, byte type) {
        ItemMap itemMap = null;
        short[] set1 = {190};
        switch (type) {
            case TDST_NM_BOSS:
                set1 = LIST_ITEM_TDST;
                break;
            case BOJACK_BOSS:
                set1 = LIST_ITEM_BOJACK;
                break;
            case FUTURE_BOSS:
                set1 = LIST_ITEM_FUTURE;
                break;
            case BIG_MOB_1:
                set1 = LIST_ITEM_BIG_MOB_1;
                break;
            case 5:
                set1 = LIST_ITEM_EVENT;
                break;
            case 6:
                set1 = LIST_ITEM_BOSS;
                break;
            case 7:
                set1 = LIST_ITEM_FOOD;
                break;
            case 8:
                set1 = LIST_ITEM_OEMGA;
                break;
            case 9:
                set1 = LIST_ITEM_BT;
                break;
            case 10:
                set1 = LIST_ITEM_BDKB;
                break;
            case 11:
                set1 = LIST_ITEM_TUIVANG;
                break;

        }
        short idItem = (short) set1[Util.nextInt(0, set1.length - 1)];
        itemMap = new ItemMap(zone, idItem, (idItem == 190) ? 30000 : 1, x, y, player.id);
        AddOptionItemMap(itemMap);
        return itemMap;
    }

    public ItemMap AddOptionItemMap(ItemMap it) {
        switch (it.itemTemplate.type) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                trangBi_ItemMap(it);
                break;
            case 5:
                Ct_ItemMap(it);
                break;
            case 14:
                Dnc_ItemMap(it);
                break;
            case 30:
                Spl_ItemMap(it);
                break;
            case 27:
                _27_ItemMap(it);
                break;
            case 11:
                VPDL_ItemMap(it);
                break;
        }

        return it;
    }

    private ItemMap trangBi_ItemMap(ItemMap it) {

        RewardService.gI().initBaseOptionClothesMap(it);

        if (Util.isTrue(90, 100)) {
            // tỉ lệ ra spl 0-4 sao
            it.options.add(new ItemOption(107, Util.nextInt(4)));
        } else {
            // tỉ lệ ra spl 5 - 6 sao
            if (Util.isTrue(95, 100)) {
                it.options.add(new ItemOption(107, 5));
            } else {
                it.options.add(new ItemOption(107, 6));
            }

        }
        return it;
    }

    private ItemMap VPDL_ItemMap(ItemMap it) {
        // switch (it.itemTemplate.id) {
        // case 1114:
        // it.options.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI,
        // Util.nextInt(3, 12))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 12)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 18))); //
        // dame chi mang

        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.VPDL_LUOI_HAI:
        // it.options.add(new ItemOption(ConstOption.DEP_PT_SUC_DANH_CHO_MOI_NGUOI,
        // Util.nextInt(3, 8))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 12)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.SAT_THUONG_CHI_MANG,
        // Util.nextInt(5, 12))); // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // }
        return it;
    }

    private ItemMap Ct_ItemMap(ItemMap it) {
        // switch (it.itemTemplate.id) {
        // case ConstCT.CT_BUJIN:
        // it.options.add(new ItemOption(50, Util.nextInt(5, 7))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 7))); //
        // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(5, 7))); //
        // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(10, 15)));
        // // tnsm

        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_KOGU:
        // it.options.add(new ItemOption(50, Util.nextInt(7, 9))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(7, 9))); //
        // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(7, 9))); //
        // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(12, 17)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_ZANGYA:
        // it.options.add(new ItemOption(50, Util.nextInt(9, 11))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(9, 11)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(9, 11)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(14, 19)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_BIDO:
        // it.options.add(new ItemOption(50, Util.nextInt(11, 13))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(11, 13)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(11, 13)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(16, 21)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_BOJACK:
        // it.options.add(new ItemOption(50, Util.nextInt(13, 15))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(13, 15)));
        // // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(13, 15)));
        // // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(18, 23)));
        // // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SUPER_BOJACK:
        // it.options.add(new ItemOption(50, Util.nextInt(20))); // sd
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(20))); //
        // hut hp
        // it.options.add(new ItemOption(ConstOption.HUT_PT_KI, Util.nextInt(20))); //
        // hut ki
        // it.options.add(new ItemOption(ConstOption.TN_SM_PT, Util.nextInt(35))); //
        // tnsm
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_4:
        // it.options.add(new ItemOption(50, Util.nextInt(9, 15))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(9, 15))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(9, 15))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(9, 15))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 2))); //
        // giáp
        // it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_3:
        // it.options.add(new ItemOption(50, Util.nextInt(10, 17))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(10, 17))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(10, 17))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(10, 17))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(1, 4))); //
        // cm
        // it.options.add(new ItemOption(93, Util.nextInt(1, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_2:
        // it.options.add(new ItemOption(50, Util.nextInt(12, 18))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(12, 18))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(12, 18))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(12, 18))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(2, 5))); //
        // cm
        // it.options.add(new ItemOption(93, Util.nextInt(2, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_1:
        // it.options.add(new ItemOption(50, Util.nextInt(12, 20))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(12, 20))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(12, 20))); // ki
        // it.options.add(new ItemOption(94, Util.nextInt(12, 20))); // giáp
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(3, 7))); //
        // cm
        // it.options.add(new ItemOption(93, Util.nextInt(3, 5))); // hsd
        // break;
        // case ConstCT.CT_SO_TDT:
        // it.options.add(new ItemOption(50, Util.nextInt(12, 23))); // sd
        // it.options.add(new ItemOption(77, Util.nextInt(12, 23))); // hp
        // it.options.add(new ItemOption(103, Util.nextInt(12, 23))); // ki
        // it.options.add(new ItemOption(ConstOption.HUT_PT_HP, Util.nextInt(5, 12)));
        // // giáp
        // it.options.add(new ItemOption(94, Util.nextInt(12, 23))); // cm
        // it.options.add(new ItemOption(ConstOption.CHI_MANG, Util.nextInt(5, 12))); //
        // giáp
        // it.options.add(new ItemOption(93, Util.nextInt(3, 7))); // hsd
        // break;
        // }
        return it;
    }

    private ItemMap Dnc_ItemMap(ItemMap it) {
        int idParma;
        switch (it.itemTemplate.id) {
            case 220: // rada
                idParma = 71;
                break;
            case 221: // giay
                idParma = 70;
                break;
            case 222: // quan
                idParma = 69;
                break;
            case 223: // ao
                idParma = 68;
                break;
            case 224: // gang
                idParma = 67;
                break;
            case 1329: // đá nâng cấp
                idParma = 224;
                break;
            default:
                idParma = 0;
                break;
        }
        if (idParma > 0) {
            it.options.add(new ItemOption(idParma, 1));
        }

        return it;
    }

    private ItemMap Spl_ItemMap(ItemMap it) {

        int idParma;
        switch (it.itemTemplate.id) {
            case 441:
                idParma = 95;
                break;
            case 442:
                idParma = 96;
                break;
            case 443:
                idParma = 97;
                break;
            case 444:
                idParma = 98;
                break;
            case 445:
                idParma = 99;
                break;
            case 446:
                idParma = 10;
                break;
            case 447:
                idParma = 101;
                break;
            default:
                idParma = 0;
        }
        if (idParma > 0) {
            it.options.add(new ItemOption(idParma, 5));
        }

        return it;
    }

    private ItemMap _27_ItemMap(ItemMap it) {
        switch (it.itemTemplate.id) {
            case 568:
            case 590:
                it.options.add(new ItemOption(86, 1));
                break;
            case 1353:
                it.options.add(new ItemOption(30, 1));
                break;
            case 570:
                it.options.add(new ItemOption(72, Util.nextInt(8, 11)));
                break;
        }
        return it;
    }

    public void addItemDoiMocNap(Player player, int select) {
        switch (select) {
            case 0: {
                short idLidst[] = {1150, 1151, 1152, 1153, 1354, 1355};
                for (short id : idLidst) {
                    Item vp = ItemService.gI().createNewItem(id);
                    switch (vp.template.id) {
                        case 1150:
                        case 1151:
                        case 1152:
                        case 1153:
                            vp.quantity = 10;
                            break;
                        case 1354:
                            vp.quantity = 300;
                            break;
                        case 1355:
                            vp.quantity = 99;
                            break;
                        default:
                            break;
                    }
                    InventoryService.gI().addItemBag(player, vp, 9999);
                }
                InventoryService.gI().sendItemBags(player);
            }
            break;
            case 1: {
                short idLidst[] = {1150, 1151, 1152, 1153, 1354, 1356};
                for (short id : idLidst) {
                    Item vp = ItemService.gI().createNewItem(id);
                    switch (vp.template.id) {
                        case 1150:
                        case 1151:
                        case 1152:
                        case 1153:
                            vp.quantity = 50;
                            break;
                        case 1354:
                            vp.quantity = 700;
                            break;
                        case 1356:
                            vp.quantity = 99;
                            break;
                        default:
                            break;
                    }
                    InventoryService.gI().addItemBag(player, vp, 9999);
                }
                InventoryService.gI().sendItemBags(player);
            }
            break;
            case 2: {
                short idList[] = {1354, 1361, 1355, 1356};
                for (short id : idList) {
                    Item vp = ItemService.gI().createNewItem(id);
                    switch (vp.template.id) {
                        case 1354:
                            vp.quantity = 1000;
                            break;
                        case 1355:
                        case 1356:
                            vp.quantity = 200;
                            break;
                        default:
                            break;
                    }
                    InventoryService.gI().addItemBag(player, vp, 9999);
                }
                InventoryService.gI().sendItemBags(player);
               
            }
            case 3: {
                short idList[] = {1354, 1361, 1363, 1357};
                for (short id : idList) {
                    Item vp = ItemService.gI().createNewItem(id);
                    switch (vp.template.id) {
                        case 1354:
                            vp.quantity = 3000;
                            break;
                        case 1356:
                            vp.quantity = 200;
                            break;
                        case 1353:
                            vp.itemOptions.add(new ItemOption(5, 30));
                            break;
                        default:
                            break;
                    }
                    InventoryService.gI().addItemBag(player, vp, 9999);
                }
                InventoryService.gI().sendItemBags(player);
            }
            break;
            default:
                break;
        }
    }
    
    
    public boolean SubThoiVang(Player player, short cost) { // trừ thỏi vàng

        if (getQuantityItemOnBag(player, (short) 457) < cost) {
            Service.getInstance().sendThongBao(player, "Bạn không đủ thỏi vàng");
            return false;
        }
        InventoryService.gI().subQuantityItemsBag(player, (short) 457, cost);
        InventoryService.gI().sendItemBags(player);
        return true;
    }
    
    public int getQuantityItemOnBag(Player player, short itemId) {
        List<Integer> quantityItems = player.inventory.itemsBag.stream()
                .filter(it -> it != null && it.isNotNullItem() && it.template.id == itemId)
                .map(item -> item.quantity).collect(Collectors.toList());
        if (!quantityItems.isEmpty()) {
            return quantityItems.stream().reduce(0, Integer::sum);
        }
        return 0;
    }

}
