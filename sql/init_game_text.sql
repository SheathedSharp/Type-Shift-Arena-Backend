USE myappdb;

INSERT INTO game_texts (
    id,
    title, 
    content, 
    source_title, 
    source_author, 
    language, 
    category, 
    difficulty, 
    is_custom
) VALUES 
(
    UUID(),
    '贾宝玉与林黛玉的别离',
    '贾宝玉凝视着林黛玉，心中波涛汹涌。他看见她眼中盈满泪水，似乎要开口说什么，却终究咽了下去。"宝玉哥哥，"林黛玉轻声说道，"我这一生，除了这满天星斗，似乎再无什么能诉说我的心事了。"贾宝玉一时语塞，天地间只剩风声掠过树梢，连蝉鸣都沉寂了。他缓缓伸出手，想抚去她脸上的泪痕，却发现她轻轻后退了一步，仿佛这一瞬间，两人之间的距离比天地还要遥远。此刻，他才真切地感受到，他们之间的情感，注定要被世俗的枷锁紧紧束缚，难以冲破。',
    '红楼梦',
    '曹雪芹',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '草船借箭',
    '诸葛亮笑看江面，水雾弥漫之中，敌船已隐约可见。他转头对鲁肃道："此计成败，全凭天助与对方无知。"果然，曹操的将士在雾中看见江中船只，慌忙下令射箭。箭雨如骤，射向诸葛亮的草船。诸葛亮从容自若，命士兵敲锣呐喊，装作军势雄壮之势。雾渐散，草船甲板上的万千羽箭，熠熠生辉。诸葛亮悠然一笑，仿佛一切尽在掌握之中。鲁肃叹道："先生之智，实在让人佩服得五体投地！"',
    '三国演义',
    '罗贯中',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '林冲风雪山神庙',
    '林冲孤身一人，冒着风雪踏入山神庙。他将油灯点燃，微弱的火光映照出满是裂痕的神像。外面寒风呼啸，积雪飞舞，而庙内却显得更加阴森。"林教头，今日便是你的末日！"突然，门外传来狞笑声，几个刺客闯入庙内。林冲见状，怒火中烧，大喝一声，手持长枪迎敌。他身影如电，枪势如虹，将刺客逐一击退。风雪之夜，山神庙成了他与命运抗争的舞台，林冲的血性与悲凉交织，铸成了英雄的孤独背影。',
    '水浒传',
    '施耐庵',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '孙悟空三打白骨精',
    '孙悟空眼见白骨精再度幻化成人形，不禁怒火中烧。他手持金箍棒，喝道："妖孽，还不现原形！"白骨精假作哀求："好汉饶命！小女子并无恶意。"唐僧却不知真相，叱责悟空道："休要造次！"悟空冷笑一声："师父，不是俺老孙心狠，而是你执迷不悟！"说罢，他凌空跃起，金箍棒劈风斩下。顷刻间，妖气散去，白骨精的原形暴露无遗。悟空转头看向师父，目光复杂，似在叹息命运的不公。',
    '西游记',
    '吴承恩',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '聂小倩',
    '宁采臣夜宿荒庙，忽见一女子飘然而入，姿容绝美，目光却透着凄楚。她轻声道："公子莫怕，我只是个无处栖身的弱女子。"宁采臣虽感惊异，却见她言行有礼，便放下戒心。聂小倩为他添薪续火，闲谈之中流露出对人间温情的渴望。然而，深夜之中，忽有冷风呼啸，庙外传来怪声。宁采臣猛然醒悟，这女子并非凡人。最终，他以诚挚之心感化了她，使得幽魂得以解脱，展现了人性善良与爱意的力量。',
    '聊斋志异',
    '蒲松龄',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '范进中举',
    '范进捧着中举的告示，双手颤抖，目光呆滞，嘴角露出无法抑制的微笑。乡邻们簇拥而至，七嘴八舌地恭贺，他却仿佛未听见，脑中一片空白。"范老爷，中举啦！"有人高呼。他猛然清醒，却因过度激动而跌倒在地，竟发疯似的笑个不停。家人手忙脚乱地扶起他，他却仍语无伦次。中举的喜悦虽如梦成真，但背后是寒窗苦读的代价，以及功名利禄对人性的深刻讽刺。',
    '儒林外史',
    '吴敬梓',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '觉慧的觉醒',
    '觉慧倚在窗前，凝望着家中繁琐的礼教规矩，他的心中掀起一阵阵的波澜。他暗自思忖："难道我们这一代，也要屈服于这样的枷锁之下吗？"他回想起琴的眼神，那是对自由与幸福的向往，而这样的情感在这座大宅院中却显得弥足珍贵。他忽然感到一种勇气涌上心头，决定摆脱这无形的牢笼。他的觉醒，不仅是个人命运的挣扎，更是对整个旧社会价值观的反抗。',
    '家',
    '巴金',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '祥子的梦碎',
    '祥子望着破旧的骆驼，眼中满是愤怒与无奈。他曾梦想靠自己的努力换来一辆车，过上安稳的生活，但现实一次次将他推向深渊。他的车被抢，他的积蓄被骗，甚至连最亲近的人也离他而去。城市的灯火辉煌，却与他无关。他愤然道："人活着，怎么就这么难？"祥子的挣扎，是那个时代普通劳动者对抗命运的缩影，却也充满了悲哀的无力感。',
    '骆驼祥子',
    '老舍',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '翠翠的等待',
    '翠翠站在渡口，目送船只远去，夕阳的余晖洒在她的脸上。她的心中满是期盼，盼着那人回来，盼着一个承诺。然而，流水无情，岁月静默，她却从未停止等待。每一个清晨，她都抱着希望，每一个黄昏，又带着失落。她的纯真与坚韧，如这座边城的小河，永远流淌，却也永远无法回头。这等待，既是对爱的忠贞，也是对命运的无声抗争。',
    '边城',
    '沈从文',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '周家的命运交响',
    '周朴园冷冷地注视着一切，他的沉默如同压在每个人心头的石块。繁漪的愤怒、四凤的哀怨、周萍的挣扎，这一家人的矛盾在雷雨之夜彻底爆发。电闪雷鸣中，真相如骤雨倾盆，无情地揭开了伪装的面具。周家每个人都是命运的囚徒，无法摆脱社会与家庭的枷锁。这个夜晚，如一场悲剧的交响，将每个灵魂的痛苦演绎到极致。',
    '雷雨',
    '曹禺',
    'CHINESE',
    'LITERATURE',
    'EASY',
    FALSE
),

