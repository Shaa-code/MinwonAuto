package org.auto.minwonauto;

import javafx.application.Platform;
import javafx.scene.control.Button;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Mouse;
import javafx.scene.control.TextArea;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static utility.ApplyCategory.EXPERIENCE;

public class MinwonService{

    private static final String MINWON_MAIN_URL = "https://intra.gov.kr/g4g_admin/AA060_new_login.jsp";
    private static final String XPATH_AUTH_BUTTON = "/html/body/div[1]/div[2]/div/div/ul[1]/li[1]/button";
    private static final String XPATH_SELECT_ADMIN =  "/html/body/div[1]/div/div[2]/div[3]/table/tbody/tr/td[2]/div";
    private static final String XPATH_CERTIFICATE_PASSWORD = "/html/body/div[1]/div/div[2]/div[5]/table/tbody/tr/td[2]/form/div[1]/input[1]";
    private static final String XPATH_CERTIFICATE_CONFIRM = "/html/body/div[1]/div/div[2]/div[6]/button[1]";

    private final WebDriver edgeDriverForMinwon;
    private final WebDriver edgeDriverForAnyWhereMinwon;
    public static final String minwonApplyPageUrl = minwonSearchCondition(6);
    public static final String minwonAnywhereApplyPageUrl = minwonAnywhereSearchCondition(6);
    private final Clipboard clipboard;
    public static boolean whenMinwonFound = false;
    public static final int REFRESH_SECOND = 5;

    private ProcessDisplayUpdater observer;
    private boolean defaultAddressFlag;
    private boolean detailAddressFlag;
    private Robot robot;
    private Mouse mouse;

    public MinwonService(ProcessDisplayUpdater observer) throws AWTException {
        this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        this.edgeDriverForMinwon = new EdgeDriver();
        this.edgeDriverForAnyWhereMinwon = new EdgeDriver();
        this.robot = new Robot();
        this.mouse = new Mouse();
        this.observer = observer;

    }

    private void notify(String message){
        if(observer != null){
            observer.updateProcessDisplay(message);
        }
    }

    private void notifyStyle(String style){
        if(observer != null){
            observer.updateStyle(style);
        }
    }

    private void setResearchButtonDisabled(boolean status){
        if(observer != null){
            observer.setResearchButtonDisabled(status);
        }
    }

    public void minwonAutoProcess(String gonginPassword) throws Throwable {

        displayAndExecute("일반 민원 로그인 시도..",
                () -> minwonLogin(edgeDriverForMinwon, gonginPassword),
                "일반 민원 로그인 성공..",
                "일반 민원 로그인 실패..");

        displayAndExecute("일반 민원 신청서 페이지로 이동..",
                () -> gotoMinwonApplyPage(edgeDriverForMinwon, minwonApplyPageUrl),
                "일반 민원 신청서 페이지로 이동 완료",
                "로그인 실패..");

        displayAndExecute("일반 민원 찾기 시작..",
                () -> busyWaitUntilFindFirstMinwon(minwonApplyPageUrl, REFRESH_SECOND),
                "일반 민원 찾기 완료..",
                "일반 민원 찾기 오류..");
    }

    public void minwonAnywhereAutoProcess(String gonginPassword) throws Throwable {
        displayAndExecute("어디서나 민원 로그인 시도..",
                () -> minwonLogin(edgeDriverForAnyWhereMinwon, gonginPassword),
                "어디서나 민원 로그인 성공..",
                "어디서나 민원 로그인 실패..");

        displayAndExecute("어디서나 민원신청서 페이지로 이동..",
                () -> gotoMinwonApplyPage(edgeDriverForAnyWhereMinwon, minwonAnywhereApplyPageUrl),
                "어디서나 민원신청서 페이지로 이동 완료",
                "어디서나 민원 로그인 실패..");

        displayAndExecute("어디서나 민원찾기 시작..",
                () -> busyWaitUntilFindFirstAnywhereMinwon(minwonAnywhereApplyPageUrl, REFRESH_SECOND),
                "어디서나 민원 찾기 완료..",
                "어디서나 민원 찾기 오류");
    }


    private void displayAndExecute(String tryMessage, Runnable task, String successMessage, String failMessage) throws Throwable {
        try {
            notify(tryMessage);
            task.run();
            notify(successMessage);
        } catch (Throwable e) {
            notify(failMessage);
        }
    }

