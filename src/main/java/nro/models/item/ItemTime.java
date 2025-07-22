package nro.models.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 */
public class ItemTime {

    //id item text
    public static final byte DOANH_TRAI = 0;
    public static final byte BAN_DO_KHO_BAU = 1;

    public static final byte TOP_DAME_ATTACK_BOSS = 2;

    public static final int TIME_ITEM = 600_000;
    public static final int TIME_OPEN_POWER = 86400000;
    public static final int TIME_MAY_DO = 1800000;
    public static final int TIME_20P = 1200000;
    public static final int TIME_EAT_MEAL = 600000;

    private Player player;
    public boolean isDuoiKhi;

    public boolean isMaTroi;
    public long lastTimeMaTroi;
    public int iconMaTroi;

    public boolean rateDragonHit;
    public long lastTimerateHit;

    public boolean rateDame;
    public long lastTimeDameDr;

    public boolean rateHPKI;
    public long lastTimerateHPKI;

    public boolean isBanhTrungThu1Trung;
    public boolean isBanhTrungThu2Trung;

    public long lastTimeBanhTrungThu1Trung;
    public long lastTimeBanhTrungThu2Trung;

    public long lastTimeDuoiKhi;

    public boolean isUseBanhChung;
    public boolean isUseBanhTet;
    public long lastTimeBanhChung;
    public long lastTimeBanhTet;

    public boolean isUseMayDo;
    public long lastTimeUseMayDo;

    public boolean isMayDo;
    public long timeMayDo;

    public boolean isOpenPower;
    public long lastTimeOpenPower;

    public boolean isUseTDLT;
    public long lastTimeUseTDLT;
    public int timeTDLT;

    public boolean isEatMeal;
    public long lastTimeEatMeal;
    public int iconMeal;

    public boolean isUseDanhHieu1;
    public boolean isUseDanhHieu2;
    public boolean isUseDanhHieu3;

    public boolean Received50Ti;
    public boolean Received100Ti;
    public boolean Receive120Ti;

    public boolean isUseMayDoBongTai;
    public long lastTimeUseMaydoBongTai;

    public long lastTimeBuyBuaCOLD;
    public boolean isBuyBuaCOLD;

    public boolean isUseMaydoHuyDiet;
    public long lastTimeUseMayDoHuyDiet;

    public boolean isUseMaydoTinhThach;
    public long lastTimeUseMaydoTinhThach;


    public boolean isTimeYardart;
    public long lastTimeYarart;

    public long lastTimeNauCa;
    public boolean isNauca;


    public long lastTimeSuperZone;
    public boolean isSuperZone;

    public long lastTimeLinhLuc;
    public boolean isLinhLuc;


