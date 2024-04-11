package org.auto.minwonauto;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import util.Mouse;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.time.format.DateTimeFormatter.ofPattern;
import static util.PathVariable.EDGE_NAME;
import static util.PathVariable.EDGE_PATH;
import static utility.ApplyCategory.*;

public class MinwonMainController {

    Mouse mouse = new Mouse();

//    MouseInterface mouseProxy = MouseProxy.newInstance(new Mouse());

    private Thread t1;
    private Thread t2;
    private Robot r;
    @FXML
    private PasswordField gonginAuthField;
    @FXML
    private Button startButton;
    @FXML
    private Button chromeButton;

    private String gonginPassword;
    private String recipientName;
    private String residentRegistrationNumber;
    private String defaultAddress;
    private String detailAddress;
    private String applyCategory;

    private static boolean defaultAddressFlag = false;
    private static boolean detailAddressFlag = false;

    private static PointerInfo pt = MouseInfo.getPointerInfo();

    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    public MinwonMainController() throws AWTException {
        r = new Robot();
    }

    private void initThreads() {
        t1 = new Thread(() -> {
            WebDriver edgeDriver = new EdgeDriver();
            minwonLogin(edgeDriver);
            minwonAutoProcess(edgeDriver);
        });

        t2 = new Thread(() -> {
            // 보육교직원/주민등록번호
            mouse.move(1622, 390);
            sleepSecond();

            mouse.leftClick(1622, 390); //주민등록번호 DropDown Click
            sleepSecond();

            mouse.move(1622, 390); //주민등록번호 DropDown Click
            sleepSecond();

            mouse.move(1622, 435);
            mouse.leftClick(1622, 435); //주민등록번호 선택
            sleepSecond();

            mouse.doubleClick(1656, 390); //주민등록번호 더블클릭(이미 값이 있다면 선택하기 위함)

            //클립보드에 주민등록번호 넣기
            Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(residentRegistrationNumber);
            clipboard.setContents(selection, selection);

            //클립보드에 있는 주민등록번호 Web에 붙여넣기.
            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_V);
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_V);

            //검색
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);

            //체크박스 Click
            Mouse.move(1119, 553);
            Mouse.leftClick(1119, 553);

            //경력증명 인쇄 Click
            Mouse.move(1586, 485);
            Mouse.leftClick(1586, 485);

            //알림창 확인[총근무연한은 1일 미만 단위를 포함]
            r.keyPress(KeyEvent.VK_ENTER);
            r.keyRelease(KeyEvent.VK_ENTER);

            //일반 주소 검증
            //1. 일반 주소 드래그
            Mouse.leftDown(124, 253);
            Mouse.move(576, 254);
            Mouse.leftUp(576, 254);

            r.keyPress(KeyEvent.VK_CONTROL);
            r.keyPress(KeyEvent.VK_C);
            r.keyRelease(KeyEvent.VK_CONTROL);
            r.keyRelease(KeyEvent.VK_C);

            defaultAddressFlag = isEqualClipboardWith(defaultAddress);

            //2. 디테일 주소 드래그
            Mouse.leftDown(124, 293);
            Mouse.move(576, 294);
            Mouse.leftUp(576, 294);

            detailAddressFlag = isEqualClipboardWith(detailAddress);

