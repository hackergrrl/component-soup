package net.sww.ecs;

public abstract class Component {

    protected Entity entity;

    void installed(Entity entity) {
        this.entity = entity;
        init();
    }
    void uninstalled(Entity entity) {
        this.entity = null;
    }
    public void update(float dt) {}
    public void onEvent(Event msg) {}
    public void init() {}

    public final <T extends Component> T get(Class<T> type) {
        return entity.get(type);
    }

    public final World getWorld() {
        return entity.world;
    }
}
