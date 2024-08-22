package com.easemob.chattyai.chat.util;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: chattyai
 * @BelongsPackage: com.easemob.chattyai.chat.util
 * @Author: alonecoder
 * @CreateTime: 2023-11-30  20:06
 * @Description: 问候语Util
 * @Version: 1.0
 */
public class GreetUtil {


    private GreetUtil(){}

    /**
     * 早上问候语list
     */
    public static List<String> moringGreetList = new ArrayList<>();

    /**
     * 中午问候语list
     */
    public static List<String> noonGreetList = new ArrayList<>();

    /**
     * 晚上问候语list
     */
    public static List<String> eveningGreetList = new ArrayList<>();



    static{
        moringGreetList.add("早上好！希望你今天精力充沛！💪");
        moringGreetList.add("早安！让我们迎接新的一天！🌞");
        moringGreetList.add("早上好！愿你的一天充满喜悦和笑声！😄");
        moringGreetList.add("晨光洒满世界，早安！愿你拥有美妙的一天！🌄");
        moringGreetList.add("嗨，新的一天开始啦！希望你无忧无虑！☀️");
        moringGreetList.add("早上好！要记得给自己一个微笑哦！😊");
        moringGreetList.add("欢迎踏入全新的一天，希望你心情愉快！🌤️");
        moringGreetList.add("起床啦，阳光！愿你的早晨充满希望！🌅");
        moringGreetList.add("早安！今天是个奇妙的日子，准备好迎接挑战了吗？💫");
        moringGreetList.add("新的一天又开始了！希望你事事顺心如意！🌈");
        moringGreetList.add("早上好！愿每一天都给你带来新的机会！🍀");
        moringGreetList.add("好梦刚刚结束，美好的一天开始迎接！早安！💤");
        moringGreetList.add("早安！别忘了为自己加油鼓劲哦！👍");
        moringGreetList.add("鸟儿在歌唱，祝你早上好！愿你今天称心如意！🎶");
        moringGreetList.add("嗨！清晨的空气里充满祝福和希望！🌤️");
        moringGreetList.add("早上好！让我们一起追逐梦想的步伐吧！💫");
        moringGreetList.add("早安！每一天都是全新开始的机会！🌞");
        moringGreetList.add("起床了，新的一天送上微笑和温馨！🌅");
        moringGreetList.add("喝口咖啡，愿你的一天充满活力！☕");
        moringGreetList.add("晨曦初现，带来了美好的一天！早上好！🌄");
        moringGreetList.add("早安，朋友！今天也要笑口常开哦！😄");
        moringGreetList.add("早上好！希望你大展宏图！🚀");
        moringGreetList.add("欢迎新的一天！愿你心里充满阳光！☀️");
        moringGreetList.add("早安！别忘记微笑，传递快乐！😊");
        moringGreetList.add("美好的一天从早上开始，早安！💖");
        moringGreetList.add("早上好！祝你开启精彩的一天！🌈");
        moringGreetList.add("早安！愿你在新的一天里充满勇气和智慧！🌞");
        moringGreetList.add("嗨！醒来后，世界更加美好！🌅");
        moringGreetList.add("早安！请记得给自己一个微笑的拥抱！😄");
        moringGreetList.add("晨风吹拂，带来幸福与快乐！早上好！🌤️");
        moringGreetList.add("早上好！今天的你将是最棒的！💪");
        moringGreetList.add("新的一天已经准备好迎接你了！早安！⭐");
        moringGreetList.add("早安！愿你的一天充满阳光和微笑！☀️");
        moringGreetList.add("踏上新的一天，愿成功与你同行！🌞");
        moringGreetList.add("早上好！感受清晨鲜活的呼吸吧！🌄");
        moringGreetList.add("早安！愿你的梦想在今天破晓！💫");
        moringGreetList.add("醒来，美好的一天正在向你招手！早上好！🌅");
        moringGreetList.add("嗨！周一早晨，让我们挥洒活力！💪");
        moringGreetList.add("早上好！让我们一起点亮世界！🌟");
        moringGreetList.add("早安！愿这一天充满惊喜和成功！🌈");
        moringGreetList.add("睁开眼，向新的一天问个早！🌤️");
        moringGreetList.add("早上好！希望你拥有愉快的开始！😊");
        moringGreetList.add("早安！别忘了为自己的梦想努力！💫");
        moringGreetList.add("晨曦微露，带来了好心情！早上好！🌞");
        moringGreetList.add("早上好！愿你的早晨充满微笑和好运！😄");
        moringGreetList.add("新的一天已经到来，为自己加油！早安！💪");
        moringGreetList.add("早安，朋友！今天也要享受每一刻！☀️");
        moringGreetList.add("早上好！希望你在今天拥有无限可能！🌟");
        moringGreetList.add("醒来吧，美梦刚结束，美好的一天即将开始！早安！💤");
        moringGreetList.add("早上好！让我们一起开创美好的一天吧！🌅");
        moringGreetList.add("喝杯咖啡，为这美好的一天疯狂！早安！☕");
        moringGreetList.add("活力四溢的早晨，愿你充满动力！🌞");
        moringGreetList.add("早安！请记得将快乐传递给他人！😊");
        moringGreetList.add("美丽的一天开始了，早上好！🌄");
        moringGreetList.add("早安！祝你拥有生机勃勃的一天！🌤️");
        moringGreetList.add("早上好！希望你的一天充满阳光和欢笑！☀️");
        moringGreetList.add("欢迎新的一天！愿你超越自我！🌟");
        moringGreetList.add("早安！请记住，每一天都是新的开始！🌈");
        moringGreetList.add("醒来，迎接美好的一天！早安！💪");
        moringGreetList.add("早上好！愿你的早晨充满希望和爱！💖");
        moringGreetList.add("早安！今天是新目标的起点！💫");
        moringGreetList.add("美梦结束，精彩的一天开始！早上好！🌅");
        moringGreetList.add("嗨！新的一天，新的机遇！愿你顺利！🌞");
        moringGreetList.add("早上好！用微笑迎接每一个挑战！😄");
        moringGreetList.add("晨光洒满大地，愿你的心情也灿烂如阳光！☀️");
        moringGreetList.add("早安！希望你在今天实现梦想！💪");
        moringGreetList.add("早上好！开始新的旅程，收获新的成就！🌟");
        moringGreetList.add("早安！愿你的一天充满幸福和成功！🌈");
        moringGreetList.add("醒来了吗？美好的一天即将开始！早上好！🌤️");
        moringGreetList.add("嗨！友人，那明媚的阳光正等着你！☀️");
        moringGreetList.add("早安！倾听心灵的声音，它告诉你今天会很棒！🌞");
        moringGreetList.add("早上好！愿你的笑容永远灿烂如初！😊");
        moringGreetList.add("新的一天，新的希望，新的开始！早安！⭐");
        moringGreetList.add("早安！愿你的一天充满喜悦和满足！☀️");
        moringGreetList.add("起床啦，准备好面对美好的一天了吗？🌄");
        moringGreetList.add("早上好！带着微笑展开美妙的一天！😄");
        moringGreetList.add("晨曦初露，带来好运和快乐！早安！🌤️");
        moringGreetList.add("早安！希望你的一天充满无尽的活力！💪");
        moringGreetList.add("早上好！祝愿你一切顺利！🌞");
        moringGreetList.add("早安！让我们一起努力，实现梦想！💫");
        moringGreetList.add("醒来了吗？新的一天等待你的奇迹！早上好！🌅");
        moringGreetList.add("嗨！愿你在新的一天里光芒四射！☀️");
        moringGreetList.add("早上好！希望你的一天充满欢乐和成功！🌟");
        moringGreetList.add("早安！一起度过愉快的一天吧！🌈");
        moringGreetList.add("早上好！打起精神，迎接挑战！💪");
        moringGreetList.add("早安！愿你的努力开启美好明天的大门！🌄");
        moringGreetList.add("醒来吧！新的一天给你带来无限可能！早上好！⭐");
        moringGreetList.add("早上好！晴空万里，愿你的心情无限晴朗！🌤️");
        moringGreetList.add("早安！感受清晨的清新，放飞心情！☀️");
        moringGreetList.add("欢迎新的一天！让我们一起创造奇迹！💫");
        moringGreetList.add("早安！相信自己，你能驾驭一切！🌞");
        moringGreetList.add("早上好！请记得为自己加油鼓劲！😄");
        moringGreetList.add("晨曦拂面，带来了好运和好心情！早安！🌅");
        moringGreetList.add("早上好！希望你享受这美好的一天！🌤️");
        moringGreetList.add("早安，朋友！愿你的精力旺盛！💪");
        moringGreetList.add("早上好！愿你在喜悦中度过每一天！😊");
        moringGreetList.add("新的一天已到，带来希望和动力！早安！⭐");
        moringGreetList.add("早安！让你的微笑点亮整个世界！☀️");
        moringGreetList.add("早上好！希望你的每个愿望都能实现！🌟");
        moringGreetList.add("醒来啦，美好的一天等着你！早上好！");



        noonGreetList.add("人生短暂，珍惜每一天。");
        noonGreetList.add("付出总会有回报，虽不一定即刻。");
        noonGreetList.add("保持学习的心态，永远保持进步。");
        noonGreetList.add("健康是最大的财富。");
        noonGreetList.add("心怀感恩，懂得珍惜拥有的一切。");
        noonGreetList.add("坚持追求梦想，不被困难击倒。");
        noonGreetList.add("对他人多一份理解和宽容。");
        noonGreetList.add("永不放弃，总会找到解决问题的方法。");
        noonGreetList.add("真正的力量来自于内心的坚定。");
        noonGreetList.add("生活是一场不断成长和改变的旅程。");
        noonGreetList.add("知足常乐，享受当下的美好。");
        noonGreetList.add("真正的幸福在于给予他人快乐。");
        noonGreetList.add("心态决定命运，积极面对挑战。");
        noonGreetList.add("信任是建立长久关系的基石。");
        noonGreetList.add("无私奉献，帮助他人是一种快乐。");
        noonGreetList.add("忍耐是实现目标的关键。");
        noonGreetList.add("勇于面对失败，从失败中汲取经验。");
        noonGreetList.add("谦虚使人进步，骄傲使人落后。");
        noonGreetList.add("容忍他人的不同，包容多样性。");
        noonGreetList.add("心灵的平静来自内心的宁静。");
        noonGreetList.add("坚持正直和道义，不背离自己的原则。");
        noonGreetList.add("品味生活中的每个细节。");
        noonGreetList.add("珍视时间，不要浪费生命的宝贵时光。");
        noonGreetList.add("勇敢面对改变，适应不断变化的世界。");
        noonGreetList.add("知识是一种无尽的财富。");
        noonGreetList.add("爱是最强大的力量，传递爱与温暖。");
        noonGreetList.add("别人的成功并不妨碍你的成功。");
        noonGreetList.add("克服恐惧，追求自我突破和成长。");
        noonGreetList.add("创造属于自己的价值和意义。");
        noonGreetList.add("慎思考虑，谨慎行动。");
        noonGreetList.add("善待自己，享受属于自己的时刻。");
        noonGreetList.add("与人为善，润物无声。");
        noonGreetList.add("感恩是一种心灵的净化。");
        noonGreetList.add("接受失败，但不接受失败的结局。");
        noonGreetList.add("爱护大自然，保护我们的地球家园。");
        noonGreetList.add("做一个乐观的人，积极面对困难和挑战。");
        noonGreetList.add("付诸行动，只有行动才能改变现实。");
        noonGreetList.add("认识自己，发掘内在的潜力。");
        noonGreetList.add("持续努力，坚持不懈。");
        noonGreetList.add("勇于承担责任，不推卸责任。");
        noonGreetList.add("追求卓越，给自己设立高标准。");
        noonGreetList.add("真诚待人，与人建立良好的关系。");
        noonGreetList.add("没有比友谊更珍贵的东西。");
        noonGreetList.add("充满爱心，关心他人的需要。");
        noonGreetList.add("对自己宽容，成为自己最好的朋友。");
        noonGreetList.add("快乐源于内心，而不是外部的物质。");
        noonGreetList.add("生活中的失败是成长的机会。");
        noonGreetList.add("接受变化，适应新的环境。");
        noonGreetList.add("坚持自己的原则和价值观。");
        noonGreetList.add("感悟生命中的美好瞬间。");
        noonGreetList.add("设定目标，追求自我实现。");
        noonGreetList.add("用善意对待他人，建立和谐的人际关系。");
        noonGreetList.add("从反面教材中吸取教训，避免重复错误。");
        noonGreetList.add("尊重他人，尊重不同的观点和文化。");
        noonGreetList.add("不断学习，保持持续的个人成长。");
        noonGreetList.add("善于倾听他人，学会与他人沟通。");
        noonGreetList.add("谨言慎行，避免伤害他人的言行。");
        noonGreetList.add("善待自己的身心健康，保持平衡的生活。");
        noonGreetList.add("与优秀的人为伍，激发自己的潜能。");
        noonGreetList.add("谦逊待人，不断学习他人的长处。");
        noonGreetList.add("勇于面对挑战，超越自己的极限。");
        noonGreetList.add("错误是成功的一部分，不要害怕失败。");
        noonGreetList.add("发扬团队合作的精神，共同成就伟业。");
        noonGreetList.add("接纳自己的不完美，欣赏自己的独特之处。");
        noonGreetList.add("善用时间，精确安排自己的生活。");
        noonGreetList.add("积极寻求帮助，分享和合作。");
        noonGreetList.add("保持积极的心态，驱散消极情绪。");
        noonGreetList.add("追求平衡，兼顾工作、家庭和个人生活。");
        noonGreetList.add("在逆境中展现勇气和坚韧。");
        noonGreetList.add("真实地面对自己，不掩饰内心的脆弱。");
        noonGreetList.add("真理与正义永远存在，不受时间限制。");
        noonGreetList.add("兑现承诺，言行一致。");
        noonGreetList.add("给予他人尊重和善意，以及时传递感谢。");
        noonGreetList.add("做好准备，抓住机遇的来临。");
        noonGreetList.add("探索内心的深处，寻找真正的意义。");
        noonGreetList.add("接纳变化，适应新的形势。");
        noonGreetList.add("珍惜友情，与朋友分享快乐和悲伤。");
        noonGreetList.add("保持谦虚和学习的态度，不断进步。");
        noonGreetList.add("面对困难，保持乐观和坚强。");
        noonGreetList.add("看待失败和挫折时，培养耐心和毅力。");
        noonGreetList.add("关注当下，享受此时此刻的美好。");
        noonGreetList.add("接纳他人的不同，包容多样性。");
        noonGreetList.add("尽力而为，不为结果而担忧。");
        noonGreetList.add("知足常乐，满足于拥有的一切。");
        noonGreetList.add("懂得放下，释放心灵的包袱。");
        noonGreetList.add("面对恐惧，勇于挑战自己的极限。");
        noonGreetList.add("珍惜家人和亲人，给予他们温暖与关爱。");
        noonGreetList.add("谨慎选择朋友，与积极正面的人为伍。");
        noonGreetList.add("用心倾听他人的需要和愿望。");
        noonGreetList.add("充满爱与希望，传播积极的能量。");
        noonGreetList.add("与自然和谐相处，保护环境的美好。");
        noonGreetList.add("悦纳生活中的小幸福，感恩生命的点滴。");
        noonGreetList.add("自律是实现目标的关键之一。");
        noonGreetList.add("创造美好的回忆，留下珍贵的人生记忆。");
        noonGreetList.add("展现勇气，敢于冒险和梦想。");
        noonGreetList.add("尊重个人的价值和尊严。");
        noonGreetList.add("探索未知，勇往直前。");
        noonGreetList.add("制定计划，追求有意义的人生。");
        noonGreetList.add("真诚对待他人，表达爱与关怀。");
        noonGreetList.add("不要羡慕别人的人生，活出自己的精彩。");

        eveningGreetList.add("夜已深，一天的劳累，就此停止，一天好坏，从心放下。不思烦恼，莫念忧愁，心无挂碍，入睡安然。晚安！");
        eveningGreetList.add("人生不求完美，平淡才是真，是你的何必去争，别人的何必去抢。晚安！");
        eveningGreetList.add("晚安，我带着我的可爱一起打烊了，帮我关一下月亮。");
        eveningGreetList.add("好好的睡一觉吧，闭上眼睛做个好梦，明天睁眼又会是美好的一天，晚安好梦。");
        eveningGreetList.add("人这一辈子，遇见对你好的人比较容易，可遇见始终待你如初的人很难。晚安。");
        eveningGreetList.add("晚安，愿你今晚做个甜蜜的梦，与星星共舞。");
        eveningGreetList.add("晚安，愿你今晚的睡眠如丝般柔软，甜美入梦。");
        eveningGreetList.add("晚安，抬头看星星，心中的祝福送给你。");
        eveningGreetList.add("晚安，放下一天的疲惫，好梦伴你入眠。");
        eveningGreetList.add("晚安，睡个好觉，明天醒来笑容绽放。");
        eveningGreetList.add("晚安，心想事成，明天更美好。");
        eveningGreetList.add("晚安，拥抱一天的疲惫，明日活力满满。");
        eveningGreetList.add("晚安，愿你在梦乡中找到宁静与安宁。");
        eveningGreetList.add("晚安，夜晚是生命的休憩，明天给你力量。");
        eveningGreetList.add("晚安，祝福你拥有一个舒适的睡眠。");
        eveningGreetList.add("晚安，愿你离愁得解，乘风破浪。");
        eveningGreetList.add("晚安，默许心灵的疲惫，梦里归航。");
        eveningGreetList.add("晚安，让雨洗尽烦恼，给你安逸的夜晚。");
        eveningGreetList.add("晚安，夜幕低垂，带给你温柔的安慰。");
        eveningGreetList.add("晚安，拖走一天的疲惫，梦里的世界更精彩。");
        eveningGreetList.add("晚安，让心在星光下舒展，睡个甜美的觉。");
        eveningGreetList.add("晚安，让浓情入梦，明日更加灿烂。");
        eveningGreetList.add("晚安，躺在床上，感受快乐入眠。");
        eveningGreetList.add("晚安，心语如歌，温暖你的每一寸皮肤。");
        eveningGreetList.add("晚安，愿你拥有梦中的花园，甜蜜如初。");
        eveningGreetList.add("晚安，星星眨眼道声晚安，好梦陪你一起编织。");
        eveningGreetList.add("晚安，让夜意深沉，梦境绽放美好。");
        eveningGreetList.add("晚安，空气中草木的芬芳送你入梦。");
        eveningGreetList.add("晚安，夜色覆盖大地，带走一天的烦恼。");
        eveningGreetList.add("晚安，枕头松软，梦境美丽如画。");
        eveningGreetList.add("晚安，愿你梦中有牵手的温暖，有快乐相伴。");
        eveningGreetList.add("晚安，让星辰傍晚伴你进入幸福甜蜜的梦乡。");
        eveningGreetList.add("晚安，让一天的辛劳化为美梦，明天重新开始。");
        eveningGreetList.add("晚安，守护你的是月亮，庇佑你的是星星。");
        eveningGreetList.add("晚安，枕着月光，拥抱温柔的梦想。");
        eveningGreetList.add("晚安，让美好的回忆伴你入眠，醒来充满活力。");
        eveningGreetList.add("晚安，愿你在梦中拥抱幸福，甜蜜连连。");
        eveningGreetList.add("晚安，乘风破浪，明日将会更美好。");
        eveningGreetList.add("晚安，轻轻闭上眼睛，梦中会有最温暖的拥抱。");
        eveningGreetList.add("晚安，给予疲惫的心灵片刻的宁静和安宁。");
        eveningGreetList.add("晚安，让繁星陪你入睡，迎接崭新的明天。");
        eveningGreetList.add("晚安，让心灵舞动在梦的海洋中。");
        eveningGreetList.add("晚安，感受夜风的温柔，享受宁静的时刻。");
        eveningGreetList.add("晚安，与星月共舞，愿梦中归来时快乐满怀。");
        eveningGreetList.add("晚安，甜蜜洒满你的梦，给你献上真挚的祝愿。");
        eveningGreetList.add("晚安，拥有一个美妙的睡眠，满载能量醒来。");
        eveningGreetList.add("晚安，给心灵一个温馨的港湾，让你进入梦境。");
        eveningGreetList.add("晚安，让星星的光辉为你铺展美好的梦。");
        eveningGreetList.add("晚安，放下白天的疲惫，让梦寻找无限可能。");
        eveningGreetList.add("晚安，温柔入梦，让你的心跳舞动星空。");
        eveningGreetList.add("晚安，梦里是另一个世界，愿你找到属于自己的幸福。");
        eveningGreetList.add("晚安，夜深了，和忧愁告别，让快乐陪你入眠。");
        eveningGreetList.add("晚安，希望你的梦想如同星星一样闪耀。");
        eveningGreetList.add("晚安，愿你的双眼紧闭，幸福和快乐与你同在。");
        eveningGreetList.add("晚安，给自己一个疗愈的夜晚，明日更美好。");
        eveningGreetList.add("晚安，让每一片星光都为你点亮夜晚的道路。");
        eveningGreetList.add("晚安，让繁忙的日子在梦中静静安放。");
        eveningGreetList.add("晚安，感受大自然的宁静，入梦与世界和谐共舞。");
        eveningGreetList.add("晚安，愿你的羁绊尽在梦里，明日轻松愉快。");
        eveningGreetList.add("晚安，与昨日道别，迎接美好的明天。");
        eveningGreetList.add("晚安，星光洒满你的梦，给你带来平安和快乐。");
        eveningGreetList.add("晚安，夜晚是自由的，愿你在梦里飞翔。");
        eveningGreetList.add("晚安，让夜风轻抚你的脸庞，给你宁静的夜晚。");
        eveningGreetList.add("晚安，把心安放在星空下，让甜蜜入睡。");
        eveningGreetList.add("晚安，愿你在梦中寻得无尽的喜悦和幸福。");
        eveningGreetList.add("晚安，愿你的被子像巧克力一样甜蜜入眠。");
        eveningGreetList.add("晚安，记得给枕头拍个晚安，它一直在支持你。");
        eveningGreetList.add("晚安，注意别让床单把你吃掉，好梦！");
        eveningGreetList.add("晚安，做个甜蜜的梦，但记得不要把棉花糖吃掉。");
        eveningGreetList.add("晚安，祝你的睡眠像熊一样美丽而长久。");
        eveningGreetList.add("晚安，希望你的枕头软如云朵，甜如棉花糖。");
        eveningGreetList.add("晚安，愿你的梦里有一座冰淇淋山，一片巧克力海。");
        eveningGreetList.add("晚安，保持微笑入眠，不要让被子妒忌你的魅力。");
        eveningGreetList.add("晚安，睡个好觉，明天醒来像老虎一样勇敢。");
        eveningGreetList.add("晚安，记得在梦中做个世界第一的小懒猪。");
        eveningGreetList.add("晚安，愿你的梦境像彩虹一样五彩斑斓。");
        eveningGreetList.add("晚安，保持正直入睡，别被被窝骗了。");
        eveningGreetList.add("晚安，枕着软软的羊毛，做个甜蜜的绵羊。");
        eveningGreetList.add("晚安，如果你在梦中遇到丧尸，请记得给它送个晚安。");
        eveningGreetList.add("晚安，今晚的梦里请允许自己成为超级英雄！");
        eveningGreetList.add("晚安，愿你的梦里都是开心的兔子跳跳。");
        eveningGreetList.add("晚安，给自己一个小目标，梦里变成一只魔法猫！");
        eveningGreetList.add("晚安，保持呼吸平稳，别让被子让你窒息。");
        eveningGreetList.add("晚安，愿你的梦想像奥利奥饼干一样美味可口。");
        eveningGreetList.add("晚安，记得把手机关掉，让梦想重启。");
        eveningGreetList.add("晚安，愿你的梦境像一个充满独角兽的仙境。");
        eveningGreetList.add("晚安，今晚的睡眠要像被扔进棉花堆一样柔软。");
        eveningGreetList.add("晚安，不要忘记给毛绒玩具晚安，它们也需要休息。");
        eveningGreetList.add("晚安，如果你在梦中遇到皇冠，别忘了试试它的大小。");
        eveningGreetList.add("晚安，梦里遇到的坏人要记得用枕头狠狠地打他们。");
        eveningGreetList.add("晚安，睡个好觉，明天醒来就是超级英雄。");
        eveningGreetList.add("晚安，愿你的梦里充满疯狂的派对和无尽的巧克力。");
        eveningGreetList.add("晚安，记得把愿望放在枕头下，明天就会实现。");
        eveningGreetList.add("晚安，梦见一只懒熊，你也可以偷懒一整天。");
        eveningGreetList.add("晚安，记得把脚伸出被子，给它们透透气。");
        eveningGreetList.add("晚安，梦境里的大饼干就在你眼前，抓住它！");
        eveningGreetList.add("晚安，梦里去旅行，不需要签证和行李。");
        eveningGreetList.add("晚安，给自己的梦想一个温暖的拥抱。");
        eveningGreetList.add("晚安，记得把幽默放在枕头下，明天醒来就笑个不停。");
        eveningGreetList.add("晚安，枕头上的疙瘩是一颗幸运的小星星。");
        eveningGreetList.add("晚安，请记得把电视关掉，让梦幕拉开。");
        eveningGreetList.add("晚安，祝你的睡眠像快乐的喵星人一样可爱。");
        eveningGreetList.add("晚安，愿你的梦境像一部好莱坞大片，精彩纷呈。");
        eveningGreetList.add("晚安，如果你在梦中遇到披着绵羊皮的狼，记得跑得快！");
        eveningGreetList.add("晚安，给自己一个微笑入睡，醒来就是开心果！");
    }

    /**
     * 根据当前时间段来随机获取一个祝福语
     * @return
     */
    public static String  getRomdanGreet(){
        int currentTimePeriod = DateUtils.getCurrentTimePeriod();
        if(0 == currentTimePeriod){
            return  RandomUtil.randomEle(GreetUtil.moringGreetList);
        }else if(1 == currentTimePeriod){
            return  RandomUtil.randomEle(GreetUtil.noonGreetList);
        }else{
            return  RandomUtil.randomEle(GreetUtil.eveningGreetList);
        }
    }

}
