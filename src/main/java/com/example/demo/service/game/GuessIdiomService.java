package com.example.demo.service.game;

import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesis;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisParam;
import com.alibaba.dashscope.aigc.imagesynthesis.ImageSynthesisResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.example.demo.model.GuessIdiomRequest;
import com.example.demo.model.GuessIdiomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 看图识成语服务
 */
@Service
@Slf4j
public class GuessIdiomService {

    @Value("${dashscope.api.key:}")
    private String apiKey;

    // 成语数据库 - 包含成语及其含义
    private static final Map<String, String> IDIOM_MEANINGS = new HashMap<>();
    
    static {
        IDIOM_MEANINGS.put("老马识途", "比喻有经验的人对事情比较熟悉");
        IDIOM_MEANINGS.put("画蛇添足", "比喻做了多余的事，非但无益，反而不合适");
        IDIOM_MEANINGS.put("井底之蛙", "比喻见识狭窄的人");
        IDIOM_MEANINGS.put("狐假虎威", "比喻依仗别人的权势来欺压、恐吓人");
        IDIOM_MEANINGS.put("守株待兔", "比喻不主动努力，而存万一的侥幸心理，希望得到意外的收获");
        IDIOM_MEANINGS.put("亡羊补牢", "比喻出了问题以后想办法补救，可以防止继续受损失");
        IDIOM_MEANINGS.put("买椟还珠", "比喻没有眼力，取舍不当");
        IDIOM_MEANINGS.put("掩耳盗铃", "比喻自己欺骗自己，明明掩盖不住的事情偏要想法子掩盖");
        IDIOM_MEANINGS.put("刻舟求剑", "比喻不懂事物已发展变化而仍静止地看问题");
        IDIOM_MEANINGS.put("南辕北辙", "比喻行动和目的正好相反");
        IDIOM_MEANINGS.put("滥竽充数", "比喻无本领的冒充有本领，次货冒充好货");
        IDIOM_MEANINGS.put("杞人忧天", "比喻不必要的或缺乏根据的忧虑和担心");
        IDIOM_MEANINGS.put("叶公好龙", "比喻口头上说爱好某事物，实际上并不真爱好");
        IDIOM_MEANINGS.put("塞翁失马", "比喻一时虽然受到损失，也许反而因此能得到好处");
        IDIOM_MEANINGS.put("破釜沉舟", "比喻下决心不顾一切地干到底");
        IDIOM_MEANINGS.put("卧薪尝胆", "比喻立志图强、奋发图强的坚强意志");
        IDIOM_MEANINGS.put("鸟语花香", "鸟叫得好听，花开得喷香。形容春天的美好景象");
        IDIOM_MEANINGS.put("龙腾虎跃", "比喻军队士气旺盛，像龙马腾跃一样有气势");
        IDIOM_MEANINGS.put("凤凰于飞", "比喻夫妻相亲相爱。常用以祝人婚姻美满");
        IDIOM_MEANINGS.put("鱼目混珠", "比喻用假的冒充真的");
    }

    /**
     * 随机获取一个成语
     */
    private String getRandomIdiom() {
        List<String> idioms = new ArrayList<>(IDIOM_MEANINGS.keySet());
        Random random = new Random();
        return idioms.get(random.nextInt(idioms.size()));
    }

    /**
     * 生成提示词
     */
    private String generatePrompt(String idiom) {
        return String.format("请你以简约手绘，易于理解，的风格来画一幅寓意为\"%s\"的图片，不要出现\"%s\"的文字,ASCII art", 
                           idiom, idiom);
    }

    /**
     * 生成看图识成语图片
     */
    public GuessIdiomResponse generateIdiomImage(GuessIdiomRequest request) {
        GuessIdiomResponse response = new GuessIdiomResponse();
        
        try {
            // 确定要使用的成语
            String idiom = request.getIdiom();
            if (idiom == null || idiom.trim().isEmpty()) {
                idiom = getRandomIdiom();
            }
            
            // 验证成语是否在我们的数据库中
            if (!IDIOM_MEANINGS.containsKey(idiom)) {
                response.setSuccess(false);
                response.setErrorMessage("不支持的成语: " + idiom);
                return response;
            }
            
            // 生成提示词
            String prompt = generatePrompt(idiom);
            
            // 调用阿里通义万相API
            String effectiveApiKey = apiKey != null && !apiKey.isEmpty() ? 
                                   apiKey : System.getenv("DASHSCOPE_API_KEY");
            
            if (effectiveApiKey == null || effectiveApiKey.isEmpty()) {
                response.setSuccess(false);
                response.setErrorMessage("API密钥未配置，请设置DASHSCOPE_API_KEY环境变量或配置文件中的dashscope.api.key");
                return response;
            }
            
            ImageSynthesisParam param = ImageSynthesisParam.builder()
                    .apiKey(effectiveApiKey)
                    .model("wanx2.1-t2i-turbo")
                    .prompt(prompt)
                    .n(request.getCount())
                    .size(request.getSize())
                    .build();

            ImageSynthesis imageSynthesis = new ImageSynthesis();
            ImageSynthesisResult result = imageSynthesis.call(param);
            
            if (result.getOutput() != null && result.getOutput().getResults() != null) {
                List<String> imageUrls = new ArrayList<>();
                result.getOutput().getResults().forEach(imgResult -> {
                    if (imgResult.get("url") != null) {
                        imageUrls.add(imgResult.get("url"));
                    }
                });
                
                response.setSuccess(true);
                response.setIdiom(idiom);
                response.setMeaning(IDIOM_MEANINGS.get(idiom));
                response.setImageUrls(imageUrls);
                response.setPrompt(prompt);
                
                log.info("成功为成语 '{}' 生成了 {} 张图片", idiom, imageUrls.size());
            } else {
                response.setSuccess(false);
                response.setErrorMessage("图片生成失败，返回结果为空");
            }
            
        } catch (ApiException e) {
            log.error("调用通义万相API时发生错误", e);
            response.setSuccess(false);
            response.setErrorMessage("API调用失败: " + e.getMessage());
        } catch (NoApiKeyException e) {
            log.error("API密钥未配置", e);
            response.setSuccess(false);
            response.setErrorMessage("API密钥未配置");
        } catch (Exception e) {
            log.error("生成成语图片时发生未知错误", e);
            response.setSuccess(false);
            response.setErrorMessage("系统错误: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * 获取所有支持的成语列表
     */
    public Map<String, String> getAllIdioms() {
        return new HashMap<>(IDIOM_MEANINGS);
    }

    /**
     * 获取随机成语（不生成图片）
     */
    public Map<String, String> getRandomIdiomInfo() {
        String idiom = getRandomIdiom();
        Map<String, String> result = new HashMap<>();
        result.put("idiom", idiom);
        result.put("meaning", IDIOM_MEANINGS.get(idiom));
        return result;
    }
} 