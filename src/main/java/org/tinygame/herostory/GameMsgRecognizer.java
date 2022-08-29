package org.tinygame.herostory;

import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.*;

//消息识别器
public class GameMsgRecognizer {

    private static Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    //私有化类默认构造器
    private GameMsgRecognizer(){
    }

    //通过msgCode得到Message.Builder
    public static Message.Builder getBuilderByMsgCode(int msgCode){
        //构建一个builder
        switch (msgCode){
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE: //如果读取到的是消息0
                //messageV3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody); //
                return  GameMsgProtocol.UserEntryCmd.newBuilder();
            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                //messageV3 = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                return GameMsgProtocol.WhoElseIsHereCmd.newBuilder();
            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                //messageV3 = GameMsgProtocol.UserMoveToCmd.parseFrom(msgBody);
                return GameMsgProtocol.UserMoveToCmd.newBuilder();

            default:
                return null;
        }

    }

    public static int getMessageCodyByMsgObject(Object msgClazz){

        if (msgClazz instanceof GameMsgProtocol.UserEntryResult){
            return GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        } else if (msgClazz instanceof GameMsgProtocol.WhoElseIsHereResult) {
            return GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        } else if (msgClazz instanceof GameMsgProtocol.UserMoveToResult) {
            return GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        } else if (msgClazz instanceof GameMsgProtocol.UserQuitResult) {
            return GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        } else{
            LOGGER.error("无法识别的消息类型,msgClazz = "+msgClazz.getClass().getName());
            return -1;
        }
    }




}
