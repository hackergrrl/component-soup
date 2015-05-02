package net.sww.ecs;

public abstract class Component {

    protected Entity owner;

    void installed(Entity entity) {
        this.owner = entity;
    }
    void uninstalled(Entity entity) {
        this.owner = null;
    }
    public void update(float dt) {}
    public void onEvent(Event msg) {}

    public <T extends Component> T get(Class<T> type) {
        return owner.get(type);
    }
}
