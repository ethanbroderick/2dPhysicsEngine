package physics;

import maths.Vec2;

public class RigidBody {

    private Vec2 position;
    private Vec2 velocity;
    private Vec2 force;
    private float mass;
    private int radius;
    private float restitution = 1;

    // Flag to tell if body is active in Object Pool
    private boolean active = false;

    public RigidBody() {
        this.position = new Vec2(0, 0);
        this.velocity = new Vec2(0, 0);
        this.force = new Vec2(0, 0);
        this.mass = 0;
        this.radius = 0;
    }

    public RigidBody(float x, float y, float mass, int radius) {
        this.position = new Vec2(x, y);
        this.velocity = new Vec2(0, 0);
        this.force = new Vec2(0, 0);
        this.radius = radius;
        this.mass = mass * 1000;
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

    public void setVelocityIComp(float i) {
        this.velocity.i = i;
    }

    public void setVelocityJComp(float j) {
        this.velocity.j = j;
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
        this.mass = mass * 1000;
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

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    // Methods
    public void collision(RigidBody other) {
        Vec2 normal = other.getPosition().sub(this.position);
        float distance = normal.getMagnitude();

        if (distance < (other.getRadius() + this.radius)) {
            // Collision Resolution
            float minDist = this.radius + other.getRadius();

            // Prevents div by 0 if two particles perfectly overlap
            if (distance == 0) return;

            // Separates overlapping particles
            float overlap = minDist - distance;

            Vec2 unitNormal = normal.divideByScalar(distance);

            this.position = this.position.sub(unitNormal.multiplyByScalar(overlap / 2));
            other.setPosition(other.getPosition().add(unitNormal.multiplyByScalar(overlap / 2)));

            // Calculation of new velocities
            Vec2 unitTangent = new Vec2(-unitNormal.j, unitNormal.i);

            float normalVectorA = unitNormal.dotProduct(this.velocity);
            float tangentVectorA = unitTangent.dotProduct(this.velocity);

            float normalVectorB = unitNormal.dotProduct(other.getVelocity());
            float tangentVectorB = unitTangent.dotProduct(other.getVelocity());

            float newNormalVectorA = ((normalVectorA * (this.mass - other.getMass())) + ((1 + restitution) * other.getMass() * normalVectorB) ) / (this.mass + other.getMass());
            float newNormalVectorB = ((normalVectorB * (other.getMass() - this.mass)) + ((1 + restitution) * this.mass * normalVectorA)) / (this.mass + other.getMass());

            Vec2 newNormalA = unitNormal.multiplyByScalar(newNormalVectorA);
            Vec2 newTangentA = unitTangent.multiplyByScalar(tangentVectorA);

            Vec2 newNormalB = unitNormal.multiplyByScalar(newNormalVectorB);
            Vec2 newTangentB = unitTangent.multiplyByScalar(tangentVectorB);

            this.velocity = newNormalA.add(newTangentA);
            other.setVelocity(newNormalB.add(newTangentB));
        }
    }

}
