package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.*;

//消息识别器
public class GameMsgRecognizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    /**
     * 消息代码和消息体字典
     */
    private static final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap<>();


    /**
     * 消息类型和消息代码字典
     */
    private static final Map<Class<?>,Integer> _msgClazzAndMsgCodeMap = new HashMap<>();



    //私有化类默认构造器
    private GameMsgRecognizer() {
    }

    /**
     * 初始化map 这个map保存 msgCode 和对应的实例
     */
    public static void init(){

        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE,GameMsgProtocol.UserEntryCmd.getDefaultInstance());
        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE,GameMsgProtocol.WhoElseIsHereCmd.getDefaultInstance());
        _msgCodeAndMsgBodyMap.put(GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE,GameMsgProtocol.UserMoveToCmd.getDefaultInstance());

        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserEntryResult.class,GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.WhoElseIsHereResult.class,GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserMoveToResult.class,GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        _msgClazzAndMsgCodeMap.put(GameMsgProtocol.UserQuitResult.class,GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }

    //通过msgCode得到Message.Builder
    public static Message.Builder getBuilderByMsgCode(int msgCode) {
        if (msgCode < 0) {
            return null;
        }

        GeneratedMessageV3 messageV3 = _msgCodeAndMsgBodyMap.get(msgCode);
        if (null == messageV3){
            return null;
        }

        //Protobuf自动生成的，通过类型，返回一个新的builder
        return messageV3.newBuilderForType();
    }

    public static int getMessageCodyByMsgClazz(Class<?> msgClazz) {
        if (null == msgClazz){
            return -1;
        }

        Integer msgCode  = _msgClazzAndMsgCodeMap.get(msgClazz);
        if (null != msgCode){
            return msgCode;
        }else{
            return -1;
        }

    }
}
