package util;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.INPUT;

import static com.sun.jna.platform.win32.BaseTSD.*;
import static com.sun.jna.platform.win32.WinDef.*;


public class Mouse{
    public static final int MOUSEEVENTF_MOVE = 1;
    public static final int MOUSEEVENTF_LEFTDOWN = 2;
    public static final int MOUSEEVENTF_LEFTUP = 4;
    public static final int MOUSEEVENTF_RIGHTDOWN = 8;
    public static final int MOUSEEVENTF_RIGHTUP = 16;
    public static final int MOUSEEVENTF_MIDDLEDOWN = 32;
    public static final int MOUSEEVENTF_MIDDLEUP = 64;
    public static final int MOUSEEVENTF_WHEEL = 2048;

    public void wheelMove(int x, int y){;
        mouseAction(x,y,MOUSEEVENTF_WHEEL);
    }

    public static void leftDown(int x, int y){
        mouseAction(x,y,MOUSEEVENTF_LEFTDOWN);
    }

    public static void leftUp(int x, int y){
        mouseAction(x,y,MOUSEEVENTF_LEFTUP);
    }

    public static void move(int x, int y){
        User32.INSTANCE.SetCursorPos(x,y);
    }

    public static void leftClick(int x, int y){
        mouseAction(x, y, MOUSEEVENTF_LEFTDOWN);
        mouseAction(x, y, MOUSEEVENTF_LEFTUP);
    }

    public static void rightClick(int x, int y) {
        mouseAction(x, y, MOUSEEVENTF_RIGHTDOWN);
        mouseAction(x, y, MOUSEEVENTF_RIGHTUP);
    }

    public static void middleClick(int x, int y) {
        mouseAction(x, y, MOUSEEVENTF_MIDDLEDOWN);
        mouseAction(x, y, MOUSEEVENTF_MIDDLEUP);
    }

    public static void doubleClick(int x, int y){
        mouseAction(x, y, MOUSEEVENTF_LEFTDOWN);
        mouseAction(x, y, MOUSEEVENTF_LEFTUP);
        mouseAction(x, y, MOUSEEVENTF_LEFTDOWN);
        mouseAction(x, y, MOUSEEVENTF_LEFTUP);
    }

    private static void mouseAction(int x, int y, int flags) {
        INPUT input = new INPUT();
        input.type = new DWORD(INPUT.INPUT_MOUSE);
        input.input.setType("mi");

        if (x != -1) {
            input.input.mi.dx = new LONG(x);
        }
        if (y != -1) {
            input.input.mi.dy = new LONG(-y);
        }

        input.input.mi.time = new DWORD(0);
        input.input.mi.dwExtraInfo = new ULONG_PTR(0);
        input.input.mi.dwFlags = new DWORD(flags);
        User32.INSTANCE.SendInput(new DWORD(1), new INPUT[]{input}, input.size());
    }

//    private static PointerInfo pt = MouseInfo.getPointerInfo();

//        //마우스 좌표
//        AtomicReference<PointerInfo> pt = new AtomicReference<>(MouseInfo.getPointerInfo());
//        Thread t1 = new Thread(() -> {
//            while (true) {
//                pt.set(MouseInfo.getPointerInfo());
//                System.out.println(pt.get().getLocation().getX() + ", "+ pt.get().getLocation().getY());
//            }
//        });
//        t1.start();
}
