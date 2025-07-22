package nro.models.skill;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import nro.models.item.Item;
import nro.models.mob.Mob;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.EffectSkillService;
import nro.services.InventoryService;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.SkillUtil;
import nro.utils.Util;

public class SkillSpecial extends Skill {

    public static final int TIME_PREPARE = 2000;
    public static final int TIME_CLOSE_24_25 = 3000;
    private Player player;
    public Skill skillSpecial;

    public byte dir;

    public short _xPlayer;

    public short _yPlayer;

    public short _xObjTaget;

    public short _yObjTaget;

    public List<Player> playersTaget;

    public List<Mob> mobsTaget;

    public boolean isStartSkillSpecial;

    public byte stepSkillSpecial;

    public long lastTimeSkillSpecial;

    public int TypeBinhChua;

    public SkillSpecial(Player player) {
        this.player = player;
        this.playersTaget = new ArrayList<>();
        this.mobsTaget = new ArrayList<>();
    }

    private void update() {
        if (isStartSkillSpecial) {
            UpdateSkillSpecial(player);
        }
    }

    public void setSkillSpecial(byte dir, short _xPlayer, short _yPlayer) {
        this.skillSpecial = this.player.playerSkill.skillSelect;

        if (skillSpecial.currLevel < 1000) {
            skillSpecial.currLevel++;
            sendCurrLevelSpecial(player, skillSpecial);
        }
        if (skillSpecial.template.id == Skill.MAFUBA) {
            this.TypeBinhChua = InventoryService.gI().findItemBag(player, 1430) != null ? 1
                    : InventoryService.gI().findItemBag(player, 1431) != null ? 2 : 0;

        }
        this.dir = dir;
        this._xPlayer = _xPlayer;
        this._yPlayer = _yPlayer;
        this._xObjTaget = (short) (skillSpecial.dx + skillSpecial.point * 75); // LENGTH SKILL
        this._yObjTaget = (short) skillSpecial.dy;
        this.isStartSkillSpecial = true;
        this.stepSkillSpecial = 0;
        this.lastTimeSkillSpecial = System.currentTimeMillis();
        this.start(250); // DELAY TIME HIT 
    }

    public void closeSkillSpecial() {
        this.isStartSkillSpecial = false;
        this.stepSkillSpecial = 0;
        this.playersTaget.clear();
        this.mobsTaget.clear();
        this.close();
    }

    private Timer timer;
    private TimerTask timerTask;
    private boolean isActive = false;

    private void close() {
        try {
            this.isActive = false;
            this.timer.cancel();
            this.timerTask.cancel();
            this.timer = null;
            this.timerTask = null;
            SkillService.gI().affterUseSkill(player, this.skillSpecial.skillId);
        } catch (Exception e) {
            this.timer = null;
            this.timerTask = null;
        }
    }

    public void start(int leep) {
        if (this.isActive == false) {
            this.isActive = true;
            this.timer = new Timer();
            this.timerTask = new TimerTask() {
                @Override
                public void run() {
                    update();
                }
            };
            this.timer.schedule(timerTask, leep, leep);
        }
    }