    private static String minwonSearchCondition(int agoFromToday) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -agoFromToday);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String today = sdf.format(calendar.getTime());
        final String sixdaysAgo = sdf.format(calendar.getTime());

        final String minwonApplyPageUrl = "https://intra.gov.kr/my/AA040ListingOffer.do?returnApp=AA040ListingOfferApp&CP=0&PROC_STATS=0102" +
                "&FROMDATE=" + sixdaysAgo + //
                "&TODATE=" + today + //
                "&DP=open&ORGAN_GUBUN=1&HIGH_MENU_ID=1000062&MENU_ID=1000689&MENU_INDEX=1";

        return minwonApplyPageUrl;
    }

    private static String minwonAnywhereSearchCondition(int agoFromToday) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -agoFromToday);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String today = sdf.format(calendar.getTime());
        final String sixdaysAgo = sdf.format(calendar.getTime());

        final String minwonAnywhereApplyPageUrl = "https://intra.gov.kr/my/AA180FaxListeringOffer.do?organ_cd=3460000&organ_type=2&proc_stat=0102" +
                "&from_accp_day " + sixdaysAgo +
                "&to_accp_day=" + today +
                "&HIGH_MENU_ID=1000552&MENU_ID=1000076&MENU_INDEX=1&SUB_MENU_INDEX=2";

        return minwonAnywhereApplyPageUrl;
    }

    private void gotoMinwonApplyPage(WebDriver webDriver, String minwonApplyPageUrl) {
        Wait<WebDriver> wait = new WebDriverWait(webDriver, Duration.ofSeconds(30));
        wait.until(driver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState")).equals("complete");

        sleepSecond(2);

        webDriver.get(minwonApplyPageUrl);

        WebElement minwonSearchButton = webDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[1]/div[1]/a"));
        minwonSearchButton.click();

        WebElement receptionButton = webDriver.findElement(By.xpath("/html/body/div[2]/div[1]/div[3]/div[1]/div[2]/ul/li[2]/span[1]"));
        receptionButton.click();
    }

    public void busyWaitUntilFindFirstMinwon(String minwonApplyPageUrl, int refreshSecond) {
        WebElement firstMinwonButton = null;
        int cnt = 1;
        while (firstMinwonButton == null && !whenMinwonFound) {
            try {
                firstMinwonButton = this.edgeDriverForMinwon.findElement(By.xpath("/html/body/div[2]/div[2]/form[2]/div[1]/table/tbody/tr/td[2]/a"));
                notify("일반 민원이 접수되었습니다.");
                notifyStyle("-fx-background-color: green");
                setResearchButtonDisabled(false);
                firstMinwonButton.click();
                whenMinwonFound = true;
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(1000 * refreshSecond); //실제로는 1.5배 더 걸림 왜인지 모르겠음.
                    notify("일반 민원 재 탐색 " + cnt++ * refreshSecond + "초 동안 진행중..");
                    this.edgeDriverForMinwon.get(minwonApplyPageUrl);
                } catch (InterruptedException ex) {
                    throw new RuntimeException();
                }
            }
        }
    }

    public void busyWaitUntilFindFirstAnywhereMinwon(String minwonApplyPageUrl, int refreshSecond) {
        WebElement firstMinwonButton = null;
        int cnt = 1;
        while (firstMinwonButton == null && !whenMinwonFound) {
            try {
                firstMinwonButton = this.edgeDriverForAnyWhereMinwon.findElement(By.xpath("/html/body/div[2]/div[2]/form/div[5]/table/tbody/tr/td[2]/a"));
                notify("어디서나 민원이 접수되었습니다.");
                notifyStyle("-fx-background-color: blue");
                setResearchButtonDisabled(false);
                firstMinwonButton.click();
                whenMinwonFound = true;
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(1000 * refreshSecond); //실제로는 1.5배 더 걸림 왜인지 모르겠음.
                    notify("어디서나 민원 재 탐색 " + cnt++ * refreshSecond + "초 동안 진행중..");
                    this.edgeDriverForAnyWhereMinwon.get(minwonApplyPageUrl);
                } catch (InterruptedException ex) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private void minwonLogin(WebDriver webDriver, String gonginPassword) {

        webDriver.get(MINWON_MAIN_URL);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        clickElement(webDriver, By.xpath(XPATH_AUTH_BUTTON));
        clickElement(webDriver, By.xpath(XPATH_SELECT_ADMIN));

        WebElement certificatePassword = webDriver.findElement(By.xpath(XPATH_CERTIFICATE_PASSWORD));
        certificatePassword.sendKeys(gonginPassword);

        clickElement(webDriver, By.xpath(XPATH_CERTIFICATE_CONFIRM));

    }

    private void clickElement(WebDriver webDriver, By by){
        WebElement element = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(by));
        element.click();
    }

    public void sleepSecond(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public void chromeAutomation(WebDriver webDriver) {
        Recipient recipient = recipientParse(webDriver);

        // 보육교직원/주민등록번호
        mouse.move(1622, 390);
        sleepSecond(2);

        mouse.leftClick(1622, 390); //주민등록번호 DropDown Click
        sleepSecond(2);

        mouse.move(1622, 390); //주민등록번호 DropDown Click
        sleepSecond(2);

        mouse.move(1622, 435);
        mouse.leftClick(1622, 435); //주민등록번호 선택
        sleepSecond(2);

        mouse.doubleClick(1656, 390); //주민등록번호 더블클릭(이미 값이 있다면 선택하기 위함)

        //클립보드에 주민등록번호 넣기
        Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(recipient.getResidentRegistrationNumber());
        clipboard.setContents(selection, selection);

        //클립보드에 있는 주민등록번호 Web에 붙여넣기.
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);

        //검색
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        //체크박스 Click
        Mouse.move(1119, 553);
        Mouse.leftClick(1119, 553);

        //경력증명 인쇄 Click
        Mouse.move(1586, 485);
        Mouse.leftClick(1586, 485);

        //알림창 확인[총근무연한은 1일 미만 단위를 포함]
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        //일반 주소 검증
        //1. 일반 주소 드래그
        Mouse.leftDown(124, 253);
        Mouse.move(576, 254);
        Mouse.leftUp(576, 254);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_C);

        defaultAddressFlag = isEqualClipboardWith(recipient.getDefaultAddress());

        //2. 디테일 주소 드래그
        Mouse.leftDown(124, 293);
        Mouse.move(576, 294);
        Mouse.leftUp(576, 294);

        detailAddressFlag = isEqualClipboardWith(recipient.getDetailAddress());

        if (defaultAddressFlag && detailAddressFlag) {
            Mouse.leftClick(422, 124);

            //증명서 종류
            //경력증명서 일 때,
            if (recipient.getApplyCategory().equals(EXPERIENCE.getName())) {
                //경력 체크박스 클릭
                Mouse.leftClick(696, 550);
                //경력증명인쇄 클릭
                Mouse.leftClick(773, 725);

                //인쇄 알림창 [인쇄하시겠습니까?]
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                // 보호자 또는 본인 요청에 의한 정보조회
                Mouse.leftClick(27, 198);

                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_TAB);

                //검색
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);

                // PDF 인쇄버튼
                sleepSecond(1);
                System.out.println(robot.getPixelColor(541, 89).getRGB());

            } else {
                defaultAddressFlag = false;
                detailAddressFlag = false;
                throw new RuntimeException("주소가 다름");
            }
        }


    }

        private Recipient recipientParse(WebDriver edgeDriver) {
        //주민등록번호
        WebElement residentRegistrationField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[1]/dl/dd[4]"));
        String residentRegistrationFieldText = residentRegistrationField.getText();
        //성명
        WebElement recipientNameField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[1]/dl/dd[2]"));
        String recipientName = recipientNameField.getText();
        //기본주소
        WebElement defaultAddressField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[2]/dl/dd[2]"));
        String defaultAddress = defaultAddressField.getText();
        //상세주소
        WebElement detailAddressField = edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[3]/dl/dd[2]"));
        String detailAddress = detailAddressField.getText();

        Optional<WebElement> optApplyCategoryField = Optional.ofNullable(edgeDriver.findElement(By.xpath("/html/body/div[2]/div[3]/div[3]/div[2]/div/form/dl/dd/dl/dd[5]/dl/dd")));

        return new Recipient.RecipientBuilder()
                .residentRegistrationNumber(residentRegistrationFieldText)
                .recipientName(recipientName)
                .defaultAddress(defaultAddress)
                .detailAddress(detailAddress)
                .build();
    }

}
