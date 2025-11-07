package com.chenbitao.action_in_my_batis_plus.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用 SSE 管理工具类（无业务耦合）
 *
 * 负责：
 *   - 管理所有 SseEmitter 连接
 *   - 提供连接、发送、广播、移除、统计等功能
 *   - 不依赖任何 userId，仅用 token 标识
 *
 * 业务层职责：
 *   - 维护 userId ↔ token 的映射关系
 *   - 控制哪些 token 可连接 / 可发送
 */
public final class SseEmitterUtil {

    /** 存储所有 SSE 连接（key = token） */
    private static final Map<String, SseEmitter> EMITTER_MAP = new ConcurrentHashMap<>();

    private SseEmitterUtil() {
        // 工具类禁止实例化
    }

    /**
     * 创建并缓存 SseEmitter
     *
     * @param token 唯一标识（建议由业务生成，例如 userId 对应的随机UUID）
     * @param timeout 超时时间，单位 ms（传入 0 表示永不过期）
     */
    public static SseEmitter connect(String token, long timeout) {
        // 默认永不过期
        long actualTimeout = timeout <= 0 ? 0L : timeout;
        SseEmitter emitter = new SseEmitter(actualTimeout);

        // 若旧连接存在，清除
        EMITTER_MAP.remove(token);
        EMITTER_MAP.put(token, emitter);

        emitter.onCompletion(() -> remove(token, "连接完成（正常关闭）"));
        emitter.onTimeout(() -> remove(token, "连接超时"));
        emitter.onError(e -> remove(token, "连接出错：" + e.getMessage()));

        log("✅ 新建连接 token=" + token);

        // 可选：初次推送一个连接成功事件
        try {
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data("SSE 已建立连接，token=" + token));
        } catch (IOException e) {
            log("❌ 发送连接事件失败：" + e.getMessage());
        }

        return emitter;
    }

    /**
     * 向指定 token 发送数据（默认事件名：message）
     */
    public static boolean send(String token, Object data) {
        return send(token, "message", data);
    }

    /**
     * 向指定 token 发送指定事件
     */
    public static boolean send(String token, String eventName, Object data) {
        SseEmitter emitter = EMITTER_MAP.get(token);
        if (emitter == null) {
            log("⚠️ 未找到 token=" + token + " 的连接");
            return false;
        }

        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
            return true;
        } catch (IOException e) {
            log("❌ 向 token=" + token + " 发送失败：" + e.getMessage());
            remove(token, "发送异常，已移除连接");
            return false;
        }
    }

    /**
     * 广播消息给所有 token
     */
    public static void broadcast(Object data) {
        broadcast("message", data);
    }

    /**
     * 广播指定事件
     */
    public static void broadcast(String eventName, Object data) {
        EMITTER_MAP.forEach((token, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                log("❌ 广播失败（token=" + token + "）：" + e.getMessage());
                remove(token, "广播异常");
            }
        });
    }

    /**
     * 主动移除指定连接
     */
    public static void remove(String token) {
        remove(token, "主动移除");
    }

    /**
     * 内部移除并打印日志
     */
    private static void remove(String token, String reason) {
        EMITTER_MAP.remove(token);
        log("🧹 移除连接 token=" + token + "（" + reason + "）");
    }

    /**
     * 当前在线连接数
     */
    public static int onlineCount() {
        return EMITTER_MAP.size();
    }

    /**
     * 打印日志（可替换成日志框架）
     */
    private static void log(String msg) {
        System.out.println("[SSE] " + msg);
    }
}