(
    UUID(),
    '黛玉葬花',
    '林黛玉手捧一篮花瓣，步履轻缓地走向园中小溪。今日的天色阴郁，微风吹拂，她的身影如一幅淡墨画般清冷。她蹲下身来，将花瓣一片一片地撒入水中，嘴里喃喃道：“花谢花飞飞满天，红消香断有谁怜？”她轻叹一声，拿起丝帕拭去泪痕，心中无限感伤。花虽美，却终归一日要凋零，就如她自己，虽有一腔才情，却不被命运所眷顾。她缓缓站起身，环顾四周，发现满园繁华，却无人能真正懂得她的孤独与哀愁。贾宝玉的温情虽让她感到一丝慰藉，但她明白，自己的命运无法由自己掌控。一阵微风吹过，柳絮飘扬，她伸手轻轻一抓，却抓不住任何实物，就如她抓不住自己的未来。她喃喃道：“侬今葬花人笑痴，他年葬侬知是谁？”一股悲凉涌上心头，她的世界似乎只有花瓣与流水能与她共鸣。然而，黛玉终究是倔强的。她用尽一生的孤傲来守护自己的灵魂，不愿向世俗低头。她抚摸着手中的花篮，仿佛抚摸着自己残存的尊严。那一刻，她的背影消融在园林的寂静中，成为一个永恒的象征，象征着一个无法被理解的灵魂与命运的抗争。',
    '红楼梦',
    '曹雪芹',
    'CHINESE',
    'LITERATURE',
    'MEDIUM',
    FALSE
),

(
    UUID(), 
    '舌战群儒',
    '诸葛亮稳坐上席，面色从容，对面的曹营众谋士却神情各异，有的目露凶光，有的冷笑不语。曹丕开口道：“听闻诸葛丞相能言善辩，不知今日是否能胜过我曹营诸位贤士？”诸葛亮微微一笑，站起身道：“既然如此，亮倒愿聆听高见。”荀攸首先开口：“丞相虽有才情，但蜀地贫瘠，兵少粮缺，如何能与我曹魏抗衡？”诸葛亮闻言朗声笑道：“曹公地广兵强，然为何十年尚不能平定天下？我蜀虽小，却占天时地利人和，此长彼短，自是各有胜负。”荀攸一时语塞，满堂皆惊。郭嘉接过话头，冷冷说道：“天时地利人和，皆是虚无缥缈之论。丞相若倚此为胜机，未免过于乐观。”诸葛亮眯眼而笑：“郭祭酒所言不无道理，然请问，若无天时，曹公何以于官渡一战中火烧袁绍粮草？若无地利，曹营又何以坚守许都？若无人和，曹公又何以收服如此众多贤士？天时地利人和，乃万事之基，不可轻视。”郭嘉顿时哑口无言。众谋士你一言我一语，轮番攻讦诸葛亮，而他始终镇定自若，以冷静的分析和精准的言辞将对方逐一驳回。曹丕不禁赞道：“真乃舌战群儒，妙哉！”这一场辩论，不仅展现了诸葛亮的智谋与语言艺术，更彰显了他对天下大势的深刻洞察。',
    '三国演义',
    '罗贯中',
    'CHINESE',
    'LITERATURE',
    'MEDIUM',
    FALSE
),

