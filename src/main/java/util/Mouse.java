package util;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser.INPUT;

import static com.sun.jna.platform.win32.BaseTSD.*;
import static com.sun.jna.platform.win32.WinDef.*;


public class Mouse implements MouseInterface {
    public static final int MOUSEEVENTF_MOVE = 1;
    public static final int MOUSEEVENTF_LEFTDOWN = 2;
    public static final int MOUSEEVENTF_LEFTUP = 4;
    public static final int MOUSEEVENTF_RIGHTDOWN = 8;
    public static final int MOUSEEVENTF_RIGHTUP = 16;
    public static final int MOUSEEVENTF_MIDDLEDOWN = 32;
    public static final int MOUSEEVENTF_MIDDLEUP = 64;
    public static final int MOUSEEVENTF_WHEEL = 2048;


    public static void mouseWheelMove(int x, int y){
        mouseAction(x,y,MOUSEEVENTF_WHEEL);
    }

    public static void mouseLeftDown(int x, int y){
        mouseAction(x,y,MOUSEEVENTF_LEFTDOWN);
    }

    public static void mouseLeftUp(int x, int y){
        mouseAction(x,y,MOUSEEVENTF_LEFTUP);
    }

    public static void mouseMove(int x, int y){
//        mouseAction(0,0,MOUSEEVENTF_MOVE);
        mouseAction(x,y,MOUSEEVENTF_MOVE);
    }

    public static void mouseLeftClick(int x, int y){
        mouseAction(x, y, MOUSEEVENTF_LEFTDOWN);
        mouseAction(x, y, MOUSEEVENTF_LEFTUP);
    }

    public static void mouseRightClick(int x, int y) {
        mouseAction(x, y, MOUSEEVENTF_RIGHTDOWN);
        mouseAction(x, y, MOUSEEVENTF_RIGHTUP);
    }

    public static void mouseMiddleClick(int x, int y) {
        mouseAction(x, y, MOUSEEVENTF_MIDDLEDOWN);
        mouseAction(x, y, MOUSEEVENTF_MIDDLEUP);
    }

    public static void mouseDoubleClick(int x, int y){
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
            input.input.mi.dy = new LONG(y);
        }
        input.input.mi.time = new DWORD(0);
        input.input.mi.dwExtraInfo = new ULONG_PTR(0);
        input.input.mi.dwFlags = new DWORD(flags);
        User32.INSTANCE.SendInput(new DWORD(1), new INPUT[]{input}, input.size());
    }

    @Override
    public void initMouseCoord() {
        mouseMove(0,0);
    }


}
