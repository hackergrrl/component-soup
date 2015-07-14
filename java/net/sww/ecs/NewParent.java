package net.sww.ecs;

public class NewParent extends Event {
    public Entity oldParent;
    public Entity newParent;

    public NewParent(Entity oldParent, Entity newParent) {
        this.oldParent = oldParent;
        this.newParent = newParent;
    }
}
