package org.tinygame.herostory.cmdHandler;

//工厂类,不需要实例化的


import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令处理器工厂
 *
 */
public final class CmdHandlerFactory {

    /**
     * 处理器字典
     *
     * Class<?>未确定类型的Class对象
     */
    private static Map<Class<?>,ICmdHandler<?extends GeneratedMessageV3>> _handlerMap = new HashMap<>();

    public static void init(){

        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHandler());

    }




    public static ICmdHandler<?extends GeneratedMessageV3> create(Class<?> msgClazz) {
        if (null == msgClazz){
            return null;
        }

        return _handlerMap.get(msgClazz);


    }


    private CmdHandlerFactory(){

    }
}
