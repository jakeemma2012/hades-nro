package nro.services;

import nro.consts.ConstAchive;
import nro.consts.ConstPlayer;
import nro.models.intrinsic.Intrinsic;
import nro.models.map.Zone;
import nro.models.mob.Mob;
import nro.models.mob.MobMe;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.pvp.PVP;
import nro.models.skill.Hit;
import nro.models.skill.Skill;
import nro.server.io.Message;
import nro.services.func.PVPServcice;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.item.ItemOption;
import nro.services.func.RadaService;

/**
 *
 */
public class SkillService {

    private static SkillService i;

    private SkillService() {

    }

    public static SkillService gI() {
        if (i == null) {
            i = new SkillService();
        }
        return i;
    }

    public boolean useSkill(Player player, Player plTarget, Mob mobTarget) {
        try {
            if (player.isDie()) {
                Service.getInstance().charDie(player);
                return false;
            }

            if (player.playerSkill == null) {
                return false;
            }
            if (player.playerSkill.skillSelect.template.type == 2 && canUseSkillWithMana(player)
                    && canUseSkillWithCooldown(player)) {
                useSkillBuffToPlayer(player, plTarget);
                return true;
            }
            if (player.playerSkill.skillSelect.template.id == 10 || player.playerSkill.skillSelect.template.id == 11) {
                if (player.playerSkill.prepareQCKK) {
                    if (System.currentTimeMillis() - player.playerSkill.lastTimeForturn >= 1500) {
                        player.playerSkill.plTarget = player.playerSkill.plTarget != null ? player.playerSkill.plTarget
                                : plTarget;
                        player.playerSkill.mobTarget = player.playerSkill.mobTarget != null
                                ? player.playerSkill.mobTarget
                                : mobTarget;
                        // ném cầu
                        quaCauKenhKi(player, player.playerSkill.plTarget, mobTarget);
                    }
                } else if (player.playerSkill.prepareLaze) {
                    if (System.currentTimeMillis() - player.playerSkill.lastTimeForturn >= 2000) {
                        player.playerSkill.plTarget = player.playerSkill.plTarget != null ? player.playerSkill.plTarget
                                : plTarget;
                        player.playerSkill.mobTarget = player.playerSkill.mobTarget != null
                                ? player.playerSkill.mobTarget
                                : mobTarget;
                        // bắn laze
                        makankosapo(player, player.playerSkill.plTarget, mobTarget);
                    }
                }
            }
            if ((player.effectSkill.isHaveEffectSkill()
                    && (player.playerSkill.skillSelect.template.id != Skill.TU_SAT
                            && player.playerSkill.skillSelect.template.id != Skill.QUA_CAU_KENH_KHI
                            && player.playerSkill.skillSelect.template.id != Skill.MAKANKOSAPPO))
                    || (plTarget != null && !canAttackPlayer(player, plTarget))
                    || (mobTarget != null && mobTarget.isDie())
                    || !canUseSkillWithMana(player) || !canUseSkillWithCooldown(player)) {
                return false;
            }
            if (player.effectSkill.useTroi) {
                EffectSkillService.gI().removeUseTroi(player);
            }
            if (player.effectSkill.isCharging) {
                EffectSkillService.gI().stopCharge(player);
            }
            if (player.isPet) {
            }
            switch (player.playerSkill.skillSelect.template.type) {
                case 1:
                    useSkillAttack(player, plTarget, mobTarget);
                    break;
                case 3:
                    useSkillAlone(player);
                    break;
                default:
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void useSkillAttack(Player player, Player plTarget, Mob mobTarget) {
        if (!player.isBoss) {
            if (player.isPet) {
                if (player.nPoint.stamina > 0) {
                    if (((Pet) player).master.charms.tdDeTu < System.currentTimeMillis()) {
                        player.nPoint.numAttack++;
                        boolean haveCharmPet = ((Pet) player).master.charms.tdDeTu > System.currentTimeMillis();
                        if (haveCharmPet ? player.nPoint.numAttack >= 5 : player.nPoint.numAttack >= 2) {
                            player.nPoint.numAttack = 0;
                            player.nPoint.stamina--;
                        }
                    }
                } else {
                    ((Pet) player).askPea();
                    return;
                }
            } else {
                if (player.nPoint.stamina > 0) {
                    if (player.charms.tdDeoDai < System.currentTimeMillis()) {
                        player.nPoint.numAttack++;
                        if (player.nPoint.numAttack == 5) {
                            player.nPoint.numAttack = 0;
                            player.nPoint.stamina--;
                            PlayerService.gI().sendCurrentStamina(player);
                        }
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Thể lực đã cạn kiệt, hãy nghỉ ngơi để lấy lại sức");
                    return;
                }
            }
        }
        List<Mob> mobs;
        boolean miss = false;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.KAIOKEN: // kaioken
                long hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    break;
                } else {
                    player.nPoint.setHp(player.nPoint.mp - hpUse);
                    PlayerService.gI().sendInfoHpMpMoney(player);
                    Service.getInstance().Send_Info_NV(player);
                }
            case Skill.DRAGON:
            case Skill.DEMON:
            case Skill.GALICK:
            case Skill.LIEN_HOAN:
                if (plTarget != null && Util.getDistance(player, plTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
                if (mobTarget != null && Util.getDistance(player, mobTarget) > Skill.RANGE_ATTACK_CHIEU_DAM) {
                    miss = true;
                }
            case Skill.KAMEJOKO:
            case Skill.MASENKO:
            case Skill.ANTOMIC:
                if (plTarget != null) {
                    playerAttackPlayer(player, plTarget, miss);
                }
                if (mobTarget != null) {
                    playerAttackMob(player, mobTarget, miss, false);
                }
                if (player.mobMe != null) {
                    player.mobMe.attack(plTarget, mobTarget);
                }
                if (player.id >= 0 && !player.playerTask.achivements.isEmpty()) {
                    if (player.playerTask.achivements.size() > ConstAchive.NOI_CONG_CAO_CUONG) {
                        player.playerTask.achivements.get(ConstAchive.NOI_CONG_CAO_CUONG).count++;
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            // ******************************************************************
            case Skill.QUA_CAU_KENH_KHI:
                if (!player.playerSkill.prepareQCKK) {
                    // bắt đầu tụ quả cầu
                    player.playerSkill.prepareQCKK = !player.playerSkill.prepareQCKK;
                    player.playerSkill.lastTimeForturn = System.currentTimeMillis();
                    player.playerSkill.plTarget = plTarget;
                    // System.err.println("PL : " + plTarget != null ? plTarget.name : " CC " + "
                    // TARGET : " + player.playerSkill.plTarget != null ?
                    // player.playerSkill.plTarget.name : " NGU" );
                    player.playerSkill.mobTarget = mobTarget;
                    sendPlayerPrepareSkill(player, 2500);
                } else {
                    if (plTarget != null) {
                        player.playerSkill.plTarget = plTarget;
                    }
                    if (mobTarget != null) {
                        player.playerSkill.mobTarget = mobTarget;
                    }
                    // ném cầu
                    quaCauKenhKi(player, plTarget, mobTarget);
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (!player.playerSkill.prepareLaze) {
                    // bắt đầu nạp laze
                    player.playerSkill.prepareLaze = !player.playerSkill.prepareLaze;
                    player.playerSkill.lastTimeForturn = System.currentTimeMillis();
                    player.playerSkill.plTarget = plTarget;
                    player.playerSkill.mobTarget = mobTarget;
                    sendPlayerPrepareSkill(player, 3000);
                } else {
                    player.playerSkill.plTarget = plTarget;
                    player.playerSkill.mobTarget = mobTarget;
                }
                PlayerService.gI().sendInfoHpMpMoney(player);
                break;
            case Skill.SOCOLA:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.SOCOLA);
                int timeSocola = SkillUtil.getTimeSocola();
                if (plTarget != null) {
                    EffectSkillService.gI().setSocola(plTarget, System.currentTimeMillis(), timeSocola);
                    Service.getInstance().Send_Caitrang(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3780, timeSocola / 1000);
                }
                if (mobTarget != null) {
                    EffectSkillService.gI().sendMobToSocola(player, mobTarget, timeSocola);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                int timeChoangDCTT = SkillUtil.getTimeDCTT(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    Service.getInstance().setPos(player, plTarget.location.x, plTarget.location.y);
                    playerAttackPlayer(player, plTarget, miss);
                    EffectSkillService.gI().setBlindDCTT(plTarget, System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.BLIND_EFFECT);
                    PlayerService.gI().sendInfoHpMpMoney(plTarget);
                    ItemTimeService.gI().sendItemTime(plTarget, 3779, timeChoangDCTT / 1000);
                }
                if (mobTarget != null) {
                    Service.getInstance().setPos(player, mobTarget.location.x, mobTarget.location.y);
                    // mobTarget.attackMob(player, false, false);
                    playerAttackMob(player, mobTarget, false, false);
                    mobTarget.effectSkill.setStartBlindDCTT(System.currentTimeMillis(), timeChoangDCTT);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.BLIND_EFFECT);
                }
                player.nPoint.isCrit100 = true;
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.THOI_MIEN:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.THOI_MIEN);
                int timeSleep = SkillUtil.getTimeThoiMien(player.playerSkill.skillSelect.point);
                if (plTarget != null) {
                    EffectSkillService.gI().setThoiMien(plTarget, System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.SLEEP_EFFECT);
                    ItemTimeService.gI().sendItemTime(plTarget, 3782, timeSleep / 1000);
                }
                if (mobTarget != null) {
                    mobTarget.effectSkill.setThoiMien(System.currentTimeMillis(), timeSleep);
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.SLEEP_EFFECT);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TROI:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.TROI);
                int timeHold = SkillUtil.getTimeTroi(player.playerSkill.skillSelect.point);
                EffectSkillService.gI().setUseTroi(player, System.currentTimeMillis(), timeHold);
                if (plTarget != null && (!plTarget.playerSkill.prepareQCKK && !plTarget.playerSkill.prepareLaze
                        && !plTarget.playerSkill.prepareTuSat)) {
                    player.effectSkill.plAnTroi = plTarget;
                    EffectSkillService.gI().sendEffectPlayer(player, plTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.HOLD_EFFECT);
                    EffectSkillService.gI().setAnTroi(plTarget, player, System.currentTimeMillis(), timeHold);
                }
                if (mobTarget != null) {
                    player.effectSkill.mobAnTroi = mobTarget;
                    EffectSkillService.gI().sendEffectMob(player, mobTarget, EffectSkillService.TURN_ON_EFFECT,
                            EffectSkillService.HOLD_EFFECT);
                    mobTarget.effectSkill.setTroi(System.currentTimeMillis(), timeHold);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
        if (!player.isBoss) {
            player.effectSkin.lastTimeAttack = System.currentTimeMillis();
        }
    }

    private void useSkillAlone(Player player) {
        List<Mob> mobs;
        List<Player> players;
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.THAI_DUONG_HA_SAN:
                int timeStun = SkillUtil.getTimeStun(player.playerSkill.skillSelect.point);
                if (player.setClothes.thienXinHang2 == 5) {
                    timeStun *= 2;
                }
                mobs = new ArrayList<>();
                players = new ArrayList<>();
                if (player == null || player.zone == null) {
                    break;
                }
                if (!player.zone.map.isMapOffline) {
                    ReentrantLock lock = new ReentrantLock();
                    lock.lock();
                    try {
                        List<Player> playersMap = player.zone.getHumanoids();
                        for (Player pl : playersMap) {
                            if (pl != null && !player.equals(pl)) {
                                if (!pl.nPoint.khangTDHS) {
                                    int distance = Util.getDistance(player, pl);
                                    int rangeStun = SkillUtil.getRangeStun(player.playerSkill.skillSelect.point);
                                    if (distance <= rangeStun && canAttackPlayer(player, pl)) {
                                        if (player.isPet && ((Pet) player).master.equals(pl)) {
                                            continue;
                                        }
                                        EffectSkillService.gI().startStun(pl, System.currentTimeMillis(), timeStun);
                                        if (pl.typePk != ConstPlayer.NON_PK) {
                                            players.add(pl);
                                        }
                                    }
                                } else {
                                    Service.getInstance().chat(pl, "Lew Lew Lew đòi choáng");
                                }
                            }
                        }
                    } catch (Exception e) {
                        lock.unlock();
                    }
                }
                for (Mob mob : player.zone.mobs) {
                    if (Util.getDistance(player, mob) <= SkillUtil.getRangeStun(player.playerSkill.skillSelect.point)) {
                        mob.effectSkill.startStun(System.currentTimeMillis(), timeStun);
                        mobs.add(mob);
                    }
                }
                EffectSkillService.gI().sendEffectBlindThaiDuongHaSan(player, players, mobs, timeStun);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;

            case Skill.DE_TRUNG:
                EffectSkillService.gI().sendEffectUseSkill(player, Skill.DE_TRUNG);
                if (player.mobMe != null) {
                    player.mobMe.mobMeDie();
                }
                player.mobMe = new MobMe(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.SAYIAN_HOA:
            case Skill.SAYIAN_HOA_NM:
            case Skill.SAYIAN_HOA_XD:
                EffectSkillService.gI().sendEffectSaiYan(player);
                EffectSkillService.gI().setIsSaiYan(player);
                EffectSkillService.gI().sendEffectSaiYan(player);
                Service.getInstance().sendSpeedPlayer(player, 0);
                Service.getInstance().Send_Caitrang(player);
                Service.getInstance().sendSpeedPlayer(player, -1);
                PlayerService.gI().sendInfoHpMp(player);
                Service.getInstance().point(player);
                Service.getInstance().Send_Info_NV(player);
                ItemTimeService.gI().sendAllItemTime(player);
                Service.getInstance().sendInfoPlayerEatPea(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.BIEN_KHI:
                if (!player.itemTime.isActive(1491)) {
                    EffectSkillService.gI().sendEffectMonkey(player);
                    EffectSkillService.gI().setIsMonkey(player);
                    EffectSkillService.gI().sendEffectMonkey(player);

                    Service.getInstance().sendSpeedPlayer(player, 0);
                    Service.getInstance().Send_Caitrang(player);
                    Service.getInstance().sendSpeedPlayer(player, -1);
                    if (!player.isPet) {
                        PlayerService.gI().sendInfoHpMp(player);
                    }
                    Service.getInstance().point(player);
                    Service.getInstance().Send_Info_NV(player);
                    Service.getInstance().sendInfoPlayerEatPea(player);
                    affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                } else {
                    Service.getInstance().sendThongBao(player, "Bạn đang sử dụng thẻ Biến khỉ !");
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                EffectSkillService.gI().setStartShield(player);
                EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT,
                        EffectSkillService.SHIELD_EFFECT);
                ItemTimeService.gI().sendItemTime(player, 3784, player.effectSkill.timeShield / 1000);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.HUYT_SAO:
                int tileHP = SkillUtil.getPercentHPHuytSao(player.playerSkill.skillSelect.point);
                if (player.zone != null) {
                    if (!player.zone.map.isMapOffline) {
                        List<Player> playersMap = player.zone.getHumanoids();
                        for (Player pl : playersMap) {
                            if (pl != null && pl.effectSkill != null && pl.effectSkill.useTroi) {
                                continue;
                            }
                            if (pl != null && pl.effectSkill != null && pl.effectSkill.useTroi) {
                                EffectSkillService.gI().removeUseTroi(pl);
                            }
                            if (pl != null && pl.effectSkill != null) {
                                if (!pl.isBoss && pl.gender != ConstPlayer.NAMEC && player.cFlag == pl.cFlag) {
                                    EffectSkillService.gI().setStartHuytSao(pl, tileHP);
                                    EffectSkillService.gI().sendEffectPlayer(pl, pl, EffectSkillService.TURN_ON_EFFECT,
                                            EffectSkillService.HUYT_SAO_EFFECT);
                                    pl.nPoint.calPoint();
                                    pl.nPoint.setHp(pl.nPoint.hp + ((long) pl.nPoint.hp * tileHP / 100));
                                    Service.getInstance().point(pl);
                                    Service.getInstance().Send_Info_NV(pl);
                                    ItemTimeService.gI().sendItemTime(pl, 3781, 30);
                                    PlayerService.gI().sendInfoHpMp(pl);
                                }
                            }
                        }
                    } else {
                        EffectSkillService.gI().setStartHuytSao(player, tileHP);
                        EffectSkillService.gI().sendEffectPlayer(player, player, EffectSkillService.TURN_ON_EFFECT,
                                EffectSkillService.HUYT_SAO_EFFECT);
                        player.nPoint.calPoint();
                        player.nPoint.setHp(player.nPoint.hp + ((long) player.nPoint.hp * tileHP / 100));
                        Service.getInstance().point(player);
                        Service.getInstance().Send_Info_NV(player);
                        ItemTimeService.gI().sendItemTime(player, 3781, 30);
                        PlayerService.gI().sendInfoHpMp(player);
                    }
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;

            case Skill.TAI_TAO_NANG_LUONG:
                EffectSkillService.gI().startCharge(player);
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
            case Skill.TU_SAT:
                if (!player.playerSkill.prepareTuSat) {
                    // gồng tự sát
                    player.playerSkill.prepareTuSat = true;
                    player.playerSkill.lastTimeTuSat = System.currentTimeMillis();
                    sendPlayerPrepareBom(player, 2000);
                } else {

                }
                break;
        }
        if (player.playerTask.achivements.size() > 0) {
            player.playerTask.achivements.get(ConstAchive.KY_NANG_THANH_THAO).count++;
        }
    }

    private void useSkillBuffToPlayer(Player player, Player plTarget) {
        switch (player.playerSkill.skillSelect.template.id) {
            case Skill.TRI_THUONG:
                List<Player> players = new ArrayList();
                int percentTriThuong = SkillUtil.getPercentTriThuong(player.playerSkill.skillSelect.point);
                if (canHsPlayer(player, plTarget)) {
                    players.add(plTarget);
                    List<Player> playersMap = player.zone.getNotBosses();
                    for (int i = playersMap.size() - 1; i >= 0; i--) {
                        Player pl = playersMap.get(i);
                        if (!pl.equals(plTarget)) {
                            if (canHsPlayer(player, plTarget) && Util.getDistance(player, pl) <= 300) {
                                players.add(pl);
                            }
                        }
                    }
                    playerAttackPlayer(player, plTarget, false);
                    for (Player pl : players) {
                        boolean isDie = pl.isDie();
                        long hpHoi = pl.nPoint.hpMax * percentTriThuong / 100;
                        long mpHoi = pl.nPoint.mpMax * percentTriThuong / 100;
                        pl.nPoint.addHp(hpHoi);
                        pl.nPoint.addMp(mpHoi);
                        if (isDie) {
                            Service.getInstance().hsChar(pl, hpHoi, mpHoi);
                            PlayerService.gI().sendInfoHpMp(pl);
                        } else {
                            Service.getInstance().Send_Info_NV(pl);
                            PlayerService.gI().sendInfoHpMp(pl);
                        }
                    }
                    long hpHoiMe = player.nPoint.hp * percentTriThuong / 100;
                    player.nPoint.addHp(hpHoiMe);
                    PlayerService.gI().sendInfoHp(player);
                }
                affterUseSkill(player, player.playerSkill.skillSelect.template.id);
                break;
        }
    }

    private void phanSatThuong(Player plAtt, Player plTarget, long dame) {
        int percentPST = plTarget.nPoint.tlPST;
        if (percentPST != 0) {
            long damePST = dame * percentPST / 100;
            Message msg;
            try {
                msg = new Message(56);
                msg.writer().writeInt((int) plAtt.id);
                if (damePST >= plAtt.nPoint.hp) {
                    damePST = plAtt.nPoint.hp - 1;
                }
                damePST = plAtt.injured(null, damePST, true, false);
                msg.writer().writeLong(plAtt.nPoint.hp);
                msg.writer().writeLong(damePST);
                msg.writer().writeBoolean(false);
                msg.writer().writeByte(36);
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hutHPMP(Player player, long dame, boolean attackMob) {
        int tiLeHutHp = player.nPoint.getTileHutHp(attackMob);
        int tiLeHutMp = player.nPoint.getTiLeHutMp();
        long hpHoi = dame * tiLeHutHp / 100;
        long mpHoi = dame * tiLeHutMp / 100;
        if (hpHoi > 0 || mpHoi > 0) {
            PlayerService.gI().hoiPhuc(player, hpHoi, mpHoi);
        }
    }

    int getRatioPhanTach(int point) {
        switch (point) {
            case 1:
                return 5;
            case 2:
                return 7;
            case 3:
                return 13;
            case 4:
                return 16;
            case 5:
                return 20;
            default:
                return 0;
        }
    }

    public void playerAttackPlayer(Player plAtt, Player plInjure, boolean miss) {
        if (plInjure.effectSkill.anTroi) {
            plAtt.nPoint.isCrit100 = true;
        }
        if (plInjure.fusion.typeFusion != 0) {
            if (plAtt.nPoint.lvPhantachTinhThan > 0) {
                if (Util.isTrue(getRatioPhanTach(plAtt.nPoint.lvPhantachTinhThan), 100)) {
                    if (Util.canDoWithTime(plAtt.lastTimePhanTach, 30000)) {
                        plInjure.pet.unFusion();
                        plAtt.lastTimePhanTach = System.currentTimeMillis();
                    }
                }
            }
        }
        if (plAtt.effectSkin.isXinbato) {
            miss = true;
        }
        long dameHit = plInjure.injured(plAtt, miss ? 0 : plAtt.nPoint.getDameAttack(false, true), false, false);
        phanSatThuong(plAtt, plInjure, dameHit);
        hutHPMP(plAtt, dameHit, false);
        Message msg;
        try {
            msg = new Message(-60);
            msg.writer().writeInt((int) plAtt.id); // id pem
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); // skill pem
            msg.writer().writeByte(1); // số người pem
            msg.writer().writeInt((int) plInjure.id); // id ăn pem
            byte typeSkill = SkillUtil.getTyleSkillAttack(plAtt.playerSkill.skillSelect);
            msg.writer().writeByte(typeSkill == 2 ? 0 : 1); // read continue
            msg.writer().writeByte(typeSkill); // type skill
            msg.writer().writeLong(dameHit); // dame ăn
            msg.writer().writeBoolean(plInjure.isDie()); // is die
            msg.writer().writeBoolean(plAtt.nPoint.isCrit); // crit
            if (typeSkill != 1) {
                Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
                msg.cleanup();
            } else {
                plInjure.sendMessage(msg);
                msg.cleanup();
                msg = new Message(-60);
                msg.writer().writeInt((int) plAtt.id); // id pem
                msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId); // skill pem
                msg.writer().writeByte(1); // số người pem
                msg.writer().writeInt((int) plInjure.id); // id ăn pem
                msg.writer().writeByte(typeSkill == 2 ? 0 : 1); // read continue
                msg.writer().writeByte(0); // type skill
                msg.writer().writeLong(dameHit); // dame ăn
                msg.writer().writeBoolean(plInjure.isDie()); // is die
                msg.writer().writeBoolean(plAtt.nPoint.isCrit); // crit
                Service.getInstance().sendMessAnotherNotMeInMap(plInjure, msg);
                msg.cleanup();
            }
            Service.getInstance().addSMTN(plInjure, (byte) 2, 1, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playerAttackMob(Player plAtt, Mob mob, boolean miss, boolean dieWhenHpFull) {
        if (!mob.isDie()) {
            if (plAtt.effectSkin.isVoHinh) {
                plAtt.effectSkin.isVoHinh = false;
            }
            long dameHit = plAtt.nPoint.getDameAttack(true, false);
            if ((plAtt.charms.tdBatTu > System.currentTimeMillis() || plAtt.itemTime.isMaTroi)
                    && plAtt.nPoint.hp == 1) {
                dameHit = 0;
            }
            if (plAtt.charms.tdManhMe > System.currentTimeMillis()) {
                dameHit += (dameHit * 150 / 100);
            }
            if (plAtt.isPet) {
                if (((Pet) plAtt).charms.tdDeTu > System.currentTimeMillis()) {
                    dameHit *= 2;
                }
            }
            if (miss || plAtt.effectSkin.isXinbato) {
                dameHit = 0;
            }
            hutHPMP(plAtt, dameHit, true);
            sendPlayerAttackMob(plAtt, mob);
            mob.injured(plAtt, dameHit, dieWhenHpFull);
        }
    }

    private void sendPlayerPrepareSkill(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(4);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPlayerPrepareBom(Player player, int affterMiliseconds) {
        Message msg;
        try {
            msg = new Message(-45);
            msg.writer().writeByte(7);
            msg.writer().writeInt((int) player.id);
            // msg.writer().writeShort(player.playerSkill.skillSelect.skillId);
            msg.writer().writeShort(104);
            msg.writer().writeShort(affterMiliseconds);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canUseSkillWithMana(Player player) {
        if (player.playerSkill.skillSelect != null) {
            if (player.playerSkill.skillSelect.template.id == Skill.KAIOKEN) {
                long hpUse = player.nPoint.hpMax / 100 * 10;
                if (player.nPoint.hp <= hpUse) {
                    return false;
                }
            }
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 1:
                    long mpUse = (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        return true;
                    } else {
                        return false;
                    }
                case 2:
                    if (player.nPoint.mp > 0) {
                        return true;
                    } else {
                        return false;
                    }
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean canUseSkillWithCooldown(Player player) {
        return Util.canDoWithTime(player.playerSkill.skillSelect.lastTimeUseThisSkill,
                player.playerSkill.skillSelect.coolDown - 50);
    }

    public void affterUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        switch (skillId) {
            case Skill.DICH_CHUYEN_TUC_THOI:
                if (intrinsic.id == 6) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.THOI_MIEN:
                if (intrinsic.id == 7) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.SOCOLA:
                if (intrinsic.id == 14) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
            case Skill.TROI:
                if (intrinsic.id == 22) {
                    player.nPoint.dameAfter = intrinsic.param1;
                }
                break;
        }
        setMpAffterUseSkill(player);
        setLastTimeUseSkill(player, skillId);
    }

    public void setMpAffterUseSkill(Player player) {
        if (player.playerSkill.skillSelect != null) {
            switch (player.playerSkill.skillSelect.template.manaUseType) {
                case 0:
                    if (player.nPoint.mp >= player.playerSkill.skillSelect.manaUse) {
                        player.nPoint.setMp(player.nPoint.mp - player.playerSkill.skillSelect.manaUse);
                    }
                    break;
                case 1:
                    long mpUse = (player.nPoint.mpMax * player.playerSkill.skillSelect.manaUse / 100);
                    if (player.nPoint.mp >= mpUse) {
                        player.nPoint.setMp(player.nPoint.mp - mpUse);
                    }
                    break;
                case 2:
                    player.nPoint.setMp(0);
                    break;
            }
            PlayerService.gI().sendInfoHpMpMoney(player);
        }
    }

    private void setLastTimeUseSkill(Player player, int skillId) {
        Intrinsic intrinsic = player.playerIntrinsic.intrinsic;
        int subTimeParam = 0;
        switch (skillId) {
            case Skill.TRI_THUONG:
                if (intrinsic.id == 10) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.THAI_DUONG_HA_SAN:
                if (intrinsic.id == 3) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.QUA_CAU_KENH_KHI:
                if (intrinsic.id == 4) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.KHIEN_NANG_LUONG:
                if (intrinsic.id == 5 || intrinsic.id == 15 || intrinsic.id == 20) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.MAKANKOSAPPO:
                if (intrinsic.id == 11) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.DE_TRUNG:
                if (intrinsic.id == 12) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.TU_SAT:
                if (intrinsic.id == 19) {
                    subTimeParam = intrinsic.param1;
                }
                break;
            case Skill.HUYT_SAO:
                if (intrinsic.id == 21) {
                    subTimeParam = intrinsic.param1;
                }
                break;
        }
        int coolDown = player.playerSkill.skillSelect.coolDown;
        player.playerSkill.skillSelect.lastTimeUseThisSkill = System.currentTimeMillis()
                - (coolDown * subTimeParam / 100);

        if (subTimeParam != 0) {
            Service.getInstance().sendTimeSkill(player);
        }
    }

    private boolean canHsPlayer(Player player, Player plTarget) {
        if (plTarget == null) {
            return false;
        }
        if (plTarget.isBoss) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_ALL) {
            return false;
        }
        if (plTarget.typePk == ConstPlayer.PK_PVP) {
            return false;
        }
        if (player.cFlag != 0) {
            if (plTarget.cFlag != 0 && plTarget.cFlag != player.cFlag) {
                return false;
            }
        } else if (plTarget.cFlag != 0) {
            return false;
        }
        return true;
    }

    public boolean canAttackPlayer(Player pl1, Player pl2) {
        if (pl2 != null && !pl1.isDie() && !pl2.isDie()) {
            if (pl1.typePk > 0 || pl2.typePk > 0) {
                return true;
            }
            if ((pl1.cFlag != 0 && pl2.cFlag != 0)
                    && (pl1.cFlag == 8 || pl2.cFlag == 8 || pl1.cFlag != pl2.cFlag)) {
                return true;
            }
            PVP pvp = PVPServcice.gI().findPvp(pl1);
            if (pvp != null) {
                if ((pvp.player1.equals(pl1) && pvp.player2.equals(pl2)
                        || (pvp.player1.equals(pl2) && pvp.player2.equals(pl1)))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private void sendPlayerAttackMob(Player plAtt, Mob mob) {
        Message msg;
        try {
            msg = new Message(54);
            msg.writer().writeInt((int) plAtt.id);
            msg.writer().writeByte(plAtt.playerSkill.skillSelect.skillId);
            msg.writer().writeByte(mob.id);
            Service.getInstance().sendMessAllPlayerInMap(plAtt, msg);
            msg.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selectSkill(Player player, int skillId) {
        Skill skillBefore = player.playerSkill.skillSelect;
        for (Skill skill : player.playerSkill.skills) {
            if (skill.skillId != -1 && skill.template.id == skillId) {
                player.playerSkill.skillSelect = skill;
                switch (skillBefore.template.id) {
                    case Skill.DRAGON:
                    case Skill.KAMEJOKO:
                    case Skill.DEMON:
                    case Skill.MASENKO:
                    case Skill.LIEN_HOAN:
                    case Skill.GALICK:
                    case Skill.ANTOMIC:
                        switch (skill.template.id) {
                            case Skill.KAMEJOKO:
                            case Skill.DRAGON:
                            case Skill.DEMON:
                            case Skill.MASENKO:
                            case Skill.LIEN_HOAN:
                            case Skill.GALICK:
                            case Skill.ANTOMIC:
                                // skill.lastTimeUseThisSkill = System.currentTimeMillis() + (skill.coolDown /
                                // 2);
                                break;
                        }
                        break;
                }
                break;
            }
        }
    }

    public void PrePareSkillNotFocus(Player player, byte dir) {
        if (player.isPl()) {
            int idToLevelSachSKill = 0;
            // player.inventory.itemsBody.get(12) != null
            // && player.inventory.itemsBody.get(12).isNotNullItem()
            // ? (player.inventory.itemsBody.get(12).template.id - 1422)
            // : 0;
            switch (idToLevelSachSKill) {
                case 0:
                    sendEffPrePareSkilLSpecial(player, (short) player.skillSpecial.skillSpecial.template.id, dir, 2000,
                            (byte) 0, 0);
                    break;
                case 1:
                case 3:
                case 5:
                    sendEffPrePareSkilLSpecial(player, (short) player.skillSpecial.skillSpecial.template.id, dir, 2000,
                            (byte) 0, 2);
                    break;
                case 2:
                case 4:
                case 6:
                    sendEffPrePareSkilLSpecial(player, (short) player.skillSpecial.skillSpecial.template.id, dir, 2000,
                            (byte) 0, 3);
                    break;
                default:
                    sendEffPrePareSkilLSpecial(player, (short) player.skillSpecial.skillSpecial.template.id, dir, 2000,
                            (byte) 0, 0);
                    break;
            }
        }
    }

    private void sendEffPrePareSkilLSpecial(Player player, short skillID, byte dir, int timePre, byte isFly,
            int TypeLevelSkill) {
        try {
            Message m = new Message(-45);
            DataOutputStream ds = m.writer();
            ds.writeByte(20);
            ds.writeInt((int) player.id);
            ds.writeShort(skillID);
            ds.writeByte((player.gender == 0 ? 1 : player.gender == 1 ? 3 : 2));// Prepare type
            ds.writeByte(dir);
            ds.writeShort(timePre);
            ds.writeByte(isFly);// isfly - 0 stand
            ds.writeByte(TypeLevelSkill);// typepaint
            ds.writeByte(0);// typeItem
            ds.flush();
            Service.getInstance().sendMessAllPlayerInMap(player.zone, m);
            m.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendEffStartSkillSpecial(Player player, short skillID, int xPlayerToObject,
            int yPlayerToObject, int time, int TypePaintSkill) {
        Message m = new Message(-45);
        DataOutputStream ds = m.writer();
        try {
            ds.writeByte(21);
            ds.writeInt((int) player.id);
            ds.writeShort(skillID);
            ds.writeShort(xPlayerToObject);
            ds.writeShort(yPlayerToObject);
            ds.writeShort(time);
            ds.writeShort((player.gender == 2 ? 25 : player.skillSpecial._yObjTaget));
            if (player.gender == 1) {
                ds.writeByte(TypePaintSkill);
                final byte size = (byte) (player.skillSpecial.playersTaget.size()
                        + player.skillSpecial.mobsTaget.size());
                ds.writeByte(size);
                for (Player playerMap : player.skillSpecial.playersTaget) {
                    ds.writeByte(1);
                    ds.writeInt((int) playerMap.id);
                }
                for (Mob mobMap : player.skillSpecial.mobsTaget) {
                    ds.writeByte(0);
                    ds.writeByte(mobMap.id);
                }
                ds.writeByte(player.skillSpecial.TypeBinhChua); // ảnh bình để hút vào : 0 = defaule ; 1 = ảnh cạnh ảnh
                // 0; 2 = ảnh cạnh ảnh 1
            } else {
                ds.writeByte(TypePaintSkill); // skill tung ra : 0 = skill mặc định
                ds.writeByte(TypePaintSkill); // skill kết : 0 = skill mặc định
                ds.writeByte(TypePaintSkill); // skill kết : 0 = skill mặc định
            }
            Service.getInstance().sendMessAllPlayerInMap(player.zone, m);
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quaCauKenhKi(Player player, Player plTarget, Mob mobTarget) {
        player.playerSkill.prepareQCKK = false;
        ArrayList<Mob> mobs = new ArrayList<>();
        if (player.playerSkill.plTarget != null) {
            playerAttackPlayer(player, player.playerSkill.plTarget, false);
            for (Mob mob : player.zone.mobs) {
                if (!mob.isDie()
                        && Util.getDistance(plTarget, mob) <= SkillUtil
                                .getRangeQCKK(player.playerSkill.skillSelect.point)) {
                    mobs.add(mob);
                }
            }
        }

        if (player.playerSkill.mobTarget != null) {
            playerAttackMob(player, player.playerSkill.mobTarget, false, true);
            for (Mob mob : player.zone.mobs) {
                if (!mob.equals(mobTarget) && !mob.isDie()
                        && Util.getDistance(mob, mobTarget) <= SkillUtil
                                .getRangeQCKK(player.playerSkill.skillSelect.point)) {
                    mobs.add(mob);
                }
            }
        }
        // for (Mob mob : mobs) {
        // // mob.injured(player, player.point.getDameAttack(), true);
        // }
        player.playerSkill.plTarget = null;
        player.playerSkill.mobTarget = null;
        PlayerService.gI().sendInfoHpMpMoney(player);
        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
    }

    public void makankosapo(Player player, Player plTarget, Mob mobTarget) {
        player.playerSkill.prepareLaze = false;
        if (plTarget != null) {
            playerAttackPlayer(player, plTarget, false);
        }
        if (mobTarget != null) {
            playerAttackMob(player, mobTarget, false, true);
            // mobTarget.attackMob(player, false, true);
        }
        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
    }

    public void phatNo(Player player) {
        // nổ
        player.playerSkill.prepareTuSat = false;
        int rangeBom = SkillUtil.getRangeBom(player.playerSkill.skillSelect.point);
        long dame = player.nPoint.hpMax;
        // if (player.isPl()&& player.inventory.itemsBody.get(5) != null &&
        // player.inventory.itemsBody.get(5).isNotNullItem()) {
        // int perCentDame = 0;
        // for (ItemOption io : player.inventory.itemsBody.get(5).itemOptions) {
        // if (io.optionTemplate.id == 215) {
        // perCentDame = io.param;
        // break;
        // }
        // }
        // dame += player.nPoint.calPercent(dame, perCentDame);
        // }
        // if (player.effectSkill.isMonkey) {
        // int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
        // dame -= percent * (dame / 100);
        // }
        for (Mob mob : player.zone.mobs) {
            if (Util.getDistance(player, mob) <= rangeBom) {
                mob.injured(player, dame, true);
            }
        }
        List<Player> playersMap = null;
        if (player.isBoss) {
            playersMap = player.zone.getNotBosses();
        } else {
            playersMap = player.zone.getHumanoids();
        }
        if (!player.zone.map.isMapOffline) {
            for (Player pl : playersMap) {
                if (pl != null && !player.equals(pl) && canAttackPlayer(player, pl)
                        && Util.getDistance(player, pl) <= rangeBom) {
                    long _dame = 0;
                    if (player.effectSkill.isMonkey) {
                        _dame = (pl.isBoss ? dame / 2 : dame);
                    } else {
                        _dame = (long) (pl.isBoss ? dame / 2 : dame);
                    }

                    ////////////////////////////////////////////////////////////////
                    // _dame = _dame + player.nPoint.calPercent(_dame, player.nPoint.satThuongBom);
                    // _dame = Math.min(_dame, Integer.MAX_VALUE - 1);r
                    ////////////////////////////////////////////////////////////////
                    pl.injured(player, _dame, false, false);
                    PlayerService.gI().sendInfoHpMpMoney(pl);
                    Service.getInstance().Send_Info_NV(pl);
                }
            }
        }
        affterUseSkill(player, player.playerSkill.skillSelect.template.id);
        player.injured(null, 2100000000, true, false);
    }
}
