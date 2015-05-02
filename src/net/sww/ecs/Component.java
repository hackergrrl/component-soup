package net.sww.ecs;

public abstract class Component {

    protected Entity owner;

    void installed(Entity entity) {
        this.owner = entity;
        init();
    }
    void uninstalled(Entity entity) {
        this.owner = null;
    }
    public void update(float dt) {}
    public void onEvent(Event msg) {}
    public void init() {}

    public <T extends Component> T get(Class<T> type) {
        return owner.get(type);
    }

    public World getWorld() {
        return owner.world;
    }
}
