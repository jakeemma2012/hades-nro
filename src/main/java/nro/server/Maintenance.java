package nro.server;
import nro.services.Service;
import nro.utils.Log;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Maintenance {
    public static boolean isRuning = false;
    public static Maintenance instance;
    public static int seconds;
    public static boolean maintenanceStarted = false;
    public ExecutorService executor;
    public Future<?> maintenanceTask;

    private Maintenance() {
        executor = Executors.newSingleThreadExecutor();
    }

    public static Maintenance gI() {
        if (instance == null) {
            instance = new Maintenance();
        }
        return instance;
    }

    public void start(int seconds) {
        if (!isRuning) {
            this.seconds = seconds;
            isRuning = true;
            maintenanceStarted = false;
            
            maintenanceTask = executor.submit(this::run);
        }
    }

    public void stopMaintenance() {
        if (!maintenanceStarted) {
            isRuning = false;
            if (maintenanceTask != null) {
                maintenanceTask.cancel(true);
            }
            System.err.println("Đã yêu cầu hủy bỏ bảo trì!");
        } else {
            System.err.println("Không thể hủy bỏ, bảo trì đã bắt đầu");
        }
    }

    private void run() {
        while (isRuning && seconds > 0) {
            Service.getInstance().sendThongBaoAllPlayer("Hệ thống sẽ bảo trì sau " + seconds
                    + " giây nữa, vui lòng thoát game để tránh mất vật phẩm");
            try {
                Thread.sleep(1000);
                seconds--;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread bảo trì bị gián đoạn");
                break;
            }
        }

        if (isRuning && seconds <= 0) {
            startMaintenance();
        } else {
            cancelMaintenance();
        }
    }

    private void startMaintenance() {
        maintenanceStarted = true;
        System.out.println("BẢO TRÌ BẮT ĐẦU...............................");
        ServerManager.gI().close(100);
    }

    private void cancelMaintenance() {
        if (!maintenanceStarted) {
            System.out.println("Bảo trì đã bị hủy bỏ");
            Service.getInstance().sendThongBaoAllPlayer("Bảo trì đã được hủy bỏ");
        } else {
            System.out.println("Không thể hủy bỏ, bảo trì đã bắt đầu");
        }
    }

    public void shutdown() {
        executor.shutdownNow();
    }
}