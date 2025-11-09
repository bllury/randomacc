package mr.bllury;

import java.io.*;
import java.util.concurrent.*;

class FileOperation implements Runnable {

    String source;
    String dest;
    int thnum;
    int threadIndex;


    public FileOperation(String source, String dest, int threadNum, int threadIndex) {
        this.source = source;
        this.dest = dest;
        this.thnum = threadNum;
        this.threadIndex = threadIndex;
    }

    public void copyFile() throws FileNotFoundException, IOException {
        File srcFile = new File(source);
        long fileSize = srcFile.length();

        long blockSize = fileSize / thnum;

        long remainder = fileSize % thnum;

        long startPos = threadIndex * blockSize + Math.min(threadIndex, remainder);
        long endPos = startPos + blockSize + (threadIndex < remainder ? 1 : 0);


        try (RandomAccessFile rafDest = new RandomAccessFile(dest, "rw")) {
            rafDest.setLength(fileSize);
        }

        try (RandomAccessFile rafSrc = new RandomAccessFile(source, "rw");
             RandomAccessFile rafDest = new RandomAccessFile(dest, "rw")) {

            rafSrc.seek(startPos);
            rafDest.seek(startPos);

            byte[] buffer = new byte[1024*1024];
            long remaining = endPos - startPos;

            while (remaining > 0) {
                int toRead = (int) Math.min(buffer.length, remaining);
                int read = rafSrc.read(buffer, 0, toRead);
                if (read == -1) break;
                rafDest.write(buffer, 0, read);
                remaining -= read;
            }
        }
        if (threadIndex == thnum-1) {
            System.out.println("源文件:" + source + "-->目标文件" + dest);
            System.out.println("文件拷贝完成, 使用 " + thnum + " 个线程");
        }
    }

    @Override
    public void run() {
        try {
            copyFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}