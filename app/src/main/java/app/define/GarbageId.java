package app.define;

import android.content.Context;

import com.topmission.gomipoi.R;

import app.application.GBApplication;
import app.jni.JniBridge;

public enum GarbageId {
    UNKNOWN(0, ""),
    // 出現率30%
    Dust(1, "gomipoi_garbage_dust"),                        // Lv.1 埃
    Paper(2, "gomipoi_garbage_dust_paper"),                       // Lv.2 ちり紙
    SmallGarbageBag(3, "gomipoi_garbage_bag_small"),             // Lv.3 ゴミ袋(小)
    MiddleGarbageBag(4, "gomipoi_garbage_bag_middle"),            // Lv.4 ゴミ袋(中)
    LargeGarbageBag(5, "gomipoi_garbage_bag_large"),             // Lv.5 ゴミ袋(大)
    PaperAirplane(6, "gomipoi_garbage_paper_airplane"),               // Lv.6 紙飛行機
    EmptyCan(7, "gomipoi_garbage_empty_can"),                    // Lv.7 空き缶
    TangerinePeel(8, "gomipoi_garbage_orange_peel"),               // Lv.8 みかんの皮
    G(9, "gomipoi_garbage_g"),                           // Lv.9 G
    Helmet(10, "gomipoi_garbage_kabuto"),                    // Lv.10 紙カブト
    // 出現率10%
    Nope(11, "gomipoi_garbage_failure"),                        // Lv.11 スカ
    SecondhandBook(12, "gomipoi_garbage_secondhand_book"),              // Lv.12 古本
    BananaPeel(13, "gomipoi_garbage_banana_peel"),                  // Lv.13 バナナの皮
    RatPis(14, "gomipoi_garbage_rat_pis"),                      // Lv.14 ネズミ小僧
    AppleCore(15, "gomipoi_garbage_apple_wick"),                   // Lv.15 リンゴの芯
    // 出現率10%
    UnfortunatelyAnswerSheet(16, "gomipoi_garbage_0point_examination_paper"),    // Lv.16 0点の回答
    EmptyBottle(17, "gomipoi_garbage_empty_bottle"),                 // Lv.17 空き瓶
    Bowl(18, "gomipoi_garbage_empty_bowl"),                        // Lv.18 どんぶり
    ShepherdsPurse(19, "gomipoi_garbage_grass"),              // Lv.19 ペンペン草
    Pencil(20, "gomipoi_garbage_pencil_leftover"),                      // Lv.20 鉛筆の最後
    // 出現率8%
    Socks(21, "gomipoi_garbage_socks"),                       // Lv.21 片足靴下
    FishBone(22, "gomipoi_garbage_fishbone"),                    // Lv.22 魚の骨
    Stone(23, "gomipoi_garbage_stone"),                       // Lv.23 石
    Record(24, "gomipoi_garbage_record"),                      // Lv.24 レコード
    LastYearText(25, "gomipoi_garbage_last_year_textbook"),                // Lv.25 去年の教科書
    // 出現率5%
    JellyfinshMushroom(26, "gomipoi_garbage_jelly_mushroom"),          // Lv.26 くらげきのこ
    Pin(27, "gomipoi_garbage_pin"),                         // Lv.27 ピン
    Snail(28, "gomipoi_garbage_maimai"),                       // Lv.28 マイマイ
    BrokenHeart(29, "gomipoi_garbage_broken_heart"),                 // Lv.29 壊れたハート
    LaughterBag(30, "gomipoi_garbage_waraibukuro"),                 // Lv.30 笑い袋
    // 出現率 4%
    HeartMushroom(31, "gomipoi_garbage_heart_mushroom"),               // Lv.31 ハートきのこ
//    OnePiece(32),                    // Lv.32 1ピース
    HotWaterBottle(32, "gomipoi_garbage_hottie"),              // Lv.32 湯たんぽ
    CardboardBox(33, "gomipoi_garbage_cardboard"),                // Lv.33 ダン箱
    Crystal(34, "gomipoi_garbage_crystal_ball"),                     // Lv.34 水晶玉
//    TelephoneCard(35),               // Lv.35 折れたテレカ
    Umbrella(35, "gomipoi_garbage_umbrella"),                    // Lv.35 傘
    // 出現率3%
    PanaeolusPapilionaceus(36, "gomipoi_garbage_laugh_mushroom"),      // Lv.36 笑い茸
    IronManNo3(37, "gomipoi_garbage_iron_man_no_3"),                  // Lv.37 鉄人3号
    SmallMedal(38, "gomipoi_garbage_minimum_medal"),                  // Lv.38 ちっこいメダル
    Spider(39, "gomipoi_garbage_spider"),                      // Lv.39 スパイダー
    Batch(40, "gomipoi_garbage_badge"),                       // Lv.40 防衛軍バッチ
    // 出現率2%
    No1Arm(41, "gomipoi_garbage_arm_no_1"),                      // Lv.41 1号の腕
    PoisonedApple(42, "gomipoi_garbage_poisoned_apple"),               // Lv.42 毒林檎
//    Horsetail(43),                   // Lv.43 つくしんぼ
    BambooShoot(43, "gomipoi_garbage_bamboo_shoot"),                 // Lv.43 筍
    Wig(44, "gomipoi_garbage_wig"),                         // Lv.44 親父のカツラ
    LuckyBag(45, "gomipoi_garbage_empty_lucky_bag"),                    // Lv.45 福袋の袋
    // 出現率1%
    KingG(46, "gomipoi_garbage_g_king"),                       // Lv.46 キングG
    PandoraCan(47, "gomipoi_garbage_pandora_canning"),                  // Lv.47 パンドラの缶詰
    Cane(48, "gomipoi_garbage_witch_wand"),                        // Lv.48 魔法使いの杖
    LuckyStone(49, "gomipoi_garbage_lucky_stone"),                  // Lv.49 ラッキーストーン
    PetitEnvelope(50, "gomipoi_garbage_red_pocket"),               // Lv.50 お年玉の袋
    // 出現率1%
    Slime(51, "gomipoi_garbage_slime"),                       // (合成)スライム<-eGarbageId_BananaPeel + eGarbageId_Snail + eGarbageId_BambooShoot
    Scorpion(52, "gomipoi_garbage_scorpion"),                    // (合成)サソリ<-eGarbageId_RatPis + JellyfinshMushroom + Spider
    UFO(53, "gomipoi_garbage_ufo"),                         // (合成)UFO<-eGarbageId_PaperAirplane + eGarbageId_Stone + IronManNo3
    GaoGao(54, "gomipoi_garbage_gaogao"),                      // (合成)ガオガオ<-eGarbageId_Bowl + eGarbageId_BrokenHeart + eGarbageId_No1Arm
    CaterpillarFungus(55, "gomipoi_garbage_plant_worm"),           // (合成)冬虫夏草<-eGarbageId_G + eGarbageId_Pin + eGarbageId_PanaeolusPapilionaceus
    Parasite(56, "gomipoi_garbage_parasite"),                    // (合成)パラサイト<-eGarbageId_ShepherdsPurse + eGarbageId_LaughterBag + eGarbageId_PoisonedApple
    MarsPassport(57, "gomipoi_garbage_mars_of_passport"),                // (合成)火星パスポート<-eGarbageId_UnfortunatelyAnswerSheet + eGarbageId_LastYearText + eGarbageId_Batch
    AdultBook(58, "gomipoi_garbage_otonano_ehon"),                   // (合成)大人の絵本<-eGarbageId_SecondhandBook + eGarbageId_Record + eGarbageId_OnePiece
    // 出現率0.5%
    LittleUncle(59, "gomipoi_garbage_ossan"),                 // (合成)小さなおっさん<-eGarbageId_Slime + eGarbageId_Scorpion + eGarbageId_Wig
    Haunted(60, "gomipoi_garbage_ghost"),                     // (合成)おばけ<-eGarbageId_UFO + eGarbageId_GaoGao + eGarbageId_PandoraCan
    TreeOfStrangeBeans(61, "gomipoi_garbage_tree_of_strange_beans"),          // (合成)不思議な豆の木<-eGarbageId_CaterpillarFungus + eGarbageId_Parasite + eGarbageId_LuckyBag
    MiraculousAnswerSheet(62, "gomipoi_garbage_miracle_examination_paper"),       // (合成)奇跡の回答<-eGarbageId_MarsPassport + eGarbageId_AdultBook + eGarbageId_LuckyStone
    GodOfPoverty(63, "gomipoi_garbage_god_of_poverty"),                // (合成)貧乏神<-eGarbageId_LittleUncle + eGarbageId_Haunted + eGarbageId_Cane
    Chest(64, "gomipoi_garbage_treasure_chest"),                       // (合成)宝箱<-eGarbageId_TreeOfStrangeBeans + eGarbageId_MiraculousAnswerSheet + eGarbageId_PetitEnvelope

