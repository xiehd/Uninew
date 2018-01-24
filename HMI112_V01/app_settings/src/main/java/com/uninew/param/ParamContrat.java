package com.uninew.param;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public interface ParamContrat {
    interface View {

        void setPresenter(Presenter presenter);

        void showHeartbeat(String time);

        void showTcpTimeOut(String time);

        void showTcpTimes(String times);

        void showMainIp(String ip);

        void showMainPort(String port);

        void showSpareIp(String ip);

        void showSparePort(String port);

        public void showReportStrategy(String reportStrategy);

        public void showReportPlan(String reportPlan);

        public void showUnLoginReportIntervalTime(String unLoginReportIntervalTime);

        public void showAccOffReportIntervalTime(String accOffReportIntervalTime);

        public void showAccOnReportIntervalTime(String accOnReportIntervalTime);

        public void showEmptyReportIntervalTime(String emptyReportIntervalTime);

        public void showNoEmptyReportIntervalTime(String noEmptyReportIntervalTime);

        public void showSleepReportIntervalTime(String sleepReportIntervalTime);

        public void showEmergencyReportIntervalTime(String emergencyReportIntervalTime);

        public void showUnLoginReportIntervalDistance(String unLoginReportIntervalDistance);

        public void showAccOffReportIntervalDistance(String accOffReportIntervalDistance);

        public void showAccOnReportIntervalDistance(String accOnReportIntervalDistance);

        public void showEmptyReportIntervalDistance(String emptyReportIntervalDistance);

        public void showNoEmptyReportIntervalDistance(String noEmptyReportIntervalDistance);

        public void showSleepReportIntervalDistance(String sleepReportIntervalDistance);

        public void showEmergencyReportIntervalDistance(String emergencyReportIntervalDistance);

    }

    interface Presenter {

        void start();

        void stop();
    }

}