(
    UUID(),
    '贾宝玉梦游太虚幻境',
    '那一晚，贾宝玉独自躺在卧榻上，辗转反侧，难以入眠。他仿佛听见远处传来隐隐约约的琴声，清幽婉转，仿若天籁。他心中一动，起身循着琴声走去，推开门，却发现一片奇异的景象：周围群山环绕，云雾缭绕，远处隐约可见一座巍峨的宫殿，宫门上书“太虚幻境”四个大字。他步履轻盈地走进大门，只见一位身着白衣的仙女缓缓走来，盈盈一拜，道：“宝玉少爷，快请随我入内，幻境主人有请。”宝玉不明所以，但心中好奇，便随仙女而行。一路上，他看见亭台楼阁鳞次栉比，庭院间花木繁盛，清溪蜿蜒，宛如仙境。进得内殿，只见一位美艳的女子端坐高堂，神情庄重，目光中却带着一丝慈悲。她微微一笑，道：“宝玉，世间诸般荣华富贵，不过过眼云烟；人间情爱，亦是苦多乐少。今日让你一见，望你日后能有所参悟。”话音刚落，一幅巨大的画卷自空中缓缓展开，画中所现，竟是贾府众人的未来命运。宝玉看得心惊肉跳：林黛玉病逝，泪尽而亡；薛宝钗寡居冷清；贾府衰败，昔日的繁华烟消云散。他心中痛如刀割，不禁跪倒在地，泪如雨下：“若真是如此，何必让我经历这一切？我宁愿不生在这世上！”女子叹息一声，轻挥衣袖，画卷消失无踪。她说道：“命运如这太虚幻境，看似真实，实则虚妄。世间情缘，若不能放下，便只能陷于无尽的苦海。去吧，宝玉，若有一日你能彻悟，这一切自然明白。”宝玉如梦初醒，回到卧榻上，额头冷汗淋漓。他望着窗外的夜色，心中久久不能平静。那一刻，他似乎明白了一些，却又仿佛什么都没有明白。这段太虚幻境的经历，不仅是宝玉内心挣扎与追问的写照，也是对人生虚实难辨的深刻寓言。曹雪芹通过这一场梦境，揭示了人世间繁华与虚妄的本质，让读者感叹生命的无常与命运的无奈。',
    '红楼梦',
    '曹雪芹',
    'CHINESE',
    'LITERATURE',
    'HARD',
    FALSE
),

(
    UUID(),
    '翠翠的孤独等待',
    '月光如水，洒在静谧的溪水边，翠翠坐在渡船旁，低头摆弄着手中的花环。她的眼神透着一丝忧郁，似是在等待，又似乎早已明白等待不过是一种无望的执念。爷爷的离世让她独自承担起了渡船的重担，而天保与傩送兄弟的离开则让她的内心多了一份难以言说的空虚。她想起了那些美好的日子，傩送在河边放歌，天保憨厚地划船，爷爷慈祥地看着她，笑声在清晨的河面上飘荡。一切都那么自然，那么温暖。可如今，时光如河水般流逝，带走了所有的欢愉，剩下的只有无尽的孤独和对未来的迷茫。村里人时常议论翠翠的命运，有人劝她早早嫁人，有人替她惋惜。可翠翠始终坚持着自己的执念，等一个不会归来的身影。她明白，傩送或许永远不会再出现，但那份深藏于心的情感，已成为她生命的一部分，无法割舍。夜深了，翠翠拖着疲惫的身体回到渡口的小屋。她坐在床边，望着窗外闪烁的星光，默默祈祷着某一天，那熟悉的脚步声能再次响起。她知道，这样的等待或许没有尽头，但她依然愿意守在这片宁静的边城，守在这条流淌的溪水旁，因为这里藏着她全部的回忆与希望。《边城》中翠翠的等待，是一种近乎信仰的坚持。她的孤独不仅是个人情感的困顿，也是对人性纯洁与爱的执着的隐喻。沈从文用细腻的笔触，将这片土地上的人情世故与美丽哀愁描绘得淋漓尽致，让人感受到时间流逝中的深沉悲凉。',
    '边城',
    '沈从文',
    'CHINESE',
    'LITERATURE',
    'HARD',
    FALSE
),