    public void UpdateSkillSpecial(Player player) {
        try {
            if (player.isDie() || player.effectSkill.isHaveEffectSkill()) {
                player.skillSpecial.closeSkillSpecial();
                return;
            }
            if (player == null || player.zone == null || player.zone.map == null) {
                return;
            }
            if (player.skillSpecial.skillSpecial.template.id == Skill.MAFUBA) {
                if (player.skillSpecial.stepSkillSpecial == 0 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, TIME_PREPARE)) {
                    player.skillSpecial.stepSkillSpecial = 1;
                    player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                    for (Player playerMap : player.zone.getPlayers()) {
                        if (playerMap == null || playerMap.id == player.id) {
                            continue;
                        } //Lấy phạm vi player
                        if (player.skillSpecial.dir == -1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && SkillService.gI().canAttackPlayer(player, playerMap)) {
                            player.skillSpecial.playersTaget.add(playerMap);
                        } else if (player.skillSpecial.dir == 1 && !playerMap.isDie() && Util.getDistance(player, playerMap) <= 500 && SkillService.gI().canAttackPlayer(player, playerMap)) {
                            player.skillSpecial.playersTaget.add(playerMap);
                        }
                    }
                    for (Mob mobMap : player.zone.mobs) {
                        if (mobMap == null) {
                            continue;
                        } // Lấy phạm vi monster
                        if (player.skillSpecial.dir == -1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        } else if (player.skillSpecial.dir == 1 && !mobMap.isDie() && Util.getDistance(player, mobMap) <= 500) {
                            player.skillSpecial.mobsTaget.add(mobMap);
                        }
                    }
                    sendeffStartSkillNotfocus(player);
                    //Hiệu ứng nhốt vào bình player
                } else if (player.skillSpecial.stepSkillSpecial == 1 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, 3000)) {
                    for (Player playerMap : player.skillSpecial.playersTaget) {
                        EffectSkillService.gI().sendPlayerToCaiBinh(playerMap, 15000, TypeBinhChua);
                    }
//                    Hiệu ứng nhốt vào bình player
                    for (Mob mobMap : player.skillSpecial.mobsTaget) {
                        mobMap.effectSkill.setCaiBinhChua(System.currentTimeMillis(), 11000);
                        int binhChua = this.TypeBinhChua == 1 ? 11184
                                : this.TypeBinhChua == 2 ? 11166 : 11175;
                        EffectSkillService.gI().sendMobToCaiBinh(player, mobMap, 11000, binhChua);
//                        double damagePercentage = 0.1;
//                        int maxMobHP = player.nPoint.hpMax;
//                        int damage = (int) (maxMobHP * damagePercentage);
//                        new Thread(() -> {
//                            long startTime = System.currentTimeMillis();
//                            while (System.currentTimeMillis() - startTime <= 11000) {
//                                try {
//                                    Thread.sleep(2000);
//                                    if (player != null) {
//                                        mobMap.injured(player, damage, true);
//                                    }
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
                    }
                    player.skillSpecial.closeSkillSpecial();
                }
            } else if (player.skillSpecial.stepSkillSpecial == 0 && Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, TIME_PREPARE)) {
                player.skillSpecial.lastTimeSkillSpecial = System.currentTimeMillis();
                player.skillSpecial.stepSkillSpecial = 1;
                sendeffStartSkillNotfocus(player);
            } else if (player.skillSpecial.stepSkillSpecial == 1 && !Util.canDoWithTime(player.skillSpecial.lastTimeSkillSpecial, TIME_CLOSE_24_25)) {
                for (Player playerMap : player.zone.getHumanoids()) {
                    if (playerMap == null) {
                        continue;
                    }
                    if (player.skillSpecial.dir == -1 && !playerMap.isDie()
                            && playerMap.location.x <= player.location.x - 15
                            && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                            && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                            && SkillService.gI().canAttackPlayer(player, playerMap)) {
                        SkillService.gI().playerAttackPlayer(player, playerMap, false);
                    }
                    if (player.skillSpecial.dir == 1 && !playerMap.isDie()
                            && playerMap.location.x >= player.location.x + 15
                            && Math.abs(playerMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                            && Math.abs(playerMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget
                            && SkillService.gI().canAttackPlayer(player, playerMap)) {
                        SkillService.gI().playerAttackPlayer(player, playerMap, false);
                    }
                }
                for (Mob mobMap : player.zone.mobs) {
                    if (mobMap == null) {
                        continue;
                    }
                    if (player.skillSpecial.dir == -1 && !mobMap.isDie()
                            && mobMap.location.x <= player.skillSpecial._xPlayer - 15
                            && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                            && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                        SkillService.gI().playerAttackMob(player, mobMap, false, false);
                    }
                    if (player.skillSpecial.dir == 1 && !mobMap.isDie()
                            && mobMap.location.x >= player.skillSpecial._xPlayer + 15
                            && Math.abs(mobMap.location.x - player.skillSpecial._xPlayer) <= player.skillSpecial._xObjTaget
                            && Math.abs(mobMap.location.y - player.skillSpecial._yPlayer) <= player.skillSpecial._yObjTaget) {
                        SkillService.gI().playerAttackMob(player, mobMap, false, false);
                    }
                }

            } else if (player.skillSpecial.stepSkillSpecial == 1) {
                player.skillSpecial.closeSkillSpecial();
                SkillService.gI().setMpAffterUseSkill(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendeffStartSkillNotfocus(Player player) {
        int xObject = (player.gender == 1
                ? player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-75) : 75)
                : player.skillSpecial._xPlayer + ((player.skillSpecial.dir == -1) ? (-player.skillSpecial._xObjTaget) : player.skillSpecial._xObjTaget));
        int yObject = (player.gender == 0
                ? player.skillSpecial._xPlayer
                : player.skillSpecial._yPlayer);
        int idToLevelSachSKill = -1;
//                player.inventory.itemsBody.get(12) != null && player.inventory.itemsBody.get(12).isNotNullItem()
//                ? (player.inventory.itemsBody.get(12).template.id - 1422) : -1;
        switch (idToLevelSachSKill) {
            case -1:
                idToLevelSachSKill = 0;
                break;
            case 1:
            case 3:
            case 5:
                idToLevelSachSKill = 2;
                break;
            case 2:
            case 4:
            case 6:
                idToLevelSachSKill = 3;
                break;
        }
        SkillService.gI().sendEffStartSkillSpecial(player, (short) player.skillSpecial.skillSpecial.template.id,
                xObject, yObject, 3000, idToLevelSachSKill);

    }

    public void sendCurrLevelSpecial(Player player, Skill skill) {
        Message message = null;
        try {
            message = Service.getInstance().messageSubCommand((byte) 62);
            message.writer().writeShort(skill.skillId);
            message.writer().writeByte(0);
            message.writer().writeShort(skill.currLevel);
            player.sendMessage(message);
        } catch (final Exception ex) {
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }
        }
    }

    public void learSkillSpecial(Player player, byte skillID) {
        Message message = null;
        try {
            Skill curSkill = SkillUtil.createSkill(skillID, 1);
            SkillUtil.setSkill(player, curSkill);
            message = Service.getInstance().messageSubCommand((byte) 23);
            message.writer().writeShort(curSkill.skillId);
            player.sendMessage(message);
            message.cleanup();
        } catch (Exception e) {
            System.out.println("88888");
        } finally {
            if (message != null) {
                message.cleanup();
                message = null;
            }

        }
    }

    public boolean checkDoBenSachBeforUseSKill(Player player) {
        Item it = player.inventory.itemsBody.get(12);
        if (it != null && it.isNotNullItem()) {
            for (int i = 1; i < it.itemOptions.size(); i++) {
                if (it.itemOptions.get(i).optionTemplate.id == 235) {
                    if (it.itemOptions.get(i).param == 0) {
                        return false;
                    } else {
                        it.itemOptions.get(i).param -= 1;
                        InventoryService.gI().sendItemBody(player);
                        return true;
                    }
                }
            }
        }
        return true;
    }

    public void dispose() {
        this.player = null;
        this.skillSpecial = null;
    }
}
