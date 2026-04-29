package net.cozyvanilla.cozylib.integrations.permission;

import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.matcher.NodeMatcher;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class LuckPermsGet {
    private final net.luckperms.api.LuckPerms api;

    public LuckPermsGet(net.luckperms.api.LuckPerms api) { this.api = api; }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public CompletableFuture<Set<UUID>> getGroupMembers(String groupName) {
        UserManager userManager = api.getUserManager();
        return userManager.searchAll(NodeMatcher.type(NodeType.INHERITANCE))
                .thenApply(resultMap -> resultMap.entrySet().stream()
                        .filter(entry -> entry.getValue().stream()
                                .anyMatch(node -> node.getGroupName().equalsIgnoreCase(groupName)))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toSet())
                );
    }
}