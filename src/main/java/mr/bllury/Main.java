
package mr.bllury;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("plz input the thread number:");
        int thnum = sc.nextInt();
        sc.nextLine();
        System.out.println("plz input the src:");
        String src = sc.nextLine();
        System.out.println("plz input the dest:");
        String dest = sc.nextLine();

        Thread[] threads = new Thread[thnum];

        for (int i = 0; i < thnum; i++) {
            threads[i] = new Thread(new FileOperation(src, dest, thnum, i));
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

    }
}
