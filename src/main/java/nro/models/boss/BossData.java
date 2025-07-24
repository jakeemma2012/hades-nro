package nro.models.boss;

import nro.consts.ConstPlayer;
import nro.models.skill.Skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;

/**
 *
 */
public class BossData {

    public static final int _0_GIAY = 0;
    public static final int _1_GIAY = 1;
    public static final int _5_GIAY = 5;
    public static final int _10_GIAY = 10;
    public static final int _30_GIAY = 30;
    public static final int _1_PHUT = 60;
    public static final int _5_PHUT = 300;
    public static final int _10_PHUT = 600;
    public static final int _15_PHUT = 900;
    public static final int _20_PHUT = 1200;
    public static final int _30_PHUT = 1800;
    public static final int _1_GIO = 3600;

    public static final Set<Integer> allMaps = Set.of(92, 93, 94);
    public static final Set<Integer> availableMaps = new HashSet<>(allMaps);
    public static final Set<Integer> usedMaps = new HashSet<>();

    public static final int _MUNGEN_GIO = Integer.MAX_VALUE;
    // --------------------------------------------------------------------------
    public String name;

    public byte gender;

    public byte typeDame;

    public byte typeHp;

    public long dame;

    public long[][] hp;

    public short[] outfit;

    public short[] mapJoin;

    public int[][] skillTemp;

    public int secondsRest;

    public boolean joinMapIdle;

    public int timeDelayLeaveMap = -1;