            if (defaultAddressFlag && detailAddressFlag) {
                Mouse.leftClick(422, 124);

                //증명서 종류
                //경력증명서 일 때,
                if (applyCategory.equals(EXPERIENCE.getName())) {
                    //경력 체크박스 클릭
                    Mouse.leftClick(696, 550);
                    //경력증명인쇄 클릭
                    Mouse.leftClick(773, 725);

                    //인쇄 알림창 [인쇄하시겠습니까?]
                    r.keyPress(KeyEvent.VK_ENTER);
                    r.keyRelease(KeyEvent.VK_ENTER);

                    // 보호자 또는 본인 요청에 의한 정보조회
                    Mouse.leftClick(27, 198);

                    r.keyPress(KeyEvent.VK_TAB);
                    r.keyRelease(KeyEvent.VK_TAB);
                    r.keyPress(KeyEvent.VK_TAB);
                    r.keyRelease(KeyEvent.VK_TAB);

                    //검색
                    r.keyPress(KeyEvent.VK_ENTER);
                    r.keyRelease(KeyEvent.VK_ENTER);

                    // PDF 인쇄버튼
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(r.getPixelColor(541, 89).getRGB());
                } else {
                    defaultAddressFlag = false;
                    detailAddressFlag = false;
                    throw new RuntimeException("주소가 다름");
                }
            }
        });
    }

    @FXML
    private void initialize() {

        //환경변수 설정
        System.setProperty(EDGE_NAME, EDGE_PATH);

        //Thread 초기화
        initThreads();

//        //마우스 좌표
//        AtomicReference<PointerInfo> pt = new AtomicReference<>(MouseInfo.getPointerInfo());
//        Thread t1 = new Thread(() -> {
//            while (true) {
//                pt.set(MouseInfo.getPointerInfo());
//                System.out.println(pt.get().getLocation().getX() + ", "+ pt.get().getLocation().getY());
//            }
//        });
//        t1.start();


        //startButton 초기화
        startButton.setText("시작");
        startButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");

        startButton.setOnAction(event -> {
            gonginPassword = gonginAuthField.getText();
            gonginAuthField.setText("");
            //Driver가 실행되는 동안 프로그램이 무한로딩이라 Thread 사용함.
            t1.start();
        });

        //chromeButton 초기화
        chromeButton.setOnAction(event -> {
            if (t2.getState() == Thread.State.NEW) {
                t2.start();
            }
        });

        chromeButton.setText("로그인 및 셋팅이 완료되면 눌러주세요.");
        chromeButton.setStyle("-fx-background-color: #457ecd; -fx-text-fill:#ffffff;");
    }

    private boolean isEqualClipboardWith(String address) {
        Transferable contents = clipboard.getContents(null);
        if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String clipboardText = (String) contents.getTransferData(DataFlavor.stringFlavor);
                return clipboardText.equals(address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void minwonAutoProcess(WebDriver edgeDriver) throws RuntimeException {

        GregorianCalendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(calendar.getTime());
        calendar.add(Calendar.DATE, -6);
        String sixdaysAgo = sdf.format(calendar.getTime());

        final String minwonApplyPageUrl = "https://intra.gov.kr/my/AA040ListingOffer.do?returnApp=AA040ListingOfferApp&CP=0&PROC_STATS=0102&FROMDATE=" + today + "&TODATE= " + sixdaysAgo + "&DP=open&ORGAN_GUBUN=1&HIGH_MENU_ID=1000062&MENU_ID=1000689&MENU_INDEX=1";

        Wait<WebDriver> wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(30));
        wait.until(driver -> ((JavascriptExecutor) edgeDriver).executeScript("return document.readyState")).equals("complete");

        try {
//        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // 작동 안 함
            Thread.sleep(2000); // 자체 sleep을 걸었음.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        edgeDriver.get(minwonApplyPageUrl);

        WebElement minwonSearchButton = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[1]/div[1]/a"));
        minwonSearchButton.click();

        WebElement receptionButton = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[1]/div[2]/ul/li[2]/span[1]"));
        receptionButton.click();

        //Busy Waiting

        WebElement firstMinwonButton = null;


        final int REFRESH_SECOND = 5;
        int cnt = 0;
        while(firstMinwonButton == null){
            try {
                firstMinwonButton = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[2]/form[2]/div[1]/table/tbody/tr/td[2]/a"));
            }
            catch (NoSuchElementException e){
                try {
                    Thread.sleep(1000 * REFRESH_SECOND);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                edgeDriver.get(minwonApplyPageUrl);
                System.out.println(++cnt * REFRESH_SECOND + "초 동안 반복중");
            }
        }

        firstMinwonButton.click();


        //주민등록번호
        WebElement residentRegistrationField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[1]/dl/dd[4]"));
        String residentRegistrationFieldText = residentRegistrationField.getText();
        residentRegistrationNumber = residentRegistrationFieldText.replace("-", "");
        System.out.println(residentRegistrationNumber);

        //성명
        WebElement recipientNameField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[1]/dl/dd[2]"));
        recipientName = recipientNameField.getText();
        System.out.println(recipientName);

        //기본주소
        WebElement defaultAddressField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[2]/dl/dd[2]"));
        defaultAddress = defaultAddressField.getText();
        System.out.println(defaultAddress);

        //상세주소
        WebElement detailAddressField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[3]/dl/dd[2]"));
        detailAddress = detailAddressField.getText();
        System.out.println(detailAddress);

        WebElement applyCategoryField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[5]/dl/dd"));
        applyCategory = applyCategoryField.getText();
        System.out.println(applyCategory);


    }

    private void minwonLogin(WebDriver edgeDriver) {
        final String minwonMainUrl = "https://intra.gov.kr/g4g_admin/AA060_new_login.jsp";

        edgeDriver.get(minwonMainUrl);

        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        WebElement authButton = edgeDriver.findElement(By.xpath("/html/body/div[1]/div[2]/div/div/ul[1]/li[1]/button"));

        edgeDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        Wait<WebDriver> wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(10));

        wait.until(edge -> authButton.isDisplayed());
        authButton.click();

        wait = new WebDriverWait(edgeDriver, Duration.ofSeconds(10));
        WebElement selectAdmin = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[3]/table/tbody/tr[2]/td[2]/div"));
        wait.until(edge -> selectAdmin.isDisplayed());
        selectAdmin.click();

        WebElement certificatePassword = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[5]/table/tbody/tr/td[2]/form/div[1]/input[1]"));
        certificatePassword.sendKeys(gonginPassword);

        WebElement certificateConfirm = edgeDriver.findElement(By.xpath("/html/body/div[1]/div/div[2]/div[6]/button[1]"));
        certificateConfirm.click();


    }

    public void sleepSecond() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
