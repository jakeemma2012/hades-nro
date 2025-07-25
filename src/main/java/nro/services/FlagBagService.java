package nro.services;

import nro.models.item.FlagBag;
import nro.models.player.Player;
import nro.server.Manager;
import nro.server.io.Message;

import java.util.ArrayList;
import java.util.List;
import nro.models.item.ItemOption;

/**
 *
 */
public class FlagBagService {

    private List<FlagBag> flagClan = new ArrayList<>();
    private static FlagBagService i;

    public static FlagBagService gI() {
        if (i == null) {
            i = new FlagBagService();
        }
        return i;
    }

    public void sendIconFlagChoose(Player player, int id) {
        FlagBag fb = getFlagBag(id);
        if (fb != null) {
            Message msg;
            try {
                msg = new Message(-62);
                msg.writer().writeByte(fb.id);
                msg.writer().writeByte(1);
                msg.writer().writeShort(fb.iconId);

                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void sendIconEffectFlag(Player player, int id) {
        FlagBag fb = getFlagBag(id);
        if (fb != null) {
            Message msg;
            try {
                msg = new Message(-63);
                msg.writer().writeByte(fb.id);
                msg.writer().writeByte(fb.iconEffect.length);
                for (Short iconId : fb.iconEffect) {
                    msg.writer().writeShort(iconId);
                }
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
            }
        }
    }

    public void sendListFlagClan(Player pl) {
        List<FlagBag> list = getFlagsForChooseClan();
        Message msg;
        try {
            msg = new Message(-46);
            msg.writer().writeByte(1); //type
            msg.writer().writeByte(list.size());
            for (FlagBag fb : list) {
                msg.writer().writeByte(fb.id);
                msg.writer().writeUTF(fb.name);
                msg.writer().writeInt(fb.gold);
                msg.writer().writeInt(fb.gem);
                StringBuilder strInfo = new StringBuilder();
                for (ItemOption itemOption : fb.itemoptions) {
                    strInfo.append(itemOption.getOptionString()).append(System.lineSeparator());
                }
                msg.writer().writeUTF(strInfo.toString());
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public FlagBag getFlagBag(int id) {
        for (FlagBag fb : Manager.FLAGS_BAGS) {
            if (fb.id == id) {
                return fb;
            }
        }
        return null;
    }

    public FlagBag getFlagBagByName(String name) {
        for (FlagBag fb : Manager.FLAGS_BAGS) {
            if (fb.name.equals(name)) {
                return fb;
            }
        }
        return null;
    }

    public List<FlagBag> getFlagsForChooseClan() {
        if (flagClan.isEmpty()) {
            int[] flagsId = {19, 22, 21, 20, 29};
            for (int i = 0; i < flagsId.length; i++) {
                flagClan.add(getFlagBag(flagsId[i]));
            }
        }
        return flagClan;
    }
}