    @Builder
    public BossData(String name, byte gender, byte typeDame, byte typeHp, long dame, long[][] hp,
                    short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, long dame, long[][] hp,
                    short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.joinMapIdle = joinMapIdle;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, long dame, long[][] hp,
                    short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, int timeDelayLeaveMap) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.timeDelayLeaveMap = timeDelayLeaveMap;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, long dame, long[][] hp,
                    short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle,
                    int timeDelayLeaveMap) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.joinMapIdle = joinMapIdle;
        this.timeDelayLeaveMap = timeDelayLeaveMap;
    }

    public static final BossData MABUPHUBAN = new BossData(
            "Hắc Bư Mập", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{297, 298, 299}, // outfit
            new short[]{215}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000},
                    {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 2000},
                    {Skill.MASENKO, 7, 2000}, {Skill.MASENKO, 1, 2000}},
            _5_PHUT);

    public static final BossData MabuROM = new BossData(
            "Hắc Bư Gầy", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{3_000_000_000l}}, // hp
            new short[]{1352, 1353, 1354}, // outfit
            new short[]{215}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000},
                    {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 2000},
                    {Skill.MASENKO, 7, 2000}, {Skill.MASENKO, 1, 2000}},
            _0_GIAY, true);

    public static final BossData MA_TROI = new BossData(
            "Ma Trơi", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            2000, // dame
            new long[][]{{500}}, // hp
            new short[]{651, 652, 653}, // outfit
            new short[]{3, 4, 5, 6, 27, 28, 29, 30, // traidat
                    9, 11, 12, 13, 10, 34, 33, 32, 31, // namec
                    16, 17, 18, 19, 20, 37, 38, 36, 35, // xayda
                    24, 25, 26}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 2000}, {Skill.MASENKO, 7, 2000},
                    {Skill.MASENKO, 1, 2000}},
            _1_PHUT);

    public static final BossData OMEGA_PLUS_DOUBLE = new BossData(
            "Omega %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            500_000, // dame
            new long[][]{{1_600_000_000}}, // hp
            new short[]{1311, 1312, 1313}, // outfit
            new short[]{43}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _MUNGEN_GIO);

    public static final BossData DOI_NHI = new BossData(
            "Dơi Nhí", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            2000, // dame
            new long[][]{{500}}, // hp
            new short[]{654, 655, 656}, // outfit
            new short[]{3, 4, 5, 6, 27, 28, 29, 30, // traidat
                    9, 11, 12, 13, 10, 34, 33, 32, 31, // namec
                    16, 17, 18, 19, 20, 37, 38, 36, 35, // xayda
                    24, 25, 26}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 2000}, {Skill.MASENKO, 7, 2000},
                    {Skill.MASENKO, 1, 2000}},
            _1_PHUT);

    public static final BossData BO_XUONG = new BossData(
            "Bộ Xương", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            2000, // dame
            new long[][]{{500}}, // hp
            new short[]{545, 548, 549}, // outfit
            new short[]{3, 4, 5, 6, 27, 28, 29, 30, // traidat
                    9, 11, 12, 13, 10, 34, 33, 32, 31, // namec
                    16, 17, 18, 19, 20, 37, 38, 36, 35, // xayda
                    24, 25, 26}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 2000}, {Skill.MASENKO, 7, 2000},
                    {Skill.MASENKO, 1, 2000}},
            _1_PHUT);

    public static final BossData OMEGA_PLUS = new BossData(
            "OMEGA", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50_000, // dame
            new long[][]{{400_000_000_000L}}, // hp
            new short[]{1552, 1553, 1554}, // outfit
            new short[]{27}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500}
            },
            _0_GIAY);

    public static final BossData THODAIKA = new BossData(
            "Thỏ Đại Ka", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            2000, // dame
            new long[][]{{500_000}}, // hp
            new short[]{403, 404, 405}, // outfit
            new short[]{3, 4, 5, 6, 27, 28, 29, 30, // traidat
                    9, 11, 12, 13, 10, 34, 33, 32, 31, // namec
                    16, 17, 18, 19, 20, 37, 38, 36, 35, // xayda
                    24, 25, 26}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.THAI_DUONG_HA_SAN, 1, 7_000},},
            _1_PHUT);

    public static final BossData BASIL = new BossData(
            "Fide con %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{1438, 1439, 1440}, // outfit
            new short[]{19}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData BERAMO = new BossData(
            "Fide %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{189, 190, 191}, // outfit
            new short[]{19}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData LAVENDER = new BossData(
            "Golden Fide %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{502, 503, 504}, // outfit
            new short[]{19}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData RAITY = new BossData(
            "Raity", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            1, // dame
            new long[][]{{100}}, // hp
            new short[]{490, 491, 492}, // outfit
            new short[]{3, 4, 5, 6, 27, 28, 29, 30, // traidat
                    9, 11, 12, 13, 10, 34, 33, 32, 31, // namec
                    16, 17, 18, 19, 20, 37, 38, 36, 35, // xayda
                    24, 25, 26}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.THAI_DUONG_HA_SAN, 1, 7_000},},
            _1_PHUT);
    // --------------------------------------------------------------------------Broly
    public static final BossData BROLY = new BossData(
            "Broly %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_PERCENT_HP_HUND, // type dame
            Boss.HP_NORMAL, // type hp
            1, // dame
            new long[][]{{100, 1000}, {1000, 100000}, {100000, 1000000}}, // hp
            new short[]{291, 292, 293}, // outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, // map
            // join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _1_PHUT // số giây nghỉ
    );

    public static final BossData SUPER_BROLY = new BossData(
            "Super Broly %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new long[][]{{200_000_000}}, // hp
            new short[]{294, 295, 296}, // outfit
            new short[]{11, 12, 13, 10, 33, 34, 32, 31}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _15_PHUT);
    public static final BossData PUT_NGU = new BossData(
            "Tân binh",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000,
            new long[][]{{500000}},
            new short[]{527, 525, 524},
            new short[]{131, 132, 133},
            new int[][]{ // skill
                    {Skill.MASENKO, 1, 1}, {Skill.MASENKO, 1, 1},
                    {Skill.DEMON, 1, 1000}, {Skill.DEMON, 1, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 1000},},
            30);

    public static final BossData DOI_TRUONG = new BossData(
            "Đội trưởng",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000,
            new long[][]{{500000}},
            new short[]{526, 525, 524},
            new short[]{131, 132, 133},
            new int[][]{ // skill
                    {Skill.MASENKO, 1, 1}, {Skill.MASENKO, 1, 1},
                    {Skill.DEMON, 1, 1000}, {Skill.DEMON, 1, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 1000},},
            _0_GIAY);

    public static final BossData BEO_DEP_TRAI = new BossData(
            "Tập sự",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000,
            new long[][]{{500000}},
            new short[]{526, 525, 524},
            new short[]{131, 132, 133},
            new int[][]{ // skill
                    {Skill.MASENKO, 1, 1}, {Skill.MASENKO, 1, 1},
                    {Skill.DEMON, 1, 1000}, {Skill.DEMON, 1, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 1000},},
            _0_GIAY);

    public static final BossData CHIEN_BINH = new BossData(
            "Chiến binh",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000,
            new long[][]{{500000}},
            new short[]{528, 525, 524},
            new short[]{131, 132, 133},
            new int[][]{ // skill
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 1, 1000},
                    {Skill.DEMON, 1, 1000}, {Skill.DEMON, 1, 1000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 1000}, {Skill.TAI_TAO_NANG_LUONG, 7, 1000},},
            _0_GIAY);

    public static final BossData SUPER_BROLY_RED = new BossData(
            "Super Broly Love %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new long[][]{{1000000000}}, // hp
            new short[]{2000, 295, 296}, // outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, // map
            // join
            // new short[]{14}, //map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _15_PHUT);
    // --------------------------------------------------------------------------Boss
    // hải tặc

    public static final BossData LUFFY = new BossData(
            "Luffy", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{12000000}}, // hp
            new short[]{582, 583, 584}, // outfit
            new short[]{137}, // map join
            new int[][]{ // skill
                    {Skill.GALICK, 7, 1000}, {Skill.GALICK, 6, 1000}, {Skill.GALICK, 5, 1000},
                    {Skill.GALICK, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData ZORO = new BossData(
            "Zoro", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{11000000}}, // hp
            new short[]{585, 586, 587}, // outfit
            new short[]{137}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 7, 1000}, {Skill.DRAGON, 6, 1000}, {Skill.DRAGON, 5, 1000},
                    {Skill.DRAGON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData SANJI = new BossData(
            "Sanji", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{10000000}}, // hp
            new short[]{588, 589, 590}, // outfit
            new short[]{137}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                    {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData USOPP = new BossData(
            "Usopp", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{7000000}}, // hp
            new short[]{597, 598, 599}, // outfit
            new short[]{136}, // map join
            new int[][]{ // skill
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 1, 1000},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 1, 1000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 1, 1000},},
            _0_GIAY, true);

    public static final BossData FRANKY = new BossData(
            "Franky", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{8000000}}, // hp
            new short[]{594, 595, 596}, // outfit
            new short[]{136}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                    {Skill.DEMON, 4, 1000},
                    {Skill.ANTOMIC, 7, 5000}
            },
            _0_GIAY, true);

    public static final BossData BROOK = new BossData(
            "Brook", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{9000000}}, // hp
            new short[]{591, 592, 593}, // outfit
            new short[]{136}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                    {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData NAMI = new BossData(
            "Nami", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{4000000}}, // hp
            new short[]{600, 601, 602}, // outfit
            new short[]{138}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                    {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData CHOPPER = new BossData(
            "Chopper", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{5000000}}, // hp
            new short[]{606, 607, 608}, // outfit
            new short[]{138}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                    {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    public static final BossData TRUNG_UY_XANH_LO_2 = new BossData(
            "Trung uý Xanh Lơ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            10, // dame
            new long[][]{{10000000}}, // hp
            new short[]{135, 136, 137}, // outfit
            new short[]{62}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                    {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                    {Skill.DEMON, 7, 400},
                    {Skill.KAMEJOKO, 2, 1500},
                    {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.THAI_DUONG_HA_SAN, 7, 30000}
            },
            _0_GIAY);

    public static final BossData ROBIN = new BossData(
            "Robin", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_PERCENT_HP_THOU, // type dame
            Boss.HP_NORMAL, // type hp
            5, // dame
            new long[][]{{6000000}}, // hp
            new short[]{603, 604, 605}, // outfit
            new short[]{138}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000},
                    {Skill.DEMON, 4, 1000}
            },
            _0_GIAY, true);

    // --------------------------------------------------------------------------Boss
    // doanh trại
    public static final BossData TRUNG_UY_TRANG = new BossData(
            "Trung uý Trắng", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50000, // dame
            new long[][]{{10_000_000_000L}}, // hp
            new short[]{141, 142, 143}, // outfit
            new short[]{223}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                    {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                    {Skill.DEMON, 7, 400}
            },
            _30_PHUT);

    public static final BossData TRUNG_UY_XANH_LO = new BossData(
            "Trung uý Xanh Lơ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{10_000_000_000L}}, // hp
            new short[]{135, 136, 137}, // outfit
            new short[]{223}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480},
                    {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420},
                    {Skill.DEMON, 7, 400},
                    {Skill.KAMEJOKO, 2, 1500},
                    {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.THAI_DUONG_HA_SAN, 7, 30000}
            },
            _30_PHUT);

    public static final BossData TRUNG_UY_THEP = new BossData(
            "Trung uý Thép", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{10_000_000_000L}}, // hp
            new short[]{129, 130, 131}, // outfit
            new short[]{223}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 300}, {Skill.DRAGON, 3, 500},
                    {Skill.DEMON, 1, 100}, {Skill.DEMON, 2, 300}, {Skill.DEMON, 3, 500},
                    {Skill.GALICK, 1, 100},
                    {Skill.MASENKO, 1, 100}, {Skill.MASENKO, 2, 100}
            },
            _30_PHUT);

    public static final BossData QUY_LAO = new BossData(
            "Quy Lão Kamê", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{2_000_000_000L}}, // hp
            new short[]{554, 555, 556}, // outfit
            new short[]{24}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 300}, {Skill.DRAGON, 3, 500},
                    {Skill.DEMON, 1, 100}, {Skill.DEMON, 2, 300}, {Skill.DEMON, 3, 500},
                    {Skill.GALICK, 1, 100},
                    {Skill.MASENKO, 1, 100}, {Skill.MASENKO, 2, 100}
            },
            _15_PHUT);

    public static final BossData Khi_6 = new BossData(
            "Super Monkey", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            5000, // dame
            new long[][]{{1_000_000_000L}}, // hp
            new short[]{200, 193, 194}, // outfit
            new short[]{102}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 300}, {Skill.DRAGON, 3, 500},
                    {Skill.DEMON, 1, 100}, {Skill.DEMON, 2, 300}, {Skill.DEMON, 3, 500},
                    {Skill.GALICK, 1, 100},
                    {Skill.MASENKO, 1, 100}, {Skill.MASENKO, 2, 100}
            },
            _15_PHUT);

    public static final BossData HONGHOA = new BossData(
            "Hoa Hồng", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            0, // dame
            new long[][]{{100}}, // hp
            new short[]{706, 707, 708}, // outfit
            new short[]{19}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 300}, {Skill.DRAGON, 3, 500},
                    {Skill.DEMON, 1, 100}, {Skill.DEMON, 2, 300}, {Skill.DEMON, 3, 500},
                    {Skill.GALICK, 1, 100},
                    {Skill.MASENKO, 1, 100}, {Skill.MASENKO, 2, 100}
            },
            _15_PHUT);

    public static final BossData NINJA_AO_TIM = new BossData(
            "Ninja áo tím", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            40, // dame
            new long[][]{{150}}, // hp
            new short[]{123, 124, 125}, // outfit
            new short[]{54}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData NINJA_AO_TIM_FAKE = new BossData(
            "Ninja áo tím", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            75, // dame
            new long[][]{{100}}, // hp
            new short[]{123, 124, 125}, // outfit
            new short[]{54}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData ROBOT_VE_SI = new BossData(
            "Rôbốt Vệ Sĩ", // name
            ConstPlayer.TRAI_DAT, // gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, // type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, // type hp
            50, // dame
            new long[][]{{120}}, // hp
            new short[]{138, 139, 140}, // outfit
            new short[]{57}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------Boss
    // xên ginder
    public static final BossData XEN_BO_HUNG_1 = new BossData(
            "Xên bọ hung 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            12000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{228, 229, 230}, // outfit
            new short[]{100}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _10_PHUT);

    public static final BossData XEN_BO_HUNG_2 = new BossData(
            "Xên bọ hung 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            16000, // dame
            new long[][]{{1_000_000_000}}, // hp
            new short[]{231, 232, 233}, // outfit
            new short[]{100}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData XEN_BO_HUNG_HOAN_THIEN = new BossData(
            "Xên hoàn thiện", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{234, 235, 236}, // outfit
            new short[]{100}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------Boss
    // xên võ đài
    public static final BossData XEN_BO_HUNG = new BossData(
            "Xên bọ hung %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{5_000_000_000L}}, // hp
            new short[]{234, 235, 236}, // outfit
            new short[]{103}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100},
                    {Skill.THAI_DUONG_HA_SAN, 5, 45000},
                    {Skill.TU_SAT, 7, 100}
            },
            _5_PHUT, true);

    public static final BossData XEN_CON = new BossData(
            "Xên con %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{264, 265, 266}, // outfit
            new short[]{103}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);

    public static final BossData SIEU_BO_HUNG = new BossData(
            "Siêu bọ hung", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new long[][]{{10_000_000_000L}}, // hp
            new short[]{234, 235, 236}, // outfit
            new short[]{103}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            },
            _0_GIAY);
    // --------------------------------------------------------------------------Boss
    // nappa
    public static final BossData KUKU = new BossData(
            "Kuku", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            10000, // dame
            new long[][]{{50000000}}, // hp
            new short[]{159, 160, 161}, // outfit
            new short[]{68, 69, 70, 71, 72}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _1_PHUT);
    public static final BossData MAP_DAU_DINH = new BossData(
            "Mập đầu đinh", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{65000000}}, // hp
            new short[]{165, 166, 167}, // outfit
            new short[]{64, 65, 63, 66, 67}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _1_PHUT);
    public static final BossData RAMBO = new BossData(
            "Rambo", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{70000000}}, // hp
            new short[]{162, 163, 164}, // outfit
            new short[]{73, 74, 75, 76, 77}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _1_PHUT);

    // --------------------------------------------------------------------------Boss
    // cold
    public static final BossData COOLER = new BossData(
            "Cooler", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            12000, // dame
            new long[][]{{1_500_000_000l}}, // hp
            new short[]{317, 318, 319}, // outfit
            new short[]{110}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    public static final BossData COOLER2 = new BossData(
            "Cooler 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            22000, // dame
            new long[][]{{2_000_000_000L}}, // hp
            new short[]{320, 321, 322}, // outfit
            new short[]{110}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------Tiểu
    // đội sát thủ
    public static final BossData SO4 = new BossData(
            "Số 4", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{200_000_000}}, // hp
            new short[]{168, 169, 170}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);
    public static final BossData SO3 = new BossData(
            "Số 3", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            35000, // dame
            new long[][]{{250_000_000}}, // hp
            new short[]{174, 175, 176}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SO2 = new BossData(
            "Số 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            40000, // dame
            new long[][]{{300_000_000}}, // hp
            new short[]{171, 172, 173}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData SO1 = new BossData(
            "Số 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            40000, // dame
            new long[][]{{350_000_000}}, // hp
            new short[]{177, 178, 179}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData TIEU_DOI_TRUONG = new BossData(
            "Tiểu đội trưởng", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{180, 181, 182}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT, true);

    // --------------------------------------------------------------------------Fide
    // đại ca
    public static final BossData FIDE_DAI_CA_1 = new BossData(
            "Fide đại ca 1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50000, // dame
            new long[][]{{200_000_000}}, // hp
            new short[]{183, 184, 185}, // outfit
            new short[]{80}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    public static final BossData FIDE_DAI_CA_2 = new BossData(
            "Fide đại ca 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            50000, // dame
            new long[][]{{300_000_000}}, // hp
            new short[]{186, 187, 188}, // outfit
            new short[]{80}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData FIDE_DAI_CA_3 = new BossData(
            "Fide đại ca 3", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            60000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{189, 190, 191}, // outfit
            new short[]{80}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------
    public static final BossData ANDROID_13 = new BossData(
            "Android 13", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{5_000_000_000l}}, // hp
            new short[]{252, 253, 254}, // outfit
            new short[]{84}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    public static final BossData ANDROID_14 = new BossData(
            "Android 14", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{3_000_000_000l}}, // hp
            new short[]{246, 247, 248}, // outfit
            new short[]{84}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);

    public static final BossData ANDROID_15 = new BossData(
            "Android 15", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{2_000_000_000l}}, // hp
            new short[]{261, 262, 263}, // outfit
            new short[]{84}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData ANDROID_19 = new BossData(
            "Android 19", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{250_000_000}}, // hp
            new short[]{249, 250, 251}, // outfit
            new short[]{93, 94, 96}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData ANDROID_20 = new BossData(
            "Dr.Kôrê", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{300_000_000}}, // hp
            new short[]{255, 256, 257}, // outfit
            new short[]{93, 94, 96}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT,
            true);
    public static final BossData PIC = new BossData(
            "Pic", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            130000, // dame
            new long[][]{{200_000_000}}, // hp
            new short[]{237, 238, 239}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY, true);
    public static final BossData POC = new BossData(
            "Poc", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            125000, // dame
            new long[][]{{250_000_000}}, // hp
            new short[]{240, 241, 242}, // outfit
            new short[]{82, 83, 79}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData KINGKONG = new BossData(
            "King Kong", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new long[][]{{300_000_000}}, // hp
            new short[]{243, 244, 245}, // outfit
            new short[]{97, 98, 99}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT, true);

    // --------------------------------------------------------------------------Boss
    // berus
    public static final BossData WHIS = new BossData(
            "Thiên Sứ Whis", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            25000, // dame
            new long[][]{{500}}, // hp
            new short[]{838, 839, 840}, // outfit
            new short[]{200}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData BILL = new BossData(
            "Thần Hủy Diệt", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            30000, // dame
            new long[][]{{2000000000}}, // hp
            new short[]{508, 509, 510}, // outfit
            new short[]{200}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    // --------------------------------------------------------------------------Boss
    // CHILLED
    public static final BossData CHILL = new BossData(
            "Chilled", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            250000, // dame
            new long[][]{{500000000}}, // hp
            new short[]{1024, 1025, 1026}, // outfit
            new short[]{163}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _10_PHUT);

    public static final BossData CHILL2 = new BossData(
            "Chilled 2", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new long[][]{{500000000}}, // hp
            new short[]{1021, 1022, 1023}, // outfit
            new short[]{163}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _0_GIAY);

    public static final BossData BULMA = new BossData(
            "Thỏ Hồng Bunma", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new long[][]{{350000000}}, // hp
            new short[]{1095, 1096, 1097}, // outfit
            new short[]{7, 43}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    public static final BossData POCTHO = new BossData(
            "POC Thỏ Đen", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new long[][]{{350000000}}, // hp
            new short[]{1101, 1102, 1103}, // outfit
            new short[]{14, 44}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    public static final BossData CHICHITHO = new BossData(
            "ChiChi Thỏ Đỏ", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            300000, // dame
            new long[][]{{350000000}}, // hp
            new short[]{1098, 1099, 1100}, // outfit
            new short[]{0, 42}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200},
                    {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200},
                    {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _5_PHUT);

    //
    // public static final BossData BROLYDEN = new BossData(
    // "S.Broly Black", //name
    // ConstPlayer.XAYDA, //gender
    // Boss.DAME_NORMAL, //type dame
    // Boss.HP_NORMAL, //type hp
    // 300000, //dame
    // new int[][]{{1000000000}}, //hp
    // new short[]{1080, 1081, 1082}, //outfit
    // new short[]{14}, //map join
    // new int[][]{ //skill
    // {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
    // {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
    // {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4,
    // 1500}, {Skill.KAMEJOKO, 2, 1000},
    // {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
    // {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
    // {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
    // {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
    // {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    // },
    // _5_PHUT
    // );
    //
    // public static final BossData BROLYXANH = new BossData(
    // "S.Broly SNamếc", //name
    // ConstPlayer.XAYDA, //gender
    // Boss.DAME_NORMAL, //type dame
    // Boss.HP_NORMAL, //type hp
    // 300000, //dame
    // new int[][]{{1000000000}}, //hp
    // new short[]{1086, 1087, 1088}, //outfit
    // new short[]{14}, //map join
    // new int[][]{ //skill
    // {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
    // {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
    // {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4,
    // 1500}, {Skill.KAMEJOKO, 2, 1000},
    // {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
    // {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
    // {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
    // {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
    // {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    // },
    // _5_PHUT
    // );
    //
    // public static final BossData BROLYVANG = new BossData(
    // "S.Broly SSJ", //name
    // ConstPlayer.XAYDA, //gender
    // Boss.DAME_NORMAL, //type dame
    // Boss.HP_NORMAL, //type hp
    // 300000, //dame
    // new int[][]{{1000000000}}, //hp
    // new short[]{1083, 1084, 1085}, //outfit
    // new short[]{14}, //map join
    // new int[][]{ //skill
    // {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
    // {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
    // {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4,
    // 1500}, {Skill.KAMEJOKO, 2, 1000},
    // {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
    // {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
    // {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
    // {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
    // {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    // },
    // _5_PHUT
    // );
    //
    public static final BossData SUPER_ZAMAX = new BossData(
            "Fusion Zamax", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{2_000_000_000L}}, // hp
            new short[]{1434, 1436, 1437}, // outfit
            new short[]{92, 93, 94, 96, 97, 98, 99,100}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500}, {Skill.KHIEN_NANG_LUONG, 7, 200000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 500000}},
            _0_GIAY);

    public static final BossData ZAMASU = new BossData(
            "Zamasu", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{1_500_000_000L}}, // hp
            new short[]{1435, 1436, 1437}, // outfit
            new short[]{92, 93, 94, 96, 97, 98, 99,100}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500}, {Skill.KHIEN_NANG_LUONG, 7, 200000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 500000}},
            _0_GIAY);

    public static final BossData BLACKGOKU = new BossData(
            "Black Goku", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{1_000_000_000L}}, // hp
            new short[]{550, 551, 552}, // outfit
            new short[]{92, 93, 94, 96, 97, 98, 99,100}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500}, {Skill.KHIEN_NANG_LUONG, 7, 200000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 500000}},
            _0_GIAY);

    public static final BossData SUPERBLACKGOKU = new BossData(
            "SBlack Goku", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{1_500_000_000L}}, // hp
            new short[]{553, 551, 552}, // outfit
            new short[]{92, 93, 94, 96, 97, 98, 99}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500}, {Skill.KHIEN_NANG_LUONG, 7, 200000},
                    {Skill.TAI_TAO_NANG_LUONG, 7, 500000}},
            _0_GIAY);

    public static final BossData LANCON = new BossData(
            "Lân con", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20000, // dame
            new long[][]{{500}}, // hp
            new short[]{763, 764, 765}, // outfit
            new short[]{224}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _0_GIAY);

    public static final BossData HOA_HONG = BossData.builder()
            .name("Hoa Hồng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new long[][]{{100}})
            .outfit(new short[]{706, 707, 708})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData SANTA_CLAUS = BossData.builder()
            .name("Ông già Nôen")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new long[][]{{500000}})
            .outfit(new short[]{657, 658, 659})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData QILIN = BossData.builder()
            .name("Lân con")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new long[][]{{5000000}})
            .outfit(new short[]{763, 764, 765})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData MABU_MAP = BossData.builder()
            .name("Mabư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(750000)
            .hp(new long[][]{{2000000000}})
            .outfit(new short[]{297, 298, 299})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData MABU_MAP2 = BossData.builder()
            .name("Bư Mập")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(750000)
            .hp(new long[][]{{100000000}})
            .outfit(new short[]{297, 298, 299})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData SUPER_BU = BossData.builder()
            .name("Super Bư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1200000)
            .hp(new long[][]{{200000000}})
            .outfit(new short[]{427, 428, 429})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData KID_BU = BossData.builder()
            .name("Kid Bư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1200000)
            .hp(new long[][]{{300000000}})
            .outfit(new short[]{439, 440, 441})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData BU_TENK = BossData.builder()
            .name("Bư Tênk")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1200000)
            .hp(new long[][]{{400000000}})
            .outfit(new short[]{439, 440, 441})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData BU_HAN = BossData.builder()
            .name("Bư Han")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1200000)
            .hp(new long[][]{{800000000}})
            .outfit(new short[]{427, 428, 429})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200},
                    {Skill.QUA_CAU_KENH_KHI, 7, 1200},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData DRABULA_TANG1 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new long[][]{{250000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{114})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData DRABULA_TANG5 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData DRABULA_TANG6 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new long[][]{{1000000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{120})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData BUIBUI_TANG2 = BossData.builder()
            .name("BuiBui")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{451, 452, 453})
            .mapJoin(new short[]{115})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData BUIBUI_TANG3 = BossData.builder()
            .name("BuiBui")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{451, 452, 453})
            .mapJoin(new short[]{117})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData CALICH_TANG5 = BossData.builder()
            .name("Ca Đíc")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{103, 16, 17})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData GOKU_TANG5 = BossData.builder()
            .name("Gôku")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{101, 1, 2})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData YACON_TANG4 = BossData.builder()
            .name("Yacôn")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{415, 416, 417})
            .mapJoin(new short[]{118})
            .skillTemp(new int[][]{
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},
                    {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000},
                    {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                    {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            })
            .secondsRest(_5_GIAY)
            .build();

    public static final BossData XEN_MAX = BossData.builder()
            .name("Xên Max")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new long[][]{{2000000000}})
            .outfit(new short[]{1296, 1297, 1298})
            .mapJoin(new short[]{99})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData SOI_HEC_QUYN = BossData.builder()
            .name("Sói Hẹc Quyn")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(40000)
            .hp(new long[][]{{5000000}})
            .outfit(new short[]{394, 395, 396})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData O_DO = BossData.builder()
            .name("Ở Dơ")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(50000)
            .hp(new long[][]{{7000000}})
            .outfit(new short[]{400, 401, 402})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData XINBATO = BossData.builder()
            .name("Xinbatô")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(60000)
            .hp(new long[][]{{15000000}})
            .outfit(new short[]{359, 360, 361})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData CHA_PA = BossData.builder()
            .name("Cha pa")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(65000)
            .hp(new long[][]{{25000000}})
            .outfit(new short[]{362, 363, 364})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData PON_PUT = BossData.builder()
            .name("Pon put")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(70000)
            .hp(new long[][]{{50000000}})
            .outfit(new short[]{365, 366, 367})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData CHAN_XU = BossData.builder()
            .name("Chan xư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(75000)
            .hp(new long[][]{{75000000}})
            .outfit(new short[]{371, 372, 373})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData TAU_PAY_PAY = BossData.builder()
            .name("Tàu Pảy Pảy")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(80000)
            .hp(new long[][]{{100000000}})
            .outfit(new short[]{92, 93, 94})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData YAMCHA = BossData.builder()
            .name("Yamcha")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(90000)
            .hp(new long[][]{{125000000}})
            .outfit(new short[]{374, 375, 376})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData JACKY_CHUN = BossData.builder()
            .name("Jacky Chun")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(100000)
            .hp(new long[][]{{150000000}})
            .outfit(new short[]{356, 357, 358})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData THIEN_XIN_HANG = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(150000)
            .hp(new long[][]{{175000000}})
            .outfit(new short[]{368, 369, 370})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.THAI_DUONG_HA_SAN, 1, 15000}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData THIEN_XIN_HANG_CLONE = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(75000)
            .hp(new long[][]{{200000000}})
            .outfit(new short[]{368, 369, 370})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.THAI_DUONG_HA_SAN, 1, 15000}
            })
            .secondsRest(_0_GIAY)
            .build();
    public static final BossData LIU_LIU = BossData.builder()
            .name("Liu Liu")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new long[][]{{250000000}})
            .outfit(new short[]{397, 398, 399})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData NGO_KHONG = BossData.builder()
            .name("Tôn Ngộ Không")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{462, 463, 464})
            .mapJoin(new short[]{124})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData BAT_GIOI = BossData.builder()
            .name("Chư Bát Giới")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new long[][]{{500000000}})
            .outfit(new short[]{465, 466, 467})
            .mapJoin(new short[]{124})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData FIDEGOLD = BossData.builder()
            .name("Fide Vàng  %1")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500)
            .hp(new long[][]{{5000}})
            .outfit(new short[]{502, 503, 504})
            .mapJoin(new short[]{5, 13, 20})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_20_PHUT)
            .build();

    public static final BossData CUMBER = BossData.builder()
            .name("Cumber")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(15000)
            .hp(new long[][]{{1_500_000_000L}})
            .outfit(new short[]{1281, 1282, 1283})
            .mapJoin(new short[]{155})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData CUMBER2 = BossData.builder()
            .name("Super Cumber")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(2000)
            .hp(new long[][]{{2_000_000_000L}})
            .outfit(new short[]{1284, 1285, 1286})
            .mapJoin(new short[]{155})
            .skillTemp(new int[][]{
                    {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300},
                    {Skill.DRAGON, 7, 700},
                    {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200},
                    {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                    {Skill.GALICK, 1, 100}
            })
            .secondsRest(_0_GIAY)
            .build();

    public static final BossData SATAN_KING = new BossData(
            "Vua Quỷ Satan", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20_000, // dame
            new long[][]{{1_900_000_000}}, // hp
            new short[]{344, 345, 346}, // outfit
            new short[]{109}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _5_PHUT);

    public static final BossData DRACULA_HUT_MAU = new BossData(
            "Dracula Hút Máu", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            20_000, // dame
            new long[][]{{1_500_000_000}}, // hp
            new short[]{353, 354, 355}, // outfit
            new short[]{109}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _15_PHUT);

    public static final BossData HIDRABULA_BONG_TOI = new BossData(
            "Hidrabula Ánh Sáng - Level %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{1207, 1208, 1209}, // outfit
            new short[]{180, 181, 182, 183}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 7, 1200},
                    {Skill.ANTOMIC, 7, 1500}, {Skill.ANTOMIC, 7, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 7, 1200},
                    {Skill.MASENKO, 7, 1500}, {Skill.MASENKO, 7, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData BUIBUI_BONG_TOI = new BossData(
            "BuiBui Ánh Sáng - Level %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{451, 452, 453}, // outfit
            new short[]{180, 181, 182, 183}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 7, 1200},
                    {Skill.ANTOMIC, 7, 1500}, {Skill.ANTOMIC, 7, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 7, 1200},
                    {Skill.MASENKO, 7, 1500}, {Skill.MASENKO, 7, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData YACON_BONG_TOI = new BossData(
            "YaCôn Ánh Sáng - Level %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{415, 416, 417}, // outfit
            new short[]{180, 181, 182, 183}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 7, 1200},
                    {Skill.ANTOMIC, 7, 1500}, {Skill.ANTOMIC, 7, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 7, 1200},
                    {Skill.MASENKO, 7, 1500}, {Skill.MASENKO, 7, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData BUBONG_TOI = new BossData(
            "Mabu Ánh Sáng - Level %1", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            135000, // dame
            new long[][]{{2_000_000_000}}, // hp
            new short[]{297, 298, 299}, // outfit
            new short[]{180, 181, 182, 183}, // map join
            new int[][]{ // skill
                    {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000},
                    {Skill.DRAGON, 7, 7000},
                    {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 7, 1200},
                    {Skill.ANTOMIC, 7, 1500}, {Skill.ANTOMIC, 7, 1700},
                    {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 7, 1200},
                    {Skill.MASENKO, 7, 1500}, {Skill.MASENKO, 7, 1700},
                    {Skill.GALICK, 1, 1000}
            },
            _15_PHUT);

    public static final BossData GRANOLA = new BossData(
            "Granola", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{2_000_000_000L}}, // hp
            new short[]{1251, 1252, 1253}, // outfit
            new short[]{178, 177}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _0_GIAY);

    public static final BossData ELEC = new BossData(
            "Elec", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{1254, 1255, 1256}, // outfit
            new short[]{178, 177}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _0_GIAY);

    public static final BossData OLI = new BossData(
            "Oli", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{1263, 1264, 1265}, // outfit
            new short[]{178, 177}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _0_GIAY, true);

    public static final BossData MACKIE = new BossData(
            "Mackie", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{1260, 1261, 1262}, // outfit
            new short[]{178, 177}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _0_GIAY, true);

    public static final BossData GAS = new BossData(
            "Gas", // name
            ConstPlayer.XAYDA, // gender
            Boss.DAME_NORMAL, // type dame
            Boss.HP_NORMAL, // type hp
            15000, // dame
            new long[][]{{500_000_000}}, // hp
            new short[]{1257, 1258, 1259}, // outfit
            new short[]{178, 177}, // map join
            new int[][]{ // skill
                    {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650},
                    {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                    {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800},
                    {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                    {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700},
                    {Skill.ANTOMIC, 7, 2000},
                    {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300},
                    {Skill.MASENKO, 6, 1500},},
            _0_GIAY, true);

}
