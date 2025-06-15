# 游戏配置关系管理界面

## 📋 关系管理API使用指南

### 🔧 API 端点

#### 1. 模式-语言关系管理

```javascript
// Vue.js 关系管理组件示例
<template>
  <div class="relation-management">
    <h2>游戏配置关系管理</h2>
    
    <!-- 模式-语言关系管理 -->
    <div class="section">
      <h3>模式-语言关系</h3>
      
      <!-- 添加关系 -->
      <div class="add-relation">
        <el-select v-model="newRelation.modeName" placeholder="选择模式">
          <el-option 
            v-for="mode in modes" 
            :key="mode.name" 
            :label="mode.displayName" 
            :value="mode.name">
          </el-option>
        </el-select>
        
        <el-select v-model="newRelation.languageName" placeholder="选择语言">
          <el-option 
            v-for="language in languages" 
            :key="language.name" 
            :label="language.displayName" 
            :value="language.name">
          </el-option>
        </el-select>
        
        <el-button @click="addModeLanguageRelation" type="primary">添加关系</el-button>
      </div>
      
      <!-- 关系列表 -->
      <div class="relation-matrix">
        <table class="matrix-table">
          <thead>
            <tr>
              <th>模式 \ 语言</th>
              <th v-for="language in languages" :key="language.name">
                {{ language.displayName }}
              </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="mode in modes" :key="mode.name">
              <td>{{ mode.displayName }}</td>
              <td v-for="language in languages" :key="language.name">
                <el-switch
                  v-model="relationMatrix[mode.name][language.name]"
                  @change="toggleModeLanguageRelation(mode.name, language.name, $event)"
                  active-color="#13ce66"
                  inactive-color="#ff4949">
                </el-switch>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- 批量设置 -->
      <div class="batch-setting">
        <h4>批量设置模式支持的语言</h4>
        <el-select v-model="batchMode" placeholder="选择模式">
          <el-option 
            v-for="mode in modes" 
            :key="mode.name" 
            :label="mode.displayName" 
            :value="mode.name">
          </el-option>
        </el-select>
        
        <el-select 
          v-model="batchLanguages" 
          multiple 
          placeholder="选择支持的语言">
          <el-option 
            v-for="language in languages" 
            :key="language.name" 
            :label="language.displayName" 
            :value="language.name">
          </el-option>
        </el-select>
        
        <el-button @click="setBatchModeLanguages" type="success">批量设置</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'GameConfigRelationManagement',
  data() {
    return {
      modes: [],
      languages: [],
      categories: [],
      difficulties: [],
      relationMatrix: {},
      newRelation: {
        modeName: '',
        languageName: ''
      },
      batchMode: '',
      batchLanguages: []
    }
  },
  
  async mounted() {
    await this.loadConfigData()
    await this.loadRelationMatrix()
  },
  
  methods: {
    // 加载基础配置数据
    async loadConfigData() {
      try {
        const [modesRes, languagesRes, categoriesRes, difficultiesRes] = await Promise.all([
          axios.get('/api/game-config/modes'),
          axios.get('/api/game-config/languages'),
          axios.get('/api/game-config/categories'),
          axios.get('/api/game-config/difficulties')
        ])
        
        this.modes = modesRes.data.data || []
        this.languages = languagesRes.data.data || []
        this.categories = categoriesRes.data.data || []
        this.difficulties = difficultiesRes.data.data || []
        
      } catch (error) {
        this.$message.error('加载配置数据失败')
        console.error(error)
      }
    },
    
    // 加载关系矩阵
    async loadRelationMatrix() {
      try {
        const response = await axios.get('/api/game-config/relations/matrix')
        this.relationMatrix = response.data.data.modeLanguageMatrix || {}
        
      } catch (error) {
        this.$message.error('加载关系矩阵失败')
        console.error(error)
      }
    },
    
    // 添加模式-语言关系
    async addModeLanguageRelation() {
      if (!this.newRelation.modeName || !this.newRelation.languageName) {
        this.$message.warning('请选择模式和语言')
        return
      }
      
      try {
        const response = await axios.post('/api/game-config/relation-management/mode-language', null, {
          params: {
            modeName: this.newRelation.modeName,
            languageName: this.newRelation.languageName
          }
        })
        
        if (response.data.success) {
          this.$message.success(response.data.message)
          await this.loadRelationMatrix()
          this.newRelation = { modeName: '', languageName: '' }
        } else {
          this.$message.warning(response.data.message)
        }
        
      } catch (error) {
        this.$message.error('添加关系失败')
        console.error(error)
      }
    },
    
    // 切换模式-语言关系
    async toggleModeLanguageRelation(modeName, languageName, enabled) {
      try {
        let response
        
        if (enabled) {
          // 添加关系
          response = await axios.post('/api/game-config/relation-management/mode-language', null, {
            params: { modeName, languageName }
          })
        } else {
          // 删除关系
          response = await axios.delete('/api/game-config/relation-management/mode-language', {
            params: { modeName, languageName }
          })
        }
        
        if (response.data.success) {
          this.$message.success(response.data.message)
        } else {
          this.$message.warning(response.data.message)
          // 还原状态
          this.relationMatrix[modeName][languageName] = !enabled
        }
        
      } catch (error) {
        this.$message.error('操作失败')
        this.relationMatrix[modeName][languageName] = !enabled
        console.error(error)
      }
    },
    
    // 批量设置模式语言
    async setBatchModeLanguages() {
      if (!this.batchMode || !this.batchLanguages.length) {
        this.$message.warning('请选择模式和语言')
        return
      }
      
      try {
        const response = await axios.put(
          `/api/game-config/relation-management/mode/${this.batchMode}/languages`,
          this.batchLanguages
        )
        
        if (response.data.success) {
          this.$message.success(response.data.message)
          await this.loadRelationMatrix()
          this.batchMode = ''
          this.batchLanguages = []
        } else {
          this.$message.warning(response.data.message)
        }
        
      } catch (error) {
        this.$message.error('批量设置失败')
        console.error(error)
      }
    },
    
    // 重建配置组合
    async rebuildCombinations() {
      try {
        const response = await axios.post('/api/game-config/relation-management/rebuild-combinations')
        
        if (response.data.success) {
          this.$message.success(`${response.data.message}，生成了 ${response.data.generatedCombinations} 个有效组合`)
        } else {
          this.$message.error(response.data.message)
        }
        
      } catch (error) {
        this.$message.error('重建配置组合失败')
        console.error(error)
      }
    },
    
    // 刷新缓存
    async refreshCache() {
      try {
        const response = await axios.post('/api/game-config/relation-management/refresh-cache')
        
        if (response.data.success) {
          this.$message.success(response.data.message)
        } else {
          this.$message.error(response.data.message)
        }
        
      } catch (error) {
        this.$message.error('刷新缓存失败')
        console.error(error)
      }
    }
  }
}
</script>

<style scoped>
.relation-management {
  padding: 20px;
}

.section {
  margin-bottom: 40px;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.add-relation {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}

.matrix-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.matrix-table th,
.matrix-table td {
  border: 1px solid #e4e7ed;
  padding: 8px;
  text-align: center;
}

.matrix-table th {
  background-color: #f5f7fa;
  font-weight: bold;
}

.batch-setting {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-wrap: wrap;
}

.batch-setting h4 {
  width: 100%;
  margin-bottom: 10px;
}
</style>
```

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

通过日志可以追踪所有配置变更历史，便于问题排查和回滚操作。 