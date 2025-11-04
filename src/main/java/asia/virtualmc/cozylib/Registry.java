package asia.virtualmc.cozylib;

import asia.virtualmc.cozylib.services.files.YamlFileReader;

public class Registry {


    private void load() {
        YamlFileReader.YamlFile reader = YamlFileReader.get(CozyLib.getInstance(), "config.yml");

    }
}
