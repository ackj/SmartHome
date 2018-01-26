package cn.itsite.apayment.payment.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description 简易线程池管理工具.
 */

public class ThreadManager {
    private static ExecutorService mExecutors = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable) {
        if (mExecutors == null) {
            mExecutors = Executors.newSingleThreadExecutor();
        }
        mExecutors.execute(runnable);
    }

    public static void shutdown() {
        if (mExecutors == null) {
            return;
        }
        if (mExecutors.isShutdown()) {
            return;
        }
        mExecutors.shutdownNow();
        mExecutors = null;
    }
}
