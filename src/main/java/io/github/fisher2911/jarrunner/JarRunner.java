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
            final String[] args = this.formatArgs(this.getConfig().getString("args", ""));
            this.getLogger().info("Running jar with args: " + args);
            final String jarName = this.getConfig().getString("jar-name", "");
//            this.getLogger().info("Running jar with name: " + jarName);
//            this.getLogger().info("First path: " + this.getDataFolder().toPath());
//            this.getLogger().info("First path: " + this.getDataFolder().toPath().getParent());
//            this.getLogger().info("First path: " + this.getDataFolder().toPath().getParent().getParent());
            final Path path = Path.of(jarName);
            this.getLogger().info("Running jar with path: " + path);
            JarClassLoader loader = new JarClassLoader(path.toUri().toURL());
            this.getLogger().info("Created jar loader");
            String main = loader.getMainClassName();
            this.getLogger().info("Found main class: " + main);
            loader.invokeClass(main, args);
            this.getLogger().info("Invoked jar");
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