    public ItemTime(Player player) {
        this.player = player;
    }

//    public void update() {
//        boolean update = false;
//        if (isLinhLuc) {
//            if (Util.canDoWithTime(lastTimeLinhLuc, TIME_ITEM)) {
//                isLinhLuc = false;
//            }
//        }
//
//        if (isSuperZone) {
//            if (Util.canDoWithTime(lastTimeSuperZone, TIME_ITEM)) {
//                isSuperZone = false;
//            }
//        }
//        if (isX5) {
//            if (Util.canDoWithTime(lastTimeX5, TIME_20P)) {
//                isX5 = false;
//            }
//        }
//        if (isTimeYardart) {
//            if (Util.canDoWithTime(lastTimeYarart, TIME_MAY_DO)) {
//                isTimeYardart = false;
//            }
//        }
//        if (isUseBinhx3) {
//            if (Util.canDoWithTime(lastTimeUseBinhx3, TIME_ITEM)) {
//                isUseBinhx3 = false;
//            }
//        }
//        if (isUseBinhx2) {
//            if (Util.canDoWithTime(lastTimeUseBinhx2, TIME_ITEM)) {
//                isUseBinhx2 = false;
//            }
//        }
//        if (isUseMaydoTinhThach) {
//            if (Util.canDoWithTime(lastTimeUseMaydoTinhThach, TIME_MAY_DO)) {
//                isUseMaydoTinhThach = false;
//            }
//        }
//        if (isUseMaydoHuyDiet) {
//            if (Util.canDoWithTime(lastTimeUseMayDoHuyDiet, TIME_MAY_DO)) {
//                isUseMaydoHuyDiet = false;
//            }
//        }
//        if (isBuyBuaCOLD) {
//            if (Util.canDoWithTime(lastTimeBuyBuaCOLD, TIME_ITEM)) {
//                isBuyBuaCOLD = false;
//            }
//        }
//        if (isUseMaydoItemVip) {
//            if (Util.canDoWithTime(lastTimeUseMaydoItemVip, TIME_MAY_DO)) {
//                isUseMaydoItemVip = false;
//            }
//        }
//        if (rateDragonHit) {
//            if (Util.canDoWithTime(lastTimerateHit, TIME_MAY_DO)) {
//                rateDragonHit = false;
//                update = true;
//            }
//        }
//        if (rateDame) {
//            if (Util.canDoWithTime(lastTimeDameDr, TIME_MAY_DO)) {
//                rateDame = false;
//                update = true;
//            }
//        }
//        if (rateHPKI) {
//            if (Util.canDoWithTime(lastTimerateHPKI, TIME_MAY_DO)) {
//                rateHPKI = false;
//                update = true;
//            }
//        }
//        if (isBanhTrungThu1Trung) {
//            if (Util.canDoWithTime(lastTimeBanhTrungThu1Trung, TIME_ITEM)) {
//                isBanhTrungThu1Trung = false;
//                update = true;
//            }
//        }
//        if (isBanhTrungThu2Trung) {
//            if (Util.canDoWithTime(lastTimeBanhTrungThu2Trung, TIME_ITEM)) {
//                isBanhTrungThu2Trung = false;
//                update = true;
//            }
//        }
//        if (isDuoiKhi) {
//            if (Util.canDoWithTime(lastTimeDuoiKhi, TIME_MAY_DO)) {
//                isDuoiKhi = false;
//                update = true;
//            }
//        }
//        if (isEatMeal) {
//            if (Util.canDoWithTime(lastTimeEatMeal, TIME_EAT_MEAL)) {
//                isEatMeal = false;
//                update = true;
//            }
//        }
//        if (isUseBoHuyet) {
//            if (Util.canDoWithTime(lastTimeBoHuyet, TIME_ITEM)) {
//                isUseBoHuyet = false;
//                update = true;
//            }
//        }
//        if (isUseBoKhi) {
//            if (Util.canDoWithTime(lastTimeBoKhi, TIME_ITEM)) {
//                isUseBoKhi = false;
//                update = true;
//            }
//        }
//        if (isUseGiapXen) {
//            if (Util.canDoWithTime(lastTimeGiapXen, TIME_ITEM)) {
//                isUseGiapXen = false;
//            }
//        }
//        if (isUseCuongNo) {
//            if (Util.canDoWithTime(lastTimeCuongNo, TIME_ITEM)) {
//                isUseCuongNo = false;
//                update = true;
//            }
//        }
//        if (isUseAnDanh) {
//            if (Util.canDoWithTime(lastTimeAnDanh, TIME_ITEM)) {
//                isUseAnDanh = false;
//            }
//        }
//        if (isUseBanhChung) {
//            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
//                isUseBanhChung = false;
//            }
//        }
//        if (isUseBanhTet) {
//            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
//                isUseBanhTet = false;
//            }
//        }
//        if (isUseBoHuyet2) {
//            if (Util.canDoWithTime(lastTimeBoHuyet2, TIME_ITEM)) {
//                isUseBoHuyet2 = false;
//                update = true;
//            }
//        }
//        if (isUseBoKhi2) {
//            if (Util.canDoWithTime(lastTimeBoKhi2, TIME_ITEM)) {
//                isUseBoKhi2 = false;
//                update = true;
//            }
//        }
//        if (isUseGiapXen2) {
//            if (Util.canDoWithTime(lastTimeGiapXen2, TIME_ITEM)) {
//                isUseGiapXen2 = false;
//            }
//        }
//        if (isUseCuongNo2) {
//            if (Util.canDoWithTime(lastTimeCuongNo2, TIME_ITEM)) {
//                isUseCuongNo2 = false;
//                update = true;
//            }
//        }
//        if (isOpenPower) {
//            if (Util.canDoWithTime(lastTimeOpenPower, TIME_OPEN_POWER)) {
//                player.nPoint.limitPower++;
//                if (player.nPoint.limitPower > NPoint.MAX_LIMIT) {
//                    player.nPoint.limitPower = NPoint.MAX_LIMIT;
//                }
//                player.nPoint.initPowerLimit();
//                Service.getInstance().sendThongBao(player, "Giới hạn sức mạnh của bạn đã được tăng lên 1 bậc");
//                isOpenPower = false;
//            }
//        }
//        if (isUseMayDo) {
//            if (Util.canDoWithTime(lastTimeUseMayDo, TIME_MAY_DO)) {
//                isUseMayDo = false;
//            }
//        }
//        if (isMayDo) {
//            if (Util.canDoWithTime(timeMayDo, TIME_MAY_DO)) {
//                isMayDo = false;
//            }
//        }
//        if (isUseTDLT) {
//            if (Util.canDoWithTime(lastTimeUseTDLT, timeTDLT)) {
//                this.isUseTDLT = false;
//                ItemTimeService.gI().sendCanAutoPlay(this.player);
//            }
//        }
//        if (isUseBanhChung) {
//            if (Util.canDoWithTime(lastTimeBanhChung, TIME_ITEM)) {
//                isUseBanhChung = false;
//                update = true;
//            }
//        }
//        if (isUseBanhTet) {
//            if (Util.canDoWithTime(lastTimeBanhTet, TIME_ITEM)) {
//                isUseBanhTet = false;
//                update = true;
//            }
//        }
//        if (isMaTroi) {
//            if (Util.canDoWithTime(lastTimeMaTroi, TIME_ITEM)) {
//                isMaTroi = false;
//                Service.getInstance().Send_Caitrang(player);
//                update = false;
//            }
//        }
//        if (update) {
//            Service.getInstance().point(player);
//        }
//    }
    public void dispose() {
        this.player = null;
    }

