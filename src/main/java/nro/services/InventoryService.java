package nro.services;

import nro.consts.ConstItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.war.BlackBallWar;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 */
public class InventoryService {

    private static InventoryService i;

    public static InventoryService gI() {
        if (i == null) {
            i = new InventoryService();
        }
        return i;
    }

    public List<Item> copyItemsBag(Player player) {
        return copyList(player.inventory.itemsBag);
    }

    private List<Item> copyList(List<Item> items) {
        List<Item> list = new ArrayList<>();
        for (Item item : items) {
            list.add(ItemService.gI().copyItem(item));
        }
        return list;
    }

    public boolean existItemBag(Player player, int tempId) {
        return existItemInList(player.inventory.itemsBag, tempId);
    }

    private boolean existItemInList(List<Item> list, int tempId) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isNotNullItem() && list.get(i).template.id == tempId) {
                return true;
            }
        }
        return false;
    }

    public Item findItemBag(Player player, int tempId) {
        return this.findItem(player.inventory.itemsBag, tempId);
    }

    public boolean addItemBag(Player player, Item item, int maxQuantity) {
        if (ItemMapService.gI().isBlackBall(item.template.id)) {
            return BlackBallWar.gI().pickBlackBall(player, item);
        }

        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 221) {
                int rdParam = io.param;
                item.itemOptions.remove(io);
                item.itemOptions.add(new ItemOption(50, Util.nextInt(5, rdParam)));
                item.itemOptions.add(new ItemOption(77, Util.nextInt(5, rdParam)));
                item.itemOptions.add(new ItemOption(103, Util.nextInt(5, rdParam)));
            }
            break;
        }

        //tennis spaceship
        if (item.template.id == 453) {
            player.haveTennisSpaceShip = true;
            return true;
        }
        //đùi gà nướng
        if (item.template.id == 74) {
            player.nPoint.setFullHpMp();
            PlayerService.gI().sendInfoHpMp(player);
            return true;
        }

        //gold, gem, ruby
        switch (item.template.type) {
            case 9:
                if (player.inventory.gold + item.quantity <= player.inventory.getGoldLimit()) {
                    if (player.playerIntrinsic.intrinsic.id == 23) {
                        player.inventory.gold += player.nPoint.calPercent(item.quantity, player.playerIntrinsic.intrinsic.param1);
                    } else {
                        player.inventory.gold += item.quantity;
                    }
                    Service.getInstance().sendMoney(player);
                    return true;
                } else {
                    Service.getInstance().sendThongBao(player, "Vàng sau khi nhặt quá giới hạn cho phép");
                    return false;
                }
            case 10:
                player.inventory.gem += item.quantity;
                Service.getInstance().sendMoney(player);
                return true;
            case 34:
                player.inventory.ruby += item.quantity;
                Service.getInstance().sendMoney(player);
                return true;
        }

        //mở rộng hành trang - rương đồ
        switch (item.template.id) {
            case 517:
                if (player.inventory.itemsBag.size() < 60) {
                    player.inventory.itemsBag.add(ItemService.gI().createItemNull());
                    Service.getInstance().sendThongBaoOK(player, "Hành trang của bạn đã được mở rộng thêm 1 ô");
                    return true;
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Hành trang của bạn đã đạt tối đa");
                    return false;
                }
            case 518:
                if (player.inventory.itemsBox.size() < 40) {
                    player.inventory.itemsBox.add(ItemService.gI().createItemNull());
                    Service.getInstance().sendThongBaoOK(player, "Rương đồ của bạn đã được mở rộng thêm 1 ô");
                    return true;
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Rương đồ của bạn đã đạt tối đa");
                    return false;
                }
            case 988:
                if (player.inventory.getGoldLimit() < 1000_000_000_000L) {
                    player.inventory.goldLimit += 100_000_000_000L;
                    Service.getInstance().sendThongBao(player, "Giới hạn vàng của bạn đã tăng lên 100Tr\n" + "Giới hạn vàng hiện tại của bạn là " + Util.numberToMoney(player.inventory.getGoldLimit()));
                    return true;
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Giới hạn vàng của bạn đã đạt tối đa");
                    return false;
                }
            default:
                break;
        }
        return addItemList(player.inventory.itemsBag, item, maxQuantity);
    }

    public boolean addItemBox(Player player, Item item, int maxQuantity) {
        return addItemList(player.inventory.itemsBox, item, maxQuantity);
    }

    public boolean addItemBody(Player player, Item item, int maxQuantity) {
        return addItemList(player.inventory.itemsBody, item, maxQuantity);
    }

    public boolean addItemNotUpToUpQuantity(List<Item> items, Item item) {
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isNotNullItem()) {
                items.set(i, item);
                return true;
            }
        }
        return false;
    }

    public boolean addItemList(List<Item> items, Item item, int maxQuantity) {
        //nếu item ko có option, add option rỗng vào
        if (item.itemOptions.isEmpty()) {
            item.itemOptions.add(new ItemOption(73, 0));
        }

        //item cộng thêm chỉ số param: tự động luyện tập
        int optionId = isItemIncrementalOption(item);
        if (optionId != -1) {
            int param = 0;
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == optionId) {
                    param = io.param;
                }
            }
            for (Item it : items) {
                if (it.isNotNullItem() && it.template.id == item.template.id) {
                    for (ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            io.param += param;
                        }
                    }
                    return true;
                }
            }
        }

        Item itemAdd = item;
        // item tăng số lượng
        if (itemAdd.template.isUpToUp) { // fix ở đây
            for (Item it : items) {
                if (!it.isNotNullItem() || it.template.id != itemAdd.template.id) {
                    continue;
                }
                // 457-thỏi vàng; 590-bí kiếp
                if (itemAdd.template.id == 457 || itemAdd.template.id == 590
                        || itemAdd.template.id == 610
                        || itemAdd.template.type == 14 || itemAdd.template.type == 12
                        || itemAdd.template.type == 35
                        || itemAdd.template.type == 27 || itemAdd.template.type == 30
                        || itemAdd.template.type == 31 || itemAdd.template.type == 29) {
                    if (itemAdd.template.type == 12 || itemAdd.template.type == 29
                            || itemAdd.template.type == 27 || itemAdd.template.type == 31) {
                        int count = 0;
                        for (ItemOption io : itemAdd.itemOptions) {
                            if (io.optionTemplate.id == 30) {
                                count++;
                                break;
                            }
                        }
                        for (ItemOption io : it.itemOptions) {
                            if (io.optionTemplate.id == 30) {
                                count--;
                                break;
                            }
                        }
                        if (count != 0) {
                            continue;
                        }
                    }
                    it.quantity += itemAdd.quantity;
                    itemAdd.quantity = 0;
                    return true;
                }
                if (itemAdd.template.id == 570) { // rương gỗ
                    int level_chest_1 = 0;
                    int level_chest_2 = 0;
                    for (ItemOption io : itemAdd.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            level_chest_1 = io.param;
                            break;
                        }
                    }
                    for (ItemOption io : it.itemOptions) {
                        if (io.optionTemplate.id == 72) {
                            level_chest_2 = io.param;
                            break;
                        }
                    }
                    if (level_chest_1 != level_chest_2) {
                        continue;
                    }
                }
                if (it.quantity < 99) {
                    int add = 99 - it.quantity;
                    if (itemAdd.quantity <= add) {
                        it.quantity += itemAdd.quantity;
                        itemAdd.quantity = 0;
                        return true;
                    } else {
                        it.quantity = 99;
                        itemAdd.quantity -= add;
                    }
                }
            }
        }

        //add item vào ô mới
        for (int i = 0; i < items.size(); i++) {
            if (!items.get(i).isNotNullItem()) {
                items.set(i, ItemService.gI().copyItem(item));
                item.quantity = 0;
                return true;
            }
        }
        return false;
    }

    private boolean isItemIncremental(Item item) { //item cộng dồn số lượng
        if (item.template.id == 454 || item.template.id == 921 || item.template.id == 1995) {
            return false;
        }
        switch (item.template.type) {
            case 75:
            case 8: //vật phẩm nhiệm vụ
            case 12: //ngọc rồng
            case 30: //sao pha lê
            case 6: //đậu thần
            case 29: //item time, đồ ăn
            case 25: //rađa dò ngọc namếc
            case 27: //đồ tạp
            case 33: //mảnh rada
            case 14: //đá nâng cấp
            case 50: //vé đổi đồ hủy diệt
                return true;
            default:
                return false;
        }
    }

    private byte isItemIncrementalOption(Item item) { //trả về id option template
        int temp = item.template.id;
        byte opp = -1;
        switch (temp) {
            case 521:
                opp = 1;
                break;
            default:
                break;

        }
        return opp;
    }

    public void throwItem(Player player, int where, int index) {
        Item itemThrow = null;
        if (where == 0) {
            if (index >= 0 && index <= player.inventory.itemsBody.size()) {
                itemThrow = player.inventory.itemsBody.get(index);
                if (itemThrow.isNotNullItem()) {
                    removeItemBody(player, index);
                    sendItemBody(player);
                }
            }
        } else if (where == 1) {
            if (index >= 0 && index <= player.inventory.itemsBag.size()) {
                itemThrow = player.inventory.itemsBag.get(index);
                if (itemThrow.isNotNullItem()) {
                    if (itemThrow.template.id != 457) {
                        removeItemBag(player, index);
                        sortItemBag(player);
                        sendItemBags(player);
                    } else {
                        Service.getInstance().sendThongBao(player, "Đây là vật phẩm quý hiếm, không thể vứt được");
                    }
                }
            }
        }
        if (!itemThrow.isNotNullItem()) {
            return;
        } else {
            Service.getInstance().point(player);
        }
        Service.getInstance().Send_Caitrang(player);
    }

    public void arrangeItems(List<Item> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (!list.get(i).isNotNullItem()) {
                int indexSwap = -1;
                for (int j = i + 1; j < list.size(); j++) {
                    if (list.get(j).isNotNullItem()) {
                        indexSwap = j;
                        break;
                    }
                }
                if (indexSwap != -1) {
                    Item sItem = ItemService.gI().createItemNull();
                    list.set(i, list.get(indexSwap));
                    list.set(indexSwap, sItem);
                } else {
                    break;
                }
            }
        }
    }

    private Item putItemBag(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
            if (!player.inventory.itemsBag.get(i).isNotNullItem()) {
                player.inventory.itemsBag.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBox(Player player, Item item) {
        for (int i = 0; i < player.inventory.itemsBox.size(); i++) {
            if (!player.inventory.itemsBox.get(i).isNotNullItem()) {
                player.inventory.itemsBox.set(i, item);
                Item sItem = ItemService.gI().createItemNull();
                return sItem;
            }
        }
        return item;
    }

    private Item putItemBody(Player player, Item item) {
        Item sItem = item;
        byte type = item.getType();
        if (item.isNotNullItem()) {
            if (type >= 0 && type <= 5 || type == 32 || type == 11 || type == 23 || type == 24 || type == 72 || type == 21 || type == 28) {
                if (item.template.gender == player.gender || item.template.gender == 3) {
                    if (item.getId() == ConstItem.QUAN_DI_BIEN) {
                        List<Item> itemsBody = player.inventory.itemsBody;
                        if (itemsBody.get(0).isNotNullItem() && itemsBody.get(5).isNotNullItem()) {
                            Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Vui lòng cởi áo để có thể sử dụng!");
                            return sItem;
                        }
                    }
                    boolean forPet = false;
                    long powerRequire = item.template.strRequire;
                    for (ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 21) {
                            powerRequire = io.param * 1000000000L;
                            break;
                        } else if (io.optionTemplate.id == 197 && !player.isPet) {
                            forPet = true;
                        }
                    }
                    if (forPet) {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị chỉ dành cho đệ tử!");
                    } else if (player.isPet && type > 6 && type != 32 && type != 11) {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
                    } else if (powerRequire <= player.nPoint.power) {
                        byte index = 0;
                        switch (type) {
                            case 11:
                                index = 7;
                                break;
                            case 23:
                            case 24:
                                index = 8;
                                break;
                            case 32:
                                index = 6;
                                break;
                            case 21:
                                index = 9;
                                break;
                            case 72:
                                index = 10;
                                break;
                            case 28:
                                index = 11;
                                break;
                            default:
                                index = type;
                        }
                        sItem = player.inventory.itemsBody.get(index);
                        player.inventory.itemsBody.set(index, item);
                    } else {
                        Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Sức mạnh không đủ yêu cầu!");
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
                }
            } else {
                Service.getInstance().sendThongBaoOK(player.isPet ? ((Pet) player).master : player, "Trang bị không phù hợp!");
            }
        }
        return sItem;
    }

    //==========================================================================
    public void itemBagToBody(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBag.size()) {
            return;
        }
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            player.inventory.itemsBag.set(index, putItemBody(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            Service.getInstance().sendFlagBag(player);
        }
    }

    public void itemBodyToBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (index == 10) {
                Service.getInstance().sendPetFollow(player, (short) 0);
            }
            if (index == 11) {
                Service.getInstance().removeTitle(player);
            }
            player.inventory.itemsBody.set(index, putItemBag(player, item));
            sendItemBags(player);
            sendItemBody(player);
            Service.getInstance().Send_Caitrang(player);
            Service.getInstance().point(player);
            Service.getInstance().sendFlagBag(player);
        }
    }

    public void itemBagToPetBody(Player player, int index) {
        if (player.pet != null && player.pet.nPoint.power >= 1500000) {
            if (index < 0 || index >= player.inventory.itemsBag.size()) {
                return;
            }
            Item item = player.inventory.itemsBag.get(index);
            if (item.isNotNullItem()) {
                if (item.template.type == 11) {
                    if (!item.haveOption(197)) {
                        Service.getInstance().sendThongBao(player, "Phải có chỉ số dành cho đệ tử !");
                        return;
                    }
                }
                Item itemSwap = putItemBody(player.pet, item);
                player.inventory.itemsBag.set(index, itemSwap);
                sendItemBags(player);
                sendItemBody(player);
                Service.getInstance().Send_Caitrang(player.pet);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().sendFlagBag(player.pet);
                if (!itemSwap.equals(item)) {
                    Service.getInstance().point(player);
                    Service.getInstance().showInfoPet(player);
                }
            }
        } else {
            Service.getInstance().sendThongBaoOK(player, "Đệ tử phải đạt 1tr5 sức mạnh mới có thể mặc");
        }
    }

    public void itemPetBodyToBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        if (player.pet != null) {
            Item item = player.pet.inventory.itemsBody.get(index);
            if (item.isNotNullItem()) {
                player.pet.inventory.itemsBody.set(index, putItemBag(player, item));
                sendItemBags(player);
                sendItemBody(player);
                Service.getInstance().Send_Caitrang(player.pet);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().point(player);
                Service.getInstance().sendFlagBag(player.pet);
                Service.getInstance().showInfoPet(player);
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn làm gì có đệ tử ?");
        }
    }

    //--------------------------------------------------------------------------
    public void itemBoxToBodyOrBag(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBox.size()) {
            return;
        }
        Item item = player.inventory.itemsBox.get(index);
        if (item.isNotNullItem()) {
            boolean done = false;
            if (item.template.type >= 0 && item.template.type <= 5 || item.template.type == 32) {
                Item itemBody = player.inventory.itemsBody.get(item.template.type == 32 ? 6 : item.template.type);
                if (!itemBody.isNotNullItem()) {
                    if (item.template.gender == player.gender || item.template.gender == 3) {
                        long powerRequire = item.template.strRequire;
                        for (ItemOption io : item.itemOptions) {
                            if (io.optionTemplate.id == 21) {
                                powerRequire = io.param * 1000000000L;
                                break;
                            }
                        }
                        if (powerRequire <= player.nPoint.power) {
                            player.inventory.itemsBody.set(item.template.type == 32 ? 6 : item.template.type, item);
                            player.inventory.itemsBox.set(index, itemBody);
                            done = true;

                            sendItemBody(player);
                            Service.getInstance().Send_Caitrang(player);
                            Service.getInstance().point(player);
                        }
                    }
                }
            }
            if (!done) {
                if (addItemBag(player, item, 99)) {
                    if (item.quantity == 0) {
                        Item sItem = ItemService.gI().createItemNull();
                        player.inventory.itemsBox.set(index, sItem);
                    }
                    sendItemBags(player);
                }
            }
            sendItemBox(player);
        }
    }

    public void itemBagToBox(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBag.size()) {
            return;
        }
        Item item = player.inventory.itemsBag.get(index);
        if (item.isNotNullItem()) {
            if (item.haveOption(93)) {
                Service.getInstance().sendThongBao(player, "Không thể bỏ vật phẩm ngày vào rương !");
                return;
            }
            if (addItemBox(player, item, 99)) {
                if (item.quantity == 0) {
                    Item sItem = ItemService.gI().createItemNull();
                    player.inventory.itemsBag.set(index, sItem);
                }
                arrangeItems(player.inventory.itemsBag);
                sendItemBags(player);
                sendItemBox(player);
            }
        }
    }

    public void itemBodyToBox(Player player, int index) {
        if (index < 0 || index >= player.inventory.itemsBody.size()) {
            return;
        }
        Item item = player.inventory.itemsBody.get(index);
        if (item.isNotNullItem()) {
            if (item.haveOption(93)) {
                Service.getInstance().sendThongBao(player, "Không thể bỏ vật phẩm ngày vào rương !");
                return;
            }
            if (index == 10) {
                Service.getInstance().sendPetFollow(player, (short) 0);
            }
            player.inventory.itemsBody.set(index, putItemBox(player, item));
            arrangeItems(player.inventory.itemsBag);
            sendItemBody(player);
            sendItemBox(player);
            Service.getInstance().Send_Caitrang(player);
            sendItemBody(player);
            Service.getInstance().point(player);
        }
    }

    //--------------------------------------------------------------------------
    public void subQuantityItemsBag(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBag, item, quantity);
    }

    public void subQuantityItemsBag(Player player, short itemId, int quantity) {
        for (Item item : player.inventory.itemsBag.stream()
                .filter(item -> item != null && item.isNotNullItem() && item.template.id == itemId)
                .collect(Collectors.toList())) {
            final int quantityItem = item.quantity;
            if (quantity > 0) {
                subQuantityItem(player.inventory.itemsBag, item, Math.min(quantity, quantityItem));
                quantity -= Math.min(quantity, quantityItem);
            }
            return;
        }
    }
    
     public void subQuantityItemsBag(Player player, int itemId, int quantity) {
        for (Item item : player.inventory.itemsBag.stream()
                .filter(item -> item != null && item.isNotNullItem() && item.template.id == itemId)
                .collect(Collectors.toList())) {
            final int quantityItem = item.quantity;
            if (quantity > 0) {
                subQuantityItem(player.inventory.itemsBag, item, Math.min(quantity, quantityItem));
                quantity -= Math.min(quantity, quantityItem);
            }
            return;
        }
    }

    public void subQuantityItemsBody(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBody, item, quantity);
    }

    public void subQuantityItem(List<Item> items, Item item, int quantity) {
        if (item != null) {
            for (Item it : items) {
//                if (it.isNotNullItem() && it.template.id == item.template.id) {
                if (item.equals(it)) {
                    it.quantity -= quantity;
                    if (it.quantity <= 0) {
                        removeItem(items, item);
                    }
                    break;
                }
            }
        }
    }

    public void sortItemBag(Player player) {
        sortItem(player.inventory.itemsBag);
    }

    public void sortItem(List<Item> items) {
        int index = 0;
        for (Item item : items) {
            if (item.isNotNullItem()) {
                items.set(index, item);
                index++;
            }
        }
        for (int i = index; i < items.size(); i++) {
            Item item = ItemService.gI().createItemNull();
            items.set(i, item);
        }
    }

    //--------------------------------------------------------------------------
    public void removeItem(List<Item> items, int index) {
        Item item = ItemService.gI().createItemNull();
        items.set(index, item);
    }

    public void subQuantityItemsBox(Player player, Item item, int quantity) {
        subQuantityItem(player.inventory.itemsBox, item, quantity);
    }

    public void removeItem(List<Item> items, Item item) {
        Item it = ItemService.gI().createItemNull();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).equals(item)) {
                items.set(i, it);
                break;
            }
        }
    }

    public void removeItemBag(Player player, int index) {
        removeItem(player.inventory.itemsBag, index);
    }

    public void removeItemBag(Player player, Item item) {
        removeItem(player.inventory.itemsBag, item);
    }

    public void removeItemBody(Player player, int index) {
        removeItem(player.inventory.itemsBody, index);
    }

    public void removeItemPetBody(Player player, int index) {
        removeItemBody(player.pet, index);
    }

    public void removeItemBox(Player player, int index) {
        removeItem(player.inventory.itemsBox, index);
    }

    public Item findItem(List<Item> list, int tempId) {
        for (Item item : list) {
            if (item.isNotNullItem() && item.template.id == tempId) {
                return item;
            }
        }
        return null;
    }

    /// item sự kiện
    public Item findVeTangNgoc(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2023)) {
                return item;
            }
        }
        return null;
    }

    public Item findBuaBaoVeNangCap(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 987) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuKeo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2013) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuBanh(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2014) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemnguyenlieuBingo(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 2015) && item.quantity >= 10) {
                return item;
            }
        }
        return null;
    }

    public Item finditemBongHoa(Player player, int soluong) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == 589) && item.quantity >= soluong) {
                return item;
            }
        }
        return null;
    }

    public boolean finditemWoodChest(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.id == 570) {
                return false;
            }
        }
        return true;
    }

    public Item finditemKeoGiangSinh(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.id == 2026) {
                return item;
            }
        }
        return null;
    }

    public Item findItemIndex(Player player, int id, int quantity) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (player.inventory.itemsBag.indexOf(item) == id) && item.quantity >= quantity) {
                return item;
            }
        }
        return null;
    }

    public Item findGodClothesIgnoreIndex(Player player, List<Integer> index) {
        for (Item item : player.inventory.itemsBag) {
            int itemIndex = player.inventory.itemsBag.indexOf(item);
            if (item.isNotNullItem()
                    && item.template.id >= 555 && item.template.id <= 567 && !index.contains(itemIndex)) {
                return item;
            }
        }
        return null;
    }

    public Item findItem(Player player, int id, int quantity) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id == id) && item.quantity >= quantity) {
                return item;
            }
        }
        return null;
    }

    public Item findItemBagByTemp(Player player, int tempId) {
        return findItem(player.inventory.itemsBag, tempId);
    }

    public List<Item> getListItem(Player player, int... items) {
        return player.inventory.itemsBag.stream().filter(i -> in(i, items)).collect(Collectors.toList());
    }

    private boolean in(Item item, int... items) {
        return IntStream.of(items).anyMatch(id -> (item.isNotNullItem() && item.template.id == id));
    }

    public Item findMealChangeDestroyClothes(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id >= 663 && item.template.id <= 667) && item.quantity >= 99) {
                return item;
            }
        }
        return null;
    }

    public Item findGodClothesByType(Player player, int type) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == type && (item.template.id >= 555 && item.template.id <= 567)) {
                return item;
            }
        }
        return null;
    }

    public Item findGodClothes(Player player) {
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && (item.template.id >= 555 && item.template.id <= 567)) {
                return item;
            }
        }
        return null;
    }

    //--------------------------------------------------------------------------
    public void sendItemBags(Player player) {
        arrangeItems(player.inventory.itemsBag);
        Message msg;
        try {
            msg = new Message(-36);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBag.size());
            for (int i = 0; i < player.inventory.itemsBag.size(); i++) {
                Item item = player.inventory.itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    continue;
                }
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                List<ItemOption> itemOptions = item.getDisplayOptions();
                msg.writer().writeByte(itemOptions.size()); //options
                for (ItemOption o : itemOptions) {
                    msg.writer().writeByte(o.optionTemplate.id);
                    msg.writer().writeShort(o.param);
                }
            }

            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemBody(Player player) {
        Message msg;
        try {
            msg = new Message(-37);
            msg.writer().writeByte(0);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeByte(player.inventory.itemsBody.size());
            for (Item item : player.inventory.itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeShort(itemOption.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (player.isPl()) {
            if (player.inventory.itemsBody.get(10).isNotNullItem()) {
                if (!player.isLinhThuFollow) {
                    player.isLinhThuFollow = true;
                    Service.getInstance().sendPetFollow(player, (short) (player.inventory.itemsBody.get(10).template.id));
                }
            } else {
                if (player.isLinhThuFollow) {
                    player.isLinhThuFollow = false;
                    Service.getInstance().sendPetFollow(player, (short) 0);
                }
            }
        }
        Service.getInstance().Send_Caitrang(player);
        Service.getInstance().sendFlagBag(player);
    }

    public void sendItemBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(0);
            msg.writer().writeByte(player.inventory.itemsBox.size());
            for (Item it : player.inventory.itemsBox) {
                msg.writer().writeShort(it.isNotNullItem() ? it.template.id : -1);
                if (it.isNotNullItem()) {
                    msg.writer().writeInt(it.quantity);
                    msg.writer().writeUTF(it.getInfo());
                    msg.writer().writeUTF(it.getContent());
                    List<ItemOption> itemOptions = it.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption io : itemOptions) {
                        msg.writer().writeByte(io.optionTemplate.id);
                        msg.writer().writeShort(io.param);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void openBox(Player player) {
        Message msg;
        try {
            msg = new Message(-35);
            msg.writer().writeByte(1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eatPea(Player player) {
        Item pea = null;
        for (Item item : player.inventory.itemsBag) {
            if (item.isNotNullItem() && item.template.type == 6) {
                pea = item;
                break;
            }
        }
        if (pea != null) {
            int hpKiHoiPhuc = 0;
            int lvPea = Integer.parseInt(pea.template.name.substring(13));
            for (ItemOption io : pea.itemOptions) {
                if (io.optionTemplate.id == 2) {
                    hpKiHoiPhuc = io.param * 1000;
                    break;
                }
                if (io.optionTemplate.id == 48) {
                    hpKiHoiPhuc = io.param;
                    break;
                }
            }
            player.nPoint.setHp(player.nPoint.hp + hpKiHoiPhuc);
            player.nPoint.setMp(player.nPoint.mp + hpKiHoiPhuc);
            PlayerService.gI().sendInfoHpMp(player);
            Service.getInstance().sendInfoPlayerEatPea(player);
            if (player.pet != null && player.zone.equals(player.pet.zone) && !player.pet.isDie()) {
                int statima = 100 * lvPea;
                player.pet.nPoint.stamina += statima;
                if (player.pet.nPoint.stamina > player.pet.nPoint.maxStamina) {
                    player.pet.nPoint.stamina = player.pet.nPoint.maxStamina;
                }
                player.pet.nPoint.setHp(player.pet.nPoint.hp + hpKiHoiPhuc);
                player.pet.nPoint.setMp(player.pet.nPoint.mp + hpKiHoiPhuc);
                Service.getInstance().sendInfoPlayerEatPea(player.pet);
                Service.getInstance().chatJustForMe(player, player.pet, "Cảm ơn sư phụ đã cho con đậu thần");
            }
            subQuantityItemsBag(player, pea, 1);
            sendItemBags(player);
        }
    }

    public int addPeaHarvest(Player player, byte level, int quantity) {
        Item pea = ItemService.gI().createNewItem(MagicTree.PEA_TEMP[level - 1], quantity);
        pea.itemOptions.add(new ItemOption(level - 1 > 1 ? 2 : 48, MagicTree.PEA_PARAM[level - 1]));
        addItemBag(player, pea, 99);
        if (pea.quantity > 0) {
            addItemBox(player, pea, 99);
        }
        if (pea.quantity < quantity) {
            Service.getInstance().sendThongBao(player, "Bạn vừa thu hoạch được " + (quantity - pea.quantity) + " hạt " + pea.template.name);
        }
        return pea.quantity;
    }

    public Item getPeaBox(Player player) {
        for (Item item : player.inventory.itemsBox) {
            if (item.isNotNullItem() && item.template.type == 6) {
                return item;
            }
        }
        return null;
    }

    private byte getNumPeaBag(Player player) {
        return getNumPea(player.inventory.itemsBag);
    }

    private byte getNumPeaBox(Player player) {
        return getNumPea(player.inventory.itemsBox);
    }

    private byte getNumPea(List<Item> items) {
        byte num = 0;
        for (Item item : items) {
            if (item.isNotNullItem() && item.template.type == 6) {
                num += item.quantity;
            }
        }
        return num;
    }

    public byte getCountEmptyBag(Player player) {
        return getCountEmptyListItem(player.inventory.itemsBag);
    }

    public byte getCountEmptyListItem(List<Item> list) {
        byte count = 0;
        for (Item item : list) {
            if (!item.isNotNullItem()) {
                count++;
            }
        }
        return count;
    }

    public boolean isminipet(int id) {
        switch (id) {
            case 936: //tloc
            case 892://tho xam
            case 893://tho trang
            case 942:
            case 943:
            case 944:
            case 967:
            case 1039:
            case 1040:
            case 1046:
            case 916:
            case 917:
            case 918:
            case 919:
            case 1188:
            case 1202:
            case 1203:
            case 1207:
            case 1213:
            case 1243:
            case 1244:
                return true;
        }
        return false;
    }

    public String itemsBagToString(Player player) {
        JSONArray dataBag = new JSONArray();
        for (Item item : player.inventory.itemsBag) {
            JSONObject dataItem = new JSONObject();
            if (item.isNotNullItem()) {
                JSONArray options = new JSONArray();
                dataItem.put("temp_id", item.template.id);
                dataItem.put("quantity", item.quantity);
                for (ItemOption io : item.itemOptions) {
                    JSONArray option = new JSONArray();
                    option.add(io.optionTemplate.id);
                    option.add(io.param);
                    options.add(option);
                }
                dataItem.put("option", options);
            } else {
                JSONArray options = new JSONArray();
                dataItem.put("temp_id", -1);
                dataItem.put("quantity", 0);
                dataItem.put("create_time", 0);
                dataItem.put("option", options);
            }
            dataBag.add(dataItem);
        }
        String itemsBag = dataBag.toJSONString();
        return itemsBag;
    }

    public Item findItemBagByIndex(Player player, int index) {
        if (player.inventory.itemsBag.get(index).isNotNullItem()) {
            return player.inventory.itemsBag.get(index);
        }
        return null;
    }

    public int getQuantity(Player player, int itemID) {
        Item item = findItem(player.inventory.itemsBag, itemID);
        if (item == null) {
            return -1;
        }
        return item.quantity;
    }

    public void addItemGiftCodeToPlayer(Player p, HashMap<Integer, HashMap<Integer, Integer>> Items) {

        Set<Integer> keySet = Items.keySet();
        String textGift = "Bạn vừa nhận được:\b";
        for (Integer key : keySet) {
            int idItem = key;
            int quantity = 0;
            int hsd = 0;
            // OPTION
            HashMap<Integer, Integer> ItemOption = Items.get(key);
            Set<Integer> keySetOption = ItemOption.keySet();
            for (Integer keyO : keySetOption) {
                quantity = keyO;
                hsd = ItemOption.get(keyO);
            }
            //
            if (idItem == -1) {
                p.inventory.gold = Math.min(p.inventory.gold + (long) quantity, 1L);
                textGift += quantity + " vàng\b";
            } else if (idItem == -2) {
                p.inventory.gem = Math.min(p.inventory.gem + quantity, 1);
                textGift += quantity + " ngọc\b";
            } else if (idItem == -3) {

            } else {
                Item itemGiftTemplate = ItemService.gI().createNewItem((short) idItem);
                if (itemGiftTemplate != null) {
                    Item itemGift = ItemService.gI().createNewItem((short) idItem);

                    if (itemGift.template.type == 5 || itemGift.template.type == 29
                            || itemGift.template.type == 27 || itemGift.template.type == 12
                            || itemGift.template.type == 14) {
                        itemGift.itemOptions.add(new ItemOption(30, 1));
                    }
                    itemGift.quantity = quantity;
                    addItemBag(p, itemGift, 999);
                    textGift += "x" + quantity + " " + itemGift.template.name + "\b";
                }
            }
        }
        sendItemBags(p);
        Service.getInstance().sendThongBaoOK(p, textGift);
    }
}
