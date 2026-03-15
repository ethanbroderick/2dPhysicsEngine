package maths;

import java.math.*;

public class Vec2 {
    public float i;
    public float j;

    public Vec2() {
        this.i = 0.0f;
        this.j = 0.0f;
    }

    public Vec2(float iComp, float jComp) {
        this.i = iComp;
        this.j = jComp;
    }

    public float dotProduct(Vec2 other) {
        return ((this.i * other.i) + (this.j * other.j));
    }

    // Takes two vectors and multiplies them together
    public static Vec2 multiply(Vec2 firstVector, Vec2 secondVector) {
        return new Vec2(firstVector.i * secondVector.i, firstVector.j * secondVector.j);
    }

    public Vec2 multiplyByScalar(float scalar) {
        return (new Vec2(this.i * scalar, this.j * scalar));
    }

    // Takes two vectors and divides them together
    public static Vec2 divide(Vec2 firstVector, Vec2 secondVector) {
        return (new Vec2(firstVector.i / secondVector.i, firstVector.j / secondVector.j));
    }

    public Vec2 divideByScalar(float divisor) {
        return (new Vec2(this.i / divisor, this.j / divisor));
    }

    // Takes two vectors and adds them together
    public static Vec2 add(Vec2 firstVector, Vec2 secondVector) {
        return (new Vec2(firstVector.i + secondVector.i, firstVector.j + secondVector.j));
    }

    public Vec2 add(Vec2 other) {
        return (new Vec2(this.i + other.i, this.j + other.j));
    }

    // Takes two vectors and subs them
    public static Vec2 sub(Vec2 firstVector, Vec2 secondVector) {
        return (new Vec2(firstVector.i - secondVector.i, firstVector.j - secondVector.j));
    }

    public Vec2 sub(Vec2 secondVector) {
        return (new Vec2(this.i - secondVector.i, this.j - secondVector.j));
    }

    public Vec2 copy() {
        return (new Vec2(this.i, this.j));
    }

    public float getMagnitude() {
        return (float) Math.sqrt((this.i * this.i) + (this.j * this.j));
    }
}