    public final Map<Integer, Boolean> isActive = new HashMap<>();
    public final Map<Integer, Long> lastTimes = new HashMap<>();
    public static final long DEFAULT_DURATION = 1800000;

    public void update() {
        boolean update = false;

        for (Integer idItem : new ArrayList<>(isActive.keySet())) {
            if (isActive.get(idItem)) {
                if (lastTimes.get(idItem) > 0) {
                    lastTimes.put(idItem, lastTimes.get(idItem) - 1000);
                    if (lastTimes.get(idItem) <= 0) {
                        isActive.put(idItem, false);
                        update = true;
                    }
                }
            }
        }
        if (update) {
            Service.getInstance().point(player);
        }
    }

    public void setIsActive(int idItem, long Time) {
        isActive.put(idItem, true);
        lastTimes.put(idItem, Time);
    }

    public boolean setActiveTime(int idItem, int time, boolean value) {
        if (isActive(idItem)) {
            if (getLastTime(idItem) + (time * 60 * 1000) < Short.MAX_VALUE * 1000) {
                lastTimes.put(idItem, getLastTime(idItem) + (time * 60 * 1000));
                return true;
            } else {
                return false;
            }
        } else {
            isActive.put(idItem, value);
            lastTimes.put(idItem, (long) (time * 60 * 1000));
            return true;
        }
    }

    public long getLastTime(int idItem) {
        return lastTimes.getOrDefault(idItem, 0L);
    }

    public boolean isActive(int idItem) {
        return isActive.getOrDefault(idItem, false);
    }

}
