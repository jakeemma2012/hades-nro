package nro.models.phuban.MabuBoss;

import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.server.ServerNotify;
import nro.services.MapService;
import nro.utils.TimeUtil;
import nro.utils.Util;

public class PhubanMabu {

    private static PhubanMabu i;

    public static long TIME_OPEN;
    public static long TIME_CLOSE;
    public static final byte HOUR_OPEN = 18;
    public static final byte MIN_OPEN = 0;
    public static final byte SECOND_OPEN = 0;
    public static final byte HOUR_CLOSE = 20;
    public static final byte MIN_CLOSE = 0;
    public static final byte SECOND_CLOSE = 0;

    private int day = -1;

    public static PhubanMabu gI() {
        if (i == null) {
            i = new PhubanMabu();
        }
        i.setTime();
        return i;
    }

    public void setTime() {
        if (i.day == -1 || i.day != TimeUtil.getCurrDay()) {
            i.day = TimeUtil.getCurrDay();
            try {
                this.TIME_OPEN = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_OPEN + ":" + MIN_OPEN + ":" + SECOND_OPEN, "dd/MM/yyyy HH:mm:ss");
                this.TIME_CLOSE = TimeUtil.getTime(TimeUtil.getTimeNow("dd/MM/yyyy") + " " + HOUR_CLOSE + ":" + MIN_CLOSE + ":" + SECOND_CLOSE, "dd/MM/yyyy HH:mm:ss");
            } catch (Exception e) {
            }
        }
    }

    public boolean isTimePhuBan() {
        long now = System.currentTimeMillis();
        return now > TIME_OPEN && now < TIME_CLOSE;
    }

    boolean SetMap;
    public boolean set = false;
    public boolean isAlive;

    public void update() {
        try {
            if (isTimePhuBan()) {
                if (!SetMap || set == true) {
                    Map rungbb = MapService.gI().getMapById(27);
                    Zone zone = rungbb.zones.get(Util.nextInt(0, rungbb.zones.size() - 1));

                    Boss f = BossFactory.createBoss(BossFactory.OMEGA_PLUS);
                    f.zone = zone;
                    f.changeStatus((byte) 1);

                    ServerNotify.gI().sendThongBaoBenDuoi("Chiến dịch đã bắt đầu , OMEGA đã xuất hiện tại Rừng Bamboo");
                    SetMap = true;
                    set = false;
                }
            } else {
                Boss omg = BossManager.gI().getBossById(BossFactory.OMEGA_PLUS);
                if (omg != null || omg != null &&  omg.zone != null) {
                    omg.die();
                    omg.leaveMap();
                    BossManager.gI().removeBoss(omg);
                }
                isAlive = false;
                SetMap = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
