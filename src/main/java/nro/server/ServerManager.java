package nro.server;

import nro.attr.AttributeManager;
import nro.jdbc.DBService;
import nro.jdbc.daos.AccountDAO;
import nro.jdbc.daos.HistoryTransactionDAO;
import nro.jdbc.daos.PlayerDAO;
import nro.login.LoginSession;
import nro.manager.TopManager;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.dungeon.DungeonManager;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Player;
import nro.server.io.Session;
import nro.services.ClanService;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import nro.manager.*;
import nro.models.minigames.KeoBuaBaoManager;
import nro.models.minigames.RandomDiemDanh;
import nro.utils.Panel_Reload;
import nro.utils.Panel_Reward;

public class ServerManager {

    public static String timeStart;

    public static final Map CLIENTS = new HashMap();

    public static String NAME = "";
    public static int PORT = 14445;

    private Controller controller;

    private static ServerManager instance;

    public static ServerSocket listenSocket;
    public static boolean isRunning;

    private TranhNgocManager tranhNgocManager;
    private SieuHangControl sieuHangControl;
    private KeoBuaBaoManager keobuabao;

    @Getter
    private LoginSession login;
    public static boolean updateTimeLogin;
    @Getter
    @Setter
    private AttributeManager attributeManager;
    private long lastUpdateAttribute;
    @Getter
    private DungeonManager dungeonManager;

    public SieuHangControl getSieuHangController() {
        return this.sieuHangControl;
    }

    public void init() {
        Manager.gI();
        // PlayerDAO.resetManhKinhmoiNgay();
        HistoryTransactionDAO.deleteHistory();
        BossFactory.initBoss();
        this.controller = new Controller();
        if (updateTimeLogin) {
            AccountDAO.updateLastTimeLoginAllAccount();
        }
    }

    public TranhNgocManager getTranhNgocManager() {
        return this.tranhNgocManager;
    }

    public static ServerManager gI() {
        if (instance == null) {
            instance = new ServerManager();
            instance.init();
        }
        return instance;
    }

    public static void main(String[] args) {
        timeStart = TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss");
        ServerManager.gI().run();
    }

    public void run() {
        isRunning = true;
        activeCommandLine();
        activeGame();
        // new Thread(TopCoin.getInstance(), "Update Top Coin").start();
        activeLogin();
        autoTask();
        RandomDiemDanh.gI().lastTimeEnd = System.currentTimeMillis() + 30000;
        // (new AutoMaintenance(23, 0, 0)).start();
        activeServerSocket();
    }

    public void activeLogin() {
        login = new LoginSession();
        login.connect(Manager.loginHost, Manager.loginPort);
    }