    //第2弾
    Garbage65(65, "gomipoi_garbage_65"),
    Garbage66(66, "gomipoi_garbage_66"),
    Garbage67(67, "gomipoi_garbage_67"),
    Garbage68(68, "gomipoi_garbage_68"),
    Garbage69(69, "gomipoi_garbage_69"),
    Garbage70(70, "gomipoi_garbage_70"),
    Garbage71(71, "gomipoi_garbage_71"),
    Garbage72(72, "gomipoi_garbage_72"),
    Garbage73(73, "gomipoi_garbage_73"),
    Garbage74(74, "gomipoi_garbage_74"),
    Garbage75(75, "gomipoi_garbage_75"),
    Garbage76(76, "gomipoi_garbage_76"),
    Garbage77(77, "gomipoi_garbage_77"),
    Garbage78(78, "gomipoi_garbage_78"),
    Garbage79(79, "gomipoi_garbage_79"),
    Garbage80(80, "gomipoi_garbage_80"),
    Garbage81(81, "gomipoi_garbage_81"),
    Garbage82(82, "gomipoi_garbage_82"),
    Garbage83(83, "gomipoi_garbage_83"),
    Garbage84(84, "gomipoi_garbage_84"),
    Garbage85(85, "gomipoi_garbage_85"),
    Garbage86(86, "gomipoi_garbage_86"),
    Garbage87(87, "gomipoi_garbage_87"),
    Garbage88(88, "gomipoi_garbage_88"),
    Garbage89(89, "gomipoi_garbage_89"),
    Garbage90(90, "gomipoi_garbage_90"),
    Garbage91(91, "gomipoi_garbage_91"),
    Garbage92(92, "gomipoi_garbage_92"),
    Garbage93(93, "gomipoi_garbage_93"),
    Garbage94(94, "gomipoi_garbage_94"),
    Garbage95(95, "gomipoi_garbage_95"),
    Garbage96(96, "gomipoi_garbage_96"),
    Garbage97(97, "gomipoi_garbage_97"),
    Garbage98(98, "gomipoi_garbage_98"),
    Garbage99(99, "gomipoi_garbage_99"),
    Garbage100(100, "gomipoi_garbage_100"),
    Garbage101(101, "gomipoi_garbage_101"),
    Garbage102(102, "gomipoi_garbage_102"),
    Garbage103(103, "gomipoi_garbage_103"),
    Garbage104(104, "gomipoi_garbage_104"),
    Garbage105(105, "gomipoi_garbage_105"),
    Garbage106(106, "gomipoi_garbage_106"),
    Garbage107(107, "gomipoi_garbage_107"),
    Garbage108(108, "gomipoi_garbage_108"),
    Garbage109(109, "gomipoi_garbage_109"),
    Garbage110(110, "gomipoi_garbage_110"),
    Garbage111(111, "gomipoi_garbage_111"),
    Garbage112(112, "gomipoi_garbage_112"),
    Garbage113(113, "gomipoi_garbage_113"),
    Garbage114(114, "gomipoi_garbage_114"),
    Garbage115(115, "gomipoi_garbage_115"),
    Garbage116(116, "gomipoi_garbage_116"),
    Garbage117(117, "gomipoi_garbage_117"),
    Garbage118(118, "gomipoi_garbage_118"),
    Garbage119(119, "gomipoi_garbage_119"),
    Garbage120(120, "gomipoi_garbage_120"),
    Garbage121(121, "gomipoi_garbage_121"),
    Garbage122(122, "gomipoi_garbage_122"),
    Garbage123(123, "gomipoi_garbage_123"),
    Garbage124(124, "gomipoi_garbage_124"),
    Garbage125(125, "gomipoi_garbage_125"),
    Garbage126(126, "gomipoi_garbage_126"),

