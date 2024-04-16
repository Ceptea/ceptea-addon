package com.ceptea.addon;

import com.ceptea.addon.modules.Fly;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;

import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Ceptea");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Ceptea Addon");

        // Modules
        Modules.get().add(new Fly());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.ceptea.addon";
    }
}
