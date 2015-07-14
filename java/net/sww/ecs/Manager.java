package net.sww.ecs;

public class Manager {

    World world;

    public Manager() {
    }

    public void onEntityAdded(Entity entity) {}
    public void onEntityRemoved(Entity entity) {}
    public void onComponentInstalled(Entity entity, Component component) {}
    public void onComponentUninstalled(Entity entity, Component component) {}
}