    Garbage127(127, "gomipoi_garbage_127"),
    Garbage128(128, "gomipoi_garbage_128"),
    Garbage129(129, "gomipoi_garbage_129"),
    Garbage130(130, "gomipoi_garbage_130"),
    Garbage131(131, "gomipoi_garbage_131"),
    Garbage132(132, "gomipoi_garbage_132"),
    Garbage133(133, "gomipoi_garbage_133"),
    Garbage134(134, "gomipoi_garbage_134"),
    Garbage135(135, "gomipoi_garbage_135"),

    ;

    public int getSpBookResource(boolean isUnlocked) {
        switch (this) {
            case Garbage127:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_127_on;
                }
                return R.drawable.book_gomipoi_garbage_127_off;

            case Garbage128:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_128_on;
                }
                return R.drawable.book_gomipoi_garbage_128_off;

            case Garbage129:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_129_on;
                }
                return R.drawable.book_gomipoi_garbage_129_off;

            case Garbage130:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_130_on;
                }
                return R.drawable.book_gomipoi_garbage_130_off;

            case Garbage131:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_131_on;
                }
                return R.drawable.book_gomipoi_garbage_131_off;

            case Garbage132:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_132_on;
                }
                return R.drawable.book_gomipoi_garbage_132_off;

            case Garbage133:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_133_on;
                }
                return R.drawable.book_gomipoi_garbage_133_off;

            case Garbage134:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_134_on;
                }
                return R.drawable.book_gomipoi_garbage_134_off;

            case Garbage135:
                if (isUnlocked) {
                    return R.drawable.book_gomipoi_garbage_135_on;
                }
                return R.drawable.book_gomipoi_garbage_135_off;
        }

        return R.drawable.book_unopened;
    }

    private final int[] dResourceArray = {
            // No0,1,2
            0, R.drawable.book_dust, R.drawable.book_paper,
            // No3,4,5
            R.drawable.book_small_garbage_bag, R.drawable.book_middle_garbage_bag, R.drawable.book_large_garbage_bag,
            // No6,7,8
            R.drawable.book_paper_airplane, R.drawable.book_empty_can, R.drawable.book_tangerine_peel,
            // No9,10,11
//            R.drawable.book_g, R.drawable.book_paper_bag, R.drawable.book_nope,
            R.drawable.book_g, R.drawable.book_helmet, R.drawable.book_nope,
            // No12,13,14
            R.drawable.book_secondhand_book, R.drawable.book_banana_peel, R.drawable.book_rat_pis,
            // No15,16,17
            R.drawable.book_apple_core, R.drawable.book_unfortunately_answer_sheet, R.drawable.book_empty_bottle,
            // No18,19,20
            R.drawable.book_bowl, R.drawable.book_shepherds_purse, R.drawable.book_pencil,
            // No21,22,23
//            R.drawable.book_socks, R.drawable.book_block, R.drawable.book_stone,
            R.drawable.book_socks, R.drawable.book_fish_bone, R.drawable.book_stone,
            // No24,25,26
            R.drawable.book_record, R.drawable.book_last_year_text, R.drawable.book_jellyfinsh_mushroom,
            // No27,28,29
            R.drawable.book_pin, R.drawable.book_snail, R.drawable.book_broken_heart,
            // No30,31,32
//            R.drawable.book_laughter_bag, R.drawable.book_heart_mushroom, R.drawable.book_one_piece,
            R.drawable.book_laughter_bag, R.drawable.book_heart_mushroom, R.drawable.book_hot_water_bottle,
            // No33,34,35
//            R.drawable.book_cardboard_box, R.drawable.book_crystal, R.drawable.book_telephone_card,
            R.drawable.book_cardboard_box, R.drawable.book_crystal, R.drawable.book_umbrella,
            // No36,37,38
            R.drawable.book_panaeolus_papilionaceus, R.drawable.book_iron_man_no3, R.drawable.book_small_medal,
            // No39,40,41
            R.drawable.book_spider, R.drawable.book_batch, R.drawable.book_no1_arm,
            // No42,43,44
//            R.drawable.book_poisoned_apple, R.drawable.book_horsetail, R.drawable.book_wig,
            R.drawable.book_poisoned_apple, R.drawable.book_bamboo_shoot, R.drawable.book_wig,
            // No45,46,47
            R.drawable.book_lucky_bag, R.drawable.book_king_g, R.drawable.book_pandora_can,
            // No48,49,50
            R.drawable.book_cane, R.drawable.book_lucky_stone, R.drawable.book_petit_envelope,
            // No51,52,53
            R.drawable.book_slime, R.drawable.book_scorpion, R.drawable.book_ufo,
            // No54,55,56
            R.drawable.book_gaogao, R.drawable.book_caterpillar_fungus, R.drawable.book_parasite,
            // No57,58,59
            R.drawable.book_mars_passport, R.drawable.book_adult_book, R.drawable.book_little_uncle,
            // No60,61,62
            R.drawable.book_haunted, R.drawable.book_tree_of_strange_beans, R.drawable.book_miraculous_answer_sheet,
            // No63,64
            R.drawable.book_god_of_poverty, R.drawable.book_chest,
            R.drawable.book_gomipoi_garbage_65, R.drawable.book_gomipoi_garbage_66,
            R.drawable.book_gomipoi_garbage_67, R.drawable.book_gomipoi_garbage_68,
            R.drawable.book_gomipoi_garbage_69, R.drawable.book_gomipoi_garbage_70,
            R.drawable.book_gomipoi_garbage_71, R.drawable.book_gomipoi_garbage_72,
            R.drawable.book_gomipoi_garbage_73, R.drawable.book_gomipoi_garbage_74,
            R.drawable.book_gomipoi_garbage_75, R.drawable.book_gomipoi_garbage_76,
            R.drawable.book_gomipoi_garbage_77, R.drawable.book_gomipoi_garbage_78,
            R.drawable.book_gomipoi_garbage_79, R.drawable.book_gomipoi_garbage_80,
            R.drawable.book_gomipoi_garbage_81, R.drawable.book_gomipoi_garbage_82,
            R.drawable.book_gomipoi_garbage_83, R.drawable.book_gomipoi_garbage_84,
            R.drawable.book_gomipoi_garbage_85, R.drawable.book_gomipoi_garbage_86,
            R.drawable.book_gomipoi_garbage_87, R.drawable.book_gomipoi_garbage_88,
            R.drawable.book_gomipoi_garbage_89, R.drawable.book_gomipoi_garbage_90,
            R.drawable.book_gomipoi_garbage_91, R.drawable.book_gomipoi_garbage_92,
            R.drawable.book_gomipoi_garbage_93, R.drawable.book_gomipoi_garbage_94,
            R.drawable.book_gomipoi_garbage_95, R.drawable.book_gomipoi_garbage_96,
            R.drawable.book_gomipoi_garbage_97, R.drawable.book_gomipoi_garbage_98,
            R.drawable.book_gomipoi_garbage_99, R.drawable.book_gomipoi_garbage_100,
            R.drawable.book_gomipoi_garbage_101, R.drawable.book_gomipoi_garbage_102,
            R.drawable.book_gomipoi_garbage_103, R.drawable.book_gomipoi_garbage_104,
            R.drawable.book_gomipoi_garbage_105, R.drawable.book_gomipoi_garbage_106,
            R.drawable.book_gomipoi_garbage_107, R.drawable.book_gomipoi_garbage_108,
            R.drawable.book_gomipoi_garbage_109, R.drawable.book_gomipoi_garbage_110,
            R.drawable.book_gomipoi_garbage_111, R.drawable.book_gomipoi_garbage_112,
            R.drawable.book_gomipoi_garbage_113, R.drawable.book_gomipoi_garbage_114,
            R.drawable.book_gomipoi_garbage_115, R.drawable.book_gomipoi_garbage_116,
            R.drawable.book_gomipoi_garbage_117, R.drawable.book_gomipoi_garbage_118,
            R.drawable.book_gomipoi_garbage_119, R.drawable.book_gomipoi_garbage_120,
            R.drawable.book_gomipoi_garbage_121, R.drawable.book_gomipoi_garbage_122,
            R.drawable.book_gomipoi_garbage_123, R.drawable.book_gomipoi_garbage_124,
            R.drawable.book_gomipoi_garbage_125, R.drawable.book_gomipoi_garbage_126,

            R.drawable.book_gomipoi_garbage_127_off, R.drawable.book_gomipoi_garbage_128_off,
            R.drawable.book_gomipoi_garbage_129_off, R.drawable.book_gomipoi_garbage_130_off,
            R.drawable.book_gomipoi_garbage_131_off, R.drawable.book_gomipoi_garbage_132_off,
            R.drawable.book_gomipoi_garbage_133_off, R.drawable.book_gomipoi_garbage_134_off,
            R.drawable.book_gomipoi_garbage_135_off,
    };

    private final int[] dSynthesisResourceArray = {
            // No0,1,2
            0, R.drawable.panel_dust, R.drawable.panel_paper,
            // No3,4,5
            R.drawable.panel_small_garbage_bag, R.drawable.panel_middle_garbage_bag, R.drawable.panel_large_garbage_bag,
            // No6,7,8
            R.drawable.panel_paper_airplane, R.drawable.panel_empty_can, R.drawable.panel_tangerine_peel,
            // No9,10,11
//            R.drawable.panel_g, R.drawable.panel_paper_bag, R.drawable.panel_nope,
            R.drawable.panel_g, R.drawable.panel_helmet, R.drawable.panel_nope,
            // No12,13,14
            R.drawable.panel_secondhand_book, R.drawable.panel_banana_peel, R.drawable.panel_rat_pis,
            // No15,16,17
            R.drawable.panel_apple_core, R.drawable.panel_unfortunately_answer_sheet, R.drawable.panel_empty_bottle,
            // No18,19,20
            R.drawable.panel_bowl, R.drawable.panel_shepherds_purse, R.drawable.panel_pencil,
            // No21,22,23
//            R.drawable.panel_socks, R.drawable.panel_block, R.drawable.panel_stone,
            R.drawable.panel_socks, R.drawable.panel_fish_bone, R.drawable.panel_stone,
            // No24,25,26
            R.drawable.panel_record, R.drawable.panel_last_year_text, R.drawable.panel_jellyfinsh_mushroom,
            // No27,28,29
            R.drawable.panel_pin, R.drawable.panel_snail, R.drawable.panel_broken_heart,
            // No30,31,32
//            R.drawable.panel_laughter_bag, R.drawable.panel_heart_mushroom, R.drawable.panel_one_piece,
            R.drawable.panel_laughter_bag, R.drawable.panel_heart_mushroom, R.drawable.panel_hot_water_bottle,
            // No33,34,35
//            R.drawable.panel_cardboard_box, R.drawable.panel_crystal, R.drawable.panel_telephone_card,
            R.drawable.panel_cardboard_box, R.drawable.panel_crystal, R.drawable.panel_umbrella,
            // No36,37,38
            R.drawable.panel_panaeolus_papilionaceus, R.drawable.panel_iron_man_no3, R.drawable.panel_small_medal,
            // No39,40,41
            R.drawable.panel_spider, R.drawable.panel_batch, R.drawable.panel_no1_arm,
            // No42,43,44
//            R.drawable.panel_poisoned_apple, R.drawable.panel_horsetail, R.drawable.panel_wig,
            R.drawable.panel_poisoned_apple, R.drawable.panel_bamboo_shoot, R.drawable.panel_wig,
            // No45,46,47
            R.drawable.panel_lucky_bag, R.drawable.panel_king_g, R.drawable.panel_pandora_can,
            // No48,49,50
            R.drawable.panel_cane, R.drawable.panel_lucky_stone, R.drawable.panel_petit_envelope,
            // No51,52,53
            R.drawable.panel_slime, R.drawable.panel_scorpion, R.drawable.panel_ufo,
            // No54,55,56
            R.drawable.panel_gaogao, R.drawable.panel_caterpillar_fungus, R.drawable.panel_parasite,
            // No57,58,59
            R.drawable.panel_mars_passport, R.drawable.panel_adult_book, R.drawable.panel_little_uncle,
            // No60,61,62
            R.drawable.panel_haunted, R.drawable.panel_tree_of_strange_beans, R.drawable.panel_miraculous_answer_sheet,
            // No63,64
            R.drawable.panel_god_of_poverty, R.drawable.panel_chest,
            R.drawable.panel_gomipoi_garbage_65, R.drawable.panel_gomipoi_garbage_66,
            R.drawable.panel_gomipoi_garbage_67, R.drawable.panel_gomipoi_garbage_68,
            R.drawable.panel_gomipoi_garbage_69, R.drawable.panel_gomipoi_garbage_70,
            R.drawable.panel_gomipoi_garbage_71, R.drawable.panel_gomipoi_garbage_72,
            R.drawable.panel_gomipoi_garbage_73, R.drawable.panel_gomipoi_garbage_74,
            R.drawable.panel_gomipoi_garbage_75, R.drawable.panel_gomipoi_garbage_76,
            R.drawable.panel_gomipoi_garbage_77, R.drawable.panel_gomipoi_garbage_78,
            R.drawable.panel_gomipoi_garbage_79, R.drawable.panel_gomipoi_garbage_80,
            R.drawable.panel_gomipoi_garbage_81, R.drawable.panel_gomipoi_garbage_82,
            R.drawable.panel_gomipoi_garbage_83, R.drawable.panel_gomipoi_garbage_84,
            R.drawable.panel_gomipoi_garbage_85, R.drawable.panel_gomipoi_garbage_86,
            R.drawable.panel_gomipoi_garbage_87, R.drawable.panel_gomipoi_garbage_88,
            R.drawable.panel_gomipoi_garbage_89, R.drawable.panel_gomipoi_garbage_90,
            R.drawable.panel_gomipoi_garbage_91, R.drawable.panel_gomipoi_garbage_92,
            R.drawable.panel_gomipoi_garbage_93, R.drawable.panel_gomipoi_garbage_94,
            R.drawable.panel_gomipoi_garbage_95, R.drawable.panel_gomipoi_garbage_96,
            R.drawable.panel_gomipoi_garbage_97, R.drawable.panel_gomipoi_garbage_98,
            R.drawable.panel_gomipoi_garbage_99, R.drawable.panel_gomipoi_garbage_100,
            R.drawable.panel_gomipoi_garbage_101, R.drawable.panel_gomipoi_garbage_102,
            R.drawable.panel_gomipoi_garbage_103, R.drawable.panel_gomipoi_garbage_104,
            R.drawable.panel_gomipoi_garbage_105, R.drawable.panel_gomipoi_garbage_106,
            R.drawable.panel_gomipoi_garbage_107, R.drawable.panel_gomipoi_garbage_108,
            R.drawable.panel_gomipoi_garbage_109, R.drawable.panel_gomipoi_garbage_110,
            R.drawable.panel_gomipoi_garbage_111, R.drawable.panel_gomipoi_garbage_112,
            R.drawable.panel_gomipoi_garbage_113, R.drawable.panel_gomipoi_garbage_114,
            R.drawable.panel_gomipoi_garbage_115, R.drawable.panel_gomipoi_garbage_116,
            R.drawable.panel_gomipoi_garbage_117, R.drawable.panel_gomipoi_garbage_118,
            R.drawable.panel_gomipoi_garbage_119, R.drawable.panel_gomipoi_garbage_120,
            R.drawable.panel_gomipoi_garbage_121, R.drawable.panel_gomipoi_garbage_122,
            R.drawable.panel_gomipoi_garbage_123, R.drawable.panel_gomipoi_garbage_124,
            R.drawable.panel_gomipoi_garbage_125, R.drawable.panel_gomipoi_garbage_126,

            R.drawable.panel_gomipoi_garbage_127, R.drawable.panel_gomipoi_garbage_128,
            R.drawable.panel_gomipoi_garbage_129, R.drawable.panel_gomipoi_garbage_130,
            R.drawable.panel_gomipoi_garbage_131, R.drawable.panel_gomipoi_garbage_132,
            R.drawable.panel_gomipoi_garbage_133, R.drawable.panel_gomipoi_garbage_134,
            R.drawable.panel_gomipoi_garbage_135,
    };

    private final int[] dDetailResourceArray = {
            // No0,1,2
            0, R.drawable.detail_dust, R.drawable.detail_paper,
            // No3,4,5
            R.drawable.detail_small_garbage_bag, R.drawable.detail_middle_garbage_bag, R.drawable.detail_large_garbage_bag,
            // No6,7,8
            R.drawable.detail_paper_airplane, R.drawable.detail_empty_can, R.drawable.detail_tangerine_peel,
            // No9,10,11
//            R.drawable.detail_g, R.drawable.detail_paper_bag, R.drawable.detail_nope,
            R.drawable.detail_g, R.drawable.detail_helmet, R.drawable.detail_nope,
            // No12,13,14
            R.drawable.detail_secondhand_book, R.drawable.detail_banana_peel, R.drawable.detail_rat_pis,
            // No15,16,17
            R.drawable.detail_apple_core, R.drawable.detail_unfortunately_answer_sheet, R.drawable.detail_empty_bottle,
            // No18,19,20
            R.drawable.detail_bowl, R.drawable.detail_shepherds_purse, R.drawable.detail_pencil,
            // No21,22,23
//            R.drawable.detail_socks, R.drawable.detail_block, R.drawable.detail_stone,
            R.drawable.detail_socks, R.drawable.detail_fish_bone, R.drawable.detail_stone,
            // No24,25,26
            R.drawable.detail_record, R.drawable.detail_last_year_text, R.drawable.detail_jellyfinsh_mushroom,
            // No27,28,29
            R.drawable.detail_pin, R.drawable.detail_snail, R.drawable.detail_broken_heart,
            // No30,31,32
//            R.drawable.detail_laughter_bag, R.drawable.detail_heart_mushroom, R.drawable.detail_one_piece,
            R.drawable.detail_laughter_bag, R.drawable.detail_heart_mushroom, R.drawable.detail_hot_water_bottle,
            // No33,34,35
//            R.drawable.detail_cardboard_box, R.drawable.detail_crystal, R.drawable.detail_telephone_card,
            R.drawable.detail_cardboard_box, R.drawable.detail_crystal, R.drawable.detail_umbrella,
            // No36,37,38
            R.drawable.detail_panaeolus_papilionaceus, R.drawable.detail_iron_man_no3, R.drawable.detail_small_medal,
            // No39,40,41
            R.drawable.detail_spider, R.drawable.detail_batch, R.drawable.detail_no1_arm,
            // No42,43,44
//            R.drawable.detail_poisoned_apple, R.drawable.detail_horsetail, R.drawable.detail_wig,
            R.drawable.detail_poisoned_apple, R.drawable.detail_bamboo_shoot, R.drawable.detail_wig,
            // No45,46,47
            R.drawable.detail_lucky_bag, R.drawable.detail_king_g, R.drawable.detail_pandora_can,
            // No48,49,50
            R.drawable.detail_cane, R.drawable.detail_lucky_stone, R.drawable.detail_petit_envelope,
            // No51,52,53
            R.drawable.detail_slime, R.drawable.detail_scorpion, R.drawable.detail_ufo,
            // No54,55,56
            R.drawable.detail_gaogao, R.drawable.detail_caterpillar_fungus, R.drawable.detail_parasite,
            // No57,58,59
            R.drawable.detail_mars_passport, R.drawable.detail_adult_book, R.drawable.detail_little_uncle,
            // No60,61,62
            R.drawable.detail_haunted, R.drawable.detail_tree_of_strange_beans, R.drawable.detail_miraculous_answer_sheet,
            // No63,64
            R.drawable.detail_god_of_poverty, R.drawable.detail_chest,
            R.drawable.detail_garbage_65, R.drawable.detail_garbage_66,
            R.drawable.detail_garbage_67, R.drawable.detail_garbage_68,
            R.drawable.detail_garbage_69, R.drawable.detail_garbage_70,
            R.drawable.detail_garbage_71, R.drawable.detail_garbage_72,
            R.drawable.detail_garbage_73, R.drawable.detail_garbage_74,
            R.drawable.detail_garbage_75, R.drawable.detail_garbage_76,
            R.drawable.detail_garbage_77, R.drawable.detail_garbage_78,
            R.drawable.detail_garbage_79, R.drawable.detail_garbage_80,
            R.drawable.detail_garbage_81, R.drawable.detail_garbage_82,
            R.drawable.detail_garbage_83, R.drawable.detail_garbage_84,
            R.drawable.detail_garbage_85, R.drawable.detail_garbage_86,
            R.drawable.detail_garbage_87, R.drawable.detail_garbage_88,
            R.drawable.detail_garbage_89, R.drawable.detail_garbage_90,
            R.drawable.detail_garbage_91, R.drawable.detail_garbage_92,
            R.drawable.detail_garbage_93, R.drawable.detail_garbage_94,
            R.drawable.detail_garbage_95, R.drawable.detail_garbage_96,
            R.drawable.detail_garbage_97, R.drawable.detail_garbage_98,
            R.drawable.detail_garbage_99, R.drawable.detail_garbage_100,
            R.drawable.detail_garbage_101, R.drawable.detail_garbage_102,
            R.drawable.detail_garbage_103, R.drawable.detail_garbage_104,
            R.drawable.detail_garbage_105, R.drawable.detail_garbage_106,
            R.drawable.detail_garbage_107, R.drawable.detail_garbage_108,
            R.drawable.detail_garbage_109, R.drawable.detail_garbage_110,
            R.drawable.detail_garbage_111, R.drawable.detail_garbage_112,
            R.drawable.detail_garbage_113, R.drawable.detail_garbage_114,
            R.drawable.detail_garbage_115, R.drawable.detail_garbage_116,
            R.drawable.detail_garbage_117, R.drawable.detail_garbage_118,
            R.drawable.detail_garbage_119, R.drawable.detail_garbage_120,
            R.drawable.detail_garbage_121, R.drawable.detail_garbage_122,
            R.drawable.detail_garbage_123, R.drawable.detail_garbage_124,
            R.drawable.detail_garbage_125, R.drawable.detail_garbage_126,

            R.drawable.detail_garbage_127_off, R.drawable.detail_garbage_128_off,
            R.drawable.detail_garbage_129_off, R.drawable.detail_garbage_130_off,
            R.drawable.detail_garbage_131_off, R.drawable.detail_garbage_132_off,
            R.drawable.detail_garbage_133_off, R.drawable.detail_garbage_134_off,
            R.drawable.detail_garbage_135_off,
    };

    private int mGarbageId;
    private String mGarbageCode;

    GarbageId(int mGarbageId, String garbageCode) {
        this.mGarbageId = mGarbageId;
        this.mGarbageCode = garbageCode;
    }

    public static GarbageId valueOf(int garbageId) {
        for (GarbageId garbage : values()) {
            if (garbage.getValue() == garbageId) {
                return garbage;
            }
        }
        return UNKNOWN;
    }

    public static GarbageId valueOfWithCode(String garbageCode) {
        for (GarbageId garbage : values()) {
            if (garbage.getCode().equals(garbageCode)) {
                return garbage;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mGarbageId;
    }

    public String getCode() {
        return mGarbageCode;
    }

    /**
     * 図鑑番号のリソースIDを取得
     */
    public int getResourceId() {
        return dResourceArray[mGarbageId];
    }

    /**
     * ゴミ合成のリソースIDを取得
     */
    public int getSynthesisResourceId() {
        return dSynthesisResourceArray[mGarbageId];
    }

    public int getDetailResourceId() {
        return dDetailResourceArray[mGarbageId];
    }

    public int getSpBookDetailResource() {
        switch (this) {
            case Garbage127:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_127_on;
                }
                return R.drawable.detail_garbage_127_off;

            case Garbage128:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_128_on;
                }
                return R.drawable.detail_garbage_128_off;

            case Garbage129:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_129_on;
                }
                return R.drawable.detail_garbage_129_off;

            case Garbage130:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_130_on;
                }
                return R.drawable.detail_garbage_130_off;

            case Garbage131:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_131_on;
                }
                return R.drawable.detail_garbage_131_off;

            case Garbage132:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_132_on;
                }
                return R.drawable.detail_garbage_132_off;

            case Garbage133:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_133_on;
                }
                return R.drawable.detail_garbage_133_off;

            case Garbage134:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_134_on;
                }
                return R.drawable.detail_garbage_134_off;

            case Garbage135:
                if (!isLocked()) {
                    return R.drawable.detail_garbage_135_on;
                }
                return R.drawable.detail_garbage_135_off;
        }

        return R.drawable.book_unopened;
    }

    public boolean isLocked() {
        return !JniBridge.nativeIsUnlockBook(getCode());
    }

    public boolean isNew() {
        return JniBridge.nativeIsNewBook(getCode());
    }

    public boolean isRare() { return JniBridge.nativeIsRareGarbage(getCode()); }

    public static boolean isNeedGotBonus() {


//        for (int pageNo = 1; pageNo <= 11; pageNo++) {
//            if (((GBApplication) context).getPreferenceManager().isGotBookBonus(pageNo)) {
//                continue;
//            }
//
//            GarbageId garbageId1 = GarbageId.valueOf(6 * (pageNo - 1) + 1);
//            if (!garbageId1.equals(GarbageId.UNKNOWN)
//                    && ((GBApplication) context).getPreferenceManager().getGarbageData(garbageId1) == 0) {
//                continue;
//            }
//
//            GarbageId garbageId2 = GarbageId.valueOf(6 * (pageNo - 1) + 2);
//            if (!garbageId2.equals(GarbageId.UNKNOWN)
//                    && ((GBApplication) context).getPreferenceManager().getGarbageData(garbageId2) == 0) {
//                continue;
//            }
//
//            GarbageId garbageId3 = GarbageId.valueOf(6 * (pageNo - 1) + 3);
//            if (!garbageId3.equals(GarbageId.UNKNOWN)
//                    && ((GBApplication) context).getPreferenceManager().getGarbageData(garbageId3) == 0) {
//                continue;
//            }
//
//            GarbageId garbageId4 = GarbageId.valueOf(6 * (pageNo - 1) + 4);
//            if (!garbageId4.equals(GarbageId.UNKNOWN)
//                    && ((GBApplication) context).getPreferenceManager().getGarbageData(garbageId4) == 0) {
//                continue;
//            }
//
//            GarbageId garbageId5 = GarbageId.valueOf(6 * (pageNo - 1) + 5);
//            if (!garbageId5.equals(GarbageId.UNKNOWN)
//                    && ((GBApplication) context).getPreferenceManager().getGarbageData(garbageId5) == 0) {
//                continue;
//            }
//
//            GarbageId garbageId6 = GarbageId.valueOf(6 * (pageNo - 1) + 6);
//            if (!garbageId6.equals(GarbageId.UNKNOWN)
//                    && ((GBApplication) context).getPreferenceManager().getGarbageData(garbageId6) == 0) {
//                continue;
//            }
//
//            ((GBApplication)context).getPreferenceManager().gotBookBonus(pageNo);
//            return true;
//        }

        return false;
    }
}
