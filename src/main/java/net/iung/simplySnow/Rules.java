package net.iung.simplySnow;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.minecraft.world.level.GameRules;

import java.security.PublicKey;

public class Rules {
    public static void register(){}
    //SnowLayerBlockMixin
    public static boolean SNOW_ON_ICE=true;
    public static final GameRules.Key<GameRules.BooleanValue> SIMPLYSNOW_SNOW_ON_ICE=
            GameRuleRegistry.register("simplySnow_snowOnIce", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    public static boolean SNOW_ON_DIRT_PATH=true;
    public static final GameRules.Key<GameRules.BooleanValue> SIMPLYSNOW_SNOW_ON_DIRT_PATH=
            GameRuleRegistry.register("simplySnow_snowOnDirtPath", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(true));

    public static double SNOW_MELT_CHANCE_PERCENT=8;
    public static final GameRules.Key<DoubleRule> SIMPLYSNOW_SNOW_MELT_CHANCE_PERCENT=
            GameRuleRegistry.register("simplySnow_snowMeltChancePercent", GameRules.Category.MISC, GameRuleFactory.createDoubleRule(8.0,0.0,100.0));

    public static int MIN_MELT_LIGHTLEVEL=3;
    public static final GameRules.Key<GameRules.IntegerValue> SIMPLYSNOW_MIN_MELT_LIGHTLEVEL=
            GameRuleRegistry.register("simplySnow_minMeltLightLevel", GameRules.Category.MISC, GameRuleFactory.createIntRule(3,0,16));

    public static double SNOW_MELT_CHANCE_WARM_BIOME_PERCENT=10;
    public static final GameRules.Key<DoubleRule> SIMPLYSNOW_SNOW_MELT_CHANCE_WARM_BIOME_PERCENT=
            GameRuleRegistry.register("simplySnow_snowMeltChanceWarmBiomePercent", GameRules.Category.MISC, GameRuleFactory.createDoubleRule(10.0,0.0,100.0));

    public static double SNOW_MELT_CHANCE_INDOOR_PERCENT=40;
    public static final GameRules.Key<DoubleRule> SIMPLYSNOW_SNOW_MELT_CHANCE_INDOOR_PERCENT=
            GameRuleRegistry.register("simplySnow_snowMeltChanceIndoorPercent", GameRules.Category.MISC, GameRuleFactory.createDoubleRule(40.0,0.0,100.0));

    public static int MIN_MELT_LIGHTLEVEL_INDOOR=1;
    public static final GameRules.Key<GameRules.IntegerValue> SIMPLYSNOW_MIN_MELT_LIGHTLEVEL_INDOOR=
            GameRuleRegistry.register("simplySnow_minMeltLightLevelIndoor", GameRules.Category.MISC, GameRuleFactory.createIntRule(1,0,16));

    public static int SPREAD_DOWN=10;
    public static final GameRules.Key<GameRules.IntegerValue> SIMPLYSNOW_SPREAD_DOWN=
            GameRuleRegistry.register("simplySnow_spreadDown", GameRules.Category.MISC, GameRuleFactory.createIntRule(10,1,100));

    //ServerLevelMixin
    public static boolean SNOW_IN_EVERY_BIOME=false;
    public static final GameRules.Key<GameRules.BooleanValue> SIMPLYSNOW_SNOW_IN_EVERY_BIOME=
            GameRuleRegistry.register("simplySnow_snowInEveryBiome", GameRules.Category.MISC, GameRuleFactory.createBooleanRule(false));

    public static double SNOW_CHANCE_PERCENT=5;
    public static final GameRules.Key<DoubleRule> SIMPLYSNOW_SNOW_CHANCE_PERCENT=
            GameRuleRegistry.register("simplySnow_snowChancePercent", GameRules.Category.MISC, GameRuleFactory.createDoubleRule(25.0,0.0,100.0));

    public static int MAX_SNOW_LIGHTLEVEL=13;
    public static final GameRules.Key<GameRules.IntegerValue> SIMPLYSNOW_MAX_SNOW_LIGHTLEVEL=
            GameRuleRegistry.register("simplySnow_maxSnowLightLevel", GameRules.Category.MISC, GameRuleFactory.createIntRule(4,0,16));


}
