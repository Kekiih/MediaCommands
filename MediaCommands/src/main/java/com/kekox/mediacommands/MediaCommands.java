package com.kekox.mediacommands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public final class MediaCommands extends Plugin {

    private Map<CommandSender, Long> cooldowns = new HashMap<>();

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new NewVideoCommand());
        getProxy().getPluginManager().registerCommand(this, new StreamCommand());
    }

    @Override
    public void onDisable() {
    }

    private class NewVideoCommand extends Command {

        public NewVideoCommand() {
            super("newvideo", "keki.newvideo");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!sender.hasPermission("keki.newvideo")) {
                sender.sendMessage(ChatColor.RED + "No tienes permiso para usar este comando.");
                return;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Uso correcto: /newvideo <enlace>");
                return;
            }

            if (hasCooldown(sender)) {
                sender.sendMessage(ChatColor.RED + "Debes esperar " + getRemainingCooldown(sender) + " segundos antes de usar este comando nuevamente.");
                return;
            }

            if (!isValidLink(args[0])) {
                sender.sendMessage(ChatColor.RED + "El enlace proporcionado no es válido.");
                return;
            }

            setCooldown(sender);

            String message = "&7&m--------------------------------------------\n"
                    + ChatColor.RED + ChatColor.BOLD + "Newvideo: " + ChatColor.RESET + ChatColor.AQUA
                    + sender.getName() + " " + ChatColor.GRAY + "hizo un nuevo video: " + ChatColor.RED + args[0] + "\n"
                    + "&7&m--------------------------------------------";

            ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', message));
            componentBuilder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, args[0]));
            getProxy().broadcast(componentBuilder.create());
        }
    }

    private class StreamCommand extends Command {

        public StreamCommand() {
            super("stream", "keki.stream");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!sender.hasPermission("keki.stream")) {
                sender.sendMessage(ChatColor.RED + "No tienes permiso para usar este comando.");
                return;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Uso correcto: /stream <enlace>");
                return;
            }

            if (hasCooldown(sender)) {
                sender.sendMessage(ChatColor.RED + "Debes esperar " + getRemainingCooldown(sender) + " segundos antes de usar este comando nuevamente.");
                return;
            }

            if (!isValidLink(args[0])) {
                sender.sendMessage(ChatColor.RED + "El enlace proporcionado no es válido.");
                return;
            }

            setCooldown(sender);

            String message = "&7&m--------------------------------------------\n"
                    + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Stream: " + ChatColor.RESET + ChatColor.AQUA
                    + sender.getName() + " " + ChatColor.GRAY + "está haciendo un stream: " + ChatColor.RED + args[0] + "\n"
                    + "&7&m--------------------------------------------";

            ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', message));
            componentBuilder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, args[0]));
            getProxy().broadcast(componentBuilder.create());
        }
    }

    private boolean isValidLink(String link) {
        String regex = "^https://.*$";
        return link.matches(regex);
    }

    private boolean hasCooldown(CommandSender sender) {
        return cooldowns.containsKey(sender) && System.currentTimeMillis() - cooldowns.get(sender) < 60000;
    }

    private void setCooldown(CommandSender sender) {
        cooldowns.put(sender, System.currentTimeMillis());
    }

    private long getRemainingCooldown(CommandSender sender) {
        return (cooldowns.get(sender) + 60000 - System.currentTimeMillis()) / 1000;
    }
}