    private void activeServerSocket() {
        try {
            Log.log("Start server......... Current thread: " + Thread.activeCount());
            listenSocket = new ServerSocket(PORT);
            while (isRunning) {
                try {
                    Socket sc = listenSocket.accept();
                    String ip = (((InetSocketAddress) sc.getRemoteSocketAddress()).getAddress()).toString().replace("/",
                            "");
                    if (canConnectWithIp(ip)) {
                        Session session = new Session(sc, controller, ip);
                        session.ipAddress = ip;
                    } else {
                        sc.close();
                    }
                } catch (Exception e) {
                }
            }
            listenSocket.close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e, "Lỗi mở port");
            System.exit(0);
        }
    }

    private boolean canConnectWithIp(String ipAddress) {
        Object o = CLIENTS.get(ipAddress);
        if (o == null) {
            CLIENTS.put(ipAddress, 1);
            return true;
        } else {
            int n = Integer.parseInt(String.valueOf(o));
            if (n < Manager.MAX_PER_IP) {
                n++;
                CLIENTS.put(ipAddress, n);
                return true;
            } else {
                return false;
            }
        }
    }

    public void disconnect(Session session) {
        Object o = CLIENTS.get(session.ipAddress);
        if (o != null) {
            int n = Integer.parseInt(String.valueOf(o));
            n--;
            if (n < 0) {
                n = 0;
            }
            CLIENTS.put(session.ipAddress, n);
        }
    }

    private void activeCommandLine() {
        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String line = sc.nextLine();
                if (line.equals("baotri")) {
                    new Thread(() -> {
                        Maintenance.gI().start(5);
                    }).start();
                } else if (line.equals("athread")) {
                    ServerNotify.gI().notify("Debug server: " + Thread.activeCount());
                } else if (line.equals("nplayer")) {
                    Log.error("Player in game: " + Client.gI().getPlayers().size());
                } else if (line.equals("panel")) {
                    Panel_Reward pn = new Panel_Reward();
                    pn.run();
                    Log.error("Start Panel");
                } else if (line.equals("reload")) {
                    Panel_Reload rl = new Panel_Reload();
                    rl.setVisible(true);
                    Log.error("Start Reload");
                } else if (line.equals("admin")) {
                    new Thread(() -> {
                        Client.gI().close();
                    }).start();
                }
            }
        }, "Active line").start();
    }

    private void activeGame() {
        long delay = 500;
        long delaySecond = 5000;
        new Thread(() -> {
            while (isRunning) {
                long l1 = System.currentTimeMillis();
                BossManager.gI().updateAllBoss();
                // PhubanMabu.gI().update();
                long l2 = System.currentTimeMillis() - l1;
                if (l2 < delay) {
                    try {
                        Thread.sleep(delay - l2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Update boss").start();

        new Thread(() -> {
            while (isRunning) {
                long start = System.currentTimeMillis();
                for (DoanhTrai dt : DoanhTrai.DOANH_TRAIS) {
                    dt.update();
                }
                for (BanDoKhoBau bdkb : BanDoKhoBau.BAN_DO_KHO_BAUS) {
                    bdkb.update();
                }
                long timeUpdate = System.currentTimeMillis() - start;
                if (timeUpdate < delay) {
                    try {
                        Thread.sleep(delay - timeUpdate);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }, "Update pho ban").start();

        new Thread(() -> {
            while (isRunning) {
                try {
                    long start = System.currentTimeMillis();
                    if (attributeManager != null) {
                        attributeManager.update();
                        if (Util.canDoWithTime(lastUpdateAttribute, 600000)) {
                            Manager.gI().updateAttributeServer();
                            // if (Client.gI().getPlayers().size() >= 1) {
                            // ChatGlobalService.gI().chat(Client.gI().getPlayers().get(Util.nextInt(Client.gI().getPlayers().size())),
                            // "Anh em tham gia sự kiện tại Page Ngọc Rồng 957 ! ");
                            // }
                        }
                    }
                    long timeUpdate = System.currentTimeMillis() - start;
                    if (timeUpdate < delay) {
                        Thread.sleep(delay - timeUpdate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "Update Attribute Server").start();
        dungeonManager = new DungeonManager();
        dungeonManager.start();
        new Thread(dungeonManager, "Phó bản").start();
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // RandomDiemDanh.gI().run();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delay) {
        // Thread.sleep(delay - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Update diem danh").start();
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // MartialCongressManager.gI().update();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delay) {
        // Thread.sleep(delay - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Update dai hoi vo thuat").start();
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // TopWhis.Update();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delay) {
        // Thread.sleep(delay - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Update top whis").start();

        // this.tranhNgocManager = new TranhNgocManager();
        // new Thread(this.tranhNgocManager, "Tranh ngoc").start();
        //
        // this.keobuabao = new KeoBuaBaoManager();
        // new Thread(this.keobuabao,"Update Kéo búa bao").start();
        // this.sieuHangControl = new SieuHangControl();
        // new Thread(this.sieuHangControl, "Sieu hang").start();
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // SieuHangManager.Update();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delay) {
        // Thread.sleep(delay - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Update giai sieu hang").start();
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // SieuHangManager.UpdatePedingFight();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delaySecond) {
        // Thread.sleep(delaySecond - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Update giai sieu hang pending").start();
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // ChuyenKhoanManager.HandleTransactionAuto();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delaySecond) {
        // Thread.sleep(delaySecond - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Check nap the").start();
        // //
        // new Thread(() -> {
        // while (isRunning) {
        // try {
        // long start = System.currentTimeMillis();
        // ChuyenKhoanManager.HandleTransactionAddMoneyAuto();
        // long timeUpdate = System.currentTimeMillis() - start;
        // if (timeUpdate < delay) {
        // Thread.sleep(delay - timeUpdate);
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }, "Cong qua nap the").start();
    }

    public void close(long delay) {
        try {
            dungeonManager.shutdown();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Manager.gI().updateEventCount();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Manager.gI().updateAttributeServer();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            Client.gI().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            ClanService.gI().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        try {
            ConsignManager.getInstance().close();
        } catch (Exception e) {
            Log.error(ServerManager.class, e);
        }
        Client.gI().close();
        Log.success("BẢO TRÌ THÀNH CÔNG!...................................");
        System.exit(0);
    }

    public void saveAll(boolean updateTimeLogout) {
        try {
            List<Player> list = Client.gI().getPlayers();
            Connection conn = DBService.gI().getConnectionForAutoSave();
            for (Player player : list) {
                try {
                    PlayerDAO.updateTimeLogout = updateTimeLogout;
                    PlayerDAO.updatePlayer(player, conn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void autoTask() {
        ScheduledExecutorService autoSave = Executors.newScheduledThreadPool(1);
        autoSave.scheduleWithFixedDelay(() -> {
            saveAll(false);
        }, 300000, 300000, TimeUnit.MILLISECONDS);

        ScheduledExecutorService autoTopPower = Executors.newScheduledThreadPool(1);
        autoTopPower.scheduleWithFixedDelay(() -> {
            TopManager.getInstance().load();
            TopManager.getInstance().loadTopMia();
            TopManager.getInstance().loadTopCa();
            TopManager.getInstance().loadTopTrungThu();
            TopManager.getInstance().loadTopOmega();
            TopManager.getInstance().loadTopBanhKem();
            TopManager.getInstance().loadTopThoiVang();
        }, 0, 600000, TimeUnit.MILLISECONDS);
    }
}