(
    UUID(),
    '街边偶遇——“你最近怎么样？”',
    '“哎，小张，好久不见，你最近怎么样？”小李笑着挥手打招呼。“挺好的，就是工作忙，没时间聚了。”小张叹气。“别太拼，身体重要啊！有空我们再约吃饭。”小李拍拍他的肩膀。“好啊，到时候带上家人，一起热闹热闹！”两人相视一笑，继续各自忙碌。',
    '日常生活',
    '未知',
    'CHINESE',
    'DAILY_CHAT',
    'EASY',
    FALSE
),

(
    UUID(),
    '菜市场对话——“今天的菜真新鲜”',
    '“老板，这青菜怎么卖啊？”李大妈指着一篮子青翠欲滴的菜问道。“2块一斤，刚从地里摘下来的！”老板热情地回答。“那给我来两斤，这个天气，吃点新鲜蔬菜对身体好。”李大妈笑着递过篮子，老板麻利地称重。菜市场里人声鼎沸，充满了生活的气息。',
    '日常生活',
    '未知',
    'CHINESE',
    'DAILY_CHAT',
    'EASY',
    FALSE
),

(
    UUID(),
    '公交车上的闲聊——“天气真热啊”',
    '“今天真热，你看这太阳，像个火球。”阿姨拿着扇子不停地扇风。“是啊，这才早上就三十多度了，下午不得四十啊！”旁边的大叔摇头叹气。“还好公交车有空调，不然真受不了。”大家纷纷点头附和，车厢里充满了夏天的吐槽和些许凉意。',
    '日常生活',
    '未知',
    'CHINESE',
    'DAILY_CHAT',
    'EASY',
    FALSE
),

(
    UUID(),
    '邻里之间——“谁家阳台上的花又开了”',
    '午后的阳光洒在社区的小花园里，几个邻居聚在一处，拉着家常。李阿姨指着楼上的阳台，笑着说：“哎呀，你们瞧，张大爷家那盆三角梅又开花了，真好看！”王奶奶抬头看了一眼，接过话头：“是呀，张大爷这花养得真有一套。前阵子我家的绿萝都快死了，还得亏他给指了路子，用点淘米水浇，果然绿得发亮。”“张大爷的手巧着呢，他那阳台跟个小花园似的，什么月季、栀子花，年年开得好！我前几天还跟他说，教教我怎么弄，可他就说多浇水、多晒太阳，嘿，这话说了等于没说。”张阿姨笑得前仰后合。李阿姨补充道：“人家是心里有数！你不知道，他平时可是有时间就上阳台伺候那些花。我家老李还笑话人家，说他这是比种地还上心。”几人正聊得热闹，张大爷从小区门口拎着一袋肥料进来，笑呵呵地打趣：“哟，说我坏话呢？这不，又买点肥料回来给你们‘观摩用’。”大家顿时哄笑起来，张大爷也乐得合不拢嘴。这样的对话，虽然平常，却流露出邻里间的和睦与温馨。花开花落的小事，成为大家生活中的点缀，也让平淡的日子增添了几分诗意。',
    '日常生活',
    '未知',
    'CHINESE',
    'DAILY_CHAT',
    'MEDIUM',
    FALSE
),

(
    UUID(),
    '亲友闲谈——“孩子上学的烦恼”',
    '“我跟你说，现在这孩子的学习啊，真是操不完的心。”刘姐一边倒茶，一边叹气，“昨天老师发了个通知，说要准备个手工作业，我家那小子光顾着打游戏，啥也没动。”王姐笑着接话：“哎哟，这不是挺正常嘛！我家那个上次班级搞活动，老师要家长帮忙做一份PPT，他倒好，什么都不管，结果我熬到凌晨才弄完，第二天还挨了个批评，说背景颜色选得不够‘生动’。”“哈哈哈，我家那姑娘更有意思。上次考试没考好，我跟她说，‘你咋不着急呢？’她反倒说，‘妈妈你着急就好啦！’我这心里别提多憋屈了。”刘姐感慨道：“其实现在的孩子啊，也挺不容易的。我们那会儿，哪有这么多课外班、兴趣班，放学回家随便玩都没人管。现在的孩子呢？每天一睁眼就得想着补习、作业，连周末都不带闲的。”王姐点头附和：“是啊，但不管怎么说，还是得多陪陪孩子，学习固然重要，健康成长更重要。”三人聊着聊着，茶杯中的水已凉了几分，但这一场谈话却拉近了彼此的距离，也为纷繁的生活增添了些许慰藉。',
    '日常生活',
    '未知',
    'CHINESE',
    'DAILY_CHAT',
    'MEDIUM',
    FALSE
),

