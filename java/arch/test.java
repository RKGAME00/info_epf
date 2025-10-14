package arch;

import java.util.Timer;
import java.util.TimerTask;

class Test {

    public Test(int version) {
        System.out.println("prog test java" + (char) version);
        System.out.println("entrez un chiffre\n");
    }

    public void delay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test testInstance = new Test(1);

        int[] t = { 1, 2, 3, 4, 5 };
        boolean exit = false;

        for (int i : t) {
            System.out.println(i + "S");
            testInstance.delay(1000);
        }

        while (!exit) {
            int c = 0;

            System.out.println("1: test1\n2: test2\n3: exit\n");

            try {
                c = System.in.read();
                System.out.println("You entered: " + (char) c);
            } catch (java.io.IOException e) {
                System.out.println("erreur de lecture");
            }

            switch (c) {
                case '1':
                    System.out.println("test 1");
                    break;
                case '2':
                    System.out.println("test2");
                    break;
                case '3':
                    System.out.println("exit");
                    exit = true;
                    break;

                default:

                    System.out.println("entrée par defaut");
                    break;
            }

        }

    }
}