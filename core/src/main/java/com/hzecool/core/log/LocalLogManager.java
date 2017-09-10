package com.hzecool.core.log;

/**
 * Created by tutu on 2017/3/2.
 */

import android.content.Context;

import com.hzecool.core.common.utils.AppUtils;
import com.hzecool.core.common.utils.FileUtils;
import com.hzecool.core.common.utils.ZipUtils;
import com.hzecool.core.cache.CacheManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户日志管理
 * 1.分天记录用户日志
 * 2.日志压缩
 * <p>
 * 注意:使用此类需要首先调用 initLog()方法初始化
 *
 * @author tutu
 */
public class LocalLogManager {

    private static LocalLogManager localLogManager;

    /**
     * 内存缓存的最大条数
     */
    private static int MAXSIZE_CACHE = 10;

    /**
     * 缓存文件最大个数
     */
    private static int MAXSIZE_CACHE_FILE = 7;

    /**
     * 内存缓存数据
     */
    private List<String> logCache = new ArrayList<>();

    /**
     * app类型的上下文
     */
    private Context appContext;

    /**
     * 缓存文件路径
     */
    private String cacheFilePath;

    /**
     * 缓存文件后缀名
     */
    private final String FILE_FORMAT = ".log";


    private LocalLogManager() {
    }

    public static LocalLogManager getInstanse() {
        if (localLogManager == null) {
            localLogManager = new LocalLogManager();
        }

        return localLogManager;
    }

    /**
     * 初始化 需要在app中初始化
     */
    public static void initLog(Context context) {
        getInstanse();
        localLogManager.appContext = context;
        localLogManager.cacheFilePath = CacheManager.getTyleCachePath(CacheManager.LOG_CACHE_PATH);
    }


    /**
     * 缓存日志  不是直接写入日志 太过频繁 影响性能
     */
    public synchronized void cacheLog(String logString) {
        if (appContext == null) {
            throw new RuntimeException("call initLog() first");
        }

        if (logCache.size() < MAXSIZE_CACHE) {
            //只添加到日志缓存
            logCache.add(logString);
        } else {
            //缓存满了  写入文件
            new Thread(() -> cacheToFile()).start();
        }
    }

    /**
     * 缓存写入文件
     */
    private void cacheToFile() {
        File file = new File(getFilePath());
        if (file.exists()) {
            //已经有了今天的日志 追加写入
            saveFileAdd(getFilePath(), catLogString());
        } else {
            //没有今天的文件日志  创建文件写入
            cheakCacheFileValidate();
            saveFile(getFilePath(), catLogString());

        }
    }

    /**
     * 检查缓存是否过期
     */
    private void cheakCacheFileValidate() {
        //缓存文件超过限制  删除旧的日志
        List<File> cacheFileList = getAllLogFiles();
        if (cacheFileList.size() > MAXSIZE_CACHE_FILE) {
            //删除过期的缓存文件
            for (int i = 0; i < cacheFileList.size() - MAXSIZE_CACHE_FILE; i++) {
                deleteFile(cacheFileList.get(i).getAbsolutePath());
            }
        }
    }

    /**
     * 组装缓存日志字符串
     */
    private synchronized String catLogString() {
        try {
            StringBuffer sb = new StringBuffer();
            List<String> logCacheTemp = new ArrayList<>();
            logCacheTemp.addAll(logCache);

            for (int i = 0; i < logCacheTemp.size(); i++) {
                sb.append("----------------------------------------------------")
                        .append("\n")
                        .append(getTimeFormateString())
                        .append("\n")
                        .append(logCacheTemp.get(i))
                        .append("\n\n");
            }
            logCache.clear();
            return sb.toString();
        } catch (Exception e) {
            return e.getStackTrace().toString();
        }
    }


    /**
     * 强制将缓存写入文件
     */
    public void flushCache2File() {
        if (appContext == null) {
            throw new RuntimeException("call initLog() first");
        }

        if (logCache.isEmpty()) {
            return;
        }

        cacheToFile();
    }


    /**
     * 保存到文件
     */
    private synchronized boolean saveFile(String filePath, final String msg) {
        new Thread(() -> {
            addFileHead(msg);
            File file = new File(filePath);

            OutputStream outputStream = null;
            try {
                if (!file.exists()) {
                    if (!FileUtils.createOrExistsFile(file)) {
                        return;
                    }
                }
                outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
                outputStreamWriter.write(msg);
                outputStreamWriter.flush();
                outputStream.close();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return true;
    }

    private String addFileHead(String msg) {

        StringBuilder builder = new StringBuilder();
        builder.append(AppUtils.getPhoneSysInfo())
                .append("\n")
                .append(msg);
        return builder.toString();
    }

    /**
     * 追加模式写入文件
     */
    private synchronized void saveFileAdd(String dicString, String content) {
        // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
        new Thread(() -> {
            try {
                FileWriter writer = new FileWriter(dicString, true);
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    /**
     * 获取文件路径
     */
    private String getFilePath() {

        File file = new File(cacheFilePath, date() + FILE_FORMAT);

        return file.getAbsolutePath();
    }

    /**
     * 格式化时间
     */
    private String getTimeFormateString() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        return t1;
    }


    /**
     * 获取当前时间
     */
    private String date() {
        final Calendar c = Calendar.getInstance();
        //c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mYear + "_" + mMonth + "_" + mDay;
    }


    /**
     * 获取当前目录下所有的log文件
     */
    public List<File> getAllLogFiles() {
        List<File> vecFile = new ArrayList<>();
        File file = new File(cacheFilePath);
        File[] subFile = file.listFiles();

        //没有文件
        if (subFile == null) {
            return vecFile;
        }

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                File filec = subFile[iFileLength];
                // 判断是否为log结尾
                if (filec.getAbsolutePath().trim().toLowerCase().endsWith(FILE_FORMAT)) {
                    vecFile.add(filec);
                }
            }
        }
        return vecFile;
    }


    /**
     * 删除文件
     */
    private boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }


    /**
     * 获取所有日志文件的zip文件
     */
    public File getZipAllLogs() throws IOException {

        List<File> filePath = getAllLogFiles();
        String zipPath = cacheFilePath + File.separator + "log.zip";
        if (filePath == null || filePath.size() <= 0) {
            new Throwable().printStackTrace();
            return null;
        }

        File zipFile = new File(zipPath);
        if (zipFile.exists()) {
            zipFile.delete();
        }

        ZipUtils.zipFiles(filePath, zipFile);

        if (zipFile.exists()) {
            return zipFile;
        } else {
            return null;
        }
    }


    /**
     * 压缩单个文件
     */
    public File getSingleLogZip(File file) throws IOException {
        if (!file.exists()) {
            return null;
        }

        File zipFile = new File(genZipFilePath(file));
        if (zipFile.exists()) {
            zipFile.delete();
        }

        ZipUtils.zipFile(file, zipFile);

        if (zipFile.exists()) {
            return zipFile;
        } else {
            return null;
        }
    }

    /**
     * @param file 根据日志文件获取zip文件路径
     * @return
     */
    private String genZipFilePath(File file) {
        String path = file.getAbsolutePath();

        int pos = path.lastIndexOf(".");

        String dir = path.substring(0, pos);

        String zipPath = dir + ".zip";

        return zipPath;
    }
}
