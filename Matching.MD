<!--
 * @Author: hiddenSharp429 z404878860@163.com
 * @Date: 2024-11-15 08:48:59
 * @LastEditors: hiddenSharp429 z404878860@163.com
 * @LastEditTime: 2024-11-15 09:01:19
-->
# 匹配系统

## 匹配流程

1. 玩家发起匹配请求
```
sequenceDiagram
    Client->>MatchmakingController: 发送匹配请求
    Note over MatchmakingController: /app/matchmaking/queue
    MatchmakingController->>MatchmakingService: addToQueue(request)
```

2. 匹配逻辑处理
```java
public void addToQueue(MatchRequest request) {
    // 1. 生成队列键（基于语言、类型、难度）
    String queueKey = generateQueueKey(request.getLanguage(), request.getCategory(), request.getDifficulty());
    
    // 2. 获取或创建对应的匹配队列
    Queue<MatchRequest> queue = matchmakingQueues.computeIfAbsent(queueKey, k -> new ConcurrentLinkedQueue<>());
    
    // 3. 尝试查找匹配的对手
    MatchRequest opponent = findMatch(queue, request);
    
    if (opponent != null) {
        // 4a. 找到对手，创建房间
        createMatchedRoom(opponent, request);
    } else {
        // 4b. 没找到对手，加入等待队列
        queue.offer(request);
    }
}
```

3. 匹配成功流程
```
sequenceDiagram
    MatchmakingService->>GameRoomService: createRoom(roomId, language, category, difficulty)
    GameRoomService-->>MatchmakingService: return new GameRoom
    MatchmakingService->>Client1: 通知玩家1匹配成功(roomId)
    MatchmakingService->>Client2: 通知玩家2匹配成功(roomId)
    Note over Client1,Client2: 通过 /queue/matchmaking/{playerId}
```


4.取消匹配流程
```
sequenceDiagram
    Client->>MatchmakingController: 发送取消请求
    Note over MatchmakingController: /app/matchmaking/cancel
    MatchmakingController->>MatchmakingService: removeFromQueue(request)
    MatchmakingService->>Client: 通知取消成功
    Note over Client: 通过 /queue/matchmaking/{playerId}
```
5. 前端订阅和处理
```javascript
// 订阅个人匹配消息
stompClient.subscribe('/queue/matchmaking/' + playerId, function(response) {
    const message = JSON.parse(response.body);
    
    switch(message.type) {
        case "MATCH_FOUND":
            // 匹配成功，加入房间
            joinRoom(message.roomId);
            break;
            
        case "MATCH_CANCELLED":
            // 匹配已取消
            handleMatchCancelled();
            break;
    }
});

// 发起匹配
function startMatching() {
    stompClient.send("/app/matchmaking/queue", {}, JSON.stringify({
        playerId: "player123",
        playerName: "Player One",
        language: "ENGLISH",
        category: "PROGRAMMING",
        difficulty: "MEDIUM",
        timestamp: Date.now()
    }));
}

// 取消匹配
function cancelMatching() {
    stompClient.send("/app/matchmaking/cancel", {}, JSON.stringify({
        playerId: "player123",
        language: "ENGLISH",
        category: "PROGRAMMING",
        difficulty: "MEDIUM"
    }));
}
```
## 可拓展点
- 添加匹配超时机制
- 实现模糊匹配（放宽条件）
- 添加技能评级匹配
- 添加黑名单机制