package net.sww.net.sww.ecs;

public abstract class Component {
    public void installed() {}
    public void update(float dt) {}
    public void onEvent(Event msg) {}
}
