package physics;

import maths.Vec2;

public class RigidBody {

    private Vec2 position;
    private Vec2 velocity;
    private Vec2 force;
    private float mass;

    // Flag to tell if body is active in Object Pool
    private boolean active = false;

    public RigidBody() {
        this.position = new Vec2(0, 0);
        this.velocity = new Vec2(0, 0);
        this.force = new Vec2(0, 0);
        this.mass = 0;
    }

    public RigidBody(float x, float y, float mass) {
        this.position = new Vec2(x, y);
        this.velocity = new Vec2(0, 0);
        this.force = new Vec2(0, 0);
        this.mass = mass;
    }

    // Getters and Setters

    public Vec2 getPosition() {
        return position;
    }

    public void setPosition(Vec2 position) {
        this.position = position;
    }

    public Vec2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec2 velocity) {
        this.velocity = velocity;
    }

    public Vec2 getForce() {
        return force;
    }

    public void setForce(Vec2 force) {
        this.force = force;
    }

    public void clearForce() {
        this.force.i = 0;
        this.force.j = 0;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public void setActive() {
        this.active = true;
    }

    public void setInactive() {
        this.active = false;
    }

    public boolean getActiveStatus() {
        return this.active;
    }
}
