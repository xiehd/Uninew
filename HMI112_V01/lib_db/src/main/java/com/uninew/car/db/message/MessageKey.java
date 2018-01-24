package com.uninew.car.db.message;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public final class MessageKey {
    private MessageKey() {

    }

    public static abstract class MessageTypeKey {
        /* 报文 */
        public static final int COMMUNIQUE_TYPE = 0;
        /* 事件*/
        public static final int EVENT_TYPE = 1;
        /* 提问 */
        public static final int QUESTION_TYPE = 2;
    }

    public static abstract class MessageAnswerState {
        /* 已应答 */
        public static final int ANSWERED = 0;
        /* 为应答*/
        public static final int UNANSWERED = 1;
    }

    public static abstract class MessageReadState {
        /* 未读 */
        public static final int UNREAD = 0;
        /* 已读*/
        public static final int READED = 1;
    }
}
