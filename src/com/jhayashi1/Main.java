package com.jhayashi1;

public class Main extends JavaPlugin implements Listener {

    private static Logger logger;

    @Override
    public void onEnable() {

        logger = getLogger();

        new cdCmd(this);

        // this.getServer().getPluginManager().registerEvents(new MiscListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new ProjectileListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new JoinQuitListener((this)), this);
        // this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        // this.getServer().getPluginManager().registerEvents(new BlockPlaceBreakListener(this), this);

        // new BlackHoleSkill(this, "Black Hole");
        // new ExplosionWandSkill(this, "Explosion wand");
        // new SmokeBombSkill(this, "Smoke Bomb");
        // new SnipeSkill(this, "Snipe");
        // new ShotgunSkill(this, "Shotgun");

        log("Plugin fully enabled");
    }

    @Override
    public void onDisable() {
        log("Plugin fully disabled");
    }

    public static Logger getPluginLogger() {
        return logger;
    }

    // public ConfigManager getConfigManager() {
    //     return configManager;
    // }

    // public ProfileManager getProfileManager() {
    //     return profileManager;
    // }

    // public BoardManager getBoardManager() {
    //     return boardManager;
    // }

    // public GameManager getGameManager() {
    //     return gameManager;
    // }

    // public MoneyManager getMoneyManager() {
    //     return moneyManager;
    // }
}