(
    UUID(),
    '朋友聚会——“一起去吃火锅吧”',
    '“最近怎么样？工作还顺利吧？”小李一边倒着饮料，一边问道。“还行吧，就是年底了，事情多得不得了。每天忙到晚上十点，回家只想躺着。”小张无奈地叹了口气。“那今晚一定要好好放松一下！对了，咱们上次说好的火锅店，今天一起去呗。”小王兴致勃勃地提议。“火锅啊？可以啊！”小张来了点精神，“不过我最近胃有点不舒服，咱们点清汤底吧，别太辣。”“没问题，咱们这次就点个鸳鸯锅。”小李笑道，“再加几个你爱吃的虾滑、鱼丸，还有小张最喜欢的毛肚。”小王拍了拍小张的肩膀：“听着就饿了吧？别看你平时吃得少，每次吃火锅都抢着下菜，根本停不下来！”“那是火锅的魅力！”小张笑得露出虎牙，“再忙再累，只要吃一顿热气腾腾的火锅，什么烦恼都忘了。”“行，那就说定了！”小李挥了挥手，“我们去叫个车，今天咱们不醉不归！”三人相视一笑，举起饮料碰杯，热闹的气氛驱散了工作带来的疲惫，也让这场友谊更加温暖。在火锅的烟火气中，平凡的日子焕发出独特的光彩。',
    '日常生活',
    '未知',
    'CHINESE',
    'DAILY_CHAT',
    'MEDIUM',
    FALSE
),

(
    UUID(),
    'The Power of Resilience',
    'In the bleak isolation of Lowood School, Jane Eyre’s spirit remains unbroken despite the harsh treatment and deprivations she endures. She finds solace in friendship with Helen Burns, whose stoic acceptance of suffering deeply impacts her. Helen, wise beyond her years, tells Jane: “Life appears to me too short to be spent in nursing animosity or registering wrongs.” This perspective, in stark contrast to Jane’s fiery temperament, plants the seeds of inner strength and forgiveness. Despite Helen’s untimely death, her words linger in Jane’s mind as a guiding principle. Jane’s resolve grows, as does her independence, her sense of justice, and her pursuit of a life defined by integrity and love. This moment encapsulates not only the trials of a Victorian woman but the universal struggle for dignity and self-realization amidst life’s adversities.',
    'Jane Eyre',
    'Charlotte Brontë',
    'ENGLISH',
    'LITERATURE',
    'MEDIUM',
    FALSE
),

(
    UUID(),
    'The Nature of True Love',
    'Elizabeth Bennet’s confrontation with Mr. Darcy at Rosings Park marks a pivotal moment in *Pride and Prejudice*. Darcy confesses his love for Elizabeth, yet his proposal is laden with condescension, highlighting her "inferior" family connections. Elizabeth’s fiery rejection shocks Darcy and reveals her own strength of character: “From the very beginning, your arrogance, your conceit, and your selfish disdain for the feelings of others have built so immovable a barrier.” This clash of wills embodies Austen’s critique of class prejudice and gender roles. Elizabeth’s refusal to compromise her principles challenges the notion that women should accept advantageous matches at the expense of personal respect. Over time, both characters evolve—Darcy learns humility, and Elizabeth reconsiders her prejudices—showing that true love requires mutual growth and understanding.',
    'Pride and Prejudice',
    'Jane Austen',
    'ENGLISH',
    'LITERATURE',
    'MEDIUM',
    FALSE
),

(
    UUID(),
    'The Weight of Conscience',
    'Raskolnikov, tormented by the guilt of his crime, wanders the streets of St. Petersburg in a feverish haze. Dostoevsky captures the intensity of his internal struggle: “Pain and suffering are always inevitable for a large intelligence and a deep heart. The truly great men must, I think, have great sadness on earth.” These words encapsulate Raskolnikov’s justification of his act, believing himself above ordinary morality. However, the burden of conscience proves otherwise. His interactions with Sonia, whose selfless faith starkly contrasts his existential torment, become a beacon of redemption. Her unwavering belief in the power of suffering to cleanse the soul forces Raskolnikov to confront his humanity. This passage underscores Dostoevsky’s exploration of morality, redemption, and the psychological torment of guilt, making it a cornerstone of existential literature.',
    'Crime and Punishment',
    'Fyodor Dostoevsky',
    'ENGLISH',
    'LITERATURE',
    'MEDIUM',
    FALSE
);
