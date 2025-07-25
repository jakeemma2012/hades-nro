package nro.models.skill;

import nro.consts.Cmd;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import nro.models.mob.Mob;
import nro.services.PlayerService;
import nro.services.SkillService;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 */
public class PlayerSkill {

    public Timer timer;
    private Player player;
    public List<Skill> skills;
    public Skill skillSelect;
    public long lastTimePrepareTuSat;

    public PlayerSkill(Player player) {
        this.player = player;
        skills = new ArrayList<>();
        timer = new Timer();
    }

    public Skill getSkillbyId(int id) {
        for (Skill skill : skills) {
            if (skill.template.id == id) {
                return skill;
            }
        }
        return null;
    }

    public byte[] skillShortCut = new byte[10];

    public void sendSkillShortCut() {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 61);
            msg.writer().writeUTF("KSkill");
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
            msg = Service.getInstance().messageSubCommand((byte) 61);
            msg.writer().writeUTF("OSkill");
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendSkillShortCutNew() {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand(Cmd.CHANGE_ONSKILL);
            msg.writer().writeInt(skillShortCut.length);
            msg.writer().write(skillShortCut);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }
    public long lastTimeForturn;
    public boolean prepareQCKK;
    public boolean prepareTuSat;
    public boolean prepareLaze;
    public long lastTimeUseQCKK;
    public long lastTimeQCKK;
    public long lastTimeLaze;
    public long lastTimeTuSat;
    public Player plTarget;
    public Mob mobTarget;

    public byte getIndexSkillSelect() {
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.KAIOKEN:
            case Skill.LIEN_HOAN:
                return 1;
            case Skill.KAMEJOKO:
            case Skill.ANTOMIC:
            case Skill.MASENKO:
                return 2;
            default:
                return 3;
        }
    }

    public byte getSizeSkill() {
        byte size = 0;
        for (Skill skill : skills) {
            if (skill.skillId != -1) {
                size++;
            }
        }
        return size;
    }

    public void dispose() {
        this.player = null;
        this.skillSelect = null;
        this.skills = null;
    }

    public void update() {
        try {
            int time = 0;
            if (this.prepareTuSat || this.prepareLaze) {
                time = 2000;
            }
            if (System.currentTimeMillis() - player.playerSkill.lastTimeForturn >= time) {
                if (this.prepareTuSat) {
                    SkillService.gI().phatNo(player);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
