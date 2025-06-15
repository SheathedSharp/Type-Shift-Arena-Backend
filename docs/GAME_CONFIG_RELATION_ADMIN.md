# 游戏配置关系管理界面

## 🛠️ 常用操作示例

### 1. 添加单个关系
```javascript
// 为 "经典模式" 添加 "中文" 支持
await axios.post('/api/game-config/relation-management/mode-language', null, {
  params: {
    modeName: 'classic',
    languageName: 'chinese'
  }
})
```

### 2. 删除单个关系
```javascript
// 删除 "经典模式" 的 "英文" 支持
await axios.delete('/api/game-config/relation-management/mode-language', {
  params: {
    modeName: 'classic',
    languageName: 'english'
  }
})
```

### 3. 批量设置关系
```javascript
// 为 "竞技模式" 设置支持的语言
await axios.put('/api/game-config/relation-management/mode/competitive/languages', [
  'chinese',
  'english',
  'japanese'
])
```

### 4. 重建配置组合
```javascript
// 重新生成所有有效的配置组合
const response = await axios.post('/api/game-config/relation-management/rebuild-combinations')
console.log(`生成了 ${response.data.generatedCombinations} 个有效组合`)
```

## 📊 管理流程建议

### 1. 关系修改流程
1. **查看当前关系**: 使用关系查看API了解现状
2. **修改关系**: 使用关系管理API进行增删改
3. **重建组合**: 调用重建API生成新的配置组合
4. **刷新缓存**: 清空缓存并重新预热
5. **验证结果**: 测试前端配置选择功能

### 2. 批量操作建议
```javascript
// 完整的批量更新流程
async function updateGameConfig() {
  try {
    // 1. 批量设置关系
    await setBatchRelations()
    
    // 2. 重建配置组合
    await axios.post('/api/game-config/relation-management/rebuild-combinations')
    
    // 3. 刷新缓存
    await axios.post('/api/game-config/relation-management/refresh-cache')
    
    // 4. 验证结果
    const combinations = await axios.get('/api/game-config/combinations')
    console.log('更新完成，当前有效组合数:', combinations.data.data.length)
    
  } catch (error) {
    console.error('批量更新失败:', error)
  }
}
```

## 🚀 性能优化建议

### 1. 缓存策略
- 每次关系修改后自动清空相关缓存
- 重建组合后自动预热常用配置
- 定期刷新缓存避免数据不一致

### 2. 操作频率控制
```javascript
// 防抖处理，避免频繁操作
const debouncedUpdate = debounce(async (modeName, languageName, enabled) => {
  await toggleModeLanguageRelation(modeName, languageName, enabled)
}, 500)
```

### 3. 批量操作优先
- 大量关系修改时优先使用批量API
- 避免频繁的单个操作导致性能问题
- 修改完成后统一重建和刷新缓存

## 📈 监控和日志

所有关系管理操作都会记录详细日志，包括：
- 操作类型和参数
- 操作结果和影响的记录数
- 错误信息和堆栈跟踪
- 缓存清理和重建过程