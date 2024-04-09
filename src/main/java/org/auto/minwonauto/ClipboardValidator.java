package org.auto.minwonauto;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.util.Objects;

public class ClipboardValidator {

    private String defaultAddress;
    private String detailAddress;

    public static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public ClipboardValidator(){
        this.defaultAddress = Objects.requireNonNull(defaultAddress);
        this.detailAddress = Objects.requireNonNull(detailAddress);
    }

//    public Notification validate(){
//        final Notification notification = new Notification();
//        Transferable contents = clipboard.getContents(null);
//        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
//            try {
//                String clipboardText = (String) contents.getTransferData(DataFlavor.stringFlavor);
////                return clipboardText.equals(address);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//    }
}
