package io.github.fisher2911.jarrunner;

import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class JarRunner extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            this.saveDefaultConfig();
            if (this.getConfig().getBoolean("ran", false)) return;
            final String[] args = this.formatArgs(this.getConfig().getString("args", ""));
            final String jarName = this.getConfig().getString("jar-name", "");
            final Path path = Path.of(jarName);
            JarClassLoader loader = new JarClassLoader(path.toUri().toURL());
            String main = loader.getMainClassName();
            this.getConfig().set("ran", true);
            this.saveConfig();
            loader.invokeClass(main, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] formatArgs(String currentArgs) {
        boolean hasQuote = false;
        final List<String> args = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : currentArgs.toCharArray()) {
            if (c == ' ') {
                if (hasQuote) {
                    sb.append(c);
                } else {
                    args.add(sb.toString());
                    sb = new StringBuilder();
                }
                continue;
            }
            if (c == '\"') {
                hasQuote = !hasQuote;
                continue;
            }
            sb.append(c);
        }
        args.add(sb.toString());
        return args.toArray(new String[0]);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
